package com.example.playlistmaker

import SongModel.Result
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var noResultsImage: ImageView
    private lateinit var noResultsText: TextView
    private lateinit var errorImage: ImageView
    private lateinit var errorText1: TextView
    private lateinit var retryButton: Button

    private lateinit var recyclerView: RecyclerView
    private var editTextValue: String? = null
    private lateinit var trackAdapter: TrackAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val songService = retrofit.create(SongApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.editText)
        clearButton = findViewById(R.id.clearIcon)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(emptyList())
        recyclerView.adapter = trackAdapter
        noResultsImage = findViewById(R.id.noResultsImage)
        noResultsText = findViewById(R.id.noResultsText)
        errorImage = findViewById(R.id.errorImage)
        errorText1 = findViewById(R.id.errorText1)
        retryButton = findViewById(R.id.retryButton)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            super.onBackPressed()
        }

        retryButton.setOnClickListener {
            hidePlaceholders()
            performSearch(inputEditText.text.toString())
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            clearButton.visibility = View.GONE
            trackAdapter.updateTracks(emptyList())
            hidePlaceholders()
        }

        editTextValue = savedInstanceState?.getString(TEXT_VALUE_KEY)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextValue = s.toString()
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch(inputEditText.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_VALUE_KEY, editTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(editTextValue)
    }

    private fun performSearch(query: String) {
        songService.search(query).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful && response.body() != null) {
                    val songResults = response.body()!!.results
                    val tracks = songResults.map { songResult ->
                        Track(
                            trackName = songResult.trackName,
                            artistName = songResult.artistName,
                            trackTime = songResult.trackTimeMillis,
                            artworkUrl100 = songResult.artworkUrl100
                        )
                    }
                    if (tracks.isEmpty()) {
                        showNoResultsMessage()
                    } else {
                        trackAdapter.updateTracks(tracks)
                    }
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                showError()
            }
        })
    }


    private fun showNoResultsMessage() {
        noResultsImage.visibility = View.VISIBLE
        noResultsText.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun showError() {
        errorImage.visibility = View.VISIBLE
        errorText1.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        hideKeyboard()
    }

    private fun hidePlaceholders() {
        noResultsImage.visibility = View.GONE
        noResultsText.visibility = View.GONE
        errorImage.visibility = View.GONE
        errorText1.visibility = View.GONE
        retryButton.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    companion object {
        const val TEXT_VALUE_KEY = "textValue"
    }

}



