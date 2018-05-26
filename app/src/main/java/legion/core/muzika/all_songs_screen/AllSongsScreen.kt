package legion.core.muzika.all_songs_screen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_all_songs.*
import kotlinx.android.synthetic.main.item_song.view.*
import legion.core.muzika.*
import java.util.*
import kotlin.collections.ArrayList

interface AllSongs {

    interface View {
        fun showSongs(songs: ArrayList<Song>)
    }

    interface Presenter {
        fun onViewInit()
    }

    interface OnSongClick {
        fun onClick(id: Long)
    }
}

class AllSongsActivity : AppCompatActivity(), AllSongs.View {

    private lateinit var presenter: AllSongsPresenter
    private lateinit var adapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_songs)

        presenter = AllSongsPresenter(this, (application as AppLoader).musicProvider, (application as AppLoader).musicController)
        adapter = SongAdapter(presenter)

        rv_songs.layoutManager = LinearLayoutManager(this)
        rv_songs.adapter = adapter

        presenter.onViewInit()
    }

    override fun showSongs(songs: ArrayList<Song>) {
        adapter.setSongs(songs)
    }
}

class AllSongsPresenter(private val view: AllSongs.View, private val musicProvider: Provider, private val musicController: Controller) : AllSongs.Presenter, AllSongs.OnSongClick {
    override fun onViewInit() {
        view.showSongs(musicProvider.getAllSongs())
    }

    override fun onClick(id: Long) {
        println("On Click: ${musicProvider.getPath(id)}")
        musicController.play(musicProvider.getPath(id))
    }

}

class SongAdapter(private val onSongClick: AllSongs.OnSongClick) : RecyclerView.Adapter<SongVH>() {

    private var songs: ArrayList<Song> = ArrayList()

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: SongVH, position: Int) = holder.bind(songs[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongVH = SongVH(parent, onSongClick)

    fun setSongs(songs: ArrayList<Song>) {
        this.songs = songs
        songs.sortWith(Comparator { song1, song2 -> song1.name.compareTo(song2.name) })
        notifyDataSetChanged()
    }
}

class SongVH(parent: ViewGroup, private val onSongClick: AllSongs.OnSongClick) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_song)) {

    private val tvName: TextView = itemView.tv_name
    private val tvArtist: TextView = itemView.tv_artist

    fun bind(song: Song) {
        tvName.text = song.name
        tvArtist.text = song.artist

        itemView.setOnClickListener { onSongClick.onClick(song.id) }
    }
}

class Song(var id: Long, var name: String, var artist: String)