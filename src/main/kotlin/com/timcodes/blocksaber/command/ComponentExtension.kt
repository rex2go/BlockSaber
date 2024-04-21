package com.timcodes.blocksaber.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

public operator fun Component.plus(other: Component): Component {
    return this.append(other)
}

public fun mm(xml: String): Component {
    return MiniMessage.miniMessage().deserialize(xml)
}