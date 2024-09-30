package net.deepacat.createpowerlines.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.io.IOException;
import java.io.InputStream;

public class ClientUtil {
    public static LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static NativeImage loadNativeImage(String name) {
        name = "/assets/" + CreatePowerlines.MODID + "/textures/" + name + ".png";
        try (InputStream is = CreatePowerlines.class.getResourceAsStream(name)) {
            return NativeImage.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
