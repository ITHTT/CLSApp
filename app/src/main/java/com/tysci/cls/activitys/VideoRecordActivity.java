package com.tysci.cls.activitys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.app.AppConfigInfo;
import com.tysci.cls.utils.EncryptUtil;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.utils.ToastUtil;
import com.tysci.cls.views.videorecord.AlreadyUsedException;
import com.tysci.cls.views.videorecord.CameraWrapper;
import com.tysci.cls.views.videorecord.CaptureConfiguration;
import com.tysci.cls.views.videorecord.VideoFile;
import com.tysci.cls.views.videorecord.VideoRecorder;
import com.tysci.cls.views.videorecord.VideoRecorderInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/10/23.
 */
public class VideoRecordActivity extends AppCompatActivity implements VideoRecorderInterface,View.OnClickListener {
    private String TAG="MideoRecordActivity";
    private static final int DEFAULT_VIDEO_WIDTH=480;
    private static final int DEFAULT_VIDEO_HEIGHT=480;
    private static final int DEFAULT_VIDEO_BITRATE=1000000;
    private static final int DEFAULT_MAX_RECORD_VIDEO_TIMES=60;
    private static final int DEFAULT_MAX_RECORD_VIDEO_SIZE=10;
    /**录制过程标记码*/
    private static final int RECORD_PROGRESS_CODE=0x0001;

    private ImageView ivBack;
    private CheckBox cbPhotoFlash;
    private CheckBox cbSwitchCamera;
    private ImageView ivDelete;
    private ImageView ivSave;
    private Button btRecord;
    private ProgressBar pbRecordProgress;
    private TextView tvRecordTime;

    /**显示摄像头画面*/
    private SurfaceView surfaceView;

