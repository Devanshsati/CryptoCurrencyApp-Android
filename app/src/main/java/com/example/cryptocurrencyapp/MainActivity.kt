package com.example.cryptocurrencyapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.cryptocurrencyapp.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import java.util.Locale

@Suppress("ObjectLiteralToLambda", "RedundantSamConstructor")
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var rvAdapter : RvAdapter
    private val data: ArrayList<Modal> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvAdapter = RvAdapter(this, data)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = rvAdapter
        apiData()

        binding.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val filterData = ArrayList<Modal>()
                for(item in data){
                    if(item.name.lowercase(Locale.getDefault()).contains(s.toString().lowercase(Locale.getDefault()))){
                        filterData.add(item)
                    }
                }
                if(filterData.isEmpty()) {
                    Toast.makeText(this@MainActivity, "No data available", Toast.LENGTH_LONG).show()
                }else{
                    rvAdapter.changeData(filterData)
                }
            }
        })
    }

    private fun apiData() {
        val url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=INR&order=market_cap_desc&per_page=20"
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest( Request.Method.GET, url, null, object : Response.Listener<JSONArray> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(response: JSONArray?) {
                    binding.progressBar.isVisible = false
                    try {
                        response?.let {
                            for (i in 0 until it.length()) {
                                val jsonObject = it.getJSONObject(i)
                                val symbol = jsonObject.getString("symbol")
                                val name = jsonObject.getString("name")
                                val price = String.format("₹ "+"%.2f K", (jsonObject.getDouble("current_price")/1000))
                                val image = jsonObject.getString("image")
                                val marketCapRank = jsonObject.getInt("market_cap_rank")
                                val marketCap = String.format("₹ "+"%.2f Cr", (jsonObject.getDouble("market_cap")/10000000))

                                data.add(Modal(name, symbol, price, image, marketCapRank.toString(), marketCap))
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