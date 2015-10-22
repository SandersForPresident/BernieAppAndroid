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
import butterknife.Bind;
import butterknife.ButterKnife;
import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.wrappers.ImgTxtItem;
import java.util.List;

/**
 * Created by Steve on 8/14/2015.
 */
public class ImgTxtAdapter extends ArrayAdapter {
  private final Typeface typeface;
  private Context ctx;
  private List list;
  private int layout;

  public ImgTxtAdapter(Context context, int resource, List objects) {
    super(context, resource, objects);
    this.ctx = context;
    this.layout = resource;
    this.list = objects;
    this.typeface = Typeface.createFromAsset(ctx.getAssets(), "Jubilat.otf");
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ImgTxtItem item = (ImgTxtItem) list.get(position);
    ViewHolder v;
    if (convertView == null) {
      LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
      convertView = inflater.inflate(layout, parent, false);
      v = new ViewHolder(convertView, typeface);
      v.img = (ImageView) convertView.findViewById(R.id.picThumb);
      convertView.setTag(v);
    } else {
      v = (ViewHolder) convertView.getTag();
    }
    Util.getPicasso(ctx).load(item.getImgSrc()).into(v.img);
    v.txt.setText(Html.fromHtml(item.getTxt()));
    return convertView;
  }

  private static class ViewHolder {
    @Bind(R.id.picThumb) TextView txt;
    @Bind(R.id.txtItem) ImageView img;

    public ViewHolder(View convertView, Typeface typeface) {
      ButterKnife.bind(this, convertView);
      txt.setTypeface(typeface);
    }
  }
}
