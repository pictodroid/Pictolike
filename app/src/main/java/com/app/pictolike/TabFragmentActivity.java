package com.app.pictolike;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class TabFragmentActivity extends AbstractAppActivity {

    private ActionBar.Tab cameraTab;
    // private ActionBar.Tab settingsTab;

    private CameraScreenFragment cameraScreen = new CameraScreenFragment();

    /* **************************************************************** */
    /* ******************* AbstractAppActivity ************************ */
    /* **************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.top_bar));
        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);
        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);
        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        cameraTab = actionBar.newTab().setIcon(R.drawable.ic_camera_tab);
        // settingsTab = actionBar.newTab().setIcon(R.drawable.ic_settings_tab);

        // Set Tab Listeners
        cameraTab.setTabListener(new TabListener(cameraScreen));
        // settingsTab.setTabListener(new TabListener(settingsScreen));

        // Add tabs to actionbar
        actionBar.addTab(cameraTab);
        // actionBar.addTab(settingsTab);
    }

    /* **************************************************************** */
    /* ************************ Utility API *************************** */
    /* **************************************************************** */

    public class TabListener implements ActionBar.TabListener {

        private android.app.Fragment fragment;

        public TabListener(android.app.Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    }
}
