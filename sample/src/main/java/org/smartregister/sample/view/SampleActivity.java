package org.smartregister.sample.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.smartregister.reporting.contract.ReportIndicatorGeneratorContract;
import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.sample.R;
import org.smartregister.sample.presenter.SamplePresenter;

public class SampleActivity extends AppCompatActivity implements ReportIndicatorGeneratorContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);
        setUpViews();
        initializeReportIndicators();
    }

    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
        }

        // Adapter that will return a fragment for a section of the Activity
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void initializeReportIndicators() {

        SamplePresenter presenter = new SamplePresenter(this);

        // TODO :: Clean up this

        // Numeric Indicator
        ReportIndicator indicator = new ReportIndicator();
        indicator.setKey("CHW-001");
        indicator.setDescription("Total U5 children");

        String indicatorQuery = "select count(*) from event";
        IndicatorQuery chw001Query = new IndicatorQuery();
        chw001Query.setIndicatorCode("CHW-001");
        chw001Query.setQuery(indicatorQuery);


        // PieChart Indicator

        presenter.initialiseIndicator(indicator);
        presenter.initialiseIndicatorQuery(chw001Query);
    }

    @Override
    public void refreshUI() {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DashboardFragment.newInstance();
                case 1:
                    return ResourcesFragment.newInstance();
                default:
                    return ResourcesFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
