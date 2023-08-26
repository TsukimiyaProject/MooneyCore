package mc.tsukimiya.mooney.core.domain

import mc.tsukimiya.mooney.core.exception.InvalidMinecraftIdException

/**
 * マイクラIDのクラス
 *
 * @property value
 */
data class Name(val value: String) {
    init {
        // マイクラIDの条件
        // 英数字(A-Z、a-z、0-9)とアンダースコア(_)
        // Floodgateプラグインで統合版には接頭辞にピリオド(.)が付く
        val pattern = """^[A-Za-z0-9_.]{3,16}$"""

        // パターンチェック
        // 条件に当てはまらなかったら例外投げる
        if (!Regex(pattern).matches(value)) {
            throw InvalidMinecraftIdException("Wrong name : $value")
        }
    }
}
