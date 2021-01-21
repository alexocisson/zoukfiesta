package ch.hearc.zoukfiesta.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore


class CreateActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.create_activity)
    }

    //Wait when the user choose the music folder
    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            Log.i("Test", "Result URI " + data.data)

            val uri: String? = data.data?.path

            //Get the directory
            val df = data?.data?.let { DocumentFile.fromTreeUri(this, it) }

            //Get the files in the directory
            val files = df?.listFiles()

            //Iterate over each file
            if (files != null) {
                files.forEach { df ->
                    //Is it a file ?
                    if(df.isFile) {
                        if (df.type == "audio/mpeg" && df.canRead()) {
                            MusicStore.musics.add(Music(df.name.toString(), 2, df.uri, this))
                        }
                    }
                }
            }
        }
    }
}