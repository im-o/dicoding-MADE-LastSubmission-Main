package com.stimednp.aplikasimoviecataloguesub4.myfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.stimednp.aplikasimoviecataloguesub4.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavFavoritesFragment extends Fragment implements SearchView.OnQueryTextListener {
    Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public NavFavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager_favorite);
        setupViewPager();
        tabLayout = view.findViewById(R.id.tab_layout_favorite);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcon();
        setHasOptionsMenu(true);
    }

    private void setTabIcon() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    //add fragment to tab
    private void setupViewPager() {
        String movie = getResources().getString(R.string.name_tab1_movies);
        String tvshow = getResources().getString(R.string.name_tab2_tvshow);
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new FavMoviesFragment(), movie);
        adapter.addFragment(new FavTvShowFragment(), tvshow);
        viewPager.setAdapter(adapter);
    }

    private int[] tabIcons = { //set icon to tab
            R.drawable.ic_movie_white_24dp,
            R.drawable.ic_live_tv_black_24dp
    };

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchView = (SearchView) menu.findItem(R.id.itemm_search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getContext(), "Favorite : "+query, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
