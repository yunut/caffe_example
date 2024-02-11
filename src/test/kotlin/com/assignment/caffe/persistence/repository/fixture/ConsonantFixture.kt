package com.assignment.caffe.persistence.repository.fixture

import com.assignment.caffe.application.domain.model.Consonant

inline fun baseConsonantBuild(block: BaseConsonantBuilder.() -> Unit = {}) =
    BaseConsonantBuilder().apply(block).build()
class BaseConsonantBuilder {

    var name: String = "상품명"
    var frontConsonant: String = "ㅅㅍㅁ"

    fun build(): Consonant = Consonant.of(
        name = name,
        frontConsonant = frontConsonant,
    )
}
