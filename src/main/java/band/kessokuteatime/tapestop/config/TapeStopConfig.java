package band.kessokuteatime.tapestop.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import band.kessokuteatime.tapestop.TapeStop;
import org.jetbrains.annotations.NotNull;

@Config(name = "tapestop")
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public class TapeStopConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public boolean enabled = true;
	@ConfigEntry.Gui.Tooltip
	public boolean debugInfoEnabled = true;
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.BoundedDiscrete(min = 1000, max = 1000 * 60 * 5)
	public long timeoutMs = 1000 * 30;

	@ConfigEntry.Gui.TransitiveObject
	@ConfigEntry.Category("trigger")
	public Trigger trigger = new Trigger();

	@ConfigEntry.Gui.TransitiveObject
	@ConfigEntry.Category("visual")
	public Visual visual = new Visual();

	public static class Trigger {
		@ConfigEntry.Gui.Tooltip
		public boolean whenMinimized = true;
		@ConfigEntry.Gui.Tooltip
		public boolean whenLostFocus = true;
		@ConfigEntry.Gui.Tooltip
		public boolean afterGUITimeout = true;
		@ConfigEntry.Gui.Tooltip
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
