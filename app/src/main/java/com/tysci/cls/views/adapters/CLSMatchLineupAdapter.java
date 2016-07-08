package com.tysci.cls.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.modles.CLSMatchLineupEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchLineupAdapter extends RecyclerView.Adapter<CLSMatchLineupAdapter.CLSMatchLineupViewHodler>{
    private List<CLSMatchLineupEntity> matchLineupEntityList=null;

    public CLSMatchLineupAdapter(List<CLSMatchLineupEntity> matchLineupEntityList) {
        this.matchLineupEntityList = matchLineupEntityList;
    }

    @Override
    public CLSMatchLineupViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==1){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_lineup_item,parent,false);
        }else if(viewType==3){
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_lineup_second_title,parent,false);
        }else{
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_lineup_item,parent,false);
        }
        return new CLSMatchLineupViewHodler(view);
    }

    @Override
    public void onBindViewHolder(CLSMatchLineupViewHodler holder, int position) {
        CLSMatchLineupEntity info=matchLineupEntityList.get(position);
        int type=getItemViewType(position);
        if(type==1){
            holder.tvHomeTeamInfo.setText(info.getHomePlayerName());
            holder.tvAwayTeamInfo.setText(info.getAwayPlayerName());
        }else if(type==3){
            holder.tvTitle.setText("替补");
        }else{
            if(!TextUtils.isEmpty(info.getHomePlayerName())){
                holder.tvHomeTeamInfo.setText(info.getHomePlayerNo()+" "+info.getHomePlayerName());
            }
            if(!TextUtils.isEmpty(info.getAwayPlayerName())){
                holder.tvAwayTeamInfo.setText(info.getAwayPlayerNo()+" "+info.getAwayPlayerName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return matchLineupEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type=matchLineupEntityList.get(position).getType();
        return type;
    }

    public static final class CLSMatchLineupViewHodler extends RecyclerView.ViewHolder{
        TextView tvHomeTeamInfo;
        TextView tvAwayTeamInfo;
        TextView tvTitle;
        public CLSMatchLineupViewHodler(View itemView) {
            super(itemView);
            tvHomeTeamInfo=(TextView)itemView.findViewById(R.id.tv_home_team_info);
            tvAwayTeamInfo=(TextView)itemView.findViewById(R.id.tv_away_team_info);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
        }
    }
}
