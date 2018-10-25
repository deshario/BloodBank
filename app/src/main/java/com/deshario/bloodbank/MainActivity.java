package com.deshario.bloodbank;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.deshario.bloodbank.Configs.ConnectionReceiver;
import com.deshario.bloodbank.Configs.Deshario_Functions;
import com.deshario.bloodbank.Fragments.BloodRequest_Frag;
import com.deshario.bloodbank.Fragments.BranchRequest_Frag;
import com.deshario.bloodbank.Fragments.Campaign_Frag;
import com.deshario.bloodbank.Fragments.Dashboard_Frag;
import com.deshario.bloodbank.Fragments.MoreMenu_Frag;
import com.deshario.bloodbank.Helper.SQLiteHandler;
import com.deshario.bloodbank.Helper.SessionManager;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    String TAG = null;
    private SQLiteHandler db;
    private SessionManager session;
    private String user_phone = null;
    private String user_id = null;
    static Animation anim_down, anim_up;
    public static AHBottomNavigation bottomNavigation;
    private ConnectionReceiver connectionReceiver;
    AlertDialog alertDialog;
    private DrawerLayout drawerLayout;
    private final int LOCATION_PERMISSION = 78;

    private static final String TAG_DASHBOARD_FRAG = "dashboard";
    public static final String TAG_CREATE_REQ_FRAG = "blood_request_create";
    private static final String TAG_BLOOD_REQ_FRAG = "blood_request";
    private static final String TAG_CAMPAIGN_FRAG = "campaign";
    private static final String TAG_BRANCH_REQ_FRAG = "branch_requests";
    private static final String TAG_NO_CONN = "no_connection";
    private static final String TAG_MOREMENU_FRAG = "more_menu";

    public static final String TAG_DONATION_FRAG = "donation_history";
    public static final String TAG_REQUEST_FRAG = "request_history";

    public static final String TAG_PLAN_DONATION = "plan_donation";
    public static final String TAG_VERIFICATION_FRAG = "verification";
    public static final String TAG_USAGE_TAB = "usage_tab";
    public static final String TAG_CAMPAIGN_VIEW_FRAG = "campaign_view";
    public static final String TAG_BRANCH_REQ_VIEW_FRAG = "branch_view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        db = new SQLiteHandler(getApplicationContext()); // DB MANAGER
        session = new SessionManager(getApplicationContext()); // SESSION MANAGER
        setSupportActionBar(toolbar);


