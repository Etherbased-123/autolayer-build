package com.autolayer.app
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_start).setOnClickListener { if (!Settings.canDrawOverlays(this)) { startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 0); Toast.makeText(this, "Enable overlay", 1).show() } else { startService(Intent(this, OverlayService::class.java)); startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) } }
        findViewById<Button>(R.id.btn_macros).setOnClickListener { startActivity(Intent(this, MacroListActivity::class.java)) }
    }
    override fun onActivityResult(r: Int, c: Int, d: Intent?) { super.onActivityResult(r, c, d); if (r == 0 && Settings.canDrawOverlays(this)) { startService(Intent(this, OverlayService::class.java)); startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) } }
}