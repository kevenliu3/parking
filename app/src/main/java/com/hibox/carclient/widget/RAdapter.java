package com.hibox.carclient.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2017/10/10 14:20
 * desc:    通用的RecyclerView的适配器。
 * 如果不是多布局使用带layoutId的构造方法，
 * 如果使用多布局，则使用不带layoutId的构造方法，并且必须重写getItemViewType的方法，方法的返回值必须为布局的id。
 */


public abstract class RAdapter<T> extends RecyclerView.Adapter<RViewHolder> {
    protected Context context;
    protected List<T> list;
    private int layoutId;
    private OnItemClickListener<T> mListener = null;

    protected RAdapter(Context context, int layoutId, List<T> list) {
        this(context, layoutId, list, null);
    }

    protected RAdapter(Context context, int layoutId, List<T> list, OnItemClickListener<T> listener) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
        this.mListener = listener;
    }

    //此构造方法只适用于多布局的情况
    protected RAdapter(Context context, List<T> list) {
        this(context, list, null);
    }

    protected RAdapter(Context context, List<T> list, OnItemClickListener<T> listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(@LayoutRes int position) {

        return layoutId;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return new RViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        init(holder, list.get(position));
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position, list.get(position));
                }
            });
        }
    }

    protected abstract void init(RViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T t);

    }

}
