package com.hibox.carclient.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.hibox.carclient.R;

import java.util.List;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2017/11/15 14:19
 * desc:    筛选弹出框
 */

public abstract class FilterPopupWindow<T> extends PopupWindow {

    private Context context;
    private List<T> mList;
    private int curIndex;
    private RAdapter<T> mAdapter;
    private OnSelectListener mListener;

    public interface OnSelectListener {
        void selected(int index);
    }

    public FilterPopupWindow(Context context, View view, List<T> list) {
        super(context);
        this.context = context;
        this.mList = list;
        init(view);
    }

    private void init(View view) {
        setContentView(view);
        RecyclerView rvFilter = view.findViewById(R.id.rv_filter);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        rvFilter.setLayoutManager(manager);
        mAdapter = new RAdapter<T>(context, R.layout.filter_item, mList, new RAdapter.OnItemClickListener<T>() {
            @Override
            public void onItemClick(int position, T t) {
                dismiss();
                if (curIndex == position) {
                    return;
                }
                mAdapter.notifyItemChanged(curIndex);
                curIndex = position;
                mAdapter.notifyItemChanged(position);
                if (mListener != null) {
                    mListener.selected(position);
                }
            }
        }) {
            @Override
            protected void init(RViewHolder holder, T t) {
                int position = holder.getAdapterPosition();
                AppCompatTextView name = holder.getView(R.id.tv_name);
                name.setTextColor(context.getResources().getColor(position == curIndex ? R.color.colorAccent : R.color.color_666));
                initItem(holder, t);
            }
        };

        rvFilter.setAdapter(mAdapter);
        rvFilter.addItemDecoration(new MyItemDecoration());
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(false);
        setFocusable(true);
        setWidth((int) (Utils.getDisplayMetrics().widthPixels / 2.0f) - Utils.dp2px(20));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public void updateData() {
//        this.mList.clear();
//        this.mList.addAll(list);
        curIndex = 0;
        mAdapter.notifyDataSetChanged();
    }

    public void setListener(OnSelectListener listener) {
        mListener = listener;
    }

    public int getCurIndex() {
        return curIndex;
    }

    protected abstract void initItem(RViewHolder holder, T t);


    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        private Paint mPaint;

        public MyItemDecoration() {
            mPaint = new Paint();
            mPaint.setColor(context.getResources().getColor(R.color.color_eaeaea));
            mPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int childCount = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                float top = view.getBottom();
                float bottom = view.getBottom() + 1;
                c.drawRect(left, top, right, bottom, mPaint);
            }


            super.onDraw(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = 1;
        }
    }
}
