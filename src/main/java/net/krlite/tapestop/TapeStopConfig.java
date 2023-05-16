package net.krlite.tapestop;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.pierced.annotation.Comment;
import net.krlite.pierced.annotation.Silent;
import net.krlite.pierced.annotation.Table;
import net.krlite.pierced.config.Pierced;

import java.io.File;

public class TapeStopConfig extends Pierced {
	@Silent
	private static final File file = FabricLoader.getInstance().getConfigDir().resolve("tapestop.toml").toFile();

	public TapeStopConfig() {
		super(TapeStopConfig.class, file);
	}

	public boolean enabled = true;

	public long timeoutMs = 25000;

	@Table("Trigger")
	@Comment("Tape stops when the game is minimized.")
	public boolean whenMinimized = true;

	@Table("Trigger")
	@Comment("Tape stops when the game loses focus.")
	public boolean whenLostFocus = true;

	@Table("Trigger")
	@Comment("Tape stops when the game is in a GUI screen (inventory, pause menu, etc.) and reaches the timeout specified above.")
	public boolean afterGUITimeout = true;

	@Table("Trigger")
	@Comment("Tape stops when the game is not in a GUI screen and reaches the timeout specified above.")
	public boolean afterGameTimeout = true;

	@Table("Visual")
	@Comment("Renders the panorama background when the tape is stopped.")
	public boolean panorama = false;

	@Table("Debug")
	@Comment("Prints debug messages to the console.")
	public boolean debug = true;
}
