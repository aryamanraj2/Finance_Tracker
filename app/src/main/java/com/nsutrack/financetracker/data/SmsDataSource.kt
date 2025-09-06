package com.nsutrack.financetracker.data

import android.content.Context
import android.provider.Telephony
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class SmsDataSource(private val context: Context) {

    fun readSms(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)

            while (it.moveToNext()) {
                val message = it.getString(bodyIndex)
                val date = it.getLong(dateIndex)
                val transaction = parseSms(message, date)
                if (transaction != null) {
                    transactions.add(transaction)
                }
            }
        }
        return transactions.sortedByDescending { it.date }
    }

    private fun parseSms(message: String, date: Long): Transaction? {
        val keywords = listOf("credited", "debited", "upi", "txn", "vpa", "rs", "received", "paid")
        if (keywords.none { message.contains(it, ignoreCase = true) }) {
            return null
        }

        val amountRegex = Regex("""(?:Rs|INR)\.?\s*([\d,]+\.?\d{0,2})""")
        val amountMatch = amountRegex.find(message)
        val amount = amountMatch?.groups?.get(1)?.value?.replace(",", "")?.toDoubleOrNull() ?: 0.0

        val type = when {
            message.contains("credited", ignoreCase = true) || message.contains("received", ignoreCase = true) -> "credit"
            message.contains("debited", ignoreCase = true) || message.contains("paid", ignoreCase = true) || message.contains("spent", ignoreCase = true) || message.contains("sent", ignoreCase = true) -> "debit"
            else -> return null // Unrecognized UPI SMS
        }

        val upiIdRegex = Regex("""[a-zA-Z0-9.\-_]{2,256}@[a-zA-Z]{2,64}""")
        val upiIdMatch = upiIdRegex.find(message)
        val upiId = upiIdMatch?.value

        val transactionDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault())

        return Transaction(amount, type, transactionDate, upiId, message)
    }
}