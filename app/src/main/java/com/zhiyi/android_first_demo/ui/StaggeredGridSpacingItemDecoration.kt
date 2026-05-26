package com.zhiyi.android_first_demo.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 专门为瀑布流设计的完美间距装饰器
 * @param spanCount 列数（比如 2 列）
 * @param spacingDp 期望的间距大小（单位：像素 px，可以通过 dp2px 转换，这里实验直接用 px 演示）
 */
class StaggeredGridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        // 动态获取当前卡片处在左边还是右边 (0 是左，1 是右)
        val spanIndex = params.spanIndex
        val position = parent.getChildAdapterPosition(view)

        // 顶部的间距控制：第一排不要顶边间距，后面的卡片头部都留出间距
        if (position < spanCount) {
            outRect.top = 0
        } else {
            outRect.top = spacing
        }

        // 左右完美对齐的核心数学算法
        if (spanIndex == 0) {
            // 左边这一列：靠左屏幕留完整间距，靠右留一半
            outRect.left = spacing
            outRect.right = spacing / 2
        } else {
            // 右边这一列：靠左留一半，靠右屏幕留完整间距
            outRect.left = spacing / 2
            outRect.right = spacing
        }

        // 每个卡片底部也都留出对应间距
        outRect.bottom = spacing
    }
}