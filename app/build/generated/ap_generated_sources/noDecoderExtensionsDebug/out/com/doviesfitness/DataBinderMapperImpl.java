package com.doviesfitness;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.doviesfitness.databinding.ActivityAddExerciseBindingImpl;
import com.doviesfitness.databinding.ActivityAddNotesNewBindingImpl;
import com.doviesfitness.databinding.ActivityAddToWorkOutPlanBindingImpl;
import com.doviesfitness.databinding.ActivityChangePasswordActvityBindingImpl;
import com.doviesfitness.databinding.ActivityContactUsBindingImpl;
import com.doviesfitness.databinding.ActivityCreateDietDetailActvityBindingImpl;
import com.doviesfitness.databinding.ActivityCreateDietPcategoryDetailActvityBindingImpl;
import com.doviesfitness.databinding.ActivityCreateDietPlanWebViewBindingImpl;
import com.doviesfitness.databinding.ActivityCreateWorkOutPlanActivtyBindingImpl;
import com.doviesfitness.databinding.ActivityCreateWorkoutBindingImpl;
import com.doviesfitness.databinding.ActivityCretatDietPlanBindingImpl;
import com.doviesfitness.databinding.ActivityCustomNotificationBindingImpl;
import com.doviesfitness.databinding.ActivityDietPlanWebViewBindingImpl;
import com.doviesfitness.databinding.ActivityExerciseDetaillistBindingImpl;
import com.doviesfitness.databinding.ActivityExerciseListingBindingImpl;
import com.doviesfitness.databinding.ActivityExpandedControlsChromecastBindingImpl;
import com.doviesfitness.databinding.ActivityFaqBindingImpl;
import com.doviesfitness.databinding.ActivityFeedDetailsBindingImpl;
import com.doviesfitness.databinding.ActivityFilterExerciseBindingImpl;
import com.doviesfitness.databinding.ActivityGoodForBindingImpl;
import com.doviesfitness.databinding.ActivityInboxBindingImpl;
import com.doviesfitness.databinding.ActivityInviteFriendBindingImpl;
import com.doviesfitness.databinding.ActivityMyDietPlanBindingImpl;
import com.doviesfitness.databinding.ActivityMyWorkoutBindingImpl;
import com.doviesfitness.databinding.ActivityNewPlayerViewBindingImpl;
import com.doviesfitness.databinding.ActivityNotificationExerciesBindingImpl;
import com.doviesfitness.databinding.ActivitySelectWorkOutCateguryBindingImpl;
import com.doviesfitness.databinding.ActivitySelectWorkOutPlanBindingImpl;
import com.doviesfitness.databinding.ActivitySettingBindingImpl;
import com.doviesfitness.databinding.ActivityShowDietPlanDetailBindingImpl;
import com.doviesfitness.databinding.ActivitySplashScreenBindingImpl;
import com.doviesfitness.databinding.ActivityStreamDetailNewBindingImpl;
import com.doviesfitness.databinding.ActivityStreamLogHistoryBindingImpl;
import com.doviesfitness.databinding.ActivityStreamVideoPlayBindingImpl;
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeBindingImpl;
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeFromDownLoadBindingImpl;
import com.doviesfitness.databinding.ActivityStreamVideoPlayLandscapeTempBindingImpl;
import com.doviesfitness.databinding.ActivityStreamWorkoutCompleteBindingImpl;
import com.doviesfitness.databinding.ActivitySubscriptionBindingImpl;
import com.doviesfitness.databinding.ActivityTempDownWorkOutsVideoActvityBindingImpl;
import com.doviesfitness.databinding.ActivityTempDownloadStreamBindingImpl;
import com.doviesfitness.databinding.ActivityVideoDetailBindingImpl;
import com.doviesfitness.databinding.ActivityViewLogImagesBindingImpl;
import com.doviesfitness.databinding.ActivityWorkOutDetailBindingImpl;
import com.doviesfitness.databinding.ActivityWorkOutPlanDetailBindingImpl;
import com.doviesfitness.databinding.ActivityWorkoutCompleteBindingImpl;
import com.doviesfitness.databinding.ActivityWorkoutLogBindingImpl;
import com.doviesfitness.databinding.ActivityWorkoutVideoPlayBindingImpl;
import com.doviesfitness.databinding.ActivityWorkoutlogListBindingImpl;
import com.doviesfitness.databinding.CollectionDownloadGroupItemViewBindingImpl;
import com.doviesfitness.databinding.CustomNotificationBindingImpl;
import com.doviesfitness.databinding.DownloadWorkoutVideoBindingImpl;
import com.doviesfitness.databinding.ExampleBindingImpl;
import com.doviesfitness.databinding.FavFragmentFeedsBindingImpl;
import com.doviesfitness.databinding.FavFragmentWorkOutBindingImpl;
import com.doviesfitness.databinding.FavFragmentWorkOutPlanBindingImpl;
import com.doviesfitness.databinding.FeedViewBindingImpl;
import com.doviesfitness.databinding.FragmentDietDetailBindingImpl;
import com.doviesfitness.databinding.FragmentDietPCategoryNewDetailBindingImpl;
import com.doviesfitness.databinding.FragmentDietPcategoryDetailBindingImpl;
import com.doviesfitness.databinding.FragmentDietPlanBindingImpl;
import com.doviesfitness.databinding.FragmentDietPlanDetailBindingImpl;
import com.doviesfitness.databinding.FragmentDietPlanWebViewBindingImpl;
import com.doviesfitness.databinding.FragmentEditProfileBindingImpl;
import com.doviesfitness.databinding.FragmentExerciseBindingImpl;
import com.doviesfitness.databinding.FragmentExerciseLibraryBindingImpl;
import com.doviesfitness.databinding.FragmentExerciseLibraryCreateBindingImpl;
import com.doviesfitness.databinding.FragmentFavoriteExerciseBindingImpl;
import com.doviesfitness.databinding.FragmentHomeTabBindingImpl;
import com.doviesfitness.databinding.FragmentMyDietPlanBindingImpl;
import com.doviesfitness.databinding.FragmentMyDietPlanNewBindingImpl;
import com.doviesfitness.databinding.FragmentMyFavouriteBindingImpl;
import com.doviesfitness.databinding.FragmentMyNewWorkoutlogBindingImpl;
import com.doviesfitness.databinding.FragmentMyPlanBindingImpl;
import com.doviesfitness.databinding.FragmentMyPlanFromProfileBindingImpl;
import com.doviesfitness.databinding.FragmentMyProfileBindingImpl;
import com.doviesfitness.databinding.FragmentMyWorkoutBindingImpl;
import com.doviesfitness.databinding.FragmentMyWorkoutSelectPlanBindingImpl;
import com.doviesfitness.databinding.FragmentOtherMediaStreamBindingImpl;
import com.doviesfitness.databinding.FragmentPagerDietPlanBindingImpl;
import com.doviesfitness.databinding.FragmentPagerPlanOverViewBindingImpl;
import com.doviesfitness.databinding.FragmentPagerWorkOutPlanBindingImpl;
import com.doviesfitness.databinding.FragmentPlansBindingImpl;
import com.doviesfitness.databinding.FragmentSearchLayoutBindingImpl;
import com.doviesfitness.databinding.FragmentSettingFragmanetBindingImpl;
import com.doviesfitness.databinding.FragmentShowPagerDietPlanBindingImpl;
import com.doviesfitness.databinding.FragmentShowPagerPlanOverViewBindingImpl;
import com.doviesfitness.databinding.FragmentShowPagerWorkOutPlanBindingImpl;
import com.doviesfitness.databinding.FragmentShowWebviewBindingImpl;
import com.doviesfitness.databinding.FragmentShowWeekOneBindingImpl;
import com.doviesfitness.databinding.FragmentShowWeekThreeBindingImpl;
import com.doviesfitness.databinding.FragmentShowWeekTwoBindingImpl;
import com.doviesfitness.databinding.FragmentStreamCollectionNewBindingImpl;
import com.doviesfitness.databinding.FragmentStreamLogBindingImpl;
import com.doviesfitness.databinding.FragmentStreamVideoBindingImpl;
import com.doviesfitness.databinding.FragmentWeekFour2BindingImpl;
import com.doviesfitness.databinding.FragmentWeekFourBindingImpl;
import com.doviesfitness.databinding.FragmentWeekOneBindingImpl;
import com.doviesfitness.databinding.FragmentWeekThreeBindingImpl;
import com.doviesfitness.databinding.FragmentWeekTwoBindingImpl;
import com.doviesfitness.databinding.FragmentWorkoutBindingImpl;
import com.doviesfitness.databinding.FragmentWorkoutCollectionBindingImpl;
import com.doviesfitness.databinding.FragmentWorkoutCollectionSelectPlanBindingImpl;
import com.doviesfitness.databinding.FragmentWorkoutGrouplistBindingImpl;
import com.doviesfitness.databinding.FragmentWorkoutPlanBindingImpl;
import com.doviesfitness.databinding.FragmentWorkoutPlanDetailBindingImpl;
import com.doviesfitness.databinding.PlayerActivityBindingImpl;
import com.doviesfitness.databinding.StreamOverviewFragmentBindingImpl;
import com.doviesfitness.databinding.StreamTrailerFragmentBindingImpl;
import com.doviesfitness.databinding.WatchOrDownloadVideoLayoutBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYADDEXERCISE = 1;

  private static final int LAYOUT_ACTIVITYADDNOTESNEW = 2;

  private static final int LAYOUT_ACTIVITYADDTOWORKOUTPLAN = 3;

  private static final int LAYOUT_ACTIVITYCHANGEPASSWORDACTVITY = 4;

  private static final int LAYOUT_ACTIVITYCONTACTUS = 5;

  private static final int LAYOUT_ACTIVITYCREATEDIETDETAILACTVITY = 6;

  private static final int LAYOUT_ACTIVITYCREATEDIETPCATEGORYDETAILACTVITY = 7;

  private static final int LAYOUT_ACTIVITYCREATEDIETPLANWEBVIEW = 8;

  private static final int LAYOUT_ACTIVITYCREATEWORKOUTPLANACTIVTY = 9;

  private static final int LAYOUT_ACTIVITYCREATEWORKOUT = 10;

  private static final int LAYOUT_ACTIVITYCRETATDIETPLAN = 11;

  private static final int LAYOUT_ACTIVITYCUSTOMNOTIFICATION = 12;

  private static final int LAYOUT_ACTIVITYDIETPLANWEBVIEW = 13;

  private static final int LAYOUT_ACTIVITYEXERCISEDETAILLIST = 14;

  private static final int LAYOUT_ACTIVITYEXERCISELISTING = 15;

  private static final int LAYOUT_ACTIVITYEXPANDEDCONTROLSCHROMECAST = 16;

  private static final int LAYOUT_ACTIVITYFAQ = 17;

  private static final int LAYOUT_ACTIVITYFEEDDETAILS = 18;

  private static final int LAYOUT_ACTIVITYFILTEREXERCISE = 19;

  private static final int LAYOUT_ACTIVITYGOODFOR = 20;

  private static final int LAYOUT_ACTIVITYINBOX = 21;

  private static final int LAYOUT_ACTIVITYINVITEFRIEND = 22;

  private static final int LAYOUT_ACTIVITYMYDIETPLAN = 23;

  private static final int LAYOUT_ACTIVITYMYWORKOUT = 24;

  private static final int LAYOUT_ACTIVITYNEWPLAYERVIEW = 25;

  private static final int LAYOUT_ACTIVITYNOTIFICATIONEXERCIES = 26;

  private static final int LAYOUT_ACTIVITYSELECTWORKOUTCATEGURY = 27;

  private static final int LAYOUT_ACTIVITYSELECTWORKOUTPLAN = 28;

  private static final int LAYOUT_ACTIVITYSETTING = 29;

  private static final int LAYOUT_ACTIVITYSHOWDIETPLANDETAIL = 30;

  private static final int LAYOUT_ACTIVITYSPLASHSCREEN = 31;

  private static final int LAYOUT_ACTIVITYSTREAMDETAILNEW = 32;

  private static final int LAYOUT_ACTIVITYSTREAMLOGHISTORY = 33;

  private static final int LAYOUT_ACTIVITYSTREAMVIDEOPLAY = 34;

  private static final int LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPE = 35;

  private static final int LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPEFROMDOWNLOAD = 36;

  private static final int LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPETEMP = 37;

  private static final int LAYOUT_ACTIVITYSTREAMWORKOUTCOMPLETE = 38;

  private static final int LAYOUT_ACTIVITYSUBSCRIPTION = 39;

  private static final int LAYOUT_ACTIVITYTEMPDOWNWORKOUTSVIDEOACTVITY = 40;

  private static final int LAYOUT_ACTIVITYTEMPDOWNLOADSTREAM = 41;

  private static final int LAYOUT_ACTIVITYVIDEODETAIL = 42;

  private static final int LAYOUT_ACTIVITYVIEWLOGIMAGES = 43;

  private static final int LAYOUT_ACTIVITYWORKOUTDETAIL = 44;

  private static final int LAYOUT_ACTIVITYWORKOUTPLANDETAIL = 45;

  private static final int LAYOUT_ACTIVITYWORKOUTCOMPLETE = 46;

  private static final int LAYOUT_ACTIVITYWORKOUTLOG = 47;

  private static final int LAYOUT_ACTIVITYWORKOUTVIDEOPLAY = 48;

  private static final int LAYOUT_ACTIVITYWORKOUTLOGLIST = 49;

  private static final int LAYOUT_COLLECTIONDOWNLOADGROUPITEMVIEW = 50;

  private static final int LAYOUT_CUSTOMNOTIFICATION = 51;

  private static final int LAYOUT_DOWNLOADWORKOUTVIDEO = 52;

  private static final int LAYOUT_EXAMPLE = 53;

  private static final int LAYOUT_FAVFRAGMENTFEEDS = 54;

  private static final int LAYOUT_FAVFRAGMENTWORKOUT = 55;

  private static final int LAYOUT_FAVFRAGMENTWORKOUTPLAN = 56;

  private static final int LAYOUT_FEEDVIEW = 57;

  private static final int LAYOUT_FRAGMENTDIETDETAIL = 58;

  private static final int LAYOUT_FRAGMENTDIETPCATEGORYNEWDETAIL = 59;

  private static final int LAYOUT_FRAGMENTDIETPCATEGORYDETAIL = 60;

  private static final int LAYOUT_FRAGMENTDIETPLAN = 61;

  private static final int LAYOUT_FRAGMENTDIETPLANDETAIL = 62;

  private static final int LAYOUT_FRAGMENTDIETPLANWEBVIEW = 63;

  private static final int LAYOUT_FRAGMENTEDITPROFILE = 64;

  private static final int LAYOUT_FRAGMENTEXERCISE = 65;

  private static final int LAYOUT_FRAGMENTEXERCISELIBRARY = 66;

  private static final int LAYOUT_FRAGMENTEXERCISELIBRARYCREATE = 67;

  private static final int LAYOUT_FRAGMENTFAVORITEEXERCISE = 68;

  private static final int LAYOUT_FRAGMENTHOMETAB = 69;

  private static final int LAYOUT_FRAGMENTMYDIETPLAN = 70;

  private static final int LAYOUT_FRAGMENTMYDIETPLANNEW = 71;

  private static final int LAYOUT_FRAGMENTMYFAVOURITE = 72;

  private static final int LAYOUT_FRAGMENTMYNEWWORKOUTLOG = 73;

  private static final int LAYOUT_FRAGMENTMYPLAN = 74;

  private static final int LAYOUT_FRAGMENTMYPLANFROMPROFILE = 75;

  private static final int LAYOUT_FRAGMENTMYPROFILE = 76;

  private static final int LAYOUT_FRAGMENTMYWORKOUT = 77;

  private static final int LAYOUT_FRAGMENTMYWORKOUTSELECTPLAN = 78;

  private static final int LAYOUT_FRAGMENTOTHERMEDIASTREAM = 79;

  private static final int LAYOUT_FRAGMENTPAGERDIETPLAN = 80;

  private static final int LAYOUT_FRAGMENTPAGERPLANOVERVIEW = 81;

  private static final int LAYOUT_FRAGMENTPAGERWORKOUTPLAN = 82;

  private static final int LAYOUT_FRAGMENTPLANS = 83;

  private static final int LAYOUT_FRAGMENTSEARCHLAYOUT = 84;

  private static final int LAYOUT_FRAGMENTSETTINGFRAGMANET = 85;

  private static final int LAYOUT_FRAGMENTSHOWPAGERDIETPLAN = 86;

  private static final int LAYOUT_FRAGMENTSHOWPAGERPLANOVERVIEW = 87;

  private static final int LAYOUT_FRAGMENTSHOWPAGERWORKOUTPLAN = 88;

  private static final int LAYOUT_FRAGMENTSHOWWEBVIEW = 89;

  private static final int LAYOUT_FRAGMENTSHOWWEEKONE = 90;

  private static final int LAYOUT_FRAGMENTSHOWWEEKTHREE = 91;

  private static final int LAYOUT_FRAGMENTSHOWWEEKTWO = 92;

  private static final int LAYOUT_FRAGMENTSTREAMCOLLECTIONNEW = 93;

  private static final int LAYOUT_FRAGMENTSTREAMLOG = 94;

  private static final int LAYOUT_FRAGMENTSTREAMVIDEO = 95;

  private static final int LAYOUT_FRAGMENTWEEKFOUR = 96;

  private static final int LAYOUT_FRAGMENTWEEKFOUR2 = 97;

  private static final int LAYOUT_FRAGMENTWEEKONE = 98;

  private static final int LAYOUT_FRAGMENTWEEKTHREE = 99;

  private static final int LAYOUT_FRAGMENTWEEKTWO = 100;

  private static final int LAYOUT_FRAGMENTWORKOUT = 101;

  private static final int LAYOUT_FRAGMENTWORKOUTCOLLECTION = 102;

  private static final int LAYOUT_FRAGMENTWORKOUTCOLLECTIONSELECTPLAN = 103;

  private static final int LAYOUT_FRAGMENTWORKOUTGROUPLIST = 104;

  private static final int LAYOUT_FRAGMENTWORKOUTPLAN = 105;

  private static final int LAYOUT_FRAGMENTWORKOUTPLANDETAIL = 106;

  private static final int LAYOUT_PLAYERACTIVITY = 107;

  private static final int LAYOUT_STREAMOVERVIEWFRAGMENT = 108;

  private static final int LAYOUT_STREAMTRAILERFRAGMENT = 109;

  private static final int LAYOUT_WATCHORDOWNLOADVIDEOLAYOUT = 110;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(110);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_add_exercise, LAYOUT_ACTIVITYADDEXERCISE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_add_notes_new, LAYOUT_ACTIVITYADDNOTESNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_add_to_work_out_plan, LAYOUT_ACTIVITYADDTOWORKOUTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_change_password_actvity, LAYOUT_ACTIVITYCHANGEPASSWORDACTVITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_contact_us, LAYOUT_ACTIVITYCONTACTUS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_create__diet_detail_actvity, LAYOUT_ACTIVITYCREATEDIETDETAILACTVITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_create__diet_pcategory_detail_actvity, LAYOUT_ACTIVITYCREATEDIETPCATEGORYDETAILACTVITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_create__diet_plan_web_view, LAYOUT_ACTIVITYCREATEDIETPLANWEBVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_create_work_out_plan_activty, LAYOUT_ACTIVITYCREATEWORKOUTPLANACTIVTY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_create_workout, LAYOUT_ACTIVITYCREATEWORKOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_cretat__diet_plan, LAYOUT_ACTIVITYCRETATDIETPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_custom_notification, LAYOUT_ACTIVITYCUSTOMNOTIFICATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_diet_plan_web_view, LAYOUT_ACTIVITYDIETPLANWEBVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_exercise_detaillist, LAYOUT_ACTIVITYEXERCISEDETAILLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_exercise_listing, LAYOUT_ACTIVITYEXERCISELISTING);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_expanded_controls_chromecast, LAYOUT_ACTIVITYEXPANDEDCONTROLSCHROMECAST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_faq, LAYOUT_ACTIVITYFAQ);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_feed_details, LAYOUT_ACTIVITYFEEDDETAILS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_filter_exercise, LAYOUT_ACTIVITYFILTEREXERCISE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_good_for, LAYOUT_ACTIVITYGOODFOR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_inbox, LAYOUT_ACTIVITYINBOX);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_invite_friend, LAYOUT_ACTIVITYINVITEFRIEND);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_my_diet_plan, LAYOUT_ACTIVITYMYDIETPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_my_workout, LAYOUT_ACTIVITYMYWORKOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_new_player_view, LAYOUT_ACTIVITYNEWPLAYERVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_notification_exercies, LAYOUT_ACTIVITYNOTIFICATIONEXERCIES);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_select_work_out_categury, LAYOUT_ACTIVITYSELECTWORKOUTCATEGURY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_select_work_out_plan, LAYOUT_ACTIVITYSELECTWORKOUTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_setting, LAYOUT_ACTIVITYSETTING);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_show_diet_plan_detail, LAYOUT_ACTIVITYSHOWDIETPLANDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_splash_screen, LAYOUT_ACTIVITYSPLASHSCREEN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_stream_detail_new, LAYOUT_ACTIVITYSTREAMDETAILNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_stream_log_history, LAYOUT_ACTIVITYSTREAMLOGHISTORY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_stream_video_play, LAYOUT_ACTIVITYSTREAMVIDEOPLAY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_stream_video_play_landscape, LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_stream_video_play_landscape_from_down_load, LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPEFROMDOWNLOAD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_stream_video_play_landscape_temp, LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPETEMP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_stream_workout_complete, LAYOUT_ACTIVITYSTREAMWORKOUTCOMPLETE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_subscription, LAYOUT_ACTIVITYSUBSCRIPTION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_temp_down_work_outs_video_actvity, LAYOUT_ACTIVITYTEMPDOWNWORKOUTSVIDEOACTVITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_temp_download_stream, LAYOUT_ACTIVITYTEMPDOWNLOADSTREAM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_video_detail, LAYOUT_ACTIVITYVIDEODETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_view_log_images, LAYOUT_ACTIVITYVIEWLOGIMAGES);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_work_out_detail, LAYOUT_ACTIVITYWORKOUTDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_work_out_plan_detail, LAYOUT_ACTIVITYWORKOUTPLANDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_workout_complete, LAYOUT_ACTIVITYWORKOUTCOMPLETE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_workout_log, LAYOUT_ACTIVITYWORKOUTLOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_workout_video_play, LAYOUT_ACTIVITYWORKOUTVIDEOPLAY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.activity_workoutlog_list, LAYOUT_ACTIVITYWORKOUTLOGLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.collection_download_group_item_view, LAYOUT_COLLECTIONDOWNLOADGROUPITEMVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.custom_notification, LAYOUT_CUSTOMNOTIFICATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.download_workout_video, LAYOUT_DOWNLOADWORKOUTVIDEO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.example, LAYOUT_EXAMPLE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fav_fragment_feeds, LAYOUT_FAVFRAGMENTFEEDS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fav_fragment_work_out, LAYOUT_FAVFRAGMENTWORKOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fav_fragment_work_out_plan, LAYOUT_FAVFRAGMENTWORKOUTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.feed_view, LAYOUT_FEEDVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_diet_detail, LAYOUT_FRAGMENTDIETDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_diet_p_category_new_detail, LAYOUT_FRAGMENTDIETPCATEGORYNEWDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_diet_pcategory_detail, LAYOUT_FRAGMENTDIETPCATEGORYDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_diet_plan, LAYOUT_FRAGMENTDIETPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_diet_plan_detail, LAYOUT_FRAGMENTDIETPLANDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_diet_plan_web_view, LAYOUT_FRAGMENTDIETPLANWEBVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_edit_profile, LAYOUT_FRAGMENTEDITPROFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_exercise, LAYOUT_FRAGMENTEXERCISE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_exercise_library, LAYOUT_FRAGMENTEXERCISELIBRARY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_exercise_library_create, LAYOUT_FRAGMENTEXERCISELIBRARYCREATE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_favorite_exercise, LAYOUT_FRAGMENTFAVORITEEXERCISE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_home_tab, LAYOUT_FRAGMENTHOMETAB);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_diet_plan, LAYOUT_FRAGMENTMYDIETPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_diet_plan_new, LAYOUT_FRAGMENTMYDIETPLANNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_favourite, LAYOUT_FRAGMENTMYFAVOURITE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_new_workoutlog, LAYOUT_FRAGMENTMYNEWWORKOUTLOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_plan, LAYOUT_FRAGMENTMYPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_plan_from_profile, LAYOUT_FRAGMENTMYPLANFROMPROFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_profile, LAYOUT_FRAGMENTMYPROFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_workout, LAYOUT_FRAGMENTMYWORKOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_my_workout_select_plan, LAYOUT_FRAGMENTMYWORKOUTSELECTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_other_media_stream, LAYOUT_FRAGMENTOTHERMEDIASTREAM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_pager_diet_plan, LAYOUT_FRAGMENTPAGERDIETPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_pager_plan_over_view, LAYOUT_FRAGMENTPAGERPLANOVERVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_pager_work_out_plan, LAYOUT_FRAGMENTPAGERWORKOUTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_plans, LAYOUT_FRAGMENTPLANS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_search_layout, LAYOUT_FRAGMENTSEARCHLAYOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_setting_fragmanet, LAYOUT_FRAGMENTSETTINGFRAGMANET);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_show_pager_diet_plan, LAYOUT_FRAGMENTSHOWPAGERDIETPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_show_pager_plan_over_view, LAYOUT_FRAGMENTSHOWPAGERPLANOVERVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_show_pager_work_out_plan, LAYOUT_FRAGMENTSHOWPAGERWORKOUTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_show_webview, LAYOUT_FRAGMENTSHOWWEBVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_show_week_one, LAYOUT_FRAGMENTSHOWWEEKONE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_show_week_three, LAYOUT_FRAGMENTSHOWWEEKTHREE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_show_week_two, LAYOUT_FRAGMENTSHOWWEEKTWO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_stream_collection_new, LAYOUT_FRAGMENTSTREAMCOLLECTIONNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_stream_log, LAYOUT_FRAGMENTSTREAMLOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_stream_video, LAYOUT_FRAGMENTSTREAMVIDEO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_week_four, LAYOUT_FRAGMENTWEEKFOUR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_week_four2, LAYOUT_FRAGMENTWEEKFOUR2);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_week_one, LAYOUT_FRAGMENTWEEKONE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_week_three, LAYOUT_FRAGMENTWEEKTHREE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_week_two, LAYOUT_FRAGMENTWEEKTWO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_workout, LAYOUT_FRAGMENTWORKOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_workout_collection, LAYOUT_FRAGMENTWORKOUTCOLLECTION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_workout_collection_select_plan, LAYOUT_FRAGMENTWORKOUTCOLLECTIONSELECTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_workout_grouplist, LAYOUT_FRAGMENTWORKOUTGROUPLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_workout_plan, LAYOUT_FRAGMENTWORKOUTPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.fragment_workout_plan_detail, LAYOUT_FRAGMENTWORKOUTPLANDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.player_activity, LAYOUT_PLAYERACTIVITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.stream_overview_fragment, LAYOUT_STREAMOVERVIEWFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.stream_trailer_fragment, LAYOUT_STREAMTRAILERFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.doviesfitness.R.layout.watch_or_download_video_layout, LAYOUT_WATCHORDOWNLOADVIDEOLAYOUT);
  }

  private final ViewDataBinding internalGetViewDataBinding0(DataBindingComponent component,
      View view, int internalId, Object tag) {
    switch(internalId) {
      case  LAYOUT_ACTIVITYADDEXERCISE: {
        if ("layout/activity_add_exercise_0".equals(tag)) {
          return new ActivityAddExerciseBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_add_exercise is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYADDNOTESNEW: {
        if ("layout/activity_add_notes_new_0".equals(tag)) {
          return new ActivityAddNotesNewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_add_notes_new is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYADDTOWORKOUTPLAN: {
        if ("layout/activity_add_to_work_out_plan_0".equals(tag)) {
          return new ActivityAddToWorkOutPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_add_to_work_out_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCHANGEPASSWORDACTVITY: {
        if ("layout/activity_change_password_actvity_0".equals(tag)) {
          return new ActivityChangePasswordActvityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_change_password_actvity is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCONTACTUS: {
        if ("layout/activity_contact_us_0".equals(tag)) {
          return new ActivityContactUsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_contact_us is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCREATEDIETDETAILACTVITY: {
        if ("layout/activity_create__diet_detail_actvity_0".equals(tag)) {
          return new ActivityCreateDietDetailActvityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_create__diet_detail_actvity is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCREATEDIETPCATEGORYDETAILACTVITY: {
        if ("layout/activity_create__diet_pcategory_detail_actvity_0".equals(tag)) {
          return new ActivityCreateDietPcategoryDetailActvityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_create__diet_pcategory_detail_actvity is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCREATEDIETPLANWEBVIEW: {
        if ("layout/activity_create__diet_plan_web_view_0".equals(tag)) {
          return new ActivityCreateDietPlanWebViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_create__diet_plan_web_view is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCREATEWORKOUTPLANACTIVTY: {
        if ("layout/activity_create_work_out_plan_activty_0".equals(tag)) {
          return new ActivityCreateWorkOutPlanActivtyBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_create_work_out_plan_activty is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCREATEWORKOUT: {
        if ("layout/activity_create_workout_0".equals(tag)) {
          return new ActivityCreateWorkoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_create_workout is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCRETATDIETPLAN: {
        if ("layout/activity_cretat__diet_plan_0".equals(tag)) {
          return new ActivityCretatDietPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_cretat__diet_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCUSTOMNOTIFICATION: {
        if ("layout/activity_custom_notification_0".equals(tag)) {
          return new ActivityCustomNotificationBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_custom_notification is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYDIETPLANWEBVIEW: {
        if ("layout/activity_diet_plan_web_view_0".equals(tag)) {
          return new ActivityDietPlanWebViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_diet_plan_web_view is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYEXERCISEDETAILLIST: {
        if ("layout/activity_exercise_detaillist_0".equals(tag)) {
          return new ActivityExerciseDetaillistBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_exercise_detaillist is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYEXERCISELISTING: {
        if ("layout/activity_exercise_listing_0".equals(tag)) {
          return new ActivityExerciseListingBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_exercise_listing is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYEXPANDEDCONTROLSCHROMECAST: {
        if ("layout/activity_expanded_controls_chromecast_0".equals(tag)) {
          return new ActivityExpandedControlsChromecastBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_expanded_controls_chromecast is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYFAQ: {
        if ("layout/activity_faq_0".equals(tag)) {
          return new ActivityFaqBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_faq is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYFEEDDETAILS: {
        if ("layout/activity_feed_details_0".equals(tag)) {
          return new ActivityFeedDetailsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_feed_details is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYFILTEREXERCISE: {
        if ("layout/activity_filter_exercise_0".equals(tag)) {
          return new ActivityFilterExerciseBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_filter_exercise is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYGOODFOR: {
        if ("layout/activity_good_for_0".equals(tag)) {
          return new ActivityGoodForBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_good_for is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYINBOX: {
        if ("layout/activity_inbox_0".equals(tag)) {
          return new ActivityInboxBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_inbox is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYINVITEFRIEND: {
        if ("layout/activity_invite_friend_0".equals(tag)) {
          return new ActivityInviteFriendBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_invite_friend is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYMYDIETPLAN: {
        if ("layout/activity_my_diet_plan_0".equals(tag)) {
          return new ActivityMyDietPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_my_diet_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYMYWORKOUT: {
        if ("layout/activity_my_workout_0".equals(tag)) {
          return new ActivityMyWorkoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_my_workout is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYNEWPLAYERVIEW: {
        if ("layout/activity_new_player_view_0".equals(tag)) {
          return new ActivityNewPlayerViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_new_player_view is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYNOTIFICATIONEXERCIES: {
        if ("layout/activity_notification_exercies_0".equals(tag)) {
          return new ActivityNotificationExerciesBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_notification_exercies is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSELECTWORKOUTCATEGURY: {
        if ("layout/activity_select_work_out_categury_0".equals(tag)) {
          return new ActivitySelectWorkOutCateguryBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_select_work_out_categury is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSELECTWORKOUTPLAN: {
        if ("layout/activity_select_work_out_plan_0".equals(tag)) {
          return new ActivitySelectWorkOutPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_select_work_out_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSETTING: {
        if ("layout/activity_setting_0".equals(tag)) {
          return new ActivitySettingBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_setting is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSHOWDIETPLANDETAIL: {
        if ("layout/activity_show_diet_plan_detail_0".equals(tag)) {
          return new ActivityShowDietPlanDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_show_diet_plan_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSPLASHSCREEN: {
        if ("layout/activity_splash_screen_0".equals(tag)) {
          return new ActivitySplashScreenBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_splash_screen is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSTREAMDETAILNEW: {
        if ("layout/activity_stream_detail_new_0".equals(tag)) {
          return new ActivityStreamDetailNewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_stream_detail_new is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSTREAMLOGHISTORY: {
        if ("layout/activity_stream_log_history_0".equals(tag)) {
          return new ActivityStreamLogHistoryBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_stream_log_history is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSTREAMVIDEOPLAY: {
        if ("layout/activity_stream_video_play_0".equals(tag)) {
          return new ActivityStreamVideoPlayBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_stream_video_play is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPE: {
        if ("layout/activity_stream_video_play_landscape_0".equals(tag)) {
          return new ActivityStreamVideoPlayLandscapeBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_stream_video_play_landscape is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPEFROMDOWNLOAD: {
        if ("layout/activity_stream_video_play_landscape_from_down_load_0".equals(tag)) {
          return new ActivityStreamVideoPlayLandscapeFromDownLoadBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_stream_video_play_landscape_from_down_load is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSTREAMVIDEOPLAYLANDSCAPETEMP: {
        if ("layout/activity_stream_video_play_landscape_temp_0".equals(tag)) {
          return new ActivityStreamVideoPlayLandscapeTempBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_stream_video_play_landscape_temp is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSTREAMWORKOUTCOMPLETE: {
        if ("layout/activity_stream_workout_complete_0".equals(tag)) {
          return new ActivityStreamWorkoutCompleteBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_stream_workout_complete is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSUBSCRIPTION: {
        if ("layout/activity_subscription_0".equals(tag)) {
          return new ActivitySubscriptionBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_subscription is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYTEMPDOWNWORKOUTSVIDEOACTVITY: {
        if ("layout/activity_temp_down_work_outs_video_actvity_0".equals(tag)) {
          return new ActivityTempDownWorkOutsVideoActvityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_temp_down_work_outs_video_actvity is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYTEMPDOWNLOADSTREAM: {
        if ("layout/activity_temp_download_stream_0".equals(tag)) {
          return new ActivityTempDownloadStreamBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_temp_download_stream is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYVIDEODETAIL: {
        if ("layout/activity_video_detail_0".equals(tag)) {
          return new ActivityVideoDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_video_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYVIEWLOGIMAGES: {
        if ("layout/activity_view_log_images_0".equals(tag)) {
          return new ActivityViewLogImagesBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_view_log_images is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYWORKOUTDETAIL: {
        if ("layout/activity_work_out_detail_0".equals(tag)) {
          return new ActivityWorkOutDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_work_out_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYWORKOUTPLANDETAIL: {
        if ("layout/activity_work_out_plan_detail_0".equals(tag)) {
          return new ActivityWorkOutPlanDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_work_out_plan_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYWORKOUTCOMPLETE: {
        if ("layout/activity_workout_complete_0".equals(tag)) {
          return new ActivityWorkoutCompleteBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_workout_complete is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYWORKOUTLOG: {
        if ("layout/activity_workout_log_0".equals(tag)) {
          return new ActivityWorkoutLogBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_workout_log is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYWORKOUTVIDEOPLAY: {
        if ("layout/activity_workout_video_play_0".equals(tag)) {
          return new ActivityWorkoutVideoPlayBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_workout_video_play is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYWORKOUTLOGLIST: {
        if ("layout/activity_workoutlog_list_0".equals(tag)) {
          return new ActivityWorkoutlogListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_workoutlog_list is invalid. Received: " + tag);
      }
      case  LAYOUT_COLLECTIONDOWNLOADGROUPITEMVIEW: {
        if ("layout/collection_download_group_item_view_0".equals(tag)) {
          return new CollectionDownloadGroupItemViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for collection_download_group_item_view is invalid. Received: " + tag);
      }
    }
    return null;
  }

  private final ViewDataBinding internalGetViewDataBinding1(DataBindingComponent component,
      View view, int internalId, Object tag) {
    switch(internalId) {
      case  LAYOUT_CUSTOMNOTIFICATION: {
        if ("layout/custom_notification_0".equals(tag)) {
          return new CustomNotificationBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for custom_notification is invalid. Received: " + tag);
      }
      case  LAYOUT_DOWNLOADWORKOUTVIDEO: {
        if ("layout/download_workout_video_0".equals(tag)) {
          return new DownloadWorkoutVideoBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for download_workout_video is invalid. Received: " + tag);
      }
      case  LAYOUT_EXAMPLE: {
        if ("layout/example_0".equals(tag)) {
          return new ExampleBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for example is invalid. Received: " + tag);
      }
      case  LAYOUT_FAVFRAGMENTFEEDS: {
        if ("layout/fav_fragment_feeds_0".equals(tag)) {
          return new FavFragmentFeedsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fav_fragment_feeds is invalid. Received: " + tag);
      }
      case  LAYOUT_FAVFRAGMENTWORKOUT: {
        if ("layout/fav_fragment_work_out_0".equals(tag)) {
          return new FavFragmentWorkOutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fav_fragment_work_out is invalid. Received: " + tag);
      }
      case  LAYOUT_FAVFRAGMENTWORKOUTPLAN: {
        if ("layout/fav_fragment_work_out_plan_0".equals(tag)) {
          return new FavFragmentWorkOutPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fav_fragment_work_out_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FEEDVIEW: {
        if ("layout/feed_view_0".equals(tag)) {
          return new FeedViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for feed_view is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTDIETDETAIL: {
        if ("layout/fragment_diet_detail_0".equals(tag)) {
          return new FragmentDietDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_diet_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTDIETPCATEGORYNEWDETAIL: {
        if ("layout/fragment_diet_p_category_new_detail_0".equals(tag)) {
          return new FragmentDietPCategoryNewDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_diet_p_category_new_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTDIETPCATEGORYDETAIL: {
        if ("layout/fragment_diet_pcategory_detail_0".equals(tag)) {
          return new FragmentDietPcategoryDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_diet_pcategory_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTDIETPLAN: {
        if ("layout/fragment_diet_plan_0".equals(tag)) {
          return new FragmentDietPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_diet_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTDIETPLANDETAIL: {
        if ("layout/fragment_diet_plan_detail_0".equals(tag)) {
          return new FragmentDietPlanDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_diet_plan_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTDIETPLANWEBVIEW: {
        if ("layout/fragment_diet_plan_web_view_0".equals(tag)) {
          return new FragmentDietPlanWebViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_diet_plan_web_view is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTEDITPROFILE: {
        if ("layout/fragment_edit_profile_0".equals(tag)) {
          return new FragmentEditProfileBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_edit_profile is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTEXERCISE: {
        if ("layout/fragment_exercise_0".equals(tag)) {
          return new FragmentExerciseBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_exercise is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTEXERCISELIBRARY: {
        if ("layout/fragment_exercise_library_0".equals(tag)) {
          return new FragmentExerciseLibraryBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_exercise_library is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTEXERCISELIBRARYCREATE: {
        if ("layout/fragment_exercise_library_create_0".equals(tag)) {
          return new FragmentExerciseLibraryCreateBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_exercise_library_create is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTFAVORITEEXERCISE: {
        if ("layout/fragment_favorite_exercise_0".equals(tag)) {
          return new FragmentFavoriteExerciseBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_favorite_exercise is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTHOMETAB: {
        if ("layout/fragment_home_tab_0".equals(tag)) {
          return new FragmentHomeTabBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_home_tab is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYDIETPLAN: {
        if ("layout/fragment_my_diet_plan_0".equals(tag)) {
          return new FragmentMyDietPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_diet_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYDIETPLANNEW: {
        if ("layout/fragment_my_diet_plan_new_0".equals(tag)) {
          return new FragmentMyDietPlanNewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_diet_plan_new is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYFAVOURITE: {
        if ("layout/fragment_my_favourite_0".equals(tag)) {
          return new FragmentMyFavouriteBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_favourite is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYNEWWORKOUTLOG: {
        if ("layout/fragment_my_new_workoutlog_0".equals(tag)) {
          return new FragmentMyNewWorkoutlogBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_new_workoutlog is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYPLAN: {
        if ("layout/fragment_my_plan_0".equals(tag)) {
          return new FragmentMyPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYPLANFROMPROFILE: {
        if ("layout/fragment_my_plan_from_profile_0".equals(tag)) {
          return new FragmentMyPlanFromProfileBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_plan_from_profile is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYPROFILE: {
        if ("layout/fragment_my_profile_0".equals(tag)) {
          return new FragmentMyProfileBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_profile is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYWORKOUT: {
        if ("layout/fragment_my_workout_0".equals(tag)) {
          return new FragmentMyWorkoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_workout is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMYWORKOUTSELECTPLAN: {
        if ("layout/fragment_my_workout_select_plan_0".equals(tag)) {
          return new FragmentMyWorkoutSelectPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_my_workout_select_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTOTHERMEDIASTREAM: {
        if ("layout/fragment_other_media_stream_0".equals(tag)) {
          return new FragmentOtherMediaStreamBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_other_media_stream is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTPAGERDIETPLAN: {
        if ("layout/fragment_pager_diet_plan_0".equals(tag)) {
          return new FragmentPagerDietPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_pager_diet_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTPAGERPLANOVERVIEW: {
        if ("layout/fragment_pager_plan_over_view_0".equals(tag)) {
          return new FragmentPagerPlanOverViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_pager_plan_over_view is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTPAGERWORKOUTPLAN: {
        if ("layout/fragment_pager_work_out_plan_0".equals(tag)) {
          return new FragmentPagerWorkOutPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_pager_work_out_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTPLANS: {
        if ("layout/fragment_plans_0".equals(tag)) {
          return new FragmentPlansBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_plans is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSEARCHLAYOUT: {
        if ("layout/fragment_search_layout_0".equals(tag)) {
          return new FragmentSearchLayoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_search_layout is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSETTINGFRAGMANET: {
        if ("layout/fragment_setting_fragmanet_0".equals(tag)) {
          return new FragmentSettingFragmanetBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_setting_fragmanet is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSHOWPAGERDIETPLAN: {
        if ("layout/fragment_show_pager_diet_plan_0".equals(tag)) {
          return new FragmentShowPagerDietPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_show_pager_diet_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSHOWPAGERPLANOVERVIEW: {
        if ("layout/fragment_show_pager_plan_over_view_0".equals(tag)) {
          return new FragmentShowPagerPlanOverViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_show_pager_plan_over_view is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSHOWPAGERWORKOUTPLAN: {
        if ("layout/fragment_show_pager_work_out_plan_0".equals(tag)) {
          return new FragmentShowPagerWorkOutPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_show_pager_work_out_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSHOWWEBVIEW: {
        if ("layout/fragment_show_webview_0".equals(tag)) {
          return new FragmentShowWebviewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_show_webview is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSHOWWEEKONE: {
        if ("layout/fragment_show_week_one_0".equals(tag)) {
          return new FragmentShowWeekOneBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_show_week_one is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSHOWWEEKTHREE: {
        if ("layout/fragment_show_week_three_0".equals(tag)) {
          return new FragmentShowWeekThreeBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_show_week_three is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSHOWWEEKTWO: {
        if ("layout/fragment_show_week_two_0".equals(tag)) {
          return new FragmentShowWeekTwoBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_show_week_two is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSTREAMCOLLECTIONNEW: {
        if ("layout/fragment_stream_collection_new_0".equals(tag)) {
          return new FragmentStreamCollectionNewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_stream_collection_new is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSTREAMLOG: {
        if ("layout/fragment_stream_log_0".equals(tag)) {
          return new FragmentStreamLogBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_stream_log is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSTREAMVIDEO: {
        if ("layout/fragment_stream_video_0".equals(tag)) {
          return new FragmentStreamVideoBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_stream_video is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWEEKFOUR: {
        if ("layout/fragment_week_four_0".equals(tag)) {
          return new FragmentWeekFourBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_week_four is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWEEKFOUR2: {
        if ("layout/fragment_week_four2_0".equals(tag)) {
          return new FragmentWeekFour2BindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_week_four2 is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWEEKONE: {
        if ("layout/fragment_week_one_0".equals(tag)) {
          return new FragmentWeekOneBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_week_one is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWEEKTHREE: {
        if ("layout/fragment_week_three_0".equals(tag)) {
          return new FragmentWeekThreeBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_week_three is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWEEKTWO: {
        if ("layout/fragment_week_two_0".equals(tag)) {
          return new FragmentWeekTwoBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_week_two is invalid. Received: " + tag);
      }
    }
    return null;
  }

  private final ViewDataBinding internalGetViewDataBinding2(DataBindingComponent component,
      View view, int internalId, Object tag) {
    switch(internalId) {
      case  LAYOUT_FRAGMENTWORKOUT: {
        if ("layout/fragment_workout_0".equals(tag)) {
          return new FragmentWorkoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_workout is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWORKOUTCOLLECTION: {
        if ("layout/fragment_workout_collection_0".equals(tag)) {
          return new FragmentWorkoutCollectionBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_workout_collection is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWORKOUTCOLLECTIONSELECTPLAN: {
        if ("layout/fragment_workout_collection_select_plan_0".equals(tag)) {
          return new FragmentWorkoutCollectionSelectPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_workout_collection_select_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWORKOUTGROUPLIST: {
        if ("layout/fragment_workout_grouplist_0".equals(tag)) {
          return new FragmentWorkoutGrouplistBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_workout_grouplist is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWORKOUTPLAN: {
        if ("layout/fragment_workout_plan_0".equals(tag)) {
          return new FragmentWorkoutPlanBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_workout_plan is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWORKOUTPLANDETAIL: {
        if ("layout/fragment_workout_plan_detail_0".equals(tag)) {
          return new FragmentWorkoutPlanDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_workout_plan_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_PLAYERACTIVITY: {
        if ("layout/player_activity_0".equals(tag)) {
          return new PlayerActivityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for player_activity is invalid. Received: " + tag);
      }
      case  LAYOUT_STREAMOVERVIEWFRAGMENT: {
        if ("layout/stream_overview_fragment_0".equals(tag)) {
          return new StreamOverviewFragmentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for stream_overview_fragment is invalid. Received: " + tag);
      }
      case  LAYOUT_STREAMTRAILERFRAGMENT: {
        if ("layout/stream_trailer_fragment_0".equals(tag)) {
          return new StreamTrailerFragmentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for stream_trailer_fragment is invalid. Received: " + tag);
      }
      case  LAYOUT_WATCHORDOWNLOADVIDEOLAYOUT: {
        if ("layout/watch_or_download_video_layout_0".equals(tag)) {
          return new WatchOrDownloadVideoLayoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for watch_or_download_video_layout is invalid. Received: " + tag);
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      // find which method will have it. -1 is necessary becausefirst id starts with 1;
      int methodIndex = (localizedLayoutId - 1) / 50;
      switch(methodIndex) {
        case 0: {
          return internalGetViewDataBinding0(component, view, localizedLayoutId, tag);
        }
        case 1: {
          return internalGetViewDataBinding1(component, view, localizedLayoutId, tag);
        }
        case 2: {
          return internalGetViewDataBinding2(component, view, localizedLayoutId, tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(4);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "featuredData");
      sKeys.put(2, "program");
      sKeys.put(3, "workout");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(110);

    static {
      sKeys.put("layout/activity_add_exercise_0", com.doviesfitness.R.layout.activity_add_exercise);
      sKeys.put("layout/activity_add_notes_new_0", com.doviesfitness.R.layout.activity_add_notes_new);
      sKeys.put("layout/activity_add_to_work_out_plan_0", com.doviesfitness.R.layout.activity_add_to_work_out_plan);
      sKeys.put("layout/activity_change_password_actvity_0", com.doviesfitness.R.layout.activity_change_password_actvity);
      sKeys.put("layout/activity_contact_us_0", com.doviesfitness.R.layout.activity_contact_us);
      sKeys.put("layout/activity_create__diet_detail_actvity_0", com.doviesfitness.R.layout.activity_create__diet_detail_actvity);
      sKeys.put("layout/activity_create__diet_pcategory_detail_actvity_0", com.doviesfitness.R.layout.activity_create__diet_pcategory_detail_actvity);
      sKeys.put("layout/activity_create__diet_plan_web_view_0", com.doviesfitness.R.layout.activity_create__diet_plan_web_view);
      sKeys.put("layout/activity_create_work_out_plan_activty_0", com.doviesfitness.R.layout.activity_create_work_out_plan_activty);
      sKeys.put("layout/activity_create_workout_0", com.doviesfitness.R.layout.activity_create_workout);
      sKeys.put("layout/activity_cretat__diet_plan_0", com.doviesfitness.R.layout.activity_cretat__diet_plan);
      sKeys.put("layout/activity_custom_notification_0", com.doviesfitness.R.layout.activity_custom_notification);
      sKeys.put("layout/activity_diet_plan_web_view_0", com.doviesfitness.R.layout.activity_diet_plan_web_view);
      sKeys.put("layout/activity_exercise_detaillist_0", com.doviesfitness.R.layout.activity_exercise_detaillist);
      sKeys.put("layout/activity_exercise_listing_0", com.doviesfitness.R.layout.activity_exercise_listing);
      sKeys.put("layout/activity_expanded_controls_chromecast_0", com.doviesfitness.R.layout.activity_expanded_controls_chromecast);
      sKeys.put("layout/activity_faq_0", com.doviesfitness.R.layout.activity_faq);
      sKeys.put("layout/activity_feed_details_0", com.doviesfitness.R.layout.activity_feed_details);
      sKeys.put("layout/activity_filter_exercise_0", com.doviesfitness.R.layout.activity_filter_exercise);
      sKeys.put("layout/activity_good_for_0", com.doviesfitness.R.layout.activity_good_for);
      sKeys.put("layout/activity_inbox_0", com.doviesfitness.R.layout.activity_inbox);
      sKeys.put("layout/activity_invite_friend_0", com.doviesfitness.R.layout.activity_invite_friend);
      sKeys.put("layout/activity_my_diet_plan_0", com.doviesfitness.R.layout.activity_my_diet_plan);
      sKeys.put("layout/activity_my_workout_0", com.doviesfitness.R.layout.activity_my_workout);
      sKeys.put("layout/activity_new_player_view_0", com.doviesfitness.R.layout.activity_new_player_view);
      sKeys.put("layout/activity_notification_exercies_0", com.doviesfitness.R.layout.activity_notification_exercies);
      sKeys.put("layout/activity_select_work_out_categury_0", com.doviesfitness.R.layout.activity_select_work_out_categury);
      sKeys.put("layout/activity_select_work_out_plan_0", com.doviesfitness.R.layout.activity_select_work_out_plan);
      sKeys.put("layout/activity_setting_0", com.doviesfitness.R.layout.activity_setting);
      sKeys.put("layout/activity_show_diet_plan_detail_0", com.doviesfitness.R.layout.activity_show_diet_plan_detail);
      sKeys.put("layout/activity_splash_screen_0", com.doviesfitness.R.layout.activity_splash_screen);
      sKeys.put("layout/activity_stream_detail_new_0", com.doviesfitness.R.layout.activity_stream_detail_new);
      sKeys.put("layout/activity_stream_log_history_0", com.doviesfitness.R.layout.activity_stream_log_history);
      sKeys.put("layout/activity_stream_video_play_0", com.doviesfitness.R.layout.activity_stream_video_play);
      sKeys.put("layout/activity_stream_video_play_landscape_0", com.doviesfitness.R.layout.activity_stream_video_play_landscape);
      sKeys.put("layout/activity_stream_video_play_landscape_from_down_load_0", com.doviesfitness.R.layout.activity_stream_video_play_landscape_from_down_load);
      sKeys.put("layout/activity_stream_video_play_landscape_temp_0", com.doviesfitness.R.layout.activity_stream_video_play_landscape_temp);
      sKeys.put("layout/activity_stream_workout_complete_0", com.doviesfitness.R.layout.activity_stream_workout_complete);
      sKeys.put("layout/activity_subscription_0", com.doviesfitness.R.layout.activity_subscription);
      sKeys.put("layout/activity_temp_down_work_outs_video_actvity_0", com.doviesfitness.R.layout.activity_temp_down_work_outs_video_actvity);
      sKeys.put("layout/activity_temp_download_stream_0", com.doviesfitness.R.layout.activity_temp_download_stream);
      sKeys.put("layout/activity_video_detail_0", com.doviesfitness.R.layout.activity_video_detail);
      sKeys.put("layout/activity_view_log_images_0", com.doviesfitness.R.layout.activity_view_log_images);
      sKeys.put("layout/activity_work_out_detail_0", com.doviesfitness.R.layout.activity_work_out_detail);
      sKeys.put("layout/activity_work_out_plan_detail_0", com.doviesfitness.R.layout.activity_work_out_plan_detail);
      sKeys.put("layout/activity_workout_complete_0", com.doviesfitness.R.layout.activity_workout_complete);
      sKeys.put("layout/activity_workout_log_0", com.doviesfitness.R.layout.activity_workout_log);
      sKeys.put("layout/activity_workout_video_play_0", com.doviesfitness.R.layout.activity_workout_video_play);
      sKeys.put("layout/activity_workoutlog_list_0", com.doviesfitness.R.layout.activity_workoutlog_list);
      sKeys.put("layout/collection_download_group_item_view_0", com.doviesfitness.R.layout.collection_download_group_item_view);
      sKeys.put("layout/custom_notification_0", com.doviesfitness.R.layout.custom_notification);
      sKeys.put("layout/download_workout_video_0", com.doviesfitness.R.layout.download_workout_video);
      sKeys.put("layout/example_0", com.doviesfitness.R.layout.example);
      sKeys.put("layout/fav_fragment_feeds_0", com.doviesfitness.R.layout.fav_fragment_feeds);
      sKeys.put("layout/fav_fragment_work_out_0", com.doviesfitness.R.layout.fav_fragment_work_out);
      sKeys.put("layout/fav_fragment_work_out_plan_0", com.doviesfitness.R.layout.fav_fragment_work_out_plan);
      sKeys.put("layout/feed_view_0", com.doviesfitness.R.layout.feed_view);
      sKeys.put("layout/fragment_diet_detail_0", com.doviesfitness.R.layout.fragment_diet_detail);
      sKeys.put("layout/fragment_diet_p_category_new_detail_0", com.doviesfitness.R.layout.fragment_diet_p_category_new_detail);
      sKeys.put("layout/fragment_diet_pcategory_detail_0", com.doviesfitness.R.layout.fragment_diet_pcategory_detail);
      sKeys.put("layout/fragment_diet_plan_0", com.doviesfitness.R.layout.fragment_diet_plan);
      sKeys.put("layout/fragment_diet_plan_detail_0", com.doviesfitness.R.layout.fragment_diet_plan_detail);
      sKeys.put("layout/fragment_diet_plan_web_view_0", com.doviesfitness.R.layout.fragment_diet_plan_web_view);
      sKeys.put("layout/fragment_edit_profile_0", com.doviesfitness.R.layout.fragment_edit_profile);
      sKeys.put("layout/fragment_exercise_0", com.doviesfitness.R.layout.fragment_exercise);
      sKeys.put("layout/fragment_exercise_library_0", com.doviesfitness.R.layout.fragment_exercise_library);
      sKeys.put("layout/fragment_exercise_library_create_0", com.doviesfitness.R.layout.fragment_exercise_library_create);
      sKeys.put("layout/fragment_favorite_exercise_0", com.doviesfitness.R.layout.fragment_favorite_exercise);
      sKeys.put("layout/fragment_home_tab_0", com.doviesfitness.R.layout.fragment_home_tab);
      sKeys.put("layout/fragment_my_diet_plan_0", com.doviesfitness.R.layout.fragment_my_diet_plan);
      sKeys.put("layout/fragment_my_diet_plan_new_0", com.doviesfitness.R.layout.fragment_my_diet_plan_new);
      sKeys.put("layout/fragment_my_favourite_0", com.doviesfitness.R.layout.fragment_my_favourite);
      sKeys.put("layout/fragment_my_new_workoutlog_0", com.doviesfitness.R.layout.fragment_my_new_workoutlog);
      sKeys.put("layout/fragment_my_plan_0", com.doviesfitness.R.layout.fragment_my_plan);
      sKeys.put("layout/fragment_my_plan_from_profile_0", com.doviesfitness.R.layout.fragment_my_plan_from_profile);
      sKeys.put("layout/fragment_my_profile_0", com.doviesfitness.R.layout.fragment_my_profile);
      sKeys.put("layout/fragment_my_workout_0", com.doviesfitness.R.layout.fragment_my_workout);
      sKeys.put("layout/fragment_my_workout_select_plan_0", com.doviesfitness.R.layout.fragment_my_workout_select_plan);
      sKeys.put("layout/fragment_other_media_stream_0", com.doviesfitness.R.layout.fragment_other_media_stream);
      sKeys.put("layout/fragment_pager_diet_plan_0", com.doviesfitness.R.layout.fragment_pager_diet_plan);
      sKeys.put("layout/fragment_pager_plan_over_view_0", com.doviesfitness.R.layout.fragment_pager_plan_over_view);
      sKeys.put("layout/fragment_pager_work_out_plan_0", com.doviesfitness.R.layout.fragment_pager_work_out_plan);
      sKeys.put("layout/fragment_plans_0", com.doviesfitness.R.layout.fragment_plans);
      sKeys.put("layout/fragment_search_layout_0", com.doviesfitness.R.layout.fragment_search_layout);
      sKeys.put("layout/fragment_setting_fragmanet_0", com.doviesfitness.R.layout.fragment_setting_fragmanet);
      sKeys.put("layout/fragment_show_pager_diet_plan_0", com.doviesfitness.R.layout.fragment_show_pager_diet_plan);
      sKeys.put("layout/fragment_show_pager_plan_over_view_0", com.doviesfitness.R.layout.fragment_show_pager_plan_over_view);
      sKeys.put("layout/fragment_show_pager_work_out_plan_0", com.doviesfitness.R.layout.fragment_show_pager_work_out_plan);
      sKeys.put("layout/fragment_show_webview_0", com.doviesfitness.R.layout.fragment_show_webview);
      sKeys.put("layout/fragment_show_week_one_0", com.doviesfitness.R.layout.fragment_show_week_one);
      sKeys.put("layout/fragment_show_week_three_0", com.doviesfitness.R.layout.fragment_show_week_three);
      sKeys.put("layout/fragment_show_week_two_0", com.doviesfitness.R.layout.fragment_show_week_two);
      sKeys.put("layout/fragment_stream_collection_new_0", com.doviesfitness.R.layout.fragment_stream_collection_new);
      sKeys.put("layout/fragment_stream_log_0", com.doviesfitness.R.layout.fragment_stream_log);
      sKeys.put("layout/fragment_stream_video_0", com.doviesfitness.R.layout.fragment_stream_video);
      sKeys.put("layout/fragment_week_four_0", com.doviesfitness.R.layout.fragment_week_four);
      sKeys.put("layout/fragment_week_four2_0", com.doviesfitness.R.layout.fragment_week_four2);
      sKeys.put("layout/fragment_week_one_0", com.doviesfitness.R.layout.fragment_week_one);
      sKeys.put("layout/fragment_week_three_0", com.doviesfitness.R.layout.fragment_week_three);
      sKeys.put("layout/fragment_week_two_0", com.doviesfitness.R.layout.fragment_week_two);
      sKeys.put("layout/fragment_workout_0", com.doviesfitness.R.layout.fragment_workout);
      sKeys.put("layout/fragment_workout_collection_0", com.doviesfitness.R.layout.fragment_workout_collection);
      sKeys.put("layout/fragment_workout_collection_select_plan_0", com.doviesfitness.R.layout.fragment_workout_collection_select_plan);
      sKeys.put("layout/fragment_workout_grouplist_0", com.doviesfitness.R.layout.fragment_workout_grouplist);
      sKeys.put("layout/fragment_workout_plan_0", com.doviesfitness.R.layout.fragment_workout_plan);
      sKeys.put("layout/fragment_workout_plan_detail_0", com.doviesfitness.R.layout.fragment_workout_plan_detail);
      sKeys.put("layout/player_activity_0", com.doviesfitness.R.layout.player_activity);
      sKeys.put("layout/stream_overview_fragment_0", com.doviesfitness.R.layout.stream_overview_fragment);
      sKeys.put("layout/stream_trailer_fragment_0", com.doviesfitness.R.layout.stream_trailer_fragment);
      sKeys.put("layout/watch_or_download_video_layout_0", com.doviesfitness.R.layout.watch_or_download_video_layout);
    }
  }
}
