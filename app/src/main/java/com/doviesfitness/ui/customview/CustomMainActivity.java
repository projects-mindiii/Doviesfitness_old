package com.doviesfitness.ui.customview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import com.doviesfitness.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


/**
 * @author TheLittleNaruto
 *         This is simple base calss of actvity which use to display posts and photos
 *         MainActivity#NavigationView  use for dispalying navigation drawer menu
 */
public class CustomMainActivity extends AppCompatActivity {

    public static final String POST_FRAGMENT = "post_fragment";
    public static final String PHOTO_FRAGMENT = "photo_fragment";
  //  private DrawerLayout mDrawerLayout;
  //  private ActionBarDrawerToggle toggle;
    private TabLayout tabLayout;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_container_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarCollapse);
        toolbarLayout.setTitle(getString(R.string.dovies_tv));
        //NavigationView introduces in android support design lib please refer
        // http://android-developers.blogspot.in/2015/05/android-design-support-library.html
      //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
      //  navigationView.setBackgroundColor(getResources().getColor(R.color.app_background_color));
      //  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
      //  mDrawerLayout.setDrawerListener(toggle);
      //  setupDrawerContent(navigationView);
        //By default add post fragment
        getSupportActionBar().setTitle(getString(R.string.dovies_tv));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        ViewPager mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                      //  mDrawerLayout.closeDrawers();
                        menuItem.setChecked(true);
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        return true;
                    }
                });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
       // toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
      //  toggle.onConfigurationChanged(newConfig);
    }


    public static class MyAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_ITEMS = 4;

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " +( position + 1);
        }
    }

    public static class ArrayListFragment extends ListFragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num + 1);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);

            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);

            TextView textView = new TextView(getActivity());
            textView.setPadding(16, 16, 16, 16);
            textView.setText("Fragment#" + mNum);
            textView.setLayoutParams(params);
            getListView().addHeaderView(textView);
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }
}
