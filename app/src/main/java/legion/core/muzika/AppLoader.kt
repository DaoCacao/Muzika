package legion.core.muzika

import android.app.Application

class AppLoader : Application() {

    val musicProvider: Provider by lazy {
        MusicProvider(applicationContext)
    }
    val musicController: Controller by lazy {
        MusicController()
    }
}