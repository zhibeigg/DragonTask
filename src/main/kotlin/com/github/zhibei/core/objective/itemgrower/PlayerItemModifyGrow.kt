package com.github.zhibei.core.objective.itemgrower

import com.gitee.grower.api.event.PlayerItemModifyGrowEvents
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("ItemGrower")
object PlayerItemModifyGrow : ObjectiveCountableI<PlayerItemModifyGrowEvents.Post>() {

    override val event: Class<PlayerItemModifyGrowEvents.Post> = PlayerItemModifyGrowEvents.Post::class.java
    override val name: String = "modify grow item"

    init {
        handler {
            it.player
        }
        addSimpleCondition("item") { data, it ->
            it.itemStack.itemMeta?.displayName?.contains(data.toString()) ?: false
        }
        addSimpleCondition("fortune") { data, it ->
            it.fortune.curse?.id == data.toString()
        }
        addSimpleCondition("perfect") { data, it ->
            it.perfect.curse?.id == data.toString()
        }
    }
}