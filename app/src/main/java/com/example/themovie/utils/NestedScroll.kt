package com.example.themovie.utils


import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class NestedScroll(
    val layoutManager: LinearLayoutManager
) : NestedScrollView.OnScrollChangeListener {

    companion object {
        val PAGE_START = 1
        private val PAGE_SIZE = 20
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (v != null) {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                    scrollY > oldScrollY
                ) {

                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading() && !isLastPage()) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                            firstVisibleItemPosition >= 0 &&
                            totalItemCount >= PAGE_SIZE
                        ) {
                            loadMoreItems()
                        }
                    }
                }
            }
        }
    }


//    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//        super.onScrolled(recyclerView, dx, dy)
//
//        val visibleItemCount = layoutManager.childCount
//        val totalItemCount = layoutManager.itemCount
//        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//        if (!isLoading() && !isLastPage()) {
//            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
//                firstVisibleItemPosition >= 0 &&
//                totalItemCount >= PAGE_SIZE
//            ) {
//                loadMoreItems()
//            }
//        }
//    }

    protected abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}