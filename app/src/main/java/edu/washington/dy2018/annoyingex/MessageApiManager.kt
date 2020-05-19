package edu.washington.dy2018.annoyingex

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MessageApiManager(private val context: Context) {
    private val queue: RequestQueue = Volley.newRequestQueue(context)
    var listOfMessages: List<String>

    init {
        listOfMessages = listOf("unable to retrieve message")

        this.getListOfMessages({messageList ->
            listOfMessages = messageList
        }, {})
    }

    fun getListOfMessages(onMessageListReady: (List<String>) -> Unit, onError: (() -> Unit)? = null) {
        val messageListUrl = "https://raw.githubusercontent.com/echeeUW/codesnippets/master/ex_messages.json"

        val request = StringRequest(
            Request.Method.GET, messageListUrl,
            { response ->
                // success
                val gson = Gson()
                Log.i("dy", "gson succeed")
                val allMessages = gson.fromJson(response, AllMessages::class.java)
                Log.i("dy", "all Messages received")
                onMessageListReady(allMessages.messages)
            }, {
                Log.i("dy", "Fetch failed")
            }
        )

        queue.add(request)
    }
}