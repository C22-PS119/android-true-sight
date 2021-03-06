package com.truesightid.utils

object StringSeparatorUtils {
    fun separateVoteResponse(votes: String?): HashMap<Int, Int> {
        if (votes == null)
            return HashMap()
        val result = HashMap<Int, Int>()
        val ans = votes.split(",")
        for (text in ans) {
            val split = text.split(":")
            if (split.isNotEmpty())
                result[split[0].toInt()] = split[1].toInt()
        }
        return result
    }

    fun separateBookmarkResponse(bookmark: String?): ArrayList<Int> {
        if (bookmark == null)
            return ArrayList()
        val result = ArrayList<Int>()
        val ans = bookmark.split(",")
        if (ans.isNotEmpty()) {
            for (item in ans) {
                result.add(item.toInt())
            }
        }
        return result
    }

    fun separateUrlResponse(url: String?): ArrayList<String> {
        if (url == null)
            return ArrayList()
        val result = ArrayList<String>()
        val filter = url.filterNot { it.isWhitespace() }
        val ans = filter.split(",")
        if (ans.isNotEmpty()) {
            for (item in ans) {
                result.add(item)
            }
        }
        return result
    }
}