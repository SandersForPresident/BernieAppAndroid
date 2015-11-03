package com.spielpark.steve.bernieapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.actMainPage;
import com.spielpark.steve.bernieapp.tasks.IssuesTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class IssuesFragment extends Fragment {

    private static IssuesFragment mIntstance;

    public static IssuesFragment getInstance() {
        if (mIntstance == null) {
            mIntstance = new IssuesFragment();
            return mIntstance;
        } else {
            return mIntstance;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView newsList = (ListView) view.findViewById(R.id.listIssues);
        new IssuesTask(getActivity(), newsList, (ProgressBar) view.findViewById(R.id.progressBar)).execute();
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((actMainPage) getActivity()).loadIssue(IssuesTask.getIssue(position));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_issues, container, false);
    }

}
