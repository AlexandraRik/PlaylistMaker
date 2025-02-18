package com.example.playlistmaker.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.data.network.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.network.RetrofitClient.retrofit
import com.example.playlistmaker.data.dto.SongSearchResponse
import com.example.playlistmaker.data.network.SongApiService
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SongInteractor
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.models.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var noResultsImage: ImageView
    private lateinit var noResultsText: TextView
    private lateinit var historyLine: TextView
    private lateinit var errorImage: ImageView
    private lateinit var errorText1: TextView
    private lateinit var retryButton: Button
    private lateinit var recyclerView: RecyclerView
    private var editTextValue: String? = null
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistory
    private lateinit var clearHistoryButton: Button
    private lateinit var historyRecyclerView: RecyclerView
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true

    private val songService = retrofit.create(SongApiService::class.java)

    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var songInteractor: SongInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.editText)
        clearButton = findViewById(R.id.clearIcon)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(emptyList()) { track ->
            onTrackClick(track)
        }
        recyclerView.adapter = trackAdapter

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = TrackAdapter(emptyList()) { track ->
            onTrackClick(track) }
        historyRecyclerView.adapter = historyAdapter

        noResultsImage = findViewById(R.id.noResultsImage)
        noResultsText = findViewById(R.id.noResultsText)
        historyLine= findViewById(R.id.historyLine)
        errorImage = findViewById(R.id.errorImage)
        errorText1 = findViewById(R.id.errorText1)
        retryButton = findViewById(R.id.retryButton)
        clearHistoryButton = findViewById(R.id.clearButton)
        progressBar = findViewById(R.id.progressBar)
       // searchHistory = SearchHistory(getSharedPreferences("search_prefs", MODE_PRIVATE))
        searchHistoryInteractor = Creator.provideHistoryInteractor(getSharedPreferences("search_prefs", MODE_PRIVATE))
        updateHistory()
        songInteractor = Creator.provideSongsInteractor()



        clearHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            updateHistory()
            hideHistory()
        }

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
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            updateHistoryVisibility()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchRunnable = Runnable { performSearch(inputEditText.text.toString()) }
                searchDebounce(searchRunnable)
                editTextValue = s.toString()
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                if (s.isNullOrEmpty()) {
                    hidePlaceholders()
                    updateHistoryVisibility()
                    trackAdapter.updateTracks(emptyList())
                } else {
                    hideHistory()
                }

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

    private fun searchDebounce(searchRunnable: Runnable) {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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
        if (query.isNotEmpty()) {
            hidePlaceholders()
            loader()
            recyclerView.visibility = View.GONE


            songInteractor.search(query, object : SongInteractor.SongConsumer {
                override fun consume(foundSongs: List<Song>) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE

                        if (foundSongs.isEmpty()) {
                            showNoResultsMessage()
                        } else {
                            val tracks = foundSongs.map { it.toTrack() }
                            trackAdapter.updateTracks(tracks)
                        }
                    }
                }
            })
        }
    }

    fun Song.toTrack(): Track {
        return Track(
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTimeMillis,
            artworkUrl100 = this.artworkUrl100,
            trackId = this.trackId,
            collectionName = this.collectionName,
            releaseDate = this.releaseDate,
            primaryGenreName = this.primaryGenreName,
            country = this.country,
            previewUrl = this.previewUrl
        )
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
    private fun loader() {
        progressBar.visibility = View.VISIBLE

    }

    private fun onTrackClick(track: Track) {
        searchHistoryInteractor.addTrack(track)
        updateHistory()
        if (clickDebounce()) {
            val displayIntent = Intent(this, PlayerActivity::class.java)
            displayIntent.putExtra(TRACK_KEY, track)
            startActivity(displayIntent)
        }
    }

    private fun updateHistory() {
        //val history = searchHistory.getHistory()
        searchHistoryInteractor.getHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(history: List<Track>) {
                runOnUiThread {
                    historyAdapter.updateTracks(history)
                }
            }
        })
    }
    private fun hideHistory() {
        historyRecyclerView.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historyLine.visibility = View.GONE

    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun updateHistoryVisibility() {
        searchHistoryInteractor.getHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(history: List<Track>) {
                runOnUiThread {

                    historyAdapter.updateTracks(history)

                    if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && history.isNotEmpty()) {
                        historyLine.visibility = View.VISIBLE
                        historyRecyclerView.visibility = View.VISIBLE
                        clearHistoryButton.visibility = View.VISIBLE
                    } else {
                        hideHistory()
                    }
                }
            }
        })
    }


    companion object {
        const val TEXT_VALUE_KEY = "textValue"
        const val TRACK_KEY = "track"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}