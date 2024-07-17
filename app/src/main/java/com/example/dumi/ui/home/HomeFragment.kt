package com.example.dumi.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.dumi.R
import com.example.mqttclient.mqttService
import org.eclipse.paho.android.service.MqttService
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

class HomeFragment : Fragment() {
    private lateinit var teksHome : TextView
    private val babi = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getStringExtra("logMessage")?.let { logMessage -> teksHome.append("$logMessage\n")}
        }
    }
    override fun onStart() {
        super.onStart()

    }

    //Template
    override fun onCreateView(
        inflater : LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //initialize UI element make R
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        teksHome     = view.findViewById(R.id.teksHome)
        //Sampe sini coy
        return view
    }
}