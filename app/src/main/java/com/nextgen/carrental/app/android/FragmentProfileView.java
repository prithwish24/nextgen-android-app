package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.nextgen.carrental.app.R;

/**
 * Display User Profile
 * @author  Prithwish
 */

public class FragmentProfileView extends Fragment implements View.OnClickListener {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_view, container, false);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().hide();
        }

        Spinner spinnerCarTypes = view.findViewById(R.id.spinnerCarType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource (
                getActivity(), R.array.car_type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarTypes.setAdapter(adapter);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View saveButton = view.findViewById(R.id.buttonSave);
        final View cancelButton = view.findViewById(R.id.buttonCancel);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSave) {
            Toast.makeText(getContext(), "Save Clicked", Toast.LENGTH_LONG).show();

        } else if (v.getId() == R.id.buttonCancel) {
            Toast.makeText(getContext(), "Cancel Clicked", Toast.LENGTH_SHORT).show();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentHome())
                .commit();

    }
}
