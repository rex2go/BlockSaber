package com.timcodes.blocksaber

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType


public fun <T, Z : Any> Entity.getPersistentData(key: String, type: PersistentDataType<T, Z>): Z? {
    val namespacedKey = NamespacedKey(BlockSaber.instance, key)
    return this.persistentDataContainer.get(namespacedKey, type)
}

public fun <T, Z : Any> Entity.setPersistentData(key: String, type: PersistentDataType<T, Z>, value: Z) {
    val namespacedKey = NamespacedKey(BlockSaber.instance, key)
    this.persistentDataContainer.set(namespacedKey, type, value)
}