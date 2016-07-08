package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.app.BaseActivity;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.fragments.CLSMatchCommentBallFragment;
import com.tysci.cls.fragments.CLSMatchEventFragment;
import com.tysci.cls.fragments.CLSMatchLineupFragment;
import com.tysci.cls.fragments.CLSMatchStatisticsFragment;
import com.tysci.cls.fragments.CLSMatchTipOffListFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.networks.GlideImageLoader;
import com.tysci.cls.views.adapters.CustomFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchDetailActivity extends BaseActivity{
    @Bind(R.id.tv_match_time)
    protected TextView tvMatchTime;
    @Bind(R.id.iv_home_team_icon)
    protected ImageView ivHomeTeamIcon;
    @Bind(R.id.tv_home_team_name)
    protected TextView tvHomeTeamName;
    @Bind(R.id.tv_home_team_score)
    protected TextView tvHomeTeamScroe;
    @Bind(R.id.iv_away_team_icon)
    protected ImageView ivAwayTeamIcon;
    @Bind(R.id.tv_away_team_name)
    protected TextView tvAwayTeamName;
    @Bind(R.id.tv_away_team_score)
    protected TextView tvAwayTeamScore;
    @Bind(R.id.tab_layout)
    protected TabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private String[] titles={"阵容","爆料","论球","事件","统计"};
    private CLSMatchEntity matchEntity;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_match_detail;
    }

    @Override
    protected void initViews() {
        setTitle("中超赛场-比赛详情");
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        matchEntity=intent.getParcelableExtra(Tag);
        if(matchEntity!=null){
            initMatchDetail(matchEntity);
            addFragments(matchEntity);
        }
    }

    private void addFragments(CLSMatchEntity matchEntity){
        List<BaseFragment> fragments=new ArrayList<>(5);

        Bundle data=new Bundle();
        data.putParcelable("match_info",matchEntity);

        BaseFragment baseFragment=new CLSMatchLineupFragment();
        baseFragment.setArguments(data);
        fragments.add(baseFragment);

        baseFragment=new CLSMatchTipOffListFragment();
        fragments.add(baseFragment);

        baseFragment=new CLSMatchCommentBallFragment();
        fragments.add(baseFragment);

        baseFragment=new CLSMatchEventFragment();
        fragments.add(baseFragment);

        baseFragment=new CLSMatchStatisticsFragment();
        fragments.add(baseFragment);

        CustomFragmentPagerAdapter adapter=new CustomFragmentPagerAdapter(getSupportFragmentManager(),titles,fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initMatchDetail(CLSMatchEntity matchEntity){
        tvMatchTime.setText(matchEntity.getMatchTime());
        tvHomeTeamName.setText(matchEntity.getHomeTeamName());
        GlideImageLoader.loadImage(this, matchEntity.getHomeTeamFlag(), R.mipmap.ic_launcher, ivHomeTeamIcon);
        tvAwayTeamName.setText(matchEntity.getAwayTeamName());
        GlideImageLoader.loadImage(this, matchEntity.getAwayTeamFlag(), R.mipmap.ic_launcher, ivAwayTeamIcon);
        tvHomeTeamScroe.setText(String.valueOf(matchEntity.getHomeTeamScore()));
        tvAwayTeamScore.setText(String.valueOf(matchEntity.getAwayTeamScore()));

    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
