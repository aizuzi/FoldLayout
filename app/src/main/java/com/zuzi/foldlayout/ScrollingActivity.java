package com.zuzi.foldlayout;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends FragmentActivity {

  private DragLayout drag_layout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initTheme();
    setContentView(R.layout.activity_scrolling);

    drag_layout = (DragLayout) findViewById(R.id.drag_layout);

    RecyclerView recyclerView = drag_layout.getRecyclerView();
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    List<String> lists = new ArrayList<>();

    for (int i = 0; i < 300; i++) {
      lists.add("item -- " + i);
    }

    recyclerView.setAdapter(new BaseQuickAdapter(R.layout.item_main, lists) {
      @Override
      protected void convert(BaseViewHolder holder, Object o) {
        holder.setText(R.id.name_tv, o + "");
      }
    });
  }

  private void initTheme(){
    setTheme(R.style.BaseTheme);
    if (Build.VERSION.SDK_INT >= 19) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
  }
}
