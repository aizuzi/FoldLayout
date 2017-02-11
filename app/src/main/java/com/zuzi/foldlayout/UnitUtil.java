package com.zuzi.foldlayout;

import android.content.Context;

/**
 * Created by admin on 17/2/11.
 */

public class UnitUtil {

  public static int dp2px(Context context, float dp) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5f);
  }

}
