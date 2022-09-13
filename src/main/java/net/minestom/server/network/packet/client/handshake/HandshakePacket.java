package net.minestom.server.network.packet.client.handshake;

import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.network.packet.client.ClientPreplayPacket;
import net.minestom.server.utils.binary.BinaryReader;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public record HandshakePacket(int protocolVersion, @NotNull String serverAddress,
                              int serverPort, int nextState) implements ClientPreplayPacket {


    public HandshakePacket(BinaryReader reader) {
        this(reader.readVarInt(), reader.readSizedString(BungeeCordProxy.isEnabled() ? Short.MAX_VALUE : 255),
                reader.readUnsignedShort(), reader.readVarInt());
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        writer.writeVarInt(protocolVersion);
        int maxLength = BungeeCordProxy.isEnabled() ? Short.MAX_VALUE : 255;
        if (serverAddress.length() > maxLength) {
            throw new IllegalArgumentException("serverAddress is " + serverAddress.length() + " characters long, maximum allowed is " + maxLength);
        }
        writer.writeSizedString(serverAddress);
        writer.writeUnsignedShort(serverPort);
        writer.writeVarInt(nextState);
    }
}
