package com.tysci.cls.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.modles.CLSMatchStatisticsEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CLSMatchStatisticsAdapter extends RecyclerView.Adapter<CLSMatchStatisticsAdapter.CLSMatchStatisticsViewHolder>{
    private List<CLSMatchStatisticsEntity> matchStatisticsEntityList=null;

    public CLSMatchStatisticsAdapter(List<CLSMatchStatisticsEntity> matchStatisticsEntityList) {
        this.matchStatisticsEntityList = matchStatisticsEntityList;
    }

    @Override
    public CLSMatchStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_statistics,parent,false);
        return new CLSMatchStatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CLSMatchStatisticsViewHolder holder, int position) {
        CLSMatchStatisticsEntity info=matchStatisticsEntityList.get(position);
        holder.tvHomeTeamInfo.setText(info.getHomeTeamValue());
        holder.tvAwayTeamInfo.setText(info.getAwayTeamValue());
        holder.tvMatchInfo.setText(info.getStatisticKey());

    }

    @Override
    public int getItemCount() {
        return matchStatisticsEntityList.size();
    }

    public static final class CLSMatchStatisticsViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_home_team_info)
        TextView tvHomeTeamInfo;
        @Bind(R.id.tv_match_info_item)
        TextView tvMatchInfo;
        @Bind(R.id.tv_away_team_info)
        TextView tvAwayTeamInfo;

        public CLSMatchStatisticsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
