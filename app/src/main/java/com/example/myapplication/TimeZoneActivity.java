package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.internal.app.LocalePicker;
import com.example.myapplication.adapter.TimeZoneAdapter;
import com.example.myapplication.bean.TimeZoneBean;
import com.example.myapplication.databinding.ActivityTimeZoneBinding;
import com.example.myapplication.decoration.DividerItemDecoration;
import com.example.myapplication.decoration.SectionItemDecoration;
import com.example.myapplication.pinyinhelper.PinyinHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

public class TimeZoneActivity extends Activity {

    private ActivityTimeZoneBinding activityTimeZoneBinding;

    private TimeZoneAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<TimeZoneBean> allTimeZones = new ArrayList<>();
    private List<TimeZoneBean> mResults = new ArrayList<>();
    private PinyinHelper mPinyinHelper;
    private Context mContext;
    private static List<Map<String, Object>> mTimeZones;

    private static String key_display_label = "display_label";
    private static String key_name = "name";
    private static String key_gmt = "gmt";
    private static String key_id = "id";
    private static String[] zoneNames;
    private static String[] zoneGmt;
    private static String[] zoneID;


    public static void getZones(Context context) {

        mTimeZones = ZoneGetter.getZonesList(context);
        zoneGmt = new String[mTimeZones.size()];
        zoneNames = new String[mTimeZones.size()];
        zoneID = new String[mTimeZones.size()];
        for (int i = 0; i < mTimeZones.size(); i++) {
            Map<String, Object> zoneMap = mTimeZones.get(i);
            zoneGmt[i] = zoneMap.get(key_gmt).toString();
            zoneNames[i] = zoneMap.get(key_name).toString();
            zoneID[i] = zoneMap.get(key_id).toString();

            LogUtil.v("Availalbe ids..........zoneMap........" + zoneMap.get(key_display_label));
            LogUtil.v("Availalbe ids..........zoneMap........" + zoneMap.get(key_name));
            LogUtil.v("Availalbe ids..........zoneMap........" + zoneMap.get(key_gmt));
        }
    }


