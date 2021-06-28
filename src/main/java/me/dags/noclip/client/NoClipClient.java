package me.dags.noclip.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.dags.noclip.common.EntityNoClipper;
import me.dags.noclip.common.NoClipData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;

import java.io.File;

/**
 * @author dags <dags@dags.me>
 */
public class NoClipClient {

    private static Binds binds = Binds.load();
    private static final NoClipData data = new NoClipData();

    public static File getGameDir() {
        return Minecraft.getInstance().gameDirectory;
    }

    public static void onTick(boolean inGame) {
        if (inGame) {
            binds.tick();
        }
    }

    public static void sendNoClipData() {
        ByteBuf buf = Unpooled.buffer().writeBoolean(NoClipClient.getNoClipData().noClip());
        CCustomPayloadPacket payload = new CCustomPayloadPacket(EntityNoClipper.NOCLIP_CHANNEL, new PacketBuffer(buf));
        assert Minecraft.getInstance().player != null;
        Minecraft.getInstance().player.connection.send(payload);
    }

    public static NoClipData getNoClipData() {
        return data;
    }
}
