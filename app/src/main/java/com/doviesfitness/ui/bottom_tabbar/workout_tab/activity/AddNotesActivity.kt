package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.utils.Utility.Companion.toEditable
import kotlinx.android.synthetic.main.activity_add_notes_new.cancel_button_note
import kotlinx.android.synthetic.main.activity_add_notes_new.et_Note
import kotlinx.android.synthetic.main.activity_add_notes_new.iv_plus
import kotlinx.android.synthetic.main.activity_add_notes_new.ll_saveButton
import kotlinx.android.synthetic.main.activity_add_notes_new.tv_done

class AddNotesActivity : BaseActivity() {
    var isNOtesFor = ""
    var flag = true
    var notesText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes_new)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        //
        if (intent.getStringExtra("notesFor") != null) {
            isNOtesFor = intent.getStringExtra("notesFor")!!
        }
        if (intent.getStringExtra("notesText") != null && intent.getStringExtra("notesText")
                .toString().trim().isNotEmpty()
        ) {
            et_Note.text = intent.getStringExtra("notesText").toString().trim().toEditable()
            notesText = intent.getStringExtra("notesText").toString().trim().toEditable().toString()
            et_Note.isEnabled = false
        }

         et_Note.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

             override fun afterTextChanged(p0: Editable?) {

                 if (p0.toString().trim().isEmpty()) {

                 } else {
                     iv_plus.visibility = View.GONE
                     tv_done.visibility = View.VISIBLE
                 }
             }
         })
        cancel_button_note.setOnClickListener { finish() }

        iv_plus.setOnClickListener {
            tv_done.visibility = View.VISIBLE
            iv_plus.visibility = View.GONE
            et_Note.isEnabled = true
            et_Note.hint = "Add note"
            showKeyboard(this)
            et_Note.requestFocus()
            et_Note.setSelection(et_Note.length())
        }
        tv_done.setOnClickListener {
            et_Note.text = et_Note.text.toString().trim().toEditable()
            hideKeyboard()
            et_Note.isEnabled = false
            tv_done.visibility = View.GONE
            iv_plus.visibility = View.VISIBLE

            if (et_Note.text.toString().trim().isNotEmpty()){
                if (notesText==et_Note.text.toString().trim())
                ll_saveButton.visibility = View.GONE
               else
                ll_saveButton.visibility = View.VISIBLE
            }
            else
                ll_saveButton.visibility = View.GONE
        }
        ll_saveButton.setOnClickListener {
            var noteString = et_Note.text.toString().trim()
            if (noteString.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("NotesFor", "$isNOtesFor")
                intent.putExtra("NoteText", "$noteString")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}