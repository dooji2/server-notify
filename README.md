# Server Notify

The image below is outdated, **Server Notify** now supports 1.19.2, 1.20.1, 1.21 and 1.21.1.
Versions 1.20.1, 1.21 and 1.21.1 can be used on NeoForge with Sintrya Connector, Forgified Fabric API is required.

![a](https://cdn.modrinth.com/data/cached_images/ebb68a4cdd0adf2c471f0fe0a063e2458da8b779.png)
#

[<img src="https://i.imgur.com/HUk4jEx.png" alt="Resource Downloader - GitHub Page" width="240" height="120">](https://github.com/dooji2/server-notify/issues)

Please check out the following before proceeding:

<details>
<summary>Getting started</summary>
This mod is command-based, and it's fairly simple to use!
There are three types of notifications, texture, text, and URL (more to be added soon!).

For now, let's go over the commands.
By typing `/server-notify`, you'll see there are quite a few options available.

![Main Command](https://cdn.modrinth.com/data/cached_images/7f02cd2aa929d68d5ecadac2262bc86a0c11ad91.png)

**Edit** lets you edit the value of a notification's variable, for example its namespace or texture path!

**Info** gives you info on a certain notification.

**List** gives you a list of all notifications (Notification Name - Type)

**New** lets you create a new notification.

**Remove** lets you remove a notification.

**UUID-List** gives you a list of all notifications (Notification UUID - Name)

All commands have some auto-suggestions, so I will not go over all of them as they are pretty straightforward.

To create a new notification, all types have a base command:
```
/server-notify new "Notification Name" type type
```
Both **type** entries should be the same; they can be **texture**, **text** or **url** (for now).

To find out how to create a new notification, please choose the type of notification you'd like to create and then click on one of the dropdowns below.
<details>
<summary>Texture Notification</summary>
A texture notification lets you display any texture from any loaded resource pack in-game.

An example command would be:
```
/server-notify new "Notification Name" texture texture <sound_namespace> <sound_path> <texture_namespace> <texture_path> <width> <height> <dismissMessage> <alwaysShow>
```

**"Notification Name"** can be anything, however it must be in quotation marks. It is for you to identify the notification later on.

**<sound_namespace>** is the namespace of the sound you'd like to play when the notification is shown.

**<sound_path>** is the path of the sound you'd like to play when the notification is shown.

**<texture_namespace>** is your resource pack's namespace.

**<texture_path>** is the path to your texture.

**<width>** is the width of your texture.

**<height>** is the height of your texture.

**<dismissMessage>** displays a "Press ESC to dismiss" text at the bottom of the screen.

**<alwaysShow>** shows the notification to a player when they join even if they have already seen it.
</details>

<details>
<summary>Text-only Notification</summary>
A text notification lets you display any message as a notification.

An example command would be:
```
/server-notify new "Notification Name" text text <sound_namespace> <sound_path> <message> <dismissButton> <dismissMessage> <alwaysShow>
```

**"Notification Name"** can be anything, however it must be in quotation marks. It is for you to identify the notification later on.

**<sound_namespace>** is the namespace of the sound you'd like to play when the notification is shown.

**<sound_path>** is the path of the sound you'd like to play when the notification is shown.

**<message>** is your message in quotation marks.

**<dismissButton>** displays a "Dismiss" button on the screen.

**<dismissMessage>** displays a "Press ESC to dismiss" text at the bottom of the screen.

**<alwaysShow>** shows the notification to a player when they join even if they have already seen it.
</details>

<details>
<summary>Image from URL Notification</summary>
An image notification lets you display any image from a URL.

An example command would be:
```
/server-notify new "Notification Name" url url <sound_namespace> <sound_path> <url> <width> <height> <dismissMessage> <alwaysShow>
```

**"Notification Name"** can be anything, however it must be in quotation marks. It is for you to identify the notification later on.

**<sound_namespace>** is the namespace of the sound you'd like to play when the notification is shown.

**<sound_path>** is the path of the sound you'd like to play when the notification is shown.

**<url>** is your image URL in quotation marks.

**<width>** is the width of your texture.

**<height>** is the height of your texture.

**<dismissMessage>** displays a "Press ESC to dismiss" text at the bottom of the screen.

**<alwaysShow>** shows the notification to a player when they join even if they have already seen it.
</details>
</details>