package org.smartregister.sample.view;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.reporting.view.ProgressIndicatorView;
import org.smartregister.reporting.view.TableView;
import org.smartregister.sample.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourcesFragment extends Fragment {

    Random random = new Random();

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
        Integer valA = getRange(14, 99);
        progressWidget.setProgress(valA);
        progressWidget.setTitle(valA + "%");
        progressWidget.setSubTitle("Sprayed");

        Integer valB = getRange(14, 99);
        progressWidget = getActivity().findViewById(R.id.progressIndicatorView2);
        progressWidget.setProgress(valB);
        progressWidget.setTitle(valB + "%");
        progressWidget.setSubTitle("Funikad");
        progressWidget.setProgressBarForegroundColor(ContextCompat.getColor(getContext(), R.color.pnc_circle_red));
        progressWidget.setProgressBarBackgroundColor(ContextCompat.getColor(getContext(), R.color.pnc_circle_yellow));


        TableView tableView = getActivity().findViewById(R.id.tableView);
        tableView.setTableData(Arrays.asList(new String[]{"Vaccine Name", "Gender", "Value"}), getDummyData());
        tableView.setHeaderTextStyle(Typeface.BOLD);
        tableView.setRowBorderHidden(false);

    }

    private List<String> getDummyData() {

        List<String> list = new ArrayList<>();

        /// Table


        for (Integer i = 0; i < 10; i++) {
            list.add((i < 3 ? "BCG " : i > 6 ? "OPV " : "PENTA ") + i);
            list.add(getRange(1, 2) == 2 ? "Female" : "Male");
            list.add(String.valueOf(getRange(500, 3000)));
        }

        return list;

    }

    private int getRange(int min, int max) {

        return random.nextInt((max - min) + 1) + min;
    }

}
