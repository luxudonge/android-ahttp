package com.alex.rss;

import java.util.ArrayList;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alex.http.activity.R;

public class RssItemAdapter extends BaseAdapter {
	
	public RssItemAdapter(LayoutInflater inflater){
		this.mInflater = inflater;
	}

	LayoutInflater mInflater;
	ArrayList<RssItem> mRssItemList;
	@Override
	public int getCount() {
		int size = 0;
		if(mRssItemList != null)
			size = mRssItemList.size();
		return size;
	}
	
	public void setData(ArrayList<RssItem> itemList){
		mRssItemList = itemList;
		this.notifyDataSetChanged();
	}

	public ArrayList<RssItem> getData(){
		return this.mRssItemList;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RssItem item = mRssItemList.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.rss_item, null);
			holder.titleText = (TextView) convertView.findViewById(R.id.titleText);
			holder.pubDateText = (TextView) convertView.findViewById(R.id.pubDateText);
			holder.descriptionText = (TextView) convertView.findViewById(R.id.descriptionText);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleText.setText(item.getTitle());
		holder.pubDateText.setText(Html.fromHtml(item.getPubData()));
		holder.descriptionText.setText(Html.fromHtml(item.getDescription()));
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView titleText;
		TextView pubDateText;
		TextView descriptionText;
		
	}

}
