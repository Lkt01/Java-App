package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.cw.androidcw1.Database.database_Expenses;
import com.cw.androidcw1.Model.Expenses;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityExpensesDetailsBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpensesDetails_Activity extends AppCompatActivity {

    private ActivityExpensesDetailsBinding binding;// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml)
    private Expenses expenses;
    private database_Expenses database_expenes;
    private String[] expenseType = {"Food", "Travel", "Accommodation", "Other"};//mảng chứa các loại chi tiêu khác nhau để hiển thị lên spinner để người dùng chọn
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpensesDetailsBinding.inflate(getLayoutInflater());// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml)
        setContentView(binding.getRoot());// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml)
        //khởi tạo database
        database_expenes = new database_Expenses(this);
        //set adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// set adapter cho spinner
        binding.spnExpenseTypeEdit.setAdapter(adapter);// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml)
        expenses = (Expenses) getIntent().getSerializableExtra("Expenses");// lấy expense từ intent gửi qua và ép kiểu về Expenses (lấy từ Expenses_Activity)
        if(expenses!=null){// nếu expense khác null thì hiển thị thông tin chi tiêu lên các view trong layout
            binding.edtExpenseAmountEdit.setText(String.valueOf(expenses.getAmount()));
            binding.edtExpenseDateEdit.setText(expenses.getTime());
            binding.spnExpenseTypeEdit.setSelection(adapter.getPosition(expenses.getType()));
        }
        binding.btnExpenseEdit.setOnClickListener(v->addExpenses());// set sự kiện cho nút edit expense
        binding.deleteExpensesDetails.setOnClickListener(v->deleteExpenses());// set sự kiện cho nút delete expense
    }

    private void deleteExpenses() {
        //khởi tạo dialog
        //sử dụng thư viện SweetAlertDialog để hiển thị dialog xác nhận xóa expense
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Delete Expense")
                .setContentText("It Will Be Deleted. You Can't Undo This Action!")
                .setConfirmText("Yes, Delete It!")
                .setCancelText("No, Cancel")
                .setConfirmClickListener(sweetAlertDialog1 -> {
                    database_expenes.deleteExpense(expenses.getId());
                    sweetAlertDialog1.dismissWithAnimation();
                    finish();
                })
                .setCancelClickListener(sweetAlertDialog1 -> sweetAlertDialog1.dismissWithAnimation())
                .show();
    }

    private void addExpenses() {// hàm thêm expense vào database
        //check null
        if(binding.edtExpenseAmountEdit.getText().toString().isEmpty()){
            binding.edtExpenseAmountEdit.setError("Please enter amount");
            return;
        }
        if(binding.edtExpenseDateEdit.getText().toString().isEmpty()){
            binding.edtExpenseDateEdit.setError("Please enter date");
            return;
        }
        //get data
        Expenses expense = new Expenses();// khởi tạo expense mới để thêm vào database
        expense.setAmount(Double.parseDouble(binding.edtExpenseAmountEdit.getText().toString()));// set amount cho expense mới
        expense.setTime(binding.edtExpenseDateEdit.getText().toString());
        expense.setType(binding.spnExpenseTypeEdit.getSelectedItem().toString());
        expense.setTripId(expenses.getTripId());
        expense.setId(expenses.getId());
        //add to database
        if(database_expenes.updateExpense(expense)){// nếu thêm thành công thì hiển thị dialog thông báo và finish activity
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Successfully")
                    .show();
        }

    }

}