package com.doviesfitness.ui.profile.favourite.fragment

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.base.EndlessRecyclerViewScrollListener
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.CommentsActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.FeedDetailsActivity
import com.doviesfitness.ui.profile.favourite.adapter.FavFeedAdapter
import com.doviesfitness.ui.profile.favourite.modal.FavFeedDataModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject
import java.lang.Exception

class FeedsFragment : BaseFragment(), FavFeedAdapter.FavFeedListOnClick, View.OnClickListener {

    lateinit var binding: com.doviesfitness.databinding.FavFragmentFeedsBinding
    lateinit private var feedAdapter: FavFeedAdapter
    //var feedList = MutableList<FavFeedDataModal.Data>()
     var feedList= mutableListOf<FavFeedDataModal.Data>()
    private var page1: Int = 1
    private lateinit var homeTabActivity: HomeTabActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fav_fragment_feeds, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        homeTabActivity = activity as HomeTabActivity
        feedAdapter = FavFeedAdapter(context!!, feedList, this)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.myFavFeedRv.layoutManager = layoutManager1
        binding.myFavFeedRv.adapter = feedAdapter


        ///********Pagination Feed List*********////
        binding.myFavFeedRv.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager1) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView) {
                if (page != 0) {
                    feedAdapter.showLoading(true)
                    page1 = page
                    getfavFeedApi()
                }
            }
        })

        setOnClick(binding.btnAddFeeds)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getfavFeedApi()
        }
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_add_feeds -> {
                homeTabActivity.onResume()
                homeTabActivity.home_ll.callOnClick()
            }
        }
    }

    private fun getfavFeedApi() {

        try {
            if (CommanUtils.isNetworkAvailable(mContext)) {

                val param = HashMap<String, String>()
                param.put(StringConstant.device_token, "")
                param.put(StringConstant.device_id, "")
                param.put(StringConstant.device_type, StringConstant.Android)
                param.put(StringConstant.module_type, "Newsfeed")
                param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
                val header = HashMap<String, String>()
                header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
                header.put(StringConstant.apiKey, "HBDEV")
                header.put(StringConstant.apiVersion, "1")

                getDataManager().getCustomerFavourites(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        if (success.equals("1")) {
                            feedList.clear()
                            //binding.swipeRefresh.setRefreshing(false)
                            binding.noFeedFound.visibility = View.GONE
                            val favFeedDataModal =
                                getDataManager().mGson?.fromJson(response.toString(), FavFeedDataModal::class.java)
                            feedList.addAll(favFeedDataModal!!.data);
                            hideFooterLoiader()
                        }
                        if (feedList.isEmpty()) {
                            //binding.swipeRefresh.setRefreshing(false)
                            binding.noFeedFound.visibility = View.VISIBLE
                        }
                        feedAdapter.notifyDataSetChanged()
                    }

                    override fun onError(anError: ANError) {
                        hideFooterLoiader()
                        //binding.swipeRefresh.setRefreshing(false)
                        Constant.showCustomToast(context!!, getString(R.string.something_wrong))
                        //binding.noFeedFound.visibility = View.VISIBLE
                    }
                })
            }else {
                Constant.showInternetConnectionDialog(mContext as Activity)
            }

        }
        catch (Ex:Exception)
        {
            Ex.printStackTrace()
        }

    }

    private fun hideFooterLoiader() {
        feedAdapter.showLoading(false)
        feedAdapter.notifyDataSetChanged()
    }

    override fun tvCommentsOnclick(featuredData: FavFeedDataModal.Data, pos: Int) {
        val intent = Intent(mContext, CommentsActivity::class.java)
        intent.putExtra("newsId", featuredData.news_id)
        intent.putExtra("position", pos)
        startActivityForResult(intent, Constant.COMMENT_COUNT_CONSTANT)
    }

    override fun ivLikeOnclick(featuredData: FavFeedDataModal.Data, pos: Int, ivHeart: ImageView) {
       // ivHeart.isEnabled = false
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.module_name, StringConstant.newsFeed)
        param.put(StringConstant.module_id, featuredData.news_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().likeUnlikeApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {




                   /* if (feedList.get(pos).news_like_status.equals("0")) {

                      *//*  var dataObj=feedList.get(pos)

                        var temp= FavFeedDataModal.Data("1","gkjk","","","","","","",""
                            ,"","","2","","","","","","","" ,
                            "","")

*//*
                        feedList.get(pos).news_like_status = "1"
                        val likeCount = feedList.get(pos).customer_likes.toInt() + 1
                        feedList.get(pos).customer_likes = likeCount.toString()
                      //  ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_active))
                        feedAdapter.notifyDataSetChanged()
                    } else {
                        feedList.get(pos).news_like_status = "0"
                        val likeCount = feedList.get(pos).customer_likes.toInt() - 1
                        feedList.get(pos).customer_likes = likeCount.toString()
                      //  ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite))

                        feedAdapter.notifyDataSetChanged()
                    }*/
                    getfavFeedApi()
                  //  ivHeart.isEnabled = true

                } else Constant.showCustomToast(context!!, "" + message)
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, activity!!)
                Constant.showCustomToast(context!!, getString(R.string.something_wrong))
            }
        })
    }

    override fun ivShareOnclick(featuredData: FavFeedDataModal.Data, pos: Int) {
        mActivity?.sharePost(featuredData.news_share_url)
    }

    override fun ivFavOnclick(featuredData: FavFeedDataModal.Data, pos: Int, view: ImageView) {

        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.module_name, StringConstant.newsFeed)
        param.put(StringConstant.module_id, featuredData.news_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    if (featuredData.news_fav_status.equals("0")) {
                        featuredData.news_fav_status = "1"
                        view.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_active))
                    } else {
                        featuredData.news_fav_status = "0"
                        view.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star))
                    }

                    feedList.removeAt(pos)
                    feedAdapter.notifyDataSetChanged()

                    getfavFeedApi()
                } else Constant.showCustomToast(context!!, "" + message)
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, activity!!)
                Constant.showCustomToast(context!!, getString(R.string.something_wrong))
            }
        })
    }

    override fun tvDescriptioOnclick(featuredData: FavFeedDataModal.Data, pos: Int) {
        val intent = Intent(mContext, FeedDetailsActivity::class.java)
        intent.putExtra("FeaturedDataFromFav", featuredData)
        intent.putExtra("Position", pos)
        startActivityForResult(intent, Constant.LIKE_COMMENT_COUNT_CONSTANT)
    }

}
