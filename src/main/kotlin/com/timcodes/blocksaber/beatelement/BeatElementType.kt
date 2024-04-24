package com.timcodes.blocksaber.beatelement

import kotlin.reflect.KClass

enum class BeatElementType(val code: String, val kClass: KClass<out AbstractBeatElement>?) {
    RED("R", RedBeatElement::class),
    BLUE("B", BlueBeatElement::class),
    EXPLOSION("X", ExplosionBeatElement::class),
    SPEED("SP", null),
    PARTICLE("PA", null);

    companion object {
        fun fromCode(code: String) = BeatElementType.entries.first { it.code == code }
    }
}