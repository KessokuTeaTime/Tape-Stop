package net.krlite.tapestop.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.krlite.tapestop.TapeStop;
import net.krlite.tapestop.config.TapeStopConfig;

public class TapeStopModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			TapeStop.CONFIG.load();
			return AutoConfig.getConfigScreen(TapeStopConfig.class, parent).get();
		};
	}
}
