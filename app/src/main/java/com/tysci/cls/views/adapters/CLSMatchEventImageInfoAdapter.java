package com.tysci.cls.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.modles.CLSMatchEventImageInfoEntity;
import com.tysci.cls.networks.GlideImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CLSMatchEventImageInfoAdapter extends BaseAdapter{
    private List<CLSMatchEventImageInfoEntity> eventImageList;

    public CLSMatchEventImageInfoAdapter(List<CLSMatchEventImageInfoEntity> eventImageList) {
        this.eventImageList = eventImageList;
    }

    @Override
    public int getCount() {
        return eventImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CLSMatchEventImageInfoViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_image_item,parent,false);
            holder=new CLSMatchEventImageInfoViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (CLSMatchEventImageInfoViewHolder) convertView.getTag();
        }
        CLSMatchEventImageInfoEntity info=eventImageList.get(position);
        GlideImageLoader.loadImage(parent.getContext(),info.getEventImage(),R.mipmap.ic_launcher,holder.ivEventIcon);
        holder.tvEventName.setText(info.getEventName());
        return convertView;
    }

    public static final class CLSMatchEventImageInfoViewHolder{
        TextView tvEventName;
        ImageView ivEventIcon;

        public CLSMatchEventImageInfoViewHolder(View itemView){
            tvEventName=(TextView)itemView.findViewById(R.id.tv_event_name);
            ivEventIcon=(ImageView)itemView.findViewById(R.id.iv_event_image_icon);
        }
    }
}
