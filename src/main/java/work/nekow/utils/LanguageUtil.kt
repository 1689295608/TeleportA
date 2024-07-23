package work.nekow.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import work.nekow.plugin
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.Objects

class LanguageUtil(path: Path, file: Path) {
    private var language: JsonObject

    init {
        if (!Files.exists(file)) {
            Files.createDirectories(path)
            plugin.getResource("languages.json").use {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val str: String = it!!.reader(StandardCharsets.UTF_8).readText()
                val languages = gson.fromJson(str, JsonObject::class.java)
                var locale = System.getProperty("user.language")
                if (!languages.has(locale)) {
                    locale = "zh"
                }
                language = languages[locale].asJsonObject
                Files.writeString(file, gson.toJson(language), StandardCharsets.UTF_8, StandardOpenOption.CREATE)
            }
        }
        language = Gson().fromJson(String(Files.readAllBytes(file)), JsonObject::class.java)
    }

    fun language(key: String, vararg args: Any?): String {
        return language[key].asString.format(*args)
    }
}