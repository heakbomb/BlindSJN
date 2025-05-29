package com.glowstudio.android.blindsjn.feature.paymanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.ocr.model.OcrResult
import com.glowstudio.android.blindsjn.feature.paymanagement.model.*
import com.glowstudio.android.blindsjn.feature.paymanagement.repository.PayManagementRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.async

private const val TAG = "PayManagementViewModel"

class PayManagementViewModel(
    private val repository: PayManagementRepository
) : ViewModel() {
    private val _selectedPeriod = MutableStateFlow("일")
    val selectedPeriod: StateFlow<String> = _selectedPeriod

    private val _salesSummary = MutableStateFlow<SalesSummaryResponse?>(null)
    val salesSummary: StateFlow<SalesSummaryResponse?> = _salesSummary

    private val _salesComparison = MutableStateFlow<SalesComparisonResponse?>(null)
    val salesComparison: StateFlow<SalesComparisonResponse?> = _salesComparison

    private val _topItems = MutableStateFlow<TopItemsResponse?>(null)
    val topItems: StateFlow<TopItemsResponse?> = _topItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _weeklySales = MutableStateFlow<List<Double>>(emptyList())
    val weeklySales: StateFlow<List<Double>> = _weeklySales

    private val _weeklyAverage = MutableStateFlow<Double>(0.0)
    val weeklyAverage: StateFlow<Double> = _weeklyAverage

    private val _monthlyGoal = MutableStateFlow<Double>(3500000.0) // 기본값 350만원
    val monthlyGoal: StateFlow<Double> = _monthlyGoal

    private val _monthlyProgress = MutableStateFlow<Double>(0.0)
    val monthlyProgress: StateFlow<Double> = _monthlyProgress

    private val _showGoalSettingDialog = MutableStateFlow(false)
    val showGoalSettingDialog: StateFlow<Boolean> = _showGoalSettingDialog

    private val _showFixedCostSettingDialog = MutableStateFlow(false)
    val showFixedCostSettingDialog: StateFlow<Boolean> = _showFixedCostSettingDialog

    private val _fixedCost = MutableStateFlow<Double>(1200000.0) // 기본값 120만원
    val fixedCost: StateFlow<Double> = _fixedCost

    private val _dailyAverage = MutableStateFlow<Double>(0.0)
    val dailyAverage: StateFlow<Double> = _dailyAverage

    private val _dailyComparison = MutableStateFlow<Double>(0.0)
    val dailyComparison: StateFlow<Double> = _dailyComparison

    init {
        loadData()
        // loadWeeklySales()  // 주간 데이터 로드 주석 처리
        loadMonthlyGoal()
        // loadDailyAverage() // 일간 평균 데이터 로드 주석 처리
        loadFixedCost()
    }

    fun setPeriod(period: String) {
        _selectedPeriod.value = period
        loadData()
    }

    fun setMonthlyGoal(goal: Double) {
        repository.saveMonthlyGoal(goal)
        _monthlyGoal.value = goal
    }

    fun showGoalSettingDialog() {
        _showGoalSettingDialog.value = true
    }

    fun hideGoalSettingDialog() {
        _showGoalSettingDialog.value = false
    }

    fun showFixedCostSettingDialog() {
        _showFixedCostSettingDialog.value = true
    }

    fun hideFixedCostSettingDialog() {
        _showFixedCostSettingDialog.value = false
    }

    fun setFixedCost(cost: Double) {
        repository.saveFixedCost(cost)
        _fixedCost.value = cost
    }

    private fun loadData() {
        viewModelScope.launch {
            Log.d(TAG, "데이터 로드 시작. Period: ${_selectedPeriod.value}")
            try {
                _isLoading.value = true
                _error.value = null

                val today = LocalDate.now()
                val dateStr = today.format(DateTimeFormatter.ISO_DATE)
                val periodForApi = when (_selectedPeriod.value) {
                    "일" -> "day"
                    "주" -> "week"
                    "월" -> "month"
                    "연" -> "year"
                    else -> "day"
                }

                // 핵심 API 호출만 유지
                val summaryResult = repository.getSalesSummary(dateStr)
                if (summaryResult.status == "success") {
                    _salesSummary.value = summaryResult
                    // 월간 진행률 업데이트는 매출 요약 데이터를 사용
                    updateMonthlyProgress(summaryResult)
                } else {
                    _error.value = summaryResult.message
                }

                // 비교 데이터 API 호출 주석 처리
                /*
                val comparisonResult = repository.getSalesComparison(dateStr)
                if (comparisonResult.status == "success") {
                    _salesComparison.value = comparisonResult
                } else {
                    _error.value = comparisonResult.message
                }
                */

                // 인기 상품 데이터는 추후 구현 예정
                _topItems.value = null

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateMonthlyProgress(summaryResult: SalesSummaryResponse) {
        viewModelScope.launch {
            try {
                if (summaryResult.summary != null) {
                    _monthlyProgress.value = summaryResult.summary.totalSales
                }
            } catch (e: Exception) {
                Log.e(TAG, "월간 진행률 업데이트 실패", e)
            }
        }
    }

    /*
    private fun loadWeeklySales() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong() - 1)
                val weeklySales = mutableListOf<Double>()
                
                // 한 번의 API 호출로 주간 데이터를 가져오도록 수정
                val dateStr = today.format(DateTimeFormatter.ISO_DATE)
                val response = repository.getSalesSummary(dateStr)
                
                if (response.status == "success" && response.data != null) {
                    // 주간 데이터 처리
                    val salesData = response.data
                    for (i in 0..6) {
                        val currentDate = startOfWeek.plusDays(i.toLong())
                        val dateStr = currentDate.format(DateTimeFormatter.ISO_DATE)
                        val daySales = salesData.find { it.date == dateStr }
                        weeklySales.add(daySales?.totalSalesAmount ?: 0.0)
                    }
                } else {
                    // 데이터가 없는 경우 0으로 채움
                    repeat(7) { weeklySales.add(0.0) }
                }
                
                _weeklySales.value = weeklySales
                _weeklyAverage.value = weeklySales.filter { it > 0 }.average()
            } catch (e: Exception) {
                Log.e(TAG, "주간 매출 데이터 로드 실패", e)
            }
        }
    }
    */

    private fun loadMonthlyGoal() {
        _monthlyGoal.value = repository.getMonthlyGoal()
    }

    private fun loadFixedCost() {
        _fixedCost.value = repository.getFixedCost()
    }

    /*
    private fun loadDailyAverage() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val dayOfWeek = today.dayOfWeek.value
                
                // 한 번의 API 호출로 일간 평균 데이터를 가져오도록 수정
                val dateStr = today.format(DateTimeFormatter.ISO_DATE)
                val response = repository.getSalesSummary(dateStr)
                
                if (response.status == "success" && response.data != null) {
                    val salesData = response.data
                    
                    // 현재 요일의 평균 계산
                    val currentDaySales = salesData.filter { 
                        LocalDate.parse(it.date).dayOfWeek.value == dayOfWeek 
                    }.map { it.totalSalesAmount }
                    
                    _dailyAverage.value = if (currentDaySales.isNotEmpty()) {
                        currentDaySales.average()
                    } else {
                        0.0
                    }
                    
                    // 다른 요일의 평균 계산
                    val otherDaysSales = salesData.filter { 
                        LocalDate.parse(it.date).dayOfWeek.value != dayOfWeek 
                    }.map { it.totalSalesAmount }
                    
                    val otherDaysAverage = if (otherDaysSales.isNotEmpty()) {
                        otherDaysSales.average()
                    } else {
                        0.0
                    }
                    
                    // 비교율 계산
                    _dailyComparison.value = if (otherDaysAverage > 0) {
                        ((_dailyAverage.value - otherDaysAverage) / otherDaysAverage) * 100
                    } else {
                        0.0
                    }
                } else {
                    _dailyAverage.value = 0.0
                    _dailyComparison.value = 0.0
                }
            } catch (e: Exception) {
                Log.e(TAG, "일간 평균 데이터 로드 실패", e)
            }
        }
    }
    */

    fun refresh() {
        loadData()
        // loadWeeklySales()  // 주간 데이터 로드 주석 처리
        // loadDailyAverage() // 일간 평균 데이터 로드 주석 처리
    }

    companion object {
        fun provideFactory(
            repository: PayManagementRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PayManagementViewModel(repository) as T
            }
        }
    }
} 