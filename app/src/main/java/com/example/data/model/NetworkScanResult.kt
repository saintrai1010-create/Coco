package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "network_scans")
@JsonClass(generateAdapter = true)
data class NetworkScanResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val ssid: String = "",
    val bssid: String = "",
    val signalStrength: Int = 0,
    val ipAddress: String = "",
    val gateway: String = "",
    val dnsServers: String = "",
    val linkSpeed: Int = 0,
    val pingGateway: Int = -1,
    val pingDns: Int = -1,
    val pingGoogle: Int = -1,
    val dnsLookupLatency: Int = -1,
    val httpLatency: Int = -1,
    val openPorts: String = "",
    val aiAnalysis: String? = null
)

data class WifiNetworkInfo(
    val ssid: String,
    val bssid: String,
    val signalStrength: Int, // dBm
    val frequency: Int, // MHz
    val security: String,
    val isCurrent: Boolean = false
)
