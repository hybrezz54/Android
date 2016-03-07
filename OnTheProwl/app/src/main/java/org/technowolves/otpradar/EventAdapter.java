package org.technowolves.otpradar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private List<Event> mEvents;

    public EventAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        mEvents = events;
    }

    @Override
    public int getPosition(Event item) {
        return mEvents.indexOf(item);
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Event getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);

        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        eventName.setText(event.getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Event) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null) {
                    ArrayList<Event> suggestions = new ArrayList<>();
                    for (Event event : mEvents) {
                        if (event.getName().toLowerCase()
                                .contains(constraint.toString().toLowerCase())) {
                            suggestions.add(event);
                        }
                    }

                    results.values = suggestions;
                    results.count = suggestions.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    clear();
                    ArrayList<Event> filteredList = (ArrayList) results.values;
                    addAll(filteredList);
                } else {
                    addAll(mEvents);
                }
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
