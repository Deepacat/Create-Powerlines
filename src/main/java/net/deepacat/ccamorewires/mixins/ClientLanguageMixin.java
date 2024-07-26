package net.deepacat.ccamorewires.mixins;

import net.deepacat.ccamorewires.blocks.connector.types.ConnectorTypes;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @ModifyVariable(method = "loadFrom", require = 1, at = @At(value = "STORE", ordinal = 0))
    private static Map<String, String> ccawires$load(Map<String, String> table) {
        ConnectorTypes.getDefaultTranslations(table);
        return table;
    }
}
