package com.noordwind.apps.collectively.presentation.addremark.tags

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.presentation.addremark.mvp.TagsListMvp
import com.noordwind.apps.collectively.presentation.settings.dagger.TagsListScreenModule
import kotlinx.android.synthetic.main.activity_tags_list.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import org.zakariya.stickyheaders.StickyHeaderLayoutManager
import javax.inject.Inject


class TagsListActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), TagsListMvp.View {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, TagsListActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: TagsListMvp.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent!!.plusTagsListScreenComponent(TagsListScreenModule(this)).inject(this)
        setContentView(R.layout.activity_tags_list);
        toolbarTitleLabel.text = getString(R.string.available_tags)
        presenter.loadTags()
    }

    override fun showTags(groupedTags: Map<String, List<String>>) {

        val stickyHeaderLayoutManager = StickyHeaderLayoutManager()
        stickyHeaderLayoutManager.headerPositionChangedCallback = object : StickyHeaderLayoutManager.HeaderPositionChangedCallback {
            override fun onHeaderPositionChanged(sectionIndex: Int, header: View, oldPosition: StickyHeaderLayoutManager.HeaderPosition, newPosition: StickyHeaderLayoutManager.HeaderPosition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY
                    header.elevation = if (elevated) 8F else 0F
                }
            }
        }

        tagsRecycler.layoutManager = stickyHeaderLayoutManager
        tagsRecycler.adapter = StickyTagsAdapter(groupedTags)
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
