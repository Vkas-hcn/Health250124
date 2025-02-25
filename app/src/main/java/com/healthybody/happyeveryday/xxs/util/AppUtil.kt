package com.healthybody.happyeveryday.xxs.util

import com.healthybody.happyeveryday.xxs.R

object AppUtil {


    val pressureColorArr = arrayOf("#0180F8","#3AC34A","#DBB60A","#EE9102","#F57602","#FE5001")
    val pressureColorArr2 = arrayOf("#FFD7ECFF","#FFBFFFC7","#FFFFE0C3","#FFFFDFAE","#FFFFDBBB","#FFFFD3D3")
    val pressureNameArr = arrayOf("Hypotension","Normal","Elevated","Hypertension Stage 1","Hypertension Stage 2","Hypertension Crisis")
    val pressureDescArr = arrayOf("sys <90 or dia <60","sys 90-119 and dis 60-79","sys 120-129 and dis 60-79","sys 130-139 or dis 80-89","sys 140-180 or dis 90-120","sys >180 or dia >120")

    fun pressureLevel(sys: Int, dia: Int): Int {
        if ((sys >= 90 && sys <= 119) && (dia >= 60 && dia < 79)) {
            return 1
        } else if ((sys >= 120 && sys <= 129) && (dia >= 60 && dia <= 79)) {
            return 2
        } else if ((sys > 180 || dia > 120)) {
            return 5
        }
        if ((sys >= 140) || (dia >= 90)) {
            return 4
        } else if ((sys >= 130) || (dia >= 80)) {
            return 3
        }
//        else{
//            return 0
//        }
        else if ((sys < 90 || dia < 60)) {
            return 0
        }
        return 0
    }

    val sugarColorArr = arrayOf("#FF0180F8","#FF3AC34A","#FFDBB60A","#FFFE5001")
    val sugarColorArr2 = arrayOf("#FFD7ECFF","#FFBFFFC7","#FFFFE0C3","#FFFFD3D3")
    val sugarNameArr = arrayOf("Low","Normal","Pre-Diabetes","Diabetes")

    private val unit00Desc = arrayOf("<72","72-99","100-126",">126")
    private val unit05Desc = arrayOf("<72","72-140","141-153",">153")
    private val unit06Desc = arrayOf("<72","72-85","86-126",">126")

    private val unit10Desc = arrayOf("<4.0","4.0-5.5","5.6-7.0",">7.0")
    private val unit15Desc = arrayOf("<4.0","4.0-7.7","7.8-8.5",">8.5")
    private val unit16Desc = arrayOf("<4.0","4.0-4.7","4.8-7.0",">7.0")


    fun sugarDesc(unit:Int,state:Int,lv:Int): String {
        if (unit!=0){
            return unit1Desc(state,lv)
        }else{
            return unit0Desc(state,lv)
        }
    }

    private fun unit0Desc(state:Int,lv:Int): String {
        if(state==5){
            return unit05Desc.get(lv)
        }else if(state==6){
            return unit06Desc.get(lv)
        }
        return unit00Desc.get(lv)
    }
    private  fun unit1Desc(state:Int,lv:Int): String {
        if(state==5){
            return unit15Desc.get(lv)
        }else if(state==6){
            return unit16Desc.get(lv)
        }
        return unit10Desc.get(lv)
    }


    fun sugarLevel(unit:Int,state:Int,value:Double): Int {
        if (unit!=0){
            return unit1(state,value)
        }else{
            return unit0(state,value)
        }
    }

    private fun unit0(state:Int,value:Double): Int {
        if (value<72.0){
            return 0
        }

        if(state==5){
            if ((value <= 140.0)) {
                return 1;
            }else if ((value > 140.0 && value <= 153.0)) {
                return 2;
            } else if ((value > 153.0)) {
                return 3;
            }
        }else if(state==6){
            if ((value <= 85.0)) {
                return 1;
            }else if ((value > 85.0 && value <= 126.0)) {
                return 2;
            }else if ((value > 126.0)) {
                return 3;
            }
        }

        if (value <=99.0){
            return 1
        }else if ((value > 99.0 && value <= 126.0)) {
            return 2
        }else  if ((value > 126.0)) {
            return 3
        }
        return 3;
    }
    private fun unit1(state:Int,value:Double): Int {
        if (value<4.0){
            return 0
        }

        if(state==5){
            if (( value <= 7.77)) {
                return 1;
            }else if ((value >7.77 && value <= 8.5)) {
                return 2;
            } else if ((value > 8.5)) {
                return 3;
            }
        }else if(state==6){
            if ((value <= 4.72)) {
                return 1;
            }else if ((value >4.72 && value <= 7.0)) {
                return 2;
            }else if ((value > 7.0)) {
                return 3;
            }
        }

        if (value <=5.5){
            return 1
        }else if ((value >5.5 && value <= 7.0)) {
            return 2;
        }else  if ((value > 7.0)) {
            return 3;
        }
        return 3;
    }


    val sugarStateImgArr = arrayOf(R.mipmap.i_opt0,R.mipmap.i_opt1,R.mipmap.i_opt2,R.mipmap.i_opt3,R.mipmap.i_opt4,R.mipmap.i_opt5,R.mipmap.i_opt6,R.mipmap.i_opt7)
    val sugarStateTextArr = arrayOf("Normal","Fasting","Before exercise","After exercise","Before a meal","After a meal(1h)","After a meal(2h)","Sleep")

}