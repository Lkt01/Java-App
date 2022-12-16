package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cw.androidcw1.Adapter.Adapter_Expenses;
import com.cw.androidcw1.Database.database_Expenses;
import com.cw.androidcw1.Model.Expenses;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityAddExpensesBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class AddExpensesActivity extends AppCompatActivity {
    private String[] expenseType = {"Food", "Travel", "Accommodation", "Other"};//mảng chứa các loại chi tiêu khác nhau để hiển thị lên spinner để người dùng chọn
    ActivityAddExpensesBinding addExpensesBinding;
    private database_Expenses database_expenes;
    private int tripID;
    private List<Expenses> expensesList;
    private Adapter_Expenses adapter_expenses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addExpensesBinding = ActivityAddExpensesBinding.inflate(getLayoutInflater());
        setContentView(addExpensesBinding.getRoot());
        tripID = getIntent().getIntExtra("TripID", 0);//lấy dữ liệu tripID từ intent gửi qua
        expensesList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseType);//khởi tạo adapter để hiển thị các loại chi tiêu lên spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//set layout cho spinner khi mở ra
        addExpensesBinding.spnExpenseType.setAdapter(adapter);//gán adapter cho spinner để hiển thị các loại chi tiêu lên spinner
        addExpensesBinding.spnExpenseType.setSelection(0);//set vị trí mặc định cho spinner là vị trí đầu tiên
        addExpensesBinding.edtExpenseDate.setOnClickListener(new View.OnClickListener() {//gán sự kiện click cho edit text ngày chi tiêu
            @Override
            public void onClick(View view) {
                //sử dụng thư viện MaterialDatePicker để hiển thị dialog chọn ngày chi tiêu(thêm vào thư viện trong theme của project với tên là material)
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();//khởi tạo builder để hiển thị dialog chọn ngày chi tiêu
                builder.setTitleText("Select date");
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    //convert date to string
                    String myFormat = "dd/MM/yyyy";// định dạng ngày tháng năm
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);// định dạng ngày tháng năm
                    addExpensesBinding.edtExpenseDate.setText(sdf.format(selection));//set text cho edit text ngày chi tiêu
                });
            }
        });
        addExpensesBinding.btnAddExpense.setOnClickListener(new View.OnClickListener() {//gán sự kiện click cho button thêm chi tiêu
            @Override
            public void onClick(View view) {
                if (addExpensesBinding.edtExpenseAmount.getText().toString().isEmpty()) {//nếu edit text số tiền chi tiêu rỗng thì hiển thị thông báo
                    addExpensesBinding.edtExpenseAmount.setError("Please enter expense amount");//hiển thị thông báo lỗi cho edit text số tiền chi tiêu
                    return;//thoát khỏi phương thức
                }
                if (addExpensesBinding.edtExpenseDate.getText().toString().isEmpty()) {
                    addExpensesBinding.edtExpenseDate.setError("Please enter expense description");
                    return;
                }
                Expenses expenses = new Expenses();//khởi tạo đối tượng expenses
                expenses.setTripId(tripID);//gán id chuyến đi cho đối tượng expenses
                expenses.setType(addExpensesBinding.spnExpenseType.getSelectedItem().toString());//gán loại chi tiêu cho đối tượng expenses
                expenses.setAmount(Double.parseDouble(addExpensesBinding.edtExpenseAmount.getText().toString()));//gán số tiền chi tiêu cho đối tượng expenses
                expenses.setTime(addExpensesBinding.edtExpenseDate.getText().toString());//gán ngày chi tiêu cho đối tượng expenses
                database_expenes = new database_Expenses(AddExpensesActivity.this);//khởi tạo database
                if(database_expenes.addExpense(expenses)){//nếu thêm chi tiêu thành công thì hiển thị thông báo
                    //hiển thị thông báo thêm chi tiêu thành công và đóng dialog(bằng thư viện Toasty với tên là toasty)
                    Toasty.success(getApplicationContext(), "Added", Toasty.LENGTH_SHORT).show();
                }
                else{//nếu thêm chi tiêu thất bại thì hiển thị thông báo
                    Toasty.error(getApplicationContext(), "Failed", Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }
}