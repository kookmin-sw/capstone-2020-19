package capstone.kookmin.silverwatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BatteryAdapter extends RecyclerView.Adapter<BatteryAdapter.ViewHolder> implements OnBatteryItemClickListener{
    ArrayList<battery_info> items = new ArrayList<battery_info>();
    OnBatteryItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.battery_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    public void setOnItemClickListener(OnBatteryItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        battery_info item  = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(battery_info item) {
        items.add(item);
    }

    public void setItems(ArrayList<battery_info> items) {
        this.items = items;
    }

    public battery_info getItem(int position) {
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        public ViewHolder(@NonNull View itemView, final OnBatteryItemClickListener listener) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(battery_info item) {
            textView.setText(item.name);
            textView2.setText(item.watch_battery);
        }
    }
}
