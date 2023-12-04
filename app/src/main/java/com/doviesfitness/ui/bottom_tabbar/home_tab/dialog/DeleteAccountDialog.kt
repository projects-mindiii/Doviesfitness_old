package com.doviesfitness.ui.bottom_tabbar.home_tab.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.ErrorDownloadViewTypeDialog
import com.doviesfitness.ui.base.BaseDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.utils.ProgressDialog
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.dialog_account_delete.*
import org.json.JSONObject
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogoutDialog.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DeleteAccountDialog : BaseDialog(), View.OnClickListener {
    val TAG = DeleteAccountDialog::class.java.name

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var blurLauout:RelativeLayout?=null

    lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_account_delete, container, false)
    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tv_logout.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_logout -> {

                    progressDialog = ProgressDialog(activity!!)
                    progressDialog.show()
                    dialog!!.dismiss()

                    deleteAccount(activity!!)


            }
            R.id.tv_cancel -> {
                mListener!!.isAcDelete()
                dialog?.dismiss()
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogoutDialog.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeleteAccountDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    private fun deleteAccount(activity: FragmentActivity) {
        if (getDataManager().getUserInfo().customer_auth_token.isNotEmpty()) {
            val param = HashMap<String, String>()
            param.put("", "")

            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            getDataManager().deleteAccountApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        progressDialog.dismiss()
                        Doviesfitness.preferences.edit().clear().apply()
                        Doviesfitness.getDataManager().logout(activity)

                        /*  val intent = Intent(mContext, InboxActivity::class.java)
                          startActivity(intent) */

                        /*        Log.d("account_delete", "onResponse: " + response.toString())
                                try {

                                    //   {"success":"1","message":"Deleted successfully"}
                                val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                                    val status = json?.get(StringConstant.success).toString()
                                    val msg = json?.get(StringConstant.message).toString()
                              Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
                                   *//* Doviesfitness.preferences.edit().clear().apply()
                            Doviesfitness.getDataManager().logout(activity!!)
*//*
                           if (status == "1") {
                                Doviesfitness.preferences.edit().clear().apply()
                                Doviesfitness.getDataManager().logout(activity!!)
                            }
                            else {
                               Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()

                            }
                        } catch (ex: Exception) {
                            progressDialog.dismiss()
//                          Toast.makeText(activity!!, "Something went wrong please try again later", Toast.LENGTH_SHORT).show()

                        }*/
                    }

                    override fun onError(anError: ANError) {
                        progressDialog.dismiss()
                        //Constant.errorHandle(anError, activity!!)
                        Toast.makeText(activity!!, anError.toString(), Toast.LENGTH_SHORT).show()

                    }


                })
        }
    }
    private var mListener: IsAcDelete? = null
    public fun setListener(listener: IsAcDelete) {
        this.mListener = listener;
    }
    interface IsAcDelete{
        public fun isAcDelete()
    }
}
