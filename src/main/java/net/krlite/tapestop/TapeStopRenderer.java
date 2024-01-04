package net.krlite.tapestop;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class TapeStopRenderer {
	public static void renderPanorama() {
		if (TapeStop.cubeMapRenderer() != null) {
			TapeStop.cubeMapRenderer().render(MinecraftClient.getInstance().getLastFrameDuration(), 1);
		}
	}

	public static void renderGrassBlock(DrawContext context) {
		MatrixStack matrixStack = context.getMatrices();
		Window window = MinecraftClient.getInstance().getWindow();

		backgroundColor:{
			context.fill(
					0, 0,
					window.getFramebufferWidth(),
					window.getFramebufferHeight(),
					TapeStop.color()
			);
		}

		grassBlockModel: {
			float offset = System.currentTimeMillis() % 27500 / 27500.0F;
			BlockState blockState = Blocks.GRASS_BLOCK.getDefaultState();
			Quaternionf modifier = RotationAxis.POSITIVE_Y.rotationDegrees(22.5F)
										   .mul(RotationAxis.POSITIVE_X.rotationDegrees(22.5F))
										   .mul(RotationAxis.POSITIVE_Y.rotationDegrees(offset * 360.0F));

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShaderColor(1, 1, 1, 1);

			matrixStack.push();
			matrixStack.translate(
					MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0F,
					MinecraftClient.getInstance().getWindow().getScaledHeight() / 2.0F,
					500
			);

			matrixStack.scale(1, -1, 1);
			matrixStack.scale(85, 85, 1);
			matrixStack.multiply(modifier);

			RenderSystem.applyModelViewMatrix();
			RenderSystem.disableCull();
			RenderSystem.enableDepthTest();

			matrixStack.translate(-0.5, -0.5, -0.5);

			float
					red = (float) (TapeStop.blockColor() >> 16 & 0xFF) / 255.0F,
					green = (float) (TapeStop.blockColor() >> 8 & 0xFF) / 255.0F,
					blue = (float) (TapeStop.blockColor() & 0xFF) / 255.0F;

			new BlockModelRenderer(MinecraftClient.getInstance().getBlockColors()).render(
					matrixStack.peek(), context.getVertexConsumers().getBuffer(RenderLayers.getBlockLayer(blockState)), blockState,
					MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState),
					red, green, blue, 0xF000F0, OverlayTexture.DEFAULT_UV
			);

			context.draw();
			RenderSystem.enableCull();
			RenderSystem.disableDepthTest();

			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
		}
	}
}
