package com.github.zhibei.storage

import com.github.zhibei.DragonTask.config
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

interface Storage {

    companion object {

        val INSTANCE by lazy {
            when (type) {
                "LOCAL", "yml" -> StorageLocal()
                "MYSQL", "sql" -> StorageMysql()
                else -> error("找不到存储方式")
            }
        }

        val type: String
            get() = config.getString("database.use", "LOCAL")!!

        @SubscribeEvent
        fun e(e: PlayerJoinEvent) {
            if (type in listOf("MYSQL", "sql")) {
                (INSTANCE as? StorageMysql)?.createUser(e.player)
            }
        }

    }

    /**存储ui大小**/
    fun setUIWidth(player: Player, width: Double)

    /**获取ui大小**/
    fun getUIWidth(player: Player): Double

}