package com.hibox.carclient.fragment;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hibox.carclient.BaseContant;
import com.hibox.carclient.R;
import com.hibox.carclient.RequestUtils;
import com.hibox.carclient.model.BaseModel;
import com.hibox.carclient.model.CameraStatus;
import com.hibox.carclient.model.StatusModel;
import com.hibox.carclient.widget.Utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static com.hibox.carclient.BaseContant.BASE_URL;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2018/09/27 11:04
 * desc:
 */
public class CameraFragment extends BaseFragment {
    private VideoView video_view1;
    private VideoView video_view2;
    private VideoView video_view3;
    private TextView tv_Status1;
    private TextView tv_Status2;
    private TextView tv_Status3;
    private ScrollView sv_cameras;
    private CheckedTextView cb_switch;
    private LinearLayout ll_status_running;
    private LinearLayout ll_status_error;
    private Call camearaCall;
    private Call switchCall;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sv_cameras = view.findViewById(R.id.sv_cameras);
        cb_switch = view.findViewById(R.id.cb_switch);
        video_view1 = view.findViewById(R.id.video_view1);
        video_view2 = view.findViewById(R.id.video_view2);
        video_view3 = view.findViewById(R.id.video_view3);
        tv_Status1 = view.findViewById(R.id.tv_status1);
        tv_Status2 = view.findViewById(R.id.tv_status2);
        tv_Status3 = view.findViewById(R.id.tv_status3);

        ll_status_running = view.findViewById(R.id.ll_status_running);
        ll_status_error = view.findViewById(R.id.ll_status_error);

        int width = Utils.getDisplayMetrics().widthPixels;
        int height = (int) (width * 9 / 16.0f);
        video_view1.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        video_view2.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        video_view3.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        sv_cameras.smoothScrollTo(0, 0);


        cb_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(cb_switch.isChecked());
            }
        });


        video_view1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                video_view1.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                return true;
            }
        });
        video_view2.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                video_view2.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                return true;
            }
        });
        video_view3.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                video_view3.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                return true;
            }
        });

        getStatus();
    }

    private void getStatus() {
        showDialog();
        final String url = BASE_URL + "parking/getDevice?deviceId=1";
        final Request request = new Request.Builder()
                .url(url)
                .build();
        RequestUtils.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("获取状态失败");
                cancelDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    BaseModel<StatusModel> modelBaseModel = JSON.parseObject(response.body().string(), new TypeReference<BaseModel<StatusModel>>() {
                    });
                    if (modelBaseModel.isSuccess()) {
//                        Log.e(TAG, JSON.toJSONString(modelBaseModel));
                        final StatusModel data = modelBaseModel.getData();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cb_switch.setChecked(data.getForbid() == 0);
                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        getCameraStatus();
                                        Thread.sleep(10 * 1000L);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    } else {
                        showToast("获取状态失败");
                    }
                } catch (Exception e) {

                    showToast("获取状态失败");
                }
                cancelDialog();
            }
        });
    }

    private void getCameraStatus() {

        String url = BASE_URL + "/parking/getDeviceStatus?deviceId=1";
        final Request request = new Request.Builder()
                .url(url)
                .build();
        if (camearaCall != null && !camearaCall.isExecuted()) {
            camearaCall.cancel();
        }
        camearaCall = RequestUtils.getInstance().getClient().newCall(request);
        camearaCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showToast("获取摄像头状态失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String string = response.body().string();
//                    Log.e(TAG, string);
                    BaseModel<Map<Integer, CameraStatus>> modelBaseModel = JSON.parseObject(string, new TypeReference<BaseModel<Map<Integer, CameraStatus>>>() {
                    });
                    if (modelBaseModel.isSuccess()) {
                        Map<Integer, CameraStatus> data = modelBaseModel.getData();
                        final CameraStatus cameraStatus1 = data.get(1);
                        final CameraStatus cameraStatus2 = data.get(2);
                        final CameraStatus cameraStatus3 = data.get(3);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = true;
                                if (cameraStatus1 == null || cameraStatus1.getStatus() != 1 || TextUtils.isEmpty(cameraStatus1.getUrl())) {
                                    b = false;
                                    tv_Status1.setText("运行异常");
                                    tv_Status1.setTextColor(getResources().getColor(R.color.red));

                                } else {
                                    tv_Status1.setText("运行正常");
                                    tv_Status1.setTextColor(getResources().getColor(R.color.color_333));

                                    video_view1.setVideoURI(Uri.parse(Uri.decode(cameraStatus1.getUrl())));
                                    video_view1.start();
                                }
                                if (cameraStatus2 == null || cameraStatus2.getStatus() != 1 || TextUtils.isEmpty(cameraStatus2.getUrl())) {
                                    b = false;
                                    tv_Status2.setText("运行异常");
                                    tv_Status2.setTextColor(getResources().getColor(R.color.red));
                                } else {
                                    tv_Status2.setText("运行正常");
                                    tv_Status2.setTextColor(getResources().getColor(R.color.color_333));

                                    video_view2.setVideoURI(Uri.parse(Uri.decode(cameraStatus2.getUrl())));
                                    video_view2.start();

                                }
                                if (cameraStatus3 == null || cameraStatus3.getStatus() != 1 || TextUtils.isEmpty(cameraStatus3.getUrl())) {
                                    b = false;
                                    tv_Status3.setText("运行异常");
                                    tv_Status3.setTextColor(getResources().getColor(R.color.red));
                                } else {
                                    tv_Status3.setText("运行正常");
                                    tv_Status3.setTextColor(getResources().getColor(R.color.color_333));

                                    video_view3.setVideoURI(Uri.parse(Uri.decode(cameraStatus3.getUrl())));
                                    video_view3.start();

                                }
                                ll_status_error.setVisibility(b ? View.GONE : View.VISIBLE);
                                ll_status_running.setVisibility(b ? View.VISIBLE : View.GONE);
                            }
                        });

                    } else {
                        showToast("获取摄像头状态失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("获取摄像头状态失败");
                }

            }
        });
    }

    private void showAlert(final boolean checked) {
        new AlertDialog.Builder(getActivity())
                .setMessage(checked ? "确定关闭设备？" : "确定打开设备？")
                .setCancelable(true)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switchStatus(checked);
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }

    private void switchStatus(final boolean checked) {
        showDialog();
        int switchStatus = checked ? 1 : 0;
        String url = BASE_URL + "/parking/abordDevice?deviceId=1&status=" + switchStatus;
        final Request request = new Request.Builder()
                .url(url)
                .build();
        if (switchCall != null && !switchCall.isExecuted()) {
            switchCall.cancel();
        }
        switchCall = RequestUtils.getInstance().getClient().newCall(request);
        switchCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showToast((checked ? "关闭" : "打开") + "设备失败");
                cancelDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {


                    JSONObject parse = (JSONObject) JSON.parse(response.body().string());
                    if (parse.containsKey("success") && parse.getBoolean("success")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cb_switch.setChecked(!checked);
                            }
                        });
                        showToast((checked ? "关闭" : "打开") + "设备成功");
                    } else {
                        showToast((checked ? "关闭" : "打开") + "设备失败");
                    }
                } catch (Exception e) {
                    showToast((checked ? "关闭" : "打开") + "设备失败");
                }
                cancelDialog();
            }
        });
    }
}
