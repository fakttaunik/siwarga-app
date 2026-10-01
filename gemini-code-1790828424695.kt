package com.example.siwarga

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WargaItemRow(
    warga: Warga,
    onStatusChange: (Boolean) -> Unit,
    onNominalChange: (Double) -> Unit
) {
    // State lokal untuk menampung teks input nominal
    var textNominal by remember(warga.totalBayar) {
        mutableStateOf(if (warga.totalBayar > 0) warga.totalBayar.toLong().toString() else "")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Informasi Rumah & Nama Warga
            Text(
                text = "${warga.kodeRumah} - ${warga.nama}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Input Nominal Manual
                OutlinedTextField(
                    value = textNominal,
                    onValueChange = { newValue ->
                        // Hanya menerima angka
                        if (newValue.all { it.isDigit() }) {
                            textNominal = newValue
                            val nominalDouble = newValue.toDoubleOrNull() ?: 0.0
                            onNominalChange(nominalDouble)
                        }
                    },
                    label = { Text("Nominal (Rp)") },
                    placeholder = { Text("Masukkan nominal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                    singleLine = true
                )

                // Status Lunas / Belum Lunas
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (warga.statusLunas) "Lunas" else "Belum",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (warga.statusLunas) Color(0xFF2E7D32) else Color.Red
                    )
                    Switch(
                        checked = warga.statusLunas,
                        onCheckedChange = { isChecked ->
                            onStatusChange(isChecked)
                        }
                    )
                }
            }
        }
    }
}