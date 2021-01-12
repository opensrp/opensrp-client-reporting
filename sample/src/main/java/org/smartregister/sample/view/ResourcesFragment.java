package org.smartregister.sample.view;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.smartregister.reporting.view.ProgressIndicator;
import org.smartregister.reporting.view.TableView;
import org.smartregister.sample.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourcesFragment extends Fragment implements View.OnClickListener {

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

        ProgressIndicator progressWidget = getActivity().findViewById(R.id.progressIndicatorView);
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

        //tableView.setTableData(Arrays.asList(new String[]{"Vaccine Name", "Gender", "Value"}), getDummyData());
        //Overloads the above
        tableView.setTableData(Arrays.asList(new String[]{"Vaccine Name", "Gender", "Value"}), getDummyData(), getDummyDataIds(), this);
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

    private List<String> getDummyDataIds() {

        List<String> list = new ArrayList<>();

        /// Table


        for (Integer i = 0; i < 10; i++) {
            list.add(UUID.randomUUID().toString());
        }

        return list;

    }

    private int getRange(int min, int max) {

        return random.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onClick(View view) {
        //Do something here with the ID eg launch a profile activity
        Toast.makeText(this.getActivity(), String.format("clicked on %s", String.valueOf(view.getTag(R.id.table_row_id))), Toast.LENGTH_LONG).show();

    }
}
