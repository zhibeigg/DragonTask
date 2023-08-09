package com.github.zhibei.core.storage

import com.github.zhibei.core.pojo.Report
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration
import java.util.*

class StorageLocal : Storage {

    override fun getUIWidth(player: Player): Double {
        return if (getData(player)["width"] == null) {
            1.5
        } else {
            getData(player)["width"].toString().toDouble()
        }
    }

    override fun setUIWidth(player: Player, width: Double) {
        val data = getData(player)
        data["width"] = width
        data.saveToFile(newFile(getDataFolder(), "/save/${player.uniqueId}.yml"))
    }


    fun getData(player: Player) : Configuration {
        return Configuration.loadFromFile(newFile(getDataFolder(), "/save/${player.uniqueId}.yml"))
    }

    override fun getReports(player: Player): List<Report> {
        TODO("Not yet implemented")
    }

    override fun removeReport(report: UUID) {
        TODO("Not yet implemented")
    }

    override fun saveReport(report: Report) {
        TODO("Not yet implemented")
    }

}