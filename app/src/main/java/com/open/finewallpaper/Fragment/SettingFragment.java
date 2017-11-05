package com.open.finewallpaper.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.open.finewallpaper.R;


/**
 * Created by SEELE on 2017/9/16.
 */

public class SettingFragment extends PreferenceFragment
        implements
        Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "SettingFragment";
    private static final String KEY_AUTO_UPDATA = "pref_auto_updata";
    private static final String KEY_ACCOUNT = "account";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.argument1);
        SharedPreferences sharePrf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnef = sharePrf.getString(KEY_AUTO_UPDATA,"");
        //Log.e(TAG, "onCreate: " + syncConnef );
        ListPreference ls = (ListPreference) findPreference(KEY_AUTO_UPDATA);
        Preference prAccount = findPreference(KEY_ACCOUNT);
        prAccount.setOnPreferenceClickListener(this);
        ls.setOnPreferenceChangeListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(KEY_AUTO_UPDATA)){


        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(KEY_ACCOUNT)){

        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e(TAG, "onSharedPreferenceChanged: " );
        if (key.equals(KEY_AUTO_UPDATA)){
            Preference pr = findPreference(key);
            setSummary(pr,sharedPreferences.getString(key,""));
        }
    }

    public void setSummary(Preference pr,String s){
        int idex = Integer.parseInt(s);
        String summary = this.getResources().getStringArray(R.array.auto_updata_entries)[idex];
        pr.setSummary(summary);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
