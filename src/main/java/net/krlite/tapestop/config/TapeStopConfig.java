package net.krlite.tapestop.config;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.pierced.annotation.Comment;
import net.krlite.pierced.annotation.Silent;
import net.krlite.pierced.annotation.Table;
import net.krlite.pierced.config.Pierced;

import java.io.File;

public class TapeStopConfig extends Pierced {
	private static @Silent final File file = FabricLoader.getInstance().getConfigDir().resolve("tapestop.toml").toFile();

	public TapeStopConfig() {
		super(TapeStopConfig.class, file);
		load();
	}

	private boolean enabled = true;

	public boolean enabled() {
		return enabled;
	}

	public void enabled(boolean enabled) {
		this.enabled = enabled;
		save();
	}

	@Comment("Must be between " + minTimeoutMs + " and " + maxTimeoutMs + ".")
	private long timeoutMs = 2000;

	public static final @Silent long minTimeoutMs = 1000, maxTimeoutMs = 1000 * 60;

	public long timeoutMs() {
		return timeoutMs;
	}

	public void timeoutMs(long timeoutMs) {
		this.timeoutMs = timeoutMs;
		save();
	}

	@Table("Trigger")
	@Comment("Tape stops when the game is minimized.")
	private boolean whenMinimized = true;

	public boolean whenMinimized() {
		return whenMinimized;
	}

	public void whenMinimized(boolean whenMinimized) {
		this.whenMinimized = whenMinimized;
		save();
	}

	@Table("Trigger")
	@Comment("Tape stops when the game loses focus.")
	private boolean whenLostFocus = true;

	public boolean whenLostFocus() {
		return whenLostFocus;
	}

	public void whenLostFocus(boolean whenLostFocus) {
		this.whenLostFocus = whenLostFocus;
		save();
	}

	@Table("Trigger")
	@Comment("Tape stops when the game is in a GUI screen(inventory, pause menu, etc.) and reaches the timeout specified above.")
	private boolean afterGUITimeout = true;

	public boolean afterGUITimeout() {
		return afterGUITimeout;
	}

	public void afterGUITimeout(boolean afterGUITimeout) {
		this.afterGUITimeout = afterGUITimeout;
		save();
	}

	@Table("Trigger")
	@Comment("Tape stops when the game is not in a GUI screen and reaches the timeout specified above.")
	private boolean afterGameTimeout = true;

	public boolean afterGameTimeout() {
		return afterGameTimeout;
	}

	public void afterGameTimeout(boolean afterGameTimeout) {
		this.afterGameTimeout = afterGameTimeout;
		save();
	}

	@Table("Visual")
	@Comment("Renders the panorama background while tape stopped.")
	private boolean panorama = false;

	public boolean panorama() {
		return panorama;
	}

	public void panorama(boolean panorama) {
		this.panorama = panorama;
		save();
	}

	@Table("Debug")
	@Comment("Prints debug messages to the console.")
	private boolean debug = true;

	public boolean debug() {
		return debug;
	}

	public void debug(boolean debug) {
		this.debug = debug;
		save();
	}
}
