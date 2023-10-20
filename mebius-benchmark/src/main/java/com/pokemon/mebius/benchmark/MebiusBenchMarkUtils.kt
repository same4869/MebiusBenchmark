package com.pokemon.mebius.benchmark

import android.content.Context

/**
 * 功能：综合评定当前设备的等级
 *
 * 1、划分高端、中-高端、中端、低端、超低端
 * 2、首先查看是否能命中Json配置，未命中，根据CPU和内存的加权表现，进行评级
 * 3、调用时，使用BenchMarkUtils.getPhoneLevel，返回Int类型，1～5，分别是超低端～高端
 * 4、具体业务方可以根据返回值，进行定制操作。  比如判断设备是否是低端机
 * */
object MebiusBenchMarkUtils {
    const val LEVEL_HIGH = 5 // 高端
    const val LEVEL_HIGH_MIDDLE = 4 // 中-高端
    const val LEVEL_MIDDLE = 3 // 中端
    const val LEVEL_LOW = 2 // 低端
    const val LEVEL_LOW_VERY = 1 // 超低端

    const val LEVEL_UNKNOWN = 0
    var jsonLevel = LEVEL_UNKNOWN

    fun getPhoneLevel(context: Context): Int {
        if (jsonLevel == LEVEL_UNKNOWN) {
            // 是否能命中json列表
            jsonLevel = JsonBenchUtils().getLevelFromJson(context)
            if (jsonLevel == LEVEL_UNKNOWN) {
                // 未命中json，使用cpu+内存兜底
                jsonLevel = PhoneYearUtils().getPhoneLevel(context)
            }
        }
        return jsonLevel
    }
}