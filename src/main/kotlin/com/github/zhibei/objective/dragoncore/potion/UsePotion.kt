package com.github.zhibei.objective.dragoncore.potion

import com.ipedg.minecraft.dragonpotions.event.UsePotionEvent
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI

@Dependency("DragonPotions")
object UsePotion : ObjectiveCountableI<UsePotionEvent>() {

    override val name = "dragoncore use potion"
    override val event = UsePotionEvent::class.java

    override val isAsync = true

    init {
        handler {
            it.player
        }
        addSimpleCondition("name") { data, it ->
            it.potionName.contains(data.toString())
        }
        addSimpleCondition("type") { data, it ->
            data.toString() == it.potionType
        }
    }
}