# Loqor

**Loqor** is a PlayerVaults-style plugin for Minecraft **1.20.4+** that adds secure, personal lockers with a touch of personality.  
Featuring unique “Vault Spirits” that talk to you, lore infusion for special items, sign integration, and simple commands — Loqor vaults blends utility with fun.

---

## ✨ Features

- **Secure Lockers** – Personal, expandable vaults for each player.
- **Vault Spirits** – Each locker has its own quirky spirit with randomized messages.
- **Lore Infusion** – Automatically adds custom lore lines to special items.
- **Sign Integration** – Bind lockers to signs for quick access.
- **Config Reload Command** – Update configs live with `/loqor reload`.
- **Config Versioning** – Automatically merges new settings without deleting your edits.
- **Lightweight & Fast** – Optimized for performance on busy servers.

---

## 📦 Installation

1. Download the latest Loqor vaults `.jar` from the [Releases](../../releases) page.
2. Drop it into your server’s `plugins` folder.
3. Restart your server to generate the default configuration.
4. Edit `config.yml` to customize settings.
5. Use `/loqor reload` to apply changes instantly.

---

## 🛠 Commands

| Command               | Description                                      | Permission                 |
|-----------------------|--------------------------------------------------|----------------------------|
| `/locker [#] [player]` | Opens a locker                                  | `loqor.commands.locker`    |
| `/lockerdel <#> [player]` | Deletes a locker                             | `loqor.commands.lockerdel` |
| `/lockersign <#> [player]` | Binds a locker to a sign                     | `loqor.commands.lockersign`|
| `/lockerfind <item> [player]` | Finds which locker contains an item       | `loqor.commands.lockerfind`|
| `/lockerdeposit [locker] [player]` | Deposits an item into a locker       | `loqor.commands.lockerdeposit` |
| `/loqor reload`       | Reloads the plugin and config                    | `loqor.reload`             |

---

## ⚙ Configuration

Loqor’s `config.yml` is designed for **easy customization**:
- Enable or disable features like personality messages or lore infusion.
- Adjust vault spirit personalities and their dialogue.
- Add trigger words for special lore lines.
- All changes can be reloaded without restarting the server.

---

## 📜 License

**All Rights Reserved**  
You may:
- Download and use Loqor on your Minecraft server.
- Customize `config.yml` for your needs.

You may not:
- Redistribute or re-upload Loqor or its source code.
- Claim the plugin or its code as your own.

See [LICENSE](License.txt) for details.

---

## 💡 Suggestions & Issues

Found a bug or have an idea?  
Open an [issue](../../issues) or start a discussion — feedback is welcome!

---

> Made with ❤️ by **Loqor**  
> For Minecraft 1.20.4 and above
