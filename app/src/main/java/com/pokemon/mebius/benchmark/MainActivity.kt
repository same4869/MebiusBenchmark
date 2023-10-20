package com.pokemon.mebius.benchmark

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "SoraBenchMarkUtils, PhoneLevel:${MebiusBenchMarkUtils.getPhoneLevel(this)}")
        findViewById<TextView>(R.id.tvGetLevel).setOnClickListener {
            Toast.makeText(this, "手机分级：${MebiusBenchMarkUtils.getPhoneLevel(this)}", Toast.LENGTH_SHORT).show()
        }
    }
}