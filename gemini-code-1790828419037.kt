package com.example.siwarga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SiWargaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiWargaApp() {
    val context = LocalContext.current

    var selectedBulan by remember { mutableStateOf("SEPTEMBER") }
    var selectedTahun by remember { mutableStateOf("2026") }

    // Data warga berdasarkan Excel dengan nominal iuran awal
    val dataWarga = remember {
        mutableStateListOf(
            Warga(1, "A3/1", "JACK ZAKARIA", statusLunas = false, totalBayar = 0.0),
            Warga(2, "A3/15", "ARMEN", statusLunas = true, totalBayar = 57000.0),
            Warga(3, "A3/2", "SRI WAHYUNI", statusLunas = true, totalBayar = 57000.0),
            Warga(4, "A3/3", "SUNI", statusLunas = false, totalBayar = 0.0),
            Warga(5, "A3/4", "JENI", statusLunas = false, totalBayar = 0.0),
            Warga(6, "A3/5", "RUDY SALAM", statusLunas = false, totalBayar = 0.0),
            Warga(7, "A3/6", "UCI SRI WAHYUNI", statusLunas = false, totalBayar = 0.0),
            Warga(8, "A3/7", "SUSI", statusLunas = true, totalBayar = 57000.0)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SiWarga - Catatan Iuran") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header Laporan & Tombol Cetak PDF
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Periode Laporan", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = "$selectedBulan $selectedTahun",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(onClick = {
                    PdfPrintHelper.generatePdf(context, selectedBulan, selectedTahun, dataWarga)
                }) {
                    Text("Print PDF")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ringkasan Total Pemasukan Iuran
            val totalLunas = dataWarga.count { it.statusLunas }
            val totalNominalTerkumpul = dataWarga.filter { it.statusLunas }.sumOf { it.totalBayar }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total Lunas", fontSize = 12.sp)
                        Text("$totalLunas / ${dataWarga.size}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total Terkumpul", fontSize = 12.sp, color = Color(0xFF2E7D32))
                        Text(
                            "Rp ${String.format("%,.0f", totalNominalTerkumpul)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List Daftar Rumah
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(dataWarga) { warga ->
                    WargaItemRow(
                        warga = warga,
                        onStatusChange = { isLunas ->
                            val index = dataWarga.indexOf(warga)
                            if (index != -1) {
                                dataWarga[index] = warga.copy(statusLunas = isLunas)
                            }
                        },
                        onNominalChange = { newNominal ->
                            val index = dataWarga.indexOf(warga)
                            if (index != -1) {
                                dataWarga[index] = warga.copy(totalBayar = newNominal)
                            }
                        }
                    )
                }
            }
        }
    }
}