package net.deepacat.createpowerlines.mixins;

import net.deepacat.createpowerlines.blocks.connector.types.ConnectorTypes;
import net.deepacat.createpowerlines.blocks.connector.types.WireMaterials;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @ModifyVariable(method = "loadFrom", require = 1, at = @At(value = "STORE", ordinal = 0))
    private static Map<String, String> createpowerlines$load(Map<String, String> table) {
        WireMaterials.getDefaultTranslations(table);
        ConnectorTypes.getDefaultTranslations(table);
        return table;
    }
}
