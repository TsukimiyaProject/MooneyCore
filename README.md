# MooneyCore
ジ・エコノミープラグイン for 月宮計画  

## コマンド
| コマンド | 引数 | 内容 | パーミッション |
| --- | --- | --- | --- |
| /money | | 自分の所持金を表示する | `tsukimiya.mooney.core.money` |
| /money show | `<player>` | `<player>`の所持金を表示する | `tsukimiya.mooney.core.show` |
| /money pay | `<player>` `<amount>` | `<player>`に`<amount>`支払う | `tsukimiya.mooney.core.pay` |
| /money set | `<player>` `<amount>` | `<player>`の所持金を`<amount>`に設定する | `tsukimiya.mooney.core.set` |
| /money plus | `<player>` `<amount>` | `<player>`の所持金を`<amount>`増やす | `tsukimiya.mooney.core.plus` |
| /money minus | `<player>` `<amount>` | `<player>`の所持金を`<amount>`減らす | `tsukimiya.mooney.core.minus` |
| /money help | | コマンドの使い方を表示する | `tsukimiya.mooney.core.help` `tsukimiya.mooney.core.help.op` |

## パーミッション
| パーミッション | 内容 | デフォルト |
| --- | --- | --- |
| tsukimiya.mooney.core.money | `/money`の実行権限 | true |
| tsukimiya.mooney.core.show | `/money show`の実行権限 | op |
| tsukimiya.mooney.core.pay | `/money pay`の実行権限 | true |
| tsukimiya.mooney.core.set | `/money set`の実行権限 | op |
| tsukimiya.mooney.core.plus | `/money plus`の実行権限 | op |
| tsukimiya.mooney.core.minus | `/money plus`の実行権限 | op |
| tsukimiya.mooney.core.help | `/money help`の実行権限 | true |
| tsukimiya.mooney.core.help.op | OP用の`/money help`の実行権限 | op |

## 依存プラグイン
- [Lib4B](https://github.com/TsukimiyaProject/Lib4B)

## API
こんな感じで使う
```kotlin
import mc.tsukimiya.mooney.core.MooneyCore

val money = MooneyCore.instance.getMoney(playerUUID)
```
