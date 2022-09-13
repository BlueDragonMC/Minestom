package net.minestom.server.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.handshake.HandshakePacket;
import net.minestom.server.network.packet.server.login.LoginDisconnectPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.network.player.PlayerSocketConnection;

import java.net.SocketAddress;
import java.util.UUID;

public class HandshakeListener {
    /**
     * Text sent if a player tries to connect with an invalid version of the client
     */
    private static final Component INVALID_VERSION_TEXT = Component.text("Invalid Version, please use " + MinecraftServer.VERSION_NAME, NamedTextColor.RED);
    private static final Component INVALID_BUNGEE_FORWARDING = Component.text("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!", NamedTextColor.RED);

    public static void listener(HandshakePacket packet, PlayerConnection connection) {
        String address = packet.serverAddress();
        // Bungee support (IP forwarding)
        if (BungeeCordProxy.isEnabled() && connection instanceof PlayerSocketConnection socketConnection && packet.nextState() == 2) {
            if (address != null) {
                final String[] split = address.split("\00");

                if (split.length == 3 || split.length == 4) {
                    address = split[0];

                    final SocketAddress socketAddress = new java.net.InetSocketAddress(split[1],
                            ((java.net.InetSocketAddress) connection.getRemoteAddress()).getPort());
                    socketConnection.setRemoteAddress(socketAddress);

                    UUID playerUuid = UUID.fromString(
                            split[2]
                                    .replaceFirst(
                                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                                    )
                    );
                    PlayerSkin playerSkin = null;

                    if (split.length == 4) {
                        playerSkin = BungeeCordProxy.readSkin(split[3]);
                    }

                    socketConnection.UNSAFE_setBungeeUuid(playerUuid);
                    socketConnection.UNSAFE_setBungeeSkin(playerSkin);
                } else {
                    socketConnection.sendPacket(new LoginDisconnectPacket(INVALID_BUNGEE_FORWARDING));
                    socketConnection.disconnect();
                    return;
                }
            } else {
                // Happen when a client ping the server, ignore
                return;
            }
        }

        if (connection instanceof PlayerSocketConnection) {
            // Give to the connection the server info that the client used
            ((PlayerSocketConnection) connection).refreshServerInformation(address, packet.serverPort(), packet.protocolVersion());
        }

        switch (packet.nextState()) {
            case 1:
                connection.setConnectionState(ConnectionState.STATUS);
                break;
            case 2:
                if (packet.protocolVersion() == MinecraftServer.PROTOCOL_VERSION) {
                    connection.setConnectionState(ConnectionState.LOGIN);
                } else {
                    // Incorrect client version
                    connection.sendPacket(new LoginDisconnectPacket(INVALID_VERSION_TEXT));
                    connection.disconnect();
                }
                break;
            default:
                // Unexpected error
                break;
        }
    }
}
