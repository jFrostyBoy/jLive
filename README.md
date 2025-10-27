# 📺 jLive

A Minecraft server plugin that allows your players to notify others about their live streams.

---

## 📝 Requirements

- **Server core**: Paper
- **Version**: 1.21.1+
- **Java**: 21

---

## 📜 Commands

- **/live [URL]**
    - **Description**: Send a stream notification
    - **Permission**: `jlive.use`

- **/jlivereload**
    - **Description**: Reload plugin configuration
    - **Permission**: `jlive.reload`

---

## ⚙ Configuration

Full customization and operation of the plugin is configured in the configuration file (`config.yml`).

```yaml
prefix: "&b[jLive] &f"
cooldown: 60 # in seconds
messages:
  console: "&fCommand &cnot executable &fon behalf of &cConsole"
  no-permission: "&fYou &cdon't have permission &fto use the command"
  usage: "&fUsage: &b/live [URL]"
  live-message: |
    &r  &b&n|&r  &fPlayer &b%player% &fis &bstreaming
    &r  &b|  &fCome in: &b%link%
  cooldown-message: "&fPlease wait &c%cooldown% sec. &fbefore using again!"

  reload: "&fPlugin configuration successfully &areloaded!"

<img width="1021" height="195" alt="зображення" src="https://github.com/user-attachments/assets/3b06d92c-2305-478f-9e08-cc2c7bc844e9" />
