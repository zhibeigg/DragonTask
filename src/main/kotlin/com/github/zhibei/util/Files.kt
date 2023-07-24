package com.github.zhibei.util

import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import java.io.File


fun files(path: String, defs: List<String>, callback: (File) -> Unit) {
    val file = File(getDataFolder(), path)
    if (!file.exists()) {
        defs.forEach { releaseResourceFile("$path/$it") }
    }
    getFiles(file).forEach { callback(it) }
}

fun getFiles(file: File): List<File> {
    val listOf = mutableListOf<File>()
    when (file.isDirectory) {
        true -> listOf += file.listFiles()!!.flatMap { getFiles(it) }
        false -> {
            listOf += file
        }
    }
    return listOf
}

fun File.toYamlName(): String {
    return name.replace(".yml", "")
}

fun File.toJsonName(): String {
    return name.replace(".json", "")
}
