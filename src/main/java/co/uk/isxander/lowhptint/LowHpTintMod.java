package co.uk.isxander.lowhptint;

import co.uk.isxander.lowhptint.command.LowHpTintCommand;
import co.uk.isxander.lowhptint.config.LowHpTintConfig;
import co.uk.isxander.lowhptint.MathUtils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class LowHpTintMod implements ClientModInitializer {
	public static final String MOD_ID = "lowhptint";

	private static final Identifier VIGNETTE = Identifier.of(MOD_ID, "tintshape.png");
	private static LowHpTintMod INSTANCE;

	private LowHpTintConfig config;

	private float prevRed = 1;
	private float prevGreen = 1;
	private float prevBlue = 1;

	@Override
	public void onInitializeClient() {
		INSTANCE = this;

		// Load config
		config = LowHpTintConfig.load();

		// Register events
		HudRenderCallback.EVENT.register(this::onRenderHud);

		// Register commands
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				LowHpTintCommand.register(dispatcher));
	}

	private void onRenderHud(DrawContext drawContext, RenderTickCounter renderTickCounter) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null && config.isEnabled()) {
			float currentHealth = client.player.getHealth();
			// Pass 'true' to get the tick delta - this is typically used to indicate
			// whether to use the smoothed/interpolated tick delta
			float tickDelta = renderTickCounter.getTickDelta(true);
			renderTint(currentHealth, drawContext, tickDelta);
		}
	}

	private void renderTint(float currentHealth, DrawContext context, float tickDelta) {
		float threshold = config.getHealth();
		if (currentHealth <= threshold) {
			MinecraftClient client = MinecraftClient.getInstance();

			float f = (threshold - currentHealth) / threshold + 1.0F / threshold * 2.0F;
			float r = prevRed = MathUtils.lerp(prevRed, MathUtils.lerp(MathUtils.getPercent(config.getRed(), 0, 255), 0.0f, f), tickDelta * (config.getSpeed() / 100f));
			float g = prevGreen = MathUtils.lerp(prevGreen, MathUtils.lerp(MathUtils.getPercent(config.getGreen(), 0, 255), 0.0f, f), tickDelta * (config.getSpeed() / 100f));
			float b = prevBlue = MathUtils.lerp(prevBlue, MathUtils.lerp(MathUtils.getPercent(config.getBlue(), 0, 255), 0.0f, f), tickDelta * (config.getSpeed() / 100f));

			int width = client.getWindow().getScaledWidth();
			int height = client.getWindow().getScaledHeight();

			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();

			// 💡 Here's the blend mode magic!
			RenderSystem.blendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);

			// Tint color — same logic
			RenderSystem.setShaderColor(1.0f - r, 1.0f - g, 1.0f - b, 1.0f);

			RenderSystem.setShader(GameRenderer::getPositionTexProgram);
			RenderSystem.setShaderTexture(0, VIGNETTE);

			context.drawTexture(VIGNETTE, 0, 0, 0, 0, width, height, width, height);

			// 🔄 Restore default blend mode and GL state
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
		}
	}


	public static LowHpTintMod getInstance() {
		return INSTANCE;
	}

	public LowHpTintConfig getConfig() {
		return config;
	}
}