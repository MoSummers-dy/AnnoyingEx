package edu.washington.dy2018.annoyingex

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MessageApiManager(context: Context) {
    private val queue: RequestQueue = Volley.newRequestQueue(context)
    var listOfMessages: List<String>

    init {
        listOfMessages = listOf("unable to retrieve message")

        this.getListOfMessages { messageList ->
            listOfMessages = messageList
        }
    }

    fun getListOfMessages(onMessageListReady: (List<String>) -> Unit) {
        val messageListUrl = "https://raw.githubusercontent.com/echeeUW/codesnippets/master/ex_messages.json"

        val request = StringRequest(
            Request.Method.GET, messageListUrl,
            { response ->
                // success
                val gson = Gson()
                val allMessages = gson.fromJson(response, AllMessages::class.java)
                onMessageListReady(allMessages.messages)
            }, {}
        )

        queue.add(request)
    }
}