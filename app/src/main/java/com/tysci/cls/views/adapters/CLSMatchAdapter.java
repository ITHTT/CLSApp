package com.tysci.cls.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.activitys.CLSMatchDetailActivity;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.networks.GlideImageLoader;
import com.tysci.cls.views.widgets.recyclerviewstickyheader.StickyHeaderAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class CLSMatchAdapter extends RecyclerView.Adapter<CLSMatchAdapter.CLSMatchViewHolder>
implements StickyHeaderAdapter<CLSMatchAdapter.CLSMatchViewHolder>{
    private List<CLSMatchEntity> matchEntityList;

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
        holder.tvHomeTeamScore.setText(String.valueOf(info.getHomeTeamScore()));
        holder.tvAwayTeamScore.setText(String.valueOf(info.getAwayTeamScore()));

        GlideImageLoader.loadImage(holder.itemView.getContext(), info.getHomeTeamFlag(), R.mipmap.ic_launcher, holder.ivHomeTeamIcon);
        GlideImageLoader.loadImage(holder.itemView.getContext(),info.getAwayTeamFlag(),R.mipmap.ic_launcher,holder.ivAwayTeamIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=holder.itemView.getContext();
                Intent intent=new Intent(context, CLSMatchDetailActivity.class);
                intent.putExtra(CLSMatchDetailActivity.class.getSimpleName(),info);
                context.startActivity(intent);
            }
        });
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
        }
    }
}
