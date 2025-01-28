package net.deepacat.createpowerlines.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_REGISTRY = "registry";
    public static final String CATEGORY_GREGTECH = "gregtech";


    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue AUDIO_ENABLED;
    public static ForgeConfigSpec.BooleanValue CONNECTOR_IGNORE_FACE_CHECK;
    public static ForgeConfigSpec.BooleanValue CONNECTOR_ALLOW_PASSIVE_IO;

//    public static ForgeConfigSpec.IntValue HUGE_CONNECTOR_MAX_INPUT;
//    public static ForgeConfigSpec.IntValue HUGE_CONNECTOR_MAX_LENGTH;

    public static ForgeConfigSpec.BooleanValue USE_BASE_CONNECTORS;
    public static ForgeConfigSpec.BooleanValue USE_GT_CONNECTORS;
    public static ForgeConfigSpec.BooleanValue USE_CCA_CONNECTORS;
//    public static ForgeConfigSpec.BooleanValue USE_CUSTOM_CONNECTORS;

    public static ForgeConfigSpec.IntValue GT_CONNECTORS_MAX_TIER;
    public static ForgeConfigSpec.BooleanValue GT_CONNECTOR_RECIPES;
    public static ForgeConfigSpec.BooleanValue GT_WIRE_RECIPES;
    public static ForgeConfigSpec.BooleanValue GT_WIRES;

    static {
        // INFO
        COMMON_BUILDER.comment("Make sure config changes are duplicated on both Clients and the Server when running a dedicated Server,")
                .comment(" as the config isnt synced between Clients and Server.");

        // SETTINGS
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        AUDIO_ENABLED = COMMON_BUILDER.comment("If audio should be enabled or not.")
                .define("audio_enabled", true);
        CONNECTOR_IGNORE_FACE_CHECK = COMMON_BUILDER.comment("Ignore checking if block face can support connector.")
                .define("connector_ignore_face_check", true);
        CONNECTOR_ALLOW_PASSIVE_IO = COMMON_BUILDER.comment("Allows blocks attached to a connector to freely pass energy to and from the connector network.")
                .define("connector_allow_passive_io", true);
        COMMON_BUILDER.pop();

        // REGISTRY
        COMMON_BUILDER.comment("Registry").push(CATEGORY_REGISTRY);
        USE_BASE_CONNECTORS = COMMON_BUILDER.comment("If the base mod connectors should be registered")
                .define("use_base_connectors", true);
        USE_GT_CONNECTORS = COMMON_BUILDER.comment("If the preset GT connectors should be registered")
                .define("use_gt_connectors", true);
        USE_CCA_CONNECTORS = COMMON_BUILDER.comment("If the preset CCA connectors should be registered")
                .define("use_cca_connectors", true);
//        USE_CUSTOM_CONNECTORS = COMMON_BUILDER.comment("If your custom connectors should be registered")
//                .define("use_custom_connectors", true);
        COMMON_BUILDER.pop();

        // GREGTECH OPTIONS
        COMMON_BUILDER.comment("GregTech Compat Options").push(CATEGORY_GREGTECH);
        GT_CONNECTORS_MAX_TIER = COMMON_BUILDER
                .comment("The max tier gt connectors will register to")
                .comment("Accepts values 0 (ULV) to 9 (UHV)")
                .defineInRange("gt_connectors_max_tier", 9, 0, 9);
        GT_CONNECTOR_RECIPES = COMMON_BUILDER.comment("If GT connectors should have recipes auto generated")
                .define("gt_connector_recipes", true);
        GT_WIRE_RECIPES = COMMON_BUILDER.comment("If GT wires should have recipes auto generated")
                .define("gt_wire_recipes", true);
//        GT_WIRES = COMMON_BUILDER.comment("If GT wire items should be auto generated")
//                .define("gt_wires", true);
        COMMON_BUILDER.pop();
//         = COMMON_BUILDER.comment("")
//                .define("", true);

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
