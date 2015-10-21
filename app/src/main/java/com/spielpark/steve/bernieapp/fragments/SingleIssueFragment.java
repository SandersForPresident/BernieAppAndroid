package com.spielpark.steve.bernieapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.Issue;

/**
 * A Fragment to display an Issue.
 */
public class SingleIssueFragment extends Fragment {
  public static final String ISSUE = "ISSUE";
  @Bind(R.id.i_txtTitle) TextView title;
  @Bind(R.id.i_txtDate) TextView date;
  @Bind(R.id.i_txtDesc) TextView description;
  @Bind(R.id.i_video) WebView webView;
  private Issue issue;

  public static SingleIssueFragment newInstance(Issue issue) {
    SingleIssueFragment fragment = new SingleIssueFragment();
    Bundle args = new Bundle();
    args.putParcelable(ISSUE, issue);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      issue = args.getParcelable(ISSUE);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_single_issue, container, false);
    ButterKnife.bind(this, view);

    title.setText(issue.getTitle());
    title.setShadowLayer(13, 0, 0, Color.BLACK);
    date.setText("Published " + issue.getPubDate());
    description.setText(Html.fromHtml(issue.getDesc()));
    description.setMovementMethod(new LinkMovementMethod());
    webView.getSettings().setJavaScriptEnabled(true);
    webView.loadData(issue.getEmbedURL(getActivity()), "text/Html", "UTF-8");

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onPause() {
    super.onPause();
    webView.onPause();
  }
}
