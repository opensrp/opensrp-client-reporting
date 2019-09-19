package org.smartregister.sample.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.reporting.view.ProgressIndicatorView;
import org.smartregister.sample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourcesFragment extends Fragment {


    public ResourcesFragment() {
        // Required empty public constructor
    }

    public static ResourcesFragment newInstance() {
        ResourcesFragment fragment = new ResourcesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressIndicatorView progressWidget = getActivity().findViewById(R.id.progressIndicatorView);
        progressWidget.setProgress(30);
        progressWidget.setTitle("30%");
        progressWidget.setSubTitle("Sprayed");

        progressWidget = getActivity().findViewById(R.id.progressIndicatorView2);
        progressWidget.setProgress(42);
        progressWidget.setTitle("42%");
        progressWidget.setSubTitle("Funikad");
        progressWidget.setProgressBarForegroundColor(ContextCompat.getColor(getContext(), R.color.pnc_circle_red));
        progressWidget.setProgressBarBackgroundColor(ContextCompat.getColor(getContext(), R.color.pnc_circle_yellow));
    }

}
