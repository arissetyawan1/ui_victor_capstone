package com.android.victor.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.victor.api.DaggerApiComponent
import com.android.victor.api.UsersApi
import com.android.victor.databinding.ActivityHomeBinding
import com.android.victor.model.Data.generateDataAdditional
import com.android.victor.model.Data.generateDataBody
import com.android.victor.model.Data.generateDataCategory
import com.android.victor.model.Data.generateDataDigestion
import com.android.victor.model.Data.generateDataEyes
import com.android.victor.model.Data.generateDataMental
import com.android.victor.model.Data.generateDataSensation
import com.android.victor.model.Data.generateDataSkin
import com.android.victor.model.Data.generateDataTht
import com.android.victor.model.MessageModel
import com.android.victor.model.PredictResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity(), MessageAdapter.OnItemClick {

    @Inject
    lateinit var usersApi: UsersApi
    init {
        DaggerApiComponent.create().inject(this)
    }

    private var items: ArrayList<String> = arrayListOf()

    private lateinit var b: ActivityHomeBinding
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(b.root)
        initiate()
        setupRecyclerView()
    }

    private fun initiate() {
        messageAdapter = MessageAdapter(arrayListOf())
    }

    private fun setupRecyclerView() {
        messageAdapter.addSingleMessage(
            MessageModel("Hello, my name is Victor, and I will help you to find out your disease. Tell me what your symptoms are. Do you want to start?", textLength = "Long")
        )
        messageAdapter.addSingleMessage(
            MessageModel("Start", messageId = "start")
        )
        messageAdapter.addSingleMessage(
            MessageModel("Close", messageId = "close")
        )
        b.rvMessage.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity).apply {
                stackFromEnd = true
            }
        }
        messageAdapter.listener = this
    }

    override fun onMessageClick(message: MessageModel) {
        when(message.messageId){
            "start" -> showCategories(true)
            "close" -> close()
            "finish" -> checkout()
            "back" -> showCategories(false)
            "body" -> categories(message)
            "digestion" -> categories(message)
            "eyes" -> categories(message)
            "mental" -> categories(message)
            "sensation" -> categories(message)
            "skin" -> categories(message)
            "tht" -> categories(message)
            "additional" -> categories(message)
            else -> addItems(message)
        }
    }

    private fun showCategories(isFirstTime: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (isFirstTime){
                messageAdapter.addSingleMessage(MessageModel(messageText = "Choose which part has symptoms of the disease?", textLength = "Long"))
            } else {
                messageAdapter.addSingleMessage(MessageModel(messageText = "Is there anything else?", textLength = "Long"))
            }
            messageAdapter.addItemMessage(generateDataCategory())
            b.rvMessage.smoothScrollToPosition(messageAdapter.countItemMessage())
        }, 1000)
    }

    private fun close() {
        this.finishAffinity()
    }

    private fun categories(message: MessageModel) {
        Handler(Looper.getMainLooper()).postDelayed({
            messageAdapter.addSingleMessage(
                MessageModel(message.messageText, "user")
            )
            b.rvMessage.smoothScrollToPosition(messageAdapter.countItemMessage())
        }, 1000)
        Handler(Looper.getMainLooper()).postDelayed({
            messageAdapter.addSingleMessage(
                MessageModel("What are your complaints about your ${message.messageId}?", "victor", textLength = "Long")
            )
            b.rvMessage.smoothScrollToPosition(messageAdapter.countItemMessage())
            when(message.messageId){
                "body" -> messageAdapter.addItemMessage(generateDataBody())
                "digestion" -> messageAdapter.addItemMessage(generateDataDigestion())
                "eyes" -> messageAdapter.addItemMessage(generateDataEyes())
                "mental" -> messageAdapter.addItemMessage(generateDataMental())
                "sensation" -> messageAdapter.addItemMessage(generateDataSensation())
                "skin" -> messageAdapter.addItemMessage(generateDataSkin())
                "tht" -> messageAdapter.addItemMessage(generateDataTht())
                "additional" -> messageAdapter.addItemMessage(generateDataAdditional())
            }
        }, 1000)
    }

    private fun addItems(message: MessageModel) {
        if (items.size <= 7){
            items.add(message.messageId)
            Log.e("TAG", "DATA : ${items.size}")
            showCategories(false)
        } else {
            Toast.makeText(this, "You have given enough symptoms. So, click finish button below!", Toast.LENGTH_LONG).show()
            showCategories(false)
        }
    }

    private fun checkout() {
        if (items.size != 0){
            if (items.size <= 7){
                for(item in 1..6){
                    items.add(item, "0")
                }
                postData()
            } else {
                postData()
            }
        } else {
            Toast.makeText(this, "Choose at least one symptoms!", Toast.LENGTH_LONG).show()
        }
    }

    private fun postData() {
        usersApi.predict(items).enqueue(object : Callback<PredictResponse>{
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                Log.e("TAG","RESPONSE: ${response.code()}")
                Log.e("TAG","RESPONSE: ${response.message()}")
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Log.e("TAG","RESPONSE: ${t.localizedMessage}")
            }
        })
    }
}