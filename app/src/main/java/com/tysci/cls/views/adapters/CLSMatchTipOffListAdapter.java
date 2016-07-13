package com.tysci.cls.views.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.modles.CLSMatchTipOffEntity;
import com.tysci.cls.networks.GlideImageLoader;
import com.tysci.cls.utils.CommonUtils;
import com.tysci.cls.views.widgets.CircleImageView;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CLSMatchTipOffListAdapter extends RecyclerView.Adapter<CLSMatchTipOffListAdapter.CLSMatchTipOffListViewHolder>{
    private List<CLSMatchTipOffEntity> matchTipOffEntityList=null;

    public CLSMatchTipOffListAdapter(List<CLSMatchTipOffEntity> matchTipOffEntityList) {
        this.matchTipOffEntityList = matchTipOffEntityList;
    }

    @Override
    public CLSMatchTipOffListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_tip_off_item,parent,false);
        return new CLSMatchTipOffListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CLSMatchTipOffListViewHolder holder, int position) {
        CLSMatchTipOffEntity info=matchTipOffEntityList.get(position);
        GlideImageLoader.loadImage(holder.itemView.getContext(), "http://static-cdn.ballq.cn/"+info.getPt(), R.mipmap.icon_user_default, holder.ivUserHeader);
        //holder.tvUserName.setText(info.getFname());
        CommonUtils.setTextViewFormatString(holder.tvUserName,"球商APP作家:"+info.getFname(),"球商APP作家", Color.parseColor("#78a639"),1.1f);
        holder.tvTipOffContent.setText(info.getCont());
        holder.tvTipOffTime.setText(info.getCtime());
        String bettingInfo="投注场次:"+info.getTipcount()+" 亚盘:"+(int)(info.getWins()*100)+"% 盈利:"+String.format(Locale.getDefault(),"%.2f",info.getRor())+"%";
        holder.tvUserBettingInfo.setText(bettingInfo);
        setBettingResult(holder,info.getStatus());
    }

    private void setBettingResult(CLSMatchTipOffListViewHolder holder,int status){
        switch (status){
            case 1:
                holder.ivBetResult.setImageResource(R.mipmap.icon_tip_game_win);
                break;
            case 2:
                holder.ivBetResult.setImageResource(R.mipmap.icon_tip_game_lose);
                break;
            case 3:
                holder.ivBetResult.setImageResource(R.mipmap.icon_tip_game_gone);
                break;
            default:
                holder.ivBetResult.setImageResource(0);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return this.matchTipOffEntityList.size();
    }

    public static final class CLSMatchTipOffListViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_user_heaeder)
        CircleImageView ivUserHeader;
        @Bind(R.id.tv_user_name)
        TextView tvUserName;
        @Bind(R.id.tv_user_betting_info)
        TextView tvUserBettingInfo;
        @Bind(R.id.tv_tip_off_time)
        TextView tvTipOffTime;
        @Bind(R.id.tv_tip_off_content)
        TextView tvTipOffContent;
        @Bind(R.id.iv_betting_result)
        ImageView ivBetResult;


        public CLSMatchTipOffListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
