package com.spielpark.steve.bernieapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.Issue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleIssueFragment extends Fragment {
  private static Issue mIssue;

  public static SingleIssueFragment newInstance(Issue i) {
    mIssue = i;
    return new SingleIssueFragment();
  }

  @Override public void onPause() {
    super.onPause();
    ((WebView) getView().findViewById(R.id.i_video)).onPause();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (mIssue == null) {
      //User exited the app and returned to it, but android cleared some stuff from memory...
      getActivity().getSupportFragmentManager()
          .popBackStack("base", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    final View root = getView();
    ((TextView) root.findViewById(R.id.i_txtTitle)).setText(mIssue.getTitle());
    ((TextView) root.findViewById(R.id.i_txtTitle)).setShadowLayer(13, 0, 0, Color.BLACK);
    ((TextView) root.findViewById(R.id.i_txtDate)).setText("Published " + mIssue.getPubDate());
    ((TextView) root.findViewById(R.id.i_txtDesc)).setText(Html.fromHtml(mIssue.getDesc()));
    ((TextView) root.findViewById(R.id.i_txtDesc)).setMovementMethod(new LinkMovementMethod());
    ((WebView) root.findViewById(R.id.i_video)).getSettings().setJavaScriptEnabled(true);
    ((WebView) root.findViewById(R.id.i_video)).loadData(mIssue.getEmbedURL(getActivity()),
        "text/Html", "UTF-8");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frag_single_issue, container, false);
  }
}
