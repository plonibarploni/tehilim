package com.karriapps.tehilimlibrary;

import com.karriapps.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.TehilimTextView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class MainFragment extends Fragment {
	
	public enum VIEW_TYPE {TEHILIM_BOOK, OTHER};
	
	private ListView mList;
	private Button mUpperButton;
	private Button mBottomButton;
	
	private VIEW_TYPE mViewType;
	
	private TehilimGenerator mTehilimGenerator;
	
	private TehilimAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.tehilim, container, false);
		
		mList = (ListView)view.findViewById(R.id.list);
		mUpperButton = (Button)view.findViewById(R.id.upperJumpTo);
		mBottomButton = (Button)view.findViewById(R.id.bottomJumpTo);
		
		mAdapter = new TehilimAdapter();
		mList.setAdapter(mAdapter);
		
		return view;
	}
	
	public void setVieeType(VIEW_TYPE type) {
		mViewType = type;
		
		if(mViewType.equals(VIEW_TYPE.TEHILIM_BOOK)) {
			mList.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					
					if(getTehilimGenerator() instanceof PsalmsGenerator) {
						if(view.getAdapter().getItemId(firstVisibleItem) <= ((PsalmsGenerator)getTehilimGenerator()).getFirstVisibleChapter() ){
							mUpperButton.setVisibility(View.VISIBLE);
						} else
							mUpperButton.setVisibility(View.GONE);
						
						if(((PsalmsGenerator)getTehilimGenerator()).getLastVisibleChapter() <= 
								view.getAdapter().getItemId((firstVisibleItem + (visibleItemCount - 1)))){
							mBottomButton.setVisibility(View.VISIBLE);
						} else
							mBottomButton.setVisibility(View.GONE);
					}
				}
			});
		}
	}
	
	public int getPosition() {
		return mList.getLastVisiblePosition();
	}
	
	public void setPosition(int position) {
		mList.setSelection(position);
	}
	
	public TehilimGenerator getTehilimGenerator() {
		return mTehilimGenerator;
	}

	public void setTehilimGenerator(TehilimGenerator mTehilimGenerator) {
		this.mTehilimGenerator = mTehilimGenerator;
		mAdapter.notifyDataSetChanged();
	}

	private class TehilimAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return getTehilimGenerator() == null ? 0 : getTehilimGenerator().getKeys().size();
		}

		@Override
		public Perek getItem(int position) {
			return getTehilimGenerator().getList().get(getTehilimGenerator().getKeys().get(position));
		}

		@Override
		public long getItemId(int position) {
			return getTehilimGenerator().getList().get(getTehilimGenerator().getKeys().get(position)).getchapterID();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			
			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater) App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.tehilim_row, parent, false);
				holder.mTextView = (TehilimTextView)convertView.findViewById(R.id.text);
				holder.mTitleTextView = (TehilimTextView)convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			String tText = getItem(position).getTitle();
			if(!getItem(position).isInScope())
				tText = "<font color=\"#aaaaaa\">" + tText + "</font>";
			holder.mTitleTextView.setText(Html.fromHtml(tText));
			
			String pText = getItem(position).getText();
			if(!getItem(position).isInScope())
				pText = "<font color=\"#aaaaaa\">" + pText + "</font>";
			holder.mTextView.setText(Html.fromHtml(pText));
			
			return convertView;
		}
		
		class ViewHolder {
			TehilimTextView mTitleTextView;
			TehilimTextView mTextView;
		}
	}
}