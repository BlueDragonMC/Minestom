package net.minestom.server.network.packet.client.login;

import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.mojangAuth.MojangCrypt;
import net.minestom.server.network.packet.client.ClientPreplayPacket;
import net.minestom.server.utils.binary.BinaryReader;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;

public record EncryptionResponsePacket(byte[] sharedSecret, byte[] verifyToken) implements ClientPreplayPacket {

    public EncryptionResponsePacket(BinaryReader reader) {
        this(reader.readByteArray(), reader.readByteArray());
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        writer.writeByteArray(sharedSecret);
        writer.writeByteArray(verifyToken);
    }
}
