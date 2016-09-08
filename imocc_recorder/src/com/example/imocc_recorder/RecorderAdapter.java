package com.example.imocc_recorder;

import java.util.List;

import com.example.imocc_recorder.MainActivity.Recorder;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecorderAdapter extends ArrayAdapter<Recorder>{

	private List<Recorder> mDatas;
	private Context mContext;
	
	private int mMinItemWidth;
	private int mMaxItemWidth;
	
	private LayoutInflater mLayoutInflater;
	
	public RecorderAdapter(Context context, List<Recorder> datas) {
		super(context, -1, datas);
		this.mDatas = datas;
		mContext = context;
		
		mLayoutInflater = LayoutInflater.from(context);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		
		mMaxItemWidth = (int) (outMetrics.widthPixels*0.7f);
		mMinItemWidth = (int) (outMetrics.widthPixels*0.15f);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if(convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.item_recorder, parent, false);
			viewHolder.seconds = (TextView) convertView.findViewById(R.id.id_recorder_time);
			viewHolder.length = convertView.findViewById(R.id.id_recorder_length);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.seconds.setText(Math.round(getItem(position).time)+"\"");
		ViewGroup.LayoutParams layoutParams = viewHolder.length.getLayoutParams();
		layoutParams.width = (int)(mMinItemWidth+(mMaxItemWidth/60f*getItem(position).time));
		return convertView;
	}
	
	class ViewHolder
	{
		TextView seconds;
		View length;
	}
}
