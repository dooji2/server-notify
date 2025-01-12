package com.dooji.sn.gui;

import com.dooji.sn.network.ClientPacketHandler;
import com.dooji.sn.network.NotificationData;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;

import java.util.List;
import java.util.ArrayList;

public class ErrorScreen extends Screen {
    private final NotificationData notificationData;

    private SoundEvent notificationSound;
    private String type;
    private String reason;

    private final int fadeDuration = 8;
    private final int stayDelay = 16;

    double startFadeOutTime = fadeDuration + stayDelay;
    double fullAnimationLength = startFadeOutTime + fadeDuration;
    double timer = 0;

    private boolean soundPlayed = false;

    public ErrorScreen(NotificationData notificationData, String type, String reason) {
        super(Text.literal(""));
        this.notificationData = notificationData;
        this.type = type;
        this.reason = reason;
        this.notificationSound = createSoundEvent(notificationData.getSoundNamespace(),
                notificationData.getSoundPath());

    }

    private SoundEvent createSoundEvent(String namespace, String path) {
        Identifier soundId = Identifier.of(namespace, path);

        if (!Registries.SOUND_EVENT.containsId(soundId)) {
            SoundEvent soundEvent = SoundEvent.of(soundId);
            Registry.register(Registries.SOUND_EVENT, soundId, soundEvent);
        }

        return Registries.SOUND_EVENT.get(soundId);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        playNotificationSound();
    
        renderBackground(context);
    
        context.fill(0, 0, width, 40, 0xFF000000);
        context.fill(0, 40, width, 70, 0xFF8B0000);
    
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Server Notify"), width / 2, 15, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Please report this error to the server owner!"), width / 2, 50, 0xFFFFFF);
    
        if (reason.equals("nullbimage")) {
            List<Text> errorLines = new ArrayList<>();
            errorLines.add(Text.literal("The image could not be downloaded. Please check the URL."));
            errorLines.add(Text.of("URL: " + notificationData.getURL()));
            errorLines.add(Text.of("Notification UUID: " + notificationData.getName()));
    
            int lineHeight = 10;
            int totalHeight = errorLines.size() * lineHeight;
    
            int startY = height / 2 - totalHeight / 2;
            startY += 30;
    
            int color = 0xFFFFFF;
            
            for (Text line : errorLines) {
                context.drawCenteredTextWithShadow(textRenderer, line, width / 2, startY, color);
                startY += lineHeight;
            }
        }
    
        if (notificationData.isDismissShow()) {
            double alpha = calculateAlpha();
            renderDismissText(context, MinecraftClient.getInstance().textRenderer, alpha);
            timer += delta;
            timer = timer % fullAnimationLength;
        }
    
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderDismissText(DrawContext context, TextRenderer textRenderer, double alpha) {
        Text dismissText = Text.literal("Press ESC to dismiss");
        int textWidth = textRenderer.getWidth(dismissText);
        int textX = (width - textWidth) / 2;
        int textY = height - 20;
        int color = 0xFFFFFF | ((int) (255 * alpha) << 24);

        context.drawTextWithShadow(textRenderer, dismissText, textX, textY, color);
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