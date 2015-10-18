package com.spielpark.steve.bernieapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.misc.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {
  private static FeedbackFragment mInstance;

  public static FeedbackFragment getInstance() {
    if (mInstance == null) {
      mInstance = new FeedbackFragment();
    }
    return mInstance;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    WebView wv = (WebView) view.findViewById(R.id.feedback_webview);
    StringBuilder bld = new StringBuilder();
    int[] wh = Util.getScreenWidthHeight(getActivity());
    bld.append(
        "<iframe src=\"https://docs.google.com/forms/d/1YtW1qhtXIb7rdiksI94XjsN6lJ8vkIGqH7LU0xLU_5Q/viewform?embedded=true\" width=\"");
    bld.append(wh[0]);
    bld.append("\" height=\"");
    bld.append(Util.getFullScreenHeight(getActivity()));
    bld.append("frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\">Loading...</iframe>");
    wv.loadData(bld.toString(), "text/Html", "UTF-8");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_feedback, container, false);
  }
}
