package com.github.zhibei.core.objective.originattribute

import ac.github.oa.api.event.item.ItemDecomposeEvent
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("OriginAttribute")
object ItemDecompose : ObjectiveCountableI<ItemDecomposeEvent.Post>() {

    override val event: Class<ItemDecomposeEvent.Post> = ItemDecomposeEvent.Post::class.java
    override val name: String = "decompose item"

    init {
        handler {
            it.player
        }
        addSimpleCondition("products") { data, it ->
            var var1 = false
            it.products.map { it.itemMeta?.displayName ?: "" }.forEach {
                if (data.asList().contains(it)) {
                    var1 = true
                    return@forEach
                }
            }
            var1
        }
        addSimpleCondition("item") { data, it ->
            it.itemStack.itemMeta?.displayName?.contains(data.toString()) ?: false
        }
    }
}