package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.cw.androidcw1.Adapter.Adapter_Trips;
import com.cw.androidcw1.Database.database_Trips;
import com.cw.androidcw1.Model.Trips;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityMainBinding;
import com.cw.androidcw1.databinding.DialogAddTripsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class AllTrips_Activity extends AppCompatActivity {
    private ActivityMainBinding binding;// ánh xạ view binding cho activity main layout (activity_main.xml)
    private database_Trips database_trips;// khai báo database
    private Trips trips;
    private Adapter_Trips adapter_trips;
    private List<Trips> tripsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());// ánh xạ view binding cho activity main layout (activity_main.xml)
        setContentView(binding.getRoot());// set content view cho activity main layout (activity_main.xml)
        binding.rvTrip.setLayoutManager(new LinearLayoutManager(this));// thiết lập layout manager cho recycler view

        binding.deleteAllTrips.setOnClickListener(v->deleteAllTrips());// sự kiện click vào button delete all trips
        database_trips = new database_Trips(this);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return false;
            }
        });
    }

    private void deleteAllTrips() {// hàm xóa tất cả các trips trong database
        //dialog delete all trips
        //sử dụng thư viện sweet alert dialog để hiển thị dialog xác nhận xóa tất cả các trips
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)//sử dụng thư viện sweetalert để hiển thị dialog
                .setTitleText("Delete All?")//tiêu đề
                .setContentText("It Will Be Deleted. You Can't Undo This Action!")//nội dung
                .setConfirmText("Delete It")//nút xác nhận
                .setConfirmClickListener(sweetAlertDialog -> {//sự kiện khi click vào nút xác nhận
                    database_trips.deleteAllTrips();//xóa tất cả các trips
                    tripsList.clear();//xóa list trips
                    adapter_trips.notifyDataSetChanged();//cập nhật lại adapter
                    sweetAlertDialog.dismissWithAnimation();//đóng dialog
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)//nút hủy và sự kiện khi click vào nút hủy
                .show();
    }


    //getdata
    public void getData(){
        tripsList.clear();
        tripsList = database_trips.getAllTrips();//lấy dữ liệu từ database
        if(tripsList.size() > 0){//nếu dữ liệu lấy được từ database lớn hơn 0
            Log.d("tesst",tripsList.get(0).getName());
            adapter_trips = new Adapter_Trips(this,tripsList);//khởi tạo adapter trips
            binding.rvTrip.setAdapter(adapter_trips);//set adapter cho recycler view trip
        }else{
            adapter_trips = new Adapter_Trips(this,tripsList);
            binding.rvTrip.setAdapter(adapter_trips);
        }
    }
    private void search(String s){
        List<Trips> tripsList = database_trips.search(s);//lấy dữ liệu từ database
        if(tripsList.size() > 0){//nếu dữ liệu lấy được từ database lớn hơn 0
            adapter_trips = new Adapter_Trips(this,tripsList);//khởi tạo adapter trips
            binding.rvTrip.setAdapter(adapter_trips);//set adapter cho recycler view trip
        }
    }
    @Override
    protected void onResume() {//sự kiện khi activity được khởi động
        super.onResume();
        getData();//lấy dữ liệu từ database
    }

    public void addTrip(View view) {
        startActivity(new Intent(AllTrips_Activity.this, AddActivity.class));
    }
}