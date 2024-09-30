package net.deepacat.createpowerlines.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;

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

    public static Pair<ResourceLocation, BlockModel> genFlatItemModel(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", CreatePowerlines.MODID + ":item/" + id);
        root.add("textures", textures);
        BlockModel model = BlockModel.fromString(root.toString());
        return Pair.of(new ResourceLocation(CreatePowerlines.MODID, "models/item/" + id + ".json"), model);
    }
}
