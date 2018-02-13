package com.github.putraxor.themovie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject
import android.widget.ArrayAdapter


class MainActivity : AppCompatActivity() {

    private var tag: String = MainActivity::class.java.simpleName
    private lateinit var popular: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        popular = findViewById(R.id.popular)
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }


    private fun loadData() {
        AndroidNetworking.get("http://api.themoviedb.org/3/movie/popular?api_key=57d587bc209aca4798423b2f24e1b933")
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        val result = response.getJSONArray("results")
                        val data = ArrayList<String>()
                        (0 until result.length() - 1).mapTo(data) { result.getJSONObject(it).getString("title") }
                        generateAdapter(data)
                        Log.d(tag, result.toString(2))
                    }

                    override fun onError(error: ANError) {
                        Log.e(tag, "Error", error)
                        Toast.makeText(applicationContext, "Oops error ${error.message}", Toast.LENGTH_LONG).show()
                    }
                })
    }


    private fun generateAdapter(data: List<String>) {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        popular.adapter = adapter
    }
}
