package net.minestom.server.listener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.server.ClientPingServerEvent;
import net.minestom.server.network.packet.client.status.PingPacket;
import net.minestom.server.network.packet.server.status.PongPacket;
import net.minestom.server.network.player.PlayerConnection;

public class PingListener {
    public static void listener(PingPacket packet, PlayerConnection connection) {
        final ClientPingServerEvent clientPingEvent = new ClientPingServerEvent(connection, packet.number());
        EventDispatcher.call(clientPingEvent);

        if (clientPingEvent.isCancelled()) {
            connection.disconnect();
        } else {
            if (clientPingEvent.getDelay().isZero()) {
                connection.sendPacket(new PongPacket(clientPingEvent.getPayload()));
                connection.disconnect();
            } else {
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    connection.sendPacket(new PongPacket(clientPingEvent.getPayload()));
                    connection.disconnect();
                }).delay(clientPingEvent.getDelay()).schedule();
            }
        }
    }
}
