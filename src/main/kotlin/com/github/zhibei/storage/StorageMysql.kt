package com.github.zhibei.storage

import com.github.zhibei.DragonTask.config
import org.bukkit.entity.Player
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost

class StorageMysql : Storage {

    val host = config.getHost("database.sql")
    companion object {

        const val ID = "id"
        const val UUID = "uuid"

        const val WIDTH = "width"

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

    val dataSource by lazy { host.createDataSource() }

    init {
        userTable.createTable(dataSource)
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

}