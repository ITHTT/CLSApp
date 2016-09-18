package com.tysci.cls.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.modles.CLSMatchEventEntity;
import com.tysci.cls.networks.GlideImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CLSMatchEventAdapter extends RecyclerView.Adapter<CLSMatchEventAdapter.CLSMatchEventViewHolder>{
    private List<CLSMatchEventEntity> matchEventEntityList;

    public CLSMatchEventAdapter(List<CLSMatchEventEntity>datas){
        matchEventEntityList=datas;
    }

    @Override
    public CLSMatchEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_event_item,parent,false);
        return new CLSMatchEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CLSMatchEventViewHolder holder, int position) {
        CLSMatchEventEntity info=matchEventEntityList.get(position);
        if(!TextUtils.isEmpty(info.getHomePlayerName())&&!TextUtils.isEmpty(info.getHomeEventImage())){
            holder.tvHomeTeamEventInfo.setText(info.getHomePlayerName());
            GlideImageLoader.loadImage(holder.itemView.getContext(),info.getHomeEventImage(),R.mipmap.ic_launcher,holder.ivHomeTeamEventIcon);
        }else{
            holder.tvHomeTeamEventInfo.setText("");
            holder.ivHomeTeamEventIcon.setImageResource(0);
        }

        if(!TextUtils.isEmpty(info.getAwayPlayerName())&&!TextUtils.isEmpty(info.getAwayEventImage())){
            holder.tvAwayTeamEventInfo.setText(info.getAwayPlayerName());
            GlideImageLoader.loadImage(holder.itemView.getContext(),info.getAwayEventImage(),R.mipmap.ic_launcher,holder.ivAwayTeamEventIcon);
        }else{
            holder.tvAwayTeamEventInfo.setText("");
            holder.ivAwayTeamEventIcon.setImageResource(0);
        }

        holder.tvMatchTime.setText(String.valueOf(info.getTime()));

        if(position%2==1){
            holder.itemView.setBackgroundResource(R.color.normal);
        }else{
            holder.itemView.setBackgroundResource(R.color.selected);
        }
    }

    @Override
    public int getItemCount() {
        return matchEventEntityList.size();
    }

    public static final class CLSMatchEventViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_home_team_event_info)
        TextView tvHomeTeamEventInfo;
        @Bind(R.id.iv_home_team_event_icon)
        ImageView ivHomeTeamEventIcon;
        @Bind(R.id.tv_away_team_event_info)
        TextView tvAwayTeamEventInfo;
        @Bind(R.id.iv_away_team_event_icon)
        ImageView ivAwayTeamEventIcon;
        @Bind(R.id.tv_match_time)
        TextView tvMatchTime;

        public CLSMatchEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
