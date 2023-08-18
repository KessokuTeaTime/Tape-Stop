package net.krlite.tapestop;

import net.fabricmc.api.ModInitializer;
import net.krlite.tapestop.config.TapeStopConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.gui.screen.world.*;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class TapeStop implements ModInitializer {
	public static final String NAME = "Tape Stop", ID = "tapestop";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final TapeStopConfig CONFIG = new TapeStopConfig();

	private static final Class<?>[] excluded = new Class[]{
			TitleScreen.class, DemoScreen.class, AccessibilityOnboardingScreen.class, SplashOverlay.class,

			AccessibilityOptionsScreen.class, ChatOptionsScreen.class, ControlsOptionsScreen.class,
			CreditsAndAttributionScreen.class, GameOptionsScreen.class, KeybindsScreen.class, LanguageOptionsScreen.class,
			MouseOptionsScreen.class, OnlineOptionsScreen.class, OptionsScreen.class, SkinOptionsScreen.class,
			SoundOptionsScreen.class, TelemetryInfoScreen.class, VideoOptionsScreen.class,

			DownloadingTerrainScreen.class, LevelLoadingScreen.class, ChatScreen.class, CreateWorldScreen.class,
			EditGameRulesScreen.class, EditWorldScreen.class, ExperimentsScreen.class, OptimizeWorldScreen.class,
			SelectWorldScreen.class, MessageScreen.class, NoticeScreen.class, OpenToLanScreen.class, PresetsScreen.class,
			Realms32BitWarningScreen.class, SleepingChatScreen.class, StatsScreen.class, TaskScreen.class, WarningScreen.class,

			MultiplayerScreen.class, MultiplayerWarningScreen.class, AddServerScreen.class, DirectConnectScreen.class,
			DisconnectedScreen.class
	};

	private static long lastActionTime = 0, tapeStopTime;
	private static int blockColor, color;
	private static @Nullable RotatingCubeMapRenderer cubeMapRenderer;

	@Override
	public void onInitialize() {
		CONFIG.save();
	}

	public static boolean shouldTapeStop(@Nullable Screen screen) {
		if (!CONFIG.enabled() || MinecraftClient.getInstance().world == null) return false;

		if (CONFIG.whenMinimized() && GLFW.glfwGetWindowAttrib(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE) return true;

		if (CONFIG.whenLostFocus() && !MinecraftClient.getInstance().isWindowFocused()) return true;

		if (screen == null && CONFIG.afterGameTimeout() && isTimeout()) return true;

		if (screen != null && !Arrays.asList(excluded).contains(screen.getClass())
					&& CONFIG.afterGUITimeout() && isTimeout()) return true;

		tapeStopTime = Util.getMeasuringTimeMs();
		updateColors();
		return false;
	}

	private static void updateColors() {
		blockColor = randomColorBits() << 16 | randomColorBits() << 8 | randomColorBits();
		color = brighten(blockColor);
	}

	private static int randomColorBits() {
		int color = new Random().nextInt(0x10, 0x3C);
		return color <= 0x10 ? 0 : color;
	}

	private static int brighten(int color) {
		if (color <= 0) return 0;

		Color hslColor = new Color(color);
		float[] hsl = Color.RGBtoHSB(hslColor.getRed(), hslColor.getGreen(), hslColor.getBlue(), null);

		float[] factors = new float[]{ 1, new Random().nextFloat(1.9F, 2.2F), new Random().nextFloat(2.3F, 3.1F) };
		for (int i = 0; i < 3; i++) {
			hsl[i] *= factors[i];
			hsl[i] = Math.max(0, Math.min(1, hsl[i]));
		}

		return Color.HSBtoRGB(hsl[0], hsl[1], hsl[2]);
	}

	public static long tapeStopTime() {
		return tapeStopTime;
	}

	public static int blockColor() {
		return blockColor;
	}

	public static int color() {
		return color;
	}

	public static boolean isTimeout() {
		return Util.getMeasuringTimeMs() - lastActionTime > CONFIG.timeoutMs();
	}

	public static void action() {
		lastActionTime = Util.getMeasuringTimeMs();
	}

	@Nullable public static RotatingCubeMapRenderer cubeMapRenderer() {
		return cubeMapRenderer;
	}

	public static void cubeMapRenderer(@Nullable RotatingCubeMapRenderer cubeMapRenderer) {
		TapeStop.cubeMapRenderer = cubeMapRenderer;
	}

	public static Text localize(String category, String... paths) {
		return Text.translatable(category + "." + ID + "." + String.join(".", paths));
	}
}
