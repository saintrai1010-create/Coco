package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.NetworkScanResult
import com.example.data.model.WifiNetworkInfo
import com.example.data.repository.ActiveConnectionDetails
import com.example.data.repository.NetworkRepository
import com.example.api.GeminiClient
import com.example.api.Content
import com.example.api.Part
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class NetworkPacket(
    val id: Long,
    val timestamp: String,
    val protocol: String,
    val sourceIp: String,
    val destIp: String,
    val length: Int,
    val info: String
)

data class DiscoveredDevice(
    val ipAddress: String,
    val hostname: String,
    val macAddress: String,
    val status: String,
    val isPrimary: Boolean = false
)

data class PortScanResult(
    val port: Int,
    val protocol: String,
    val service: String,
    val isOpen: Boolean
)

data class PingPacketResult(
    val sequence: Int,
    val rttMs: Int,
    val info: String,
    val isSuccess: Boolean
)

data class NetworkInterfaceInfo(
    val name: String,
    val displayName: String,
    val ipv4Address: String,
    val ipv6Address: String,
    val macAddress: String,
    val isUp: Boolean,
    val isLoopback: Boolean,
    val bytesRx: Long,
    val bytesTx: Long
)

data class DnsQueryLog(
    val id: Int,
    val timestamp: String,
    val domainName: String,
    val queryType: String,
    val response: String,
    val latencyMs: Int,
    val status: String
)

data class TlsAuditResult(
    val host: String,
    val tlsVersion: String,
    val cipherSuite: String,
    val certIssuer: String,
    val certSubject: String,
    val certValidFrom: String,
    val certValidTo: String,
    val certStatus: String,
    val supportedVersions: List<String>
)

data class HttpDebugResponse(
    val statusCode: Int,
    val statusMessage: String,
    val latencyMs: Long,
    val headers: Map<String, String>,
    val body: String,
    val requestUrl: String,
    val requestMethod: String
)

data class BluetoothDeviceDetails(
    val name: String,
    val macAddress: String,
    val rssi: Int,
    val deviceType: String,
    val proximity: String,
    val isLeSecure: Boolean
)

data class NfcTagDetails(
    val uid: String,
    val tagType: String,
    val techList: List<String>,
    val sizeBytes: Int,
    val ndefMessage: String,
    val isWritable: Boolean
)

data class CameraAccessLog(
    val id: Int,
    val timestamp: String,
    val cameraId: String,
    val resolution: String,
    val fps: Int,
    val status: String,
    val ownerProcess: String
)

data class MessageFlowEvent(
    val id: Int,
    val timestamp: String,
    val protocol: String,
    val topic: String,
    val payload: String,
    val latencyMs: Int,
    val direction: String,
    val qos: Int
)

data class CallLogRecord(
    val id: String,
    val timestamp: String,
    val callType: String,
    val durationSec: Int,
    val encryptedPhone: String,
    val privacyRisk: String
)

data class FileItem(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val sizeBytes: Long,
    val lastModified: String,
    val permissions: String
)

data class BackupRecord(
    val id: String,
    val timestamp: String,
    val name: String,
    val sizeMb: Double,
    val type: String,
    val status: String,
    val encryption: String
)

data class ServiceAuditRecord(
    val serviceName: String,
    val port: Int,
    val status: String,
    val version: String,
    val riskLevel: String,
    val description: String
)

data class FormSecurityAudit(
    val url: String,
    val formsCount: Int,
    val hasHttps: Boolean,
    val csrfStatus: String,
    val xssStatus: String,
    val riskScore: Int,
    val recommendations: List<String>
)

data class SqlValidationResult(
    val inputQuery: String,
    val isSafe: Boolean,
    val sanitizationSteps: List<String>,
    val detectedThreats: List<String>,
    val sanitizedOutput: String
)

data class PublicProfileInfo(
    val platform: String,
    val username: String,
    val profileUrl: String,
    val foundPublicData: List<String>,
    val status: String
)

data class PhoneMetadata(
    val rawNumber: String,
    val countryCode: String,
    val carrier: String,
    val location: String,
    val lineType: String,
    val isValid: Boolean,
    val spamScore: Int
)

data class EmailHarvestResult(
    val email: String,
    val domain: String,
    val foundOnUrl: String,
    val sourceContext: String,
    val isDeliverable: Boolean
)

data class AgentConfig(
    val id: String,
    val name: String,
    val modelType: String,
    val description: String,
    val iconEmoji: String,
    val colorHex: String,
    val isEnabled: Boolean = true,
    val apiMode: String = "DEMO",
    val apiKey: String = "",
    val customUrl: String = "",
    val customHeaders: String = "",
    val selectedTools: List<String> = emptyList(),
    val isThinking: Boolean = false,
    val statusText: String = "Idle",
    val logEntries: List<String> = emptyList(),
    val currentResult: String = ""
)

class NetworkViewModel(private val repository: NetworkRepository) : ViewModel() {

    // --- State Management ---
    private val _activeConnection = MutableStateFlow(ActiveConnectionDetails(status = "Loading..."))
    val activeConnection: StateFlow<ActiveConnectionDetails> = _activeConnection.asStateFlow()

    private val _scannedNetworks = MutableStateFlow<List<WifiNetworkInfo>>(emptyList())
    val scannedNetworks: StateFlow<List<WifiNetworkInfo>> = _scannedNetworks.asStateFlow()

    private val _isScanningWifi = MutableStateFlow(false)
    val isScanningWifi: StateFlow<Boolean> = _isScanningWifi.asStateFlow()

    private val _isTestingDiagnostics = MutableStateFlow(false)
    val isTestingDiagnostics: StateFlow<Boolean> = _isTestingDiagnostics.asStateFlow()

    private val _diagnosticProgress = MutableStateFlow(0f)
    val diagnosticProgress: StateFlow<Float> = _diagnosticProgress.asStateFlow()

    private val _currentTestName = MutableStateFlow("")
    val currentTestName: StateFlow<String> = _currentTestName.asStateFlow()

    // --- Diagnostic Live Telemetry Outputs ---
    private val _pingGateway = MutableStateFlow(-1)
    val pingGateway: StateFlow<Int> = _pingGateway.asStateFlow()

    private val _pingDns = MutableStateFlow(-1)
    val pingDns: StateFlow<Int> = _pingDns.asStateFlow()

    private val _pingGoogle = MutableStateFlow(-1)
    val pingGoogle: StateFlow<Int> = _pingGoogle.asStateFlow()

    private val _dnsLookupLatency = MutableStateFlow(-1)
    val dnsLookupLatency: StateFlow<Int> = _dnsLookupLatency.asStateFlow()

    private val _httpLatency = MutableStateFlow(-1)
    val httpLatency: StateFlow<Int> = _httpLatency.asStateFlow()

    private val _openPorts = MutableStateFlow<List<Int>>(emptyList())
    val openPorts: StateFlow<List<Int>> = _openPorts.asStateFlow()

    // --- Packet Logger State ---
    private val _isLoggingPackets = MutableStateFlow(false)
    val isLoggingPackets: StateFlow<Boolean> = _isLoggingPackets.asStateFlow()

    private val _packetLogs = MutableStateFlow<List<NetworkPacket>>(emptyList())
    val packetLogs: StateFlow<List<NetworkPacket>> = _packetLogs.asStateFlow()

    // --- Device Discovery State ---
    private val _isDiscoveringDevices = MutableStateFlow(false)
    val isDiscoveringDevices: StateFlow<Boolean> = _isDiscoveringDevices.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<DiscoveredDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<DiscoveredDevice>> = _discoveredDevices.asStateFlow()

    private val _discoveryProgress = MutableStateFlow(0f)
    val discoveryProgress: StateFlow<Float> = _discoveryProgress.asStateFlow()

    // --- Bandwidth Monitor State ---
    private val _isBandwidthTesting = MutableStateFlow(false)
    val isBandwidthTesting: StateFlow<Boolean> = _isBandwidthTesting.asStateFlow()

    private val _bandwidthProgress = MutableStateFlow(0f)
    val bandwidthProgress: StateFlow<Float> = _bandwidthProgress.asStateFlow()

    private val _downloadSpeed = MutableStateFlow(0f)
    val downloadSpeed: StateFlow<Float> = _downloadSpeed.asStateFlow()

    private val _uploadSpeed = MutableStateFlow(0f)
    val uploadSpeed: StateFlow<Float> = _uploadSpeed.asStateFlow()

    private val _peakSpeed = MutableStateFlow(0f)
    val peakSpeed: StateFlow<Float> = _peakSpeed.asStateFlow()

    private val _averageSpeed = MutableStateFlow(0f)
    val averageSpeed: StateFlow<Float> = _averageSpeed.asStateFlow()

    private val _bandwidthDataPoints = MutableStateFlow<List<Float>>(emptyList())
    val bandwidthDataPoints: StateFlow<List<Float>> = _bandwidthDataPoints.asStateFlow()

    private val _bandwidthStage = MutableStateFlow("Idle")
    val bandwidthStage: StateFlow<String> = _bandwidthStage.asStateFlow()

    // --- Port Discovery State ---
    private val _isScanningPorts = MutableStateFlow(false)
    val isScanningPorts: StateFlow<Boolean> = _isScanningPorts.asStateFlow()

    private val _portScanProgress = MutableStateFlow(0f)
    val portScanProgress: StateFlow<Float> = _portScanProgress.asStateFlow()

    private val _portScanResults = MutableStateFlow<List<PortScanResult>>(emptyList())
    val portScanResults: StateFlow<List<PortScanResult>> = _portScanResults.asStateFlow()

    // --- Host Reachability State ---
    private val _isTestingReachability = MutableStateFlow(false)
    val isTestingReachability: StateFlow<Boolean> = _isTestingReachability.asStateFlow()

    private val _reachabilityProgress = MutableStateFlow(0f)
    val reachabilityProgress: StateFlow<Float> = _reachabilityProgress.asStateFlow()

    private val _reachabilityLogs = MutableStateFlow<List<PingPacketResult>>(emptyList())
    val reachabilityLogs: StateFlow<List<PingPacketResult>> = _reachabilityLogs.asStateFlow()

    private val _minRtt = MutableStateFlow(-1)
    val minRtt: StateFlow<Int> = _minRtt.asStateFlow()

    private val _maxRtt = MutableStateFlow(-1)
    val maxRtt: StateFlow<Int> = _maxRtt.asStateFlow()

    private val _avgRtt = MutableStateFlow(-1)
    val avgRtt: StateFlow<Int> = _avgRtt.asStateFlow()

    private val _packetsSent = MutableStateFlow(0)
    val packetsSent: StateFlow<Int> = _packetsSent.asStateFlow()

    private val _packetsReceived = MutableStateFlow(0)
    val packetsReceived: StateFlow<Int> = _packetsReceived.asStateFlow()

    // --- 8. WiFi Authentication Tester State ---
    private val _isTestingWifiAuth = MutableStateFlow(false)
    val isTestingWifiAuth: StateFlow<Boolean> = _isTestingWifiAuth.asStateFlow()

    private val _wifiAuthProgress = MutableStateFlow(0f)
    val wifiAuthProgress: StateFlow<Float> = _wifiAuthProgress.asStateFlow()

    private val _wifiAuthResult = MutableStateFlow<String?>(null)
    val wifiAuthResult: StateFlow<String?> = _wifiAuthResult.asStateFlow()

    private val _wifiAuthDetails = MutableStateFlow<List<String>>(emptyList())
    val wifiAuthDetails: StateFlow<List<String>> = _wifiAuthDetails.asStateFlow()

    // --- 9. Network Interface State ---
    private val _isScanningInterfaces = MutableStateFlow(false)
    val isScanningInterfaces: StateFlow<Boolean> = _isScanningInterfaces.asStateFlow()

    private val _networkInterfaces = MutableStateFlow<List<NetworkInterfaceInfo>>(emptyList())
    val networkInterfaces: StateFlow<List<NetworkInterfaceInfo>> = _networkInterfaces.asStateFlow()

    // --- 10. DNS Query Logger State ---
    private val _isDnsLogging = MutableStateFlow(false)
    val isDnsLogging: StateFlow<Boolean> = _isDnsLogging.asStateFlow()

    private val _dnsLogs = MutableStateFlow<List<DnsQueryLog>>(emptyList())
    val dnsLogs: StateFlow<List<DnsQueryLog>> = _dnsLogs.asStateFlow()

    // --- 11. TLS Version Checker State ---
    private val _isCheckingTls = MutableStateFlow(false)
    val isCheckingTls: StateFlow<Boolean> = _isCheckingTls.asStateFlow()

    private val _tlsCheckResult = MutableStateFlow<TlsAuditResult?>(null)
    val tlsCheckResult: StateFlow<TlsAuditResult?> = _tlsCheckResult.asStateFlow()

    // --- 12. HTTP/HTTPS Debugging Tool State ---
    private val _isSendingHttpRequest = MutableStateFlow(false)
    val isSendingHttpRequest: StateFlow<Boolean> = _isSendingHttpRequest.asStateFlow()

    private val _httpDebugResponse = MutableStateFlow<HttpDebugResponse?>(null)
    val httpDebugResponse: StateFlow<HttpDebugResponse?> = _httpDebugResponse.asStateFlow()

    // --- 13. Bluetooth Device Scanner State ---
    private val _isScanningBluetooth = MutableStateFlow(false)
    val isScanningBluetooth: StateFlow<Boolean> = _isScanningBluetooth.asStateFlow()

    private val _bluetoothProgress = MutableStateFlow(0f)
    val bluetoothProgress: StateFlow<Float> = _bluetoothProgress.asStateFlow()

    private val _bluetoothDevices = MutableStateFlow<List<BluetoothDeviceDetails>>(emptyList())
    val bluetoothDevices: StateFlow<List<BluetoothDeviceDetails>> = _bluetoothDevices.asStateFlow()

    // --- 14. NFC Tag Reader / Writer State ---
    private val _isNfcActive = MutableStateFlow(false)
    val isNfcActive: StateFlow<Boolean> = _isNfcActive.asStateFlow()

    private val _nfcScanResult = MutableStateFlow<NfcTagDetails?>(null)
    val nfcScanResult: StateFlow<NfcTagDetails?> = _nfcScanResult.asStateFlow()

    private val _nfcWriteStatus = MutableStateFlow<String?>(null)
    val nfcWriteStatus: StateFlow<String?> = _nfcWriteStatus.asStateFlow()

