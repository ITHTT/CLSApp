package com.tysci.cls.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.activitys.CLSMatchDetailActivity;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.networks.GlideImageLoader;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class CLSMatchAdapter extends RecyclerView.Adapter<CLSMatchAdapter.CLSMatchViewHolder>
implements StickyHeaderAdapter<CLSMatchAdapter.CLSMatchViewHolder>{
    private List<CLSMatchEntity> matchEntityList;
    private int index=0;
    private String type;

    public CLSMatchAdapter(List<CLSMatchEntity> matchEntityList) {
        this.matchEntityList = matchEntityList;
    }

    @Override
    public CLSMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_item,parent,false);
        return new CLSMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CLSMatchViewHolder holder, int position) {
        final CLSMatchEntity info=matchEntityList.get(position);
        holder.tvMatchTime.setText(info.getMatchTime());
        holder.tvHomeTeamName.setText(info.getHomeTeamName());
        holder.tvAwayTeamName.setText(info.getAwayTeamName());
        if(info.getStatus()!=0) {
            holder.tvHomeTeamScore.setText(String.valueOf(info.getHomeTeamScore()));
            holder.tvAwayTeamScore.setText(String.valueOf(info.getAwayTeamScore()));
        }else{
            holder.tvHomeTeamScore.setText("");
            holder.tvAwayTeamScore.setText("");
        }

        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getHomeTeamFlag(), R.mipmap.ic_launcher, holder.ivHomeTeamIcon);
        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getAwayTeamFlag(),R.mipmap.ic_launcher,holder.ivAwayTeamIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, CLSMatchDetailActivity.class);
                intent.putExtra(CLSMatchDetailActivity.class.getSimpleName(), info);
                context.startActivity(intent);
            }
        });

        String header=getHeaderId(position);
        if(TextUtils.isEmpty(type)||!header.equals(type)){
            index=1;
            type=header;
            holder.layoutContentInfo.setBackgroundResource(R.drawable.match_white_bg);
        }else{
            index++;
            KLog.e("index:"+index);
            if(index%2==0){
                KLog.e("设置灰色背景色");
                holder.layoutContentInfo.setBackgroundResource(R.drawable.match_gray_bg);
            }else{
                holder.layoutContentInfo.setBackgroundResource(R.drawable.match_white_bg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return matchEntityList.size();
    }

    @Override
    public String getHeaderId(int position) {
        if(position>=0&&position<getItemCount()){
            return matchEntityList.get(position).getMatchDateTransWeek();
        }
        return null;
    }

    @Override
    public CLSMatchViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_header_title,parent,false);
        return new CLSMatchViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(CLSMatchViewHolder viewholder, int position) {
        if(position>=0&&position<getItemCount()){
            CLSMatchEntity info=matchEntityList.get(position);
            viewholder.tvMatchCount.setText(info.getGroupName());
            viewholder.tvMatchDate.setText(info.getMatchDateWeek());
        }
    }

    public static final class CLSMatchViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHomeTeamIcon;
        TextView tvHomeTeamName;
        TextView tvHomeTeamScore;
        ImageView ivAwayTeamIcon;
        TextView tvAwayTeamName;
        TextView tvAwayTeamScore;
        TextView tvMatchTime;
        TextView tvMatchCount;
        TextView tvMatchDate;
        View layoutContentInfo;

        public CLSMatchViewHolder(View itemView) {
            super(itemView);
            ivHomeTeamIcon=(ImageView)itemView.findViewById(R.id.iv_home_team_icon);
            tvHomeTeamName=(TextView)itemView.findViewById(R.id.tv_home_team_name);
            tvHomeTeamScore=(TextView)itemView.findViewById(R.id.tv_home_team_score);
            ivAwayTeamIcon=(ImageView)itemView.findViewById(R.id.iv_away_team_icon);
            tvAwayTeamName=(TextView)itemView.findViewById(R.id.tv_away_team_name);
            tvAwayTeamScore=(TextView)itemView.findViewById(R.id.tv_away_team_score);
            tvMatchTime=(TextView)itemView.findViewById(R.id.tv_match_time);

            tvMatchCount=(TextView)itemView.findViewById(R.id.tv_match_count);
            tvMatchDate=(TextView)itemView.findViewById(R.id.tv_match_date);

            layoutContentInfo=itemView.findViewById(R.id.layout_content_info);
        }
    }
}
