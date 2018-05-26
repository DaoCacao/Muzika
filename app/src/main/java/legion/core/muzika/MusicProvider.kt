package legion.core.muzika

import android.content.Context
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.MediaStore.Audio.AudioColumns.ARTIST
import android.provider.MediaStore.MediaColumns.TITLE
import legion.core.muzika.all_songs_screen.Song
import android.provider.MediaStore.Images.Media.*


interface Provider {
    fun getIds(): ArrayList<Long>
    fun getTitle(id: Long): String
    fun getArtist(id: Long): String
    fun getPath(id: Long): String

    fun getAllSongs(): ArrayList<Song>
}

interface Controller {
    fun play(path: String)
}

class MusicProvider(private val context: Context) : Provider {

    private val musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    override fun getIds(): ArrayList<Long> {
        val ids = ArrayList<Long>()
        val projection = arrayOf(_ID)
        val cursor = getQuery(musicUri, projection)

        while (cursor.moveToNext()) {
            ids.add(cursor.getLongByName(_ID))
        }
        cursor.close()
        return ids
    }

    override fun getTitle(id: Long): String {
        return getQuery(musicUri, arrayOf(TITLE), "$_ID == \"$id\"").getSingleString(TITLE)
    }

    override fun getArtist(id: Long): String {
        return getQuery(musicUri, arrayOf(ARTIST), "$_ID == \"$id\"").getSingleString(ARTIST)
    }

    override fun getPath(id: Long): String {
        return getQuery(musicUri, arrayOf(DATA), "$_ID == \"$id\"").getSingleString(DATA)
    }

    override fun getAllSongs(): ArrayList<Song> {
        val projection = arrayOf(_ID, TITLE, ARTIST)

        val songs = ArrayList<Song>()

        val musicCursor = getQuery(musicUri, projection)

        while (musicCursor.moveToNext()) {
            val idIndex = musicCursor.getColumnIndex(_ID)
            val titleIndex = musicCursor.getColumnIndex(TITLE)
            val artistIndex = musicCursor.getColumnIndex(ARTIST)

            val id = musicCursor.getLong(idIndex)
            val title = musicCursor.getString(titleIndex)
            val artist = musicCursor.getString(artistIndex)

            println("TITLE: $id $title $artist")

            val song = Song(id, title, artist)
            songs.add(song)
        }
        musicCursor.close()

        return songs
    }

    private fun getQuery(uri: Uri, projection: Array<String>? = null, selection: String? = null, selectionArgs: Array<String>? = null, sortOrder: String? = null): Cursor {
        return context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }
}

class MusicController : Controller {

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    override fun play(path: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(path)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

}