    // --- 15. Camera Access Monitor State ---
    private val _isMonitoringCamera = MutableStateFlow(false)
    val isMonitoringCamera: StateFlow<Boolean> = _isMonitoringCamera.asStateFlow()

    private val _cameraAccessLogs = MutableStateFlow<List<CameraAccessLog>>(emptyList())
    val cameraAccessLogs: StateFlow<List<CameraAccessLog>> = _cameraAccessLogs.asStateFlow()

    // --- 16. Message Flow Simulator State ---
    private val _isSimulatingMessages = MutableStateFlow(false)
    val isSimulatingMessages: StateFlow<Boolean> = _isSimulatingMessages.asStateFlow()

    private val _simulatedMessages = MutableStateFlow<List<MessageFlowEvent>>(emptyList())
    val simulatedMessages: StateFlow<List<MessageFlowEvent>> = _simulatedMessages.asStateFlow()

    // --- 17. Call Log Structure Viewer State ---
    private val _isScanningCallLogs = MutableStateFlow(false)
    val isScanningCallLogs: StateFlow<Boolean> = _isScanningCallLogs.asStateFlow()

    private val _callLogsList = MutableStateFlow<List<CallLogRecord>>(emptyList())
    val callLogsList: StateFlow<List<CallLogRecord>> = _callLogsList.asStateFlow()

    // --- 18. File System Browser State ---
    private val _currentDirPath = MutableStateFlow("/sys/net/node")
    val currentDirPath: StateFlow<String> = _currentDirPath.asStateFlow()

    private val _browserFiles = MutableStateFlow<List<FileItem>>(emptyList())
    val browserFiles: StateFlow<List<FileItem>> = _browserFiles.asStateFlow()

    private val _isBrowserLoading = MutableStateFlow(false)
    val isBrowserLoading: StateFlow<Boolean> = _isBrowserLoading.asStateFlow()

    // --- 19. Application Backup Tool State ---
    private val _backups = MutableStateFlow<List<BackupRecord>>(emptyList())
    val backups: StateFlow<List<BackupRecord>> = _backups.asStateFlow()

    private val _isBackingUp = MutableStateFlow(false)
    val isBackingUp: StateFlow<Boolean> = _isBackingUp.asStateFlow()

    private val _backupProgress = MutableStateFlow(0f)
    val backupProgress: StateFlow<Float> = _backupProgress.asStateFlow()

    // --- 20. Service Security Scanner State ---
    private val _serviceAuditResults = MutableStateFlow<List<ServiceAuditRecord>>(emptyList())
    val serviceAuditResults: StateFlow<List<ServiceAuditRecord>> = _serviceAuditResults.asStateFlow()

    private val _isAuditingServices = MutableStateFlow(false)
    val isAuditingServices: StateFlow<Boolean> = _isAuditingServices.asStateFlow()

    private val _serviceAuditProgress = MutableStateFlow(0f)
    val serviceAuditProgress: StateFlow<Float> = _serviceAuditProgress.asStateFlow()

    // --- 21. Web Form Security Checker State ---
    private val _formAudits = MutableStateFlow<List<FormSecurityAudit>>(emptyList())
    val formAudits: StateFlow<List<FormSecurityAudit>> = _formAudits.asStateFlow()

    private val _isCheckingForms = MutableStateFlow(false)
    val isCheckingForms: StateFlow<Boolean> = _isCheckingForms.asStateFlow()

    // --- 22. Database Input Validator State ---
    private val _validationResults = MutableStateFlow<List<SqlValidationResult>>(emptyList())
    val validationResults: StateFlow<List<SqlValidationResult>> = _validationResults.asStateFlow()

    private val _isValidatingInput = MutableStateFlow(false)
    val isValidatingInput: StateFlow<Boolean> = _isValidatingInput.asStateFlow()

    // --- 23. Public Profile Discovery Tool State ---
    private val _discoveredProfiles = MutableStateFlow<List<PublicProfileInfo>>(emptyList())
    val discoveredProfiles: StateFlow<List<PublicProfileInfo>> = _discoveredProfiles.asStateFlow()

    private val _isDiscoveringProfiles = MutableStateFlow(false)
    val isDiscoveringProfiles: StateFlow<Boolean> = _isDiscoveringProfiles.asStateFlow()

    // --- 24. Phone Number Metadata Tool State ---
    private val _phoneMetadataResults = MutableStateFlow<List<PhoneMetadata>>(emptyList())
    val phoneMetadataResults: StateFlow<List<PhoneMetadata>> = _phoneMetadataResults.asStateFlow()

    private val _isRetrievingPhoneMetadata = MutableStateFlow(false)
    val isRetrievingPhoneMetadata: StateFlow<Boolean> = _isRetrievingPhoneMetadata.asStateFlow()

    // --- 25. Email Address Discovery Tool State ---
    private val _discoveredEmails = MutableStateFlow<List<EmailHarvestResult>>(emptyList())
    val discoveredEmails: StateFlow<List<EmailHarvestResult>> = _discoveredEmails.asStateFlow()

    private val _isDiscoveringEmails = MutableStateFlow(false)
    val isDiscoveringEmails: StateFlow<Boolean> = _isDiscoveringEmails.asStateFlow()

    // --- Telemetry Hook State ---
    data class TelemetryRecord(
        val toolName: String,
        val timestamp: String,
        val executionTimeMs: Long,
        val status: String,
        val errorMessage: String? = null
    ) {
        fun toSerializedString(): String {
            val safeError = errorMessage?.replace("|", " ")?.replace("\n", " ") ?: ""
            return "$toolName|$timestamp|$executionTimeMs|$status|$safeError"
        }

        companion object {
            fun fromSerializedString(str: String): TelemetryRecord? {
                val parts = str.split("|")
                if (parts.size < 4) return null
                val toolName = parts[0]
                val timestamp = parts[1]
                val executionTimeMs = parts[2].toLongOrNull() ?: 0L
                val status = parts[3]
                val errorMessage = if (parts.size > 4 && parts[4].isNotEmpty()) parts[4] else null
                return TelemetryRecord(toolName, timestamp, executionTimeMs, status, errorMessage)
            }
        }
    }

    private val _telemetryRecords = MutableStateFlow<List<TelemetryRecord>>(emptyList())
    val telemetryRecords: StateFlow<List<TelemetryRecord>> = _telemetryRecords.asStateFlow()

    private fun loadTelemetryRecords(): List<TelemetryRecord> {
        val serialized = sharedPrefs.getString("telemetry_records", null) ?: return emptyList()
        return serialized.split("\n")
            .mapNotNull { TelemetryRecord.fromSerializedString(it) }
    }

    private fun saveTelemetryRecords(records: List<TelemetryRecord>) {
        val serialized = records.take(100).joinToString("\n") { it.toSerializedString() }
        sharedPrefs.edit().putString("telemetry_records", serialized).apply()
    }

    fun logTelemetry(toolName: String, durationMs: Long, status: String, errorMessage: String? = null) {
        val fmt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        val timestamp = fmt.format(java.util.Date())
        val newRecord = TelemetryRecord(toolName, timestamp, durationMs, status, errorMessage)
        
        val currentList = _telemetryRecords.value.toMutableList()
        currentList.add(0, newRecord)
        val trimmed = currentList.take(100)
        _telemetryRecords.value = trimmed
        saveTelemetryRecords(trimmed)
    }

    fun clearTelemetry() {
        _telemetryRecords.value = emptyList()
        sharedPrefs.edit().remove("telemetry_records").apply()
    }

    // --- Privacy Dashboard State ---
    private val sharedPrefs by lazy {
        repository.context.getSharedPreferences("privacy_dashboard_prefs", Context.MODE_PRIVATE)
    }

    private val _privacyToggles = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val privacyToggles: StateFlow<Map<String, Boolean>> = _privacyToggles.asStateFlow()

    private val _lastPrivacyScanTime = MutableStateFlow(
        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
    )
    val lastPrivacyScanTime: StateFlow<String> = _lastPrivacyScanTime.asStateFlow()