//        FrameLayout mContainer = (FrameLayout) findViewById(R.id.frame_container);
//
//        CoordinatorLayout.LayoutParams params =
//                (CoordinatorLayout.LayoutParams) mContainer.getLayoutParams();
//        params.setBehavior(null);
//        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
//        mContainer.requestLayout();

        broadCasting();
        //manageLogin();
        if (savedInstanceState == null) { // It is the first time being called
            if (amIConnected()) {
                showHome(new Dashboard_Frag());
            } else {
                DisconnectedCallback();
            }
        }
        initNav();
        // initNavigationDrawer();
    }

    public void initNav() {
        //int[] tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_menu);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.new_material_red));  // Set background color
        bottomNavigation.setBehaviorTranslationEnabled(false); // Disable the translation inside the CoordinatorLayout

        // Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

        bottomNavigation.setAccentColor(getResources().getColor(R.color.yellow)); // Change colors
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.fbwhite));
        //bottomNavigation.setForceTint(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Fragment mFragment = null;
                switch (position) {
                    case 0:
                        if (wasSelected == true) {
                            mFragment = null;
                        } else {
                            mFragment = new Dashboard_Frag();
                            TAG = TAG_DASHBOARD_FRAG;
                        }
                        break;
                    case 1:
                        if (wasSelected == true) {
                            mFragment = null;
                        } else {
                            mFragment = new BloodRequest_Frag();
                            TAG = TAG_BLOOD_REQ_FRAG;
                        }
                        break;
                    case 2:
                        if (wasSelected == true) {
                            mFragment = null;
                        } else {
                            LocationPermission();
                        }
                        break;
                    case 3:
                        if (wasSelected == true) {
                            mFragment = null;
                        } else {
                            mFragment = new Campaign_Frag();
                            TAG = TAG_CAMPAIGN_FRAG;
                        }
                        break;
                    case 4:
                        if (wasSelected == true) {
                            mFragment = null;
                            //Toast.makeText(MainActivity.this,"Already Sel",Toast.LENGTH_SHORT).show();
                        } else {
                            mFragment = new MoreMenu_Frag();
                            TAG = TAG_MOREMENU_FRAG;
                        }
                        break;
                    default:
                }
                if (TAG != null)
                    changefrag(mFragment, TAG);
                return true;
            }
        });
        navigationAdapter.setupWithBottomNavigation(bottomNavigation);
    }

    public void initNavigationDrawer() {
//        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                int id = menuItem.getItemId();
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });
//        View header = navigationView.getHeaderView(0);
//        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
//        tv_email.setText("sd.deshario@linuxmail.org");
//        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
    }

    private void showHome(Fragment fragment) {
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(TAG_DASHBOARD_FRAG);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment != null) {
            fragmentTransaction.remove(homeFragment);
        }
        fragmentTransaction.replace(R.id.frame_container, fragment, TAG_DASHBOARD_FRAG);
        fragmentTransaction.commit();
    }

    private void changefrag(Fragment fragment, String TAG) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (fragment != new MoreMenu_Frag()) {
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
            }
            // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in,android.R.anim.fade_out);
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                Deshario_Functions.showToast(MainActivity.this, "BACKSTAK > 1");
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragmentTransaction.replace(R.id.frame_container, fragment, TAG);
            //fragmentManager.addOnBackStackChangedListener(this);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void broadCasting() {
        connectionReceiver = new ConnectionReceiver(MainActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    private boolean amIConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void DisconnectedCallback() {
        System.out.println("No Internet Connection");
        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    public void ConnectedCallback(String param) {
        //System.out.println(param);
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
            if (currentFragment == null) {
                showHome(new Dashboard_Frag());
            } else { // load previous fragment
                changefrag(currentFragment, currentFragment.getTag());
                Toast.makeText(MainActivity.this, "Connected to Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void hidebottomnav() {
        //bottomNavigation.hideBottomNavigation(true);
        final View view = bottomNavigation;
        view.setVisibility(View.GONE);
        //view.startAnimation(anim_down);
    }

    public static void restoreBottomNav() {
        //bottomNavigation.restoreBottomNavigation(true);
        final View view = bottomNavigation;
        if (bottomNavigation.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            //view.startAnimation(anim_up);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //resumeFrag();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        //if(getSupportFragmentManager().findFragmentByTag(TAG) != null){
        if (currentFragment != null) {
            String CTAG = currentFragment.getTag();
            System.out.println("CTAG :: " + CTAG);
            if (CTAG != TAG_DASHBOARD_FRAG) {
                //if (CTAG == TAG_CREATE_REQ_FRAG || CTAG == TAG_CAMPAIGN_VIEW_FRAG || CTAG == TAG_VERIFICATION_FRAG || CTAG == TAG_USAGE_TAB || CTAG == TAG_PLAN_DONATION || TAG.equals(TAG_BRANCH_VIEW)){
                if (CTAG == TAG_CREATE_REQ_FRAG || CTAG == TAG_CAMPAIGN_VIEW_FRAG || CTAG == TAG_VERIFICATION_FRAG || CTAG == TAG_BRANCH_REQ_VIEW_FRAG || CTAG == TAG_USAGE_TAB || CTAG == TAG_PLAN_DONATION) {
                    getSupportFragmentManager().popBackStack();
                    overridePendingTransition(0, 0);
                    restoreBottomNav();
                    System.out.println("Inner");
                } else {
                    System.out.println("Outter");
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    showHome(new Dashboard_Frag());
                    bottomNavigation.setCurrentItem(0);
                }
            } else { // Finish
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void LocationPermission() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (result == PackageManager.PERMISSION_GRANTED) { // result = 0
               location_granted();
            } else { // result = -1
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            }
        } else {
           location_granted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { // MAIN OUTTER PERMISSION
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Granted
                    location_granted();
                } else { // Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void location_granted() {
        Fragment mFragment = new BranchRequest_Frag();
        TAG = TAG_BRANCH_REQ_FRAG;
        changefrag(mFragment, TAG);
    }

}
