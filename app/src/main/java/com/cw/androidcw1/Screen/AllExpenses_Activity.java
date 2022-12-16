package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cw.androidcw1.Adapter.Adapter_Expenses;
import com.cw.androidcw1.Database.database_Expenses;
import com.cw.androidcw1.Model.Expenses;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityAllExpensesBinding;
import com.cw.androidcw1.databinding.DialogAddExpensesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class AllExpenses_Activity extends AppCompatActivity {
    private ActivityAllExpensesBinding binding;//khởi tạo biến binding để ánh xạ các view trong layout activity_all_expenses
    private int tripID;
    private database_Expenses database_expenes;
    private Adapter_Expenses adapter_expenses;
    private List<Expenses> expensesList;
    private String[] expenseType = {"Food", "Travel", "Accommodation", "Other"};//mảng chứa các loại chi tiêu khác nhau để hiển thị lên spinner để người dùng chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllExpensesBinding.inflate(getLayoutInflater());//gán biến binding với layout activity_all_expenses bằng phương thức inflate
        setContentView(binding.getRoot());//set view cho activity bằng phương thức getRoot() của biến binding
        tripID = getIntent().getIntExtra("TripID", 0);//lấy dữ liệu tripID từ intent gửi qua
        expensesList = new ArrayList<>();//khởi tạo mảng expensesList để chứa các đối tượng Expenses
        binding.fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllExpenses_Activity.this, AddExpensesActivity.class);//khởi tạo intent để chuyển sang màn hình AddExpenses_Activity
                intent.putExtra("TripID", tripID);//gửi dữ liệu tripID qua intent
                startActivity(intent);//chuyển sang màn hình AddExpenses_Activity
            }
        });//gán sự kiện click cho floating action button để mở dialog thêm chi tiêu
        binding.deleteAllExpenses.setOnClickListener(v -> deleteAllExpenses());//gán sự kiện click cho button xóa tất cả các chi tiêu


    }

    private void deleteAllExpenses() {//phương thức xóa tất cả các chi tiêu
        database_expenes = new database_Expenses(this);//khởi tạo database
        //sử dụng thư viện SweetAlert để hiển thị dialog xác nhận xóa tất cả các chi tiêu
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);//khởi tạo dialog cảnh báo với loại là cảnh báo (WARNING_TYPE) để xác nhận xóa tất cả các chi tiêu
        sweetAlertDialog.setTitleText("Delete all expenses")
                .setContentText("Are you sure you want to delete all expenses?")
                .setConfirmText("Delete")
                .setCancelText("Cancel")
                .setConfirmClickListener(sweetAlertDialog1 -> {//nếu người dùng chọn xóa thì thực hiện xóa tất cả các chi tiêu
                    database_expenes.deleteAllExpenses(tripID);//gọi phương thức deleteAllExpenses() của database để xóa tất cả các chi tiêu
                    sweetAlertDialog1.dismissWithAnimation();//đóng dialog
                    //sử dụng thư viện Toasty để hiển thị thông báo xóa thành công tất cả các chi tiêu
                    Toasty.success(this, "Successfully!", Toasty.LENGTH_SHORT).show();//hiển thị thông báo xóa thành công
                    finish();
                })
                .setCancelClickListener(sweetAlertDialog1 -> sweetAlertDialog1.dismissWithAnimation())//nếu người dùng chọn hủy thì đóng dialog
                .show();
    }


    //getdata
    private void getData() {//phương thức lấy dữ liệu chi tiêu từ database và hiển thị lên recyclerview
        //khởi tạo database
        database_expenes = new database_Expenses(this);
        expensesList = database_expenes.getAllExpenses(tripID);//lấy dữ liệu chi tiêu từ database
        adapter_expenses = new Adapter_Expenses(this, expensesList);//khởi tạo adapter
        binding.rvExpenses.setAdapter(adapter_expenses);
        binding.rvExpenses.setLayoutManager(new LinearLayoutManager(this));//set layout cho recyclerview
    }
    @Override
    protected void onResume() {//phương thức được gọi khi activity được khởi động
        super.onResume();
        getData();//gọi phương thức getData() để lấy dữ liệu chi tiêu từ database
    }
}