package net.krlite.tapestop.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import net.krlite.tapestop.TapeStop;
import net.krlite.tapestop.TapeStopRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Locale;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Unique
	private boolean skipped = false;

	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;skipGameRender:Z", opcode = Opcodes.GETFIELD))
	private boolean skipGameRender(MinecraftClient client) {
		boolean skip = TapeStop.shouldTapeStop(client.currentScreen);
		if (skip) {
			Window window = MinecraftClient.getInstance().getWindow();
			RenderSystem.clear(256, MinecraftClient.IS_SYSTEM_MAC);

			Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, (float) ((double) window.getFramebufferWidth() / window.getScaleFactor()), (float) ((double) window.getFramebufferHeight() / window.getScaleFactor()), 0.0F, 1000.0F, 3000.0F);
			RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_Z);
			DrawContext context = new DrawContext(client, client.getBufferBuilders().getEntityVertexConsumers());
			MatrixStack matrixStack = context.getMatrices();

			matrixStack.push();
			matrixStack.loadIdentity();
			matrixStack.translate(0.0F, 0.0F, -2000.0F);
			RenderSystem.applyModelViewMatrix();

			if (TapeStop.CONFIG.panorama() && TapeStop.cubeMapRenderer() != null) {
				panorama: {
					TapeStopRenderer.renderPanorama();
				}

				if (!skipped) {
					TapeStop.LOGGER.info("Tape stopped. Rendering panorama");
				}
			}

			else {
				grassBlock: {
					TapeStopRenderer.renderGrassBlock(context);
				}

				if (!skipped) {
					TapeStop.LOGGER.info("Tape stopped. Rendering overlay with background color " + String.format("#%06x", TapeStop.color() & 0xFFFFFF).toUpperCase(Locale.ROOT));
				}
			}

			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
		}
		return skipped = skip;
	}
}
