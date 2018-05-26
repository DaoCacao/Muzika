package legion.core.muzika

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

//TODO --> why it's working? learn
inline fun <reified A : Activity> Activity.launchActivity() {
    startActivity(Intent(this, A::class.java))
}

fun ViewGroup.inflate(layoutRes: Int): View? {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Cursor.getSingleString(name: String): String {
    moveToNext()
    val string = getString(getColumnIndex(name))
    close()
    return string
}

fun Cursor.getStringByName(name: String): String {
    return getString(getColumnIndex(name))
}

fun Cursor.getLongByName(name: String): Long {
    return getLong(getColumnIndex(name))
}