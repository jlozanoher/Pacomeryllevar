package com.bonaro.restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bonaro.restaurant.Models.Carta;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivityComs {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FragmentManager mFragmentManager;

    private boolean mIsSimSupport;
    private boolean mInviteFriends = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TextView tx = (TextView)findViewById(R.id.txtTitle);
//        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/CASBANTN.TTF");
//        tx.setTypeface(custom_font);

        //gets the current TelephonyManager
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mIsSimSupport = !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mFragmentManager = getSupportFragmentManager();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
//        mTabLayout.setupWithViewPager(mViewPager, true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        outState.putInt("value",1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Read SharedPreferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        mInviteFriends = sharedPref.getBoolean(getString(R.string.invite_friends), false);
        int language = sharedPref.getInt(getString(R.string.shared_language), 1);

        Controller.getInstance().createDatabase(this, language);
        updateSectionAdapter();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemInvite = menu.findItem(R.id.action_invite);
        MenuItem menuItemMaecenas = menu.findItem(R.id.action_earn_money);
        menuItemInvite.setVisible(mInviteFriends && mIsSimSupport);
        menuItemMaecenas.setVisible(mIsSimSupport);
        menuItemMaecenas.setVisible(false); // Todo: habilitate  maecenas
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_settings){
            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.show(mFragmentManager, "123");
            return true;
        }

        if(id == R.id.action_location){
            double latitude = 23.13718;
            double longitude = -82.38688;
            String label = "ABC Label";
            String uriBegin = "geo:" + latitude + "," + longitude;
            String query = latitude + "," + longitude + "(" + label + ")";
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_earn_money) {
            MaecenasFragment maecenasDialog = new MaecenasFragment();
            maecenasDialog.show(mFragmentManager, "123");
            return true;
        }

        if (id == R.id.action_invite) {
            // Sms intent
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.putExtra("sms_body", getString(R.string.invitation));
            smsIntent.setData(Uri.parse("smsto:"));
            try{
                startActivity(smsIntent);
            } catch (Exception e){
                Log.d("SMS error", "inviting friends");
            }

//            smsIntent.setType("vnd.android-dir/mms-sms");
//            smsIntent.putExtra("address","your desired phoneNumber");
            return true;
        }

        if (id == R.id.action_aboutUs) {
            AboutusFragment aboutusFragment = new AboutusFragment();
            aboutusFragment.show(mFragmentManager, "123");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateSectionAdapter(){
        invalidateOptionsMenu();
        mSectionsPagerAdapter = new SectionsPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    @Override
    public void updateData() {
        mSectionsPagerAdapter.updateData();
    }

    @Override
    public void enableInviteFriends() {
        mInviteFriends = true;

        // Write SharedPreferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.invite_friends), true);
        editor.apply();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements SectionInterface{

        int mTotalCartas;
        List<Carta> mCartaList;
        Fragment[] mSectionFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mCartaList = Controller.getInstance().getCartaList();
            mTotalCartas = mCartaList.size();
            mSectionFragments = new Fragment[mTotalCartas];
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a SectionFragment
            if(mSectionFragments[position] == null) {
                mSectionFragments[position] = SectionFragment.newInstance(position);
            }

            return mSectionFragments[position];
//            return SectionFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show n + 1 total pages.
            return mTotalCartas;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCartaList.get(position).getNombre();
        }

        @Override
        public void updateLanguage(){
            for(Fragment sectionFragment : mSectionFragments){
                if(sectionFragment != null)
                    ((SectionInterface) sectionFragment).updateLanguage();
            }
        }

        @Override
        public void updateData() {
            Log.d("MainActivity", "updateData()");
            for(Fragment sectionFragment : mSectionFragments){
                if(sectionFragment != null)
                    ((SectionInterface) sectionFragment).updateData();
            }
        }
    }
}
