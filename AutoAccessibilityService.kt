package com.autolayer.app
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
class AutoAccessibilityService : AccessibilityService() {
    companion object { var instance: AutoAccessibilityService? = null }
    private val rec = mutableListOf<MacroAction>(); private var isRec = false
    override fun onServiceConnected() { instance = this; Toast.makeText(this, "AutoLayer Online", 0).show() }
    fun startRecording() { rec.clear(); isRec = true }
    fun stopRecording() { isRec = false }
    fun isRecording(): Boolean = isRec
    fun saveRecording(n: String) { MacroStorage.saveMacro(this, Macro(n, rec.toList())); rec.clear() }
    fun discardRecording() { rec.clear() }
    override fun onAccessibilityEvent(e: AccessibilityEvent?) { if (isRec && e?.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) { val b = e.source?.boundsInScreen ?: return; rec.add(MacroAction(type = "TAP", x = b.centerX().toFloat(), y = b.centerY().toFloat())) } }
    fun replayMacro(m: Macro) { Thread { for (a in m.actions) { when (a.type) { "TAP" -> { val p = Path(); p.moveTo(a.x, a.y); dispatchGesture(GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(p, 0, 50)).build(), null, null) }; "WAIT" -> Thread.sleep(a.duration); "LAUNCH_APP" -> { val i = packageManager.getLaunchIntentForPackage(a.packageName); i?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i) }; "WAIT_TEXT" -> { var f = false; var t = 0; while (!f && t < 20) { if (findText(a.text)) f = true else Thread.sleep(500); t++ } } }; Thread.sleep(300) } }.start() }
    private fun findText(t: String): Boolean { val r = rootInActiveWindow ?: return false; return findNode(r, t) != null }
    private fun findNode(n: AccessibilityNodeInfo, t: String): AccessibilityNodeInfo? { if (n.text?.toString()?.contains(t, true) == true) return n; for (i in 0 until n.childCount) { val c = n.getChild(i) ?: continue; val r = findNode(c, t); if (r != null) return r }; return null }
    override fun onInterrupt() {}
}