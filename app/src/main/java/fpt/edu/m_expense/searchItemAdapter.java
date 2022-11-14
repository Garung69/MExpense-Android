package fpt.edu.m_expense;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class searchItemAdapter extends ArrayAdapter<searchItem> {

    private List<searchItem> mListItem;

    public searchItemAdapter(@NonNull Context context, int resource, @NonNull List<searchItem> objects) {
        super(context, resource, objects);
        mListItem = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        }

        TextView textSearch = convertView.findViewById(R.id.result_ForSearch);
        LinearLayout tripId = convertView.findViewById(R.id.a123);
        searchItem data = getItem(position);
        if(data.getTripId() != null && tripId != null){
            tripId.setId(Integer.valueOf(data.getTripId()));
        }
        textSearch.setText(data.getTextDisplay());



        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<searchItem> mListSugggest = new ArrayList<>();
                if(constraint == null || constraint.length() == 0){
                    mListSugggest.addAll(mListItem);
                }
                else {
                    String filter = constraint.toString().toLowerCase().trim();
                    for (searchItem items : mListItem){
                        if(items.getTextSearching().toLowerCase().contains(filter)){
                            mListSugggest.add(items);
                        }
                    }
                }
                FilterResults  results = new FilterResults();
                results.values = mListSugggest;
                results.count = mListSugggest.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<searchItem>)results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((searchItem) resultValue).getTextSearching();
            }
        };
    }
}
