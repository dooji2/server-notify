package com.dooji.sn.gui;

import com.dooji.sn.network.ClientPacketHandler;
import com.dooji.sn.network.NotificationData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;

public class TextureNotificationScreen extends Screen {
    private final NotificationData notificationData;

    private SoundEvent notificationSound;
    private String type;

    private final int fadeDuration = 8;
    private final int stayDelay = 16;

    double startFadeOutTime = fadeDuration + stayDelay;
    double fullAnimationLength = startFadeOutTime + fadeDuration;
    double timer = 0;

    private boolean soundPlayed = false;

    public TextureNotificationScreen(NotificationData notificationData, String type) {
        super(Text.literal(""));
        this.notificationData = notificationData;
        this.type = type;
        this.notificationSound = createSoundEvent(notificationData.getSoundNamespace(),
                notificationData.getSoundPath());

    }

    private SoundEvent createSoundEvent(String namespace, String path) {
        Identifier soundId = new Identifier(namespace, path);
        return new SoundEvent(soundId);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        playNotificationSound();

        renderBackground(matrices);
        renderTexture(matrices, notificationData.getTexture(), width / 2, height / 2, notificationData.getWidth(),
                notificationData.getHeight());

        if (notificationData.isDismissShow()) {
            double alpha = calculateAlpha();

            renderDismissText(matrices, MinecraftClient.getInstance().textRenderer, alpha);

            timer += delta;
            timer = timer % fullAnimationLength;
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void renderTexture(MatrixStack matrices, String texturePath, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, new Identifier(notificationData.getNamespace(), texturePath));
        RenderSystem.enableBlend();

        int textureX = x - width / 2;
        int textureY = y - height / 2;

        drawTexture(matrices, textureX, textureY, 0, 0, width, height, width, height);
        RenderSystem.disableBlend();
    }

    private void renderDismissText(MatrixStack matrices, TextRenderer textRenderer, double alpha) {
        Text dismissText = Text.literal("Press ESC to dismiss");
        int textWidth = textRenderer.getWidth(dismissText);
        int textX = (width - textWidth) / 2;
        int textY = height - 20;
        int color = 0xFFFFFF | ((int) (255 * alpha) << 24);

        drawTextWithShadow(matrices, textRenderer, dismissText, textX, textY, color);
    }

    double calculateAlpha() {
        if (timer < fadeDuration) {
            return timer / fadeDuration;
        } else if (timer < startFadeOutTime) {
            return 1.0f;
        } else {
            double fadeOutProgress = (timer - startFadeOutTime) / fadeDuration;
            return Math.max(0, 1 - fadeOutProgress);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void close() {
        super.close();
        MinecraftClient.getInstance().setScreen(null);

        client.execute(() -> {
            ClientPacketHandler.isDisplayingNotification = false;
            ClientPacketHandler.displayNextNotification(MinecraftClient.getInstance());
        });
    }

    private void playNotificationSound() {
        if (!soundPlayed && notificationSound != null && MinecraftClient.getInstance().getSoundManager() != null) {
            MinecraftClient.getInstance().getSoundManager()
                    .play(PositionedSoundInstance.master(notificationSound, 1.0f));
            soundPlayed = true;
        }
    }
}