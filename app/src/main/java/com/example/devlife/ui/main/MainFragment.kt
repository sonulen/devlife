package com.example.devlife.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

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


        if (savedInstanceState == null) {
            previousFab.isEnabled = !viewModel.isHistoryEmpty()

            previousFab.setOnClickListener {
                Toast.makeText(it.context, "Prev fab clicked", Toast.LENGTH_SHORT).show()

            }

            nextFab.setOnClickListener {
                Toast.makeText(it.context, "Next fab clicked", Toast.LENGTH_SHORT).show()
            }
        }

        // Подпишимся на пост
        val data: LiveData<Post>? = viewModel.getData()
        data?.observe(this, Observer<Post?> {
            previousFab.isEnabled = !viewModel.isHistoryEmpty()
            Toast.makeText(context, "Data changed", Toast.LENGTH_SHORT).show()

            it?.let {
                imageTitle.text = it.description

                Glide
                    .with(this).asGif()
                    .load(it.gifURL)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.ic_loading)
                    .error(R.mipmap.ic_error)
                    .into(mImageView);
            }


        })

        imageTitle.text = getString(R.string.empty_image_title)

        val resourceId: Int = R.mipmap.ic_launcher
        Glide
            .with(this)
            .load(resourceId)
            .fitCenter()
            .into(mImageView);

    }

}