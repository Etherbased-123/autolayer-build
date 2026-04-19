package com.autolayer.app
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
object MacroStorage {
    private val gson = Gson()
    fun saveMacro(c: Context, m: Macro) { val l = getMacros(c).toMutableList().apply { removeAll { it.name == m.name }; add(m) }; File(c.filesDir, "macros.json").writeText(gson.toJson(l)) }
    fun getMacros(c: Context): List<Macro> { val f = File(c.filesDir, "macros.json"); return if (!f.exists()) emptyList() else gson.fromJson(f.readText(), object : TypeToken<List<Macro>>() {}.type) ?: emptyList() }
    fun deleteMacro(c: Context, n: String) { val l = getMacros(c).filter { it.name != n }; File(c.filesDir, "macros.json").writeText(gson.toJson(l)) }
}