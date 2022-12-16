package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cw.androidcw1.Database.database_Trips;
import com.cw.androidcw1.Model.Trips;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityAddBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    private Trips trips;
    private database_Trips databaseTrips;// khai báo database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseTrips = new database_Trips(this);
        binding.edtTripDateAdd.setOnClickListener(v->{//sự kiện click vào edit text trip date
            //sử dụng thư viện material date picker để hiển thị dialog chọn ngày tháng năm (để sử dụng thư viện này cần thêm materialCalendarStyle vào theme.xml)
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();//khởi tạo builder để hiển thị date picker
            builder.setTitleText("Select a date");
            MaterialDatePicker materialDatePicker = builder.build();
            materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                //convert date to string
                String myFormat = "dd/MM/yyyy";// định dạng ngày tháng năm
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);// định dạng ngày tháng năm
                binding.edtTripDateAdd.setText(sdf.format(selection));
            });
        });
        //add trips to database
        binding.btnAddTripAdd.setOnClickListener(v->{//sự kiện click vào button add trip
            if(binding.edtTripNameAdd.getText().toString().isEmpty()){//kiểm tra dữ liệu nhập vào có rỗng hay không (nếu rỗng thì hiển thị thông báo)
                binding.edtTripNameAdd.setError("Please enter trip name");//hiển thị thông báo lỗi
            }else if(binding.edtTripDestinationAdd.getText().toString().isEmpty()){
                binding.edtTripDestinationAdd.setError("Please enter trip destination");
            }else if(binding.edtTripDescriptionAdd.getText().toString().isEmpty()){
                binding.edtTripDescriptionAdd.setError("Please enter trip description");
            }else if(binding.edtTripDateAdd.getText().toString().isEmpty()){
                binding.edtTripDateAdd.setError("Please enter trip date");
            }else{
                //insert data
                trips = new Trips();//khởi tạo trips
                trips.setName(binding.edtTripNameAdd.getText().toString());//lấy dữ liệu từ edit text trip name
                trips.setDestination(binding.edtTripDestinationAdd.getText().toString());
                trips.setDescription(binding.edtTripDescriptionAdd.getText().toString());
                trips.setDate(binding.edtTripDateAdd.getText().toString());
                trips.setRequireAssessement(binding.radioGroupAssessmentAdd.getCheckedRadioButtonId() == R.id.Yes ? "Yes" : "No");//lấy dữ liệu từ radio button assessment (nếu radio button yes được chọn thì lấy dữ liệu là yes, nếu radio button no được chọn thì lấy dữ liệu là no)
                if(databaseTrips.addTrip(trips)){//thêm dữ liệu vào database
                    //sử dụng thư viện toasty để hiển thị thông báo (để sử dụng thư viện này cần thêm toasty vào build.gradle)
                    Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
                    binding.edtTripNameAdd.setText("");//xóa dữ liệu trong edit text
                    binding.edtTripDestinationAdd.setText("");
                    binding.edtTripDescriptionAdd.setText("");
                    binding.edtTripDateAdd.setText("");
                }
                else {
                    Toasty.error(this, "Failed!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        binding.btnCancelTripAdd.setOnClickListener(
                v->{
                    finish();//đóng activity
                }
        );//sự kiện click vào button cancel

    }

    public void allTrip(View view) {
        Intent intent = new Intent(this, AllTrips_Activity.class);
        startActivity(intent);
    }
}