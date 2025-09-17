package com.nsutrack.financetracker.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SubscriptionManager(private val context: Context) {
    
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { src, _, _ ->
            com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        })
        .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
            LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        })
        .create()
    private val prefs = context.getSharedPreferences("subscriptions_prefs", Context.MODE_PRIVATE)
    
    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions.asStateFlow()
    
    init {
        loadSubscriptions()
    }
    
    fun addSubscription(name: String, amount: Double) {
        val newSubscription = Subscription(
            name = name,
            amount = amount,
            createdAt = LocalDateTime.now()
        )
        
        val updatedList = (_subscriptions.value + newSubscription).sortedByDescending { it.createdAt }
        _subscriptions.value = updatedList
        saveSubscriptions()
    }
    
    fun removeSubscription(subscriptionId: String) {
        val updatedList = _subscriptions.value.filter { it.id != subscriptionId }
        _subscriptions.value = updatedList
        saveSubscriptions()
    }
    
    fun getTotalMonthlyAmount(): Double {
        return _subscriptions.value.sumOf { it.amount }
    }
    
    fun getTotalWeeklyAmount(): Double {
        return getTotalMonthlyAmount() / 4.0 // Rough weekly estimate
    }
    
    private fun saveSubscriptions() {
        val json = gson.toJson(_subscriptions.value)
        prefs.edit().putString("subscriptions_list", json).apply()
    }
    
    private fun loadSubscriptions() {
        val json = prefs.getString("subscriptions_list", null)
        if (json != null) {
            try {
                val type = object : TypeToken<List<Subscription>>() {}.type
                val subscriptions: List<Subscription> = gson.fromJson(json, type)
                _subscriptions.value = subscriptions.sortedByDescending { it.createdAt }
            } catch (e: Exception) {
                _subscriptions.value = emptyList()
            }
        }
    }
}