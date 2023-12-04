package com.doviesfitness.utils

import android.util.Log
import android.text.TextUtils
class ConvertUnits {

    companion object {

        fun getLbsToKgs2(unit: Double): Double {
            val unitInKgs = unit * 0.45359237
            return unitInKgs
        }
        fun getLbsToKgs(unit: Int): Int {
            val unitInKgs = unit * 0.45
            return Math.round(unitInKgs).toInt()
        }

        fun getKgsToLbs2(unit: Double): String {
            val unitKgsToLbs = unit / 0.45359237
            return unitKgsToLbs.toString()
        }
        fun getKgsToLbs(unit: Int): Int {
            val unitKgsToLbs = unit / 0.45
            return Math.round(unitKgsToLbs).toInt()
        }

        fun getHeightFromFeetToCm(valueINComa: String): Int {
            val splitValue = valueINComa.split(" ")
            var feet = splitValue[0]
            var intchVallue = splitValue[1]
            if (feet!=null&&!feet.isEmpty()&&feet.contains("'"))
            {
                feet=  feet.replace("'","")
            }
            if (intchVallue!=null&&!intchVallue.isEmpty()&&intchVallue.contains("''"))
            {
                intchVallue= intchVallue.replace("''","")
            }

            val valueINCm = feet.toInt() * 30.48 + intchVallue.toInt()* 2.54

            Log.v("valueINCm", "" + valueINCm)
            return valueINCm.toInt()
        }
        fun centimeterToFeet(centimeter: String): String {
            var feetPart = 0
            var inchesPart = 0
            if (!TextUtils.isEmpty(centimeter)) {
                val dCentimeter = java.lang.Double.valueOf(centimeter)
                feetPart = Math.floor(dCentimeter / 2.54 / 12).toInt()
                println(dCentimeter / 2.54 - feetPart * 12)
                inchesPart = Math.ceil(dCentimeter / 2.54 - feetPart * 12).toInt()
            }
            return String.format("%d' %d''", feetPart, inchesPart)
        }
    }

}

