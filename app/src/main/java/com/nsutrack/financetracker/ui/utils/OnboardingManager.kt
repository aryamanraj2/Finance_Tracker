package com.nsutrack.financetracker.ui.utils

import android.content.Context
import android.content.SharedPreferences

object OnboardingManager {
    private const val PREFS_NAME = "financetracker_prefs"
    private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    private const val KEY_MONTHLY_ALLOWANCE = "monthly_allowance"
    private const val KEY_MONTHLY_SPEND = "monthly_spend"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isOnboardingCompleted(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_ONBOARDING_COMPLETE, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_ONBOARDING_COMPLETE, completed).apply()
    }

    fun getMonthlyAllowance(context: Context): Float {
        return getPreferences(context).getFloat(KEY_MONTHLY_ALLOWANCE, 0f)
    }

    fun setMonthlyAllowance(context: Context, allowance: Double) {
        getPreferences(context).edit().putFloat(KEY_MONTHLY_ALLOWANCE, allowance.toFloat()).apply()
    }

    fun getMonthlySpend(context: Context): Float {
        return getPreferences(context).getFloat(KEY_MONTHLY_SPEND, 0f)
    }

    fun setMonthlySpend(context: Context, spend: Double) {
        getPreferences(context).edit().putFloat(KEY_MONTHLY_SPEND, spend.toFloat()).apply()
    }
}