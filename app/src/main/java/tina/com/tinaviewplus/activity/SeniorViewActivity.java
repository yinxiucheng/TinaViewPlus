package tina.com.tinaviewplus.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import tina.com.tinaviewplus.R;
import tina.com.tinaviewplus.fragment.PageFragment;

public class SeniorViewActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    List<PageModel> pageModels = new ArrayList<>();
    {
        pageModels.add(new PageModel(R.layout.fragment_matrial_edit_view, R.string.title_matrial_edittext));
        pageModels.add(new PageModel(R.layout.fragment_flowlayout_view, R.string.title_flowlayout_view));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_view_activity);

        pager = findViewById(R.id.pager);
        pager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
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

   public class MyFragmentPageAdapter extends FragmentPagerAdapter{

       public MyFragmentPageAdapter(FragmentManager fm) {
           super(fm);
       }

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
   }

}
