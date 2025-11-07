# 📺 JLiveStream

Plugin to announce live streams, fully customizable.

---

## 📝 Requirements

- **Server core**: Paper
- **Version**: 1.16.5 - 1.21.10
- **Java**: 16-21

---

## 📜 Commands

- **/live [URL]**
    - **Description**: Send a start stream notification
    - **Permission**: `jlivestream.live`

- **/livestop**
    - **Description**: Send a stop stream notification
    - **Permission**: `jlivestream.stop`

- **/jlivestreamreload**
    - **Description**: Reload plugin configuration
    - **Permission**: `jlivestream.reload`

---

## ⚙ Configuration

Full customization and operation of the plugin is configured in the configuration file (`config.yml`).

```yaml
# Configuration plugin settings
# Live Stream Announcements Plugin
# Fully customizable messages and configurations

# Prefix for all messages related to live streams
prefix: "&b[jLiveStream] "
# Cooldown time in seconds between live stream announcements
cooldown: 60

sound:
  start:
    name: "ENTITY_PLAYER_LEVELUP"
    volume: 1.0
    pitch: 2.0
  stop:
    name: "BLOCK_ANVIL_DESTROY"
    volume: 1.0
    pitch: 1.5

# Messages related to live stream
broadcast:
  live:
    # Message displayed when a player starts a live stream
    started:
      - " "
      - "  &b%player% &fis now &blive!"
      - "  &fWatch the stream here: &b%link%"
      - " "

    # Message displayed when a player ends a live stream
    stopped:
      - " "
      - "  &b%player% &fhas &cended &ftheir &blive stream."
      - "  &fThank you for watching! &bSee later!"
      - " "

# Messages displayed to players
messages:
  only-player: "&fOnly &cplayers &fcan use this command."
  no-permission: "&fYou &cdon't have &fpermission to use this command."
  live-usage: "&fUsage: &b/live <url>"
  livestop-usage: "&fUsage: &b/livestop"
  invalid-link: "&fLink must start with &chttp:// &for &chttps://"
  live-started: "&fYou have &astarted &fyour live stream! Use &c/livestop &fwhen you end."
  already-live: "&fYou are &calready &flive! Use &c/livestop &fto end."
  live-stopped: "&fYou have &cstopped &fyour live stream! &aThank you &ffor you streaming of us!"
  not-live: "&fYou are &cnot currently &flive!"
  cooldown-message: "&fYou must wait &c%cooldown% sec. &fbefore using this command again."
  reload: "&fConfiguration has been successfully &areloaded!"
  ```

<img width="1000" height="587" alt="image" src="https://github.com/user-attachments/assets/6017d2b5-d375-46eb-9a8b-72957e53beb6" />







