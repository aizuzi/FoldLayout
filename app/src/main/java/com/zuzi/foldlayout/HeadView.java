package com.zuzi.foldlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by admin on 16/12/30.
 */

public class HeadView extends RelativeLayout {
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

  private void init() {
    LayoutInflater.from(getContext()).inflate(R.layout.item_head, this, true);
    imageview = (ImageView) findViewById(R.id.imageview);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if(imagevViewY ==0)
    imagevViewY = imageview.getY();
  }

  public void offsetPixel(float percentage) {
    int viewHeight = getHeight();
    imageview.setY(imagevViewY+(int) ((viewHeight-imageview.getHeight())/2*percentage));
  }
}
