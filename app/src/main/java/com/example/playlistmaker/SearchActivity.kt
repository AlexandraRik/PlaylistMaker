package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private var editTextValue: String? = null
    private lateinit var trackAdapter: TrackAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById<EditText>(R.id.editText)
        clearButton = findViewById<ImageView>(R.id.clearIcon)
        recyclerView = findViewById(R.id.recyclerView)


        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(TrackRepository.tracks)
        recyclerView.adapter = trackAdapter


        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            super.onBackPressed()
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            clearButton.visibility = View.GONE
        }
        editTextValue = savedInstanceState?.getString(TEXT_VALUE_KEY)



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextValue = s.toString()
                if (s != null) {
                    if (s.isNotEmpty() && s.isNotBlank()) {
                        clearButton.visibility = View.VISIBLE
                    } else {
                        clearButton.visibility = View.GONE
                    }
                }

            }
            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)


    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       // val text = inputEditText.text.toString()
        outState.putString(TEXT_VALUE_KEY, editTextValue)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //val textValue = savedInstanceState.getString(TEXT_VALUE_KEY)
        inputEditText.setText(editTextValue)
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    companion object {
        const val TEXT_VALUE_KEY = "textValue"
    }

}