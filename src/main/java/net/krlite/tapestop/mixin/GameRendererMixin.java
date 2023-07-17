package net.krlite.tapestop.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.tapestop.TapeStop;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
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

			Matrix4f matrix4f = Matrix4f.projectionMatrix(0.0F, (float) ((double) window.getFramebufferWidth() / window.getScaleFactor()), 0.0F, (float) ((double) window.getFramebufferHeight() / window.getScaleFactor()), 1000.0F, 3000.0F);
			RenderSystem.setProjectionMatrix(matrix4f);
			MatrixStack matrixStack = RenderSystem.getModelViewStack();

			matrixStack.push();
			matrixStack.loadIdentity();
			matrixStack.translate(0.0F, 0.0F, -2000.0F);
			RenderSystem.applyModelViewMatrix();

			if (TapeStop.CONFIG.panorama() && TapeStop.cubeMapRenderer() != null) {
				panorama: {
					TapeStop.cubeMapRenderer().render(MinecraftClient.getInstance().getLastFrameDuration(), 1);
				}

				if (!skipped) {
					TapeStop.LOGGER.info("Tape stopped. Rendering panorama");
				}
			}

			else {
				backgroundColor:{
					DrawableHelper.fill(
							new MatrixStack(),
							0, 0,
							window.getFramebufferWidth(),
							window.getFramebufferHeight(),
							TapeStop.color()
					);
				}

				grassBlockModel: {
					float offset = System.currentTimeMillis() % 27500 / 27500.0F;
					BlockState blockState = Blocks.GRASS_BLOCK.getDefaultState();
					MatrixStack modelMatrixStack = new MatrixStack();

					RenderSystem.enableBlend();
					RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
					RenderSystem.setShaderColor(1, 1, 1, 1);

					modelMatrixStack.push();
					modelMatrixStack.translate(
							MinecraftClient.getInstance().getWindow().getScaledWidth() / 2.0F,
							MinecraftClient.getInstance().getWindow().getScaledHeight() / 2.0F,
							500
					);

					modelMatrixStack.scale(1, -1, 1);
					modelMatrixStack.scale(85, 85, 1);

					modelMatrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(22.5F));
					modelMatrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(22.5F));
					modelMatrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(offset * 360.0F));


					RenderSystem.applyModelViewMatrix();
					RenderSystem.disableCull();
					RenderSystem.enableDepthTest();

					modelMatrixStack.translate(-0.5, -0.5, -0.5);

					VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

					float
							red = (float) (TapeStop.color() >> 16 & 0xFF) / 255.0F,
							green = (float) (TapeStop.color() >> 8 & 0xFF) / 255.0F,
							blue = (float) (TapeStop.color() & 0xFF) / 255.0F;

					new BlockModelRenderer(MinecraftClient.getInstance().getBlockColors()).render(
							modelMatrixStack.peek(), immediate.getBuffer(RenderLayers.getBlockLayer(blockState)), blockState,
							MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState),
							red, green, blue, 0xF000F0, OverlayTexture.DEFAULT_UV
					);

					immediate.draw();
					RenderSystem.enableCull();
					RenderSystem.disableDepthTest();

					modelMatrixStack.pop();
					RenderSystem.applyModelViewMatrix();
				}

				if (!skipped) {
					TapeStop.LOGGER.info("Tape stopped. Rendering overlay with background color 0x" + Integer.toHexString(TapeStop.color()).toUpperCase(Locale.ROOT));
				}
			}

			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();
		}
		return skipped = skip;
	}
}
