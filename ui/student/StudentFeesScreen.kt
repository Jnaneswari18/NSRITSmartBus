package com.example.smartbus.ui.student

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartbus.data.viewmodel.BusViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun StudentFeesScreen(
    viewModel: BusViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val userId = viewModel.getCurrentUserId()
    val feeStatus by if (userId != null) {
        viewModel.getStudentFeeStatus(userId).observeAsState("Pending")
    } else {
        remember { mutableStateOf("Pending") }
    }

    var name by remember { mutableStateOf("") }
    var roll by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("15000") }
    var transactionId by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    // ---------------------------------------------------------------
    // UPI result handler — Firebase is ONLY updated on real SUCCESS
    // ---------------------------------------------------------------
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isProcessing = false
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data         = result.data!!
            val status       = data.getStringExtra("Status") ?: data.getStringExtra("status") ?: ""
            val txnRef       = data.getStringExtra("txnRef") ?: data.getStringExtra("txnId") ?: ""
            val responseCode = data.getStringExtra("responseCode") ?: ""

            when {
                status.equals("SUCCESS", ignoreCase = true) ||
                responseCode.equals("00", ignoreCase = true) -> {
                    transactionId = txnRef
                    coroutineScope.launch {
                        userId?.let { uid ->
                            val paymentAmount = amount.toDoubleOrNull() ?: 0.0
                            val success = viewModel.payStudentFee(uid, paymentAmount).await()
                            if (success) {
                                Toast.makeText(context, "✅ Payment Successful! TxnRef: $txnRef", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Payment done but DB update failed. Contact admin.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                status.equals("FAILURE", ignoreCase = true) -> {
                    Toast.makeText(context, "❌ Payment Failed. Please try again.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context, "⏳ Payment Pending — check PhonePe for status.", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Payment cancelled. No amount was deducted.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Fees Payment",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF1E3C72)
        )

        Spacer(Modifier.height(20.dp))

        if (feeStatus == "Paid") {
            ReceiptUI(
                name   = name.ifEmpty { "Student" },
                roll   = roll.ifEmpty { "N/A" },
                branch = branch.ifEmpty { "N/A" },
                amount = amount,
                txnId  = transactionId
            )

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                onClick = {
                    generatePDF(context, name.ifEmpty { "Student" }, roll.ifEmpty { "N/A" }, branch.ifEmpty { "N/A" }, amount, transactionId)
                }
            ) {
                Text("Download Receipt PDF", color = Color.White)
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "⚠️ Pending Fee Amount:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Confirm Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = roll,
                        onValueChange = { roll = it },
                        label = { Text("Roll Number") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text("Branch") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Fees Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F2EEA)),
                        onClick = {
                            if (name.isEmpty() || roll.isEmpty() || branch.isEmpty() || amount.isEmpty()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val amountVal = amount.toDoubleOrNull()
                            if (amountVal == null || amountVal <= 0) {
                                Toast.makeText(context, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            isProcessing = true
                            launchPhonePePayment(
                                context  = context,
                                launcher = launcher,
                                amount   = amount,
                                txnRef   = "TXN${System.currentTimeMillis()}",
                                payerName = name
                            )
                        }
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                        Text("Pay ₹$amount via PhonePe", color = Color.White)
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Paying to: NSRIT College (nsrit@upi)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------------
// Receipt UI — shows after confirmed SUCCESS
// ------------------------------------------------------------------
@Composable
fun ReceiptUI(name: String, roll: String, branch: String, amount: String, txnId: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "✅ Payment Receipt",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1B5E20)
            )
            Spacer(Modifier.height(12.dp))
            Text("Student Name : $name")
            Text("Roll No       : $roll")
            Text("Branch        : $branch")
            Text("Amount Paid   : ₹$amount")
            Text("Payment Mode  : PhonePe / UPI")
            if (txnId.isNotEmpty()) Text("Transaction ID: $txnId")
            Spacer(Modifier.height(6.dp))
            Text("Status : PAID ✅", color = Color(0xFF2E7D32))
        }
    }
}

// ------------------------------------------------------------------
// Launch PhonePe via UPI Intent
//
// Uses the standard `upi://pay` deep link which all UPI apps handle.
// If PhonePe (com.phonepe.app) is installed it opens directly;
// otherwise a chooser is shown for any installed UPI app.
// Amount MUST be 2 decimal places per UPI specification.
// ------------------------------------------------------------------
fun launchPhonePePayment(
    context: Context,
    launcher: ActivityResultLauncher<Intent>,
    amount: String,
    txnRef: String,
    payerName: String
) {
    val formattedAmount = try {
        String.format("%.2f", amount.toDouble())
    } catch (_: NumberFormatException) {
        "15000.00"
    }

    val upiUri = Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa", "nsrit@upi")          // ← Replace with your actual payee UPI ID from QR
        .appendQueryParameter("pn", "NSRIT College")
        .appendQueryParameter("tr", txnRef)
        .appendQueryParameter("tn", "NSRIT Bus Fee – $payerName")
        .appendQueryParameter("am", formattedAmount)
        .appendQueryParameter("cu", "INR")
        .build()

    val intent = Intent(Intent.ACTION_VIEW, upiUri).apply {
        setPackage("com.phonepe.app")   // Target PhonePe directly
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        launcher.launch(intent)
    } else {
        // Fallback: any UPI app installed on device
        val fallback = Intent(Intent.ACTION_VIEW, upiUri)
        launcher.launch(Intent.createChooser(fallback, "Pay Fees Using UPI"))
    }
}

// ------------------------------------------------------------------
// PDF Receipt generation
// ------------------------------------------------------------------
fun generatePDF(
    context: Context,
    name: String,
    roll: String,
    branch: String,
    amount: String,
    txnId: String = ""
) {
    val pdfDocument = PdfDocument()
    val page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(300, 460, 1).create())
    val paint = Paint().apply { textSize = 12f }
    var y = 30

    page.canvas.apply {
        drawText("NSRIT Bus Fees Receipt", 50f, y.toFloat(), paint)
        y += 30; drawText("Name: $name", 10f, y.toFloat(), paint)
        y += 20; drawText("Roll: $roll", 10f, y.toFloat(), paint)
        y += 20; drawText("Branch: $branch", 10f, y.toFloat(), paint)
        y += 20; drawText("Amount Paid: Rs.$amount", 10f, y.toFloat(), paint)
        y += 20; drawText("Payment Mode: PhonePe / UPI", 10f, y.toFloat(), paint)
        if (txnId.isNotEmpty()) { y += 20; drawText("Transaction ID: $txnId", 10f, y.toFloat(), paint) }
        y += 20; drawText("Status: SUCCESS", 10f, y.toFloat(), paint)
    }
    pdfDocument.finishPage(page)

    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "NSRIT_Fees_Receipt.pdf"
    )
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "Receipt saved in Downloads", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "PDF error: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        pdfDocument.close()
    }
}
