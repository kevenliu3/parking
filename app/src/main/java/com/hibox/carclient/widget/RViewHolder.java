package com.hibox.carclient.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ramo
 * Everyday is another day, keep going.
 */
public class RViewHolder extends RecyclerView.ViewHolder {
    //使用SparseArray缓存通过findViewById获取到的控件，避免多次findViewById
    private SparseArray<View> mArray = new SparseArray<>();
    private Context context;

    public RViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
    }

    public <E extends View> E getView(int id) {
        View view = mArray.get(id);
        if (view == null) {
            mArray.put(id, itemView.findViewById(id));
            view = mArray.get(id);
        }
        return (E) view;
    }

    public void setText(int id, String value) {

        setText(id, value == null ? null : new SpannableString(value));

    }

    public void setVisible(int id, int visibility) {
        getView(id).setVisibility(visibility);
    }

    public void setText(int id, int resId) {
        setText(id, new SpannableString(context.getString(resId)));

    }

    public void setText(int id, SpannableString value) {
        TextView textView = getView(id);
        textView.setText(value);
    }
    public void setText(int id, SpannableStringBuilder value) {
        TextView textView = getView(id);
        textView.setText(value);
    }
    public void setImage(int id, int resId) {
        ImageView view = getView(id);
        view.setImageResource(resId);

    }

    public void setImage(int id, Bitmap bitmap) {
        ImageView view = getView(id);
        view.setImageBitmap(bitmap);

    }

    public void setClickListener(int id, View.OnClickListener listener) {
        getView(id).setOnClickListener(listener);
    }
}
