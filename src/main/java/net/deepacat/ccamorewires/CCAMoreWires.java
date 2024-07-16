package net.deepacat.ccamorewires;

import net.deepacat.ccamorewires.blocks.connector.types.ConnectorTypes;
import net.deepacat.ccamorewires.index.*;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.deepacat.ccamorewires.config.Config;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import net.deepacat.ccamorewires.network.EnergyNetworkPacket;
import net.deepacat.ccamorewires.network.ObservePacket;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TooltipModifier;

@Mod(CCAMoreWires.MODID)
public class CCAMoreWires {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "ccawires";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CCAMoreWires.MODID);

    private static final String PROTOCOL = "1";
	public static final SimpleChannel Network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CCAMoreWires() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
        //FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, CARecipes::register);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("ccawires-common.toml"));

        CACreativeModeTabs.register(eventBus);
        REGISTRATE.registerEventListeners(eventBus);
        CABlocks.register();
        CAItems.register();
        CARecipes.register(eventBus);
        ConnectorTypes.register();
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        RenderType cutout = RenderType.cutoutMipped();

        //ItemBlockRenderTypes.setRenderLayer(CABlocks.SMALL_LIGHT_CONNECTOR.get(), cutout);
    }

    public void postInit(FMLLoadCompleteEvent evt) {
        Network.registerMessage(0, ObservePacket.class, ObservePacket::encode, ObservePacket::decode, ObservePacket::handle);
        Network.registerMessage(1, EnergyNetworkPacket.class, EnergyNetworkPacket::encode, EnergyNetworkPacket::decode, EnergyNetworkPacket::handle);

    	System.out.println("C&A More Wires Initialized!");
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event) {
    	CommandDispatcher<CommandSourceStack> dispather = event.getDispatcher();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
