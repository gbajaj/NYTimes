package codepath.gauravbajaj.com.nytimes.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.data.UserPreferences;

/**
 * Created by gauravb on 3/15/17.
 */


public class SettingsDialogFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = SettingsDialogFragment.class.getSimpleName();
    private static final String DATE_FRAGMENT_TAG = "DatePickerFragment";
    @BindView(R.id.settings_dialog_tvDatePicker)
    TextView tvDate;
    @BindView(R.id.settings_dialog_tvSave)
    TextView btnSave;
    @BindView(R.id.settings_dialog_sortorder_spinner)
    Spinner sortOrderSpinner;
    @BindView(R.id.settings_dialog_chk_arts)
    CheckBox newDeskValuesChkBoxArts;
    @BindView(R.id.settings_dialog_chk_fashion)
    CheckBox newDeskValuesChkBoxFashion;
    @BindView(R.id.settings_dialog_chk_sports)
    CheckBox newDeskValuesChkBoxSports;
    UserPreferences userPreferences = new UserPreferences();
    private Date newsBeginDate = null;
    private boolean enableArtSearch = false;
    private boolean enableFashionSearch = false;
    private boolean enableSportsSearch = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_dialog_fragment_layout, container, false);
        getDialog().setTitle("Simple Dialog");
        ButterKnife.bind(this, rootView);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                final DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(SettingsDialogFragment.this, 300);
                datePickerFragment.show(fm, DATE_FRAGMENT_TAG);
                Log.d(TAG, "Show Date Fragment");
            }
        });
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_order_array, R.layout.spinner_item1);
        sortOrderSpinner.setAdapter(adapter);
        setSpinnerToValue(sortOrderSpinner, userPreferences.getSortOrder());

        //init arts with saved preferences
        enableArtSearch = userPreferences.isArtNewsEnabled();
        newDeskValuesChkBoxArts.setChecked(enableArtSearch);

        //init fashion with saved preferences
        enableFashionSearch = userPreferences.isFashionNewsEnabled();
        newDeskValuesChkBoxFashion.setChecked(enableFashionSearch);

        //init sports with saved preferences
        enableSportsSearch = userPreferences.isSportsNewsEnabled();
        newDeskValuesChkBoxSports.setChecked(enableSportsSearch);

        //init begin date with saved preferences or set to default
        long savedDate = userPreferences.getSearchBeginDate();
        if (savedDate == -1) {
            newsBeginDate = defaultBeginDate();
        } else {
            newsBeginDate = new Date(savedDate);
        }
        final SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        final String dateStr = format1.format(newsBeginDate);
        tvDate.setText(dateStr);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch sort order from spinner
                String value = sortOrderSpinner.getSelectedItem().toString();
                Log.d(TAG, "News Sort Order " + value);
                //save spinner value
                userPreferences.setSortOrder(value);

                //save begin date
                userPreferences.setBeginDate(newsBeginDate.getTime());
                Log.d(TAG, "Date Saved " + newsBeginDate);

                //save news desk values
                userPreferences.setArtNewsEnable(enableArtSearch);
                userPreferences.setFashionNewsEnable(enableFashionSearch);
                userPreferences.setSportsNewsEnable(enableSportsSearch);
                userPreferences.commit();
                SettingsDialogFragment.this.dismiss();
            }
        });
        newDeskValuesChkBoxArts.setOnCheckedChangeListener(checkListener);
        newDeskValuesChkBoxFashion.setOnCheckedChangeListener(checkListener);
        newDeskValuesChkBoxSports.setOnCheckedChangeListener(checkListener);
        return rootView;
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }
    // Defines a listener for every time a checkbox is checked or unchecked
    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean checked) {
            // compoundButton is the checkbox
            // boolean is whether or not checkbox is checked
            // Check which checkbox was clicked
            String key = "";
            switch (view.getId()) {
                case R.id.settings_dialog_chk_arts:
                    enableArtSearch = checked;
                    break;
                case R.id.settings_dialog_chk_fashion:
                    enableFashionSearch = checked;
                    break;
                case R.id.settings_dialog_chk_sports:
                    enableSportsSearch = checked;
                    break;

            }
        }
    };

    // This is called when the dialog is completed and the results have been passed
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        Date date = c.getTime();
        Log.d(TAG, "Date Selected " + date);
        newsBeginDate = date;
        final String dateStr = format1.format(newsBeginDate);
        tvDate.setText(dateStr);
    }

    private Date defaultBeginDate() {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }
}
