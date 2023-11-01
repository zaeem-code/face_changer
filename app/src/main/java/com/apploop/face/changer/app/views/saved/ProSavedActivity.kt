package com.apploop.face.changer.app.views.saved

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.ActivityProSavedBinding
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.views.mainactivity.MainActivity

class ProSavedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProSavedBinding

    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProSavedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.background)
        init()
        path = intent.extras!!.getString("savedPath")!!
    }


    private fun init() {

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.closeIMG.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }
}