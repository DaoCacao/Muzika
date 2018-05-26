package legion.core.muzika.main_screen

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import legion.core.muzika.all_songs_screen.AllSongsActivity
import legion.core.muzika.R
import legion.core.muzika.launchActivity

interface Main {

    interface View {
        fun navigateToAllSongs()
    }

    interface Presenter {
        fun onBtnAllClick()
    }
}


class MainActivity : AppCompatActivity(), Main.View {
    private var presenter = MainPresenter(this) as Main.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_all.setOnClickListener { presenter.onBtnAllClick() }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }
    }

    override fun navigateToAllSongs() {
        launchActivity<AllSongsActivity>()
    }
}


class MainPresenter(private var view: Main.View) : Main.Presenter {

    override fun onBtnAllClick() {
        view.navigateToAllSongs()
    }
}