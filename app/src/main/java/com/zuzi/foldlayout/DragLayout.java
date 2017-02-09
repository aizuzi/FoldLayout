package com.zuzi.foldlayout;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 17/2/9.
 */

public class DragLayout extends ViewGroup {

  private CustomRecyclerView mRecyclerView;

  private HeadView mHeadView;

  private ViewDragHelper mDragHelper;

  //HeadView的高度
  private int topViewHeight;

  //HeadView的最高高度
  public int MIN_HEIGHT = dp2px(getContext(), 80);

  public DragLayout(Activity context) {
    super(context);
    init();
  }

  public DragLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    mDragHelper = ViewDragHelper
        .create(this, 1f, new MDragCallback());
    mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);

    mHeadView = new HeadView(getContext());
    mRecyclerView = new CustomRecyclerView(getContext());
    mRecyclerView.setMinHeight(MIN_HEIGHT);

    addView(mHeadView);

    LayoutParams recyclerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    addView(mRecyclerView, recyclerLp);

  }

  private void onScroll(int marginTop) {
    int maxScroll = topViewHeight - MIN_HEIGHT;
    float percentage = (float) Math.abs(marginTop) / (float) maxScroll;

    if (mHeadView != null) {
      mHeadView.offsetPixel(percentage);
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
  }

  @Override
  public void computeScroll() {
    if (mDragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  private class MDragCallback extends ViewDragHelper.Callback {

    @Override
    public void onViewPositionChanged(View changedView, int left, int top,
                                      int dx, int dy) {
      int index = changedView == mRecyclerView?2:1;

      onViewPosChanged(index);
      if (index == 1) {
        onScroll(top);
      } else {
        onScroll(topViewHeight - top);
      }
    }

    /**
     * 获取RecyclerView滚动的距离
     * @return
     */
    public int getScrollYDistance() {
      LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
      int position = layoutManager.findFirstVisibleItemPosition();
      View firstVisiableChildView = layoutManager.findViewByPosition(position);
      if (firstVisiableChildView == null) return 0;
      int itemHeight = firstVisiableChildView.getHeight();
      return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
      if (child == mRecyclerView && getScrollYDistance() > 0) {
        mRecyclerView.requestFocus();
        return false;
      }
      return true;
    }

    @Override
    public int getViewVerticalDragRange(View child) {
      return 1;
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
      animTopOrBottom(releasedChild, yvel);
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
      int scrollTop = top;
      if (child == mHeadView) {
        // 拖动HeadView
        if (top <= -(topViewHeight - MIN_HEIGHT)) {
          scrollTop = -(topViewHeight - MIN_HEIGHT);
        } else if (top > 0) {
          scrollTop = 0;
        }
      } else if (child == mRecyclerView) {
        // 拖动RecyclerView
        if (top <= MIN_HEIGHT) {
          scrollTop = MIN_HEIGHT;
        } else if (top > topViewHeight) {
          scrollTop = topViewHeight;
        }
      }

      return scrollTop;
    }
  }

  private void animTopOrBottom(View releasedChild, float yvel) {
    int scrollTop;

    int viewHeight = topViewHeight - MIN_HEIGHT;
    if (releasedChild == mHeadView) {
      if ((yvel < viewHeight) && (yvel < -viewHeight
          || (mHeadView.getTop() < -viewHeight / 2))) {
        scrollTop = -viewHeight;
      } else {
        scrollTop = 0;
      }
    } else {
      if ((yvel > -viewHeight) && (yvel > viewHeight
          || (releasedChild.getTop() > (topViewHeight - viewHeight / 2)))) {
        scrollTop = topViewHeight;
      } else {
        scrollTop = MIN_HEIGHT;
      }
    }
    if (mDragHelper.smoothSlideViewTo(releasedChild, 0, scrollTop)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  private void onViewPosChanged(int index) {
    if (index == 1) {
      int offsetTopBottom = topViewHeight + mHeadView.getTop()
          - mRecyclerView.getTop();
      mRecyclerView.offsetTopAndBottom(offsetTopBottom);
    } else if (index == 2) {
      int offsetTopBottom = mRecyclerView.getTop() - topViewHeight
          - mHeadView.getTop();
      mHeadView.offsetTopAndBottom(offsetTopBottom);
    }

    invalidate();
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {

    boolean shouldIntercept;

    try {
      shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
      return shouldIntercept;
    } catch (Exception e) {
    }
    int action = ev.getActionMasked();

    if (action == MotionEvent.ACTION_DOWN) {
      mDragHelper.processTouchEvent(ev);
    }

    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    mDragHelper.processTouchEvent(e);
    return true;
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
      topViewHeight = mHeadView.getMeasuredHeight();
      mHeadView.layout(l, 0, r, topViewHeight);
      mRecyclerView.layout(l, 0, r, b - t - MIN_HEIGHT);

      mRecyclerView.offsetTopAndBottom(topViewHeight);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    measureChildren(widthMeasureSpec, heightMeasureSpec);

    int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
    int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(
        View.resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
        View.resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
  }

  public RecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  public int dp2px(Context context, float dp) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5f);
  }

}
