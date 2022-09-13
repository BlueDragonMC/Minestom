package net.minestom.server.network.packet.client.status;

import net.minestom.server.network.packet.client.ClientPreplayPacket;
import net.minestom.server.utils.binary.BinaryReader;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public record StatusRequestPacket() implements ClientPreplayPacket {
    public StatusRequestPacket(BinaryReader reader) {
        this();
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        // Empty
    }
}
