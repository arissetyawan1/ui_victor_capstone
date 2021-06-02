package com.android.victor.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.android.victor.R
import com.android.victor.databinding.ActivityHomeBinding
import com.android.victor.model.MessageModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


open class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var b: ActivityHomeBinding
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(b.root)
        setListener()
        checkCurrentUser()
    }

    private fun setListener() {
        b.fab.setOnClickListener(this)
    }

    private fun checkCurrentUser() {
        if (FirebaseAuth.getInstance().currentUser == null){
            resultLauncher.launch(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .build())
        } else {
            Toast.makeText(
                this,
                "Hello " + (FirebaseAuth.getInstance()
                    .currentUser
                    ?.displayName ?: ""),
                Toast.LENGTH_LONG
            )
                .show()
            displayMessage()
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK){
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
            displayMessage()
        } else {
            Toast.makeText(this, "Please try again later.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun displayMessage() {
        val mbase = FirebaseDatabase.getInstance().reference
        val optionList = FirebaseListOptions.Builder<MessageModel>()
            .setQuery(mbase, MessageModel::class.java)
            .build()
        val adapter = object : FirebaseListAdapter<MessageModel>(optionList) {
            override fun populateView(v: View, model: MessageModel, position: Int) {
                val name = findViewById<AppCompatTextView>(R.id.tvMessageName)
                val message = findViewById<AppCompatTextView>(R.id.tvMessage)
                val time = findViewById<AppCompatTextView>(R.id.tvTime)
                name.text = model.messageUser
                message.text = model.messageText
                time.text = model.messageTime
            }
        }
        b.listMessage.adapter = adapter
    }

    @SuppressLint("SimpleDateFormat")
    override fun onClick(v: View?) {
        if (v?.id == R.id.fab){
            val sdf = SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)")
            val currentDate = sdf.format(Date())
            val model = FirebaseAuth.getInstance()
                .currentUser
                ?.displayName?.let {
                    MessageModel(
                        b.etSendMessage.text.toString(),
                        it,
                        currentDate
                    )
                }
            FirebaseDatabase.getInstance()
                .reference
                .push()
                .setValue(model)
            b.etSendMessage.setText("")
        }
    }
}