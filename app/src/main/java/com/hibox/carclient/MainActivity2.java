package com.hibox.carclient;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.hibox.carclient.fragment.CameraFragment;
import com.hibox.carclient.fragment.HistoryFragment;
import com.hibox.carclient.fragment.MineFragment;

import java.util.List;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2018/09/27 10:40
 * desc:
 */
public class MainActivity2 extends AppCompatActivity {
    public static final SparseArray<String> TAGS = new SparseArray<String>() {
        {
            put(R.id.rb_camera, "CAMERA");
            put(R.id.rb_history, "HISTORY");
            put(R.id.rb_mine, "MINE");
        }
    };

    private int curIndex = -1;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mFragmentManager = getSupportFragmentManager();

        RadioGroup group = findViewById(R.id.rg_tab);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switchTab(i);
            }
        });
        switchTab(R.id.rb_camera);
    }

    private void switchTab(int index) {
        Fragment fragment = mFragmentManager.findFragmentByTag(TAGS.get(index));
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (fragment == null) {
            switch (index) {
                case R.id.rb_camera:
                    fragment = CameraFragment.newInstance();
                    break;
                case R.id.rb_history:
                    fragment = HistoryFragment.newInstance();
                    break;
                case R.id.rb_mine:
                    fragment = MineFragment.newInstance();
                    break;
            }
            transaction.add(R.id.fl_content, fragment, TAGS.get(index));
        }
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment1 : fragments) {
                transaction.hide(fragment1);
            }
        }
        transaction.show(fragment).commit();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 触摸其他地方，隐藏软键盘的的方法
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
