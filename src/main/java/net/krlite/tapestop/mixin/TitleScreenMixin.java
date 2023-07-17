package net.krlite.tapestop.mixin;

import net.krlite.tapestop.TapeStop;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Shadow @Final private RotatingCubeMapRenderer backgroundRenderer;

	@Inject(method = "render", at = @At("RETURN"))
	private void updateCubeMapRenderer(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		TapeStop.cubeMapRenderer(backgroundRenderer);
	}
}
