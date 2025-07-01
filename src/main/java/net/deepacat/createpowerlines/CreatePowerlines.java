package net.deepacat.createpowerlines;

import net.createmod.catnip.lang.FontHelper;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.deepacat.createpowerlines.item.WireMaterials;
import net.deepacat.createpowerlines.index.*;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import net.deepacat.createpowerlines.config.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import net.deepacat.createpowerlines.network.EnergyNetworkPacket;
import net.deepacat.createpowerlines.network.ObservePacket;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TooltipModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CreatePowerlines.MODID)
public class CreatePowerlines {
    public static final String MODID = "createpowerlines";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreatePowerlines.MODID);

    private static final String PROTOCOL = "1";
    public static final SimpleChannel Network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CreatePowerlines() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));

        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::postInit);
        CACreativeModeTabs.register(modBus);
        REGISTRATE.registerEventListeners(modBus);
        WireMaterials.init();
        ConnectorTypes.registerAll();
    }

    public void postInit(FMLLoadCompleteEvent evt) {
        Network.registerMessage(0, ObservePacket.class, ObservePacket::encode, ObservePacket::decode, ObservePacket::handle);
        Network.registerMessage(1, EnergyNetworkPacket.class, EnergyNetworkPacket::encode, EnergyNetworkPacket::decode, EnergyNetworkPacket::handle);
        System.out.println("Create Powerlines Initialized!");
    }
}
