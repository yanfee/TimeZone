package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.LogUtil;
import com.example.myapplication.R;
import com.example.myapplication.bean.TimeZoneBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/*

 */
public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneAdapter.ZoneListViewHolder> {
    private List<TimeZoneBean> mEntities = new ArrayList<>();
    private Context mContext;

    public TimeZoneAdapter(Context mContext, List<TimeZoneBean> entities) {
        this.mContext = mContext;
        this.mEntities = entities;
    }

    public void updateData(List<TimeZoneBean> entities) {
        this.mEntities = entities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ZoneListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //第三个参数attachToRoot不能省略,否则报java.lang.IllegalStateException: ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)
        return new ZoneListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_timezone_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ZoneListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(mEntities.get(position).getName());
        holder.tv_gmt.setText(mEntities.get(position).getGmt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, holder.tv_name.getText().toString(), Toast.LENGTH_LONG).show();
                setTimeZone(mEntities.get(position).getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mEntities.size();
    }

    static class ZoneListViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_gmt;
        public View view;


        public ZoneListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_item_name);
            tv_gmt = itemView.findViewById(R.id.tv_item_gmt);
            view = itemView;
        }
    }


    /**
     * 设置系统时区
     *
     * @param timeZone
     */
    public void setTimeZone(String timeZone) {
//        final Calendar now = Calendar.getInstance();
//        TimeZone tz = TimeZone.getTimeZone(timeZone);
//        now.setTimeZone(tz);
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setTimeZone(timeZone);
        String current = TimeZone.getDefault().getDisplayName();
        LogUtil.v("current--->"+current);
    }
}
