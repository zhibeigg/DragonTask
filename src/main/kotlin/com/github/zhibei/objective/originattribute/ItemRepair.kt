package com.github.zhibei.objective.originattribute

import ac.github.oa.api.event.item.ItemRepairEvent
import com.github.zhibei.objective.Loader
import com.github.zhibei.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("OriginAttribute")
object ItemRepair : ObjectiveCountableI<ItemRepairEvent.Post>() {

    override val event: Class<ItemRepairEvent.Post> = ItemRepairEvent.Post::class.java
    override val name: String = "repair item"

    init {
        handler {
            it.player
        }
        addSimpleCondition("money") { data, it ->
            data.toDouble() <= it.money
        }
        addSimpleCondition("point") { data, it ->
            data.toDouble() <= it.points
        }
        addSimpleCondition("item") { data, it ->
            it.itemStack.itemMeta?.displayName?.contains(data.toString()) ?: false
        }
    }
}