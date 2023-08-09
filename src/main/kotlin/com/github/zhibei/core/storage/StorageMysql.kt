package com.github.zhibei.core.storage

import com.github.zhibei.DragonTask.config
import com.github.zhibei.core.pojo.Report
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.util.*

class StorageMysql : Storage {

    val host = config.getHost("database.sql")
    companion object {

        const val ID = "id"
        const val UUID = "uuid"

        const val PLAYER = "player"
        const val REPORTER = "reporter"

        const val WIDTH = "width"

        const val FLYER = "flyer"
        const val KILLER = "killer"
        const val BLOCKER = "blocker"
        const val FUCKER = "fucker"
        const val INFO = "info"

        const val TIME = "time"

    }

    val userTable: Table<*, *> = Table("dragon_task_user", host) {
        add(ID) { id() }
        add(UUID) { type(ColumnTypeSQL.VARCHAR, 36) }
        add(WIDTH) {
            type(ColumnTypeSQL.DOUBLE) {
                def(1.5)
            }
        }
    }

    val reportTable: Table<*, *> = Table("dragon_task_report", host) {
        add(ID) { id() }
        add(REPORTER) { type(ColumnTypeSQL.VARCHAR, 36) }
        add(PLAYER) { type(ColumnTypeSQL.VARCHAR, 36) }
        add(UUID) { type(ColumnTypeSQL.VARCHAR, 36) }
        add(FLYER) { type(ColumnTypeSQL.BOOLEAN) }
        add(KILLER) { type(ColumnTypeSQL.BOOLEAN) }
        add(BLOCKER) { type(ColumnTypeSQL.BOOLEAN) }
        add(FUCKER) { type(ColumnTypeSQL.BOOLEAN) }
        add(INFO) { type(ColumnTypeSQL.VARCHAR, 128) }
        add(TIME) { type(ColumnTypeSQL.BIGINT) }
    }

    val dataSource by lazy { host.createDataSource() }

    init {
        userTable.createTable(dataSource)
        reportTable.createTable(dataSource)
    }

    fun createUser(player: Player) {
        val uuid = userTable.select(dataSource) {
            where { UUID eq player.uniqueId.toString() }
            rows(UUID)
        }.firstOrNull { getString(UUID) }

        if (uuid == null) {
            userTable.insert(dataSource, UUID, WIDTH) {
                value(player.uniqueId.toString(), 1.5)
            }
        }
    }

    override fun getUIWidth(player: Player): Double {
        return userTable.select(dataSource) {
            where { UUID eq player.uniqueId.toString() }
            rows(WIDTH)
        }.firstOrNull {
            getDouble(WIDTH)
        } ?: 1.5
    }

    override fun setUIWidth(player: Player, width: Double) {
        userTable.update(dataSource) {
            where { UUID eq player.uniqueId.toString() }
            set(WIDTH, width)
        }
    }

    override fun getReports(player: Player): List<Report> {
        return reportTable.select(dataSource) {
            where { PLAYER eq player.uniqueId.toString() }
            rows(REPORTER, UUID, FLYER, KILLER, BLOCKER, FUCKER, INFO, TIME)
        }.mapNotNull {
            val uuid = java.util.UUID.fromString(getString(UUID))!!
            val option = listOf(getBoolean(FLYER), getBoolean(KILLER), getBoolean(BLOCKER), getBoolean(FUCKER))
            val reporter = Bukkit.getPlayer(java.util.UUID.fromString(getString(REPORTER))!!)
            Report(uuid, reporter, player, getString(INFO), option, getLong(TIME))
        }
    }

    override fun removeReport(report: UUID) {
        reportTable.delete(dataSource) {
            where { UUID eq report.toString() }
        }
    }

    override fun saveReport(report: Report) {
        reportTable.insert(dataSource, REPORTER, PLAYER, UUID, FLYER, KILLER, BLOCKER, FUCKER, INFO, TIME) {
            value(
                report.reporter?.uniqueId ?: report.cheater.uniqueId,
                report.cheater.uniqueId,
                report.uuid,
                report.flyer,
                report.killer,
                report.blocker,
                report.blocker,
                report.fucker,
                report.reportInfo,
                report.time,
                1.5
            )
        }
    }

}