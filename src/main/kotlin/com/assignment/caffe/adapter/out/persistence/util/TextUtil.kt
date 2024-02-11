package com.assignment.caffe.adapter.out.persistence.util

fun textFarsing(str: String): String {
    var tmpstr = ""
    var j = 0
    val i = str.length

    while (j < i) {
        val col1 = str[j].toString()

        tmpstr += when {
            col1 < "ㄱ" -> col1
            'ㄱ'.code <= col1[0].code && col1[0].code <= 'ㅎ'.code -> col1
            col1 < "까" -> "ㄱ"
            col1 < "나" -> "ㄲ"
            col1 < "다" -> "ㄴ"
            col1 < "따" -> "ㄷ"
            col1 < "라" -> "ㄸ"
            col1 < "마" -> "ㄹ"
            col1 < "바" -> "ㅁ"
            col1 < "빠" -> "ㅂ"
            col1 < "사" -> "ㅃ"
            col1 < "싸" -> "ㅅ"
            col1 < "아" -> "ㅆ"
            col1 < "자" -> "ㅇ"
            col1 < "짜" -> "ㅈ"
            col1 < "차" -> "ㅉ"
            col1 < "카" -> "ㅊ"
            col1 < "타" -> "ㅋ"
            col1 < "파" -> "ㅌ"
            col1 < "하" -> "ㅍ"
            col1 <= "힣" -> "ㅎ"
            else -> col1
        }

        j++
    }

    return tmpstr
}
