package com.application.testproject

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imagesList: ArrayList<String>
    private lateinit var adapterImagesList: AdapterImagesList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagesList = arrayListOf()
        imagesList.add("https://cdn.wallpapersafari.com/36/6/WCkZue.png")
        imagesList.add("https://www.iliketowastemytime.com/sites/default/files/hamburg-germany-nicolas-kamp-hd-wallpaper_0.jpg")
        imagesList.add("https://images.hdqwalls.com/download/drift-transformers-5-the-last-knight-qu-5120x2880.jpg")
        imagesList.add("https://survarium.com/sites/default/files/calendars/survarium-wallpaper-calendar-february-2016-en-2560x1440.png")

        adapterImagesList = AdapterImagesList(this, imagesList)

        rvImageDownload.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = AdapterImagesList(this@MainActivity, imagesList)
            adapter = adapterImagesList
        }

        btStartDownload.setOnClickListener(this)
        ivQuestion.setOnClickListener(this)
    }

    companion object {
        var isStartDownload: Boolean = false
        fun getRootDirPath(context: Context): String? {
            return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val file: File = ContextCompat.getExternalFilesDirs(
                    context.applicationContext,
                    null
                )[0]
                file.absolutePath
            } else {
                context.applicationContext.filesDir.absolutePath
            }
        }
    }

    override fun onClick(clickView: View?) {
        when (clickView) {
            btStartDownload -> {
                isStartDownload = !isStartDownload
                adapterImagesList.notifyDataSetChanged()
                btStartDownload.visibility = View.GONE
            }

            ivQuestion->{
                AlertDialog.Builder(this)
                    .setTitle("Alert!")
                    .setMessage("Please open this path to see the downloaded images "+ getRootDirPath(this))
                    .setPositiveButton(android.R.string.ok
                    ) { dialog, which ->
                      dialog.dismiss()
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        }
    }
}