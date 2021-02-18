package com.hilbing.arkanhelp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hilbing.arkanhelp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {
    NavController navController;

    @BindView(R.id.test_btn)
    AppCompatButton test_btn;
    @BindView(R.id.alert_btn)
    AppCompatButton alert_btn;
    @BindView(R.id.help_btn)
    AppCompatButton help_btn;
    @BindView(R.id.activate_messages_swt)
    SwitchCompat activate_messages_swt;
    Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        ButterKnife.bind(this, view);
        activate_messages_swt.setChecked(getFromSharedPreferences("activate_messages"));
        activate_messages_swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveInSharedPreferences("activate_messages", b);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private boolean getFromSharedPreferences(String key){

        SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getBoolean(key, false);
    }

    private void saveInSharedPreferences(String key, boolean value){
        SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


}