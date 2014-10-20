package com.app.pictolike;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class TabFragmentActivity extends AbstractAppActivity {

    private ActionBar.Tab cameraTab;
    // private ActionBar.Tab settingsTab;

    private CameraScreenFragment cameraScreen = new CameraScreenFragment();
    private HomePageActivity homeScreen = new HomePageActivity(this);
    private SettingsScreenActivity settingsScreen = new SettingsScreenActivity();
    private ProfileScreenActivity profileScreen = new ProfileScreenActivity();
    private Tab homeTab;
    private Tab profileTab;
    private Tab settingsTab;
    /* **************************************************************** */
    /* ******************* AbstractAppActivity ************************ */
    /* **************************************************************** */
    private ViewPager mViewPager;
    private class TabsAdapter extends FragmentPagerAdapter{

        public TabsAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            switch (position){
                case 0:
                    return homeScreen;
                case 1:
                    return cameraScreen;
                case 2:
                    return profileScreen;
                case 3:
                    return settingsScreen;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.top_bar));
        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);
        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);
        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        homeTab = actionBar.newTab().setIcon(R.drawable.ic_home_tab);
        cameraTab = actionBar.newTab().setIcon(R.drawable.ic_camera_tab);
        profileTab = actionBar.newTab().setIcon(R.drawable.ic_profile_tab);
        settingsTab = actionBar.newTab().setIcon(R.drawable.ic_settings_tab);

        // Set Tab Listeners
        homeTab.setTabListener(new TabListener(homeScreen,0));
        cameraTab.setTabListener(new TabListener(cameraScreen,1));
        profileTab.setTabListener(new TabListener(profileScreen,2));
        settingsTab.setTabListener(new TabListener(settingsScreen,3));

        // Add tabs to actionbar
        actionBar.addTab(homeTab);
        actionBar.addTab(cameraTab);
        actionBar.addTab(profileTab);
        actionBar.addTab(settingsTab);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int i, final float v, final int i2) {

            }

            @Override
            public void onPageSelected(final int i) {
                Tab selectedTab = getSelectedTab(i);
                actionBar.selectTab(selectedTab);
            }

            private Tab getSelectedTab(final int pI) {
                switch (pI){
                    case 0:
                        return homeTab;
                    case 1:
                        return cameraTab;
                    case 2:
                        return profileTab;
                    case 3:
                        return settingsTab;
                }
                return null;
            }

            @Override
            public void onPageScrollStateChanged(final int i) {

            }
        });
        mViewPager.setAdapter(new TabsAdapter(getFragmentManager()));
    }

    /* **************************************************************** */
    /* ************************ Utility API *************************** */
    /* **************************************************************** */

    public class TabListener implements ActionBar.TabListener {

        private android.app.Fragment fragment;
        private int mIndex;

        public TabListener(android.app.Fragment fragment,int index) {
            this.fragment = fragment;
            mIndex = index;
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
//            ft.replace(R.id.fragment_container, fragment);
            if (mViewPager!=null) {
                mViewPager.setCurrentItem(mIndex);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//            ft.remove(fragment);
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    }
}
