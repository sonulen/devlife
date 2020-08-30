package com.example.devlife.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.devlife.R
import com.example.devlife.data.Category
import com.example.devlife.data.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.main_fragment.view.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance(category: Category) =
            MainFragment().apply {
                this.category = category
            }
    }

    private lateinit var viewModel: FeedViewModel
    private lateinit var imageTitle: TextView
    private lateinit var mImageView: ImageView
    private lateinit var previousFab: FloatingActionButton
    private lateinit var nextFab: FloatingActionButton
    private lateinit var category: Category


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v = inflater.inflate(R.layout.main_fragment, container, false)

        imageTitle = v.imageTitle
        mImageView = v.imageView
        previousFab = v.fab_prev
        nextFab = v.fab_next

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders
            .of(this, FeedViewModelFactory(category))[FeedViewModel::class.java]

        // Обновим fabs и установим на них listener'ов
        if (savedInstanceState == null) {
            updateFabVisible()

            previousFab.setOnClickListener {
                viewModel.prevPost()
            }

            nextFab.setOnClickListener {
                viewModel.nextPost()
            }
        }

        // Подпишимся на обновления текущего поста
        val data: LiveData<Post>? = viewModel.getData()
        data?.observe(this, Observer<Post?> {
            updateFabVisible()

            it?.let {
                imageTitle.text = it.description

                Glide.with(this).load(it.gifURL)
                    .thumbnail(
                        Glide.with(this).load(Uri.parse("file:///android_asset/loading.gif"))
                    )
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mImageView)
            }


        })

        // Загрузим дефолтное состояние
        imageTitle.text = getString(R.string.empty_image_title)
        Glide
            .with(this)
            .load(Uri.parse("file:///android_asset/loading.gif"))
            .fitCenter()
            .transition(withCrossFade())
            .into(mImageView);
    }

    fun updateFabVisible() {
        if (viewModel.isHistoryEmpty()) {
            previousFab.hide()
        } else {
            previousFab.show()
        }

        if (viewModel.yetAnotherPost()) {
            nextFab.show()
        } else {
            nextFab.hide()
        }
    }

}