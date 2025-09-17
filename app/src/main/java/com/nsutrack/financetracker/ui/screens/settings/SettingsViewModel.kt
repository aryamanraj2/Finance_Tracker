package com.nsutrack.financetracker.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.nsutrack.financetracker.ui.utils.OnboardingManager

class SettingsViewModel : ViewModel() {
    
    fun getMonthlyAllowance(context: Context): Float {
        return OnboardingManager.getMonthlyAllowance(context)
    }
    
    fun getMonthlySpend(context: Context): Float {
        return OnboardingManager.getMonthlySpend(context)
    }
    
    fun updateFinancialSettings(context: Context, allowance: Double, spend: Double) {
        OnboardingManager.setMonthlyAllowance(context, allowance)
        OnboardingManager.setMonthlySpend(context, spend)
    }
}