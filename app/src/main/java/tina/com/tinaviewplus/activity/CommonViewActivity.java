package tina.com.tinaviewplus.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import tina.com.tinaviewplus.R;
import tina.com.tinaviewplus.fragment.PageFragment;

public class CommonViewActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    List<PageModel> pageModels = new ArrayList<>();
    {
        pageModels.add(new PageModel(R.layout.fragment_avatar_view, R.string.title_avatar_view));
        pageModels.add(new PageModel(R.layout.fragment_dashboard, R.string.title_dashboard_view));
        pageModels.add(new PageModel(R.layout.fragment_pie_chart, R.string.title_piechart_view));
        pageModels.add(new PageModel(R.layout.fragment_circle_view, R.string.title_circle_view));
        pageModels.add(new PageModel(R.layout.fragment_scaleable_view, R.string.title_scaleable_view));
        pageModels.add(new PageModel(R.layout.fragment_squarel_view, R.string.title_squarel_view));
        pageModels.add(new PageModel(R.layout.fragment_sports_view, R.string.title_sports_view));
        pageModels.add(new PageModel(R.layout.fragment_watch_view, R.string.title_watch_view));
        pageModels.add(new PageModel(R.layout.fragment_flowlayout_view, R.string.title_flowlayout_view));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_view_activity);

        pager = findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);
                return PageFragment.newInstance(pageModel.sampleLayoutRes);
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(pageModels.get(position).titleRes);
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    private class PageModel {
        @LayoutRes
        int sampleLayoutRes;
        @StringRes
        int titleRes;

        PageModel(@LayoutRes int sampleLayoutRes, @StringRes int titleRes) {
            this.sampleLayoutRes = sampleLayoutRes;
            this.titleRes = titleRes;
        }
    }

}
