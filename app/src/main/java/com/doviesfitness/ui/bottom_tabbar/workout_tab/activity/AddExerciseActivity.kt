package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityAddExerciseBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.AddExercisePagerAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.ExerciseLibraryCreateWorkout
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.FavoriteExerciseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_add_exercise.*
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_CreateLeftAndRight
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_CreateSuperSet
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_addExercise_setAndReps
 import kotlinx.android.synthetic.main.activity_exercise_detaillist.ll_setsAndRepsBottomView
import kotlinx.android.synthetic.main.activity_exercise_detaillist.txt_filter


class AddExerciseActivity : BaseActivity(), View.OnClickListener,
    FavoriteExerciseFragment.MyCallback {
    private var forReplace: String? = ""
    private var exerciseType: String? = ""
    private var isAddingInRound: String? = ""
    private lateinit var binding: ActivityAddExerciseBinding
    private lateinit var pagerAdapter: AddExercisePagerAdapter
    private var mLastClickTime: Long = 0
    private var selectedList = ArrayList<ExerciseListingResponse.Data>()
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()

    private var isSuperSetEnable: Boolean = false
    private var isLeftAndRightEnable: Boolean = false
    private var isAddExerciseEnable: Boolean = false
    var ifReplaceExerciese = false



    private var filterTags: String = ""
    private var filterLevel: String = ""
    private var filterBodyPart: String = ""
    private var filterEquipments: String = ""
    private var filterGoodFor: String = ""
    private var filterExercises: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_exercise)
        initialization()
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    private fun initialization() {

        if (intent.hasExtra("forReplace")) {
            forReplace = intent.getStringExtra("forReplace")
            if (forReplace!!.isNotEmpty()) {
                ifReplaceExerciese = true
            }
        }

        if (intent.hasExtra("isAddingInRound")) {
            isAddingInRound = intent.getStringExtra("isAddingInRound")
        }

        if (intent.getStringExtra("workout_Type") != null) {
            exerciseType = intent.getStringExtra("workout_Type")
            Log.d("exssss", ": exerciseType $exerciseType")



        }

        fragments.add(
            ExerciseLibraryCreateWorkout.newInstance(
                "create",
                forReplace,
                "$exerciseType",
                isAddingInRound
            )
        )
        fragments.add(
            FavoriteExerciseFragment.newInstance(
                "$forReplace",
                exerciseType,
                isAddingInRound
            )
        )
        var fragment=fragments[1] as FavoriteExerciseFragment
        fragment.setCallback(this)
        binding.filterIcon.setOnClickListener(this)
        binding.searchImg.setOnClickListener(this)
        binding.btnAddExercise.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.replaceExercises.setOnClickListener(this)
        binding.btnCreateLeftAndRight.setOnClickListener(this)
        binding.btnCreateSuperSet.setOnClickListener(this)
        binding.btnAddExerciseSetAndReps.setOnClickListener(this)
         pagerAdapter = AddExercisePagerAdapter(supportFragmentManager, getActivity(), fragments)
        binding.viewpager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {

                    binding.filterIcon.visibility = View.VISIBLE
                    binding.searchImg.visibility = View.VISIBLE
                    binding.doneBtn.visibility = View.GONE

                    binding.llSetAndRepsBView.visibility = View.GONE
                    binding.llFollowAlongBottomView.visibility = View.GONE
                } else {



                    binding.filterIcon.visibility = View.GONE
                    binding.searchImg.visibility = View.GONE
                    binding.doneBtn.visibility = View.GONE


                    if (forReplace!!.isEmpty()) {
                        binding.replaceExercises.visibility = View.GONE
                        if (exerciseType == "SetAndReps") {
                            binding.llSetAndRepsBView.visibility = View.VISIBLE
                            binding.llFollowAlongBottomView.visibility = View.GONE

                        } else {
                            binding.llSetAndRepsBView.visibility = View.GONE
                            binding.llFollowAlongBottomView.visibility = View.VISIBLE
                        }
                    } else {
                        binding.replaceExercises.visibility = View.VISIBLE
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_addExercise -> {
                selectedList.addAll((fragments[1] as FavoriteExerciseFragment).getSelectedExercise())
                val intent = Intent()
                intent.putExtra("list", selectedList)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            R.id.replace_exercises -> {
                selectedList.addAll((fragments[1] as FavoriteExerciseFragment).getSelectedExercise())
                val intent = Intent()
                intent.putExtra("list", selectedList)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            /*  R.id.done_btn -> {
                  selectedList.addAll((fragments[1] as FavoriteExerciseFragment).getSelectedExercise())
                  val intent = Intent()
                  intent.putExtra("list", selectedList)
                  setResult(Activity.RESULT_OK, intent)
                  finish()
              }*/
            R.id.search_img -> {

                if (forReplace != null && forReplace!!.isNotEmpty() && forReplace.equals(
                        "forReplace",
                        true
                    )
                ) {
                    startActivityForResult(
                        Intent(getActivity(), AddExerciseFromSearchActivity::class.java)
                            .putExtra("create", "create")
                            .putExtra("forReplace", forReplace), 7
                    )
                } else {
                    startActivityForResult(
                        Intent(getActivity(), AddExerciseFromSearchActivity::class.java)
                            .putExtra("create", "create")
                            .putExtra("forReplace", ""), 7
                    )
                }
            }

            R.id.iv_back -> {
                onBackPressed()
            }





            R.id.btn_CreateLeftAndRight -> {
                if (isLeftAndRightEnable) {


                    getSelectedExercise("LeftAndRight")
                }
            }

            R.id.btn_CreateSuperSet -> {
                if (isSuperSetEnable) {
                     getSelectedExercise("SuperSet")
                }
            }
            R.id.btn_addExercise_setAndReps -> {
                if (isAddExerciseEnable) {

                    getSelectedExercise("")
                }
            }


            R.id.filter_icon -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }

                if (forReplace != null && !forReplace!!.isEmpty() && forReplace.equals(
                        "forReplace",
                        true
                    )
                ) {
                    startActivityForResult(
                        Intent(getActivity(), FilterExerciseActivity::class.java)
                            .putExtra("category_id", "")
                            .putExtra("create", "create")
                            .putExtra("forReplace", forReplace),
                        7
                    )
                } else {
                    startActivityForResult(
                        Intent(getActivity(), FilterExerciseActivity::class.java)
                            .putExtra("category_id", "")
                            .putExtra("create", "create"),
                        7
                    )

                }

                /*
                                startActivity(
                                    Intent(getActivity(), FilterExerciseActivity::class.java)
                                        .putExtra("category_id", "").putExtra("create", "create")
                                )
                */
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            selectedList.clear()
            selectedList.addAll(data!!.getSerializableExtra("list") as java.util.ArrayList<ExerciseListingResponse.Data>)
            val intent = Intent()
            intent.putExtra("list", selectedList)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    /** getting callback from fragment*/
    override fun onDataReceived(selectedList: ArrayList<ExerciseListingResponse.Data>) {// getting here selected exercise from favorite list

        if (exerciseType == "SetAndReps"){
            manageBottomButtonsEnableDiable(selectedList)
        }else{
            if (selectedList.isNotEmpty())
                binding.selectedCount.text = "${selectedList.size} SELECTED"
            else
                binding.selectedCount.text = "SELECTED"
        }
    }





    fun manageBottomButtonsEnableDiable(selectedList: ArrayList<ExerciseListingResponse.Data>) {
        val filteredList =  selectedList.filter { it.isSelected }
        if (filteredList.isNotEmpty()) {
            btn_addExercise_setAndReps.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.exo_white
                )
            );

            if (exerciseType == "SetAndReps") {
                /** for SetAndReps related selection and bottom view upedations  done by @Santosh jaat */
                if (isAddingInRound == "yes" || isAddingInRound == "Yes"){
                    btn_addExercise.visibility = View.VISIBLE

                    if (filteredList.size>1)
                        btn_addExercise.text = "Add Exercises"
                    else
                        btn_addExercise.text = "Add Exercise"
                }
                else if (forReplace == "forReplace"){
                    btn_addExercise.visibility = View.VISIBLE

                    btn_addExercise.text = "Replace"
                }
                else

                    llSetAndRepsBView.visibility = View.VISIBLE



                llFollowAlongBottomView.visibility = View.GONE
                if (filteredList.size == 1) {
                    /** only add exercise will be enable because for creating super set and left and right need atleast 2 exercise*/
                    isSuperSetEnable = false
                    isLeftAndRightEnable = false
                    isAddExerciseEnable = true
                    btn_addExercise_setAndReps.text = "ADD EXERCISE"
                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    binding.btnCreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );
                    binding.btnCreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );

                }
                else if (filteredList.size == 2) {
                    /**left and right , super set and add exercise will be enable for 2 selected exercises*/
                    isSuperSetEnable = true
                    isLeftAndRightEnable = true
                    isAddExerciseEnable = true
                    btn_addExercise_setAndReps.text = "ADD EXERCISES"

                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    binding.btnCreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    binding.btnCreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );


                } else if (filteredList.size >= 3) {
                    /**super set and add exercise button is enable because selected exercises are equal or more then 3*/
                    isSuperSetEnable = true
                    isLeftAndRightEnable = false
                    isAddExerciseEnable = true
                    btn_addExercise_setAndReps.text = "ADD EXERCISES"

                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    binding.btnCreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    binding.btnCreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );

                } else {
                    /**all buttons are disable because all no exercise is selected*/
                    isSuperSetEnable = false
                    isLeftAndRightEnable = false
                    isAddExerciseEnable = false
                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );
                    binding.btnCreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );
                    binding.btnCreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );

                }


            } else {
                /** for follow along related selection and bottom view upedations  done by @Santosh jaat */
                llFollowAlongBottomView.visibility = View.VISIBLE
                if (filteredList.size == 1) {
                    selected_count.text = "${filteredList.size} SELECTED"
                    btn_addExercise.text = "ADD EXERCISE"
                } else {
                    selected_count.text = "${filteredList.size} SELECTED"
                    btn_addExercise.text = "ADD EXERCISES"
                }
            }


        } else {
            btn_addExercise.visibility = View.GONE
            llFollowAlongBottomView.visibility = View.GONE
            llSetAndRepsBView.visibility = View.GONE
            /** all buttons are disable because all no exercise is selected */
            isSuperSetEnable = false
            isLeftAndRightEnable = false
            isAddExerciseEnable = false
            btn_addExercise_setAndReps.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.line_color
                )
            );
            binding.btnCreateSuperSet.setTextColor(ContextCompat.getColor(this, R.color.line_color));
            binding.btnCreateLeftAndRight.setTextColor(ContextCompat.getColor(this, R.color.line_color));
        }
    }


    fun getSelectedExercise(exerciseType: String) {
        selectedList.clear()
        selectedList.addAll((fragments[1] as FavoriteExerciseFragment).getSelectedExercise())
        if (selectedList.isNotEmpty()) {
            selectedList.forEach {
                it.leftAndRightOrSuperSetOrAddExercise = exerciseType
            }
            if (ifReplaceExerciese) {

                selectedList.clear()
                selectedList.addAll((fragments[1] as FavoriteExerciseFragment).getSelectedExercise())
                Log.v("exerciseListing", "" + selectedList)
                val intent = Intent()
                intent.putExtra("list", selectedList)
                setResult(Activity.RESULT_OK, intent)
                finish()

            } else {
                val intent = Intent()
                intent.putExtra("list", selectedList)
                intent.putExtra("exerciseType", exerciseType)
                setResult(Activity.RESULT_OK, intent)
                finish()

            }
        }


    }
}