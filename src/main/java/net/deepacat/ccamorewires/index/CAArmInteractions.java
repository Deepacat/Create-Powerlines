package net.deepacat.ccamorewires.index;

import net.deepacat.ccamorewires.CCAMoreWires;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CAArmInteractions {
    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = factory.apply(CCAMoreWires.asResource(id));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static void register() {}
}
