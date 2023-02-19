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

## サンプルコード
### import文
```kotlin
import mc.tsukimiya.mooney.core.MooneyCore
```

### 所持金を取得する
```kotlin
// @param player UUID プレイヤーのUUID
val money: Int = MooneyCore.instance.getMoney(player)
```

### 所持金を設定する
```kotlin
// @param amount Int 金額
MooneyCore.instance.setMoney(player, amount)
```

### 所持金を増やす
```kotlin
MooneyCore.instance.increaseMoney(player, amount)
```

### 所持金を減らす
```kotlin
MooneyCore.instance.decreaseMoney(player, amount)
```

### 所持金を支払う
```kotlin
// @param from UUID 支払元 
// @param to   UUID 支払先
MooneyCore.instance.payMoney(from, to, amount)
```

### アカウント作成(使用非推奨)
```kotlin
// @param defaultMoney Int 初期所持金
MooneyCore.instance.createAccount(player, defaultMoney)
```

### アカウント削除(使用非推奨)
```kotlin
MooneyCore.instance.deleteAccount(player)
```

