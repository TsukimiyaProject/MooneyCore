package mc.tsukimiya.mooney.core

data class PagedResult<T>(val items: List<T>, val currentPage: Int, val totalPages: Int)
