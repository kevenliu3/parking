package com.hibox.carclient.fragment;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import com.hibox.carclient.R;
import com.hibox.carclient.ToastUtil;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2018/09/27 15:06
 * desc:
 */
public class BaseFragment extends Fragment {
    private Dialog mDialog;

    protected void showToast(final String msg) {
        if (getActivity() != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(getActivity(), msg);

                }
            });
        }
    }

    protected void showDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog == null) {
                    mDialog = new Dialog(getActivity(), R.style.selectDialog);
                    mDialog.setContentView(R.layout.view_small_progress);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                }
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }
        });

    }

    protected void cancelDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
