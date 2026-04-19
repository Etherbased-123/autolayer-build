package com.autolayer.app
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
class MacroListActivity : AppCompatActivity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_macro_list)
        val lv = findViewById<ListView>(R.id.macro_list); fun ref() { lv.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MacroStorage.getMacros(this).map { "${it.name} (${it.actions.size})" }) }; ref()
        lv.setOnItemClickListener { _, _, p, _ -> AutoAccessibilityService.instance?.replayMacro(MacroStorage.getMacros(this)[p]); Toast.makeText(this, "Playing", 0).show(); finish() }
        lv.setOnItemLongClickListener { _, _, p, _ -> val m = MacroStorage.getMacros(this)[p]; AlertDialog.Builder(this).setTitle("Delete ${m.name}?").setPositiveButton("Delete") { _, _ -> MacroStorage.deleteMacro(this, m.name); ref() }.setNegativeButton("Cancel", null).show(); true }
    }
}