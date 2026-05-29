package com.zhiyi.android_first_demo.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import org.maplibre.android.maps.MapView

/**
 * 专门解决地图与外层 ScrollView/NestedScrollView 触摸事件冲突的自定义 MapView
 * 采用 @JvmOverloads constructor 自动生成多个构造函数，保证 XML 能够正常解析
 */
class TouchableMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 当用户手指按下地图时，强行让父布局（ScrollView）不要拦截事件，保证地图滑行丝滑
                parent?.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 当用户手指抬起或离开时，恢复父布局的拦截权限，不影响页面上下滑动
                parent?.requestDisallowInterceptTouchEvent(false)
            }
        }
        // 保持原有的触摸事件分发流程
        return super.dispatchTouchEvent(ev)
    }
}