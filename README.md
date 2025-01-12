# Server Notify

**Server Notify** is a Minecraft mod that lets you display notifications to players joining your Minecraft server. Easily set up notifications using commands, with options for texture, text, or URL-based notifications.

**Required on Client and Server!**

**Requires Fabric API. Also compatible with Forge and NeoForge, using [Sinytra Connector](https://modrinth.com/mod/connector), requires [Forgified Fabric API](https://modrinth.com/mod/forgified-fabric-api).**

## Notification Types
1. **Texture Notifications**: Display textures from any loaded resource pack.
2. **Text-only Notifications**: Display simple messages.
3. **Image from URL**: Display images from a specified URL.

## Getting Started
1. **Command-based**: All features are accessed via commands in-game.
2. **Commands**: Type `/server-notify` to view available options.

![Main Command](https://cdn.modrinth.com/data/cached_images/7f02cd2aa929d68d5ecadac2262bc86a0c11ad91.png)

### Command Options:
- **Edit**: Modify a notification's properties.
- **Info**: View details of a specific notification.
- **List**: Display all notifications.
- **New**: Create a new notification.
- **Remove**: Delete a notification.
- **UUID-List**: Show all notifications with their UUIDs.

## Creating Notifications

Use the command `/server-notify new type "Notification Name"` to create a notification, replacing `"Notification Name"` with your preferred name and `type` with **texture**, **text**, or **url**.

### Notification Types and Commands

<details>
<summary>Text-only Notification</summary>
A text notification lets you display any message as a notification.

An example command would be:
```
/server-notify new text "Notification Name" <sound_namespace> <sound_path> <message> <dismissButton> <dismissMessage> <alwaysShow>
```

**"Notification Name"** can be anything, however it must be in quotation marks. It is for you to identify the notification later on.

**<sound_namespace>** is the namespace of the sound you'd like to play when the notification is shown.

**<sound_path>** is the path of the sound you'd like to play when the notification is shown.

**<message>** is your message in quotation marks.

**<dismissButton>** displays a "Dismiss" button on the screen.

**<dismissMessage>** displays a "Press ESC to dismiss" text at the bottom of the screen.

**<alwaysShow>** shows the notification to a player when they join even if they have already seen it.

**Another Text Notification example would be:**
```
/server-notify new text "Notification Test" minecraft "ambient.cave" "hey, testing" false true true
```
</details>

<details>
<summary>Image from URL Notification</summary>
An image notification lets you display any image from a URL.

An example command would be:
```
/server-notify new url "Notification Name" <sound_namespace> <sound_path> <url> <width> <height> <dismissMessage> <alwaysShow>
```

**"Notification Name"** can be anything, however it must be in quotation marks. It is for you to identify the notification later on.

**<sound_namespace>** is the namespace of the sound you'd like to play when the notification is shown.

**<sound_path>** is the path of the sound you'd like to play when the notification is shown.

**<url>** is your image URL in quotation marks.

**<width>** is the width of your image (you might have to use a scaled down resolution to accomodate for Minecraft's GUI scale, for example for an image that's 1920x1080, scaled down while also keeping the aspect ratio would be 480 by 270).

**<height>** is the height of your image (you might have to use a scaled down resolution to accomodate for Minecraft's GUI scale, for example for an image that's 1920x1080, scaled down while also keeping the aspect ratio would be 480 by 270).

**<dismissMessage>** displays a "Press ESC to dismiss" text at the bottom of the screen.

**<alwaysShow>** shows the notification to a player when they join even if they have already seen it.

**Another Image Notification example would be:**
```
/server-notify new url "Image Test" minecraft "ambient.cave" "https://some_direct_image_url" 480 270 true true
```
</details>
</details>
