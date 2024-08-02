package net.deepacat.createpowerlines.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Config {

    public static final String CATAGORY_GENERAL = "general";
    public static final String CATAGORY_WIRES = "wires";


    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue AUDIO_ENABLED;

    //	public static ForgeConfigSpec.IntValue HUGE_CONNECTOR_MAX_INPUT;
    public static ForgeConfigSpec.IntValue HUGE_CONNECTOR_MAX_LENGTH;

    public static ForgeConfigSpec.BooleanValue USE_BASE_CONNECTORS;
    public static ForgeConfigSpec.BooleanValue USE_GT_CONNECTORS;
    public static ForgeConfigSpec.BooleanValue USE_CCA_CONNECTORS;
    public static ForgeConfigSpec.BooleanValue USE_CUSTOM_CONNECTORS;

    public static ForgeConfigSpec.BooleanValue CONNECTOR_IGNORE_FACE_CHECK;
    public static ForgeConfigSpec.BooleanValue CONNECTOR_ALLOW_PASSIVE_IO;

    static {
        COMMON_BUILDER.comment("Make sure config changes are duplicated on both Clients and the Server when running a dedicated Server,")
                .comment(" as the config isnt synced between Clients and Server.");
        COMMON_BUILDER.comment("General Settings").push(CATAGORY_GENERAL);

        AUDIO_ENABLED = COMMON_BUILDER.comment("If audio should be enabled or not.")
                .define("audio_enabled", true);

        COMMON_BUILDER.comment("Wires").push(CATAGORY_WIRES);

        USE_BASE_CONNECTORS = COMMON_BUILDER.comment("If the base mod connectors should be registered")
                .define("use_base_connectors", true);

        USE_GT_CONNECTORS = COMMON_BUILDER.comment("If the preset GT connectors should be registered")
                .define("use_gt_connectors", true);

        USE_CCA_CONNECTORS = COMMON_BUILDER.comment("If the preset CCA connectors should be registered")
                .define("use_cca_connectors", true);

        USE_CUSTOM_CONNECTORS = COMMON_BUILDER.comment("If your custom connectors should be registered")
                .define("use_custom_connectors", true);

//		HUGE_CONNECTOR_MAX_INPUT = COMMON_BUILDER.comment("Huge Connector max input in FE/t (Energy transfer).")
//				.defineInRange("huge_connector_max_input", 2147483647, 0, Integer.MAX_VALUE);
//
//		HUGE_CONNECTOR_MAX_OUTPUT = COMMON_BUILDER.comment("Huge Connector max output in FE/t (Energy transfer).")
//				.defineInRange("huge_connector_max_output", 2147483647, 0, Integer.MAX_VALUE);

		HUGE_CONNECTOR_MAX_LENGTH = COMMON_BUILDER.comment("Huge Connector max wire length in blocks.")
				.defineInRange("huge_connector_wire_length", 64, 0, 256);

        CONNECTOR_IGNORE_FACE_CHECK = COMMON_BUILDER.comment("Ignore checking if block face can support connector.")
                .define("connector_ignore_face_check", true);

        CONNECTOR_ALLOW_PASSIVE_IO = COMMON_BUILDER.comment("Allows blocks attached to a connector to freely pass energy to and from the connector network.")
                .define("connector_allow_passive_io", true);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, java.nio.file.Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }
}
