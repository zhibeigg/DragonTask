package com.github.zhibei.core.objective.itemgrower

import com.gitee.grower.api.event.PlayerIdentifyEvents
import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("ItemGrower")
object PlayerIdentify : ObjectiveCountableI<PlayerIdentifyEvents.Post>() {

    override val event: Class<PlayerIdentifyEvents.Post> = PlayerIdentifyEvents.Post::class.java
    override val name: String = "identify item"

    init {
        handler {
            it.player
        }
        addSimpleCondition("item") { data, it ->
            it.before.itemMeta?.displayName?.contains(data.toString()) ?: false
        }
    }
}