# MooneyCore
ジ・エコノミープラグイン for 月宮計画  

## コマンド
| コマンド | 引数 | 内容 | 権限 |
| --- | --- | --- | --- |
| /money | | 自分の所持金を表示する | `Player` |
| /money show | `<player>` | `<player>`の所持金を表示する | `OP` `Console` |
| /money pay | `<player>` `<amount>` | `<player>`に`<amount>`支払う | `Player` |
| /money set | `<player>` `<amount>` | `<player>`の所持金を`<amount>`に設定する | `OP` `Console` |
| /money plus | `<player>` `<amount>` | `<player>`の所持金を`<amount>`増やす | `OP` `Console` |
| /money minus | `<player>` `<amount>` | `<player>`の所持金を`<amount>`減らす | `OP` `Console` |

## 依存プラグイン
- [Lib4B](https://github.com/TsukimiyaProject/Lib4B)

## API
こんな感じで使う
```kotlin
import mc.tsukimiya.mooney.core.MooneyCore

val money = MooneyCore.instance.getMoney(playerUUID)
```

## 実装予定
- ランキング表示
- ヘルプコマンド
