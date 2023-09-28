package net.krlite.tapestop.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;

public class TapeStopModMenuImpl implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new TapeStopConfigScreen(parent).build();
	}
}
