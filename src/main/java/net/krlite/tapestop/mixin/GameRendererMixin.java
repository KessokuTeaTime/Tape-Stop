package net.krlite.tapestop.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.tapestop.TapeStop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "render", at = @At("HEAD"))
	private void tick(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		TapeStop.tick();
	}

	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;skipGameRender:Z", opcode = Opcodes.GETFIELD))
	private boolean skipGameRender(MinecraftClient client) {
		boolean skip = TapeStop.shouldTapeStop(client.currentScreen);
		if (skip){
			Window window = MinecraftClient.getInstance().getWindow();
			RenderSystem.clear(256, MinecraftClient.IS_SYSTEM_MAC);

			Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, (float) ((double) window.getFramebufferWidth() / window.getScaleFactor()), (float) ((double) window.getFramebufferHeight() / window.getScaleFactor()), 0.0F, 1000.0F, 3000.0F);
			RenderSystem.setProjectionMatrix(matrix4f);
			MatrixStack matrixStack = RenderSystem.getModelViewStack();

			matrixStack.push();
			matrixStack.loadIdentity();
			matrixStack.translate(0.0F, 0.0F, -2000.0F);
			RenderSystem.applyModelViewMatrix();

			if (TapeStop.CONFIG.panorama) {
				new RotatingCubeMapRenderer(TitleScreen.PANORAMA_CUBE_MAP).render(TapeStop.delta(), 1);
			}
			else {
				DrawableHelper.fill(
						new MatrixStack(),
						0, 0,
						window.getFramebufferWidth(),
						window.getFramebufferHeight(),
						TapeStop.color()
				);
			}

			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
		}
		return skip;
	}
}
