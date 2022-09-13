package net.minestom.server.network.packet.client.login;

import net.minestom.server.network.packet.client.ClientPreplayPacket;
import net.minestom.server.utils.binary.BinaryReader;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record LoginPluginResponsePacket(int messageId, byte @Nullable [] data) implements ClientPreplayPacket {

    public LoginPluginResponsePacket(BinaryReader reader) {
        this(reader.readVarInt(), reader.readBoolean() ? reader.readRemainingBytes() : null);
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        writer.writeVarInt(messageId);
        writer.writeBoolean(data != null);
        if (data != null) writer.writeBytes(data);
    }
}
