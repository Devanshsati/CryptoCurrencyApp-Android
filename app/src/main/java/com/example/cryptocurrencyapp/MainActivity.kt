package com.example.cryptocurrencyapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.cryptocurrencyapp.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException

@Suppress("RedundantSamConstructor", "ObjectLiteralToLambda")
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var rvAdapter : RvAdapter
    private lateinit var data : ArrayList<Modal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        apiData()
        data = ArrayList()
        rvAdapter = RvAdapter(this, data)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = rvAdapter
    }

    private fun apiData() {
        val url =
            "https://api.coingecko.com/api/v3/coins/markets?vs_currency=INR&order=market_cap_desc&per_page=5"
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest( Request.Method.GET, url, null, object : Response.Listener<JSONArray> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(response: JSONArray?) {
                    try {
                        response?.let {
                            for (i in 0 until it.length()) {
                                val jsonObject = it.getJSONObject(i)
                                val symbol = jsonObject.getString("symbol")
                                val name = jsonObject.getString("name")
                                val price = jsonObject.getDouble("current_price")
                                val image = jsonObject.getString("image")
                                val marketCapRank = jsonObject.getInt("market_cap_rank")

                                data.add(Modal(name, symbol, price.toString(),image,marketCapRank))
                            }
                            rvAdapter.notifyDataSetChanged()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@MainActivity, "JSON Exception: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonArrayRequest)
    }
}