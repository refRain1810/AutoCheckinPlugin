package cn.martinkay.autocheckinplugin

import android.content.Context
import android.content.SharedPreferences

const val IS_OPEN_MORNING_START_WORK_SIGN_TASK = "is_open_morning_start_work_sign_task"
const val IS_OPEN_MORNING_OFF_WORK_SIGN_TASK = "is_open_morning_off_work_sign_task"

const val IS_OPEN_AFTERNOON_START_WORK_SIGN_TASK = "is_open_afternoon_start_work_sign_task"
const val IS_OPEN_AFTERNOON_OFF_WORK_SIGN_TASK = "is_open_afternoon_off_work_sign_task"


const val IS_OPEN_SATURDAY_SIGN_TASK = "is_open_saturday_sign_task"
const val IS_OPEN_SUNDAY_SIGN_TASK = "is_open_sunday_sign_task"

const val SIGN_TASK_MORNING_START_WORK_START_TIME = "sign_task_morning_start_work_start_time"
const val SIGN_TASK_MORNING_START_WORK_STOP_TIME = "sign_task_morning_start_work_stop_time"

const val SIGN_TASK_MORNING_OFF_WORK_START_TIME = "sign_task_morning_off_work_start_time"
const val SIGN_TASK_MORNING_OFF_WORK_STOP_TIME = "sign_task_morning_off_work_stop_time"

const val SIGN_TASK_AFTERNOON_START_WORK_START_TIME = "sign_task_start_work_start_time"
const val SIGN_TASK_AFTERNOON_START_WORK_STOP_TIME = "sign_task_start_work_stop_time"

const val SIGN_TASK_AFTERNOON_OFF_WORK_START_TIME = "sign_task_stop_work_start_time"
const val SIGN_TASK_AFTERNOON_OFF_WORK_STOP_TIME = "sign_task_stop_work_stop_time"

const val IS_FINISH_MORNING_START_WORK_SIGN_TASK = "is_finish_morning_start_work_sign_task"
const val IS_FINISH_MORNING_OFF_WORK_SIGN_TASK = "is_finish_morning_off_work_sign_task"
const val IS_FINISH_AFTERNOON_START_WORK_SIGN_TASK = "is_finish_afternoon_start_work_sign_task"
const val IS_FINISH_AFTERNOON_OFF_WORK_SIGN_TASK = "is_finish_afternoon_off_work_sign_task"

object SharePrefHelper {
    private var mShare: SharedPreferences? = null

    init {
        getSharePref()
    }

    private fun initSharePref(ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)
    }

    fun getSharePref(): SharedPreferences {
        if (null == mShare) {
            mShare = initSharePref(SignApplication.getApp())
        }
        return mShare!!
    }

    fun putString(key: String, value: String?) {
        val editor = getSharePref().edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, default: String?): String {
        return getSharePref().getString(key, default).toString()
    }

    fun putBoolean(key: String, value: Boolean = false) {
        val editor = getSharePref().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        return getSharePref().getBoolean(key, default)
    }
}

// 获取 早上上班的开始时间范围
fun getMorningStartWorkStartTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_MORNING_START_WORK_START_TIME, "8:50")
}

// 获取 早上上班的结束时间范围
fun getMorningStartWorkStopTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_MORNING_START_WORK_STOP_TIME, "9:04")
}


// 获取 早上下班的开始时间范围
fun getMorningOffWorkStartTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_MORNING_OFF_WORK_START_TIME, "12:00")
}

// 获取 早上下班的结束时间范围
fun getMorningOffWorkStopTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_MORNING_OFF_WORK_STOP_TIME, "12:10")
}

/**
 * 获取 下午下班的开始时间范围
 */
fun getAfternoonStartWorkStartTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_AFTERNOON_START_WORK_START_TIME, "12:50")
}

/**
 * 获取 下午下班的结束时间范围
 */
fun getAfternoonStartWorkStopTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_AFTERNOON_START_WORK_STOP_TIME, "13:25")
}


/**
 * 获取 下午下班的开始时间范围
 */
fun getAfternoonOffWorkStartTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_AFTERNOON_OFF_WORK_START_TIME, "18:00")
}

/**
 * 获取 下午下班的结束时间范围
 */
fun getAfternoonOffWorkStopTimeStr(): String {
    return SharePrefHelper.getString(SIGN_TASK_AFTERNOON_OFF_WORK_STOP_TIME, "18:10")
}