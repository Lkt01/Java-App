package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cw.androidcw1.Database.database_Trips;
import com.cw.androidcw1.Model.Trips;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityTripsDetailsBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class TripsDetails_Activity extends AppCompatActivity {
    private ActivityTripsDetailsBinding binding;// ánh xạ view binding cho activity trips details layout (activity_trips_details.xml)
    private database_Trips databaseTrips;// khai báo database
    private Trips trips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripsDetailsBinding.inflate(getLayoutInflater());// ánh xạ view binding cho activity trips details layout (activity_trips_details.xml)
        setContentView(binding.getRoot());// set content view cho activity trips details layout (activity_trips_details.xml)

        binding.btnTripEdit.setOnClickListener(v->editTrips());// sự kiện click vào button edit trip
        binding.deleteTrips.setOnClickListener(v->deleteTrip());// sự kiện click vào button delete trip
        binding.btnShowExpensesTripEdit.setOnClickListener(v->showExpenses());// sự kiện click vào button show expenses
        trips = (Trips) getIntent().getSerializableExtra("Trip");// lấy trips từ intent gửi qua và ép kiểu về Trips
        if(trips!=null){// nếu trips khác null thì set dữ liệu cho các view trong activity trips details layout (activity_trips_details.xml)
            binding.edtTripNameEdit.setText(trips.getName());
            binding.edtTripDestinationEdit.setText(trips.getDestination());
            binding.edtTripDateEdit.setText(trips.getDate());
            binding.edtTripDescriptionEdit.setText(trips.getDescription());
            binding.radioGroupAssessmentEdit.check(trips.getRequireAssessement().equals("Yes")?R.id.Yes_Edit:R.id.No_Edit);// set radio button theo dữ liệu trips đã lấy được từ intent gửi qua (Yes hoặc No)
        }
    }

    private void showExpenses() {// hàm show expenses
        Toasty.info(this,"Show Expenses",Toasty.LENGTH_SHORT).show();// hiển thị thông báo
        Intent intent = new Intent(this,AllExpenses_Activity.class);
        intent.putExtra("TripID",trips.getId());// gửi id của trips qua activity all expenses
        startActivity(intent);
    }

    private void deleteTrip() {// hàm delete trip
        // hiển thị dialog xác nhận xóa
        databaseTrips = new database_Trips(this);
        // hiển thị dialog xác nhận xóa
        //sử dụng thư viện sweet alert dialog để hiển thị dialog xác nhận xóa
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)//
                .setTitleText("Delete Trip?")
                .setContentText("It Will Be Deleted. You Can't Undo This Action!")
                .setConfirmText("Delete It")
                .setConfirmClickListener(sweetAlertDialog -> {// sự kiện click vào button yes
                    databaseTrips.deleteTrip(trips.getId());// xóa trips theo id
                    Toasty.success(this, "Deleted", Toasty.LENGTH_SHORT).show();// hiển thị thông báo
                    sweetAlertDialog.dismissWithAnimation();// đóng dialog
                    finish();// đóng activity
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    private void editTrips() {// hàm add trips
        //check null
        if (binding.edtTripNameEdit.getText().toString().isEmpty()){// nếu tên trips rỗng thì hiển thị thông báo
            binding.edtTripNameEdit.setError("Please enter trip name");
            return;
        }
        if (binding.edtTripDateEdit.getText().toString().isEmpty()){
            binding.edtTripDateEdit.setError("Please enter trip date");
            return;
        }
        if (binding.edtTripDestinationEdit.getText().toString().isEmpty()){
            binding.edtTripDestinationEdit.setError("Please enter trip destination");
            return;
        }
        if (binding.edtTripDescriptionEdit.getText().toString().isEmpty()){
            binding.edtTripDescriptionEdit.setError("Please enter trip description");
            return;
        }
        Trips trip = new Trips();// khai báo trips mới
        trip.setId(trips.getId());
        trip.setName(binding.edtTripNameEdit.getText().toString());
        trip.setDate(binding.edtTripDateEdit.getText().toString());
        trip.setDestination(binding.edtTripDestinationEdit.getText().toString());
        trip.setDescription(binding.edtTripDescriptionEdit.getText().toString());
        trip.setRequireAssessement(binding.radioGroupAssessmentEdit.getCheckedRadioButtonId() == R.id.Yes_Edit ? "Yes" : "No");// lấy dữ liệu từ radio button (Yes hoặc No) nếu checked radio button Yes thì set Yes nếu checked radio button No thì set No
        databaseTrips = new database_Trips(this);// khởi tạo database
        if(databaseTrips.updateTrip(trip)){// nếu update trips thành công thì hiển thị thông báo
            //success update
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success")
                    .setContentText("Successfully")
                    .show();
        }
    }
}