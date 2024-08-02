package net.deepacat.createpowerlines.index;

import net.deepacat.createpowerlines.CreatePowerlines;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CARecipes {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreatePowerlines.MODID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CreatePowerlines.MODID);

	private static <T extends Recipe<?>> Supplier<RecipeType<T>> register(String id) {
		return RECIPE_TYPES.register(id, () -> new RecipeType<>() {
			public String toString() {
				return id;
			}
		});
	}

    public static void register(IEventBus event) {

    	SERIALIZERS.register(event);
		RECIPE_TYPES.register(event);
    }
}
