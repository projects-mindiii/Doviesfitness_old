package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.content.Intent
import android.graphics.Rect
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.databinding.FragmentWorkoutGrouplistBinding
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.CollectionGroupItemAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.popularStreamWorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.CollectionDetailModel
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.utils.CommanUtils
import java.lang.Exception


class StreamCollectionDetailFragment: BaseFragment(), View.OnClickListener, popularStreamWorkoutAdapter.OnItemCLick{


    private lateinit var binding: FragmentWorkoutGrouplistBinding
    private var workoutGroupList = ArrayList<CollectionDetailModel.Settings.Data.Workout>()
    var workout_group_id = ""
    lateinit var adapter: CollectionGroupItemAdapter
    lateinit var  collectionData: StreamDataModel.Settings.Data.Collection
    private var mLastClickTime: Long = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     //  activity!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     //   activity!!.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout_grouplist, container, false);
        initialization()
        return binding.root
    }

    private fun initialization() {
        binding.ivBack.setOnClickListener(this)
     //  activity!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // workout_group_id = arguments!!.getString("workout_group_id")
        collectionData = arguments!!.getSerializable("data") as StreamDataModel.Settings.Data.Collection
        Log.d("description...","description...."+collectionData.stream_workout_collection_description)
/*
        if (collectionData.stream_workout_collection_description!=null&&!collectionData.stream_workout_collection_description.isEmpty())
        binding.description.text=collectionData.stream_workout_collection_description
        else
            binding.description.visibility=View.GONE
*/
      //  binding.workoutName.text=collectionData.stream_workout_collection_title

        if (collectionData.stream_workout_collection_title!=null && !collectionData.stream_workout_collection_title.isEmpty()) {
            var name = CommanUtils.capitaliseName(collectionData.stream_workout_collection_title)
            binding.workoutName.text = "" + name
        }
        if (collectionData.stream_workout_collection_subtitle!=null
            && !collectionData.stream_workout_collection_subtitle.isEmpty()){
            var name=( CommanUtils.capitaliseName(collectionData.stream_workout_collection_subtitle))
            binding.levelName.text=""+name
        }

        if(collectionData.title_show_in_app.equals("Yes")){
            binding.workoutName.visibility=View.VISIBLE
        }else{
            binding.workoutName.visibility=View.INVISIBLE

        }
        if(collectionData.subtitle_show_in_app.equals("Yes"))
        {
            binding.levelName.visibility=View.VISIBLE

        }else{
            binding.levelName.visibility=View.INVISIBLE

        }

/*
        if (collectionData.stream_workout_collection_subtitle!=null
            && !collectionData.stream_workout_collection_subtitle.isEmpty()&& collectionData.collection_workout_count.toInt()>0 &&
            collectionData.collection_workout_count.toInt()==1) {
          //  binding.levelName.text = collectionData.stream_workout_collection_subtitle + " - " + collectionData.collection_workout_count + " Workout"

            var name=( CommanUtils.capitaliseName(collectionData.stream_workout_collection_subtitle)+ " - " +
                    collectionData.collection_workout_count + " Workout")

                    binding.levelName.text=""+name

        }
       else if (collectionData.stream_workout_collection_subtitle!=null
            && !collectionData.stream_workout_collection_subtitle.isEmpty()&&
            collectionData.collection_workout_count!=null&&collectionData.collection_workout_count.toInt()>1) {
          //  binding.levelName.text = collectionData.stream_workout_collection_subtitle + " - " + collectionData.collection_workout_count + " Workouts"

            var name=( CommanUtils.capitaliseName(collectionData.stream_workout_collection_subtitle)+ " - " +
                    collectionData.collection_workout_count + " Workouts")
            binding.levelName.text=""+name

        }
        else {
          //  binding.levelName.text = collectionData.stream_workout_collection_subtitle + " - " + "Coming soon"
           if (collectionData.stream_workout_collection_subtitle!=null
               && !collectionData.stream_workout_collection_subtitle.isEmpty()) {
               var name =
                   (CommanUtils.capitaliseName(collectionData.stream_workout_collection_subtitle) + " - " + "Coming Soon")
               binding.levelName.text = "" + name
           }

        }
*/
     //   binding.levelName.text=collectionData.stream_workout_collection_subtitle
        if (collectionData!=null&&collectionData.stream_workout_collection_image!=null)
        Glide.with(mContext).load(collectionData.stream_workout_collection_image_url+collectionData.stream_workout_collection_image)
           // .error(ContextCompat.getDrawable(mContext,R.drawable.app_icon))
           // .placeholder(ContextCompat.getDrawable(mContext,R.drawable.app_icon))
            .into(binding.workoutImg)
        else{}
/*
        var layoutManager= androidx.recyclerview.widget.LinearLayoutManager(mContext,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
*/
        var layoutManager= GridLayoutManager(context, 3);
        adapter= CollectionGroupItemAdapter(mContext,workoutGroupList,this)
       // val spacingInPixels = resources.getDimensionPixelSize(R.dimen._10sdp)
       // binding.workoutGroupRv.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        binding.workoutGroupRv.layoutManager=layoutManager
        binding.workoutGroupRv.adapter= adapter
        val spacing = Constant.deviceSize(activity!!) / 2
// apply spacing
        binding.workoutGroupRv.setPadding(spacing, spacing, spacing, resources.getDimensionPixelSize(R.dimen._55sdp))
        binding.workoutGroupRv.setClipToPadding(false)
        binding.workoutGroupRv.setClipChildren(false)
        binding.workoutGroupRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })



        binding.swipeRefresh.setOnRefreshListener {
            getWorkoutGrouplist()
        }

        val handler = Handler()
        handler.postDelayed(object:Runnable {
            public override fun run() {
                try {
                    getWorkoutGrouplist()
                    hideNavigationBar()
                }
                catch (Ex:Exception){
                    Ex.printStackTrace()
                }

            }
        }, 1000)
    }

    override fun onItemCLick(pos: Int, str: String, workoutID: String) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        val intent = Intent(mContext, StreamDetailActivity::class.java)
        var data=StreamDataModel.Settings.Data.RecentWorkout(workoutGroupList.get(pos).access_level,workoutGroupList.get(pos).display_new_tag,workoutGroupList.get(pos).display_new_tag_text,workoutGroupList.get(pos).media_title_name,
            workoutGroupList.get(pos).stream_workout_access_level, workoutGroupList.get(pos).stream_workout_id,workoutGroupList.get(pos).stream_workout_image,workoutGroupList.get(pos).stream_workout_image_url,
            workoutGroupList.get(pos).stream_workout_name,"",workoutGroupList.get(pos).stream_workout_subtitle,workoutGroupList.get(pos).subtitle_show_in_app,workoutGroupList.get(pos).title_show_in_app)
        intent.putExtra("data", data)
            intent.putExtra("from", str)
            startActivity(intent)
    }

    private fun getWorkoutGrouplist() {
        val param = HashMap<String, String>()
        param.put("collection_id", ""+collectionData.stream_workout_collection_id)
        var customerType=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
        if (customerType!=null&& !customerType!!.isEmpty()){
            param.put(StringConstant.auth_customer_subscription, customerType)
        }
        else{
            param.put(StringConstant.auth_customer_subscription, "free")
        }
        param.put("limit", "10")
        param.put("offset", "0")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_id
        )

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().getCollectionDetail(param,header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                workoutGroupList.clear()
                Log.d("TAG", "group response...."+ response!!.toString(4))
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                binding.swipeRefresh.isRefreshing=false

                if (success.equals("1")) {
                   // Constant.showCustomToast(mContext!!, "success..." + message)
                    val collectionDetail = getDataManager().mGson?.fromJson(response.toString(), CollectionDetailModel::class.java)
                    workoutGroupList.addAll(collectionDetail!!.settings.data.workout_list)
                  //  adapter.notifyDataSetChanged()
                    if (collectionDetail.settings.data.collection_detail.stream_workout_collection_description!=null &&
                       ! collectionDetail.settings.data.collection_detail.stream_workout_collection_description.isEmpty()) {
                        binding.description.text =
                            CommanUtils.capitaliseOnlyFirstLetter(collectionDetail.settings.data.collection_detail.stream_workout_collection_description)
                        binding.description.visibility=View.VISIBLE
                    }
                    else
                        binding.description.visibility=View.GONE
                    var layoutManager= GridLayoutManager(context, 3);
                    adapter= CollectionGroupItemAdapter(mContext,workoutGroupList,this@StreamCollectionDetailFragment )
                    binding.workoutGroupRv.layoutManager=layoutManager
                    binding.workoutGroupRv.adapter= adapter
                    binding.rltvLoader.visibility=View.GONE
                    binding.llparent.visibility=View.VISIBLE


                } else {
                    binding.rltvLoader.visibility=View.GONE
                    binding.llparent.visibility=View.VISIBLE

                    // Constant.showCustomToast(mContext!!, "" + message)
                }
            }
            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, activity!!)
                Constant.showCustomToast(mContext!!, getString(R.string.something_wrong))
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
        }
    }

    class SpacesItemDecoration(space: Int): androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int
        init {
            this.space=space
        }
        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.left = space
            }
            else if (position == parent.getAdapter()!!.getItemCount() - 1) {
                outRect.right = space
            }
            else{

            }
        }
    }


}