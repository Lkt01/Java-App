package com.cw.androidcw1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.androidcw1.Model.Trips;
import com.cw.androidcw1.R;
import com.cw.androidcw1.Screen.TripsDetails_Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_Trips extends RecyclerView.Adapter<Adapter_Trips.ViewHolder> {
    //xây dựng adapter cho recyclerview trips để hiển thị danh sách các trips
    // khai báo các biến
    private Context context;
    private List<Trips> tripsList;

    // khởi tạo adapter và truyền vào context và list trips
    public Adapter_Trips(Context context, List<Trips> tripsList) {
        this.context = context;
        this.tripsList = tripsList;

    }

    // khởi tạo viewholder và truyền vào layout item trips
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trips, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // gán dữ liệu cho các view trong item trips
        Trips trips = tripsList.get(position);// lấy trips tại vị trí position
        holder.bind(trips, position);// gọi hàm bind để gán dữ liệu cho các view trong item trips
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public void search(ArrayList<Trips> tripsList) {
        // hàm search để tìm kiếm trips theo tên
        this.tripsList = tripsList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {// khởi tạo viewholder
        // khai báo các view trong item trips
        private TextView tvstt,tvName, tvDestination, tvDate, tvRequireAssessment;
        // khởi tạo viewholder và truyền vào view item trips
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_trips_item);
            tvDestination = itemView.findViewById(R.id.tv_Destination_trips_item);
            tvDate = itemView.findViewById(R.id.tv_date_trips_item);
            tvRequireAssessment = itemView.findViewById(R.id.tv_Assessement_trips_item);
            tvstt = itemView.findViewById(R.id.tv_stt_trips_item);
        }
        // hàm bind để gán dữ liệu cho các view trong item trips
        public void bind(Trips trips, int position) {// truyền vào trips và vị trí của trips trong list
            tvstt.setText(String.valueOf(position+1));
            tvName.setText(trips.getName());
            tvDestination.setText(trips.getDestination());
            tvDate.setText(trips.getDate());
            tvRequireAssessment.setText("Require Assessment: "+trips.getRequireAssessement());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // khi click vào item trips thì sẽ chuyển sang màn hình trips details
                    Intent intent = new Intent(context, TripsDetails_Activity.class);// khởi tạo intent
                    intent.putExtra("Trip", trips);
                    context.startActivity(intent);
                }
            });
        }
    }
}
