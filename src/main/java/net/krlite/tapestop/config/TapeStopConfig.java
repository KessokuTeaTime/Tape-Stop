package net.krlite.tapestop.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.krlite.tapestop.TapeStop;
import org.jetbrains.annotations.NotNull;

@Config(name = "tapestop")
public class TapeStopConfig implements ConfigData {
	public boolean enabled = true;

	public boolean debugInfoEnabled = true;

	@ConfigEntry.BoundedDiscrete(min = 1000, max = 1000 * 60 * 5)
	public long timeoutMs = 1000 * 30;

	@ConfigEntry.Gui.TransitiveObject
	@ConfigEntry.Category("trigger")
	public Trigger trigger = new Trigger();

	@ConfigEntry.Gui.TransitiveObject
	@ConfigEntry.Category("visual")
	public Visual visual = new Visual();

	public static class Trigger {
		public boolean whenMinimized = true;
		public boolean whenLostFocus = true;
		public boolean afterGUITimeout = true;
		public boolean afterGameTimeout = true;
	}

	public static class Visual {
		@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
		public BackgroundStyle backgroundStyle = BackgroundStyle.PURE_COLOR;
	}

	public enum BackgroundStyle implements SelectionListEntry.Translatable {
		PURE_COLOR("config." + TapeStop.ID + ".background_style.pure_color"),
		PANORAMA("config." + TapeStop.ID + ".background_style.panorama");

		private final String key;

        BackgroundStyle(String key) {
            this.key = key;
        }

        @Override
		public @NotNull String getKey() {
			return key;
		}
	}
}
