package com.doviesfitness.data.local

import android.app.Activity
import com.doviesfitness.data.model.UserInfoBean
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import java.util.ArrayList
import java.util.HashMap

interface PreferencesHelper {
    fun setInfo(info: String)
    fun isLoggedIn(): Boolean?

    fun getUserInfo(): UserInfoBean

    fun setUserInfo(userInfo: UserInfoBean)

    fun setRememberMe(email: String, pwd: String)

    fun getRememberMe(): Array<String?>

    fun getHeader(): HashMap<String, String>

    fun statusUpdateOfWorkout(): Boolean

    fun setPreferanceStatus(Key: String, info: String)

    fun getPreferanceStatus(Key: String): String?

    fun logout(activity: Activity)
    fun saveFilterExerciseList(exerciseList: List<FilterExerciseResponse.Data>)
    fun getFilterExerciseList(): ArrayList<FilterExerciseResponse.Data>?
    fun getStringData(Key: String): String?
    fun setStringData(Key: String, info: String)
    fun getUserStringData(Key: String): String?
    fun setUserStringData(Key: String, info: String)
}