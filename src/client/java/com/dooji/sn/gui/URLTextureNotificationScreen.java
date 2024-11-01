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
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.sound.SoundEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class URLTextureNotificationScreen extends Screen {
    private final NotificationData notificationData;
    private static NativeImageBackedTexture texture2;
    private static BufferedImage bimage;

    private SoundEvent notificationSound;
    private String type;

    private final int fadeDuration = 8;
    private final int stayDelay = 16;

    double startFadeOutTime = fadeDuration + stayDelay;
    double fullAnimationLength = startFadeOutTime + fadeDuration;
    double timer = 0;

    private boolean soundPlayed = false;

    public URLTextureNotificationScreen(NotificationData notificationData, String type) {
        super(Text.literal(""));
        this.notificationData = notificationData;
        this.type = type;
        this.notificationSound = createSoundEvent(notificationData.getSoundNamespace(),
                notificationData.getSoundPath());

        try {
            bimage = ImageIO.read(new URL(notificationData.getURL()));

            if (bimage != null) {
                NativeImage nimage = new NativeImage(bimage.getWidth(), bimage.getHeight(), false);
                texture2 = new NativeImageBackedTexture(nimage);

                convertToNativeImage(bimage);

                texture2.upload();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertToNativeImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argbColor = bufferedImage.getRGB(x, y);
                int abgrColor = toAbgr(argbColor);
                texture2.getImage().setColor(x, y, abgrColor);
            }
        }
    }

    private static int toAbgr(int rgb) {
        int a = (rgb >> 24) & 255;
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = (rgb) & 255;
        return a << 24 | b << 16 | g << 8 | r;
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
        super.render(context, mouseX, mouseY, delta);

        playNotificationSound();

        if (bimage != null) {
            renderTexture(context, width / 2, height / 2, notificationData.getWidth(),
                    notificationData.getHeight());
        } else {
            super.close();

            ErrorScreen notificationScreen = new ErrorScreen(notificationData, type, "nullbimage");
            client.setScreen(notificationScreen);
        }

        if (notificationData.isDismissShow()) {
            double alpha = calculateAlpha();

            renderDismissText(context, MinecraftClient.getInstance().textRenderer, alpha);

            timer += delta;
            timer = timer % fullAnimationLength;
        }
    }

    private void renderTexture(DrawContext context, int x, int y, int width, int height) {
        int textureX = x - width / 2;
        int textureY = y - height / 2;

        context.drawTexture(client.getTextureManager().registerDynamicTexture("server-notification", texture2), textureX, textureY, 0, 0, width, height, width, height);
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