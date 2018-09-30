package com.hibox.carclient.fragment;

import android.content.SyncStatusObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hibox.carclient.BaseContant;
import com.hibox.carclient.CalendarTool;
import com.hibox.carclient.R;
import com.hibox.carclient.RequestUtils;
import com.hibox.carclient.model.CarModel;
import com.hibox.carclient.model.ResultModel;
import com.hibox.carclient.widget.*;
import com.squareup.picasso.Picasso;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2018/09/27 11:04
 * desc:
 */
public class HistoryFragment extends BaseFragment {
    private TextView tv_street;
    private RecyclerView rv_list;
    private List<CarModel> mList = new ArrayList<>();
    private FilterPopupWindow<String> orderByWindow;
    private Call dataCall;
    private RAdapter<CarModel> mAdapter;
    private String streeIndex = "太平桥路";

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_street = view.findViewById(R.id.tv_street);
        rv_list = view.findViewById(R.id.rv_list);
        tv_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showOrderBy();
            }
        });

        mAdapter = new RAdapter<CarModel>(getActivity(), R.layout.list_item, mList) {
            @Override
            protected void init(RViewHolder holder, CarModel carModel) {
                ImageView imageView = holder.getView(R.id.iv_pic);
                TextView textStatus = holder.getView(R.id.tv_status);

                Byte status = carModel.getStatus();
                if (status == 0) {
                    textStatus.setText("正常停车");
                    textStatus.setTextColor(getResources().getColor(R.color.color_537ff3));
                    textStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.normal_mine, 0, 0, 0);
                } else if (status == 1) {
                    textStatus.setText("疑似违停");
                    textStatus.setTextColor(getResources().getColor(R.color.color_00cc13));
                    textStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.wenhao_mine, 0, 0, 0);
                } else {
                    textStatus.setText("违章停车");
                    textStatus.setTextColor(getResources().getColor(R.color.color_f30b0b));
                    textStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.error_mine, 0, 0, 0);
                }

                String imageUrl = carModel.getImage1();
                if(imageUrl==null||imageUrl.equals("")){
                    imageUrl = carModel.getImage2();
                }

                if(imageUrl==null||imageUrl.equals("")){
                    imageUrl = carModel.getImage3();
                }

                Picasso.get().load(Uri.parse(imageUrl)).resize(Utils.dp2px(80), Utils.dp2px(44)).into(imageView);
                holder.setText(R.id.tv_name, carModel.getPlateNumber());
                holder.setText(R.id.tv_time, CalendarTool.getPastTimeString(carModel.getStartTime().getTime()));


            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new ItemSpacing(0, 0, 0, 2));
        rv_list.setAdapter(mAdapter);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        getData(streeIndex);
                        Thread.sleep(30 * 1000L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //getData("太平");
    }

    private void showOrderBy() {
        if (orderByWindow == null) {
            View view = View.inflate(getActivity(), R.layout.filter_window, null);
            final List<String> list = new ArrayList<>();
            list.add("太平桥路");
            //list.add("关山大道");
            //list.add("武珞路");
            //list.add("珞瑜东路");
            orderByWindow = new FilterPopupWindow<String>(getActivity(), view, list) {
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
                    streeIndex = list.get(index);
                    getData(list.get(index));
                }
            });
        }
        if (!orderByWindow.isShowing()) {
            ViewGroup.LayoutParams layoutParams = tv_street.getLayoutParams();
            int i = (Utils.getDisplayMetrics().widthPixels / 2 - Utils.dp2px(20) - layoutParams.width) / 2;
            System.out.println(layoutParams.width + "  " + i);
            orderByWindow.showAsDropDown(tv_street, -i, 0, Gravity.BOTTOM);

        }
    }

    private void getData(String index) {
//        String encode = "";
//        try {
//            encode = URLEncoder.encode(BaseContant.BASE_URL + "parking/getIllegalStopList?deviceId=1&iDisplayStart=0&iDisplayLength=1000&section_number=" + index, "utf-8");
//            System.out.println("encode:" + encode);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        final Uri url = Uri.parse(BaseContant.BASE_URL + "parking/getIllegalStopList?deviceId=1&iDisplayStart=0&iDisplayLength=1000&section_number=" + index);
        System.out.println(url);
        final Request request = new Request.Builder()
                .url(url.toString())
                .build();
        if (dataCall != null && !dataCall.isExecuted()) {
            dataCall.cancel();
        }
        dataCall = RequestUtils.getInstance().getClient().newCall(request);
        dataCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
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
                    getActivity().runOnUiThread(new Runnable() {
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


}
