package com.github.zhibei.objective.dragoncore.collect

import com.github.zhibei.objective.dragoncore.Loader
import com.ipedg.minecraft.dragoncollect.event.PlayerCollectEndEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Loader
@Dependency("DragonCollect")
object DragonCollectEnd : ObjectiveCountableI<PlayerCollectEndEvent>() {

    override val name = "dragoncore collect end"
    override val event = PlayerCollectEndEvent::class.java

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