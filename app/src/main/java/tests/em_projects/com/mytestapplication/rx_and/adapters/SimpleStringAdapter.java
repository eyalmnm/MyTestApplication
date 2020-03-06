package tests.em_projects.com.mytestapplication.rx_and.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tests.em_projects.com.mytestapplication.R;

public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.ViewHolder> {

    private Context context;
    private List<String> stringList = new ArrayList<>();

    public SimpleStringAdapter(Context context) {
        this.context = context;
    }

    public void setStrings(List<String> newStrings) {
        stringList.clear();
        stringList.addAll(newStrings);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rx_string_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.colorTextView.setText(stringList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, stringList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView colorTextView;

        public ViewHolder(View view) {
            super(view);
            colorTextView = view.findViewById(R.id.color_display);
        }
    }
}
