package com.application.testproject

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.testproject.MainActivity.Companion.getRootDirPath
import com.bumptech.glide.Glide
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.Status
import kotlinx.android.synthetic.main.adapter_layout_imageslist.view.*


class AdapterImagesList(val context: Context, var imagesArrayList: ArrayList<String>) :
    RecyclerView.Adapter<AdapterImagesList.ImagesListViewHolder>() {

    private var dirPath: String? = null

    class ImagesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesListViewHolder =
        ImagesListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.adapter_layout_imageslist, parent, false)
        )

    override fun getItemCount(): Int = imagesArrayList.size

    override fun onBindViewHolder(holder: ImagesListViewHolder, position: Int) {
        var itemValue = 0
        Glide.with(context).load(imagesArrayList[position]).into(holder.itemView.ivImage)
        dirPath = getRootDirPath(context)

        holder.itemView.setOnClickListener {

            if (!MainActivity.isStartDownload) {
                return@setOnClickListener
            }

            holder.itemView.tvStatus.visibility = View.VISIBLE

            if (holder.itemView.tvStatus.text == context.getString(R.string.completed)) {
                Toast.makeText(context, "Downloading completed already", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Status.RUNNING == PRDownloader.getStatus(itemValue)) {
                PRDownloader.pause(itemValue);
                return@setOnClickListener
            }

            if (Status.RUNNING == PRDownloader.getStatus(itemValue)) {
                PRDownloader.pause(itemValue);
                return@setOnClickListener
            }

            holder.itemView.progressBar.isIndeterminate = true;
            holder.itemView.progressBar.indeterminateDrawable.setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN
            )

            if (Status.PAUSED == PRDownloader.getStatus(itemValue)) {
                PRDownloader.resume(itemValue);
                return@setOnClickListener
            }

        }

        if (MainActivity.isStartDownload) {
            holder.itemView.tvStatus.visibility = View.VISIBLE
            itemValue = PRDownloader.download(imagesArrayList[position], dirPath, "$position.png")
                .build()
                .setOnStartOrResumeListener {
                    holder.itemView.progressBar.isIndeterminate = false
                    holder.itemView.tvStatus.text = context.getString(R.string.pause)
                }
                .setOnPauseListener {
                    holder.itemView.tvStatus.text = context.getString(R.string.resume)
                }
                .setOnCancelListener {
                    holder.itemView.progressBar.progress = 0
                    holder.itemView.progressBar.isIndeterminate = false
                }
                .setOnProgressListener { progress ->
                    val progressPercent =
                        progress.currentBytes * 100 / progress.totalBytes
                    holder.itemView.progressBar.progress = progressPercent.toInt()
                    holder.itemView.tvProgressPercentage.text = "%$progressPercent"
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
//                        Glide.with(context).load(dirPath+"$position.png").into(holder.itemView.ivImage)
                        holder.itemView.tvStatus.text = context.getString(R.string.completed)
                    }

                    override fun onError(error: com.downloader.Error?) {
                        holder.itemView.progressBar.progress = 0
                    }

                })
        }

    }
}