    private void updateLanguage(Locale locale) {
        try {
            Object objIActMag, objActMagNative;

            Class clzIActMag = Class.forName("android.app.IActivityManager");

            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");


            Method mtdActMagNative$getDefault = clzActMagNative
                    .getDeclaredMethod("getDefault");

            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);

            Method mtdIActMag$getConfiguration = clzIActMag
                    .getDeclaredMethod("getConfiguration");

            Configuration config = (Configuration) mtdIActMag$getConfiguration
                    .invoke(objIActMag);

            config.locale = locale;

            Class clzConfig = Class.forName("android.content.res.Configuration");
            java.lang.reflect.Field userSetLocale = clzConfig.getField("userSetLocale");
            userSetLocale.set(config, true);

            // 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
            // 会重新调用 onCreate();
            Class[] clzParams = {Configuration.class};

            Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod("updateConfiguration", clzParams);

            mtdIActMag$updateConfiguration.invoke(objIActMag, config);

            BackupManager.dataChanged("com.android.providers.settings");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Requests the system to update the system locale. Note that the system looks halted
     * for a while during the Locale migration, so the caller need to take care of it.
     */
    public static void updateLocale(Locale locale) {
        try {
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();

            // Will set userSetLocale to indicate this isn't some passing default - the user
            // wants this remembered
            config.setLocale(locale);

            am.updateConfiguration(config);
            // Trigger the dirty bit for the Settings Provider.
            BackupManager.dataChanged("com.android.providers.settings");
        } catch (RemoteException e) {
            // Intentionally left blank
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityTimeZoneBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_zone);
        mPinyinHelper = new PinyinHelper();
        DateUtil.setSysDate(this, 2098, 2, 3);

//        String str1 = Locale.getDefault().toString();
//       // Locale locale = new Locale("fr", "FR");
//       // Locale locale = new Locale("it", "IT");
//       Locale locale = new Locale("es", "ES");
//       LocalePicker.updateLocale(locale);
//
//        updateLocale(locale);
//        updateLanguage(locale);
//
//        Locale locale0;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            locale0 = LocaleList.getDefault().get(0);
//        } else {
//            locale0 = Locale.getDefault();
//        }
//        String language = locale.getLanguage() + "-" + locale.getCountry();

        mContext = this;
        getZones(this);
        init();
    }

    private void searchTimeZones() {
        activityTimeZoneBinding.titleBar.setEditVisible(true);
        activityTimeZoneBinding.titleBar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    ((SectionItemDecoration) (activityTimeZoneBinding.rvTimezone.getItemDecorationAt(0))).setData(allTimeZones);
                    adapter.updateData(allTimeZones);
                } else {
                    mResults.clear();
                    // 判断数据中是否含有所输入的字符，将含有该字符的集合转换、排序、更新列表
                    LogUtil.i("timezone", "afterTextChanged()--keyword==" + keyword);
                    for (int i = 0; i < allTimeZones.size(); i++) {
                        if (allTimeZones.get(i).getName().contains(keyword)) {
                            mResults.add(allTimeZones.get(i));
                            LogUtil.i("timezone", "result1==" + allTimeZones.get(i));
                        } else if (allTimeZones.get(i).getIndexTag().equals(keyword.toUpperCase())) {
                            mResults.add(allTimeZones.get(i));
                            LogUtil.i("timezone", "result2==" + allTimeZones.get(i));
                        }
                    }
                    // 使用tinypinyin转换、排序
                    mPinyinHelper.sortSourceDatas(mResults);
                    ((SectionItemDecoration) (activityTimeZoneBinding.rvTimezone.getItemDecorationAt(0))).setData(mResults);
                    adapter.updateData(mResults);
                }
            }
        });
    }

    private boolean isSoftShowing() {
        //获取当屏幕内容的高度
        int screenHeight = this.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        LogUtil.v("screenHigh: " + screenHeight + " rectViewBom : " + rect.bottom);
        return screenHeight * 2 / 3 > rect.bottom;
    }

    //此方法，如果显示则隐藏，如果隐藏则显示
    private void hintKbOne() {
        View view = (this).getWindow().peekDecorView();
        if (isSoftShowing()) {
            if (view != null && view.getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                activityTimeZoneBinding.titleBar.getEditText().clearFocus();
                activityTimeZoneBinding.titleBar.getEditText().setText("");
            }
        } else {
//            InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            finish();
        }
    }

    private void showSoftInput() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                activityTimeZoneBinding.titleBar.getEditText().requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(activityTimeZoneBinding.titleBar.getEditText(), InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);   //0.1秒

    }

    public void init() {

        activityTimeZoneBinding.titleBar.setRightIconVisible(true);
        activityTimeZoneBinding.titleBar.setOnActionListener(new CommonTitleBar.OnActionListener() {
            @Override
            public void onBack() {
                hintKbOne();
            }

            @Override
            public void onActionClick() {
                activityTimeZoneBinding.rlTimezone.setBackground(null);
                searchTimeZones();
                showSoftInput();
            }
        });


        initData(zoneNames);
        adapter = new TimeZoneAdapter(this, allTimeZones);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityTimeZoneBinding.rvTimezone.setLayoutManager(linearLayoutManager);
        activityTimeZoneBinding.rvTimezone.addItemDecoration(new SectionItemDecoration(this, allTimeZones), 0);
        activityTimeZoneBinding.rvTimezone.addItemDecoration(new DividerItemDecoration(this, allTimeZones), 1);
        activityTimeZoneBinding.rvTimezone.setAdapter(adapter);

    }


    public void initData(String[] zones) {

        if (zones == null || zones.length == 0) return;
        for (int i = 0; i < zones.length; i++) {
            TimeZoneBean timeZoneBean = new TimeZoneBean();
            timeZoneBean.setName(zones[i]);
            timeZoneBean.setGmt(zoneGmt[i]);
            timeZoneBean.setId(zoneID[i]);
            allTimeZones.add(timeZoneBean);

        }
        mPinyinHelper.sortSourceDatas(allTimeZones);

    }

    /**
     * 设置系统时区
     *
     * @param timeZone
     */
    public static void setTimeZone(String timeZone) {
        final Calendar now = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        now.setTimeZone(tz);
    }
}