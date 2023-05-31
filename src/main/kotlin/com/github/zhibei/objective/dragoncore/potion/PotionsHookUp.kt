package com.github.zhibei.objective.dragoncore.potion

import com.ipedg.minecraft.dragonpotions.event.PotionsHookUpEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("DragonPotions")
object PotionsHookUp : ObjectiveCountableI<PotionsHookUpEvent>() {

    override val name = "dragoncore hook up potion"
    override val event = PotionsHookUpEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("name") { data, it ->
            data.toString() == it.potionName
        }
        addSimpleCondition("type") { data, it ->
            data.toString() == it.potionType
        }
    }
}