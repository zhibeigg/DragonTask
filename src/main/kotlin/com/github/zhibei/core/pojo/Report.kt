package com.github.zhibei.core.pojo

import org.bukkit.entity.Player
import java.util.*

class Report(val uuid: UUID, val reporter: Player?, val cheater: Player, val reportInfo: String, val option: List<Boolean>, val time: Long) {

    val flyer: Boolean = option[0]
    val killer: Boolean = option[1]
    val blocker: Boolean = option[2]
    val fucker: Boolean = option[3]

}