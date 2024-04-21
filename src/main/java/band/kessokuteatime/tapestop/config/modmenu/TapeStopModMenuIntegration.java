package band.kessokuteatime.tapestop.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import band.kessokuteatime.tapestop.TapeStop;
import band.kessokuteatime.tapestop.config.TapeStopConfig;

public class TapeStopModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			TapeStop.CONFIG.load();
			return AutoConfig.getConfigScreen(TapeStopConfig.class, parent).get();
		};
	}
}
