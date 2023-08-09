package com.github.zhibei.api

import com.github.zhibei.DragonTask.config
import com.github.zhibei.core.pojo.Report
import com.github.zhibei.core.storage.Storage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.Schedule
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import java.util.*

object ReportManager {

    val saveDays: Int
        get() = config.getInt("保存时间")

    @Schedule(async = true, period = 20)
    fun sch(){
        val beforeTime = System.currentTimeMillis() - saveDays*24*3600*100
        Bukkit.getOnlinePlayers().forEach {
            clearReport(beforeTime, it)
        }
    }

    fun clearReport(beforeTime: Long, player: Player) {
        getReports(player).forEach {
            if (it.time <= beforeTime) {
                if (Bukkit.isPrimaryThread()) {
                    removeReport(it)
                } else {
                    submit(async = false) { removeReport(it) }
                }
            }
        }
    }

    fun getTimesOfReports(player: Player): Int {
        return getReports(player).size
    }

    fun saveReport(report: Report) {
        submitAsync {
            Storage.INSTANCE.saveReport(report)
        }
    }

    fun getReports(player: Player): List<Report> {
        return Storage.INSTANCE.getReports(player)
    }

    fun removeReport(report: Report) {
        submitAsync {
            Storage.INSTANCE.removeReport(report.uuid)
        }
    }

    fun removeReport(uuid: UUID) {
        submitAsync {
            Storage.INSTANCE.removeReport(uuid)
        }
    }

}