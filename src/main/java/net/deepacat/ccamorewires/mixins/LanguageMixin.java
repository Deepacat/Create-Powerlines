package net.deepacat.ccamorewires.mixins;

import net.deepacat.ccamorewires.blocks.connector.types.ConnectorTypes;
import net.minecraft.locale.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(Language.class)
public class LanguageMixin {
    @ModifyArg(method = "loadDefault", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/server/LanguageHook;captureLanguageMap(Ljava/util/Map;)V"))
    private static Map<String, String> ccawires$load(Map<String, String> table) {
        ConnectorTypes.getDefaultTranslations(table);
        return table;
    }
}
