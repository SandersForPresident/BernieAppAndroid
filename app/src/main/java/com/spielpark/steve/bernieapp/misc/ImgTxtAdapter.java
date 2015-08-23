package com.spielpark.steve.bernieapp.misc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.ImgTxtItem;

import java.util.List;

/**
 * Created by Steve on 8/14/2015.
 */
public class ImgTxtAdapter extends ArrayAdapter {
    private Context ctx;
    private List list;
    private int layout;
    public ImgTxtAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.layout = resource;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImgTxtItem item = (ImgTxtItem) list.get(position);
        ViewHolder v;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            v = new ViewHolder();
            v.img = (ImageView) convertView.findViewById(R.id.picThumb);
            v.txt = (TextView) convertView.findViewById(R.id.txtItem);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        if (item.getImg() != null) {
            v.img.setImageBitmap(item.getImg());
        }
        v.txt.setTypeface(Typeface.createFromAsset(ctx.getAssets(), "Jubilat.otf"));
        v.txt.setText(Html.fromHtml(item.getTxt()));
        return convertView;
    }

    private static class ViewHolder {
        TextView txt;
        ImageView img;
    }
}
