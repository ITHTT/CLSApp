package com.tysci.cls.views.videorecord;

import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/10/26.
 */
public class VideoFile {
    /**录制视频的根目录*/
    private String videoDirs;
    /**录制视频文件名*/
    private String videoName;
    /**文件大小*/
    private long videoFileSize;
    /**文件时长*/
    private long videoTimes;

    private Date mDate;

    private static final String DATE_FORMAT			= "yyyyMMdd_HHmmss";
    private static final String DEFAULT_PREFIX		= "video_";
    private static final String DEFAULT_EXTENSION	= ".mp4";

    public VideoFile(String videoDirs,String videoName){
        this.videoDirs=videoDirs;
        this.videoName=videoName;
    }

    public long getVideoFileSize() {
        return videoFileSize;
    }

    public void setVideoFileSize(long videoFileSize) {
        this.videoFileSize = videoFileSize;
    }

    public long getVideoTimes() {
        return videoTimes;
    }

    public void setVideoTimes(long videoTimes) {
        this.videoTimes = videoTimes;
    }

    public String getFullPath() {
        return getFile().getAbsolutePath();
    }

    public File getFile() {
		/*
		final String filename = generateFilename();
		if (filename.contains(DIRECTORY_SEPARATOR)) return new File(filename);

		final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		path.mkdirs();
		return new File(path, generateFilename());
		*/
        File path=new File(videoDirs);
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path,generateFilename());
    }


    private String generateFilename() {
        if (isValidFilename()) return videoName;

        final String dateStamp = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(getDate());
        return DEFAULT_PREFIX + dateStamp + DEFAULT_EXTENSION;
    }

    public boolean isValidFilename() {
        if(TextUtils.isEmpty(videoName)){
            return false;
        }

        return true;
    }

    private Date getDate() {
        if (mDate == null) {
            mDate = new Date();
        }
        return mDate;
    }




}
