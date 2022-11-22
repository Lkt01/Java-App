package com.cw.androidcw1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.androidcw1.Model.Expenses;
import com.cw.androidcw1.R;
import com.cw.androidcw1.Screen.ExpensesDetails_Activity;

import java.util.List;

public class Adapter_Expenses extends RecyclerView.Adapter<Adapter_Expenses.ViewHolder> {
    //xây dựng adapter cho recyclerview expenses để hiển thị danh sách các expenses
    // khai báo các biến
    private Context context;
    private List<Expenses> expensesList;

    // khởi tạo adapter và truyền vào context và list expenses
    public Adapter_Expenses(Context context, List<Expenses> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // khởi tạo viewholder và truyền vào layout item expenses
        View view = LayoutInflater.from(context).inflate(R.layout.item_one_exp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // gán dữ liệu cho các view trong item expenses
        Expenses expenses = expensesList.get(position);// lấy expenses tại vị trí position
        holder.tvstt.setText(String.valueOf(position + 1));
        holder.tvType.setText(expenses.getType());
        holder.tvAmount.setText(String.valueOf(expenses.getAmount()));
        holder.tvTime.setText(expenses.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khi click vào item expenses thì sẽ chuyển sang màn hình chi tiết expenses
                Intent intent = new Intent(context, ExpensesDetails_Activity.class);// khởi tạo intent
                intent.putExtra("Expenses", expenses);// truyền expenses vào intent
                context.startActivity(intent);// chuyển sang màn hình chi tiết expenses
            }
        });
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // khai báo các view trong item expenses
        private TextView tvType, tvAmount, tvTime,tvstt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_name_trip_exp);
            tvAmount = itemView.findViewById(R.id.tv_destination_exp);
            tvTime = itemView.findViewById(R.id.tv_date_exp);
            tvstt = itemView.findViewById(R.id.tvstt);
        }
    }
}