    /**配置录制视频的参数*/
    private CaptureConfiguration mCaptureConfiguration;
    /**视频录制的类*/
    private VideoRecorder mVideoRecorder;
    /**录制视频后的文件*/
    private VideoFile videoFile;
    /**录制时长*/
    private long recordTimes=0;
    /**计时器*/
    private Timer timer;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==RECORD_PROGRESS_CODE){
                pbRecordProgress.setProgress((int) recordTimes * 100 / DEFAULT_MAX_RECORD_VIDEO_TIMES);
                String timeText="00:00:";
                if(recordTimes<10){
                    timeText=timeText+"0"+recordTimes;
                }else{
                    timeText=timeText+recordTimes;
                }
                tvRecordTime.setText(timeText);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Logger.init(TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        initViews();
    }

    private void initViews(){
        ivBack=(ImageView)this.findViewById(R.id.iv_back);
        cbPhotoFlash=(CheckBox)this.findViewById(R.id.cb_photoflash);

        cbSwitchCamera=(CheckBox)this.findViewById(R.id.cb_swithc_camera);
        cbSwitchCamera.setOnClickListener(this);

        ivDelete=(ImageView)this.findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(this);
        ivSave=(ImageView)this.findViewById(R.id.iv_save);
        ivSave.setOnClickListener(this);
        btRecord=(Button)this.findViewById(R.id.bt_record);
        btRecord.setOnClickListener(this);
        surfaceView=(SurfaceView)this.findViewById(R.id.surfaceView);
        pbRecordProgress=(ProgressBar)this.findViewById(R.id.record_progress);
        tvRecordTime=(TextView)this.findViewById(R.id.tv_record_time);

        //initRecordVideo();
    }

    private void initRecordVideo(){
        initVideoFile();
        initRecordVideoConfiguration();
        if(mVideoRecorder==null) {
            mVideoRecorder = new VideoRecorder(this, mCaptureConfiguration, videoFile, new CameraWrapper(), surfaceView.getHolder());
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private void initVideoFile(){
        if(videoFile==null) {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            if (sdCardExist) {
                String fileName = System.currentTimeMillis() + ".mp4";
                videoFile = new VideoFile(AppConfigInfo.APP_VIDEO_PATH, fileName);
            } else {
                ToastUtil.show(this,"SD卡不可用");
                finish();
            }

        }
    }

    private void initRecordVideoConfiguration(){
        if(mCaptureConfiguration==null) {
            mCaptureConfiguration = new CaptureConfiguration(DEFAULT_VIDEO_WIDTH,
                    DEFAULT_VIDEO_HEIGHT,
                    DEFAULT_VIDEO_BITRATE,
                    DEFAULT_MAX_RECORD_VIDEO_TIMES,
                    DEFAULT_MAX_RECORD_VIDEO_SIZE);
        }
    }

    @Override
    protected void onResume() {
        initRecordVideo();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mVideoRecorder != null) {
            mVideoRecorder.stopRecording(null);
        }
        releaseAllResources();
        super.onPause();
    }

    private void startRecordVideo(){
        try {
            mVideoRecorder.toggleRecording();
        } catch (AlreadyUsedException e) {
            //CLog.d(CLog.ACTIVITY, "Cannot toggle recording after cleaning up all resources");
            return;
        }
    }

    /**
     * 删除录制的视频文件
     */
    private void deleteVideoFile(){
        File file=new File(videoFile.getFullPath());
        if(file!=null&&file.exists()){
            file.delete();
        }
        ivDelete.setVisibility(View.INVISIBLE);
        ivSave.setVisibility(View.INVISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private void saveVideoFile(){
//        Intent intent=new Intent(this,VideoPreviewActivity.class);
//        intent.putExtra(AppParams.DATA_MEDIA_FILE, videoFile.getFullPath());
//        this.startActivity(intent);
        Intent intent=getIntent();
        KLog.e("file_time_length:"+videoFile.getVideoTimes());
        KLog.e("file_path:"+videoFile.getFullPath());

        String path=videoFile.getFullPath();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile.getFullPath());
        long times=Long.parseLong(retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION));
        Bitmap coverBitmap=retriever.getFrameAtTime();
        String converPath=AppConfigInfo.APP_VIDEO_PATH+File.separator+EncryptUtil.getInstance().MD5(path)+".jpg";
        File converBitmapFile=new File(converPath);
        if(!converBitmapFile.exists()){
            try {
                converBitmapFile.createNewFile();
                FileOutputStream outputStream=new FileOutputStream(converBitmapFile);
                coverBitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        intent.putExtra("video_path",path);
        intent.putExtra("video_times",times);
        intent.putExtra("video_cover",converBitmapFile.getAbsolutePath());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onRecordingStopped(String message) {
        //Logger.i(TAG,"录制停止"+message);
        timer.cancel();
        timer=null;
        recordTimes=0;
        pbRecordProgress.setProgress(0);
        btRecord.setSelected(false);
        btRecord.setText("录制");
        ivDelete.setVisibility(View.VISIBLE);
        ivSave.setVisibility(View.VISIBLE);
        if(cbSwitchCamera.getVisibility()== View.GONE){
            cbSwitchCamera.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecordingStarted() {
       // Logger.i(TAG,"开始录制...");
        if(timer==null){
            timer=new Timer();
        }
        /**开启计时器*/
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                recordTimes+=1;
                handler.sendEmptyMessage(RECORD_PROGRESS_CODE);
            }
        },0,1000);
        btRecord.setSelected(true);
        btRecord.setText("停止");
        ivDelete.setVisibility(View.GONE);
        ivSave.setVisibility(View.GONE);
        /**录制时禁止切换摄像头*/
        cbSwitchCamera.setVisibility(View.GONE);
    }

    @Override
    public void onRecordingSuccess() {
        //Logger.i(TAG, "录制成功...");
    }

    @Override
    public void onRecordingFailed(String message) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_record:
                startRecordVideo();
                break;
            case R.id.iv_delete:
                deleteVideoFile();
                break;
            case R.id.iv_save:
                saveVideoFile();
                break;
            case R.id.cb_swithc_camera:
                if(mVideoRecorder!=null){
                    mVideoRecorder.switchCamera();
                }
        }
    }

    private void releaseAllResources() {
        if (mVideoRecorder != null) {
            mVideoRecorder.releaseAllResources();
            mVideoRecorder=null;
        }
    }

}
