package fpt.edu.m_expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.m_expense.entities.trip;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder>{

    private Context mContext;
    private List<trip> mListTrip;

    public TripAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<trip> list){
        this.mListTrip = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new  TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        trip trips = mListTrip.get(position);
        if(trips == null){
            return;
        }
        holder.tripId.setId(trips.getId());
        holder.tripName.setText(trips.getName());
        holder.tripDate.setText(trips.getDate());
        holder.tripDestination.setText(trips.getDestinationFromId() + " - " + trips.getDestinationToId());
        holder.tripTotalCost.setText("Total cost: "+trips.getTotalCost()+"$");

    }

    @Override
    public int getItemCount() {
        if(mListTrip != null){
            return mListTrip.size();
        }
        return 0;
    }

    public class TripViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout tripId;
        private TextView tripName;
        private TextView tripDate;
        private TextView tripDestination;
        private TextView tripTotalCost;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripId = itemView.findViewById(R.id.id123);
            tripName = itemView.findViewById(R.id.item_trip_name);
            tripDate = itemView.findViewById(R.id.item_trip_date);
            tripDestination = itemView.findViewById(R.id.item_trip_from_to);
            tripTotalCost = itemView.findViewById(R.id.item_trip_total_cost);
        }
    }
}
