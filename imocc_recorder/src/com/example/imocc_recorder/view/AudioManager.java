package com.example.imocc_recorder.view;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.util.Log;

public class AudioManager {

	private MediaRecorder mMediaRecorder;
	private String mDir;
	private String mCurrentFilePath;
	
	private static AudioManager mInstance;
	
	private boolean isPrepared = false;
	
	public AudioManager(String mDir) {
		this.mDir = mDir;
	}
	
	/**
	 * 回调准备完毕
	 * @author lenovo
	 *
	 */
	public interface AudioStateListener
	{
		void wellPrepared();
	}
	
	public AudioStateListener mListener;
	
	public void setOnAudioStateListener(AudioStateListener mListener) {
		this.mListener = mListener;
	}

	public static AudioManager getInstance(String mDir)
	{
		if(mInstance == null)
		{
			synchronized (AudioManager.class)
			{
				mInstance = new AudioManager(mDir);
			}
		}
		return mInstance;
	}
	
	public void prepareAudio()
	{
		try {
			isPrepared = false;
			File dir = new File(mDir);
			if(!dir.exists())
				dir.mkdirs();
			
			String fileName = generateName();
			File file = new File(dir, fileName);
			
			mCurrentFilePath = file.getAbsolutePath();
			mMediaRecorder = new MediaRecorder();
			//设置输出文件
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			//设置音频源为麦克风
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//设置音频格式
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			//设置音频编码
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			
//			mMediaRecorder.setAudioSamplingRate(8000);
			
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			
			isPrepared = true;
			if(mListener != null)
				mListener.wellPrepared();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private String generateName() {
		return UUID.randomUUID().toString()+".amr";
	}

	public int getVoiceLevel(int maxLevel)
	{
		if(isPrepared)
		{
			try {
				//mMediaRecorder.getMaxAmplitude() 1-32767
				//注意此处mMediaRecorder.getMaxAmplitude 只能取一次，如果前面取了一次，后边再取就为0了
				return ((mMediaRecorder.getMaxAmplitude()*maxLevel)/32768)+1;
			} catch (Exception e) {
			}
		}
		return 1;
	}
	
	public void release()
	{
		if(mMediaRecorder != null)
		{
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}
	
	public void cancel()
	{
		release();
		if(mCurrentFilePath != null)
		{
			File file = new File(mCurrentFilePath);
			if(file.exists())
			{
				file.delete();
				mCurrentFilePath = null;
			}
		}
	}

	public String getCurrentFilePath() {
		return mCurrentFilePath;
	}
}
