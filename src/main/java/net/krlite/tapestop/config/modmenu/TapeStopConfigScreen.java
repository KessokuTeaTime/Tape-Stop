package net.krlite.tapestop.config.modmenu;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.tapestop.TapeStop;
import net.krlite.tapestop.config.TapeStopConfig;
import net.minecraft.client.gui.screen.Screen;

import static net.krlite.tapestop.TapeStop.CONFIG;

public class TapeStopConfigScreen {
	public TapeStopConfigScreen(Screen parent) {
		builder.setParentScreen(parent);
		initEntries();
	}

	public Screen build() {
		return builder.build();
	}

	private final ConfigBuilder builder = ConfigBuilder.create()
												  .setTitle(TapeStop.localize("screen", "config", "title"))
												  .transparentBackground()
												  .setSavingRunnable(CONFIG::save);

	private final ConfigEntryBuilder entryBuilder = builder.entryBuilder();

	private final ConfigCategory general = builder.getOrCreateCategory(TapeStop.localize("config", "category", "general"));

	private final ConfigCategory visual = builder.getOrCreateCategory(TapeStop.localize("config", "category", "visual"));

	private final ConfigCategory trigger = builder.getOrCreateCategory(TapeStop.localize("config", "category", "trigger"));

	private final ConfigCategory debug = builder.getOrCreateCategory(TapeStop.localize("config", "category", "debug"));

	private void initEntries() {
		// General

		general.addEntry(entryBuilder.startBooleanToggle(
						TapeStop.localize("config", "general", "enabled"),
						CONFIG.enabled()
				)
								 .setDefaultValue(true)
								 .setTooltip(TapeStop.localize("config", "general", "enabled", "tooltip"))
								 .setSaveConsumer(CONFIG::enabled)
								 .build());

		general.addEntry(entryBuilder.startLongSlider(
						TapeStop.localize("config", "general", "timeout_ms"),
						CONFIG.timeoutMs(),
						TapeStopConfig.minTimeoutMs, TapeStopConfig.maxTimeoutMs
				)
								 .setDefaultValue(1000 * 30)
								 .setTooltip(TapeStop.localize("config", "general", "timeout_ms", "tooltip"))
								 .setSaveConsumer(CONFIG::timeoutMs)
								 .build());

		// Trigger

		trigger.addEntry(entryBuilder.startBooleanToggle(
						TapeStop.localize("config", "trigger", "when_minimized"),
						CONFIG.whenMinimized()
				)
								 .setDefaultValue(true)
								 .setTooltip(TapeStop.localize("config", "trigger", "when_minimized", "tooltip"))
								 .setSaveConsumer(CONFIG::whenMinimized)
								 .build());

		trigger.addEntry(entryBuilder.startBooleanToggle(
						TapeStop.localize("config", "trigger", "when_lost_focus"),
						CONFIG.whenLostFocus()
				)
								 .setDefaultValue(true)
								 .setTooltip(TapeStop.localize("config", "trigger", "when_lost_focus", "tooltip"))
								 .setSaveConsumer(CONFIG::whenLostFocus)
								 .build());

		trigger.addEntry(entryBuilder.startBooleanToggle(
						TapeStop.localize("config", "trigger", "after_gui_timeout"),
						CONFIG.afterGUITimeout()
				)
								 .setDefaultValue(true)
								 .setTooltip(TapeStop.localize("config", "trigger", "after_gui_timeout", "tooltip"))
								 .setSaveConsumer(CONFIG::afterGUITimeout)
								 .build());

		trigger.addEntry(entryBuilder.startBooleanToggle(
						TapeStop.localize("config", "trigger", "after_game_timeout"),
						CONFIG.afterGameTimeout()
				)
								 .setDefaultValue(true)
								 .setTooltip(TapeStop.localize("config", "trigger", "after_game_timeout", "tooltip"))
								 .setSaveConsumer(CONFIG::afterGameTimeout)
								 .build());

		// Visual

		visual.addEntry(entryBuilder.startBooleanToggle(
						TapeStop.localize("config", "visual", "panorama"),
						CONFIG.panorama()
				)
								.setDefaultValue(true)
								.setTooltip(TapeStop.localize("config", "visual", "panorama", "tooltip"))
								.setSaveConsumer(CONFIG::panorama)
								.build());

		// Debug

		debug.addEntry(entryBuilder.startBooleanToggle(
						TapeStop.localize("config", "debug", "enabled"),
						CONFIG.debugEnabled()
				)
							   .setDefaultValue(false)
							   .setTooltip(TapeStop.localize("config", "debug", "enabled", "tooltip"))
							   .setSaveConsumer(CONFIG::debugEnabled)
							   .build());
	}
}
