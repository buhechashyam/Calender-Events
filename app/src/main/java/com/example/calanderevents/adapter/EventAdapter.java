package com.example.calanderevents.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calanderevents.view.EventActivity;
import com.example.calanderevents.R;
import com.example.calanderevents.model.Event;
import com.example.calanderevents.viewmodel.EventViewModel;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    Context context;
    List<Event> mListEvents;
    EventViewModel viewModel;

    public EventAdapter(Context context, List<Event> mListEvents, EventViewModel viewModel) {
        this.context = context;
        this.mListEvents = mListEvents;
        this.viewModel = viewModel;
    }

    public void addList(List<Event> mList){
        this.mListEvents = mList;
        notifyDataSetChanged();
    }

    public void removeAllItems(){
        mListEvents.clear();
        notifyDataSetChanged();
    }
    public void addItem(Event event){
        mListEvents.add(event);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event,parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Event event = mListEvents.get(position);
        holder.eventCategory.setText("Category : "   + event.getEventType());
        holder.eventTime.setText(event.getEventTime());
        String date = "Date : " + event.getEventDate().getDay() + "/" + event.getEventDate().getMonth() + "/" + event.getEventDate().getYear();
        holder.eventDate.setText(date);
        holder.eventTitle.setText( event.getEventName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("id",event.getId());
                intent.putExtra("name",event.getEventName());
                intent.putExtra("desc",event.getEventDesc());
                String date = event.getEventDate().getDay() + "/" + event.getEventDate().getMonth() + "/" + event.getEventDate().getYear();

                intent.putExtra("date",event.getEventDate());
                intent.putExtra("time",event.getEventTime());
                intent.putExtra("type",event.getEventType());
                intent.putExtra("color",event.getColor());
                intent.putExtra("span",event.getSpanNum());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListEvents.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDate, eventTime, eventCategory;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventTitle = itemView.findViewById(R.id.text_event_title);
            eventDate = itemView.findViewById(R.id.text_event_date);
            eventTime = itemView.findViewById(R.id.text_event_time);
            eventCategory = itemView.findViewById(R.id.text_event_type);
        }
    }
}
