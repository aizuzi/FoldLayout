package com.zuzi.foldlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by admin on 16/12/30.
 */

public class HeadView extends LinearLayout {
  private ImageView imageview;

  private float imagevViewY;
  public HeadView(Context context) {
    super(context);
    init();
  }

  public HeadView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  /**
   * 初始化头部布局
   */
  private void init() {
    imageview = new ImageView(getContext());
    imageview.setImageResource(R.mipmap.ic_launcher);
    addView(imageview,new LayoutParams(UnitUtil.dp2px(getContext(),80),UnitUtil.dp2px(getContext(),80)));

    setGravity(Gravity.CENTER);
    setBackgroundColor(getResources().getColor(R.color.colorAccent));
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if(imagevViewY ==0)
    imagevViewY = imageview.getY();
  }

  /**
   * 拖动时改变控件的位置
   * @param percentage
   */
  public void offsetPixel(float percentage) {
    int viewHeight = getHeight();
    imageview.setY(imagevViewY+(int) ((viewHeight-imageview.getHeight())/2*percentage));
  }
}
