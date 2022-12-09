package com.example.notes;

import static com.example.notes.NoteFragment.PDateTime;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.notes.Adapter.SectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ParentDateTimeFragment extends DialogFragment {


    static final String SELECTED_NOTE = "note";
    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;
    private Note note;

    public ParentDateTimeFragment() {
        // Required empty public constructor
    }


    public static ParentDateTimeFragment newInstance(Note note) {
        ParentDateTimeFragment fragment = new ParentDateTimeFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            requireActivity().getSupportFragmentManager().popBackStack();
    }
    //Call onActivity Create method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myFragment = inflater.inflate(R.layout.fragment_parent_date_time,
                container, false);
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        datePickerFragment = DatePickerFragment.newInstance(note);
        timePickerFragment = TimePickerFragment.newInstance(note);
        adapter.addFragment(datePickerFragment, "Сменить дату");
        adapter.addFragment(timePickerFragment, "Сменить время");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle arguments = getArguments();
        if (arguments != null) {

            note = arguments.getParcelable(SELECTED_NOTE);
        }
        PDateTime = 1;
        Button Parent_button_back = view.findViewById(R.id.parent_button_back);
        if (Parent_button_back != null)
            Parent_button_back.setOnClickListener(view1 -> {
                PDateTime = 0;
                dismiss();
            });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {

        Fragment CurrentFragment = requireActivity().getSupportFragmentManager().findFragmentByTag("PARENT_FRAGMENT");

        if ((!isLandscape()) && (menu != null) &&
                (CurrentFragment != null && CurrentFragment.isVisible())) {
            menu.clear();
            inflater.inflate(R.menu.parent_data_time_menu, menu);
        }
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }
}