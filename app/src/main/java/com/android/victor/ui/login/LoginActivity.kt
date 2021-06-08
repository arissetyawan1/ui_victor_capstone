package com.android.victor.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.victor.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}