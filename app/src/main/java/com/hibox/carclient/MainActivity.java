package com.hibox.carclient;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hibox.carclient.model.*;
import com.hibox.carclient.widget.*;
import com.squareup.picasso.Picasso;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FilterPopupWindow<String> orderByWindow;
    private CheckedTextView tv_street;
    private CheckedTextView tv_status;
    private LinearLayout ll_status_running;
    private LinearLayout ll_status_error;
    private CheckedTextView cb_switch;
    private RecyclerView rv_list;
    //    private RecyclerView rv_status;
    private ScrollView rv_status1;
    private RAdapter<CarModel> mAdapter;
    private RAdapter<CameraStatus> mAdapter2;
    private List<CarModel> mList = new ArrayList<>();
    private List<CameraStatus> mList2 = new ArrayList<>();
    public static final String BASE_URL = "https://app.hibox.vip/";
    private static final String TAG = "MainActivity";
    private Call dataCall;
    private Call switchCall;
    private Call camearaCall;
    private Dialog mDialog;

    private VideoView video_view1;
    private VideoView video_view2;
    private VideoView video_view3;
    private TextView tv_Status1;
    private TextView tv_Status2;
    private TextView tv_Status3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_street = findViewById(R.id.tv_street);
        video_view1 = findViewById(R.id.video_view1);
        video_view2 = findViewById(R.id.video_view2);
        video_view3 = findViewById(R.id.video_view3);
        tv_Status1 = findViewById(R.id.tv_status1);
        tv_Status2 = findViewById(R.id.tv_status2);
        tv_Status3 = findViewById(R.id.tv_status3);
        tv_status = findViewById(R.id.tv_status);
        ll_status_running = findViewById(R.id.ll_status_running);
        ll_status_error = findViewById(R.id.ll_status_error);
        cb_switch = findViewById(R.id.cb_switch);
        rv_list = findViewById(R.id.rv_list);
//        rv_status = findViewById(R.id.rv_status);
//        rv_status.setVisibility(View.GONE);
        rv_status1 = findViewById(R.id.rv_status1);
        tv_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showOrderBy();
