package com.example.imocc_recorder;

import java.util.ArrayList;
import java.util.List;

import com.example.imocc_recorder.view.AudioRecorderButton;
import com.example.imocc_recorder.view.AudioRecorderButton.AudioFinishRecorderListener;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity{

	private ArrayAdapter<Recorder> mAdapter;
	private List<Recorder> mDatas = new ArrayList<Recorder>();
	private ListView mListView;
	
	private AudioRecorderButton mAudioRecorderButton;
	
	private View mAnimView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.id_listview);
		mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
		
		mAudioRecorderButton.setAudioFinishRecorderListener(new AudioFinishRecorderListener() {
			
			@Override
			public void onFinish(float time, String filepath) {
				Recorder recorder = new Recorder(time, filepath);
				mDatas.add(recorder);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mDatas.size()-1);
			}
		});
		
		mAdapter = new RecorderAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//播放帧动画
				mAnimView = view.findViewById(R.id.id_recorder_anim);
				mAnimView.setBackgroundResource(R.drawable.play_anim);
				AnimationDrawable anim = (AnimationDrawable) mAnimView.getBackground();
				anim.start();
				
				//播放声音
				MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener(){

					@Override
					public void onCompletion(MediaPlayer mp) {
						mAnimView.setBackgroundResource(R.drawable.adj);
					}
				});
			}
			
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MediaManager.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MediaManager.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MediaManager.release();
	}
	
	class Recorder{
		float time;
		String filePath;
		public Recorder(float time, String filePath) {
			this.time = time;
			this.filePath = filePath;
		}
		public float getTime() {
			return time;
		}
		public void setTime(float time) {
			this.time = time;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
	}
}
