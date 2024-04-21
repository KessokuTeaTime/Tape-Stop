package band.kessokuteatime.tapestop.mixin;

import band.kessokuteatime.tapestop.TapeStop;
import net.minecraft.client.Keyboard;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ActionTrigger {
	@Mixin(Keyboard.class)
	public static class KeyboardMixin {
		@Inject(method = "onKey", at = @At("HEAD"))
		private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
			TapeStop.action();
		}
	}

	@Mixin(Mouse.class)
	public static class MouseMixin {
		@Inject(method = "onMouseButton", at = @At("HEAD"))
		private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
			TapeStop.action();
		}

		@Inject(method = "onMouseScroll", at = @At("HEAD"))
		private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
			TapeStop.action();
		}

		@Inject(method = "onCursorPos", at = @At("HEAD"))
		private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
			TapeStop.action();
		}
	}
}
