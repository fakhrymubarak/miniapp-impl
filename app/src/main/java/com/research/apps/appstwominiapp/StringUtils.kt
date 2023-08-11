package com.research.apps.appstwominiapp

fun String.capitalizeEachWord(): String {
    val words = split(" ")
    val sb = StringBuilder()
    words.forEach {
        if (it != "") {
            sb.append(it[0].uppercase()).append(it.substring(1))
        }
        sb.append(" ")
    }

    return sb.toString().trim { it <= ' ' }
}

fun capitalizeEachWord(str: String): String {
    val words = str.split(" ")
    val sb = StringBuilder()
    words.forEach {
        if (it != "") {
            sb.append(it[0].uppercase()).append(it.substring(1))
        }
        sb.append(" ")
    }

    return sb.toString().trim { it <= ' ' }
}
