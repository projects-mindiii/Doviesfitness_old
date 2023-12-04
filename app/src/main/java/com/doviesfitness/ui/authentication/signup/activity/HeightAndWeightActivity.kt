package com.doviesfitness.ui.authentication.signup.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import com.doviesfitness.R
import com.doviesfitness.data.model.SignUpInfo
import com.doviesfitness.ui.authentication.signup.dialog.HeightAndWeightDialog
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.ConvertUnits
import kotlinx.android.synthetic.main.activity_height_and_weight.*
import kotlinx.android.synthetic.main.activity_height_and_weight.iv_back

class HeightAndWeightActivity : BaseActivity(), View.OnClickListener,
    HeightAndWeightDialog.HeightWeightCallBack {
    private var inch: String = ""
    private var feet: String = ""
    private var cm: String = ""
    private var kg: String = ""
    private var lbs: String = ""

    lateinit var signUpInfo: SignUpInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()

        setContentView(R.layout.activity_height_and_weight)
        inItView()
    }

    fun inItView() {
        signUpInfo = intent.getSerializableExtra("SignUpInfo") as SignUpInfo

        ft_btn.setOnClickListener(this)
        cm_btn.setOnClickListener(this)
        kg_btn.setOnClickListener(this)
        lbs_btn.setOnClickListener(this)
        ll_feet.setOnClickListener(this)
        ll_inch.setOnClickListener(this)
        ll_cm.setOnClickListener(this)
        ll_lbs.setOnClickListener(this)
        ll_kg.setOnClickListener(this)
        iv_next_btn.setOnClickListener(this)
        tv_skip.setOnClickListener(this)
        iv_back.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        tv_skip.isEnabled = true
        iv_next_btn.isEnabled = true

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.ft_btn -> {
                tv_cm.text = "0"
                ll_cm.setVisibility(View.GONE)
                ll_ft.setVisibility(View.VISIBLE)
                ft_btn.background = ContextCompat.getDrawable(this, R.drawable.rectangle_back_white)
                cm_btn.background = ContextCompat.getDrawable(this, R.drawable.rigth_trans_corner)
                ft_btn.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                cm_btn.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            }
            R.id.cm_btn -> {
                tv_feet.text = "0"
                tv_inch.text = "0"
                ll_cm.setVisibility(View.VISIBLE)
                ll_ft.setVisibility(View.GONE)
                ft_btn.background = ContextCompat.getDrawable(this, R.drawable.left_trans_corner)
                cm_btn.background =
                    ContextCompat.getDrawable(this, R.drawable.right_rectangle_back_white)
                ft_btn.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                cm_btn.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            }
            R.id.lbs_btn -> {
                tv_kg.text = "0"
                ll_kg.setVisibility(View.GONE)
                ll_lbs.setVisibility(View.VISIBLE)
                lbs_btn.background =
                    ContextCompat.getDrawable(this, R.drawable.rectangle_back_white)
                kg_btn.background = ContextCompat.getDrawable(this, R.drawable.rigth_trans_corner)
                lbs_btn.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                kg_btn.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            }
            R.id.kg_btn -> {
                tv_lbs.text = "0"
                ll_kg.visibility = View.VISIBLE
                ll_lbs.visibility = View.GONE
                lbs_btn.background = ContextCompat.getDrawable(this, R.drawable.left_trans_corner)
                kg_btn.background =
                    ContextCompat.getDrawable(this, R.drawable.right_rectangle_back_white)
                lbs_btn.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                kg_btn.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            }
            R.id.ll_feet -> {
                val openDialog =
                    HeightAndWeightDialog.newInstance(3, 7, "HeightInFeet", this)
                        .show(supportFragmentManager)

            }
            R.id.ll_inch -> {
                val openDialog =
                    HeightAndWeightDialog.newInstance(1, 11, "HeightInInch", this)
                        .show(supportFragmentManager)

            }
            R.id.ll_cm -> {
                val openDialog =
                    HeightAndWeightDialog.newInstance(91, 242, "HeightInCm", this)
                        .show(supportFragmentManager)

            }
            R.id.ll_lbs -> {
                val openDialog =
                    HeightAndWeightDialog.newInstance(30, 500, "WeightInLbs", this)
                        .show(supportFragmentManager)

            }
            R.id.ll_kg -> {
                val openDialog =
                    HeightAndWeightDialog.newInstance(13, 227, "WeightInKg", this)
                        .show(supportFragmentManager)
            }
            R.id.iv_next_btn -> {
                iv_next_btn.isEnabled = false

                //weight related work
                if (!lbs.trim().isEmpty() || !lbs.trim().equals("")) {
                    //kg and lbs
                    val inKgsFromLbs = ConvertUnits.getLbsToKgs(lbs.toInt())
                    signUpInfo.setWeight(inKgsFromLbs.toString())
                } else {
                    signUpInfo.setWeight(kg.trim())
                }

                // height related work
                if (!feet.trim().isEmpty() || !feet.trim().equals("") && inch.trim().isEmpty() || !inch.trim().equals("")) {
                    //convert feet and inch to cm
                    var setHeight=""
                    if (!feet.isEmpty()&&!inch.isEmpty())
                     setHeight = ConvertUnits.getHeightFromFeetToCm(feet + " " + inch).toString()
                   else if (!feet.isEmpty()&& inch.isEmpty())
                     setHeight = ConvertUnits.getHeightFromFeetToCm(feet + " " + 0).toString()
                   else if (feet.isEmpty()&&!inch.isEmpty())
                     setHeight = ConvertUnits.getHeightFromFeetToCm("0" + " " + inch).toString()
                    else
                        setHeight=""
                    signUpInfo.setHeight(setHeight)
                } else {
                    signUpInfo.setHeight(cm)
                }

                val intent = Intent(this, CreateUserActivity::class.java)
                intent.putExtra("SignUpInfo", signUpInfo)
                startActivity(intent)
              //  finish()
            }
            R.id.tv_skip -> {
                tv_skip.isEnabled = false
                signUpInfo.setHeight("0")
                signUpInfo.setWeight("0")
                intent = Intent(this, CreateUserActivity::class.java)
                intent.putExtra("SignUpInfo", signUpInfo)
                startActivity(intent)
              //  finish()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    /*Callback value listener
    * @param: value (number picker selected value
    * @param: type (Type of value)
    * */
    override fun valueOnClick(value: Int, type: String) {
        if (type.equals("HeightInFeet")) {
            tv_feet.text = "" + value
            feet = "" + value
            cm = ""
        } else if (type.equals("HeightInInch")) {
            tv_inch.text = "" + value
            inch = "" + value
            cm = ""
        } else if (type.equals("HeightInCm")) {
            tv_cm.text = "" + value
            cm = "" + value
            feet = ""
            inch = ""
        } else if (type.equals("WeightInLbs")) {
            tv_lbs.text = "" + value
            lbs = "" + value
            kg = ""
        } else if (type.equals("WeightInKg")) {
            tv_kg.text = "" + value
            kg = "" + value
            lbs = ""
        } else {

        }

        hideNavigationBar()
    }



}
