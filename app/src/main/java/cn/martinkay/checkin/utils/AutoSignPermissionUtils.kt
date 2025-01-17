package cn.martinkay.checkin.utils

import android.util.Log
import cn.martinkay.checkin.ENABLE_SMART_RECOGNITION_JUMP
import cn.martinkay.checkin.IS_OPEN_AFTERNOON_OFF_WORK_SIGN_TASK
import cn.martinkay.checkin.IS_OPEN_AFTERNOON_START_WORK_SIGN_TASK
import cn.martinkay.checkin.IS_OPEN_MORNING_OFF_WORK_SIGN_TASK
import cn.martinkay.checkin.IS_OPEN_MORNING_START_WORK_SIGN_TASK
import cn.martinkay.checkin.SIGN_CALENDAR_SCHEME_CACHE
import cn.martinkay.checkin.SIGN_OPEN_INTENT_START_TIME
import cn.martinkay.checkin.SharePrefHelper
import cn.martinkay.checkin.model.CalendarScheme
import com.alibaba.fastjson.JSON
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

object AutoSignPermissionUtils {

    val notifyCalendarSchemeEvent: MutableStateFlow<Unit?> = MutableStateFlow(null)

    private val workingDays = listOf(
        Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY
    )

    fun isTodayAutoSignAllowed(): Boolean {
        val calendarSchemeMap = kotlin.runCatching {
            JSON.parseArray(
                SharePrefHelper.getString(SIGN_CALENDAR_SCHEME_CACHE, ""),
                CalendarScheme::class.java
            ).associateBy { it.date }
        }.onFailure { it.printStackTrace() }.getOrNull()
        val calendar = Calendar.getInstance()
        val workDay = workingDays.contains(calendar[Calendar.DAY_OF_WEEK])
        if (calendarSchemeMap == null) {
            return workDay
        }
        val date = com.haibin.calendarview.Calendar().apply {
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH) + 1
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }.toString()
        val value = calendarSchemeMap[date] ?: return workDay
        return value.scheme != CalendarScheme.AUTO_SIGN_DAY_FORBIDDEN
    }

    fun increaseTodayAutoSignCount() {
        val calendarSchemeMap = kotlin.runCatching {
            JSON.parseArray(
                SharePrefHelper.getString(SIGN_CALENDAR_SCHEME_CACHE, ""),
                CalendarScheme::class.java
            ).associateBy { it.date }.toMutableMap()
        }.onFailure { it.printStackTrace() }.getOrElse { mutableMapOf() }
        val calendar = Calendar.getInstance()
        val date = com.haibin.calendarview.Calendar().apply {
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH) + 1
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }.toString()
        val value = calendarSchemeMap[date] ?: CalendarScheme().apply {
            this.date = date
        }
        value.nextScheme()
        calendarSchemeMap[date] = value
        SharePrefHelper.putString(
            SIGN_CALENDAR_SCHEME_CACHE,
            JSON.toJSONString(calendarSchemeMap.map { it.value }.toList())
        )
        GlobalScope.launch { notifyCalendarSchemeEvent.emit(Unit) }
    }

    fun isMobileAutoSignLaunch(): Boolean {
        // 判断是否开启智能识别跳转 如果关闭则直接返回true
        val enableSmartRecognitionJump = SharePrefHelper.getBoolean(
            ENABLE_SMART_RECOGNITION_JUMP, false
        )
        if (!enableSmartRecognitionJump) {
            return true
        }
        val startTime = SharePrefHelper.getLong(SIGN_OPEN_INTENT_START_TIME, 0)
        if (System.currentTimeMillis() - startTime > 5000) {
            Log.i("CompleteProcessor", "不是由程序打开的，忽略")
            return false
        }
        return true
    }

    fun isEnableCurrentTimePeriod(requestCode: Int): Boolean {
        when (requestCode) {
            0 -> {
                // 早上上班打卡
                val isMorningStartOpen = SharePrefHelper.getBoolean(
                    IS_OPEN_MORNING_START_WORK_SIGN_TASK, false
                )
                Log.i("ContentValues", "早上上班打卡状态：$isMorningStartOpen")
                return isMorningStartOpen
            }

            1 -> {
                // 早上下班打卡
                val isMorningOffOpen = SharePrefHelper.getBoolean(IS_OPEN_MORNING_OFF_WORK_SIGN_TASK, false)
                Log.i("ContentValues", "早上下班打卡状态：$isMorningOffOpen")
                return isMorningOffOpen
            }

            2 -> {
                // 下午上班打卡
                val isAfternoonStartOpen = SharePrefHelper.getBoolean(
                    IS_OPEN_AFTERNOON_START_WORK_SIGN_TASK, false
                )
                Log.i("ContentValues", "下午上班打卡状态：$isAfternoonStartOpen")
                return isAfternoonStartOpen
            }

            3 -> {
                // 下午下班打卡
                val isAfternoonOffOpen = SharePrefHelper.getBoolean(
                    IS_OPEN_AFTERNOON_OFF_WORK_SIGN_TASK, false
                )
                Log.i("ContentValues", "下午下班打卡状态：$isAfternoonOffOpen")
                return isAfternoonOffOpen
            }

            else -> {
                return false
            }
        }
    }

}