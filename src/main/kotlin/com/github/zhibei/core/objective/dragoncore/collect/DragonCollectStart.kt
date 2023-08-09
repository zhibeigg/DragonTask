package com.github.zhibei.core.objective.dragoncore.collect

import com.github.zhibei.core.objective.Loader
import com.github.zhibei.core.objective.Plugin
import com.ipedg.minecraft.dragoncollect.event.PlayerCollectStartEvent
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Plugin("DragonCollect")
object DragonCollectStart : ObjectiveCountableI<PlayerCollectStartEvent>() {

    override val name = "dragoncore collect start"
    override val event = PlayerCollectStartEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("name") { data, it ->
            it.collectEntity.modelName.contains(data.toString())
        }
    }
}