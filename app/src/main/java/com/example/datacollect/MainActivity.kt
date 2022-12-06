package com.example.datacollect

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast", "SuspiciousIndentation")
    lateinit var bAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val battery_Button = findViewById<Button>(R.id.battery)
        val wifi_button = findViewById<Button>(R.id.check_wifi)
        val gprs_button = findViewById<Button>(R.id.check_gprs)
        val bt_button = findViewById<Button>(R.id.check_bt)


        battery_Button.setOnClickListener{
            val BM = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
            val batLevel:Int = BM.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            val showBattery = findViewById<TextView>(R.id.battery_text)
            showBattery.text = batLevel.toString()
        }

        wifi_button.setOnClickListener {

            val showWifi = findViewById<TextView>(R.id.wifi_text)
            if (checkForWifi(this))
                showWifi.text = "Connected"
            else
                showWifi.text = "Not Connected"
        }

        gprs_button.setOnClickListener {
            val showGPRS = findViewById<TextView>(R.id.gprs_status)
            if(checkForGPRS(this))
                showGPRS.text = "Connected"
            else
                showGPRS.text = "Not Connected"
        }

        bt_button.setOnClickListener {
            bAdapter = BluetoothAdapter.getDefaultAdapter()
            val showBT = findViewById<TextView>(R.id.BT_text)
            if( bAdapter.isEnabled)
                showBT.text = "BT is enabled"
            else
                showBT.text = "BT is disabled"
        }
    }

    private fun checkForGPRS(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = cm.activeNetwork ?: return false
            val activeNetwork = cm.getNetworkCapabilities(network) ?: return false
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        else{
            @Suppress("DEPRECATION") val networkInfo = cm.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION") return networkInfo.isConnected
        }
    }

    private fun checkForWifi(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = cm.activeNetwork ?: return false
            val activeNetwork = cm.getNetworkCapabilities(network) ?: return false
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                else -> false
            }
        }
        else{
            @Suppress("DEPRECATION") val networkInfo = cm.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION") return networkInfo.isConnected
        }
    }
}