//                rv_status.setVisibility(View.GONE);
                rv_status1.setVisibility(View.GONE);
                tv_street.setChecked(true);
                tv_status.setChecked(false);
                rv_list.setVisibility(View.VISIBLE);
            }
        });
        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rv_status.setVisibility(View.VISIBLE);
                rv_status1.setVisibility(View.VISIBLE);
                rv_list.setVisibility(View.GONE);
                tv_street.setChecked(false);
                tv_status.setChecked(true);
            }
        });
        cb_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchStatus(cb_switch.isChecked());
            }
        });
        mAdapter = new RAdapter<CarModel>(this, R.layout.list_item, mList) {
            @Override
            protected void init(RViewHolder holder, CarModel carModel) {
                ImageView imageView = holder.getView(R.id.iv_pic);
                Picasso.get().load(Uri.parse(carModel.getImage1())).resize(Utils.dp2px(80), Utils.dp2px(44)).into(imageView);
                holder.setText(R.id.tv_name, carModel.getPlateNumber());
                holder.setText(R.id.tv_time, CalendarTool.getPastTimeString(carModel.getStartTime().getTime()));
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new ItemSpacing(0, 0, 0, 2));
        rv_list.setAdapter(mAdapter);


//        mAdapter2 = new RAdapter<CameraStatus>(this, R.layout.status_item, mList2) {
//            @Override
//            protected void init(RViewHolder holder, CameraStatus carModel) {
//                int position = holder.getAdapterPosition();
//                ImageView imageView = holder.getView(R.id.iv_pic);
//                if (carModel != null) {
//                    Picasso.get().load(carModel.getImage()).resize(Utils.dp2px(240), Utils.dp2px(132)).placeholder(R.mipmap.zw).error(R.mipmap.zw).into(imageView);
//                } else {
//                    Picasso.get().load(R.mipmap.zw).resize(Utils.dp2px(240), Utils.dp2px(132)).placeholder(R.mipmap.zw).error(R.mipmap.zw).into(imageView);
//
//                }
//                String name = (position == 0 ? "前" : (position == 1 ? "中" : "后")) + "摄像头";
//                holder.setText(R.id.tv_name, name);
//                TextView view = holder.getView(R.id.tv_status);
//                if (carModel != null && carModel.getStatus() == 1 && !TextUtils.isEmpty(carModel.getUrl())) {
//                    view.setText("正常运行");
//                    view.setTextColor(getResources().getColor(R.color.color_666));
//                } else {
//                    view.setText("运行异常");
//                    view.setTextColor(getResources().getColor(R.color.red));
//
//                }
//            }
//        };
//        mAdapter2.setOnItemClickListener(new RAdapter.OnItemClickListener<CameraStatus>() {
//            @Override
//            public void onItemClick(int position, CameraStatus cameraStatus) {
//                if (cameraStatus.getStatus() == null || cameraStatus.getStatus() != 1 || TextUtils.isEmpty(cameraStatus.getUrl())) {
//                    showToast("无法预览当前摄像头的状态");
//                    return;
//                }
//                String url = cameraStatus.getUrl();
////                String url = "https://ugc.cp31.ott.cibntv.net/67746C16C51357152E3F5266B/03000804015B5AC9812317400E06449292FB3C-6C4A-4854-9C68-82BC942FF59C.mp4?ccode=0808&duration=391&expire=18000&psid=218cd2415dc5385e6c095b880f94e776&sp=&ups_client_netip=0badabe4&ups_ts=1537582514&ups_userid=&utid=0000_1537582514_218cd2415dc5385e6c095b880f94e776&vid=XMzc0ODYzMDY1Ng%3D%3D&vkey=B13955205a7087819171c578cea370247";
////                String url = "https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537511820606_279729.mp4";
////                String url = "http://hibox.oss-cn-shanghai.aliyuncs.com/video/wopao.mp4";
////                String extension = MimeTypeMap.getFileExtensionFromUrl(url);
////                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
////                Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
////                mediaIntent.setDataAndType(Uri.parse(url), mimeType);
////                startActivity(mediaIntent);
//                Bundle bundle = new Bundle();
//                bundle.putString("url", Utils.urlEncoder(url));
//                Intent i = new Intent(MainActivity.this, PlayerActivity.class);
//                i.putExtras(bundle);
//                startActivity(i);
//            }
//        });
//        LinearLayoutManager manager2 = new LinearLayoutManager(this);
//        rv_status.setLayoutManager(manager2);
//        rv_status.addItemDecoration(new ItemSpacing(0, 0, 0, 20));
//        rv_status.setAdapter(mAdapter2);

        getStatus();

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

//        Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
//            @Override
//            public void run() {
//                rv_status1.scrollTo(0, 0);
//            }
//        }, 1, TimeUnit.SECONDS);
        rv_status1.smoothScrollTo(0, 0);
    }

    private void showDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog == null) {
                    mDialog = new Dialog(MainActivity.this, R.style.selectDialog);
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

    private void cancelDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void showOrderBy() {
        if (orderByWindow == null) {
            View view = View.inflate(this, R.layout.filter_window, null);
            final List<String> list = new ArrayList<>();
            list.add("光谷大道");
            list.add("关山大道");
            list.add("武珞路");
            list.add("珞瑜东路");
            orderByWindow = new FilterPopupWindow<String>(this, view, list) {
                @Override
                protected void initItem(RViewHolder holder, String s) {
                    holder.setText(R.id.tv_name, s);
                }
            };
            orderByWindow.setListener(new FilterPopupWindow.OnSelectListener() {
                @Override
                public void selected(int index) {
                    tv_street.setText(list.get(index));
                    showDialog();
                    getData();
                }
            });
        }
        if (!orderByWindow.isShowing()) {
            orderByWindow.showAsDropDown(tv_street, 0, 0, Gravity.END);

        }
    }


    private void getData() {
        final String url = BASE_URL + "parking/getIllegalStopList?deviceId=1&iDisplayStart=0&iDisplayLength=1000";
        final Request request = new Request.Builder()
                .url(url)
                .build();
        if (dataCall != null && !dataCall.isExecuted()) {
            dataCall.cancel();
        }
        dataCall = RequestUtils.getInstance().getClient().newCall(request);
        dataCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure", e);
                showToast("获取数据失败");
                cancelDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                cancelDialog();
                try {
                    String string = response.body().string();
                    ResultModel resultModel = JSON.parseObject(string, ResultModel.class);
                    mList.clear();
                    mList.addAll(resultModel.getAaData());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("获取数据失败");
                }

            }
        });

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
                        runOnUiThread(new Runnable() {
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

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        getData();
                                        Thread.sleep(30 * 1000L);
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
                Log.e(TAG, "onFailure", e);
                showToast((checked ? "关闭" : "打开") + "设备失败");
                cancelDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {


                    JSONObject parse = (JSONObject) JSON.parse(response.body().string());
                    if (parse.containsKey("success") && parse.getBoolean("success")) {
                        runOnUiThread(new Runnable() {
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

    private boolean isFirst = true;

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
                Log.e(TAG, "onFailure", e);
                showToast("获取摄像头状态失败");
                cancelDialog();
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
                        mList2.clear();
                        mList2.add(cameraStatus1);
                        mList2.add(cameraStatus2);
                        mList2.add(cameraStatus3);
                        runOnUiThread(new Runnable() {
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
//                                mAdapter2.notifyDataSetChanged();
//                                String url = "https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537582934140_1.mp4";
//
//                                if (cameraStatus1 != null) {
////                                    video_view1.setVideoURI(Uri.parse(Uri.decode(url)));
//                                    video_view1.setVideoURI(Uri.parse(Uri.decode(cameraStatus1.getUrl())));
//                                }
//                                if (cameraStatus2 != null) {
//                                    video_view2.setVideoURI(Uri.parse(Uri.decode(cameraStatus2.getUrl())));
//                                }
//                                if (cameraStatus3 != null) {
//                                    video_view3.setVideoURI(Uri.parse(Uri.decode(cameraStatus3.getUrl())));
//                                }
//                                video_view1.start();
//
//                                video_view2.start();
//                                video_view3.start();
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

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(MainActivity.this, msg);

            }
        });
    }
}
