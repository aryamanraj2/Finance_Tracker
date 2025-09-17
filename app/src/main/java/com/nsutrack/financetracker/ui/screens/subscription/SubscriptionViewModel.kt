package com.nsutrack.financetracker.ui.screens.subscription

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nsutrack.financetracker.data.Subscription
import com.nsutrack.financetracker.data.SubscriptionManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(application: Application) : AndroidViewModel(application) {
    
    private val subscriptionManager = SubscriptionManager(application)
    
    val subscriptions: StateFlow<List<Subscription>> = subscriptionManager.subscriptions
    
    fun addSubscription(name: String, amount: Double) {
        if (name.isNotBlank() && amount > 0) {
            viewModelScope.launch {
                subscriptionManager.addSubscription(name.trim(), amount)
            }
        }
    }
    
    fun removeSubscription(subscriptionId: String) {
        viewModelScope.launch {
            subscriptionManager.removeSubscription(subscriptionId)
        }
    }
    
    fun getTotalMonthlyAmount(): Double {
        return subscriptionManager.getTotalMonthlyAmount()
    }
    
    fun getTotalWeeklyAmount(): Double {
        return subscriptionManager.getTotalWeeklyAmount()
    }
}