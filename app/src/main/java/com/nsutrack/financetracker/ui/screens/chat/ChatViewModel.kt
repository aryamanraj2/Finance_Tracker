package com.nsutrack.financetracker.ui.screens.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsutrack.financetracker.data.ChatMessage
import com.nsutrack.financetracker.data.ChatState
import com.nsutrack.financetracker.data.MessageRole
import com.nsutrack.financetracker.ui.utils.OnboardingManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    
    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()
    
    private val financialInsightTemplates = listOf(
        "Based on your spending pattern, I notice you're spending around {weeklyAmount} per week. That's {percentage} of your monthly allowance of ₹{monthlyAllowance}. As a student in India, here are some insights:\n\n💡 **Smart Tips:**\n• Consider using student discounts for food delivery apps\n• Try cooking simple meals - it can save 40-50% on food costs\n• Use public transport or cycle when possible\n• Set aside ₹{savingsAmount} monthly in a liquid fund for emergencies",
        
        "Looking at your financial data, you have ₹{monthlyAllowance} monthly allowance and spend roughly ₹{weeklyAmount} weekly. Here's my analysis:\n\n📊 **Financial Health:** {healthStatus}\n\n🎯 **Student-focused advice for India:**\n• Open a zero-balance savings account with digital banking\n• Start a monthly SIP of ₹{sipAmount} in an index fund\n• Use apps like Paytm, PhonePe for cashbacks\n• Track subscriptions - cancel unused Netflix, Spotify, etc.\n• Consider freelancing or part-time tutoring for extra income",
        
        "Your spending pattern shows ₹{weeklyAmount} weekly expenses against ₹{monthlyAllowance} monthly budget. As an Indian student, here's what I recommend:\n\n💰 **Investment Starting Guide:**\n• Begin with ELSS mutual funds for tax saving\n• Try micro-investing apps like Groww or Zerodha\n• Start with ₹{sipAmount}/month SIP\n• Keep 3 months expenses as emergency fund\n• Learn about PPF for long-term tax-free growth\n\n🛡️ **Money-saving hacks:**\n• Use student ID for discounts everywhere\n• Buy books second-hand or use library\n• Group buy subscriptions with friends\n• Cook in hostels/PG when allowed"
    )
    
    private val generalAdviceResponses = listOf(
        "Great question! As a student in India, here are some practical investment options:\n\n📈 **Beginner-friendly investments:**\n• **SIP in Index Funds** - Start with ₹500-1000/month\n• **PPF** - ₹500 minimum, 15-year lock-in, tax-free returns\n• **ELSS Funds** - Tax saving + growth potential\n• **Fixed Deposits** - Safe but low returns, good for emergency fund\n\n🎯 **Pro tips:**\n• Use apps like Groww, Kuvera for easy investing\n• Don't invest all money - keep 3-6 months expenses liquid\n• Learn basics before investing in stocks\n• Consider gold ETFs for 5-10% portfolio allocation",
        
        "Excellent question about reducing expenses! Here are India-specific tips for students:\n\n💡 **Food & Dining:**\n• Cook basic meals - saves ₹3000-5000/month\n• Use Zomato Pro, Swiggy One only if you order frequently\n• Try local mess/tiffin services - much cheaper than delivery\n\n🚗 **Transportation:**\n• Use bus passes, metro cards for discounts\n• Share cabs with friends\n• Consider cycling for short distances\n\n📱 **Digital subscriptions:**\n• Share Netflix, Spotify with family/friends\n• Use free alternatives like YouTube Music (with ads)\n• Cancel unused apps and subscriptions\n\n🛍️ **Shopping smart:**\n• Use student discounts everywhere\n• Shop during sales (Big Billion Days, Great Indian Festival)\n• Buy second-hand textbooks, electronics\n• Use cashback apps and credit cards responsibly",
        
        "Here's how you can build wealth as an Indian student:\n\n🎯 **Income Generation:**\n• **Tutoring** - ₹300-800/hour for subjects you're good at\n• **Content Creation** - YouTube, Instagram (takes time but scalable)\n• **Freelancing** - Writing, design, coding on Upwork, Fiverr\n• **Campus jobs** - Library assistant, research assistant\n\n💼 **Skill Development:**\n• Learn high-demand skills: coding, digital marketing, design\n• Get certified in Google Ads, Analytics, AWS\n• Participate in hackathons and competitions\n• Build projects and portfolio\n\n📈 **Long-term Wealth Building:**\n• Start investing early - even ₹500/month compounds significantly\n• Learn about compound interest and power of starting young\n• Read books: \"Rich Dad Poor Dad\", \"The Intelligent Investor\"\n• Follow Indian finance YouTubers: Labour Law Advisor, Asset Yogi\n\n🚀 **Mindset:**\n• Track every expense for 3 months to understand patterns\n• Set financial goals: short-term (6 months) and long-term (5 years)\n• Automate savings - pay yourself first\n• Learn to delay gratification"
    )
    
    fun initializeChat(context: Context) {
        if (_chatState.value.messages.isEmpty()) {
            val monthlyAllowance = OnboardingManager.getMonthlyAllowance(context)
            val monthlySpend = OnboardingManager.getMonthlySpend(context)
            val weeklySpend = (monthlySpend * 12 / 52).toInt() // Convert to weekly
            
            val welcomeMessage = """
                👋 Hi! I'm your AI financial advisor powered by Gemini. 
                
                I've analyzed your spending data:
                • Monthly Allowance: ₹${monthlyAllowance.toInt()}
                • Estimated Weekly Spend: ₹${weeklySpend}
                
                I can help you with:
                💰 Investment advice for students
                📊 Expense optimization tips  
                🎯 Financial planning for Indian students
                💡 Money-saving strategies
                
                What would you like to know about your finances?
            """.trimIndent()
            
            _chatState.value = _chatState.value.copy(
                messages = listOf(
                    ChatMessage(
                        content = welcomeMessage,
                        role = MessageRole.ASSISTANT,
                        shouldAnimate = false // Welcome message should not animate
                    )
                )
            )
        }
    }

    fun onChatResumed() {
        // When the screen is resumed, reset the typing state if we are not currently loading a message.
        // Also disable animations for all existing messages to prevent re-animation on resume
        if (!_chatState.value.isLoading) {
            val messagesWithoutAnimation = _chatState.value.messages.map { message ->
                message.copy(shouldAnimate = false)
            }
            _chatState.value = _chatState.value.copy(
                isTyping = false,
                messages = messagesWithoutAnimation
            )
        }
    }
    
    fun sendMessage(message: String, context: Context) {
        if (message.isBlank()) return
        
        val userMessage = ChatMessage(
            content = message.trim(),
            role = MessageRole.USER
        )
        
        _chatState.value = _chatState.value.copy(
            messages = _chatState.value.messages + userMessage,
            isLoading = true,
            isTyping = true
        )
        
        viewModelScope.launch {
            delay(1500) // Simulate API delay
            
            val response = generateMockResponse(message, context)
            val assistantMessage = ChatMessage(
                content = response,
                role = MessageRole.ASSISTANT
            )
            
            _chatState.value = _chatState.value.copy(
                messages = _chatState.value.messages + assistantMessage,
                isLoading = false,
                isTyping = false
            )
        }
    }
    
    private fun generateMockResponse(userMessage: String, context: Context): String {
        val monthlyAllowance = OnboardingManager.getMonthlyAllowance(context)
        val monthlySpend = OnboardingManager.getMonthlySpend(context)
        val weeklyAmount = (monthlySpend * 12 / 52).toInt()
        val percentage = ((monthlySpend / monthlyAllowance) * 100).toInt()
        val savingsAmount = (monthlyAllowance * 0.2).toInt()
        val sipAmount = (monthlyAllowance * 0.15).toInt()
        
        val healthStatus = when {
            percentage < 70 -> "Excellent - You're saving well!"
            percentage < 85 -> "Good - Room for improvement"
            else -> "Needs attention - High spending"
        }
        
        return when {
            userMessage.contains("analyze", true) || 
            userMessage.contains("insight", true) ||
            userMessage.contains("spending", true) ||
            userMessage.contains("finance", true) -> {
                financialInsightTemplates.random()
                    .replace("{weeklyAmount}", "₹$weeklyAmount")
                    .replace("{monthlyAllowance}", monthlyAllowance.toInt().toString())
                    .replace("{percentage}", "$percentage%")
                    .replace("{savingsAmount}", savingsAmount.toString())
                    .replace("{sipAmount}", sipAmount.toString())
                    .replace("{healthStatus}", healthStatus)
            }
            
            userMessage.contains("invest", true) ||
            userMessage.contains("investment", true) -> {
                generalAdviceResponses[0]
            }
            
            userMessage.contains("reduce", true) ||
            userMessage.contains("save", true) ||
            userMessage.contains("expense", true) -> {
                generalAdviceResponses[1]
            }
            
            userMessage.contains("earn", true) ||
            userMessage.contains("income", true) ||
            userMessage.contains("money", true) -> {
                generalAdviceResponses[2]
            }
            
            else -> {
                // General helpful response
                """
                I understand you're looking for financial advice! Here are some areas I can help with:
                
                💰 **Ask me about:**
                • "How should I invest as a student?"
                • "Analyze my spending patterns"
                • "How can I reduce my expenses?"
                • "Ways to earn extra money"
                • "Best saving strategies for students"
                
                What specific area would you like guidance on?
                """.trimIndent()
            }
        }
    }
    
    fun clearError() {
        _chatState.value = _chatState.value.copy(error = null)
    }
    
    fun onScroll() {
        // Disable animations for all messages when user scrolls
        val messagesWithoutAnimation = _chatState.value.messages.map { message ->
            message.copy(shouldAnimate = false)
        }
        _chatState.value = _chatState.value.copy(messages = messagesWithoutAnimation)
    }
}
