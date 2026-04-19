package com.autolayer.app
data class MacroAction(val type: String, val x: Float = 0f, val y: Float = 0f, val x2: Float = 0f, val y2: Float = 0f, val duration: Long = 100, val text: String = "", val packageName: String = "")
data class Macro(val name: String, val actions: List<MacroAction>)