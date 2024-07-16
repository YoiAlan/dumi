package com.example.mqttclient

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MqttService : Service() {

    private lateinit var mqttClient: MqttAndroidClient
    private val serverUri = "tcp://10.42.0.1:7010"
    private val clientId = "AndroidClient"
    private val subscriptionTopic = "AlertLevel"

    override fun onCreate() {
        super.onCreate()
        mqttClient = MqttAndroidClient(applicationContext, serverUri, clientId)
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                // Handle connection lost
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                // Broadcast the received message
                val intent = Intent("mqttMessage")
                intent.putExtra("mqttMessage", message.toString())
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // Handle delivery complete
            }
        })
        connect()
    }

    private fun connect() {
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = false

        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    subscribe()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    // Handle connection failure
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun subscribe() {
        try {
            mqttClient.subscribe(subscriptionTopic, 1, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    // Subscription successful
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    // Subscription failed
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "com.example.mqttclient.PUBLISH") {
            val message = intent.getStringExtra("message")
            publishMessage(message)
        }
        return START_NOT_STICKY
    }

    private fun publishMessage(message: String?) {
        if (message != null) {
            try {
                val mqttMessage = MqttMessage()
                mqttMessage.payload = message.toByteArray()
                mqttClient.publish(subscriptionTopic, mqttMessage)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.disconnect()
    }
}