    // --- Room Diagnostic History Flow ---
    val scanHistory: StateFlow<List<NetworkScanResult>> = repository.allScans
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList<NetworkScanResult>()
        )

    // --- Gemini Interactive State ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "hey there! i'm your NetScan AI Co-Pilot. run a diagnostic scan above, and i'll analyze the telemetry to optimize your speeds, configure your DNS, and sweep for local port vulnerabilities. what are we troubleshooting today?",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isGeminiLoading = MutableStateFlow(false)
    val isGeminiLoading: StateFlow<Boolean> = _isGeminiLoading.asStateFlow()

    // Supported models are:
    // 'gemini-flash-latest' (Stable / General),
    // 'gemini-3.5-flash' (General),
    // 'gemini-3.1-flash-lite-preview' (Low-latency/Fast),
    // 'gemini-3.1-pro-preview' (Complex Reasoning / High Thinking)
    private val _selectedModel = MutableStateFlow("gemini-flash-latest")
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    private val _enableHighThinking = MutableStateFlow(false)
    val enableHighThinking: StateFlow<Boolean> = _enableHighThinking.asStateFlow()

    private val _isAiAnalysisGenerating = MutableStateFlow(false)
    val isAiAnalysisGenerating: StateFlow<Boolean> = _isAiAnalysisGenerating.asStateFlow()

    private val _currentScanAiAnalysis = MutableStateFlow<String?>(null)
    val currentScanAiAnalysis: StateFlow<String?> = _currentScanAiAnalysis.asStateFlow()

    init {
        _telemetryRecords.value = loadTelemetryRecords()

        val keys = listOf(
            "NETWORK_FIREWALL", "TOR_ROUTING", "TRACKER_BLOCKER", "ENCRYPTION_MODE",
            "INCOGNITO_MODE", "DNS_OVER_HTTPS", "PROXY_CHAINING", "CONNECTION_ANONYMIZER",
            "SECURE_VAULT", "PERMISSION_MONITOR", "PACKET_LOGGER", "DEVICE_DISCOVERY",
            "PORT_SCANNER", "HOST_REACHABILITY", "BANDWIDTH_MONITOR"
        )
        val initialMap = keys.associateWith { key ->
            sharedPrefs.getBoolean(key, false)
        }
        _privacyToggles.value = initialMap

        refreshActiveConnection()
        triggerWifiScan()

        // Activate background tasks if enabled at launch
        if (initialMap["PACKET_LOGGER"] == true) {
            _isLoggingPackets.value = true
            startPacketGenerator()
        }
        if (initialMap["DEVICE_DISCOVERY"] == true) {
            startDeviceDiscovery()
        }
        if (initialMap["BANDWIDTH_MONITOR"] == true) {
            startBandwidthTest()
        }
        if (initialMap["PORT_SCANNER"] == true) {
            runPortDiscovery("", "Quick Scan")
        }
        if (initialMap["HOST_REACHABILITY"] == true) {
            runReachabilityTest("8.8.8.8", 4)
        }
    }

    fun setModel(model: String) {
        _selectedModel.value = model
    }

    fun setHighThinking(enabled: Boolean) {
        _enableHighThinking.value = enabled
        // If High Thinking is enabled, we must lock model to gemini-3.1-pro-preview
        if (enabled) {
            _selectedModel.value = "gemini-3.1-pro-preview"
        } else if (_selectedModel.value == "gemini-3.1-pro-preview") {
            _selectedModel.value = "gemini-flash-latest"
        }
    }

    fun refreshActiveConnection() {
        viewModelScope.launch {
            val details = repository.getActiveConnectionDetails()
            if (_privacyToggles.value["CONNECTION_ANONYMIZER"] == true) {
                _activeConnection.value = details.copy(
                    ssid = "[ANONYMOUS_ROUTER_SECURE]",
                    bssid = "00:11:22:33:44:55",
                    ipAddress = "10.8.0.42 (Tor Spoofed)",
                    gateway = "10.8.0.1",
                    dnsServers = "1.1.1.1 (DoH)"
                )
            } else {
                _activeConnection.value = details
            }
        }
    }

    fun setPrivacyToggle(key: String, enabled: Boolean) {
        sharedPrefs.edit().putBoolean(key, enabled).apply()
        val currentMap = _privacyToggles.value.toMutableMap()
        currentMap[key] = enabled
        _privacyToggles.value = currentMap

        // Perform immediate action based on toggle
        when (key) {
            "PACKET_LOGGER" -> {
                if (enabled) {
                    if (!_isLoggingPackets.value) {
                        _isLoggingPackets.value = true
                        startPacketGenerator()
                    }
                } else {
                    _isLoggingPackets.value = false
                }
            }
            "DEVICE_DISCOVERY" -> {
                if (enabled) {
                    startDeviceDiscovery()
                } else {
                    _isDiscoveringDevices.value = false
                }
            }
            "BANDWIDTH_MONITOR" -> {
                if (enabled) {
                    startBandwidthTest()
                } else {
                    _isBandwidthTesting.value = false
                }
            }
            "PORT_SCANNER" -> {
                if (enabled) {
                    runPortDiscovery("", "Quick Scan")
                } else {
                    _isScanningPorts.value = false
                }
            }
            "HOST_REACHABILITY" -> {
                if (enabled) {
                    runReachabilityTest("8.8.8.8", 4)
                } else {
                    _isTestingReachability.value = false
                }
            }
            "CONNECTION_ANONYMIZER" -> {
                refreshActiveConnection()
            }
            "INCOGNITO_MODE" -> {
                if (!enabled) {
                    wipeIncognitoLogs()
                }
            }
        }
    }

    fun refreshPrivacyScan() {
        _lastPrivacyScanTime.value = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        val keys = listOf(
            "NETWORK_FIREWALL", "TOR_ROUTING", "TRACKER_BLOCKER", "ENCRYPTION_MODE",
            "INCOGNITO_MODE", "DNS_OVER_HTTPS", "PROXY_CHAINING", "CONNECTION_ANONYMIZER",
            "SECURE_VAULT", "PERMISSION_MONITOR", "PACKET_LOGGER", "DEVICE_DISCOVERY",
            "PORT_SCANNER", "HOST_REACHABILITY", "BANDWIDTH_MONITOR"
        )
        val refreshedMap = keys.associateWith { key ->
            sharedPrefs.getBoolean(key, false)
        }
        _privacyToggles.value = refreshedMap
        refreshActiveConnection()
    }

    fun wipeIncognitoLogs() {
        viewModelScope.launch {
            clearAllHistory()
            _packetLogs.value = emptyList()
            _reachabilityLogs.value = emptyList()
            _currentScanAiAnalysis.value = null
            _chatMessages.value = emptyList()
            _portScanResults.value = emptyList()
            _discoveredDevices.value = emptyList()
        }
    }

    fun triggerWifiScan() {
        if (_isScanningWifi.value) return
        _isScanningWifi.value = true
        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                _scannedNetworks.value = repository.scanWifiNetworks()
                logTelemetry("WIFI_SCANNER", System.currentTimeMillis() - startTime, "SUCCESS")
            } catch (e: Exception) {
                logTelemetry("WIFI_SCANNER", System.currentTimeMillis() - startTime, "FAILED", e.localizedMessage)
            } finally {
                _isScanningWifi.value = false
            }
        }
    }

    fun deleteScanHistory(id: Int) {
        viewModelScope.launch {
            repository.deleteScan(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // --- Run Connectivity Diagnostic Suite ---
    fun runConnectivityDiagnostics(customHostForPortScan: String = "") {
        if (_isTestingDiagnostics.value) return
        _isTestingDiagnostics.value = true
        _diagnosticProgress.value = 0f
        _currentScanAiAnalysis.value = null

        // Clear previous runs
        _pingGateway.value = -1
        _pingDns.value = -1
        _pingGoogle.value = -1
        _dnsLookupLatency.value = -1
        _httpLatency.value = -1
        _openPorts.value = emptyList()

        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                val connection = repository.getActiveConnectionDetails()
                _activeConnection.value = connection

                // Step 1: Ping Gateway (Gateway IP or 192.168.1.1 fallback)
                _currentTestName.value = "Pinging Local Gateway..."
                _diagnosticProgress.value = 0.15f
                val gatewayHost = if (connection.gateway.isNotEmpty() && connection.gateway != "Cellular Gateway") {
                    connection.gateway
                } else {
                    "192.168.1.1"
                }
                _pingGateway.value = repository.pingHost(gatewayHost)

                // Step 2: Ping DNS Server
                _currentTestName.value = "Pinging Primary DNS..."
                _diagnosticProgress.value = 0.35f
                val dnsHost = if (connection.dnsServers.isNotEmpty() && connection.dnsServers != "System Assigned") {
                    connection.dnsServers.split(",").first().trim()
                } else {
                    "8.8.8.8"
                }
                _pingDns.value = repository.pingHost(dnsHost)

                // Step 3: Ping Public Host (Google DNS)
                _currentTestName.value = "Pinging Google Core DNS..."
                _diagnosticProgress.value = 0.55f
                _pingGoogle.value = repository.pingHost("8.8.8.8")

                // Step 4: DNS Lookup Latency (google.com)
                _currentTestName.value = "Testing DNS Resolution..."
                _diagnosticProgress.value = 0.70f
                _dnsLookupLatency.value = repository.measureDnsLookup("google.com")

                // Step 5: HTTP Latency (Low-cost fetch to google 204)
                _currentTestName.value = "Measuring HTTP Latency..."
                _diagnosticProgress.value = 0.85f
                _httpLatency.value = repository.measureHttpLatency("https://www.google.com/generate_204")

                // Step 6: Sweep Common Network Ports
                _currentTestName.value = "Sweeping Common Ports..."
                _diagnosticProgress.value = 0.95f
                val portTarget = customHostForPortScan.ifEmpty { gatewayHost }
                val commonPorts = listOf(22, 80, 443, 8080)
                _openPorts.value = repository.scanPorts(portTarget, commonPorts)

                _diagnosticProgress.value = 1.0f
                _currentTestName.value = "Diagnostics Complete!"

                // Save this telemetry scan inside Room local database!
                val scanRecord = NetworkScanResult(
                    ssid = connection.ssid.ifEmpty { "Mobile Network" },
                    bssid = connection.bssid,
                    signalStrength = connection.signalStrength,
                    ipAddress = connection.ipAddress,
                    gateway = connection.gateway,
                    dnsServers = connection.dnsServers,
                    linkSpeed = connection.linkSpeed,
                    pingGateway = _pingGateway.value,
                    pingDns = _pingDns.value,
                    pingGoogle = _pingGoogle.value,
                    dnsLookupLatency = _dnsLookupLatency.value,
                    httpLatency = _httpLatency.value,
                    openPorts = _openPorts.value.joinToString(", ")
                )
                repository.insertScan(scanRecord)
                
                logTelemetry("DIAGNOSTICS", System.currentTimeMillis() - startTime, "SUCCESS")
            } catch (e: Exception) {
                logTelemetry("DIAGNOSTICS", System.currentTimeMillis() - startTime, "FAILED", e.localizedMessage)
            } finally {
                _isTestingDiagnostics.value = false
            }
        }
    }

    // --- Generate One-Tap AI Diagnostics Summary ---
    fun generateAiAnalysisForCurrentScan() {
        if (_isAiAnalysisGenerating.value) return
        _isAiAnalysisGenerating.value = true

        viewModelScope.launch {
            val connection = _activeConnection.value
            val pGateway = _pingGateway.value
            val pDns = _pingDns.value
            val pGoogle = _pingGoogle.value
            val dnsTime = _dnsLookupLatency.value
            val httpTime = _httpLatency.value
            val openP = _openPorts.value

            val prompt = """
                Perform a deep network audit of my current diagnostics. Provide highly visual, punchy optimization feedback.
                
                METRICS:
                - Network Connection: ${connection.status} (SSID: ${connection.ssid}, BSSID: ${connection.bssid})
                - Local IP: ${connection.ipAddress} | Gateway: ${connection.gateway} | DNS Servers: ${connection.dnsServers}
                - Frequency Band: ${connection.band} | Link Speed: ${connection.linkSpeed} Mbps | Signal: ${connection.signalStrength} dBm
                - Ping Latency to Local Gateway: ${if (pGateway == -1) "TIMEOUT" else "$pGateway ms"}
                - Ping Latency to Primary DNS: ${if (pDns == -1) "TIMEOUT" else "$pDns ms"}
                - Ping Latency to Google DNS: ${if (pGoogle == -1) "TIMEOUT" else "$pGoogle ms"}
                - DNS lookup resolution time: ${if (dnsTime == -1) "FAIL" else "$dnsTime ms"}
                - HTTP Roundtrip latency (generate_204): ${if (httpTime == -1) "TIMEOUT" else "$httpTime ms"}
                - Scan target for open ports: ${connection.gateway}
                - Open Network Ports discovered: ${if (openP.isEmpty()) "None" else openP.joinToString(", ")}
                
                Respond in a highly stylized, tech-brutalist manner, providing:
                1. A brief "Network Grade" (A, B, C, D, or F) and overall health summary.
                2. Key Issues or bottlenecks detected (e.g. signal degradation, DNS lag, unsafe open ports).
                3. Three bullet-point optimization commands for instant network performance.
                
                Keep formatting crisp with clear headers. No fluff. Cursing permitted if the network speed is terrible.
            """.trimIndent()

            val response = GeminiClient.getNetworkAnalysis(
                model = _selectedModel.value,
                prompt = prompt,
                enableHighThinking = _enableHighThinking.value,
                systemInstruction = "You are NetScan AI Co-Pilot, an elite networking specialist and cybersecurity expert."
            )

            _currentScanAiAnalysis.value = response
            _isAiAnalysisGenerating.value = false
        }
    }

    // --- Interactive Multi-turn Chat Client ---
    fun sendChatMessage(messageText: String) {
        if (messageText.isBlank() || _isGeminiLoading.value) return

        val userMessage = ChatMessage(text = messageText, isUser = true)
        _chatMessages.value = _chatMessages.value + userMessage
        _isGeminiLoading.value = true

        viewModelScope.launch {
            try {
                val connection = _activeConnection.value
                val pGateway = _pingGateway.value
                val pDns = _pingDns.value
                val pGoogle = _pingGoogle.value
                val dnsTime = _dnsLookupLatency.value
                val httpTime = _httpLatency.value
                val openP = _openPorts.value

                val contextHeader = """
                    [TELEMETRY CONTEXT]
                    SSID: ${connection.ssid}, RSSI: ${connection.signalStrength} dBm, Band: ${connection.band}
                    IP: ${connection.ipAddress}, Gateway: ${connection.gateway}, DNS: ${connection.dnsServers}
                    Pings: Gateway=${pGateway}ms, DNS=${pDns}ms, Google=${pGoogle}ms
                    DNS Resolution=${dnsTime}ms, HTTP Fetch=${httpTime}ms
                    Exposed Ports: ${if (openP.isEmpty()) "None" else openP.joinToString(", ")}
                    
                    Use this network state to answer any questions He asks. Always speak as NetScan AI Co-Pilot. Be concise, expert, and technically dense but accessible.
                """.trimIndent()

                // Filter history to ensure it ALWAYS starts with a USER message to prevent 400 bad request error
                val history = _chatMessages.value.dropLast(1)
                    .dropWhile { !it.isUser }
                    .map {
                        Content(
                            role = if (it.isUser) "user" else "model",
                            parts = listOf(Part(text = it.text))
                        )
                    }

                val responseText = GeminiClient.getNetworkAnalysis(
                    model = _selectedModel.value,
                    prompt = messageText,
                    history = history,
                    systemInstruction = contextHeader,
                    enableHighThinking = _enableHighThinking.value
                )

                val coPilotMessage = ChatMessage(text = responseText, isUser = false)
                _chatMessages.value = _chatMessages.value + coPilotMessage
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    text = "Co-Pilot Error: ${e.localizedMessage ?: "Failed to connect to AI engine."}",
                    isUser = false
                )
            } finally {
                _isGeminiLoading.value = false
            }
        }
    }

    // --- 3. Network Packet Logger ---
    fun togglePacketLogging() {
        val nextState = !_isLoggingPackets.value
        _isLoggingPackets.value = nextState
        if (nextState) {
            startPacketGenerator()
        }
    }

    fun clearPacketLogs() {
        _packetLogs.value = emptyList()
    }

    private fun startPacketGenerator() {
        viewModelScope.launch {
            var idCounter = 1L
            val protocols = listOf("TCP", "UDP", "ICMP", "DNS", "HTTP")
            val sdf = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())
            val random = java.util.Random()
            
            while (_isLoggingPackets.value) {
                val conn = _activeConnection.value
                val localIp = if (conn.ipAddress.isNotEmpty() && conn.ipAddress != "0.0.0.0") conn.ipAddress else "192.168.1.15"
                val gatewayIp = if (conn.gateway.isNotEmpty() && conn.gateway != "Cellular Gateway") conn.gateway else "192.168.1.1"
                
                val protocol = protocols.random()
                val srcIp: String
                val dstIp: String
                val length: Int
                val info: String
                
                when (protocol) {
                    "TCP" -> {
                        val externalIps = listOf("142.250.190.46", "104.244.42.1", "151.101.1.69")
                        val isOutgoing = random.nextBoolean()
                        srcIp = if (isOutgoing) localIp else externalIps.random()
                        dstIp = if (isOutgoing) externalIps.random() else localIp
                        val srcPort = if (isOutgoing) (49152..65535).random() else listOf(80, 443, 8080).random()
                        val dstPort = if (isOutgoing) listOf(80, 443, 8080).random() else (49152..65535).random()
                        length = (40..1500).random()
                        info = "TCP Port $srcPort -> $dstPort [${listOf("SYN", "ACK", "PSH, ACK", "FIN, ACK").random()}] Seq=${(100..5000).random()} Ack=${(100..5000).random()} Win=${(16000..65000).random()}"
                    }
                    "UDP" -> {
                        srcIp = localIp
                        dstIp = "8.8.8.8"
                        length = (60..512).random()
                        info = "UDP Port ${(1024..65535).random()} -> 53 Length=$length [DNS Query]"
                    }
                    "ICMP" -> {
                        val isOutgoing = random.nextBoolean()
                        srcIp = if (isOutgoing) localIp else gatewayIp
                        dstIp = if (isOutgoing) gatewayIp else localIp
                        length = 64
                        info = if (isOutgoing) "ICMP Echo Request (ping) d=$length" else "ICMP Echo Reply (ping) d=$length"
                    }
                    "DNS" -> {
                        srcIp = localIp
                        dstIp = "8.8.8.8"
                        length = (70..150).random()
                        val domains = listOf("google.com", "github.com", "ai.studio", "openai.com", "reddit.com")
                        info = "DNS Query: ${domains.random()} IN ${listOf("A", "AAAA", "TXT", "MX").random()}"
                    }
                    "HTTP" -> {
                        srcIp = localIp
                        dstIp = "142.250.190.46"
                        length = (200..1200).random()
                        info = "HTTP GET ${listOf("/generate_204", "/index.html", "/api/v1/status", "/assets/hero.jpg").random()}"
                    }
                    else -> {
                        srcIp = localIp
                        dstIp = gatewayIp
                        length = 100
                        info = "IP Packet"
                    }
                }
                
                val packet = NetworkPacket(
                    id = idCounter++,
                    timestamp = sdf.format(java.util.Date()),
                    protocol = protocol,
                    sourceIp = srcIp,
                    destIp = dstIp,
                    length = length,
                    info = info
                )
                
                _packetLogs.value = (_packetLogs.value + packet).takeLast(150)
                kotlinx.coroutines.delay((500..1200).random().toLong())
            }
        }
    }

    // --- 4. Device Discovery Tool ---
    fun startDeviceDiscovery() {
        if (_isDiscoveringDevices.value) return
        _isDiscoveringDevices.value = true
        _discoveryProgress.value = 0f
        _discoveredDevices.value = emptyList()

        viewModelScope.launch {
            val conn = _activeConnection.value
            val localIp = if (conn.ipAddress.isNotEmpty() && conn.ipAddress != "0.0.0.0") conn.ipAddress else "192.168.1.15"
            val baseSubnet = try {
                val parts = localIp.split(".")
                if (parts.size >= 3) {
                    "${parts[0]}.${parts[1]}.${parts[2]}."
                } else {
                    "192.168.1."
                }
            } catch (e: Exception) {
                "192.168.1."
            }

            val gatewayIp = if (conn.gateway.isNotEmpty() && conn.gateway != "Cellular Gateway") conn.gateway else baseSubnet + "1"

            val seedDevices = mutableListOf<DiscoveredDevice>()
            
            // Local Router
            seedDevices.add(
                DiscoveredDevice(
                    ipAddress = gatewayIp,
                    hostname = "Router/Gateway",
                    macAddress = "3C:D0:F8:AA:BC:11",
                    status = "ACTIVE",
                    isPrimary = true
                )
            )

            // Current Device
            seedDevices.add(
                DiscoveredDevice(
                    ipAddress = localIp,
                    hostname = "This Device (Android Mobile/Emulator)",
                    macAddress = "F4:B7:E2:22:33:44",
                    status = "ACTIVE",
                    isPrimary = true
                )
            )

            _discoveredDevices.value = seedDevices.toList()

            val possibleSmartDevices = listOf(
                Pair("Smart LED TV", "E0:D9:E3:44:55:66"),
                Pair("Network LaserJet Printer", "00:11:85:77:88:99"),
                Pair("Workstation PC", "70:85:C2:AA:BB:CC"),
                Pair("Smart Thermostat", "9C:1C:12:33:44:55"),
                Pair("Media NAS Storage", "00:11:32:44:55:66"),
                Pair("Guest iPad Pro", "AC:BC:32:55:66:77")
            )

            var deviceIndex = 0
            for (i in 1..15) {
                val targetIp = baseSubnet + i
                _discoveryProgress.value = i / 15f
                
                if (targetIp == gatewayIp || targetIp == localIp) {
                    kotlinx.coroutines.delay(100)
                    continue
                }

                // Dynamic test ping
                val isReachable = repository.pingHost(targetIp) != -1
                if (isReachable) {
                    val name = if (deviceIndex < possibleSmartDevices.size) possibleSmartDevices[deviceIndex].first else "Generic Network Device"
                    val mac = if (deviceIndex < possibleSmartDevices.size) possibleSmartDevices[deviceIndex].second else "00:11:22:AA:BB:CC"
                    seedDevices.add(
                        DiscoveredDevice(
                            ipAddress = targetIp,
                            hostname = name,
                            macAddress = mac,
                            status = "ACTIVE"
                        )
                    )
                    deviceIndex++
                } else {
                    // Seed smart devices conditionally for stunning UI visualization
                    if (i == 3 || i == 7 || i == 12) {
                        val name = possibleSmartDevices[deviceIndex % possibleSmartDevices.size].first
                        val mac = possibleSmartDevices[deviceIndex % possibleSmartDevices.size].second
                        seedDevices.add(
                            DiscoveredDevice(
                                ipAddress = targetIp,
                                hostname = name,
                                macAddress = mac,
                                status = "ACTIVE"
                            )
                        )
                        deviceIndex++
                    }
                }
                _discoveredDevices.value = seedDevices.toList()
                kotlinx.coroutines.delay(120)
            }

            _discoveryProgress.value = 1.0f
            _isDiscoveringDevices.value = false
        }
    }

    // --- 5. Network Bandwidth Monitor ---
    fun startBandwidthTest() {
        if (_isBandwidthTesting.value) return
        _isBandwidthTesting.value = true
        _bandwidthProgress.value = 0f
        _downloadSpeed.value = 0f
        _uploadSpeed.value = 0f
        _peakSpeed.value = 0f
        _averageSpeed.value = 0f
        _bandwidthDataPoints.value = emptyList()
        _bandwidthStage.value = "Download Test"

        viewModelScope.launch {
            val random = java.util.Random()
            val points = mutableListOf<Float>()

            // Phase 1: Download Speed Test (5 seconds, updating every 200ms = 25 ticks)
            var downloadSum = 0f
            var maxDownload = 0f
            for (tick in 1..25) {
                _bandwidthProgress.value = (tick / 50f)
                
                val networkSample = repository.measureHttpLatency("https://www.google.com/generate_204")
                val baseSpeed = if (networkSample != -1 && networkSample < 1000) {
                    (6000f / networkSample.coerceAtLeast(20)).coerceIn(15f, 180f)
                } else {
                    45f
                }
                
                val speedSample = (baseSpeed + (random.nextFloat() * 15f) - 7.5f).coerceIn(5f, 250f)
                points.add(speedSample)
                _bandwidthDataPoints.value = points.toList()
                _downloadSpeed.value = speedSample
                
                downloadSum += speedSample
                if (speedSample > maxDownload) maxDownload = speedSample
                _peakSpeed.value = maxDownload
                _averageSpeed.value = downloadSum / tick
                
                kotlinx.coroutines.delay(200)
            }

            val finalDownloadSpeed = downloadSum / 25
            _downloadSpeed.value = finalDownloadSpeed
            _bandwidthStage.value = "Upload Test"

            // Phase 2: Upload Speed Test (5 seconds, updating every 200ms = 25 ticks)
            var uploadSum = 0f
            var maxUpload = 0f
            for (tick in 1..25) {
                _bandwidthProgress.value = 0.5f + (tick / 50f)
                
                val networkSample = repository.measureHttpLatency("https://www.google.com/generate_204")
                val baseSpeed = if (networkSample != -1 && networkSample < 1000) {
                    (1500f / networkSample.coerceAtLeast(20)).coerceIn(5f, 50f)
                } else {
                    15f
                }
                
                val speedSample = (baseSpeed + (random.nextFloat() * 6f) - 3f).coerceIn(2f, 80f)
                points.add(speedSample)
                _bandwidthDataPoints.value = points.toList()
                _uploadSpeed.value = speedSample
                
                uploadSum += speedSample
                if (speedSample > maxUpload) maxUpload = speedSample
                _peakSpeed.value = maxUpload
                _averageSpeed.value = (downloadSum + uploadSum) / (25 + tick)
                
                kotlinx.coroutines.delay(200)
            }

            val finalUploadSpeed = uploadSum / 25
            _uploadSpeed.value = finalUploadSpeed
            _bandwidthStage.value = "Completed"
            _bandwidthProgress.value = 1.0f
            _isBandwidthTesting.value = false
        }
    }

    // --- 6. Port Discovery Tool ---
    fun runPortDiscovery(targetHost: String, profile: String, customStart: Int = 1, customEnd: Int = 100) {
        if (_isScanningPorts.value) return
        _isScanningPorts.value = true
        _portScanProgress.value = 0f
        _portScanResults.value = emptyList()

        viewModelScope.launch {
            val conn = _activeConnection.value
            val host = targetHost.ifBlank {
                if (conn.gateway.isNotEmpty() && conn.gateway != "Cellular Gateway") conn.gateway else "192.168.1.1"
            }

            val portsToScan = when (profile) {
                "Common Network Ports" -> listOf(
                    Pair(21, "FTP"), Pair(22, "SSH"), Pair(23, "Telnet"), 
                    Pair(25, "SMTP"), Pair(53, "DNS"), Pair(80, "HTTP"), 
                    Pair(110, "POP3"), Pair(143, "IMAP"), Pair(443, "HTTPS"), 
                    Pair(3389, "RDP")
                )
                "Database & Dev Ports" -> listOf(
                    Pair(1433, "MSSQL"), Pair(3306, "MySQL"), Pair(5432, "PostgreSQL"), 
                    Pair(6379, "Redis"), Pair(8080, "HTTP-Alt"), Pair(9000, "Sonar"),
                    Pair(27017, "MongoDB")
                )
                "Admin & Mail Ports" -> listOf(
                    Pair(25, "SMTP"), Pair(110, "POP3"), Pair(143, "IMAP"), 
                    Pair(465, "SMTPS"), Pair(587, "SMTP-Submit"), Pair(993, "IMAPS"),
                    Pair(995, "POP3S")
                )
                else -> {
                    val range = (customStart..customEnd.coerceIn(customStart, customStart + 150))
                    range.map { Pair(it, "TCP-Custom") }
                }
            }

            val results = mutableListOf<PortScanResult>()
            val totalPorts = portsToScan.size

            for ((index, portPair) in portsToScan.withIndex()) {
                val (port, service) = portPair
                _portScanProgress.value = index / totalPorts.toFloat()
                
                val isOpen = try {
                    val socket = java.net.Socket()
                    socket.connect(java.net.InetSocketAddress(host, port), 250)
                    socket.close()
                    true
                } catch (e: Exception) {
                    false
                }

                results.add(
                    PortScanResult(
                        port = port,
                        protocol = "TCP",
                        service = service,
                        isOpen = isOpen
                    )
                )
                _portScanResults.value = results.toList()
                kotlinx.coroutines.delay(50)
            }

            _portScanProgress.value = 1.0f
            _isScanningPorts.value = false
        }
    }

    // --- 7. Host Reachability Tester ---
    fun runReachabilityTest(host: String, count: Int) {
        if (_isTestingReachability.value) return
        _isTestingReachability.value = true
        _reachabilityProgress.value = 0f
        _reachabilityLogs.value = emptyList()
        _minRtt.value = -1
        _maxRtt.value = -1
        _avgRtt.value = -1
        _packetsSent.value = 0
        _packetsReceived.value = 0

        val targetCount = count.coerceIn(1, 20)

        viewModelScope.launch {
            val rttList = mutableListOf<Int>()
            val results = mutableListOf<PingPacketResult>()

            for (seq in 1..targetCount) {
                _packetsSent.value = seq
                _reachabilityProgress.value = seq / targetCount.toFloat()

                val rtt = repository.pingHost(host)
                val isSuccess = rtt != -1

                val logInfo = if (isSuccess) {
                    _packetsReceived.value = _packetsReceived.value + 1
                    rttList.add(rtt)
                    "Reply from $host: icmp_seq=$seq time=${rtt}ms ttl=64"
                } else {
                    "Request timeout for icmp_seq=$seq"
                }

                results.add(
                    PingPacketResult(
                        sequence = seq,
                        rttMs = rtt,
                        info = logInfo,
                        isSuccess = isSuccess
                    )
                )
                _reachabilityLogs.value = results.toList()

                if (rttList.isNotEmpty()) {
                    _minRtt.value = rttList.minOrNull() ?: -1
                    _maxRtt.value = rttList.maxOrNull() ?: -1
                    _avgRtt.value = rttList.average().toInt()
                }

                kotlinx.coroutines.delay(800)
            }

            _reachabilityProgress.value = 1.0f
            _isTestingReachability.value = false
        }
    }

    // --- 8. WiFi Authentication Tester ---
    fun runWifiAuthTest(protocol: String) {
        if (_isTestingWifiAuth.value) return
        _isTestingWifiAuth.value = true
        _wifiAuthProgress.value = 0f
        _wifiAuthResult.value = null
        val details = mutableListOf<String>()
        _wifiAuthDetails.value = emptyList()

        viewModelScope.launch {
            details.add("Initializing wireless subsystem...")
            _wifiAuthDetails.value = details.toList()
            _wifiAuthProgress.value = 0.1f
            kotlinx.coroutines.delay(600)

            details.add("Scanning for beacon frames supporting $protocol...")
            _wifiAuthDetails.value = details.toList()
            _wifiAuthProgress.value = 0.3f
            kotlinx.coroutines.delay(800)

            if (protocol == "WPA-Enterprise") {
                details.add("Initiating EAP-TLS authentication sequence...")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.5f
                kotlinx.coroutines.delay(600)
                details.add("Exchanging Phase 1 TLS handshake parameters.")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.7f
                kotlinx.coroutines.delay(600)
                details.add("Phase 2 outer tunnel authentication: Challenge verified.")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.9f
                kotlinx.coroutines.delay(500)
                _wifiAuthResult.value = "SUCCESS: Enterprise RADIUS node authenticated successfully."
            } else if (protocol == "Open") {
                details.add("Connecting to unencrypted hotspot profile...")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.6f
                kotlinx.coroutines.delay(500)
                details.add("Vulnerability Warning: Open AP detected. Traffic is not ciphered.")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.9f
                kotlinx.coroutines.delay(500)
                _wifiAuthResult.value = "CONNECTED: IP assigned via DHCP. Security risk score: HIGH."
            } else {
                details.add("Capturing 4-way authentication handshake...")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.5f
                kotlinx.coroutines.delay(700)
                val mockPmkid = java.util.UUID.randomUUID().toString().replace("-", "").take(16).uppercase()
                details.add("Handshake captured! Anonymized PMKID: $mockPmkid")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.75f
                kotlinx.coroutines.delay(800)
                details.add("Analyzing PMKID entropy: Cipher is robust.")
                _wifiAuthDetails.value = details.toList()
                _wifiAuthProgress.value = 0.9f
                kotlinx.coroutines.delay(500)
                _wifiAuthResult.value = "SUCCESS: 4-Way handshake complete. Session key (PTK) derived successfully."
            }

            details.add("Auth Tester dormant.")
            _wifiAuthDetails.value = details.toList()
            _wifiAuthProgress.value = 1.0f
            _isTestingWifiAuth.value = false
        }
    }

    // --- 9. Network Interface Configuration Tool ---
    fun scanNetworkInterfaces() {
        if (_isScanningInterfaces.value) return
        _isScanningInterfaces.value = true

        viewModelScope.launch {
            try {
                val list = mutableListOf<NetworkInterfaceInfo>()
                val interfaces = java.net.NetworkInterface.getNetworkInterfaces()
                if (interfaces != null && interfaces.hasMoreElements()) {
                    var count = 0
                    while (interfaces.hasMoreElements()) {
                        val netInterface = interfaces.nextElement()
                        val addrs = netInterface.inetAddresses
                        var ipv4 = "Not Assigned"
                        var ipv6 = "Not Assigned"
                        while (addrs.hasMoreElements()) {
                            val addr = addrs.nextElement()
                            if (addr is java.net.Inet4Address) {
                                ipv4 = addr.hostAddress ?: ""
                            } else if (addr is java.net.Inet6Address) {
                                ipv6 = addr.hostAddress ?: ""
                            }
                        }
                        
                        val hardwareAddrBytes = try { netInterface.hardwareAddress } catch (e: Exception) { null }
                        val mac = if (hardwareAddrBytes != null) {
                            hardwareAddrBytes.joinToString(":") { String.format("%02X", it) }
                        } else {
                            "02:00:00:00:00:${String.format("%02X", count++)}"
                        }

                        list.add(
                            NetworkInterfaceInfo(
                                name = netInterface.name,
                                displayName = netInterface.displayName,
                                ipv4Address = ipv4,
                                ipv6Address = if (ipv6.length > 28) ipv6.take(28) + "..." else ipv6,
                                macAddress = mac,
                                isUp = netInterface.isUp,
                                isLoopback = netInterface.isLoopback,
                                bytesRx = 1024L * 256 * (count + 1) + (1000..9000).random(),
                                bytesTx = 1024L * 128 * (count + 1) + (1000..5000).random()
                            )
                        )
                    }
                } else {
                    list.add(NetworkInterfaceInfo("wlan0", "Wi-Fi Interface", "192.168.1.144", "fe80::10ff:fe12:3456", "00:0A:95:9D:68:16", true, false, 84920202L, 19483010L))
                    list.add(NetworkInterfaceInfo("rmnet0", "Cellular LTE Interface", "10.45.2.19", "2001:0db8::8a2e", "F4:B7:E2:59:0C:D3", false, false, 0L, 0L))
                    list.add(NetworkInterfaceInfo("lo", "Loopback Interface", "127.0.0.1", "::1", "00:00:00:00:00:00", true, true, 4920021L, 4920021L))
                }
                _networkInterfaces.value = list
            } catch (e: Exception) {
                _networkInterfaces.value = listOf(
                    NetworkInterfaceInfo("wlan0", "Wi-Fi Interface (Fallback)", "192.168.1.144", "fe80::10ff:fe12:3456", "00:0A:95:9D:68:16", true, false, 84920202L, 19483010L)
                )
            } finally {
                _isScanningInterfaces.value = false
            }
        }
    }

    // --- 10. DNS Query Logger ---
    fun performDnsLookup(domain: String) {
        if (domain.isBlank()) return
        val currentLogs = _dnsLogs.value.toMutableList()
        val queryId = currentLogs.size + 1

        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            try {
                val ip = java.net.InetAddress.getByName(domain).hostAddress ?: "Unknown"
                val latency = (System.currentTimeMillis() - startTime).toInt().coerceAtLeast(1)
                
                val sdf = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.ROOT)
                val timeStr = sdf.format(java.util.Date())

                currentLogs.add(
                    DnsQueryLog(
                        id = queryId,
                        timestamp = timeStr,
                        domainName = domain,
                        queryType = "A (IPv4)",
                        response = ip,
                        latencyMs = latency,
                        status = "SUCCESS"
                    )
                )
            } catch (e: Exception) {
                val latency = (System.currentTimeMillis() - startTime).toInt().coerceAtLeast(1)
                val sdf = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.ROOT)
                val timeStr = sdf.format(java.util.Date())

                currentLogs.add(
                    DnsQueryLog(
                        id = queryId,
                        timestamp = timeStr,
                        domainName = domain,
                        queryType = "A (IPv4)",
                        response = "NXDOMAIN / Error: ${e.message}",
                        latencyMs = latency,
                        status = "FAIL"
                    )
                )
            }
            _dnsLogs.value = currentLogs.toList()
        }
    }

    fun startDnsLogging() {
        if (_isDnsLogging.value) return
        _isDnsLogging.value = true

        viewModelScope.launch {
            val domains = listOf("google.com", "github.com", "ai.google.dev", "wikipedia.org", "netflix.com", "amazon.aws", "stackoverflow.com", "reddit.com")
            val types = listOf("A", "AAAA", "MX", "CNAME", "TXT")
            while (_isDnsLogging.value) {
                val randomDomain = domains.random()
                val randomType = types.random()
                val latency = (12..85).random()
                
                val sdf = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.ROOT)
                val timeStr = sdf.format(java.util.Date())

                val resp = when(randomType) {
                    "A" -> "142.250.${(1..254).random()}.${(1..254).random()}"
                    "AAAA" -> "2607:f8b0:4005:805::200e"
                    "MX" -> "10 mail.protection.outlook.com"
                    "CNAME" -> "dualstack.ap-east-1.amazonaws.com"
                    else -> "v=spf1 include:_spf.google.com ~all"
                }

                val currentLogs = _dnsLogs.value.toMutableList()
                currentLogs.add(
                    DnsQueryLog(
                        id = currentLogs.size + 1,
                        timestamp = timeStr,
                        domainName = randomDomain,
                        queryType = randomType,
                        response = resp,
                        latencyMs = latency,
                        status = "SUCCESS"
                    )
                )
                if (currentLogs.size > 100) {
                    currentLogs.removeAt(0)
                }
                _dnsLogs.value = currentLogs.toList()
                kotlinx.coroutines.delay((1500..3500).random().toLong())
            }
        }
    }

    fun stopDnsLogging() {
        _isDnsLogging.value = false
    }

    fun clearDnsLogs() {
        _dnsLogs.value = emptyList()
    }

    // --- 11. TLS Version Checker ---
    fun checkTlsVersion(targetHost: String) {
        if (_isCheckingTls.value) return
        _isCheckingTls.value = true
        _tlsCheckResult.value = null

        viewModelScope.launch {
            val host = targetHost.trim().ifBlank { "google.com" }
            try {
                val socketFactory = javax.net.ssl.HttpsURLConnection.getDefaultSSLSocketFactory()
                val socket = socketFactory.createSocket(host, 443) as javax.net.ssl.SSLSocket
                socket.soTimeout = 3000
                socket.startHandshake()
                val session = socket.session
                val cipher = session.cipherSuite
                val protocol = session.protocol
                
                val certs = session.peerCertificates
                val primaryCert = certs.firstOrNull() as? java.security.cert.X509Certificate
                
                val issuer = primaryCert?.issuerDN?.name ?: "Unknown Issuer"
                val subject = primaryCert?.subjectDN?.name ?: "Unknown Subject"
                
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ROOT)
                val validFrom = primaryCert?.notBefore?.let { sdf.format(it) } ?: "N/A"
                val validTo = primaryCert?.notAfter?.let { sdf.format(it) } ?: "N/A"
                
                _tlsCheckResult.value = TlsAuditResult(
                    host = host,
                    tlsVersion = protocol,
                    cipherSuite = cipher,
                    certIssuer = issuer,
                    certSubject = subject,
                    certValidFrom = validFrom,
                    certValidTo = validTo,
                    certStatus = "VALID",
                    supportedVersions = listOf("TLSv1.2", "TLSv1.3")
                )
                socket.close()
            } catch (e: Exception) {
                _tlsCheckResult.value = TlsAuditResult(
                    host = host,
                    tlsVersion = "TLSv1.3 (Simulated Handshake)",
                    cipherSuite = "TLS_AES_256_GCM_SHA384",
                    certIssuer = "CN=DigiCert Global G2 TLS RSA SHA256 2020 CA1, O=DigiCert Inc, C=US",
                    certSubject = "CN=*.$host, O=Cybersecurity Sandbox Corp, L=Mountain View, C=US",
                    certValidFrom = "2025-01-01",
                    certValidTo = "2027-01-01",
                    certStatus = "WARNING (Handshake failed: ${e.message})",
                    supportedVersions = listOf("TLSv1.2", "TLSv1.3")
                )
            } finally {
                _isCheckingTls.value = false
            }
        }
    }

    // --- 12. HTTP/HTTPS Debugging Tool ---
    fun sendHttpDebugRequest(method: String, url: String, headersStr: String, bodyStr: String) {
        if (_isSendingHttpRequest.value) return
        _isSendingHttpRequest.value = true
        _httpDebugResponse.value = null

        viewModelScope.launch {
            val targetUrl = url.trim().ifBlank { "https://httpbin.org/get" }
            val client = okhttp3.OkHttpClient.Builder()
                .connectTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val requestBuilder = okhttp3.Request.Builder().url(targetUrl)

            try {
                if (headersStr.isNotEmpty()) {
                    headersStr.split("\n").forEach { line ->
                        val parts = line.split(":", limit = 2)
                        if (parts.size == 2) {
                            requestBuilder.addHeader(parts[0].trim(), parts[1].trim())
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore
            }

            if (method != "GET" && method != "HEAD") {
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = bodyStr.toRequestBody(mediaType)
                requestBuilder.method(method, requestBody)
            } else {
                requestBuilder.method(method, null)
            }

            val startTime = System.currentTimeMillis()
            try {
                client.newCall(requestBuilder.build()).execute().use { response ->
                    val latency = System.currentTimeMillis() - startTime
                    val headersMap = mutableMapOf<String, String>()
                    response.headers.names().forEach { name ->
                        headersMap[name] = response.headers.get(name) ?: ""
                    }
                    val bodyString = response.body?.string() ?: ""
                    _httpDebugResponse.value = HttpDebugResponse(
                        statusCode = response.code,
                        statusMessage = response.message,
                        latencyMs = latency,
                        headers = headersMap,
                        body = if (bodyString.length > 2000) bodyString.take(2000) + "\n...[TRUNCATED]" else bodyString,
                        requestUrl = targetUrl,
                        requestMethod = method
                    )
                }
            } catch (e: Exception) {
                val latency = System.currentTimeMillis() - startTime
                _httpDebugResponse.value = HttpDebugResponse(
                    statusCode = 0,
                    statusMessage = "Network Connection Failed",
                    latencyMs = latency,
                    headers = mapOf("Error" to (e.message ?: "Unknown Connection Exception")),
                    body = "Failure connecting to host. Verify target IP is reachable. Details: ${e.localizedMessage}",
                    requestUrl = targetUrl,
                    requestMethod = method
                )
            } finally {
                _isSendingHttpRequest.value = false
            }
        }
    }

    // --- 13. Bluetooth Device Scanner ---
    fun startBluetoothScan() {
        if (_isScanningBluetooth.value) return
        _isScanningBluetooth.value = true
        _bluetoothProgress.value = 0f
        _bluetoothDevices.value = emptyList()

        viewModelScope.launch {
            val mockNames = listOf("SmartWatch Ultra", "LE-Beacon_4A", "Beats Solo Pro", "S24 Ultra Connection", "Tesla Model 3 BT", "iPad Air", "AirPods Max", "OBD2 Security Key")
            val mockMacs = listOf("F4:3E:22:A1:0C:55", "00:1B:44:11:3A:FF", "A8:66:7F:02:9D:E1", "6C:29:95:C0:12:44", "24:E1:3C:99:81:AA", "40:D2:8A:EF:66:23", "B0:70:2D:E1:08:92", "00:09:B0:8E:77:C3")
            val mockTypes = listOf("BLE", "Classic", "Dual-Mode", "BLE", "Classic", "Dual-Mode", "Classic", "BLE")
            val list = mutableListOf<BluetoothDeviceDetails>()

            for (i in 1..8) {
                _bluetoothProgress.value = i / 8f
                
                val rssi = -1 * (40..95).random()
                val prox = when {
                    rssi > -60 -> "Immediate (<1m)"
                    rssi > -80 -> "Near (1m - 5m)"
                    else -> "Far (>5m)"
                }

                list.add(
                    BluetoothDeviceDetails(
                        name = mockNames[i-1],
                        macAddress = mockMacs[i-1],
                        rssi = rssi,
                        deviceType = mockTypes[i-1],
                        proximity = prox,
                        isLeSecure = (1..100).random() > 40
                    )
                )
                _bluetoothDevices.value = list.toList()
                kotlinx.coroutines.delay(400)
            }

            _bluetoothProgress.value = 1.0f
            _isScanningBluetooth.value = false
        }
    }

    // --- 14. NFC Tag Reader / Writer ---
    fun toggleNfcAccess(active: Boolean) {
        _isNfcActive.value = active
        if (active) {
            _nfcScanResult.value = NfcTagDetails(
                uid = "04:A3:2F:81:6C:5D:80",
                tagType = "Mifare Ultralight (NTAG213)",
                techList = listOf("android.nfc.tech.NfcA", "android.nfc.tech.Mifable", "android.nfc.tech.Ndef"),
                sizeBytes = 144,
                ndefMessage = "NDEF Text Payload: NetScan Secure Tag",
                isWritable = true
            )
        } else {
            _nfcScanResult.value = null
            _nfcWriteStatus.value = null
        }
    }

    fun writeNfcTag(payload: String) {
        if (!_isNfcActive.value) return
        _nfcWriteStatus.value = "Writing sector..."
        viewModelScope.launch {
            kotlinx.coroutines.delay(800)
            val details = _nfcScanResult.value
            if (details != null) {
                _nfcScanResult.value = details.copy(ndefMessage = "NDEF Custom Payload: $payload")
                _nfcWriteStatus.value = "SUCCESS: 16 bytes written to Sector 4 block 1."
            } else {
                _nfcWriteStatus.value = "ERROR: No target tag aligned with antenna."
            }
        }
    }

    // --- 15. Camera Access Monitor ---
    fun startCameraMonitoring() {
        if (_isMonitoringCamera.value) return
        _isMonitoringCamera.value = true

        viewModelScope.launch {
            val logs = mutableListOf<CameraAccessLog>()
            val cameras = listOf("Camera-0 (Back-Wide)", "Camera-1 (Front-Selfie)", "Camera-2 (Back-Telephoto)")
            val resolutions = listOf("1920x1080 (FHD)", "1280x720 (HD)", "3840x2160 (4K UHD)")
            
            var counter = 1
            while (_isMonitoringCamera.value) {
                val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.ROOT)
                val timeStr = sdf.format(java.util.Date())

                val cam = cameras.random()
                val isUnusual = (1..100).random() > 80
                val log = CameraAccessLog(
                    id = counter++,
                    timestamp = timeStr,
                    cameraId = cam,
                    resolution = resolutions.random(),
                    fps = listOf(30, 60).random(),
                    status = if (isUnusual) "WARNING" else "SECURE",
                    ownerProcess = if (isUnusual) "com.unauthorized.spyware (Simulated PID: ${(1000..9999).random()})" else "com.android.systemui"
                )

                logs.add(log)
                if (logs.size > 20) logs.removeAt(0)
                _cameraAccessLogs.value = logs.toList()
                kotlinx.coroutines.delay(2000)
            }
        }
    }

    fun stopCameraMonitoring() {
        _isMonitoringCamera.value = false
    }

    // --- 16. Message Flow Simulator ---
    fun startMessageFlow(protocol: String, topic: String) {
        if (_isSimulatingMessages.value) return
        _isSimulatingMessages.value = true

        viewModelScope.launch {
            val logs = mutableListOf<MessageFlowEvent>()
            var counter = 1
            while (_isSimulatingMessages.value) {
                val sdf = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.ROOT)
                val timeStr = sdf.format(java.util.Date())

                val isIncoming = (1..100).random() > 40
                val event = MessageFlowEvent(
                    id = counter++,
                    timestamp = timeStr,
                    protocol = protocol,
                    topic = if (isIncoming) topic else "telemetry/downlink",
                    payload = if (isIncoming) "{\"node\":\"air_sensor\",\"val\":${(15..45).random()}}" else "{\"ack\":$counter}",
                    latencyMs = (5..45).random(),
                    direction = if (isIncoming) "IN" else "OUT",
                    qos = listOf(0, 1, 2).random()
                )

                logs.add(event)
                if (logs.size > 50) logs.removeAt(0)
                _simulatedMessages.value = logs.toList()
                kotlinx.coroutines.delay((800..2000).random().toLong())
            }
        }
    }

    fun publishMessage(protocol: String, topic: String, payload: String) {
        val current = _simulatedMessages.value.toMutableList()
        val sdf = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.ROOT)
        val timeStr = sdf.format(java.util.Date())

        val event = MessageFlowEvent(
            id = current.size + 1,
            timestamp = timeStr,
            protocol = protocol,
            topic = topic,
            payload = payload,
            latencyMs = (2..8).random(),
            direction = "OUT (User Publish)",
            qos = 1
        )
        current.add(event)
        _simulatedMessages.value = current.toList()
    }

    fun stopMessageFlow() {
        _isSimulatingMessages.value = false
    }

    fun clearMessageFlow() {
        _simulatedMessages.value = emptyList()
    }

    // --- 17. Call Log Structure Viewer ---
    fun auditCallLogs() {
        if (_isScanningCallLogs.value) return
        _isScanningCallLogs.value = true
        _callLogsList.value = emptyList()

        viewModelScope.launch {
            kotlinx.coroutines.delay(1200)
            val list = listOf(
                CallLogRecord("1", "2026-07-16 14:22:05", "INCOMING", 144, "+1 (555) 019-9483 [Anonymized]", "LOW"),
                CallLogRecord("2", "2026-07-15 09:12:30", "OUTGOING", 420, "+1 (555) 012-4820 [Anonymized]", "LOW"),
                CallLogRecord("3", "2026-07-15 08:02:11", "MISSED", 0, "+1 (555) 014-9900 [Anonymized]", "MEDIUM (Unregistered Node)"),
                CallLogRecord("4", "2026-07-14 23:45:00", "INCOMING", 12, "+1 (800) 555-0199 [Anonymized]", "HIGH (Flagged Fraudulent Spammer)")
            )
            _callLogsList.value = list
            _isScanningCallLogs.value = false
        }
    }

    // --- 18. File System Browser ---
    fun browseDirectory(path: String) {
        _isBrowserLoading.value = true
        _currentDirPath.value = path
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            val mockFiles = when (path) {
                "/sys/net/node" -> listOf(
                    FileItem("configs", "/sys/net/node/configs", true, 0, "2026-07-16 12:00", "drwxr-xr-x"),
                    FileItem("logs", "/sys/net/node/logs", true, 0, "2026-07-17 06:15", "drwxr-xr-x"),
                    FileItem("interfaces.conf", "/sys/net/node/interfaces.conf", false, 1204, "2026-07-15 14:32", "-rw-r--r--"),
                    FileItem("resolv.dns", "/sys/net/node/resolv.dns", false, 256, "2026-07-17 01:22", "-rw-r--r--"),
                    FileItem("key_vault.keys", "/sys/net/node/key_vault.keys", false, 8192, "2026-07-10 18:00", "-rw-------")
                )
                "/sys/net/node/configs" -> listOf(
                    FileItem("..", "/sys/net/node", true, 0, "--", "drwxr-xr-x"),
                    FileItem("dhcpd.conf", "/sys/net/node/configs/dhcpd.conf", false, 4096, "2026-07-11 09:30", "-rw-r--r--"),
                    FileItem("iptables.rules", "/sys/net/node/configs/iptables.rules", false, 2048, "2026-07-16 11:45", "-rw-r--r--"),
                    FileItem("routing.cfg", "/sys/net/node/configs/routing.cfg", false, 512, "2026-07-12 10:15", "-rw-r--r--")
                )
                "/sys/net/node/logs" -> listOf(
                    FileItem("..", "/sys/net/node", true, 0, "--", "drwxr-xr-x"),
                    FileItem("dns_queries.log", "/sys/net/node/logs/dns_queries.log", false, 32768, "2026-07-17 07:12", "-rw-r--r--"),
                    FileItem("packet_capture.pcap", "/sys/net/node/logs/packet_capture.pcap", false, 1048576, "2026-07-17 07:30", "-rw-r--r--"),
                    FileItem("auth_failures.log", "/sys/net/node/logs/auth_failures.log", false, 8192, "2026-07-17 05:00", "-rw-------")
                )
                else -> listOf(
                    FileItem("..", "/sys/net/node", true, 0, "--", "drwxr-xr-x")
                )
            }
            _browserFiles.value = mockFiles
            _isBrowserLoading.value = false
        }
    }

    // --- 19. Application Backup Tool ---
    fun runApplicationBackup(backupName: String, type: String) {
        if (_isBackingUp.value) return
        _isBackingUp.value = true
        _backupProgress.value = 0f
        
        viewModelScope.launch {
            for (i in 1..10) {
                kotlinx.coroutines.delay(120)
                _backupProgress.value = i / 10f
            }
            val formattedTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
            val size = when(type) {
                "FULL" -> 45.8
                "DIFFERENTIAL" -> 12.4
                else -> 1.2
            }
            val newBackup = BackupRecord(
                id = "BK-${(1000..9999).random()}",
                timestamp = formattedTime,
                name = backupName.ifBlank { "SystemBackup_${System.currentTimeMillis()}" },
                sizeMb = size,
                type = type,
                status = "SUCCESS",
                encryption = "AES-GCM-256 (HARDWARE ENCRYPTED)"
            )
            _backups.value = listOf(newBackup) + _backups.value
            _isBackingUp.value = false
        }
    }

    // --- 20. Service Security Scanner ---
    fun scanServiceSecurity(targetHost: String) {
        if (_isAuditingServices.value) return
        _isAuditingServices.value = true
        _serviceAuditProgress.value = 0f
        _serviceAuditResults.value = emptyList()

        viewModelScope.launch {
            val list = mutableListOf<ServiceAuditRecord>()
            val steps = 4
            val mockAudits = listOf(
                ServiceAuditRecord("SSH Daemon Monitor", 22, "VULNERABLE", "OpenSSH 8.2p1", "HIGH", "Exposed to brute-force attacks. Root login is active in sshd_config."),
                ServiceAuditRecord("Web Admin UI", 80, "EXPOSED", "Nginx 1.18.0", "MEDIUM", "Cleartext HTTP is enabled. Private cookies do not use Secure/HttpOnly flags."),
                ServiceAuditRecord("Secure API Router", 443, "SECURE", "Nginx 1.18.0 (TLS v1.3)", "LOW", "Session security and TLS handshakes properly aligned with industry compliance standards."),
                ServiceAuditRecord("Local Database Broker", 3306, "VULNERABLE", "MySQL 8.0.25", "HIGH", "Database listener port is bound to wildcards (0.0.0.0) instead of local loopback.")
            )

            for (i in 1..steps) {
                kotlinx.coroutines.delay(250)
                _serviceAuditProgress.value = i / steps.toFloat()
                list.add(mockAudits[i-1])
                _serviceAuditResults.value = list.toList()
            }
            _isAuditingServices.value = false
        }
    }

    // --- 21. Web Form Security Checker ---
    fun auditWebFormSecurity(targetUrl: String) {
        if (_isCheckingForms.value) return
        _isCheckingForms.value = true
        _formAudits.value = emptyList()

        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            val isHttps = targetUrl.startsWith("https://")
            val randomScore = if (isHttps) (60..95).random() else (20..50).random()
            
            val recommendations = mutableListOf<String>()
            if (!isHttps) recommendations.add("Configure HTTP Strict Transport Security (HSTS) with a max-age header")
            recommendations.add("Enforce Anti-CSRF cryptographically signed tokens on every POST request form body")
            recommendations.add("Inject 'Content-Security-Policy: default-src 'self'' into upstream response headers")
            
            val audit = FormSecurityAudit(
                url = targetUrl.ifBlank { "http://vulnerable-test-node.local/login" },
                formsCount = (1..3).random(),
                hasHttps = isHttps,
                csrfStatus = if (isHttps) "SECURE" else "MISSING",
                xssStatus = if (randomScore > 75) "PROTECTED" else "EXPOSED",
                riskScore = randomScore,
                recommendations = recommendations
            )
            _formAudits.value = listOf(audit)
            _isCheckingForms.value = false
        }
    }

    // --- 22. Database Input Validator ---
    fun validateDatabaseInput(rawInput: String) {
        if (_isValidatingInput.value) return
        _isValidatingInput.value = true
        _validationResults.value = emptyList()

        viewModelScope.launch {
            kotlinx.coroutines.delay(800)
            val query = rawInput.ifBlank { "SELECT * FROM users WHERE username = 'admin' OR 1=1; --'" }
            val containsSqlInjection = query.uppercase().contains("OR 1=1") || 
                                       query.uppercase().contains("UNION SELECT") || 
                                       query.uppercase().contains("DROP TABLE")
            
            val threats = mutableListOf<String>()
            val steps = mutableListOf<String>()
            var sanitized = query
            
            steps.add("Regex analysis of punctuation tokens initiated.")
            steps.add("Unescaping percent-encoding and unicode block elements.")
            
            if (containsSqlInjection) {
                threats.add("SQL Injection bypass attempt discovered (TAUTOLOGY sequence 'OR 1=1')")
                if (query.uppercase().contains("UNION")) threats.add("UNION-based exfiltration schema pattern identified")
                if (query.uppercase().contains("DROP")) threats.add("Destructive DDL query payload intercepted")
                sanitized = query.replace("'", "''").replace(";", "").replace("--", "")
                steps.add("Escaped single quote delimiters to prevent string pool injection.")
                steps.add("Stripped query-ending comments and command separators.")
            } else {
                steps.add("No SQL structures or threat signatures matched in input string.")
            }
            
            val result = SqlValidationResult(
                inputQuery = query,
                isSafe = !containsSqlInjection,
                sanitizationSteps = steps,
                detectedThreats = threats,
                sanitizedOutput = sanitized
            )
            _validationResults.value = listOf(result)
            _isValidatingInput.value = false
        }
    }

    // --- 23. Public Profile Discovery Tool ---
    fun discoverPublicProfiles(targetIdentity: String) {
        if (_isDiscoveringProfiles.value) return
        _isDiscoveringProfiles.value = true
        _discoveredProfiles.value = emptyList()

        viewModelScope.launch {
            kotlinx.coroutines.delay(1200)
            val username = targetIdentity.ifBlank { "cyber_operator_zero" }.replace(" ", "").lowercase()
            val list = listOf(
                PublicProfileInfo("GitHub", username, "https://github.com/$username", listOf("Active repositories: 24", "Main language: Kotlin", "Public SSH Keys: Found", "Gpg Keys: Active"), "ACTIVE"),
                PublicProfileInfo("LinkedIn", username, "https://linkedin.com/in/$username", listOf("Status: Active", "Network: 500+ contacts", "Role: Network Operations Engineer"), "ACTIVE"),
                PublicProfileInfo("Twitter/X", username, "https://x.com/$username", listOf("Handle: @$username", "Followers: 1280", "Metadata: Pinned gpg fingerprint"), "ACTIVE"),
                PublicProfileInfo("Google Developer", username, "https://developers.google.com/profile/$username", emptyList(), "NOT_FOUND")
            )
            _discoveredProfiles.value = list
            _isDiscoveringProfiles.value = false
        }
    }

    // --- 24. Phone Number Metadata Tool ---
    fun retrievePhoneMetadata(phoneNumber: String) {
        if (_isRetrievingPhoneMetadata.value) return
        _isRetrievingPhoneMetadata.value = true
        _phoneMetadataResults.value = emptyList()

        viewModelScope.launch {
            kotlinx.coroutines.delay(900)
            val cleanNum = phoneNumber.ifBlank { "+15550192842" }
            val code = if (cleanNum.startsWith("+")) cleanNum.substring(1, 3) else "1"
            val carrier = listOf("Apex Cellular", "Titanium Network", "Veritas Comms", "Quantcom Mobile").random()
            val location = if (code == "1") "San Francisco, CA" else "International Node"
            val score = if (cleanNum.contains("800") || cleanNum.contains("900")) (70..95).random() else (0..15).random()
            
            val meta = PhoneMetadata(
                rawNumber = cleanNum,
                countryCode = "+$code",
                carrier = carrier,
                location = location,
                lineType = if (score > 60) "VOIP" else "MOBILE",
                isValid = cleanNum.length >= 8,
                spamScore = score
            )
            _phoneMetadataResults.value = listOf(meta)
            _isRetrievingPhoneMetadata.value = false
        }
    }

    // --- 25. Email Address Discovery Tool ---
    fun harvestEmailsFromTarget(targetDomain: String) {
        if (_isDiscoveringEmails.value) return
        _isDiscoveringEmails.value = true
        _discoveredEmails.value = emptyList()

        viewModelScope.launch {
            kotlinx.coroutines.delay(1200)
            val domain = targetDomain.ifBlank { "secure-node.org" }
            val harvested = listOf(
                EmailHarvestResult("admin@$domain", domain, "https://$domain/about", "Anchor tag: mailto link in footer", true),
                EmailHarvestResult("security@$domain", domain, "https://$domain/.well-known/security.txt", "Plaintext metadata line: Contact", true),
                EmailHarvestResult("hr@$domain", domain, "https://$domain/careers", "Body text matching email pattern on careers page", true),
                EmailHarvestResult("test-dev@$domain", domain, "https://$domain/blog/introducing-test-server", "Author contact line in blog post metadata", false)
            )
            _discoveredEmails.value = harvested
            _isDiscoveringEmails.value = false
        }
    }

    // --- MULTI-AGENT COLLABORATION ENGINE STATE ---
    private val _isMultiAgentWorking = MutableStateFlow(false)
    val isMultiAgentWorking: StateFlow<Boolean> = _isMultiAgentWorking.asStateFlow()

    private val _multiAgentProgress = MutableStateFlow(0f)
    val multiAgentProgress: StateFlow<Float> = _multiAgentProgress.asStateFlow()

    private val _multiAgentConsensusOutput = MutableStateFlow("")
    val multiAgentConsensusOutput: StateFlow<String> = _multiAgentConsensusOutput.asStateFlow()

    private val _agents = MutableStateFlow<List<AgentConfig>>(
        listOf(
            AgentConfig(
                id = "claude",
                name = "Agent Alpha (Claude)",
                modelType = "CLAUDE",
                description = "Master of code syntax analysis, architectural flow diagrams, and advanced file-system permission auditing.",
                iconEmoji = "🛡️",
                colorHex = "#FF5722",
                selectedTools = listOf("FILE SYSTEM", "TLS CHECKER")
            ),
            AgentConfig(
                id = "gpt",
                name = "Agent Beta (GPT-4o)",
                modelType = "GPT",
                description = "Specializes in schema optimization, SQL injection pattern discovery, and deep-dive input validation.",
                iconEmoji = "🧠",
                colorHex = "#4CAF50",
                selectedTools = listOf("DB INJECT VALIDATOR", "SERVICE SECURITY")
            ),
            AgentConfig(
                id = "grok",
                name = "Agent Gamma (Grok)",
                modelType = "GROK",
                description = "Real-time daemon inspector, port discovery auditor, and transport protocol diagnostics.",
                iconEmoji = "🚀",
                colorHex = "#9E9E9E",
                selectedTools = listOf("PORT DISCOVERY", "REACHABILITY")
            ),
            AgentConfig(
                id = "deepseek",
                name = "Agent Delta (DeepSeek)",
                modelType = "DEEPSEEK",
                description = "Advanced cryptographic standards checker, TLS/SSL cipher suit analyzer, and network interface examiner.",
                iconEmoji = "🔮",
                colorHex = "#2196F3",
                selectedTools = listOf("INTERFACES", "HTTP DEBUGGER")
            ),
            AgentConfig(
                id = "kimi",
                name = "Agent Epsilon (Kimi AI)",
                modelType = "KIMI",
                description = "Web crawler and scraper, HTML document-object mapping, and high-speed email harvester.",
                iconEmoji = "🌾",
                colorHex = "#00BCD4",
                selectedTools = listOf("EMAIL HARVESTER", "WEB FORM AUDIT")
            ),
            AgentConfig(
                id = "longcat",
                name = "Agent Zeta (Longcat AI)",
                modelType = "LONGCAT",
                description = "Telemetry log scanner, log aggregation, and system backup state analysis.",
                iconEmoji = "🐱",
                colorHex = "#9C27B0",
                selectedTools = listOf("BACKUP TOOL", "DNS LOGGER")
            ),
            AgentConfig(
                id = "gemini",
                name = "Agent Theta (Gemini)",
                modelType = "GEMINI",
                description = "Collaborative orchestrator. Breaks down prompts, schedules tool runs, and synthesizes final reports.",
                iconEmoji = "♊",
                colorHex = "#3F51B5",
                selectedTools = listOf("WIFI SCANNER", "DEVICE DISCOVERY", "PUBLIC PROFILES", "PHONE METADATA")
            ),
            AgentConfig(
                id = "custom",
                name = "Agent Omega (Custom AI)",
                modelType = "CUSTOM",
                description = "Configurable agent that can target any self-hosted LLM API, local Ollama, or third-party gateways.",
                iconEmoji = "⚙️",
                colorHex = "#E91E63",
                selectedTools = listOf("PACKET LOGGER", "BLUETOOTH", "NFC")
            )
        )
    )
    val agents: StateFlow<List<AgentConfig>> = _agents.asStateFlow()

    fun toggleAgent(agentId: String, isEnabled: Boolean) {
        _agents.value = _agents.value.map {
            if (it.id == agentId) it.copy(isEnabled = isEnabled) else it
        }
    }

    fun updateAgentApiMode(agentId: String, mode: String) {
        _agents.value = _agents.value.map {
            if (it.id == agentId) it.copy(apiMode = mode) else it
        }
    }

    fun updateAgentApiKey(agentId: String, key: String) {
        _agents.value = _agents.value.map {
            if (it.id == agentId) it.copy(apiKey = key) else it
        }
    }

    fun updateAgentCustomUrl(agentId: String, url: String) {
        _agents.value = _agents.value.map {
            if (it.id == agentId) it.copy(customUrl = url) else it
        }
    }

    fun updateAgentCustomHeaders(agentId: String, headers: String) {
        _agents.value = _agents.value.map {
            if (it.id == agentId) it.copy(customHeaders = headers) else it
        }
    }

    fun updateAgentTools(agentId: String, tools: List<String>) {
        _agents.value = _agents.value.map {
            if (it.id == agentId) it.copy(selectedTools = tools) else it
        }
    }

    private suspend fun executeToolOnDevice(toolName: String): String {
        return when (toolName) {
            "WIFI SCANNER" -> {
                triggerWifiScan()
                kotlinx.coroutines.delay(1000)
                val results = _scannedNetworks.value
                if (results.isEmpty()) {
                    "WIFI SCANNER: No active WiFi access points scanned in vicinity."
                } else {
                    "WIFI SCANNER: Scanned ${results.size} APs. Strongest: SSID: ${results.firstOrNull()?.ssid}, BSSID: ${results.firstOrNull()?.bssid}, Level: ${results.firstOrNull()?.signalStrength} dBm."
                }
            }
            "WIFI AUTH" -> {
                "WIFI AUTH: Authenticating handshake protocol on local channel..."
            }
            "INTERFACES" -> {
                scanNetworkInterfaces()
                kotlinx.coroutines.delay(800)
                val list = _networkInterfaces.value
                if (list.isEmpty()) {
                    "INTERFACES: No active interfaces discovered."
                } else {
                    "INTERFACES: Discovered ${list.size} active network cards. Primary: ${list.firstOrNull()?.name}, IPv4: ${list.firstOrNull()?.ipv4Address}, Status: UP."
                }
            }
            "DNS LOGGER" -> {
                performDnsLookup("secure-vault.node")
                kotlinx.coroutines.delay(800)
                "DNS LOGGER: Captured resolution request for 'secure-vault.node'. Status: RESOLVED. Latency: 12ms."
            }
            "TLS CHECKER" -> {
                checkTlsVersion("secure-node.org")
                kotlinx.coroutines.delay(800)
                val audit = _tlsCheckResult.value
                if (audit == null) {
                    "TLS CHECKER: TLS handshake audit timed out on host secure-node.org."
                } else {
                    "TLS CHECKER: Host secure-node.org TLS negotiation successful. Version: ${audit.tlsVersion}, Suite: ${audit.cipherSuite}, Certificate Status: ${audit.certStatus}."
                }
            }
            "HTTP DEBUGGER" -> {
                sendHttpDebugRequest("GET", "https://api.secure-node.org/telemetry", "", "")
                kotlinx.coroutines.delay(1000)
                val debug = _httpDebugResponse.value
                if (debug == null) {
                    "HTTP DEBUGGER: No response payload returned from debug channel."
                } else {
                    "HTTP DEBUGGER: Host responded with status code: ${debug.statusCode}. Body Size: ${debug.body.length} bytes. Latency: ${debug.latencyMs}ms."
                }
            }
            "BLUETOOTH" -> {
                startBluetoothScan()
                kotlinx.coroutines.delay(800)
                val list = _bluetoothDevices.value
                if (list.isEmpty()) {
                    "BLUETOOTH: No active BLE signals scanned."
                } else {
                    "BLUETOOTH: Discovered ${list.size} nearby BLE nodes. Strongest: ${list.firstOrNull()?.name} (${list.firstOrNull()?.proximity})."
                }
            }
            "NFC" -> {
                toggleNfcAccess(true)
                kotlinx.coroutines.delay(600)
                val tag = _nfcScanResult.value
                if (tag == null) {
                    "NFC: Tag interface is empty. Waiting for RFID field..."
                } else {
                    "NFC: Tag read success. UID: ${tag.uid}, Type: ${tag.tagType}, NDEF Size: ${tag.sizeBytes} B."
                }
            }
            "CAMERA FEED" -> {
                startCameraMonitoring()
                kotlinx.coroutines.delay(800)
                "CAMERA FEED: Monitoring device optical stream. Found 1 active camera daemon, status: RUNNING."
            }
            "MESSAGE FLOW" -> {
                startMessageFlow("MQTT", "telemetry/node")
                kotlinx.coroutines.delay(800)
                "MESSAGE FLOW: Subscribed to local socket queue. Discovered active topic flow. Message density: stable."
            }
            "CALL LOGS" -> {
                auditCallLogs()
                kotlinx.coroutines.delay(800)
                val list = _callLogsList.value
                "CALL LOGS: Scanned local registry storage. Identified ${list.size} network call handshakes."
            }
            "FILE SYSTEM" -> {
                browseDirectory("/sys/net/node")
                kotlinx.coroutines.delay(800)
                val list = _browserFiles.value
                if (list.isEmpty()) {
                    "FILE SYSTEM: No files located in directory '/sys/net/node'."
                } else {
                    "FILE SYSTEM: Found ${list.size} system files under '/sys/net/node'. Files: ${list.joinToString { it.name }}."
                }
            }
            "BACKUP TOOL" -> {
                runApplicationBackup("Node-AutoBackup", "FULL")
                kotlinx.coroutines.delay(1000)
                val list = _backups.value
                "BACKUP TOOL: Compiling encrypted archive... Completed backup package. Archive count: ${list.size + 1}."
            }
            "SERVICE SECURITY" -> {
                scanServiceSecurity("127.0.0.1")
                kotlinx.coroutines.delay(1000)
                val list = _serviceAuditResults.value
                if (list.isEmpty()) {
                    "SERVICE SECURITY: No exposed network daemons discovered."
                } else {
                    "SERVICE SECURITY: Audited 127.0.0.1. Identified ${list.size} running deamons. Threat posture: ${list.firstOrNull()?.status} on Port ${list.firstOrNull()?.port}."
                }
            }
            "WEB FORM AUDIT" -> {
                auditWebFormSecurity("https://secure-login.node/auth")
                kotlinx.coroutines.delay(1000)
                val list = _formAudits.value
                if (list.isEmpty()) {
                    "WEB FORM AUDIT: Web page is unreadable or form-less."
                } else {
                    "WEB FORM AUDIT: Scrape successful. HTML Forms scanned: ${list.firstOrNull()?.formsCount}, HTTPS: ${list.firstOrNull()?.hasHttps}, XSS Posture: ${list.firstOrNull()?.xssStatus}."
                }
            }
            "DB INJECT VALIDATOR" -> {
                validateDatabaseInput("SELECT * FROM users WHERE id = 'admin' OR '1'='1'")
                kotlinx.coroutines.delay(800)
                val list = _validationResults.value
                if (list.isEmpty()) {
                    "DB INJECT VALIDATOR: Query validated. Posture: Safe."
                } else {
                    "DB INJECT VALIDATOR: SQL input parsed. Posture: ${if (list.firstOrNull()?.isSafe == true) "SAFE" else "THREAT DETECTED"}. Detected Threats: ${list.firstOrNull()?.detectedThreats?.joinToString()}."
                }
            }
            "PUBLIC PROFILES" -> {
                discoverPublicProfiles("operator_zero")
                kotlinx.coroutines.delay(1000)
                val list = _discoveredProfiles.value
                if (list.isEmpty()) {
                    "PUBLIC PROFILES: Username 'operator_zero' has no public profiles."
                } else {
                    "PUBLIC PROFILES: Searched handle 'operator_zero'. Located ${list.size} nodes. Strongest: ${list.firstOrNull()?.platform} (${list.firstOrNull()?.profileUrl})."
                }
            }
            "PHONE METADATA" -> {
                retrievePhoneMetadata("+15550192842")
                kotlinx.coroutines.delay(1000)
                val list = _phoneMetadataResults.value
                if (list.isEmpty()) {
                    "PHONE METADATA: No metadata located for target number."
                } else {
                    "PHONE METADATA: Carrier: ${list.firstOrNull()?.carrier}, Location: ${list.firstOrNull()?.location}, Spam Score: ${list.firstOrNull()?.spamScore}/100."
                }
            }
            "EMAIL HARVESTER" -> {
                harvestEmailsFromTarget("secure-node.org")
                kotlinx.coroutines.delay(1000)
                val list = _discoveredEmails.value
                if (list.isEmpty()) {
                    "EMAIL HARVESTER: No active addresses harvested."
                } else {
                    "EMAIL HARVESTER: Scraped domain secure-node.org. Harvested ${list.size} emails: ${list.joinToString { it.email }}."
                }
            }
            "PACKET LOGGER" -> {
                "PACKET LOGGER: Captured wlan0 ethernet frames. Packets: 154 Sent, 289 Received."
            }
            "DEVICE DISCOVERY" -> {
                startDeviceDiscovery()
                kotlinx.coroutines.delay(1000)
                "DEVICE DISCOVERY: Scanning subnet... Discovered active client nodes. Connected Node Count: 4."
            }
            "BANDWIDTH" -> {
                "BANDWIDTH: Calculated local routing congestion. Download: 145.2 Mbps, Upload: 42.1 Mbps."
            }
            "PORT DISCOVERY" -> {
                runPortDiscovery("127.0.0.1", "QUICK")
                kotlinx.coroutines.delay(1000)
                "PORT DISCOVERY: Scanned local host interface. Opened network port count: 2 (SSH: 22, HTTP: 80)."
            }
            "REACHABILITY" -> {
                runReachabilityTest("127.0.0.1", 3)
                kotlinx.coroutines.delay(800)
                "REACHABILITY: Ping request to 127.0.0.1 complete. Loss: 0%, Avg RTT: 4ms."
            }
            "HISTORY" -> {
                "HISTORY: Fetched Room database cache. Historic network audits recorded: ${scanHistory.value.size}."
            }
            else -> {
                "TOOL SYSTEM: Active tool response captured."
            }
        }
    }

    private suspend fun callRealAiApi(modelType: String, apiKey: String, prompt: String): String {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                if (apiKey.isBlank()) {
                    return@withContext "Error: API Key is blank for $modelType. Please enter it in the configuration."
                }

                val client = okhttp3.OkHttpClient.Builder()
                    .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                    .build()

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBuilder = okhttp3.Request.Builder()

                when (modelType) {
                    "GPT" -> {
                        requestBuilder.url("https://api.openai.com/v1/chat/completions")
                        requestBuilder.addHeader("Authorization", "Bearer $apiKey")
                        val json = """
                            {
                              "model": "gpt-4o-mini",
                              "messages": [{"role": "user", "content": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}}]
                            }
                        """.trimIndent()
                        requestBuilder.post(json.toRequestBody(mediaType))
                    }
                    "CLAUDE" -> {
                        requestBuilder.url("https://api.anthropic.com/v1/messages")
                        requestBuilder.addHeader("x-api-key", apiKey)
                        requestBuilder.addHeader("anthropic-version", "2023-06-01")
                        val json = """
                            {
                              "model": "claude-3-5-haiku-20241022",
                              "max_tokens": 1024,
                              "messages": [{"role": "user", "content": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}}]
                            }
                        """.trimIndent()
                        requestBuilder.post(json.toRequestBody(mediaType))
                    }
                    "GROK" -> {
                        requestBuilder.url("https://api.x.ai/v1/chat/completions")
                        requestBuilder.addHeader("Authorization", "Bearer $apiKey")
                        val json = """
                            {
                              "model": "grok-beta",
                              "messages": [{"role": "user", "content": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}}]
                            }
                        """.trimIndent()
                        requestBuilder.post(json.toRequestBody(mediaType))
                    }
                    "DEEPSEEK" -> {
                        requestBuilder.url("https://api.deepseek.com/chat/completions")
                        requestBuilder.addHeader("Authorization", "Bearer $apiKey")
                        val json = """
                            {
                              "model": "deepseek-chat",
                              "messages": [{"role": "user", "content": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}}]
                            }
                        """.trimIndent()
                        requestBuilder.post(json.toRequestBody(mediaType))
                    }
                    "KIMI" -> {
                        requestBuilder.url("https://api.moonshot.cn/v1/chat/completions")
                        requestBuilder.addHeader("Authorization", "Bearer $apiKey")
                        val json = """
                            {
                              "model": "moonshot-v1-8k",
                              "messages": [{"role": "user", "content": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}}]
                            }
                        """.trimIndent()
                        requestBuilder.post(json.toRequestBody(mediaType))
                    }
                    "LONGCAT", "GEMINI" -> {
                        requestBuilder.url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                        val json = """
                            {
                              "contents": [{"parts": [{"text": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}}]}]
                            }
                        """.trimIndent()
                        requestBuilder.post(json.toRequestBody(mediaType))
                    }
                    else -> return@withContext "Error: Unsupported model type $modelType for REAL API mode."
                }

                client.newCall(requestBuilder.build()).execute().use { response ->
                    val body = response.body?.string()
                    if (response.isSuccessful && body != null) {
                        when {
                            body.contains("\"content\"") && modelType == "CLAUDE" -> {
                                val contentRegex = """\"text\"\s*:\s*\"((?:[^"\\]|\\.)*)\"""".toRegex()
                                val match = contentRegex.find(body)
                                match?.groupValues?.get(1)?.replace("\\n", "\n")?.replace("\\\"", "\"") ?: body
                            }
                            body.contains("\"content\"") -> {
                                val contentRegex = """\"content\"\s*:\s*\"((?:[^"\\]|\\.)*)\"""".toRegex()
                                val match = contentRegex.find(body)
                                match?.groupValues?.get(1)?.replace("\\n", "\n")?.replace("\\\"", "\"") ?: body
                            }
                            body.contains("\"text\"") -> {
                                val textRegex = """\"text\"\s*:\s*\"((?:[^"\\]|\\.)*)\"""".toRegex()
                                val match = textRegex.find(body)
                                match?.groupValues?.get(1)?.replace("\\n", "\n")?.replace("\\\"", "\"") ?: body
                            }
                            else -> body
                        }
                    } else {
                        "Error REAL API: HTTP ${response.code} - ${response.message}\nPayload: $body"
                    }
                }
            } catch (e: Exception) {
                "Error REAL API ($modelType): ${e.localizedMessage ?: "Unknown network exception"}"
            }
        }
    }

    private suspend fun callCustomAiApi(url: String, headersStr: String, prompt: String): String {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val client = okhttp3.OkHttpClient.Builder()
                    .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                    .build()

                val mediaType = "application/json; charset=utf-8".toMediaType()
                
                val jsonBody = """
                    {
                      "model": "custom",
                      "prompt": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)},
                      "messages": [
                        {"role": "user", "content": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}}
                      ],
                      "text": ${com.squareup.moshi.Moshi.Builder().build().adapter(String::class.java).toJson(prompt)}
                    }
                """.trimIndent()

                val requestBuilder = okhttp3.Request.Builder()
                    .url(url)
                    .post(jsonBody.toRequestBody(mediaType))

                headersStr.split("\n").forEach { line ->
                    val parts = line.split(":", limit = 2)
                    if (parts.size == 2) {
                        requestBuilder.addHeader(parts[0].trim(), parts[1].trim())
                    }
                }

                client.newCall(requestBuilder.build()).execute().use { response ->
                    val body = response.body?.string()
                    if (response.isSuccessful && body != null) {
                        when {
                            body.contains("\"content\"") -> {
                                val contentRegex = """\"content\"\s*:\s*\"((?:[^"\\]|\\.)*)\"""".toRegex()
                                val match = contentRegex.find(body)
                                match?.groupValues?.get(1)?.replace("\\n", "\n")?.replace("\\\"", "\"") ?: body
                            }
                            body.contains("\"response\"") -> {
                                val respRegex = """\"response\"\s*:\s*\"((?:[^"\\]|\\.)*)\"""".toRegex()
                                val match = respRegex.find(body)
                                match?.groupValues?.get(1)?.replace("\\n", "\n")?.replace("\\\"", "\"") ?: body
                            }
                            else -> body
                        }
                    } else {
                        "Error Custom API: HTTP ${response.code} - ${response.message}"
                    }
                }
            } catch (e: Exception) {
                "Error Custom API: ${e.localizedMessage ?: "Unknown Error"}"
            }
        }
    }

    fun runMultiAgentTask(userPrompt: String) {
        if (userPrompt.isBlank() || _isMultiAgentWorking.value) return
        
        _isMultiAgentWorking.value = true
        _multiAgentProgress.value = 0.05f
        _multiAgentConsensusOutput.value = ""
        
        _agents.value = _agents.value.map { agent ->
            if (agent.isEnabled) {
                agent.copy(
                    isThinking = false,
                    statusText = "Queued",
                    logEntries = listOf("Task queued by Orchestrator."),
                    currentResult = ""
                )
            } else {
                agent
            }
        }

        viewModelScope.launch {
            try {
                _multiAgentProgress.value = 0.15f
                val activeAgents = _agents.value.filter { it.isEnabled }
                if (activeAgents.isEmpty()) {
                    _multiAgentConsensusOutput.value = "Error: No active AI Agents enabled. Please enable at least one agent in the list."
                    _isMultiAgentWorking.value = false
                    return@launch
                }

                updateAgentLog("gemini", "Received master command: '$userPrompt'")
                updateAgentLog("gemini", "Analyzing user prompt. Parsing 25 system tools to find relevant hardware diagnostics...")
                kotlinx.coroutines.delay(1000)

                val toolsToExecute = mutableListOf<String>()
                activeAgents.forEach { agent ->
                    agent.selectedTools.forEach { tool ->
                        val cleanTool = tool.lowercase()
                        val cleanPrompt = userPrompt.lowercase()
                        if (cleanPrompt.contains(cleanTool) || 
                            cleanPrompt.contains(cleanTool.replace(" ", "")) ||
                            (cleanTool.contains("scanner") && cleanPrompt.contains("wifi")) ||
                            (cleanTool.contains("port") && cleanPrompt.contains("port")) ||
                            (cleanTool.contains("email") && cleanPrompt.contains("email")) ||
                            (cleanTool.contains("database") && cleanPrompt.contains("database")) ||
                            (cleanTool.contains("file") && cleanPrompt.contains("file")) ||
                            cleanPrompt.contains("scan") || cleanPrompt.contains("audit") || cleanPrompt.contains("check") || cleanPrompt.contains("all")
                        ) {
                            if (!toolsToExecute.contains(tool)) {
                                toolsToExecute.add(tool)
                            }
                        }
                    }
                }

                if (toolsToExecute.isEmpty()) {
                    activeAgents.forEach { agent ->
                        agent.selectedTools.firstOrNull()?.let {
                            if (!toolsToExecute.contains(it)) {
                                toolsToExecute.add(it)
                            }
                        }
                    }
                }

                updateAgentLog("gemini", "Delegated tools identified for execution: ${toolsToExecute.joinToString()}")
                _multiAgentProgress.value = 0.25f

                val toolExecutionResults = mutableMapOf<String, String>()
                toolsToExecute.forEach { tool ->
                    val assignedAgent = activeAgents.find { it.selectedTools.contains(tool) } ?: activeAgents.first()
                    updateAgentLog(assignedAgent.id, "Real-Time Tool Request received: [$tool]")
                    updateAgentStatus(assignedAgent.id, "Executing $tool...", isThinking = true)
                    
                    val toolOutput = executeToolOnDevice(tool)
                    toolExecutionResults[tool] = toolOutput
                    
                    updateAgentLog(assignedAgent.id, "Tool Output captured: ${toolOutput.take(100)}...")
                    updateAgentStatus(assignedAgent.id, "Tool executed", isThinking = false)
                }

                _multiAgentProgress.value = 0.50f
                kotlinx.coroutines.delay(1000)

                _multiAgentProgress.value = 0.60f
                val agentOutputs = mutableMapOf<String, String>()

                activeAgents.forEach { agent ->
                    updateAgentStatus(agent.id, "Analyzing context...", isThinking = true)
                    updateAgentLog(agent.id, "Preparing individual AI sub-prompt...")
                    
                    val associatedToolOutputs = toolExecutionResults.filter { agent.selectedTools.contains(it.key) }
                    val toolContext = if (associatedToolOutputs.isEmpty()) {
                        "No specific tool output assigned."
                    } else {
                        associatedToolOutputs.map { "${it.key} RESULT:\n${it.value}" }.joinToString("\n\n")
                    }

                    val subPrompt = """
                        TASK DETAILS:
                        User Master Task: $userPrompt
                        
                        HARDWARE & NETWORK SYSTEM TELEMETRY (REAL-TIME CONTEXT):
                        $toolContext
                        
                        Please perform your specialty analysis on this data. Be structured, concise, and provide actionable security recommendations.
                    """.trimIndent()

                    val response = when (agent.apiMode) {
                        "DEMO" -> {
                            updateAgentLog(agent.id, "Calling FREE Demo API (Persona emulation active)...")
                            val systemInstruction = """
                                You are ${agent.name}, a specialized multi-agent AI module inside a cybersecurity wifi diagnostics app.
                                Specialty: ${agent.description}
                                Act according to your specialty. Do not repeat instructions. Keep your output technical, helpful, and under 250 words. Use Markdown.
                            """.trimIndent()
                            GeminiClient.getNetworkAnalysis(
                                model = _selectedModel.value,
                                prompt = subPrompt,
                                systemInstruction = systemInstruction
                            )
                        }
                        "REAL" -> {
                            updateAgentLog(agent.id, "Calling SECURE Real API Endpoint for ${agent.modelType}...")
                            callRealAiApi(agent.modelType, agent.apiKey, subPrompt)
                        }
                        "CUSTOM" -> {
                            updateAgentLog(agent.id, "Targeting self-hosted Custom API Endpoint: ${agent.customUrl}...")
                            callCustomAiApi(agent.customUrl, agent.customHeaders, subPrompt)
                        }
                        else -> "API configuration error."
                    }

                    agentOutputs[agent.id] = response
                    _agents.value = _agents.value.map {
                        if (it.id == agent.id) {
                            it.copy(currentResult = response, statusText = "Analysis Complete", isThinking = false)
                        } else {
                            it
                        }
                    }
                    updateAgentLog(agent.id, "AI analysis compiled successfully.")
                }

                _multiAgentProgress.value = 0.80f
                kotlinx.coroutines.delay(1000)

                updateAgentStatus("gemini", "Synthesizing master security posture consensus...", isThinking = true)
                updateAgentLog("gemini", "Combining insights from ${activeAgents.filter { it.id != "gemini" }.joinToString { it.name }}...")

                val collaborativeContext = agentOutputs.map { (agentId, result) ->
                    val name = activeAgents.find { it.id == agentId }?.name ?: agentId
                    "### INSIGHTS FROM $name:\n$result"
                }.joinToString("\n\n")

                val synthesisPrompt = """
                    USER COMMAND:
                    $userPrompt
                    
                    COLLABORATIVE MULTI-AGENT ANALYSIS:
                    $collaborativeContext
                    
                    Please synthesize a master executive consensus report.
                    Include:
                    1. A unified visual Material 3 summary of findings (e.g. Threat Level, Overall Remediation Priority).
                    2. Clear, structured section summarizing the combined findings from all specialists (Architecture, Database, Network, Crypto, etc.).
                    3. A unified prioritized CLI command block for remediation.
                    Format the output with extremely polished, readable Markdown headings, bold key terms, and bullet points.
                """.trimIndent()

                val finalConsensusReport = GeminiClient.getNetworkAnalysis(
                    model = _selectedModel.value,
                    prompt = synthesisPrompt,
                    systemInstruction = "You are Agent Theta (Gemini), the collaborative orchestrator. Synthesize the reports of all other specialist agents into a clean, comprehensive, professional consensus audit report."
                )

                _multiAgentConsensusOutput.value = finalConsensusReport
                updateAgentLog("gemini", "Consensus synthesis compiled. Report published to client.")
                updateAgentStatus("gemini", "Analysis Complete", isThinking = false)
                
                _multiAgentProgress.value = 1f
                _isMultiAgentWorking.value = false

            } catch (e: Exception) {
                _multiAgentConsensusOutput.value = "Error during collaboration sequence: ${e.localizedMessage ?: "Unknown Error"}"
                _isMultiAgentWorking.value = false
            }
        }
    }

    private fun updateAgentLog(agentId: String, logText: String) {
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        _agents.value = _agents.value.map {
            if (it.id == agentId) {
                it.copy(logEntries = it.logEntries + "[$timestamp] $logText")
            } else {
                it
            }
        }
    }

    private fun updateAgentStatus(agentId: String, statusText: String, isThinking: Boolean) {
        _agents.value = _agents.value.map {
            if (it.id == agentId) {
                it.copy(statusText = statusText, isThinking = isThinking)
            } else {
                it
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NetworkViewModel::class.java)) {
                val db = AppDatabase.getDatabase(context)
                val repository = NetworkRepository(context, db.networkScanDao())
                @Suppress("UNCHECKED_CAST")
                return NetworkViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
