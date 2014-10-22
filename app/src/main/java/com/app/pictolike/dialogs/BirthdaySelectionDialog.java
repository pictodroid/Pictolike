package com.app.pictolike.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.DatePicker;

import com.app.pictolike.R;

import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by Alexandr on 10/22/2014.
 * Dialog let user to select birthdate
 */
public class BirthdaySelectionDialog extends DialogFragment {

    private final Calendar mSelectedDate;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(final DatePicker pDatePicker, final int year, final int month, final int day) {
            //set selected date to field and send notification to subscribers
            mSelectedDate.set(year,month,day);
            EventBus.getDefault().post(BirthdaySelectionDialog.this);

        }
    };;

    public BirthdaySelectionDialog() {
        mSelectedDate = Calendar.getInstance();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.AppTheme_Dialog, mOnDateSetListener, mSelectedDate.get(Calendar.YEAR), mSelectedDate.get(Calendar.MONTH), mSelectedDate.get(Calendar.DAY_OF_MONTH));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2004);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        return dialog;
    }

    public Date getSelectedDate() {
        return mSelectedDate.getTime();
    }

    public void setInitialDate(final Calendar pBirthDay) {
        mSelectedDate.setTime(pBirthDay.getTime());
    }
}
