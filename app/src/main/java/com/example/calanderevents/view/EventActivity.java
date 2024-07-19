package com.example.calanderevents.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.calanderevents.R;
import com.example.calanderevents.databinding.ActivityEventBinding;
import com.example.calanderevents.model.Event;
import com.example.calanderevents.viewmodel.EventViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    ActivityEventBinding binding;
    String eventName, eventDesc, eventTime, eventType;
    int spanNum;
    int eventColor;
    int id;
    int selectedColor = 0;

    CalendarDay eventDate;
    String eventCategory = "";
    String time = "";
    String[] mListEventsCategory;
    EventViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mListEventsCategory = getResources().getStringArray(R.array.event_list);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventActivity.this, MainActivity.class));
            }
        });

        viewModel = new ViewModelProvider(EventActivity.this).get(EventViewModel.class);

        eventName = getIntent().getStringExtra("name");
        id = getIntent().getIntExtra("id", 0);
        Log.d("MAIN", String.valueOf(id));
        eventDesc = getIntent().getStringExtra("desc");
        eventDate = getIntent().getParcelableExtra("date");
        eventTime = getIntent().getStringExtra("time");
        time = eventTime;
        eventType = getIntent().getStringExtra("type");
        eventColor = getIntent().getIntExtra("color", 0);
        spanNum = getIntent().getIntExtra("span", 0);

        String date = eventDate.getDay() + "/" + eventDate.getMonth() + "/" + eventDate.getYear();

        binding.textEventName.setText(String.format("Event Name : %s", eventName));
        binding.textEventDesc.setText(String.format("Description : %s", eventDesc));
        binding.textEventDate.setText(String.format("Date : %s", date));
        binding.textEventType.setText(String.format("Type : %s", eventType));
        binding.textEventTime.setText(String.format("Time : %s", eventTime));

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEventToCalendar(id, new Event(eventName, eventDesc, eventDate, eventTime, eventType, eventColor, spanNum));
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.delete(id);
                startActivity(new Intent(EventActivity.this, MainActivity.class));
            }
        });

        binding.btnAddToGoogleCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(eventDate.getDate());
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar)
                        .putExtra(CalendarContract.Events.TITLE, eventName)
                        .putExtra(CalendarContract.Events.DESCRIPTION, eventDesc)
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(intent);
            }
        });
    }

    private void updateEventToCalendar(int id, Event event) {

        View view = LayoutInflater.from(EventActivity.this).inflate(R.layout.item_set_event, null);
        TextInputEditText etEventTitle = view.findViewById(R.id.et_event_title);
        TextInputEditText etEventDesc = view.findViewById(R.id.et_event_desc);
        Spinner spinner = view.findViewById(R.id.spn_category);
        TimePicker timePicker = view.findViewById(R.id.time_picker_event);

        //New Add two buttons
        MaterialButton mButtonCancel = view.findViewById(R.id.btn_cancel);
        MaterialButton mButtonUpdateEvent = view.findViewById(R.id.btn_add_event);

        mButtonUpdateEvent.setText("Update Event");

        etEventDesc.setText(event.getEventDesc());
        etEventTitle.setText(event.getEventName());


        ArrayAdapter adapter = new ArrayAdapter<>(EventActivity.this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, mListEventsCategory);
        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int currentEvent = getEventPosition(eventType);
        spinner.setSelection(currentEvent);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                eventCategory = mListEventsCategory.get(position);

                eventCategory = mListEventsCategory[position];
                selectedColor = position;
                if (position != 0) {
                    mButtonUpdateEvent.setEnabled(true);
                    mButtonUpdateEvent.setBackgroundColor(getColor(R.color.blue));
                } else {
                    mButtonUpdateEvent.setEnabled(false);
                    mButtonUpdateEvent.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay <= 12) {
                    time = hourOfDay + " : " + minute + " AM";
                } else {
                    time = (hourOfDay - 12) + " : " + minute + " PM";
                }
            }
        });


        mButtonUpdateEvent.setEnabled(false);
        mButtonUpdateEvent.setBackgroundColor(Color.GRAY);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (spinner.getSelectedItemPosition() != 0 && etEventTitle.getText().toString().length() > 0 && etEventDesc.getText().toString().length() > 0) {
                    mButtonUpdateEvent.setEnabled(true);
                    mButtonUpdateEvent.setBackgroundColor(getColor(R.color.blue));

                } else {
                    mButtonUpdateEvent.setEnabled(false);
                    mButtonUpdateEvent.setBackgroundColor(Color.GRAY);
                    Toast.makeText(EventActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etEventTitle.addTextChangedListener(textWatcher);
        etEventDesc.addTextChangedListener(textWatcher);

        AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
        builder.setCancelable(false);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        mButtonUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventCategory == "Select Event Category" || etEventTitle.getText().toString().isEmpty() || etEventDesc.getText().toString().isEmpty()) {
                    Toast.makeText(EventActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    eventName = etEventTitle.getText().toString();
                    eventDesc = etEventDesc.getText().toString();

                    viewModel.update(id, etEventTitle.getText().toString(), etEventDesc.getText().toString(), time, eventCategory);
                    binding.textEventName.setText(String.format("Event Name : %s", eventName));
                    binding.textEventDesc.setText(String.format("Description : %s", eventDesc));
                    binding.textEventTime.setText(String.format("Time : %s", time));
                    String date = "Date : " + event.getEventDate().getDay() + "/" + event.getEventDate().getMonth() + "/" + event.getEventDate().getYear();
                    binding.textEventDate.setText(String.format("Date : %s", date));
                    binding.textEventType.setText(String.format("Type : %s", eventCategory));
                    dialog.dismiss();
                }
            }
        });


        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();

    }

    public int getEventPosition(String event) {
        for (int i = 0; i < mListEventsCategory.length; i++) {
            if (event.equals(mListEventsCategory[i])) {
                return i;
            }
        }
        return 3;
    }

}