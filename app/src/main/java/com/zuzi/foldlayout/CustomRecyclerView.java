package com.zuzi.foldlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class CustomRecyclerView extends RecyclerView {
  private static final int TOUCH_IDLE = 0;
  private static final int TOUCH_DRAG_LAYOUT = 1;

  private int scrollMode;
  private float downY;
  private int minHeight;
  boolean isAtTop;

  public CustomRecyclerView(Context arg0) {
    super(arg0);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (getTop() > minHeight) {
      getParent().requestDisallowInterceptTouchEvent(false);
      return false;
    }
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
      downY = ev.getRawY();
      isAtTop = isReadyForPullStart();
      scrollMode = TOUCH_IDLE;
      getParent().requestDisallowInterceptTouchEvent(true);
    } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
      if (scrollMode == TOUCH_IDLE) {
        float yDistance = Math.abs(downY - ev.getRawY());
        if (yDistance > 0) {
          scrollMode = TOUCH_DRAG_LAYOUT;
          if (downY < ev.getRawY() && isAtTop) {
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
          }
        }
      }
    }

    return super.dispatchTouchEvent(ev);
  }

  //传入头部布局的最小高度，用于分发触摸事件
  public void setMinHeight(int minHeight) {
    this.minHeight = minHeight;
  }

  //判断RecyclerView是否滚动到顶部
  protected boolean isReadyForPullStart() {
    if (getChildCount() <= 0)
      return false;
    View view = getChildAt(0);
    int firstVisiblePosition = getChildAdapterPosition(view);
    if (firstVisiblePosition == 0) {
      return view.getTop() >= getPaddingTop();
    } else {
      return false;
    }
  }
}