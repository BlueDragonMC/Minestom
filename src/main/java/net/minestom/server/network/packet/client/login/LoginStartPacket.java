package net.minestom.server.network.packet.client.login;

import net.minestom.server.network.packet.client.ClientPreplayPacket;
import net.minestom.server.utils.binary.BinaryReader;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public record LoginStartPacket(@NotNull String username) implements ClientPreplayPacket {

    public LoginStartPacket(BinaryReader reader) {
        this(reader.readSizedString(16));
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        if (username.length() > 16)
            throw new IllegalArgumentException("Username is not allowed to be longer than 16 characters");
        writer.writeSizedString(username);
    }
}
