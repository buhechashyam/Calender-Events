package com.example.calanderevents.view;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.calanderevents.R;
import com.example.calanderevents.adapter.EventAdapter;
import com.example.calanderevents.calendar.EventDecorate;
import com.example.calanderevents.databinding.ActivityMainBinding;
import com.example.calanderevents.model.Event;
import com.example.calanderevents.room.AppDatabase;
import com.example.calanderevents.viewmodel.EventViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String time = "";
    String eventCategory = "";

    String[] mListEventsCategory;

    int[] mListColors = new int[]{Color.RED, Color.BLACK, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.LTGRAY};

    EventViewModel viewModel;
    EventAdapter adapter;
    int selectedPosition = 0;
    int selectedColor = 0;

    int[] spans = {0, 1, 2, 3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mListEventsCategory = getApplicationContext().getResources().getStringArray(R.array.event_list);

        binding.recyclerviewEvents.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        adapter = new EventAdapter(MainActivity.this, new ArrayList<Event>() {
        }, viewModel);

        //set category spinner
        mListEventsCategory[0] = "All";
        ArrayAdapter adapter2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mListEventsCategory);
        ;
        adapter2.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        binding.spnSortEvent.setAdapter(adapter2);

        binding.spnSortEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.removeAllItems();
                binding.spnSortEvent.setSelection(position);

                List<Event> mSortedList = getEventByCategory(mListEventsCategory[position]);

                List<Event> mNew = new ArrayList<>();

                if (position == 0) {
                    mListEventsCategory[0] = "All";
                }
                binding.calenderview.removeDecorators();
                for (int i = 0; i < mSortedList.size(); i++) {
                    Event event = mSortedList.get(i);
                    mNew.add(event);

                    binding.calenderview.addDecorator(new EventDecorate(event.getEventName(), event.getEventTime(), event.getColor(), event.getEventDate(), event.getSpanNum()));

                }
                adapter.removeAllItems();
                adapter.addList(mNew);
                adapter.notifyDataSetChanged();

                binding.recyclerviewEvents.setAdapter(adapter);
                Log.d("MAIN", String.valueOf(mNew.size()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (selectedPosition == 0) {
            viewModel.getAllEvents().observe(MainActivity.this, new Observer<List<Event>>() {
                @Override
                public void onChanged(List<Event> events) {
                    //           binding.recyclerviewEvents.setAdapter(new EventAdapter(MainActivity.this, events, viewModel));

                }
            });
        }

        binding.calenderview.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                binding.spnSortEvent.setSelection(0);
                int c = checkMultipleEventAtSameDay(date);

                Log.d("MAIN", String.valueOf(c));
                addEventToCalendar(date, c);
            }
        });
    }

    private void addEventToCalendar(CalendarDay date, int dotNum) {

        View view = LayoutInflater.from(this).inflate(R.layout.item_set_event, null);
        TextInputEditText etEventTitle = view.findViewById(R.id.et_event_title);
        TextInputEditText etEventDesc = view.findViewById(R.id.et_event_desc);
        Spinner spinner = view.findViewById(R.id.spn_category);
        TimePicker timePicker = view.findViewById(R.id.time_picker_event);
        //New Add two buttons
        MaterialButton mButtonCancel = view.findViewById(R.id.btn_cancel);
        MaterialButton mButtonAddEvent = view.findViewById(R.id.btn_add_event);

        mListEventsCategory[0] = "Select Event Category";
        ArrayAdapter adapter2 = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, mListEventsCategory);
        adapter2.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventCategory = mListEventsCategory[position];
                selectedColor = position;
                if (position != 0) {
                    mButtonAddEvent.setEnabled(true);
                    mButtonAddEvent.setBackgroundColor(getColor(R.color.blue));
                } else {
                    mButtonAddEvent.setEnabled(false);
                    mButtonAddEvent.setBackgroundColor(Color.GRAY);
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

        mButtonAddEvent.setEnabled(false);
        mButtonAddEvent.setBackgroundColor(Color.GRAY);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (spinner.getSelectedItemPosition() != 0 && etEventTitle.getText().toString().length() > 0 && etEventDesc.getText().toString().length() > 0) {
                    mButtonAddEvent.setEnabled(true);
                    mButtonAddEvent.setBackgroundColor(getColor(R.color.blue));

                } else {
                    mButtonAddEvent.setEnabled(false);
                    mButtonAddEvent.setBackgroundColor(Color.GRAY);
                    Toast.makeText(MainActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etEventTitle.addTextChangedListener(textWatcher);
        etEventDesc.addTextChangedListener(textWatcher);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mButtonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MAIN", "Clicked");
//                int color = mListColors.get(selectedColor);
                int color = mListColors[spinner.getSelectedItemPosition()];

                Event event = new Event(etEventTitle.getText().toString(), etEventDesc.getText().toString(), date, time, eventCategory, color, spans[dotNum]);

                adapter.addItem(event);
                adapter.notifyDataSetChanged();

                EventDecorate decorate = new EventDecorate(etEventTitle.getText().toString(), time, color, date, spans[dotNum]);
                Log.d("MAIN", "M-" + spans[dotNum]);
                binding.calenderview.addDecorator(decorate);

                viewModel.insert(event);
                dialog.dismiss();

            }
        });
        dialog.create();
        dialog.show();
    }

    public List<Event> getEventByCategory(String eventCategory) {

        List<Event> nListAllEvents;
        List<Event> nListSortedEvent = new ArrayList<>();

        nListAllEvents = AppDatabase.getINSTANCE(getApplication()).eventDao().getAllEvents();

        if (binding.spnSortEvent.getSelectedItemPosition() == 0) {
            nListSortedEvent.addAll(nListAllEvents);
        } else {
            for (int i = 0; i < nListAllEvents.size(); i++) {
                Event event = nListAllEvents.get(i);
                if (event.getEventType().equals(eventCategory)) {
                    nListSortedEvent.add(event);
                }
            }
        }

        return nListSortedEvent;
    }

    public int checkMultipleEventAtSameDay(CalendarDay day) {
        List<Event> mListEvent = AppDatabase.getINSTANCE(getApplication()).eventDao().getAllEvents();
        int c = 0;
        for (Event event : mListEvent) {
            if (event.getEventDate().equals(day)) {
                c++;
            }
        }
        return c;
    }
}