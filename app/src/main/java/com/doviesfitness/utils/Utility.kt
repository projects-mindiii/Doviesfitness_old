package com.doviesfitness.utils

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.doviesfitness.R
import com.doviesfitness.setting.recivers.MyNotificationPublisher
import java.io.IOException
import java.nio.charset.Charset
import android.os.Build
import android.text.Editable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.showTost
import java.util.*

class Utility {

    companion object {

        fun getScreenWidthDivideIntoThreeAndHlf(context: Context): Double {
            val screenWidth = context.resources.displayMetrics.widthPixels
            val partWidth = screenWidth / 3.8
            return partWidth
        }

        fun getScreenWidthDivideIntoThree(context: Context): Double {
            val screenWidth = context.resources.displayMetrics.widthPixels
            val partWidth = screenWidth / 3.0
            return partWidth
        }

        fun getScreenWidthDivideIntoTwo(context: Context): Int {
            val screenWidth = context.resources.displayMetrics.widthPixels
            val partWidth = screenWidth / 2
            return partWidth
        }

        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)


        fun showAddNoteDialog( context: Context, note:String, listner: (String) -> Unit) {
           var dialog = Dialog(context, R.style.MyTheme_Transparent)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
                override fun onDismiss(dialog: DialogInterface?) {
                    //binding.viewTransParancy.visibility = View.GONE
                }
            })
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_add_note_ui);
            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

            val iv_cancle_dialog = dialog.findViewById(R.id.iv_cancle_dialog) as ImageView
            val et_add_note = dialog.findViewById(R.id.et_add_note) as EditText
            val tv_save_note = dialog.findViewById(R.id.tv_save_note) as TextView

              if (note.isNotEmpty()){
                  et_add_note.text=note.toEditable()
              }else{
                  et_add_note.hint="Add note for exercise"
              }

            dialog.show();
            dialog.setCanceledOnTouchOutside(true)
               iv_cancle_dialog.setOnClickListener {
                dialog.dismiss()
            }

            tv_save_note.setOnClickListener {
                if (et_add_note.text.toString().trim().isNotEmpty()){
                    listner.invoke(et_add_note.text.toString().trim())
                    dialog.dismiss()
                }else
                    context.showTost("Please enter note for exercise.")
            }
        }



        fun convertPixelToSdpAndSetToLinerLayout(context: Context, pixel: Int, levelLayout: LinearLayout)/*: Int*/ {
            val density = context.resources.displayMetrics.density
            //return (pixel * density).toInt()
            val params: LinearLayout.LayoutParams =  levelLayout.layoutParams as LinearLayout.LayoutParams
            params.height = (pixel * density).toInt()
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            levelLayout.layoutParams = params
        }
        fun convertPixelToSdpAndSetToTextView(context: Context, pixel: Int, textView: TextView)/*: Int*/ {
            val density = context.resources.displayMetrics.density
            //return (pixel * density).toInt()
            textView.textSize=(pixel * density)

        }
        fun convertPixelToSdp(context: Context, pixel: Int): Int {
            val density = context.resources.displayMetrics.density
             return (pixel * density).toInt()
           // textView.textSize=(pixel * density)

        }

      /*  fun getScreenWidthDivideIntoThreeAndHlf(context: Context): Double {
            val screenWidth = context.resources.displayMetrics.widthPixels
            val partWidth = screenWidth / 3.8
            return partWidth
        }*/
      /*  fun getScreenWidthDivideIntoTwo(context: Context): Int {
            val screenWidth = context.resources.displayMetrics.widthPixels
            val partWidth = screenWidth / 2
            return partWidth
        }*/


        fun loadJSONFromAsset(context: Context, filename: String): String {
            val json: String
            try {
                val ips = context.assets.open(filename)
                val size = ips.available()
                val buffer = ByteArray(size)
                val tBytes = ips.read(buffer)
                ips.close()
                json = String(buffer, Charset.forName("UTF-8"))
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

            return json
        }

         fun scheduleNotification(context: Context, hour: String, min: String) {

            val currentCal = Calendar.getInstance()
            val firingCal = Calendar.getInstance()
            firingCal.setTimeInMillis(System.currentTimeMillis())
            firingCal.set(Calendar.HOUR_OF_DAY, hour.toInt())
            firingCal.set(Calendar.MINUTE, min.toInt())
            firingCal.set(Calendar.SECOND, 0)
            var intendedTime = firingCal.timeInMillis
            val currentTime = currentCal.timeInMillis

            if (intendedTime >= currentTime) {
            }
            else {
                firingCal.add(Calendar.DAY_OF_MONTH, 1)
                intendedTime = firingCal.timeInMillis

            }

            val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
            notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarmManager != null) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    intendedTime,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        intendedTime,
                        pendingIntent
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    var alarmClockInfo =
                        AlarmManager.AlarmClockInfo(intendedTime, pendingIntent)
                    alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        intendedTime,
                        pendingIntent
                    )
                } else
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        intendedTime,
                        pendingIntent
                    )
            }

        }


    var countryFlags = intArrayOf(

        R.drawable.af,
        R.drawable.al,
        R.drawable.dz,
        R.drawable.`as`,
        R.drawable.ad,
        R.drawable.ao,
        R.drawable.ai,
        R.drawable.ax,
        R.drawable.aq,
        R.drawable.ag,
        R.drawable.ar,
        R.drawable.am,

        R.drawable.aw,
        R.drawable.au,
        R.drawable.at,
        R.drawable.az,
        R.drawable.bs,
        R.drawable.bh,
        R.drawable.bd,
        R.drawable.bb,
        R.drawable.by,
        R.drawable.be,

        R.drawable.bz,
        R.drawable.bj,
        R.drawable.bm,
        R.drawable.bt,
        R.drawable.bo,
        R.drawable.bn,
        R.drawable.ba,
        R.drawable.bw,
        R.drawable.br,
        R.drawable.io,
        R.drawable.bg,
        R.drawable.bf,

        R.drawable.bi,
        R.drawable.kh,
        R.drawable.cm,
        R.drawable.ca,
        R.drawable.cv,
        R.drawable.ky,
        R.drawable.cf,
        R.drawable.td,
        R.drawable.cl,
        R.drawable.cn,

        R.drawable.cx,
        R.drawable.cc,
        R.drawable.cd,
        R.drawable.ci,
        R.drawable.co,
        R.drawable.km,
        R.drawable.cg,
        R.drawable.ck,
        R.drawable.cr,
        R.drawable.hr,
        R.drawable.cu,
        R.drawable.cy,
        R.drawable.cz,

        R.drawable.dk,
        R.drawable.dj,
        R.drawable.dm,
//..........do..........
        R.drawable.doo,
        R.drawable.ec,
        R.drawable.eg,
        R.drawable.sv,
        R.drawable.gq,
        R.drawable.er,
        R.drawable.ee,

        R.drawable.et,
        R.drawable.fk,
        R.drawable.fo,
        R.drawable.fj,
        R.drawable.fi,
        R.drawable.fr,
        R.drawable.gf,
        R.drawable.pf,
        R.drawable.ga,
        R.drawable.gm,
        R.drawable.ge,


        R.drawable.de,
        R.drawable.gh,
        R.drawable.gi,
        R.drawable.gr,
        R.drawable.gl,
        R.drawable.gd,
        R.drawable.gp,
        R.drawable.gu,
        R.drawable.gt,
        R.drawable.gn,

        R.drawable.gw,
        R.drawable.gg,
        R.drawable.gy,
        R.drawable.ht,
        R.drawable.hn,
        R.drawable.hk,
        R.drawable.hu,
        R.drawable.`in`,
        R.drawable.`is`,

        R.drawable.id,
        R.drawable.iq,
        R.drawable.ie,

        R.drawable.il,
        R.drawable.it,
        R.drawable.jm,
        R.drawable.jp,
        R.drawable.je,
        R.drawable.jo,
        R.drawable.kz,
        R.drawable.ke,
        R.drawable.ki,
        R.drawable.kp,
        R.drawable.kr,
        R.drawable.kw,
        R.drawable.kg,

        R.drawable.lv,
        R.drawable.lb,
        R.drawable.ls,
        R.drawable.lr,
        R.drawable.ly,
        R.drawable.li,
        R.drawable.lt,
        R.drawable.lu,
        R.drawable.mo,
        R.drawable.mg,
        R.drawable.mw,
        R.drawable.my,
        R.drawable.mv,


        R.drawable.ml,
        R.drawable.mt,
        R.drawable.mh,
        R.drawable.mq,
        R.drawable.mr,
        R.drawable.mu,
        R.drawable.yt,
        R.drawable.mx,
        R.drawable.mc,
        R.drawable.mn,

        R.drawable.me,
        R.drawable.ms,
        R.drawable.ma,
        R.drawable.mz,
        R.drawable.mm,
        R.drawable.na,
        R.drawable.nr,
        R.drawable.np,
        R.drawable.nl,
        R.drawable.an,
        R.drawable.nc,
        R.drawable.nz,

        R.drawable.ni,
        R.drawable.ne,
        R.drawable.ng,
        R.drawable.nu,
        R.drawable.nf,
        R.drawable.mp,
        R.drawable.no,
        R.drawable.om,
        R.drawable.pk,
        R.drawable.pw,

        R.drawable.pa,
        R.drawable.pg,
        R.drawable.py,
        R.drawable.ps,
        R.drawable.pn,
        R.drawable.pe,
        R.drawable.ph,
        R.drawable.pl,
        R.drawable.pt,
        R.drawable.pr,
        R.drawable.qa,
        R.drawable.re,
        R.drawable.ro,
        R.drawable.ru,

        R.drawable.rw,
        R.drawable.sh,
        R.drawable.kn,
        R.drawable.lc,
        R.drawable.mf,
        R.drawable.pm,
        R.drawable.vc,
        R.drawable.st,
        R.drawable.so,
        R.drawable.sj,
        R.drawable.sy,
        R.drawable.ws,
        R.drawable.sm,
        R.drawable.sa,
        R.drawable.sn,
        R.drawable.rs,
        R.drawable.sc,
        R.drawable.sl,
        R.drawable.sg,
        R.drawable.sk,


        R.drawable.si,
        R.drawable.sb,
        R.drawable.za,
        R.drawable.gs,
        R.drawable.es,
        R.drawable.lk,
        R.drawable.sd,
        R.drawable.sr,
        R.drawable.sz,
        R.drawable.se,


        R.drawable.ch,
        R.drawable.tj,
        R.drawable.tw,
        R.drawable.tz,
        R.drawable.tl,
        R.drawable.th,
        R.drawable.tg,
        R.drawable.tk,
        R.drawable.to,
        R.drawable.tt,
        R.drawable.tn,
        R.drawable.tr,
        R.drawable.tm,


        R.drawable.tc,
        R.drawable.tv,
        R.drawable.ug,
        R.drawable.ua,
        R.drawable.ae,
        R.drawable.gb,
        R.drawable.us,
        R.drawable.uy,
        R.drawable.uz,
        R.drawable.vu,
        R.drawable.ve,
        R.drawable.vn,
        R.drawable.vg,
        R.drawable.vi,


        R.drawable.wf,
        R.drawable.ye,
        R.drawable.zm,
        R.drawable.zw
    )
}

}