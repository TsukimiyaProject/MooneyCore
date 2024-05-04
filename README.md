# MooneyCore
ジ・エコノミープラグイン for 月宮計画

## コマンド
| コマンド        | 引数 | 内容 | パーミッション                                                      |
|-------------| --- | --- |--------------------------------------------------------------|
| /mymoney    | | 自分の所持金を表示する | `tsukimiya.mooney.core.mymoney`                              |
| /showmoeny  | `<player>` | `<player>`の所持金を表示する | `tsukimiya.mooney.core.showmoney`                            |
| /paymoney   | `<player>` `<amount>` | `<player>`に`<amount>`支払う | `tsukimiya.mooney.core.paymoney`                             |
| /setmoney   | `<player>` `<amount>` | `<player>`の所持金を`<amount>`に設定する | `tsukimiya.mooney.core.setmoney`                             |
| /givemoney  | `<player>` `<amount>` | `<player>`の所持金を`<amount>`増やす | `tsukimiya.mooney.core.givemoney`                            |
| /takemoney  | `<player>` `<amount>` | `<player>`の所持金を`<amount>`減らす | `tsukimiya.mooney.core.takemoney`                            |
| /topmoney | `<page>` | 所持金のランキングを表示する | `tsukimiya.mooney.core.topmoney` |
| /logmoney | `<page>` | 所持金のログを表示する | `tsukimiya.mooney.core.logmoney` |

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
// @param id UUID プレイヤーのUUID
val money: Int = MooneyCore.api.getMoney(id)
```

### 所持金を増やす
```kotlin
// @param money Long
// @param reason 理由(ログ用)
MooneyCore.api.depositMoney(id, money, reason)
```

### 所持金を減らす
```kotlin
MooneyCore.api.withdrawMoney(player, money, reason)
```

### 所持金を支払う
```kotlin
// @param from UUID 支払元 
// @param to   UUID 支払先
MooneyCore.api.payMoney(from, to, amount, fromReason, toReason)
```

### アカウント作成
```kotlin
// @param defaultMoney Int 初期所持金
MooneyCore.api.storeAccount(id, name, money, reason)
```

### その他
APIを見てください。  
[MooneyCoreAPI](https://github.com/TsukimiyaProject/MooneyCore/blob/master/src/main/kotlin/mc/tsukimiya/mooney/core/MooneyCoreAPI.kt)
```kotlin
MooneyCore.api
```
