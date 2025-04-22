package co.uk.isxander.lowhptint;

import co.uk.isxander.lowhptint.LowHpTintScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory getModConfigScreenFactory() {
        return parent -> new LowHpTintScreen();
    }
}