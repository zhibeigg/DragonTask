package com.github.zhibei

import com.github.zhibei.objective.dragoncore.DragonCustomPacket
import com.github.zhibei.objective.dragoncore.DragonKeyPress
import com.github.zhibei.objective.dragoncore.DragonKeyRelease
import com.github.zhibei.objective.planners.*
import eos.moe.dragoncore.api.CoreAPI
import eos.moe.dragoncore.api.event.KeyPressEvent
import ink.ptms.chemdah.api.ChemdahAPI
import ink.ptms.chemdah.api.event.collect.PluginReloadEvent
import ink.ptms.chemdah.core.quest.QuestLoader.register
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.warning
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object DragonTask : Plugin() {

    @Config("config.yml", migrate = true, autoReload = true)
    lateinit var config: Configuration

    override fun onEnable() {
        say("&6==================")
        say("&6DragonTask!&a启动！&cby.zhi_bei")
        reg()
        say("&6DragonTask!&a启动成功！&cby.zhi_bei")
        say("&6==================")
    }

    override fun onDisable() {
        say("&6==================")
        say("&6DragonTask!&a卸载成功！&cby.zhi_bei")
        say("&6==================")
    }

    fun reg() {
        //dragonCore
        reg(DragonKeyPress)
        reg(DragonKeyRelease)
        reg(DragonCustomPacket)
        //dragonPotion
        //reg(PotionsHookUp)
        //reg(UsePotion)
        //planners
        reg(PlayerSelectedJob)
        reg(PlayerCastSkill)
        reg(PlayerSkillUpgrade)
        reg(PlayerSkillBind)
        reg(PlayerTransfer)
        //重载
        ChemdahAPI.reloadAll()
    }

    fun reg(cs : ObjectiveCountableI<*>) {
        cs.register()
        say("&6CHEM拓展注册 ${cs.name} &a成功")
    }

    fun parse(s: String): String {
        return s.replace("&", "§").replace("§§", "&")
    }

    fun say(s: String?) {
        val sender: CommandSender = Bukkit.getConsoleSender()
        sender.sendMessage(s?.let { parse(it) })
    }

    fun debug(s: String?) {
        if (!config.getBoolean("debug")) return
        warning(s)
    }

    @SubscribeEvent
    fun e(e: KeyPressEvent) {
        debug(e.key)
    }

    @SubscribeEvent
    fun e(e: PluginReloadEvent.Quest) {
        val keys = mutableSetOf<String>()
        ChemdahAPI.questTemplate.forEach {
            it.value.taskMap.forEach { task ->
                if (task.value.objective.name == "dragoncore keypress" || task.value.objective.name == "dragoncore keyrelease" ) {
                    keys.add(task.value.condition["key"].toString())
                }
            }
        }
        keys.forEach {
            if (it != "null") {
                CoreAPI.registerKey(it)
                say("&a龙核按键&e$it&a注册成功")
            }
        }
    }

}