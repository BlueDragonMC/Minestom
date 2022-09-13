package net.minestom.server.network.packet.client.status;

import net.minestom.server.network.packet.client.ClientPreplayPacket;
import net.minestom.server.utils.binary.BinaryReader;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public record PingPacket(long number) implements ClientPreplayPacket {
    public PingPacket(BinaryReader reader) {
        this(reader.readLong());
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        writer.writeLong(number);
    }
}
