package com.autolayer.app
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
class OverlayService : Service() {
    private lateinit var wm: WindowManager; private var v: View? = null; private var lp: WindowManager.LayoutParams? = null
    override fun onCreate() { super.onCreate(); wm = getSystemService(WINDOW_SERVICE) as WindowManager; show() }
    private fun show() {
        lp = WindowManager.LayoutParams(-2, -2, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT); lp!!.gravity = Gravity.TOP or Gravity.START; lp!!.x = 0; lp!!.y = 100
        v = LayoutInflater.from(this).inflate(R.layout.overlay_bubble, null)
        v?.findViewById<Button>(R.id.btn_record)?.setOnClickListener { AutoAccessibilityService.instance?.startRecording(); Toast.makeText(this, "Recording...", 0).show(); (it as Button).text = "STOP" }
        v?.findViewById<Button>(R.id.btn_record)?.setOnLongClickListener { if (AutoAccessibilityService.instance?.isRecording() == true) { AutoAccessibilityService.instance?.stopRecording(); val i = EditText(this); i.hint = "Macro name"; AlertDialog.Builder(this).setTitle("Save Macro").setView(i).setPositiveButton("Save") { _, _ -> val n = i.text.toString(); if (n.isNotEmpty()) { AutoAccessibilityService.instance?.saveRecording(n); v?.findViewById<Button>(R.id.btn_record)?.text = "REC"; Toast.makeText(this, "Saved", 0).show() } }.setNegativeButton("Discard") { _, _ -> AutoAccessibilityService.instance?.discardRecording(); v?.findViewById<Button>(R.id.btn_record)?.text = "REC" }.create().apply { window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY); show() } }; true }
        v?.findViewById<Button>(R.id.btn_play)?.setOnClickListener { startActivity(Intent(this, MacroListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        v?.setOnTouchListener(object : View.OnTouchListener { var ix=0; var iy=0; var itx=0f; var ity=0f; override fun onTouch(v: View, e: MotionEvent): Boolean { when (e.action) { MotionEvent.ACTION_DOWN -> { ix=lp!!.x; iy=lp!!.y; itx=e.rawX; ity=e.rawY; return true }; MotionEvent.ACTION_MOVE -> { lp!!.x = ix + (e.rawX - itx).toInt(); lp!!.y = iy + (e.rawY - ity).toInt(); wm.updateViewLayout(v, lp); return true } }; return false } })
        wm.addView(v, lp)
    }
    override fun onDestroy() { super.onDestroy(); if (v != null) wm.removeView(v) }
    override fun onBind(i: Intent?): IBinder? = null
}