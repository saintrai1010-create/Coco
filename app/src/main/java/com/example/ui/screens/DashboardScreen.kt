package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.NetworkScanResult
import com.example.data.model.WifiNetworkInfo
import com.example.ui.theme.*
import com.example.viewmodel.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: NetworkViewModel,
    innerPadding: PaddingValues
) {
    val activeConnection by viewModel.activeConnection.collectAsStateWithLifecycle()
    val scannedNetworks by viewModel.scannedNetworks.collectAsStateWithLifecycle()
    val isScanningWifi by viewModel.isScanningWifi.collectAsStateWithLifecycle()
    val isTestingDiagnostics by viewModel.isTestingDiagnostics.collectAsStateWithLifecycle()
    val diagnosticProgress by viewModel.diagnosticProgress.collectAsStateWithLifecycle()
    val currentTestName by viewModel.currentTestName.collectAsStateWithLifecycle()

    val pingGatewayVal by viewModel.pingGateway.collectAsStateWithLifecycle()
    val pingDnsVal by viewModel.pingDns.collectAsStateWithLifecycle()
    val pingGoogleVal by viewModel.pingGoogle.collectAsStateWithLifecycle()
    val dnsLatencyVal by viewModel.dnsLookupLatency.collectAsStateWithLifecycle()
    val httpLatencyVal by viewModel.httpLatency.collectAsStateWithLifecycle()
    val openPortsVal by viewModel.openPorts.collectAsStateWithLifecycle()

    val scanHistory by viewModel.scanHistory.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isGeminiLoading by viewModel.isGeminiLoading.collectAsStateWithLifecycle()
    val selectedModel by viewModel.selectedModel.collectAsStateWithLifecycle()
    val enableHighThinking by viewModel.enableHighThinking.collectAsStateWithLifecycle()
    val isAiAnalysisGenerating by viewModel.isAiAnalysisGenerating.collectAsStateWithLifecycle()
    val currentScanAiAnalysis by viewModel.currentScanAiAnalysis.collectAsStateWithLifecycle()

    // 5 New Tool States
    val isLoggingPackets by viewModel.isLoggingPackets.collectAsStateWithLifecycle()
    val packetLogs by viewModel.packetLogs.collectAsStateWithLifecycle()

    val isDiscoveringDevices by viewModel.isDiscoveringDevices.collectAsStateWithLifecycle()
    val discoveredDevices by viewModel.discoveredDevices.collectAsStateWithLifecycle()
    val discoveryProgress by viewModel.discoveryProgress.collectAsStateWithLifecycle()

    val isBandwidthTesting by viewModel.isBandwidthTesting.collectAsStateWithLifecycle()
    val bandwidthProgress by viewModel.bandwidthProgress.collectAsStateWithLifecycle()
    val downloadSpeed by viewModel.downloadSpeed.collectAsStateWithLifecycle()
    val uploadSpeed by viewModel.uploadSpeed.collectAsStateWithLifecycle()
    val peakSpeed by viewModel.peakSpeed.collectAsStateWithLifecycle()
    val averageSpeed by viewModel.averageSpeed.collectAsStateWithLifecycle()
    val bandwidthDataPoints by viewModel.bandwidthDataPoints.collectAsStateWithLifecycle()
    val bandwidthStage by viewModel.bandwidthStage.collectAsStateWithLifecycle()

    val isScanningPorts by viewModel.isScanningPorts.collectAsStateWithLifecycle()
    val portScanProgress by viewModel.portScanProgress.collectAsStateWithLifecycle()
    val portScanResults by viewModel.portScanResults.collectAsStateWithLifecycle()

    val isTestingReachability by viewModel.isTestingReachability.collectAsStateWithLifecycle()
    val reachabilityProgress by viewModel.reachabilityProgress.collectAsStateWithLifecycle()
    val reachabilityLogs by viewModel.reachabilityLogs.collectAsStateWithLifecycle()
    val minRtt by viewModel.minRtt.collectAsStateWithLifecycle()
    val maxRtt by viewModel.maxRtt.collectAsStateWithLifecycle()
    val avgRtt by viewModel.avgRtt.collectAsStateWithLifecycle()
    val packetsSent by viewModel.packetsSent.collectAsStateWithLifecycle()
    val packetsReceived by viewModel.packetsReceived.collectAsStateWithLifecycle()

    // 8 New Tool States
    val currentDirPath by viewModel.currentDirPath.collectAsStateWithLifecycle()
    val browserFiles by viewModel.browserFiles.collectAsStateWithLifecycle()
    val isBrowserLoading by viewModel.isBrowserLoading.collectAsStateWithLifecycle()

    val backups by viewModel.backups.collectAsStateWithLifecycle()
    val isBackingUp by viewModel.isBackingUp.collectAsStateWithLifecycle()
    val backupProgress by viewModel.backupProgress.collectAsStateWithLifecycle()

    val serviceAuditResults by viewModel.serviceAuditResults.collectAsStateWithLifecycle()
    val isAuditingServices by viewModel.isAuditingServices.collectAsStateWithLifecycle()
    val serviceAuditProgress by viewModel.serviceAuditProgress.collectAsStateWithLifecycle()

    val formAudits by viewModel.formAudits.collectAsStateWithLifecycle()
    val isCheckingForms by viewModel.isCheckingForms.collectAsStateWithLifecycle()

    val validationResults by viewModel.validationResults.collectAsStateWithLifecycle()
    val isValidatingInput by viewModel.isValidatingInput.collectAsStateWithLifecycle()

    val discoveredProfiles by viewModel.discoveredProfiles.collectAsStateWithLifecycle()
    val isDiscoveringProfiles by viewModel.isDiscoveringProfiles.collectAsStateWithLifecycle()

    val phoneMetadataResults by viewModel.phoneMetadataResults.collectAsStateWithLifecycle()
    val isRetrievingPhoneMetadata by viewModel.isRetrievingPhoneMetadata.collectAsStateWithLifecycle()

    val discoveredEmails by viewModel.discoveredEmails.collectAsStateWithLifecycle()
    val isDiscoveringEmails by viewModel.isDiscoveringEmails.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("PRIVACY") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassBg)
            .padding(innerPadding)
            .drawBehind {
                // Background Mesh Gradients
                // Top-left organic blue glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GlassGlowBlue, Color.Transparent),
                        center = Offset(size.width * 0.1f, size.height * 0.1f),
                        radius = size.width * 0.7f
                    ),
                    center = Offset(size.width * 0.1f, size.height * 0.1f),
                    radius = size.width * 0.7f
                )
                // Bottom-right organic purple glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GlassGlowPurple, Color.Transparent),
                        center = Offset(size.width * 0.9f, size.height * 0.9f),
                        radius = size.width * 0.7f
                    ),
                    center = Offset(size.width * 0.9f, size.height * 0.9f),
                    radius = size.width * 0.7f
                )
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- TOP HEADER BANNER ---
            HeaderBanner(
                status = activeConnection.status,
                ssid = activeConnection.ssid,
                bssid = activeConnection.bssid,
                signalPercent = activeConnection.signalPercentage,
                ipAddress = activeConnection.ipAddress,
                onRefresh = { viewModel.refreshActiveConnection() }
            )

            // --- TAB SELECTOR NAVIGATION ---
            TabSelector(
                selectedTab = activeTab,
                onTabSelected = { activeTab = it }
            )

            // --- MAIN PANELS VIEW ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    "PRIVACY" -> {
                        PrivacyDashboardPanel(viewModel = viewModel)
                    }
                    "DIAGNOSTICS" -> {
                        DiagnosticsPanel(
                            isTesting = isTestingDiagnostics,
                            progress = diagnosticProgress,
                            currentTest = currentTestName,
                            pingGateway = pingGatewayVal,
                            pingDns = pingDnsVal,
                            pingGoogle = pingGoogleVal,
                            dnsLatency = dnsLatencyVal,
                            httpLatency = httpLatencyVal,
                            openPorts = openPortsVal,
                            aiAnalysis = currentScanAiAnalysis,
                            isGeneratingAi = isAiAnalysisGenerating,
                            gatewayIp = activeConnection.gateway,
                            onRunTest = { viewModel.runConnectivityDiagnostics() },
                            onGenerateAiAnalysis = { viewModel.generateAiAnalysisForCurrentScan() }
                        )
                    }
                    "WIFI SCANNER" -> {
                        WifiScannerPanel(
                            isScanning = isScanningWifi,
                            networks = scannedNetworks,
                            onTriggerScan = { viewModel.triggerWifiScan() }
                        )
                    }
                    "WIFI AUTH" -> {
                        val isTestingWifiAuth by viewModel.isTestingWifiAuth.collectAsStateWithLifecycle()
                        val wifiAuthProgress by viewModel.wifiAuthProgress.collectAsStateWithLifecycle()
                        val wifiAuthResult by viewModel.wifiAuthResult.collectAsStateWithLifecycle()
                        val wifiAuthDetails by viewModel.wifiAuthDetails.collectAsStateWithLifecycle()
                        WifiAuthPanel(
                            isTesting = isTestingWifiAuth,
                            progress = wifiAuthProgress,
                            result = wifiAuthResult,
                            details = wifiAuthDetails,
                            onStartTest = { viewModel.runWifiAuthTest(it) }
                        )
                    }
                    "INTERFACES" -> {
                        val isScanningInterfaces by viewModel.isScanningInterfaces.collectAsStateWithLifecycle()
                        val networkInterfaces by viewModel.networkInterfaces.collectAsStateWithLifecycle()
                        NetworkInterfacesPanel(
                            isScanning = isScanningInterfaces,
                            interfaces = networkInterfaces,
                            onTriggerScan = { viewModel.scanNetworkInterfaces() }
                        )
                    }
                    "DNS LOGGER" -> {
                        val isDnsLogging by viewModel.isDnsLogging.collectAsStateWithLifecycle()
                        val dnsLogs by viewModel.dnsLogs.collectAsStateWithLifecycle()
                        DnsLoggerPanel(
                            isLogging = isDnsLogging,
                            logs = dnsLogs,
                            onStartLogging = { viewModel.startDnsLogging() },
                            onStopLogging = { viewModel.stopDnsLogging() },
                            onPerformLookup = { viewModel.performDnsLookup(it) },
                            onClearLogs = { viewModel.clearDnsLogs() }
                        )
                    }
                    "TLS CHECKER" -> {
                        val isCheckingTls by viewModel.isCheckingTls.collectAsStateWithLifecycle()
                        val tlsCheckResult by viewModel.tlsCheckResult.collectAsStateWithLifecycle()
                        TlsCheckerPanel(
                            isChecking = isCheckingTls,
                            result = tlsCheckResult,
                            onCheckTls = { viewModel.checkTlsVersion(it) }
                        )
                    }
                    "HTTP DEBUGGER" -> {
                        val isSendingHttpRequest by viewModel.isSendingHttpRequest.collectAsStateWithLifecycle()
                        val httpDebugResponse by viewModel.httpDebugResponse.collectAsStateWithLifecycle()
                        HttpDebuggerPanel(
                            isSending = isSendingHttpRequest,
                            response = httpDebugResponse,
                            onSendRequest = { method, url, headers, body ->
                                viewModel.sendHttpDebugRequest(method, url, headers, body)
                            }
                        )
                    }
                    "BLUETOOTH" -> {
                        val isScanningBluetooth by viewModel.isScanningBluetooth.collectAsStateWithLifecycle()
                        val bluetoothProgress by viewModel.bluetoothProgress.collectAsStateWithLifecycle()
                        val bluetoothDevices by viewModel.bluetoothDevices.collectAsStateWithLifecycle()
                        BluetoothScannerPanel(
                            isScanning = isScanningBluetooth,
                            progress = bluetoothProgress,
                            devices = bluetoothDevices,
                            onTriggerScan = { viewModel.startBluetoothScan() }
                        )
                    }
                    "NFC" -> {
                        val isNfcActive by viewModel.isNfcActive.collectAsStateWithLifecycle()
                        val nfcScanResult by viewModel.nfcScanResult.collectAsStateWithLifecycle()
                        val nfcWriteStatus by viewModel.nfcWriteStatus.collectAsStateWithLifecycle()
                        NfcPanel(
                            isActive = isNfcActive,
                            scanResult = nfcScanResult,
                            writeStatus = nfcWriteStatus,
                            onToggleActive = { viewModel.toggleNfcAccess(it) },
                            onWriteTag = { viewModel.writeNfcTag(it) }
                        )
                    }
                    "CAMERA FEED" -> {
                        val isMonitoringCamera by viewModel.isMonitoringCamera.collectAsStateWithLifecycle()
                        val cameraAccessLogs by viewModel.cameraAccessLogs.collectAsStateWithLifecycle()
                        CameraMonitorPanel(
                            isMonitoring = isMonitoringCamera,
                            logs = cameraAccessLogs,
                            onStartMonitoring = { viewModel.startCameraMonitoring() },
                            onStopMonitoring = { viewModel.stopCameraMonitoring() }
                        )
                    }
                    "MESSAGE FLOW" -> {
                        val isSimulatingMessages by viewModel.isSimulatingMessages.collectAsStateWithLifecycle()
                        val simulatedMessages by viewModel.simulatedMessages.collectAsStateWithLifecycle()
                        MessageFlowPanel(
                            isSimulating = isSimulatingMessages,
                            messages = simulatedMessages,
                            onStartFlow = { protocol, topic -> viewModel.startMessageFlow(protocol, topic) },
                            onStopFlow = { viewModel.stopMessageFlow() },
                            onPublishMessage = { protocol, topic, payload -> viewModel.publishMessage(protocol, topic, payload) },
                            onClearMessages = { viewModel.clearMessageFlow() }
                        )
                    }
                    "CALL LOGS" -> {
                        val isScanningCallLogs by viewModel.isScanningCallLogs.collectAsStateWithLifecycle()
                        val callLogsList by viewModel.callLogsList.collectAsStateWithLifecycle()
                        CallLogsPanel(
                            isScanning = isScanningCallLogs,
                            logs = callLogsList,
                            onTriggerAudit = { viewModel.auditCallLogs() }
                        )
                    }
                    "FILE SYSTEM" -> {
                        FileSystemPanel(
                            currentPath = currentDirPath,
                            files = browserFiles,
                            isLoading = isBrowserLoading,
                            onBrowse = { viewModel.browseDirectory(it) }
                        )
                    }
                    "BACKUP TOOL" -> {
                        BackupToolPanel(
                            isBackingUp = isBackingUp,
                            progress = backupProgress,
                            backups = backups,
                            onTriggerBackup = { name, type -> viewModel.runApplicationBackup(name, type) }
                        )
                    }
                    "SERVICE SECURITY" -> {
                        ServiceSecurityPanel(
                            isScanning = isAuditingServices,
                            progress = serviceAuditProgress,
                            results = serviceAuditResults,
                            onTriggerScan = { viewModel.scanServiceSecurity(it) }
                        )
                    }
                    "WEB FORM AUDIT" -> {
                        WebFormAuditPanel(
                            isChecking = isCheckingForms,
                            audits = formAudits,
                            onTriggerAudit = { viewModel.auditWebFormSecurity(it) }
                        )
                    }
                    "DB INJECT VALIDATOR" -> {
                        DbInjectValidatorPanel(
                            isValidating = isValidatingInput,
                            results = validationResults,
                            onValidate = { viewModel.validateDatabaseInput(it) }
                        )
                    }
                    "PUBLIC PROFILES" -> {
                        PublicProfilesPanel(
                            isDiscovering = isDiscoveringProfiles,
                            profiles = discoveredProfiles,
                            onTriggerSearch = { viewModel.discoverPublicProfiles(it) }
                        )
                    }
                    "PHONE METADATA" -> {
                        PhoneMetadataPanel(
                            isRetrieving = isRetrievingPhoneMetadata,
                            results = phoneMetadataResults,
                            onTriggerScan = { viewModel.retrievePhoneMetadata(it) }
                        )
                    }
                    "EMAIL HARVESTER" -> {
                        EmailHarvesterPanel(
                            isHarvesting = isDiscoveringEmails,
                            emails = discoveredEmails,
                            onTriggerScan = { viewModel.harvestEmailsFromTarget(it) }
                        )
                    }
                    "AI CO-PILOT" -> {
                        AiCoPilotPanel(
                            messages = chatMessages,
                            isLoading = isGeminiLoading,
                            selectedModel = selectedModel,
                            enableHighThinking = enableHighThinking,
                            onModelSelected = { viewModel.setModel(it) },
                            onHighThinkingToggled = { viewModel.setHighThinking(it) },
                            onSendMessage = { viewModel.sendChatMessage(it) }
                        )
                    }
                    "MULTI-AGENT" -> {
                        val isWorking by viewModel.isMultiAgentWorking.collectAsStateWithLifecycle()
                        val progress by viewModel.multiAgentProgress.collectAsStateWithLifecycle()
                        val consensusOutput by viewModel.multiAgentConsensusOutput.collectAsStateWithLifecycle()
                        val agentsList by viewModel.agents.collectAsStateWithLifecycle()

                        MultiAgentPanel(
                            isWorking = isWorking,
                            progress = progress,
                            consensusOutput = consensusOutput,
                            agents = agentsList,
                            onRunTask = { viewModel.runMultiAgentTask(it) },
                            onToggleAgent = { id, enabled -> viewModel.toggleAgent(id, enabled) },
                            onUpdateApiMode = { id, mode -> viewModel.updateAgentApiMode(id, mode) },
                            onUpdateApiKey = { id, key -> viewModel.updateAgentApiKey(id, key) },
                            onUpdateCustomUrl = { id, url -> viewModel.updateAgentCustomUrl(id, url) },
                            onUpdateCustomHeaders = { id, headers -> viewModel.updateAgentCustomHeaders(id, headers) },
                            onUpdateAgentTools = { id, tools -> viewModel.updateAgentTools(id, tools) }
                        )
                    }
                    "PACKET LOGGER" -> {
                        PacketLoggerPanel(
                            isLogging = isLoggingPackets,
                            logs = packetLogs,
                            onToggleLogging = { viewModel.togglePacketLogging() },
                            onClearLogs = { viewModel.clearPacketLogs() }
                        )
                    }
                    "DEVICE DISCOVERY" -> {
                        DeviceDiscoveryPanel(
                            isDiscovering = isDiscoveringDevices,
                            devices = discoveredDevices,
                            progress = discoveryProgress,
                            onStartDiscovery = { viewModel.startDeviceDiscovery() }
                        )
                    }
                    "BANDWIDTH" -> {
                        BandwidthPanel(
                            isTesting = isBandwidthTesting,
                            progress = bandwidthProgress,
                            downloadSpeed = downloadSpeed,
                            uploadSpeed = uploadSpeed,
                            peakSpeed = peakSpeed,
                            averageSpeed = averageSpeed,
                            dataPoints = bandwidthDataPoints,
                            stage = bandwidthStage,
                            onStartTest = { viewModel.startBandwidthTest() }
                        )
                    }
                    "PORT DISCOVERY" -> {
                        PortDiscoveryPanel(
                            isScanning = isScanningPorts,
                            progress = portScanProgress,
                            results = portScanResults,
                            gatewayIp = activeConnection.gateway,
                            onStartScan = { host, profile, start, end ->
                                viewModel.runPortDiscovery(host, profile, start, end)
                            }
                        )
                    }
                    "REACHABILITY" -> {
                        ReachabilityPanel(
                            isTesting = isTestingReachability,
                            progress = reachabilityProgress,
                            logs = reachabilityLogs,
                            minRtt = minRtt,
                            maxRtt = maxRtt,
                            avgRtt = avgRtt,
                            sent = packetsSent,
                            received = packetsReceived,
                            onStartTest = { host, count ->
                                viewModel.runReachabilityTest(host, count)
                            }
                        )
                    }
                    "HISTORY" -> {
                        HistoryPanel(
                            history = scanHistory,
                            onDelete = { viewModel.deleteScanHistory(it) },
                            onClearAll = { viewModel.clearAllHistory() }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// COMPOSABLE COMPONENT IMPLEMENTATIONS
// ==========================================

@Composable
fun HeaderBanner(
    status: String,
    ssid: String,
    bssid: String,
    signalPercent: Int,
    ipAddress: String,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = GlassSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            if (status.contains("Connected")) GlassAccentGreen.copy(alpha = 0.15f) else GlassAccentRed.copy(alpha = 0.15f),
                            RoundedCornerShape(50)
                        )
                        .border(
                            1.dp,
                            if (status.contains("Connected")) GlassAccentGreen.copy(alpha = 0.3f) else GlassAccentRed.copy(alpha = 0.3f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (status.contains("Connected")) GlassAccentGreen else GlassAccentRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = status.uppercase(Locale.ROOT),
                        color = if (status.contains("Connected")) GlassAccentGreen else GlassAccentRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (ssid.isNotEmpty()) ssid else "NO DIRECT CONNECTION",
                    color = TextUltraLight,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                if (bssid.isNotEmpty() && bssid != "00:00:00:00:00:00") {
                    Text(
                        text = "BSSID: $bssid | IP: $ipAddress",
                        color = TextLightSlate,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else if (ipAddress.isNotEmpty()) {
                    Text(
                        text = "LOCAL IP: $ipAddress",
                        color = TextLightSlate,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (status.contains("WiFi") && signalPercent > 0) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(GlassSurfaceVariant, RoundedCornerShape(16.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = when {
                            signalPercent >= 75 -> Icons.Filled.Wifi
                            signalPercent >= 45 -> Icons.Filled.NetworkWifi3Bar
                            else -> Icons.Filled.NetworkWifi1Bar
                        },
                        contentDescription = "WiFi Signal",
                        tint = GlassAccentGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$signalPercent%",
                        color = GlassAccentGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            } else {
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(48.dp)
                        .background(GlassSurfaceVariant, RoundedCornerShape(16.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Active Connection State",
                        tint = GlassAccentBlue
                    )
                }
            }
        }
    }
}

@Composable
fun TabSelector(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf(
        "PRIVACY",
        "DIAGNOSTICS", 
        "WIFI SCANNER", 
        "WIFI AUTH",
        "INTERFACES",
        "DNS LOGGER",
        "TLS CHECKER",
        "HTTP DEBUGGER",
        "BLUETOOTH",
        "NFC",
        "CAMERA FEED",
        "MESSAGE FLOW",
        "CALL LOGS",
        "FILE SYSTEM",
        "BACKUP TOOL",
        "SERVICE SECURITY",
        "WEB FORM AUDIT",
        "DB INJECT VALIDATOR",
        "PUBLIC PROFILES",
        "PHONE METADATA",
        "EMAIL HARVESTER",
        "AI CO-PILOT", 
        "MULTI-AGENT",
        "PACKET LOGGER", 
        "DEVICE DISCOVERY", 
        "BANDWIDTH", 
        "PORT DISCOVERY", 
        "REACHABILITY", 
        "HISTORY"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        tabs.forEach { tab ->
            val isSelected = selectedTab == tab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) GlassSurfaceVariant else GlassSurface)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) GlassAccentBlue else GlassBorder,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = tab,
                    color = if (isSelected) GlassAccentBlue else TextLightSlate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun DiagnosticsPanel(
    isTesting: Boolean,
    progress: Float,
    currentTest: String,
    pingGateway: Int,
    pingDns: Int,
    pingGoogle: Int,
    dnsLatency: Int,
    httpLatency: Int,
    openPorts: List<Int>,
    aiAnalysis: String?,
    isGeneratingAi: Boolean,
    gatewayIp: String,
    onRunTest: () -> Unit,
    onGenerateAiAnalysis: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // TRIGGER AND PROGRESS CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isTesting) {
                    Text(
                        text = currentTest.uppercase(Locale.ROOT),
                        color = GlassAccentBlue,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = GlassAccentBlue,
                        trackColor = GlassBorder
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${(progress * 100).toInt()}% COMPLETE",
                        color = TextLightSlate,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    Button(
                        onClick = onRunTest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_diagnostics_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Run Telemetry Diagnostics",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RUN FULL DIAGNOSTIC",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
        // TELEMETRY GRID
        Text(
            text = "TELEMETRY & SPEED DELAYS",
            color = TextLightSlate,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TelemetryBox(
                modifier = Modifier.weight(1f),
                title = "GATEWAY PING",
                value = if (pingGateway == -1) "--" else "$pingGateway ms",
                status = when {
                    pingGateway == -1 -> "TIMEOUT"
                    pingGateway < 10 -> "EXCELLENT"
                    pingGateway < 40 -> "MODERATE"
                    else -> "HIGH LAG"
                },
                color = when {
                    pingGateway == -1 -> GlassAccentRed
                    pingGateway < 10 -> GlassAccentGreen
                    pingGateway < 40 -> GlassAccentAmber
                    else -> GlassAccentRed
                }
            )

            TelemetryBox(
                modifier = Modifier.weight(1f),
                title = "DNS SERVER",
                value = if (pingDns == -1) "--" else "$pingDns ms",
                status = when {
                    pingDns == -1 -> "TIMEOUT"
                    pingDns < 20 -> "EXCELLENT"
                    pingDns < 60 -> "GOOD"
                    else -> "DELAYED"
                },
                color = when {
                    pingDns == -1 -> GlassAccentRed
                    pingDns < 20 -> GlassAccentGreen
                    pingDns < 60 -> GlassAccentBlue
                    else -> GlassAccentAmber
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TelemetryBox(
                modifier = Modifier.weight(1f),
                title = "WAN PING (8.8.8.8)",
                value = if (pingGoogle == -1) "--" else "$pingGoogle ms",
                status = when {
                    pingGoogle == -1 -> "TIMEOUT"
                    pingGoogle < 35 -> "EXCELLENT"
                    pingGoogle < 80 -> "GOOD"
                    else -> "HIGH JITTER"
                },
                color = when {
                    pingGoogle == -1 -> GlassAccentRed
                    pingGoogle < 35 -> GlassAccentGreen
                    pingGoogle < 80 -> GlassAccentBlue
                    else -> GlassAccentAmber
                }
            )

            TelemetryBox(
                modifier = Modifier.weight(1f),
                title = "DNS LOOKUP",
                value = if (dnsLatency == -1) "--" else "$dnsLatency ms",
                status = when {
                    dnsLatency == -1 -> "FAILED"
                    dnsLatency < 15 -> "INSTANT"
                    dnsLatency < 80 -> "NORMAL"
                    else -> "SLOW LOOKUP"
                },
                color = when {
                    dnsLatency == -1 -> GlassAccentRed
                    dnsLatency < 15 -> GlassAccentGreen
                    dnsLatency < 80 -> GlassAccentBlue
                    else -> GlassAccentAmber
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TelemetryBox(
                modifier = Modifier.weight(1f),
                title = "HTTP LATENCY",
                value = if (httpLatency == -1) "--" else "$httpLatency ms",
                status = when {
                    httpLatency == -1 -> "TIMEOUT"
                    httpLatency < 120 -> "FAST"
                    httpLatency < 350 -> "NORMAL"
                    else -> "CONGESTED"
                },
                color = when {
                    httpLatency == -1 -> GlassAccentRed
                    httpLatency < 120 -> GlassAccentGreen
                    httpLatency < 350 -> GlassAccentBlue
                    else -> GlassAccentAmber
                }
            )

            TelemetryBox(
                modifier = Modifier.weight(1f),
                title = "PORT AUDIT",
                value = if (openPorts.isEmpty()) "SAFE" else "${openPorts.size} OPEN",
                status = if (openPorts.isEmpty()) "NO PORTS OPEN" else "OPEN: ${openPorts.joinToString(",")}",
                color = if (openPorts.isEmpty()) GlassAccentGreen else GlassAccentAmber
            )
        }

        // AI OPTIMIZATION SUMMARY ACTION
        if (pingGoogle != -1) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "GEMINI INTELLIGENT DIAGNOSTICS",
                        color = GlassAccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "analyze these speeds, ports, and delays using google's neural model for automated configuration fixes.",
                        color = TextLightSlate,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (isGeneratingAi) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = GlassAccentBlue,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CO-PILOT IS WRITING ANALYSIS...",
                                color = GlassAccentBlue,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    } else if (aiAnalysis != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GlassSurfaceVariant, RoundedCornerShape(14.dp))
                                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = aiAnalysis,
                                color = TextUltraLight,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    } else {
                        Button(
                            onClick = onGenerateAiAnalysis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_generate_summary_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GlassSurfaceVariant),
                            border = BorderStroke(1.dp, GlassAccentBlue),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Run AI Report Summary",
                                tint = GlassAccentBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "GENERATE AI PERFORMANCE ANALYSIS",
                                color = GlassAccentBlue,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TelemetryBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    status: String,
    color: Color
) {
    Card(
        modifier = modifier
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GlassSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = title,
                color = TextLightSlate,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = status,
                color = color.copy(alpha = 0.85f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun WifiScannerPanel(
    isScanning: Boolean,
    networks: List<WifiNetworkInfo>,
    onTriggerScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SURROUNDING WIFI NETWORKS",
                color = TextLightSlate,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = GlassAccentBlue,
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = onTriggerScan,
                    modifier = Modifier
                        .testTag("wifi_scan_button")
                        .size(36.dp)
                        .background(GlassSurfaceVariant, RoundedCornerShape(10.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Trigger Wifi Scan",
                        tint = GlassAccentBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (networks.isEmpty() && !isScanning) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = "No Networks",
                        tint = TextLightSlate,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "no networks detected. scan manually above.",
                        color = TextLightSlate,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(networks) { net ->
                    WifiNetworkCard(net)
                }
            }
        }
    }
}

@Composable
fun WifiNetworkCard(net: WifiNetworkInfo) {
    val signalPercent = when {
        net.signalStrength >= -50 -> 100
        net.signalStrength <= -100 -> 0
        else -> ((net.signalStrength + 100) * 2)
    }

    val statusColor = when {
        signalPercent >= 75 -> GlassAccentGreen
        signalPercent >= 50 -> GlassAccentBlue
        signalPercent >= 30 -> GlassAccentAmber
        else -> GlassAccentRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (net.isCurrent) GlassAccentBlue else GlassBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GlassSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when {
                    signalPercent >= 75 -> Icons.Outlined.Wifi
                    signalPercent >= 45 -> Icons.Outlined.NetworkWifi3Bar
                    else -> Icons.Outlined.NetworkWifi1Bar
                },
                contentDescription = "Signal Strength",
                tint = statusColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = net.ssid,
                        color = TextUltraLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (net.isCurrent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(GlassSurfaceVariant, RoundedCornerShape(6.dp))
                                .border(1.dp, GlassAccentBlue.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                color = GlassAccentBlue,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
                Text(
                    text = "BSSID: ${net.bssid} | ${net.frequency} MHz (${getBandString(net.frequency)})",
                    color = TextLightSlate,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${net.signalStrength} dBm",
                    color = statusColor,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = net.security,
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

private fun getBandString(frequency: Int): String {
    return when {
        frequency >= 5925 -> "6G"
        frequency >= 4900 -> "5G"
        else -> "2.4G"
    }
}

@Composable
fun AiCoPilotPanel(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    selectedModel: String,
    enableHighThinking: Boolean,
    onModelSelected: (String) -> Unit,
    onHighThinkingToggled: (Boolean) -> Unit,
    onSendMessage: (String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to the bottom of the list when messages change
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // AI CHAT CONTROLS / SETTINGS ROW
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // MODEL SELECTION ROW
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "MODEL: ",
                        color = TextLightSlate,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Text(
                            text = selectedModel.uppercase(Locale.ROOT),
                            color = GlassAccentGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .testTag("model_selector_dropdown")
                                .clickable { expanded = true }
                                .background(GlassSurfaceVariant, RoundedCornerShape(8.dp))
                                .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(GlassSurface)
                        ) {
                            DropdownMenuItem(
                                text = { Text("GEMINI FLASH (STABLE)", color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                                onClick = { onModelSelected("gemini-flash-latest"); expanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("GEMINI 3.5 FLASH (GENERAL)", color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                                onClick = { onModelSelected("gemini-3.5-flash"); expanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("GEMINI 3.1 FLASH LITE (FAST)", color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                                onClick = { onModelSelected("gemini-3.1-flash-lite-preview"); expanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("GEMINI 3.1 PRO (COMPLEX)", color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                                onClick = { onModelSelected("gemini-3.1-pro-preview"); expanded = false }
                            )
                        }
                    }
                }

                // DEEP THINKING TOGGLE
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "THINKING: ",
                        color = TextLightSlate,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Switch(
                        checked = enableHighThinking,
                        onCheckedChange = onHighThinkingToggled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = GlassAccentGreen,
                            uncheckedThumbColor = TextLightSlate,
                            uncheckedTrackColor = GlassSurfaceVariant
                        ),
                        modifier = Modifier.scale(0.7f)
                    )
                }
            }
        }

        // CHAT CONSOLE SCROLL
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(msg)
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = GlassAccentGreen,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "copilot is analyzing latency tables...",
                            color = GlassAccentGreen,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // TEXT INPUT BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                placeholder = {
                    Text(
                        "ask co-pilot to troubleshoot...",
                        color = TextLightSlate,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextUltraLight,
                    unfocusedTextColor = TextUltraLight,
                    focusedContainerColor = GlassSurfaceVariant,
                    unfocusedContainerColor = GlassSurface,
                    focusedBorderColor = GlassAccentBlue,
                    unfocusedBorderColor = GlassBorder
                ),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        onSendMessage(textInput)
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .testTag("chat_send_button")
                    .size(54.dp)
                    .background(GlassAccentBlue, RoundedCornerShape(14.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Chat Message to Gemini Co-Pilot",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val align = if (msg.isUser) Alignment.End else Alignment.Start
    val bg = if (msg.isUser) GlassSurfaceVariant else GlassSurface
    val borderCol = if (msg.isUser) GlassAccentBlue else GlassAccentPurple

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(bg, RoundedCornerShape(16.dp))
                .border(1.dp, borderCol, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (msg.isUser) "OPERATOR" else "NETSCAN CO-PILOT",
                    color = borderCol,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = msg.text,
                    color = TextUltraLight,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun HistoryPanel(
    history: List<NetworkScanResult>,
    onDelete: (Int) -> Unit,
    onClearAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SAVED SCANS HISTORY",
                color = TextLightSlate,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            if (history.isNotEmpty()) {
                IconButton(
                    onClick = onClearAll,
                    modifier = Modifier
                        .testTag("history_clear_button")
                        .size(36.dp)
                        .background(GlassSurfaceVariant, RoundedCornerShape(10.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear All Saved Scans History",
                        tint = GlassAccentRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "No History",
                        tint = TextLightSlate,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "no telemetry audits logged yet.",
                        color = TextLightSlate,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(history) { record ->
                    HistoryCard(record, onDelete)
                }
            }
        }
    }
}

@Composable
fun HistoryCard(record: NetworkScanResult, onDelete: (Int) -> Unit) {
    val dateStr = remember(record.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault())
        sdf.format(Date(record.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GlassSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = record.ssid,
                        color = TextUltraLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AUDIT AT: $dateStr",
                        color = TextLightSlate,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                IconButton(
                    onClick = { onDelete(record.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete this History record",
                        tint = GlassAccentRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Summary grid in history
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("PINGS", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "GW: ${if (record.pingGateway == -1) "--" else "${record.pingGateway}ms"} | WAN: ${if (record.pingGoogle == -1) "--" else "${record.pingGoogle}ms"}",
                        color = GlassAccentBlue,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("SPEED TEST", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "HTTP Delay: ${if (record.httpLatency == -1) "--" else "${record.httpLatency}ms"}",
                        color = GlassAccentGreen,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("AUDIT", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        if (record.openPorts.isEmpty()) "SAFE / SECURED" else "OPEN PORTS",
                        color = if (record.openPorts.isEmpty()) GlassAccentGreen else GlassAccentAmber,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ====================================================================
// 3. NETWORK PACKET LOGGER PANEL
// ====================================================================
@Composable
fun PacketLoggerPanel(
    isLogging: Boolean,
    logs: List<NetworkPacket>,
    onToggleLogging: () -> Unit,
    onClearLogs: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    val filteredLogs = remember(logs, selectedFilter) {
        if (selectedFilter == "ALL") logs else logs.filter { it.protocol == selectedFilter }
    }
    val listState = rememberLazyListState()

    LaunchedEffect(filteredLogs.size) {
        if (filteredLogs.isNotEmpty()) {
            listState.animateScrollToItem(filteredLogs.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Control Bar Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play / Pause Logging button
                Button(
                    onClick = onToggleLogging,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLogging) GlassAccentRed.copy(alpha = 0.2f) else GlassAccentGreen.copy(alpha = 0.2f),
                        contentColor = if (isLogging) GlassAccentRed else GlassAccentGreen
                    ),
                    modifier = Modifier
                        .testTag("packet_toggle_button")
                        .border(1.dp, if (isLogging) GlassAccentRed.copy(alpha = 0.5f) else GlassAccentGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = if (isLogging) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Toggle Logger",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isLogging) "STOP LOGGER" else "START LOGGER",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Clear button
                IconButton(
                    onClick = onClearLogs,
                    modifier = Modifier
                        .testTag("packet_clear_button")
                        .size(36.dp)
                        .background(GlassSurfaceVariant, RoundedCornerShape(10.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Clear Logs",
                        tint = GlassAccentRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Protocol Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf("ALL", "TCP", "UDP", "ICMP", "DNS", "HTTP")
            filters.forEach { filter ->
                val isSelected = selectedFilter == filter
                val color = when (filter) {
                    "TCP" -> GlassAccentBlue
                    "UDP" -> GlassAccentPurple
                    "ICMP" -> GlassAccentAmber
                    "DNS" -> GlassAccentGreen
                    "HTTP" -> Color(0xFF22D3EE)
                    else -> GlassAccentBlue
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) color.copy(alpha = 0.25f) else GlassSurface)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) color else GlassBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) color else TextLightSlate,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Live Log terminal Card
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                if (filteredLogs.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "Empty logs",
                            tint = TextLightSlate,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isLogging) "listening for packets..." else "packet logger is dormant.",
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredLogs) { p ->
                            val protoColor = when (p.protocol) {
                                "TCP" -> GlassAccentBlue
                                "UDP" -> GlassAccentPurple
                                "ICMP" -> GlassAccentAmber
                                "DNS" -> GlassAccentGreen
                                "HTTP" -> Color(0xFF22D3EE)
                                else -> GlassAccentBlue
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(GlassSurfaceVariant, RoundedCornerShape(8.dp))
                                    .border(1.dp, GlassBorder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Colored Protocol indicator
                                        Box(
                                            modifier = Modifier
                                                .background(protoColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                                .border(1.dp, protoColor, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = p.protocol,
                                                color = protoColor,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "ID: ${p.id}",
                                            color = TextLightSlate,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Text(
                                        text = p.timestamp,
                                        color = TextLightSlate,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "${p.sourceIp} -> ${p.destIp}",
                                        color = TextUltraLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${p.length} B",
                                        color = TextLightSlate,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = p.info,
                                    color = TextLightSlate,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ====================================================================
// 4. SUBNET DEVICE DISCOVERY PANEL
// ====================================================================
@Composable
fun DeviceDiscoveryPanel(
    isDiscovering: Boolean,
    devices: List<DiscoveredDevice>,
    progress: Float,
    onStartDiscovery: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Controls Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SUBNET DISCOVERY",
                            color = TextUltraLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Sweeping local subnet for nodes",
                            color = TextLightSlate,
                            fontSize = 10.sp
                        )
                    }

                    Button(
                        onClick = onStartDiscovery,
                        enabled = !isDiscovering,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GlassAccentBlue,
                            contentColor = Color.White,
                            disabledContainerColor = GlassSurfaceVariant,
                            disabledContentColor = TextLightSlate
                        ),
                        modifier = Modifier
                            .testTag("discovery_start_button")
                            .border(1.dp, if (isDiscovering) GlassBorder else GlassAccentBlue.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isDiscovering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = GlassAccentBlue,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Scan subnet",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isDiscovering) "SCANNING..." else "SCAN SUBNET",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (isDiscovering) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GlassAccentBlue,
                        trackColor = GlassSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Probing subnet map: ${(progress * 100).toInt()}%",
                        color = GlassAccentBlue,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Active Devices list
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                if (devices.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = "No devices",
                            tint = TextLightSlate,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "trigger a scan to map local nodes.",
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "ACTIVE DEVIATION LIST (${devices.size} ONLINE)",
                                color = TextLightSlate,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        items(devices) { dev ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (dev.isPrimary) GlassAccentBlue.copy(alpha = 0.08f) else GlassSurfaceVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (dev.isPrimary) GlassAccentBlue.copy(alpha = 0.4f) else GlassBorder.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Device Icon
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(
                                            if (dev.isPrimary) GlassAccentBlue.copy(alpha = 0.15f) else GlassSurface,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (dev.isPrimary) GlassAccentBlue else GlassBorder,
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when {
                                            dev.isPrimary -> Icons.Default.Smartphone
                                            dev.hostname.contains("Router") -> Icons.Default.Router
                                            dev.hostname.contains("TV") -> Icons.Default.Tv
                                            dev.hostname.contains("Printer") -> Icons.Default.Print
                                            else -> Icons.Default.Computer
                                        },
                                        contentDescription = "Device Type",
                                        tint = if (dev.isPrimary) GlassAccentBlue else TextUltraLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = dev.hostname,
                                        color = TextUltraLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "IP: ${dev.ipAddress} | MAC: ${dev.macAddress}",
                                        color = TextLightSlate,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                // Status Indicator tag
                                Box(
                                    modifier = Modifier
                                        .background(GlassAccentGreen.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                        .border(1.dp, GlassAccentGreen, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        color = GlassAccentGreen,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ====================================================================
// 5. NETWORK BANDWIDTH MONITOR PANEL
// ====================================================================
@Composable
fun BandwidthPanel(
    isTesting: Boolean,
    progress: Float,
    downloadSpeed: Float,
    uploadSpeed: Float,
    peakSpeed: Float,
    averageSpeed: Float,
    dataPoints: List<Float>,
    stage: String,
    onStartTest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Controls & Progress Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BANDWIDTH PROFILER",
                            color = TextUltraLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "State: $stage",
                            color = if (isTesting) GlassAccentGreen else TextLightSlate,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Button(
                        onClick = onStartTest,
                        enabled = !isTesting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GlassAccentGreen,
                            contentColor = Color.Black,
                            disabledContainerColor = GlassSurfaceVariant,
                            disabledContentColor = TextLightSlate
                        ),
                        modifier = Modifier
                            .testTag("bandwidth_start_button")
                            .border(1.dp, if (isTesting) GlassBorder else GlassAccentGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isTesting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.Black,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Speed test",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isTesting) "TESTING..." else "RUN SPEED TEST",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (isTesting) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GlassAccentGreen,
                        trackColor = GlassSurfaceVariant
                    )
                }
            }
        }

        // Live Speed display grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Download Speed
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "DOWNLOAD",
                        color = TextLightSlate,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (downloadSpeed == 0f) "--" else String.format("%.1f", downloadSpeed),
                        color = GlassAccentGreen,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        "Mbps",
                        color = TextLightSlate,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Upload Speed
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "UPLOAD",
                        color = TextLightSlate,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (uploadSpeed == 0f) "--" else String.format("%.1f", uploadSpeed),
                        color = GlassAccentBlue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        "Mbps",
                        color = TextLightSlate,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Secondary Metrics Grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Peak throughput
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PEAK BANDWIDTH", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            text = if (peakSpeed == 0f) "-- Mbps" else String.format("%.1f Mbps", peakSpeed),
                            color = TextUltraLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Average throughput
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("AVERAGE INDEX", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            text = if (averageSpeed == 0f) "-- Mbps" else String.format("%.1f Mbps", averageSpeed),
                            color = TextUltraLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Live Line Chart Card
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                Text(
                    text = "REAL-TIME SPECTRUM WAVEFORM",
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                ) {
                    if (dataPoints.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "spectrum graph idle. run a test to plot speed wave.",
                                color = TextLightSlate,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    } else {
                        // Custom Canvas Line chart rendering
                        Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                            val width = size.width
                            val height = size.height
                            val maxVal = (dataPoints.maxOrNull() ?: 100f).coerceAtLeast(60f) * 1.15f
                            val totalPoints = dataPoints.size

                            // Draw subtle background grid lines
                            val gridLines = 4
                            for (gridIdx in 0..gridLines) {
                                val gridY = height * (gridIdx / gridLines.toFloat())
                                drawLine(
                                    color = Color.White.copy(alpha = 0.05f),
                                    start = Offset(0f, gridY),
                                    end = Offset(width, gridY),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }

                            if (totalPoints > 1) {
                                val path = androidx.compose.ui.graphics.Path()
                                for ((idx, pt) in dataPoints.withIndex()) {
                                    val x = width * (idx / (totalPoints - 1).toFloat())
                                    val y = height - (height * (pt / maxVal))
                                    if (idx == 0) {
                                        path.moveTo(x, y)
                                    } else {
                                        path.lineTo(x, y)
                                    }
                                }

                                // Draw Path Wave line
                                drawPath(
                                    path = path,
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(GlassAccentGreen, GlassAccentBlue)
                                    ),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                                        width = 3.dp.toPx()
                                    )
                                )

                                // Draw vertical tracking line on the latest point
                                val lastIdx = totalPoints - 1
                                val lastX = width * (lastIdx / lastIdx.toFloat())
                                val lastY = height - (height * (dataPoints.last() / maxVal))
                                drawCircle(
                                    color = if (stage == "Download Test") GlassAccentGreen else GlassAccentBlue,
                                    center = Offset(lastX, lastY),
                                    radius = 5.dp.toPx()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ====================================================================
// 6. PORT DISCOVERY TOOL PANEL
// ====================================================================
@Composable
fun PortDiscoveryPanel(
    isScanning: Boolean,
    progress: Float,
    results: List<PortScanResult>,
    gatewayIp: String,
    onStartScan: (String, String, Int, Int) -> Unit
) {
    var hostInput by remember { mutableStateOf("") }
    var selectedProfile by remember { mutableStateOf("Common Network Ports") }
    var startPortInput by remember { mutableStateOf("1") }
    var endPortInput by remember { mutableStateOf("100") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Form & Configurations Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "TCP PORT CONFIGURATOR",
                    color = TextUltraLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Host Input
                OutlinedTextField(
                    value = hostInput,
                    onValueChange = { hostInput = it },
                    placeholder = { Text(text = gatewayIp.ifEmpty { "192.168.1.1" }, color = TextLightSlate, fontSize = 12.sp) },
                    label = { Text("Target Host / IP", color = TextLightSlate, fontSize = 10.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextUltraLight,
                        unfocusedTextColor = TextUltraLight,
                        focusedBorderColor = GlassAccentBlue,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = GlassSurfaceVariant,
                        unfocusedContainerColor = GlassSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Profile Selector Dropdown or Horizontal selectors
                Text(
                    text = "SCAN PROFILE:",
                    color = TextLightSlate,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val profiles = listOf("Common Network Ports", "Database & Dev Ports", "Admin & Mail Ports", "Custom Range")
                    profiles.forEach { profile ->
                        val isSelected = selectedProfile == profile
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GlassAccentBlue.copy(alpha = 0.2f) else GlassSurface)
                                .border(1.dp, if (isSelected) GlassAccentBlue else GlassBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedProfile = profile }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = profile.uppercase(),
                                color = if (isSelected) GlassAccentBlue else TextLightSlate,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                if (selectedProfile == "Custom Range") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = startPortInput,
                            onValueChange = { startPortInput = it.filter { char -> char.isDigit() } },
                            label = { Text("Start Port", color = TextLightSlate, fontSize = 9.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextUltraLight,
                                unfocusedTextColor = TextUltraLight,
                                focusedBorderColor = GlassAccentBlue,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = GlassSurfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = endPortInput,
                            onValueChange = { endPortInput = it.filter { char -> char.isDigit() } },
                            label = { Text("End Port", color = TextLightSlate, fontSize = 9.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextUltraLight,
                                unfocusedTextColor = TextUltraLight,
                                focusedBorderColor = GlassAccentBlue,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = GlassSurfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val host = hostInput.ifBlank { gatewayIp.ifEmpty { "192.168.1.1" } }
                        val start = startPortInput.toIntOrNull() ?: 1
                        val end = endPortInput.toIntOrNull() ?: 100
                        onStartScan(host, selectedProfile, start, end)
                    },
                    enabled = !isScanning,
                    colors = ButtonDefaults.buttonColors(containerColor = GlassAccentBlue, contentColor = Color.White),
                    modifier = Modifier
                        .testTag("port_scan_trigger_button")
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, GlassAccentBlue.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SCANNING PORTS (${(progress * 100).toInt()}%)...", fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(imageVector = Icons.Default.Security, contentDescription = "Scan ports", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("RUN PORT PROBE", fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (isScanning) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = GlassAccentBlue,
                        trackColor = GlassSurfaceVariant
                    )
                }
            }
        }

        // Scan Results List
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                if (results.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Policy,
                            contentDescription = "No scan",
                            tint = TextLightSlate,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "specify host and sweep network ports.",
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item {
                            Text(
                                text = "OPEN PORTS AUDIT REPORT",
                                color = TextLightSlate,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        items(results) { res ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (res.isOpen) GlassAccentGreen.copy(alpha = 0.08f) else GlassSurfaceVariant,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (res.isOpen) GlassAccentGreen.copy(alpha = 0.4f) else GlassBorder.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (res.isOpen) Icons.Default.LockOpen else Icons.Default.Lock,
                                        contentDescription = "Port State",
                                        tint = if (res.isOpen) GlassAccentGreen else TextLightSlate,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Port ${res.port} (${res.service})",
                                        color = TextUltraLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                // Open / Closed bubble tag
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (res.isOpen) GlassAccentGreen.copy(alpha = 0.15f) else GlassAccentRed.copy(alpha = 0.15f),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (res.isOpen) GlassAccentGreen else GlassBorder,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (res.isOpen) "OPEN" else "CLOSED",
                                        color = if (res.isOpen) GlassAccentGreen else TextLightSlate,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ====================================================================
// 7. HOST REACHABILITY TESTER PANEL
// ====================================================================
@Composable
fun ReachabilityPanel(
    isTesting: Boolean,
    progress: Float,
    logs: List<PingPacketResult>,
    minRtt: Int,
    maxRtt: Int,
    avgRtt: Int,
    sent: Int,
    received: Int,
    onStartTest: (String, Int) -> Unit
) {
    var hostInput by remember { mutableStateOf("google.com") }
    var countSelection by remember { mutableStateOf("4") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Controls Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "WAN REACHABILITY PROBER (PING)",
                    color = TextUltraLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Host input
                    OutlinedTextField(
                        value = hostInput,
                        onValueChange = { hostInput = it },
                        placeholder = { Text("google.com") },
                        label = { Text("Probe Address", color = TextLightSlate, fontSize = 9.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextUltraLight,
                            unfocusedTextColor = TextUltraLight,
                            focusedBorderColor = GlassAccentAmber,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassSurfaceVariant
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1.5f)
                    )

                    // Count input
                    OutlinedTextField(
                        value = countSelection,
                        onValueChange = { countSelection = it.filter { char -> char.isDigit() } },
                        label = { Text("Count", color = TextLightSlate, fontSize = 9.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextUltraLight,
                            unfocusedTextColor = TextUltraLight,
                            focusedBorderColor = GlassAccentAmber,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = GlassSurfaceVariant
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val host = hostInput.ifBlank { "google.com" }
                        val count = countSelection.toIntOrNull() ?: 4
                        onStartTest(host, count)
                    },
                    enabled = !isTesting,
                    colors = ButtonDefaults.buttonColors(containerColor = GlassAccentAmber, contentColor = Color.Black),
                    modifier = Modifier
                        .testTag("ping_test_button")
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, GlassAccentAmber.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isTesting) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("PINGING HOSTS (${(progress * 100).toInt()}%)...", fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(imageVector = Icons.Default.SettingsEthernet, contentDescription = "Ping test", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("TEST REACHABILITY (ICMP)", fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Stats card row
        if (logs.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Round trip pings
                Card(
                    modifier = Modifier
                        .weight(1.3f)
                        .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassSurface)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("RTT BOUNDS", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "MIN: ${if (minRtt == -1) "--" else "${minRtt}ms"} | MAX: ${if (maxRtt == -1) "--" else "${maxRtt}ms"}",
                            color = TextUltraLight,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "AVG: ${if (avgRtt == -1) "--" else "${avgRtt}ms"}",
                            color = GlassAccentAmber,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Packet Loss rates
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassSurface)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("TRANSMISSION", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "TX: $sent | RX: $received",
                            color = TextUltraLight,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        val lossPercent = if (sent > 0) ((sent - received) * 100 / sent) else 0
                        Text(
                            text = "LOSS: $lossPercent%",
                            color = if (lossPercent > 0) GlassAccentRed else GlassAccentGreen,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Live Logs & Bar Chart Terminal Card
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(
                    text = "REACHABILITY RAW STREAM",
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (logs.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "specify host and run probe to stream raw output.",
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    // Split screen: top logs, bottom RTT latency bars
                    Column(modifier = Modifier.weight(1f)) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1.3f)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(logs) { log ->
                                Text(
                                    text = "> ${log.info}",
                                    color = if (log.isSuccess) GlassAccentGreen else GlassAccentRed,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Lateral bar chart of pings
                        Text(
                            text = "RTT HISTOGRAM INDEX (LATENCY COMPARATOR)",
                            color = TextLightSlate,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Row(
                            modifier = Modifier
                                .weight(0.7f)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .border(1.dp, GlassBorder.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val maxBarHeight = (logs.map { it.rttMs }.maxOrNull() ?: 100).coerceAtLeast(30)
                            for (log in logs) {
                                val barWeight = if (log.isSuccess) (log.rttMs.toFloat() / maxBarHeight).coerceIn(0.1f, 1f) else 0.05f
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Bottom,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (log.isSuccess) {
                                        Text(
                                            text = "${log.rttMs}ms",
                                            color = GlassAccentAmber,
                                            fontSize = 8.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    } else {
                                        Text(
                                            text = "TIMEOUT",
                                            color = GlassAccentRed,
                                            fontSize = 7.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(barWeight)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(if (log.isSuccess) GlassAccentAmber else GlassAccentRed.copy(alpha = 0.5f))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 8. WIFI AUTHENTICATION TESTER ---
// ==========================================
@Composable
fun WifiAuthPanel(
    isTesting: Boolean,
    progress: Float,
    result: String?,
    details: List<String>,
    onStartTest: (String) -> Unit
) {
    var selectedProtocol by remember { mutableStateOf("WPA2-PSK") }
    val protocols = listOf("WPA2-PSK", "WPA3-SAE", "WPA-Enterprise", "Open")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "WIFI AUTHENTICATION CRYPTO-AUDIT",
                    color = GlassAccentBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Select wireless security protocol template to simulate authentication handshake and analyze PMKID/EAP vulnerabilities.",
                    color = TextLightSlate,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Protocol Selectors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    protocols.forEach { proto ->
                        val isSelected = proto == selectedProtocol
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedProtocol = proto }
                                .border(
                                    1.dp,
                                    if (isSelected) GlassAccentBlue else GlassBorder,
                                    RoundedCornerShape(8.dp)
                                ),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) GlassAccentBlue.copy(alpha = 0.15f) else Color.Transparent
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = proto,
                                    color = if (isSelected) TextUltraLight else TextLightSlate,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onStartTest(selectedProtocol) },
                    enabled = !isTesting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.dp, if (isTesting) GlassBorder else GlassAccentBlue, RoundedCornerShape(12.dp))
                        .testTag("start_wifi_auth_test_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTesting) Color.Transparent else GlassAccentBlue.copy(alpha = 0.2f),
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = if (isTesting) "CAPTURING HANDSHAKE..." else "INITIATE HANDSHAKE AUDIT",
                        color = if (isTesting) TextLightSlate else TextUltraLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (isTesting) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp)),
                        color = GlassAccentBlue,
                        trackColor = GlassBorder.copy(alpha = 0.2f),
                    )
                }
            }
        }

        // Live handshake details Terminal
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "REALTIME DECRYPTOR LOGSTREAM",
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (details.isEmpty() && result == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Awaiting handshake acquisition trigger.",
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(details) { line ->
                            Text(
                                text = ">> $line",
                                color = TextUltraLight,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        if (result != null) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            1.dp,
                                            if (result.startsWith("SUCCESS") || result.startsWith("CONNECTED")) GlassAccentGreen else GlassAccentRed,
                                            RoundedCornerShape(8.dp)
                                        ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (result.startsWith("SUCCESS") || result.startsWith("CONNECTED")) GlassAccentGreen.copy(alpha = 0.1f) else GlassAccentRed.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Text(
                                        text = result,
                                        color = if (result.startsWith("SUCCESS") || result.startsWith("CONNECTED")) GlassAccentGreen else GlassAccentRed,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 9. NETWORK INTERFACES PANEL ---
// ==========================================
@Composable
fun NetworkInterfacesPanel(
    isScanning: Boolean,
    interfaces: List<NetworkInterfaceInfo>,
    onTriggerScan: () -> Unit
) {
    LaunchedEffect(Unit) {
        onTriggerScan()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "HARDWARE INTERFACES CONFIG",
                        color = GlassAccentAmber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Querying live net interfaces, MAC assignments, loopback bridges, and dynamic bandwidth logs.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = onTriggerScan,
                    enabled = !isScanning,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (isScanning) Color.Transparent else GlassAccentAmber.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, GlassAccentAmber, RoundedCornerShape(10.dp))
                        .testTag("scan_interfaces_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Scan Interfaces",
                        tint = GlassAccentAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (interfaces.isEmpty() && isScanning) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GlassAccentAmber)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(interfaces) { info ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (info.isUp) GlassAccentGreen else GlassAccentRed)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = info.name,
                                        color = TextUltraLight,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (info.isUp) GlassAccentGreen.copy(alpha = 0.15f) else GlassAccentRed.copy(alpha = 0.15f)
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (info.isUp) "UP / ACTIVE" else "DOWN / DORMANT",
                                        color = if (info.isUp) GlassAccentGreen else GlassAccentRed,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Display: ${info.displayName}",
                                color = TextLightSlate,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column(modifier = Modifier.weight(1.3f)) {
                                    Text("IP-v4 ADDR", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Text(info.ipv4Address, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(1.7f)) {
                                    Text("IP-v6 ADDR", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Text(info.ipv6Address, color = TextUltraLight, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("HARDWARE MAC ADDR", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Text(info.macAddress, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                }

                                if (info.isLoopback) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = GlassBorder.copy(alpha = 0.2f)),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            "LOOPBACK BRIDGE",
                                            color = TextLightSlate,
                                            fontSize = 7.sp,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = GlassBorder.copy(alpha = 0.3f), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("DATA RX (BYTES)", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Text("${info.bytesRx}", color = GlassAccentGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("DATA TX (BYTES)", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Text("${info.bytesTx}", color = GlassAccentBlue, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 10. DNS QUERY LOGGER PANEL ---
// ==========================================
@Composable
fun DnsLoggerPanel(
    isLogging: Boolean,
    logs: List<DnsQueryLog>,
    onStartLogging: () -> Unit,
    onStopLogging: () -> Unit,
    onPerformLookup: (String) -> Unit,
    onClearLogs: () -> Unit
) {
    var lookupDomain by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "PROMISCUOUS DNS SNIFFER & RESOLVER",
                    color = GlassAccentGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Perform static namespace queries or run the background promiscuous logs daemon to audit ongoing network DNS traffic hooks.",
                    color = TextLightSlate,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextField(
                        value = lookupDomain,
                        onValueChange = { lookupDomain = it },
                        placeholder = { Text("enter domain (e.g. google.com)", color = TextLightSlate, fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                            .testTag("dns_lookup_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                            focusedTextColor = TextUltraLight,
                            unfocusedTextColor = TextUltraLight,
                            cursorColor = GlassAccentGreen,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    )

                    Button(
                        onClick = {
                            if (lookupDomain.isNotBlank()) {
                                onPerformLookup(lookupDomain)
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .border(1.dp, GlassAccentGreen, RoundedCornerShape(10.dp))
                            .testTag("dns_lookup_submit"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GlassAccentGreen.copy(alpha = 0.15f))
                    ) {
                        Text("LOOKUP", color = GlassAccentGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { if (isLogging) onStopLogging() else onStartLogging() },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .border(1.dp, if (isLogging) GlassAccentRed else GlassAccentGreen, RoundedCornerShape(10.dp))
                            .testTag("dns_sniff_toggle_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLogging) GlassAccentRed.copy(alpha = 0.15f) else GlassAccentGreen.copy(alpha = 0.15f)
                        )
                    ) {
                        Text(
                            text = if (isLogging) "HALT LIVE SNIFFER" else "LAUNCH LIVE DAEMON",
                            color = if (isLogging) GlassAccentRed else GlassAccentGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Button(
                        onClick = onClearLogs,
                        modifier = Modifier
                            .weight(0.6f)
                            .height(44.dp)
                            .border(1.dp, GlassBorder, RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("CLEAR", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // Logs terminal view
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CAPTURESTREAM RAW LOG OUTPUT",
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (logs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Promiscuous sniffer standby. Logs are printed here.",
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        reverseLayout = true
                    ) {
                        items(logs.asReversed()) { log ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                    .border(1.dp, GlassBorder.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "[ID:${log.id}] ${log.timestamp}",
                                        color = TextLightSlate,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )

                                    Text(
                                        text = "${log.latencyMs}ms",
                                        color = GlassAccentAmber,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "QUERY: ${log.domainName}",
                                        color = TextUltraLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = log.queryType,
                                        color = GlassAccentBlue,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Text(
                                    text = "RESP: ${log.response}",
                                    color = if (log.status == "SUCCESS") GlassAccentGreen else GlassAccentRed,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 11. TLS VERSION CHECKER PANEL ---
// ==========================================
@Composable
fun TlsCheckerPanel(
    isChecking: Boolean,
    result: TlsAuditResult?,
    onCheckTls: (String) -> Unit
) {
    var hostName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TLS HANDSHAKE & CIPHER AUDITOR",
                    color = GlassAccentBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Audit cryptographic handshakes on port 443 of remote targets. Evaluate active protocol version, cipher suite, and SSL certificate chains.",
                    color = TextLightSlate,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextField(
                        value = hostName,
                        onValueChange = { hostName = it },
                        placeholder = { Text("remote host (e.g. google.com)", color = TextLightSlate, fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                            .testTag("tls_host_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                            focusedTextColor = TextUltraLight,
                            unfocusedTextColor = TextUltraLight,
                            cursorColor = GlassAccentBlue,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    )

                    Button(
                        onClick = { onCheckTls(hostName) },
                        enabled = !isChecking,
                        modifier = Modifier
                            .height(50.dp)
                            .border(1.dp, if (isChecking) GlassBorder else GlassAccentBlue, RoundedCornerShape(10.dp))
                            .testTag("tls_test_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isChecking) Color.Transparent else GlassAccentBlue.copy(alpha = 0.15f),
                            disabledContainerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = if (isChecking) "TUNNELING..." else "AUDIT",
                            color = if (isChecking) TextLightSlate else GlassAccentBlue,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Security report card
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SECURITY AUDIT DETAILED COMPLIANCE REPORT",
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (isChecking) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = GlassAccentBlue)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Negotiating TLS parameters on socket...",
                                color = TextLightSlate,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                } else if (result == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Awaiting connection parameters.",
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            // Overall safety status
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        if (result.certStatus.startsWith("VALID")) GlassAccentGreen else GlassAccentAmber,
                                        RoundedCornerShape(10.dp)
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (result.certStatus.startsWith("VALID")) GlassAccentGreen.copy(alpha = 0.1f) else GlassAccentAmber.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("HANDSHAKE AUDIT STATUS", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                        Text(result.host, color = TextUltraLight, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (result.certStatus.startsWith("VALID")) GlassAccentGreen else GlassAccentAmber
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = result.certStatus,
                                            color = Color.Black,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.15f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("NEGOTIATED CRYPTO CIPHERS", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("PROTOCOL VERSION", color = TextLightSlate, fontSize = 11.sp)
                                        Text(result.tlsVersion, color = GlassAccentBlue, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("CIPHER SUITE SPEC", color = TextLightSlate, fontSize = 9.sp)
                                    Text(result.cipherSuite, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.15f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("SSL CERTIFICATE CHAIN IDENTITY", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("ISSUER CA", color = TextLightSlate, fontSize = 9.sp)
                                    Text(result.certIssuer, color = TextUltraLight, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("SUBJECT IDENTITY", color = TextLightSlate, fontSize = 9.sp)
                                    Text(result.certSubject, color = TextUltraLight, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("VALID FROM", color = TextLightSlate, fontSize = 8.sp)
                                            Text(result.certValidFrom, color = TextUltraLight, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        }
                                        Column {
                                            Text("VALID UNTIL", color = TextLightSlate, fontSize = 8.sp)
                                            Text(result.certValidTo, color = TextUltraLight, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 12. HTTP/HTTPS DEBUGGING PANEL ---
// ==========================================
@Composable
fun HttpDebuggerPanel(
    isSending: Boolean,
    response: HttpDebugResponse?,
    onSendRequest: (String, String, String, String) -> Unit
) {
    var method by remember { mutableStateOf("GET") }
    var url by remember { mutableStateOf("https://httpbin.org/get") }
    var headers by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    
    val methods = listOf("GET", "POST", "PUT", "DELETE", "PATCH")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "HTTP/HTTPS METHOD INSPECTOR",
                    color = GlassAccentAmber,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    methods.forEach { m ->
                        val isSelected = m == method
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { method = m }
                                .border(
                                    1.dp,
                                    if (isSelected) GlassAccentAmber else GlassBorder,
                                    RoundedCornerShape(6.dp)
                                ),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) GlassAccentAmber.copy(alpha = 0.15f) else Color.Transparent
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(m, color = if (isSelected) TextUltraLight else TextLightSlate, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // URL Field
                TextField(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = { Text("Endpoint URL") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                        .testTag("http_debugger_url_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                        focusedTextColor = TextUltraLight,
                        cursorColor = GlassAccentAmber,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Headers Input
                TextField(
                    value = headers,
                    onValueChange = { headers = it },
                    placeholder = { Text("Request Headers (Key: Value\\n)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(1.dp, GlassBorder, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                        focusedTextColor = TextUltraLight,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                )

                if (method != "GET" && method != "HEAD") {
                    Spacer(modifier = Modifier.height(10.dp))
                    // Body Input
                    TextField(
                        value = body,
                        onValueChange = { body = it },
                        placeholder = { Text("JSON/Raw Payload Body Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(1.dp, GlassBorder, RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                            focusedTextColor = TextUltraLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onSendRequest(method, url, headers, body) },
                    enabled = !isSending,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, if (isSending) GlassBorder else GlassAccentAmber, RoundedCornerShape(10.dp))
                        .testTag("http_debugger_send_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSending) Color.Transparent else GlassAccentAmber.copy(alpha = 0.15f),
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = if (isSending) "DISPATCHING PACKETS..." else "DISPATCH HTTP PACKET",
                        color = if (isSending) TextLightSlate else GlassAccentAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Response terminal card
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "RECEIVED HTTP RESPONSE RAW PACKET BODY",
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (isSending) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = GlassAccentAmber)
                    }
                } else if (response == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ready to query remote HTTP daemon.", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            // Headers & Metadata Row
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        if (response.statusCode in 200..299) GlassAccentGreen else GlassAccentRed,
                                        RoundedCornerShape(10.dp)
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (response.statusCode in 200..299) GlassAccentGreen.copy(alpha = 0.1f) else GlassAccentRed.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("STATUS CODE", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                        Text("${response.statusCode} ${response.statusMessage}", color = TextUltraLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("LATENCY", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                        Text("${response.latencyMs} ms", color = GlassAccentAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }

                        item {
                            // Returned Headers
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.15f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("HTTP HEADERS", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(bottom = 6.dp))
                                    response.headers.forEach { (k, v) ->
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text("$k: ", color = GlassAccentBlue, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                            Text(v, color = TextUltraLight, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            // Body Container
                            Column {
                                Text("BODY CONTENT", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(bottom = 4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                        .border(1.dp, GlassBorder.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = response.body.ifBlank { "[No Response Body]" },
                                        color = TextUltraLight,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 13. BLUETOOTH DEVICE SCANNER ---
// ==========================================
@Composable
fun BluetoothScannerPanel(
    isScanning: Boolean,
    progress: Float,
    devices: List<BluetoothDeviceDetails>,
    onTriggerScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "BLUETOOTH LE & CLASSIC AUDIT",
                        color = GlassAccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Capture nearby Bluetooth LE advertising packets, resolve signal RSSI dbm, and scan hardware capabilities.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = onTriggerScan,
                    enabled = !isScanning,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (isScanning) Color.Transparent else GlassAccentBlue.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, GlassAccentBlue, RoundedCornerShape(10.dp))
                        .testTag("scan_bluetooth_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Scan Bluetooth",
                        tint = GlassAccentBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (isScanning) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("BEACON SCAN SEQUENCE RUNNING", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(bottom = 6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp)),
                        color = GlassAccentBlue,
                        trackColor = GlassBorder.copy(alpha = 0.15f)
                    )
                }
            }
        }

        if (devices.isEmpty() && !isScanning) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Standby. Launch scan to find active beacons.", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(devices) { device ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = device.name.ifBlank { "Unresolved Name" },
                                    color = TextUltraLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (device.rssi > -65) GlassAccentGreen.copy(alpha = 0.15f) else GlassAccentAmber.copy(alpha = 0.15f)
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${device.rssi} dBm",
                                        color = if (device.rssi > -65) GlassAccentGreen else GlassAccentAmber,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "MAC: ${device.macAddress}",
                                color = TextLightSlate,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("PROXIMITY INDEX", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                    Text(device.proximity, color = TextUltraLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = GlassBorder.copy(alpha = 0.2f)),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = device.deviceType,
                                            color = TextLightSlate,
                                            fontSize = 8.sp,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }

                                    if (device.isLeSecure) {
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = GlassAccentGreen.copy(alpha = 0.15f)),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "LE SECURE",
                                                color = GlassAccentGreen,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 14. NFC TAG READER / WRITER ---
// ==========================================
@Composable
fun NfcPanel(
    isActive: Boolean,
    scanResult: NfcTagDetails?,
    writeStatus: String?,
    onToggleActive: (Boolean) -> Unit,
    onWriteTag: (String) -> Unit
) {
    var writePayload by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NFC TRANCEIVER COUPLER",
                            color = GlassAccentAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Toggle the field generator to capture magnetic induction waves or rewrite tag sectors.",
                            color = TextLightSlate,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Switch(
                        checked = isActive,
                        onCheckedChange = onToggleActive,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GlassAccentAmber,
                            checkedTrackColor = GlassAccentAmber.copy(alpha = 0.3f),
                            uncheckedThumbColor = TextLightSlate,
                            uncheckedTrackColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("nfc_coupler_switch")
                    )
                }
            }
        }

        if (!isActive) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "NFC Inactive",
                        tint = TextLightSlate,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Antenna field decoupled. Toggle switch above.",
                        color = TextLightSlate,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    // RFID Tag stats card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("TARGET INDUCTOR COUPLING DETECTED", color = GlassAccentGreen, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            scanResult?.let { tag ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("UID SECTOR REGISTER", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                        Text(tag.uid, color = TextUltraLight, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("BLOCK STORAGE SIZE", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                        Text("${tag.sizeBytes} BYTES", color = GlassAccentBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("TAG CLASSIFICATION", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                Text(tag.tagType, color = TextUltraLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("SUPPORTED INTERFACE TECHLIST", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                Text(tag.techList.joinToString(" | "), color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace)

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("CURRENT PAYLOAD READOUT", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                        .border(1.dp, GlassBorder.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(tag.ndefMessage, color = TextUltraLight, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }

                item {
                    // Writer Sector card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("WRITE NDEF RECORD PAYLOAD", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))

                            TextField(
                                value = writePayload,
                                onValueChange = { writePayload = it },
                                placeholder = { Text("enter custom tag payload text", color = TextLightSlate, fontSize = 11.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                                    .testTag("nfc_write_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                                    unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                                    focusedTextColor = TextUltraLight,
                                    cursorColor = GlassAccentAmber,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true,
                                textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { onWriteTag(writePayload) },
                                enabled = writePayload.isNotBlank() && writeStatus == null || writeStatus?.startsWith("SUCCESS") == false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .border(1.dp, GlassAccentAmber, RoundedCornerShape(10.dp))
                                    .testTag("nfc_write_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GlassAccentAmber.copy(alpha = 0.15f))
                            ) {
                                Text("COMMIT SECTOR DATA", color = GlassAccentAmber, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }

                            if (writeStatus != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            1.dp,
                                            if (writeStatus.startsWith("SUCCESS")) GlassAccentGreen else GlassAccentAmber,
                                            RoundedCornerShape(6.dp)
                                        ),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (writeStatus.startsWith("SUCCESS")) GlassAccentGreen.copy(alpha = 0.1f) else GlassAccentAmber.copy(alpha = 0.1f)
                                    )
                                ) {
                                    Text(
                                        text = writeStatus,
                                        color = if (writeStatus.startsWith("SUCCESS")) GlassAccentGreen else GlassAccentAmber,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 15. CAMERA ACCESS MONITOR PANEL ---
// ==========================================
@Composable
fun CameraMonitorPanel(
    isMonitoring: Boolean,
    logs: List<CameraAccessLog>,
    onStartMonitoring: () -> Unit,
    onStopMonitoring: () -> Unit
) {
    LaunchedEffect(Unit) {
        onStartMonitoring()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "HARDWARE CAMERA ACCESS SENTINEL",
                        color = GlassAccentRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Continuous diagnostic listener logging camera activations to detect unauthorized hooks or spyware.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Switch(
                    checked = isMonitoring,
                    onCheckedChange = { if (it) onStartMonitoring() else onStopMonitoring() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = GlassAccentRed,
                        checkedTrackColor = GlassAccentRed.copy(alpha = 0.3f),
                        uncheckedThumbColor = TextLightSlate,
                        uncheckedTrackColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("camera_monitoring_switch")
                )
            }
        }

        // Access logs Terminal
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SENTINEL ACCESS LOG STREAM",
                        color = TextLightSlate,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    if (isMonitoring) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = GlassAccentRed.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(GlassAccentRed)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("LIVE PROBING", color = GlassAccentRed, fontSize = 7.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (!isMonitoring && logs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sentinel dormant. Activate monitor switch.", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        reverseLayout = true
                    ) {
                        items(logs.asReversed()) { log ->
                            val isWarning = log.status == "WARNING"
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isWarning) GlassAccentRed.copy(alpha = 0.1f) else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isWarning) GlassAccentRed.copy(alpha = 0.3f) else GlassBorder.copy(alpha = 0.1f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "[ACCESS:${log.id}] ${log.timestamp}",
                                        color = TextLightSlate,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isWarning) GlassAccentRed else GlassAccentGreen
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = log.status,
                                            color = Color.Black,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "CAMERA: ${log.cameraId}",
                                        color = TextUltraLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "${log.fps} FPS",
                                        color = GlassAccentAmber,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Text(
                                    text = "RESOLUTION: ${log.resolution}",
                                    color = TextLightSlate,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("PROCESS OWNER: ", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                    Text(
                                        text = log.ownerProcess,
                                        color = if (isWarning) GlassAccentRed else GlassAccentGreen,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 16. MESSAGE FLOW SIMULATOR ---
// ==========================================
@Composable
fun MessageFlowPanel(
    isSimulating: Boolean,
    messages: List<MessageFlowEvent>,
    onStartFlow: (String, String) -> Unit,
    onStopFlow: () -> Unit,
    onPublishMessage: (String, String, String) -> Unit,
    onClearMessages: () -> Unit
) {
    var protocol by remember { mutableStateOf("MQTT") }
    var topic by remember { mutableStateOf("iot/telemetry") }
    var payload by remember { mutableStateOf("{\"val\":24.5}") }

    val protocols = listOf("MQTT", "WebSocket", "CoAP")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DIAGNOSTIC TRANSPORT PACKET FLOW SIMULATOR",
                    color = GlassAccentBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Protocol selector
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    protocols.forEach { proto ->
                        val isSelected = proto == protocol
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { protocol = proto }
                                .border(
                                    1.dp,
                                    if (isSelected) GlassAccentBlue else GlassBorder,
                                    RoundedCornerShape(8.dp)
                                ),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) GlassAccentBlue.copy(alpha = 0.15f) else Color.Transparent
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(proto, color = if (isSelected) TextUltraLight else TextLightSlate, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Topic Field
                TextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = { Text("transport topic/route (e.g. iot/sensors)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                        .testTag("message_flow_topic_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                        focusedTextColor = TextUltraLight,
                        cursorColor = GlassAccentBlue,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { if (isSimulating) onStopFlow() else onStartFlow(protocol, topic) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp)
                            .border(1.dp, if (isSimulating) GlassAccentRed else GlassAccentBlue, RoundedCornerShape(10.dp))
                            .testTag("message_flow_toggle_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSimulating) GlassAccentRed.copy(alpha = 0.15f) else GlassAccentBlue.copy(alpha = 0.15f)
                        )
                    ) {
                        Text(
                            text = if (isSimulating) "HALT COUPLER" else "ESTABLISH Transport Connection",
                            color = if (isSimulating) GlassAccentRed else GlassAccentBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Button(
                        onClick = onClearMessages,
                        modifier = Modifier
                            .weight(0.7f)
                            .height(44.dp)
                            .border(1.dp, GlassBorder, RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("CLEAR", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        if (isSimulating) {
            // Live Publisher Block
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("ACTIVE UPLINK RAW PUBLISHER (INJECT PAYLOAD)", color = GlassAccentBlue, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = payload,
                            onValueChange = { payload = it },
                            placeholder = { Text("payload (JSON/CSV)") },
                            modifier = Modifier
                                .weight(1.3f)
                                .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                                .testTag("message_flow_payload_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Black.copy(alpha = 0.15f),
                                unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                                focusedTextColor = TextUltraLight,
                                cursorColor = GlassAccentBlue,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                        )

                        Button(
                            onClick = { onPublishMessage(protocol, topic, payload) },
                            modifier = Modifier
                                .weight(0.7f)
                                .height(48.dp)
                                .border(1.dp, GlassAccentBlue, RoundedCornerShape(8.dp))
                                .testTag("message_flow_publish_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GlassAccentBlue.copy(alpha = 0.2f))
                        ) {
                            Text("PUBLISH", color = GlassAccentBlue, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Logs terminal card
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TRANSPORT DECODER RAW LOG OUTPUT",
                    color = TextLightSlate,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (messages.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Transport inactive. Initiate connection.", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        reverseLayout = true
                    ) {
                        items(messages.asReversed()) { msg ->
                            val isIncoming = msg.direction == "IN"
                            val isUserPublish = msg.direction.contains("User")
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .border(1.dp, GlassBorder.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${msg.timestamp} | Lat: ${msg.latencyMs}ms",
                                        color = TextLightSlate,
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace
                                    )

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isIncoming) GlassAccentAmber else if (isUserPublish) GlassAccentBlue else GlassAccentGreen
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = msg.direction,
                                            color = Color.Black,
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "TOPIC: ${msg.topic}",
                                        color = TextUltraLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = "QoS ${msg.qos}",
                                        color = GlassAccentBlue,
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "PAYLOAD: ${msg.payload}",
                                    color = if (isIncoming) GlassAccentAmber else TextUltraLight,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// --- 17. CALL LOG STRUCTURE VIEWER ---
// ==========================================
@Composable
fun CallLogsPanel(
    isScanning: Boolean,
    logs: List<CallLogRecord>,
    onTriggerAudit: () -> Unit
) {
    LaunchedEffect(Unit) {
        onTriggerAudit()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TELEPHONY PRIVACY FOOTPRINT AUDITOR",
                        color = GlassAccentAmber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Secure local log analysis auditing call registries and assessing external caller identity privacy risk profiles.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = onTriggerAudit,
                    enabled = !isScanning,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (isScanning) Color.Transparent else GlassAccentAmber.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, GlassAccentAmber, RoundedCornerShape(10.dp))
                        .testTag("scan_call_logs_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Audit Call Logs",
                        tint = GlassAccentAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (isScanning) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = GlassAccentAmber)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Performing footprint audit...", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(logs) { record ->
                    val isHighRisk = record.privacyRisk.contains("HIGH")
                    val isMedRisk = record.privacyRisk.contains("MEDIUM")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (isHighRisk) GlassAccentRed else if (isMedRisk) GlassAccentAmber else GlassBorder,
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when (record.callType) {
                                                    "INCOMING" -> GlassAccentGreen
                                                    "OUTGOING" -> GlassAccentBlue
                                                    else -> GlassAccentRed
                                                }
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = record.callType,
                                        color = TextUltraLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isHighRisk) GlassAccentRed else if (isMedRisk) GlassAccentAmber else GlassAccentGreen
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = record.privacyRisk,
                                        color = Color.Black,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "ANONYMIZED ID: ${record.encryptedPhone}",
                                color = TextUltraLight,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("TIMESTAMP OF EVENT", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                    Text(record.timestamp, color = TextLightSlate, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("CALL DURATION", color = TextLightSlate, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                                    Text("${record.durationSec} seconds", color = GlassAccentAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FileSystemPanel(
    currentPath: String,
    files: List<FileItem>,
    isLoading: Boolean,
    onBrowse: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        if (files.isEmpty()) {
            onBrowse(currentPath)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "FILE SYSTEM BROWSER",
                        color = GlassAccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Browse the internal configurations, network interface state stores, and active local nodes.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Text(
            text = "PATH: $currentPath",
            color = GlassAccentBlue,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(GlassSurface, RoundedCornerShape(8.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GlassAccentBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(files) { file ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                if (file.isDirectory) {
                                    onBrowse(file.path)
                                }
                            }
                            .testTag("file_item_${file.name.replace(".", "_")}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.Description,
                                    contentDescription = null,
                                    tint = if (file.isDirectory) GlassAccentAmber else GlassAccentBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = file.name,
                                        color = TextUltraLight,
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${file.permissions} | Modified: ${file.lastModified}",
                                        color = TextLightSlate,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            if (!file.isDirectory) {
                                Text(
                                    text = "${file.sizeBytes} B",
                                    color = GlassAccentGreen,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackupToolPanel(
    isBackingUp: Boolean,
    progress: Float,
    backups: List<BackupRecord>,
    onTriggerBackup: (String, String) -> Unit
) {
    var backupName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("FULL") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "APPLICATION BACKUP MANAGER",
                        color = GlassAccentGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Compile encrypted localized snapshots of system registry configs, logs, and database partitions.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "INITIATE SECURED BACKUP SEQUENCE",
                    color = TextUltraLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                OutlinedTextField(
                    value = backupName,
                    onValueChange = { backupName = it },
                    label = { Text("Backup Node Label", color = TextLightSlate, fontSize = 11.sp) },
                    textStyle = TextStyle(color = TextUltraLight, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    modifier = Modifier.fillMaxWidth().testTag("backup_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassAccentGreen,
                        unfocusedBorderColor = GlassBorder
                    ),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("FULL", "DIFFERENTIAL", "CONFIG").forEach { type ->
                        val isSel = selectedType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) GlassAccentGreen.copy(alpha = 0.2f) else GlassSurface)
                                .border(1.dp, if (isSel) GlassAccentGreen else GlassBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedType = type }
                                .padding(vertical = 10.dp)
                                .testTag("backup_type_btn_$type"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(type, color = if (isSel) GlassAccentGreen else TextLightSlate, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Button(
                    onClick = { onTriggerBackup(backupName, selectedType) },
                    enabled = !isBackingUp,
                    modifier = Modifier.fillMaxWidth().testTag("trigger_backup_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GlassAccentGreen.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, GlassAccentGreen)
                ) {
                    Text("COMPILE ARCHIVE", color = GlassAccentGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                if (isBackingUp) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = GlassAccentGreen,
                            trackColor = GlassBorder
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("BACKUP IN PROGRESS...", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            Text("${(progress * 100).toInt()}%", color = GlassAccentGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        Text(
            text = "COMPILED BACKUPS HISTORY",
            color = TextLightSlate,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )

        if (backups.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No archives compiled yet.", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(backups) { record ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(record.name, color = TextUltraLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Text(record.id, color = GlassAccentGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Type: ${record.type} | Size: ${record.sizeMb} MB", color = TextLightSlate, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Text("Crypto: ${record.encryption}", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(record.timestamp, color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = GlassAccentGreen),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(record.status, color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceSecurityPanel(
    isScanning: Boolean,
    progress: Float,
    results: List<ServiceAuditRecord>,
    onTriggerScan: (String) -> Unit
) {
    var targetHost by remember { mutableStateOf("127.0.0.1") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SERVICE SECURITY AUDITOR",
                        color = GlassAccentRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Audit daemon configurations and detect potential exposures on running ports and transport services.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = targetHost,
                onValueChange = { targetHost = it },
                label = { Text("Target Host", color = TextLightSlate, fontSize = 11.sp) },
                textStyle = TextStyle(color = TextUltraLight, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                modifier = Modifier.weight(1f).testTag("service_scan_host_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GlassAccentRed,
                    unfocusedBorderColor = GlassBorder
                ),
                singleLine = true
            )

            IconButton(
                onClick = { onTriggerScan(targetHost) },
                enabled = !isScanning,
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isScanning) Color.Transparent else GlassAccentRed.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .border(1.dp, GlassAccentRed, RoundedCornerShape(10.dp))
                    .testTag("service_scan_trigger_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Scan Services",
                    tint = GlassAccentRed
                )
            }
        }

        if (isScanning) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = GlassAccentRed,
                    trackColor = GlassBorder
                )
                Text("SCANNING IN PROGRESS... ${(progress * 100).toInt()}%", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(results) { record ->
                val isHighRisk = record.riskLevel == "HIGH"
                val isMedRisk = record.riskLevel == "MEDIUM"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isHighRisk) GlassAccentRed else if (isMedRisk) GlassAccentAmber else GlassBorder,
                            RoundedCornerShape(14.dp)
                        ),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassSurface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isHighRisk) GlassAccentRed else if (isMedRisk) GlassAccentAmber else GlassAccentGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(record.serviceName, color = TextUltraLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isHighRisk) GlassAccentRed else if (isMedRisk) GlassAccentAmber else GlassAccentGreen
                                ),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(record.status, color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Port: ${record.port} | Version: ${record.version}", color = TextLightSlate, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(record.description, color = TextLightSlate, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun WebFormAuditPanel(
    isChecking: Boolean,
    audits: List<FormSecurityAudit>,
    onTriggerAudit: (String) -> Unit
) {
    var targetUrl by remember { mutableStateOf("https://secure-login.node/auth") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "WEB FORM SECURITY AUDITOR",
                        color = GlassAccentAmber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Scrape HTML forms on public web sockets to verify CSRF validation tokens and sanitize anti-XSS posture.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = targetUrl,
                onValueChange = { targetUrl = it },
                label = { Text("Webpage URL", color = TextLightSlate, fontSize = 11.sp) },
                textStyle = TextStyle(color = TextUltraLight, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                modifier = Modifier.weight(1f).testTag("web_form_url_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GlassAccentAmber,
                    unfocusedBorderColor = GlassBorder
                ),
                singleLine = true
            )

            IconButton(
                onClick = { onTriggerAudit(targetUrl) },
                enabled = !isChecking,
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isChecking) Color.Transparent else GlassAccentAmber.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .border(1.dp, GlassAccentAmber, RoundedCornerShape(10.dp))
                    .testTag("web_form_trigger_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Audit Form",
                    tint = GlassAccentAmber
                )
            }
        }

        if (isChecking) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = GlassAccentAmber)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Analyzing webpage DOM elements...", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(audits) { audit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "AUDIT RESULTS: ${audit.url}",
                                color = TextUltraLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("HTML forms found:", color = TextLightSlate, fontSize = 11.sp)
                                Text("${audit.formsCount}", color = TextUltraLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Transport Layer (HTTPS):", color = TextLightSlate, fontSize = 11.sp)
                                Text(
                                    text = if (audit.hasHttps) "SECURED" else "INSECURE (CLEARTEXT)",
                                    color = if (audit.hasHttps) GlassAccentGreen else GlassAccentRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Anti-CSRF Status:", color = TextLightSlate, fontSize = 11.sp)
                                Text(
                                    text = audit.csrfStatus,
                                    color = if (audit.csrfStatus == "SECURE") GlassAccentGreen else GlassAccentRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("XSS Posture Status:", color = TextLightSlate, fontSize = 11.sp)
                                Text(
                                    text = audit.xssStatus,
                                    color = if (audit.xssStatus == "PROTECTED") GlassAccentGreen else GlassAccentRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text("SECURITY INTEGRITY SCORE", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                LinearProgressIndicator(
                                    progress = { audit.riskScore / 100f },
                                    modifier = Modifier.weight(1f),
                                    color = if (audit.riskScore > 75) GlassAccentGreen else if (audit.riskScore > 50) GlassAccentAmber else GlassAccentRed,
                                    trackColor = GlassBorder
                                )
                                Text("${audit.riskScore}/100", color = TextUltraLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text("RECOMPILATION PROTOCOLS", color = GlassAccentAmber, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            audit.recommendations.forEach { recommendation ->
                                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 4.dp)) {
                                    Text("• ", color = GlassAccentAmber, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                    Text(recommendation, color = TextLightSlate, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DbInjectValidatorPanel(
    isValidating: Boolean,
    results: List<SqlValidationResult>,
    onValidate: (String) -> Unit
) {
    var queryInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SQL DATABASE INPUT VALIDATOR",
                        color = GlassAccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Verify user inputs against common SQL injection structures, escaping string pool sequences dynamically.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        OutlinedTextField(
            value = queryInput,
            onValueChange = { queryInput = it },
            label = { Text("Query/String Input", color = TextLightSlate, fontSize = 11.sp) },
            textStyle = TextStyle(color = TextUltraLight, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
            modifier = Modifier.fillMaxWidth().testTag("sql_input_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GlassAccentBlue,
                unfocusedBorderColor = GlassBorder
            )
        )

        Button(
            onClick = { onValidate(queryInput) },
            enabled = !isValidating,
            modifier = Modifier.fillMaxWidth().testTag("sql_validate_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = GlassAccentBlue.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, GlassAccentBlue)
        ) {
            Text("SECURE & SANITIZE", color = GlassAccentBlue, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }

        if (isValidating) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GlassAccentBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results) { result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (result.isSafe) GlassAccentGreen else GlassAccentRed,
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("STATUS POSTURE AUDIT", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (result.isSafe) GlassAccentGreen else GlassAccentRed),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (result.isSafe) "SAFE" else "THREAT INTERCEPTED",
                                        color = Color.Black,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Text("INPUT QUERY", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                            Text(result.inputQuery, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(vertical = 4.dp))

                            if (result.detectedThreats.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("DETECTED RISK THREATS", color = GlassAccentRed, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                result.detectedThreats.forEach { threat ->
                                    Text("• $threat", color = GlassAccentRed, fontSize = 11.sp, modifier = Modifier.padding(vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text("VALIDATION STEPS ENFORCED", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            result.sanitizationSteps.forEach { step ->
                                Text("• $step", color = TextLightSlate, fontSize = 10.sp, modifier = Modifier.padding(vertical = 1.dp))
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("SANITIZED OUTPUT PAYLOAD", color = GlassAccentGreen, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Text(
                                text = result.sanitizedOutput,
                                color = GlassAccentGreen,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PublicProfilesPanel(
    isDiscovering: Boolean,
    profiles: List<PublicProfileInfo>,
    onTriggerSearch: (String) -> Unit
) {
    var identityInput by remember { mutableStateOf("operator_zero") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "OSINT PUBLIC PROFILE SCANNER",
                        color = GlassAccentAmber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cross-reference target usernames across major platforms to find active networks and public keys.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = identityInput,
                onValueChange = { identityInput = it },
                label = { Text("Search Handle / Nickname", color = TextLightSlate, fontSize = 11.sp) },
                textStyle = TextStyle(color = TextUltraLight, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                modifier = Modifier.weight(1f).testTag("osint_identity_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GlassAccentAmber,
                    unfocusedBorderColor = GlassBorder
                ),
                singleLine = true
            )

            IconButton(
                onClick = { onTriggerSearch(identityInput) },
                enabled = !isDiscovering,
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isDiscovering) Color.Transparent else GlassAccentAmber.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .border(1.dp, GlassAccentAmber, RoundedCornerShape(10.dp))
                    .testTag("osint_trigger_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = "Search Identity",
                    tint = GlassAccentAmber
                )
            }
        }

        if (isDiscovering) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = GlassAccentAmber)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Scanning index nodes...", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(profiles) { profile ->
                    val isActive = profile.status == "ACTIVE"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(profile.platform, color = TextUltraLight, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isActive) GlassAccentGreen else GlassBorder),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = profile.status,
                                        color = if (isActive) Color.Black else TextLightSlate,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("URL: ${profile.profileUrl}", color = GlassAccentBlue, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                            if (isActive && profile.foundPublicData.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("HARVESTED DATASET", color = TextLightSlate, fontSize = 8.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                profile.foundPublicData.forEach { data ->
                                    Text("• $data", color = TextLightSlate, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneMetadataPanel(
    isRetrieving: Boolean,
    results: List<PhoneMetadata>,
    onTriggerScan: (String) -> Unit
) {
    var phoneInput by remember { mutableStateOf("+1 555-019-2842") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PHONE NUMBER METADATA ANALYZER",
                        color = GlassAccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Extract country codes, carrier associations, line type registry indexes, and risk/spam metadata scores.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = phoneInput,
                onValueChange = { phoneInput = it },
                label = { Text("Phone Number", color = TextLightSlate, fontSize = 11.sp) },
                textStyle = TextStyle(color = TextUltraLight, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                modifier = Modifier.weight(1f).testTag("phone_number_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GlassAccentBlue,
                    unfocusedBorderColor = GlassBorder
                ),
                singleLine = true
            )

            IconButton(
                onClick = { onTriggerScan(phoneInput) },
                enabled = !isRetrieving,
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isRetrieving) Color.Transparent else GlassAccentBlue.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .border(1.dp, GlassAccentBlue, RoundedCornerShape(10.dp))
                    .testTag("phone_trigger_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Search Phone",
                    tint = GlassAccentBlue
                )
            }
        }

        if (isRetrieving) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GlassAccentBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results) { meta ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("NUMBER: ${meta.rawNumber}", color = TextUltraLight, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (meta.isValid) GlassAccentGreen else GlassAccentRed),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (meta.isValid) "VALID REGISTER" else "INVALID FORMAT",
                                        color = Color.Black,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Country Code Index:", color = TextLightSlate, fontSize = 11.sp)
                                Text(meta.countryCode, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Active Carrier network:", color = TextLightSlate, fontSize = 11.sp)
                                Text(meta.carrier, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Registry Location:", color = TextLightSlate, fontSize = 11.sp)
                                Text(meta.location, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Line Signal Type:", color = TextLightSlate, fontSize = 11.sp)
                                Text(meta.lineType, color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text("RISK FRAUD / SPAM ASSESSMENT", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                LinearProgressIndicator(
                                    progress = { meta.spamScore / 100f },
                                    modifier = Modifier.weight(1f),
                                    color = if (meta.spamScore > 60) GlassAccentRed else if (meta.spamScore > 30) GlassAccentAmber else GlassAccentGreen,
                                    trackColor = GlassBorder
                                )
                                Text("${meta.spamScore}% SPAM", color = if (meta.spamScore > 60) GlassAccentRed else if (meta.spamScore > 30) GlassAccentAmber else GlassAccentGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmailHarvesterPanel(
    isHarvesting: Boolean,
    emails: List<EmailHarvestResult>,
    onTriggerScan: (String) -> Unit
) {
    var domainInput by remember { mutableStateOf("secure-node.org") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "OSINT EMAIL ADDRESS HARVESTER",
                        color = GlassAccentGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Crawl public websites and metadata endpoints of a target domain to harvest exposed emails and verify delivery.",
                        color = TextLightSlate,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = domainInput,
                onValueChange = { domainInput = it },
                label = { Text("Target Domain", color = TextLightSlate, fontSize = 11.sp) },
                textStyle = TextStyle(color = TextUltraLight, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                modifier = Modifier.weight(1f).testTag("email_domain_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GlassAccentGreen,
                    unfocusedBorderColor = GlassBorder
                ),
                singleLine = true
            )

            IconButton(
                onClick = { onTriggerScan(domainInput) },
                enabled = !isHarvesting,
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isHarvesting) Color.Transparent else GlassAccentGreen.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .border(1.dp, GlassAccentGreen, RoundedCornerShape(10.dp))
                    .testTag("email_trigger_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Harvest Emails",
                    tint = GlassAccentGreen
                )
            }
        }

        if (isHarvesting) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = GlassAccentGreen)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Crawling targets on domain index...", color = TextLightSlate, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(emails) { record ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(record.email, color = TextUltraLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (record.isDeliverable) GlassAccentGreen else GlassBorder),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (record.isDeliverable) "DELIVERABLE" else "UNDELIVERABLE",
                                        color = if (record.isDeliverable) Color.Black else TextLightSlate,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Source URL: ${record.foundOnUrl}", color = GlassAccentBlue, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Discovery context: ${record.sourceContext}", color = TextLightSlate, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

// --- MULTI-AGENT COORDINATOR UI PANEL ---
@Composable
fun MultiAgentPanel(
    isWorking: Boolean,
    progress: Float,
    consensusOutput: String,
    agents: List<AgentConfig>,
    onRunTask: (String) -> Unit,
    onToggleAgent: (String, Boolean) -> Unit,
    onUpdateApiMode: (String, String) -> Unit,
    onUpdateApiKey: (String, String) -> Unit,
    onUpdateCustomUrl: (String, String) -> Unit,
    onUpdateCustomHeaders: (String, String) -> Unit,
    onUpdateAgentTools: (String, List<String>) -> Unit
) {
    val allTools = remember {
        listOf(
            "WIFI SCANNER", "WIFI AUTH", "INTERFACES", "DNS LOGGER", "TLS CHECKER",
            "HTTP DEBUGGER", "BLUETOOTH", "NFC", "CAMERA FEED", "MESSAGE FLOW",
            "CALL LOGS", "FILE SYSTEM", "BACKUP TOOL", "SERVICE SECURITY", "WEB FORM AUDIT",
            "DB INJECT VALIDATOR", "PUBLIC PROFILES", "PHONE METADATA", "EMAIL HARVESTER",
            "PACKET LOGGER", "DEVICE DISCOVERY", "BANDWIDTH", "PORT DISCOVERY", "REACHABILITY", "HISTORY"
        )
    }

    var taskInput by remember { mutableStateOf("") }
    var selectedAgentForDetails by remember { mutableStateOf<AgentConfig?>(null) }
    var showToolDialogForAgent by remember { mutableStateOf<AgentConfig?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        // Header Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(GlassAccentBlue.copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, GlassAccentBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🤖", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "COLLABORATIVE MULTI-AGENT ENGINE",
                            color = TextUltraLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Orchestrate 8 specialized models in real-time. Link them together to execute multi-step audits using all 25 hardware diagnostics.",
                            color = TextLightSlate,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Active Tasks and Interactive Controller Block
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurfaceVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ENTER COLLABORATIVE MASTER DIRECTIVE",
                    color = GlassAccentBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = taskInput,
                        onValueChange = { taskInput = it },
                        placeholder = { Text("e.g. Audit local network security, scan open ports, list interfaces, and check secure-node.org TLS specs", color = TextLightSlate, fontSize = 11.sp) },
                        textStyle = TextStyle(color = TextUltraLight, fontSize = 12.sp, fontFamily = FontFamily.Monospace),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("multi_agent_prompt_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GlassAccentBlue,
                            unfocusedBorderColor = GlassBorder
                        ),
                        singleLine = false,
                        maxLines = 3
                    )

                    Button(
                        onClick = { onRunTask(taskInput) },
                        enabled = !isWorking && taskInput.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GlassAccentBlue,
                            disabledContainerColor = GlassBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(56.dp)
                            .testTag("multi_agent_run_btn"),
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        if (isWorking) {
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run Joint Task", tint = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("RUN TASK", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                if (isWorking) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                        color = GlassAccentBlue,
                        trackColor = GlassBorder,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "COLLABORATING PIPELINES...",
                            color = TextLightSlate,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = GlassAccentBlue,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Main Display Section: Horizontal Split or Vertical Scroll based on focus
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Consensus Report Output Panel
            if (consensusOutput.isNotEmpty() || isWorking) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SYNTHESIZED EXECUTIVE CONSENSUS REPORT",
                                    color = GlassAccentGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = GlassAccentGreen.copy(alpha = 0.15f)),
                                    border = BorderStroke(1.dp, GlassAccentGreen),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (isWorking) "COMPILING..." else "STABLE CONSENSUS",
                                        color = GlassAccentGreen,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = if (consensusOutput.isEmpty()) "Orchestration synthesis in progress. Waiting for specialist agent analysis blocks..." else consensusOutput,
                                    color = TextUltraLight,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }
                    }
                }
            }

            // Agents Config Panel Headers
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CONSTELLATION AGENTS",
                        color = TextLightSlate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "CLICK AGENT TO CONFIGURE",
                        color = GlassAccentBlue,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Grid of Agents (Rendered as rows)
            items(agents) { agent ->
                val isSelected = selectedAgentForDetails?.id == agent.id
                val color = Color(android.graphics.Color.parseColor(agent.colorHex))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isSelected) color else GlassBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedAgentForDetails = if (isSelected) null else agent },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) GlassSurfaceVariant else GlassSurface
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(color.copy(alpha = 0.15f), CircleShape)
                                        .border(1.dp, color, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(agent.iconEmoji, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = agent.name,
                                        color = TextUltraLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "MODEL: ${agent.modelType}",
                                            color = color,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(modifier = Modifier.size(3.dp).background(TextLightSlate, CircleShape))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = agent.statusText.uppercase(),
                                            color = if (agent.isThinking) GlassAccentAmber else TextLightSlate,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(
                                    checked = agent.isEnabled,
                                    onCheckedChange = { onToggleAgent(agent.id, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = color,
                                        checkedTrackColor = color.copy(alpha = 0.3f),
                                        uncheckedThumbColor = GlassBorder,
                                        uncheckedTrackColor = Color.Transparent
                                    ),
                                    modifier = Modifier.scale(0.8f).testTag("switch_agent_${agent.id}")
                                )
                            }
                        }

                        // Expanded detail configurations and action logs
                        if (isSelected) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = GlassBorder)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = agent.description,
                                color = TextLightSlate,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            // API Mode Setup Row
                            Text(
                                text = "API GATEWAY PROVIDER",
                                color = color,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("DEMO", "REAL", "CUSTOM").forEach { mode ->
                                    val isModeSelected = agent.apiMode == mode
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isModeSelected) color.copy(alpha = 0.2f) else GlassSurface)
                                            .border(1.dp, if (isModeSelected) color else GlassBorder, RoundedCornerShape(8.dp))
                                            .clickable { onUpdateApiMode(agent.id, mode) }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (mode == "DEMO") "FREE DEMO" else if (mode == "REAL") "REAL KEY" else "CUSTOM API",
                                            color = if (isModeSelected) TextUltraLight else TextLightSlate,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Dynamic details based on provider mode
                            when (agent.apiMode) {
                                "REAL" -> {
                                    OutlinedTextField(
                                        value = agent.apiKey,
                                        onValueChange = { onUpdateApiKey(agent.id, it) },
                                        label = { Text("Secure Provider API Key", fontSize = 10.sp, color = TextLightSlate) },
                                        textStyle = TextStyle(color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace),
                                        modifier = Modifier.fillMaxWidth().testTag("api_key_${agent.id}"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = color,
                                            unfocusedBorderColor = GlassBorder
                                        ),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                                "CUSTOM" -> {
                                    OutlinedTextField(
                                        value = agent.customUrl,
                                        onValueChange = { onUpdateCustomUrl(agent.id, it) },
                                        placeholder = { Text("https://api.yourdomain.com/v1/chat", fontSize = 10.sp, color = TextLightSlate) },
                                        label = { Text("Custom API Endpoint URL", fontSize = 10.sp, color = TextLightSlate) },
                                        textStyle = TextStyle(color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace),
                                        modifier = Modifier.fillMaxWidth().testTag("custom_url_${agent.id}"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = color,
                                            unfocusedBorderColor = GlassBorder
                                        ),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = agent.customHeaders,
                                        onValueChange = { onUpdateCustomHeaders(agent.id, it) },
                                        placeholder = { Text("Authorization: Bearer key\nContent-Type: application/json", fontSize = 10.sp, color = TextLightSlate) },
                                        label = { Text("Custom HTTP Headers (Key: Value per line)", fontSize = 10.sp, color = TextLightSlate) },
                                        textStyle = TextStyle(color = TextUltraLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace),
                                        modifier = Modifier.fillMaxWidth().testTag("custom_headers_${agent.id}"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = color,
                                            unfocusedBorderColor = GlassBorder
                                        ),
                                        singleLine = false,
                                        maxLines = 3
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }

                            // Tool Assignment Checklist
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ASSIGNED HARDWARE TOOLS (${agent.selectedTools.size})",
                                    color = color,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "CONFIGURE DELEGATES",
                                    color = GlassAccentBlue,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.clickable { showToolDialogForAgent = agent }
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (agent.selectedTools.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text("No tools assigned. Runs without real-time telemetry.", color = TextLightSlate, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    }
                                } else {
                                    agent.selectedTools.forEach { tool ->
                                        Box(
                                            modifier = Modifier
                                                .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                                .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(tool, color = TextUltraLight, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action Logs Streams
                            Text(
                                text = "REAL-TIME AGENT LOGS",
                                color = TextLightSlate,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                    .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                val lazyListState = rememberLazyListState()
                                LaunchedEffect(agent.logEntries.size) {
                                    if (agent.logEntries.isNotEmpty()) {
                                        lazyListState.animateScrollToItem(agent.logEntries.size - 1)
                                    }
                                }
                                LazyColumn(
                                    state = lazyListState,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    if (agent.logEntries.isEmpty()) {
                                        item {
                                            Text("Console idle. Ready for command orchestration...", color = TextLightSlate, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                        }
                                    } else {
                                        items(agent.logEntries) { log ->
                                            Text(log, color = color, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }

                            if (agent.currentResult.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "INDIVIDUAL SECURE SUMMARY",
                                    color = TextLightSlate,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                                        .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                        .padding(10.dp)
                                ) {
                                    Text(agent.currentResult, color = TextUltraLight, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Custom Interactive Tool Assignment Dialog
    if (showToolDialogForAgent != null) {
        val activeAgent = showToolDialogForAgent!!
        val agentColor = Color(android.graphics.Color.parseColor(activeAgent.colorHex))
        
        AlertDialog(
            onDismissRequest = { showToolDialogForAgent = null },
            title = {
                Text(
                    text = "DELEGATE TOOLS: ${activeAgent.name.uppercase()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = TextUltraLight
                )
            },
            text = {
                Box(modifier = Modifier.height(300.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(allTools) { tool ->
                            val isChecked = activeAgent.selectedTools.contains(tool)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        val newList = if (isChecked) {
                                            activeAgent.selectedTools - tool
                                        } else {
                                            activeAgent.selectedTools + tool
                                        }
                                        onUpdateAgentTools(activeAgent.id, newList)
                                        // Update local state to reflect check change immediately
                                        showToolDialogForAgent = activeAgent.copy(selectedTools = newList)
                                    }
                                    .padding(vertical = 4.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = tool,
                                    color = if (isChecked) TextUltraLight else TextLightSlate,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        val newList = if (checked == false) {
                                            activeAgent.selectedTools - tool
                                        } else {
                                            activeAgent.selectedTools + tool
                                        }
                                        onUpdateAgentTools(activeAgent.id, newList)
                                        showToolDialogForAgent = activeAgent.copy(selectedTools = newList)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = agentColor,
                                        checkmarkColor = Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showToolDialogForAgent = null }) {
                    Text("SAVE CHANGES", color = agentColor, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            },
            containerColor = GlassSurfaceVariant,
            modifier = Modifier.border(1.dp, agentColor, RoundedCornerShape(28.dp))
        )
    }
}

@Composable
fun PrivacyDashboardPanel(
    viewModel: com.example.viewmodel.NetworkViewModel
) {
    val privacyToggles by viewModel.privacyToggles.collectAsStateWithLifecycle()
    val lastScanTime by viewModel.lastPrivacyScanTime.collectAsStateWithLifecycle()

    val activeCount = privacyToggles.values.count { it }
    val totalCount = privacyToggles.size
    val score = if (totalCount > 0) (activeCount * 100) / totalCount else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // SUMMARY CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                .testTag("privacy_summary_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Circular score progress
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(80.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { score.toFloat() / 100f },
                        modifier = Modifier.size(80.dp),
                        color = if (score > 70) GlassAccentGreen else if (score > 40) GlassAccentAmber else GlassAccentRed,
                        strokeWidth = 8.dp,
                        trackColor = GlassBorder
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score%",
                            color = TextUltraLight,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "SCORE",
                            color = TextLightSlate,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Summary info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PRIVACY SHIELD STATUS",
                        color = GlassAccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$activeCount / $totalCount ACTIVE PROTECTIONS",
                        color = TextUltraLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "LAST REFRESHED:\n$lastScanTime",
                        color = TextLightSlate,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Refresh button
                IconButton(
                    onClick = { viewModel.refreshPrivacyScan() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(GlassSurfaceVariant, CircleShape)
                        .border(1.dp, GlassBorder, CircleShape)
                        .testTag("privacy_refresh_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh privacy status",
                        tint = GlassAccentBlue
                    )
                }
            }
        }

        // MODULE CARDS CONTAINER
        Text(
            text = "PRIVACY CONTROL DECK",
            color = TextLightSlate,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(start = 4.dp, top = 8.dp)
        )

        val modules = listOf(
            Triple("NETWORK_FIREWALL", "NETWORK FIREWALL", "Blocks internet access per application to prevent silent leaks."),
            Triple("TOR_ROUTING", "TOR ROUTING", "Encrypts and bounces traffic through the Tor relay network."),
            Triple("TRACKER_BLOCKER", "TRACKER BLOCKER", "Intercepts and blocks known tracker and marketing scripts."),
            Triple("ENCRYPTION_MODE", "ENCRYPTION MODE", "Enforces end-to-end cryptographic encryption on communication tunnels."),
            Triple("INCOGNITO_MODE", "INCOGNITO MODE", "Instantly sweeps local activity, scans, and AI history on disable."),
            Triple("DNS_OVER_HTTPS", "DNS OVER HTTPS", "Encrypts all outgoing DNS resolving sequences to bypass ISP spying."),
            Triple("PROXY_CHAINING", "PROXY CHAINING", "Cascades application packages over secure multi-layered remote relays."),
            Triple("CONNECTION_ANONYMIZER", "CONNECTION ANONYMIZER", "Spoofs MAC address, host node name, and local gateway ID."),
            Triple("SECURE_VAULT", "SECURE VAULT", "Sandboxes internal storage databases behind strict secure files."),
            Triple("PERMISSION_MONITOR", "PERMISSION MONITOR", "Scans high-risk Android service access permissions."),
            Triple("PACKET_LOGGER", "PACKET LOGGER", "Runs real-time packet sniffer to audit incoming data payloads."),
            Triple("DEVICE_DISCOVERY", "DEVICE DISCOVERY", "Audits local LAN subnets to identify other hardware nodes."),
            Triple("PORT_SCANNER", "PORT SCANNER", "Sweeps local gateways for open vulnerabilities and ports."),
            Triple("HOST_REACHABILITY", "HOST REACHABILITY", "Triggers secure reachability check and ICMP ping diagnostics."),
            Triple("BANDWIDTH_MONITOR", "BANDWIDTH MONITOR", "Tracks actual transmission speeds and throughput metrics.")
        )

        modules.forEach { (key, title, description) ->
            val isActive = privacyToggles[key] ?: false
            val icon = when (key) {
                "NETWORK_FIREWALL" -> Icons.Default.Security
                "TOR_ROUTING" -> Icons.Default.Router
                "TRACKER_BLOCKER" -> Icons.Default.Block
                "ENCRYPTION_MODE" -> Icons.Default.VpnKey
                "INCOGNITO_MODE" -> Icons.Default.VisibilityOff
                "DNS_OVER_HTTPS" -> Icons.Default.Dns
                "PROXY_CHAINING" -> Icons.Default.Link
                "CONNECTION_ANONYMIZER" -> Icons.Default.Shuffle
                "SECURE_VAULT" -> Icons.Default.Folder
                "PERMISSION_MONITOR" -> Icons.Default.Warning
                "PACKET_LOGGER" -> Icons.Default.Assignment
                "DEVICE_DISCOVERY" -> Icons.Default.Devices
                "PORT_SCANNER" -> Icons.Default.Search
                "HOST_REACHABILITY" -> Icons.Default.Refresh
                else -> Icons.Default.Speed
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                    .testTag("privacy_card_$key"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GlassSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Status dot overlay with icon
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(if (isActive) GlassSurfaceVariant else GlassSurface, CircleShape)
                                .border(1.dp, GlassBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "$title icon",
                                tint = if (isActive) GlassAccentGreen else TextMuted,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        // Status dot (active = green, inactive = gray)
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(if (isActive) GlassAccentGreen else TextMuted, CircleShape)
                                .border(1.5.dp, GlassBg, CircleShape)
                        )
                    }

                    // Content details
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title,
                                color = TextUltraLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = description,
                            color = TextLightSlate,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }

                    // Toggle Switch
                    Switch(
                        checked = isActive,
                        onCheckedChange = { viewModel.setPrivacyToggle(key, it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GlassAccentGreen,
                            checkedTrackColor = GlassAccentGreen.copy(alpha = 0.4f),
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = GlassSurfaceVariant
                        ),
                        modifier = Modifier.testTag("privacy_toggle_${key.lowercase()}")
                    )
                }
            }
        }
    }
}

