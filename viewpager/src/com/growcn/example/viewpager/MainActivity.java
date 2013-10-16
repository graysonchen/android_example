package com.growcn.example.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {

	private ViewPager viewPager;
	private PagerTitleStrip pagerTitleStrip;
	private MyAdapter adapter = null;

	private List<View> list = null;
	private List<String> title = null;

	//private LayoutInflater inflater = null;
	private View view1 = null;
	private View view2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Resources res = getResources();
		viewPager = (ViewPager) this.findViewById(R.id.viewpager);
		pagerTitleStrip = (PagerTitleStrip) this.findViewById(R.id.patertitle);

		adapter = new MyAdapter();

		// 加载布局
		view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab, null);
		view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab2, null);
		
		list = new ArrayList<View>();
		list.add(view1);
		list.add(view2);

		title = new ArrayList<String>();
		title.add(res.getString(R.string.tab1));
		title.add(res.getString(R.string.tab2));

		// 先初始化页面
		viewPager.setAdapter(adapter);

		
	    viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override 
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				Log.i("test","---------"+arg0);
				//在这里开启线程云下载网数据
//				View view = inflater.inflate(R.layout.tab, null);
//				list.add(view);
//				adapter.notifyDataSetChanged();
			}
		});
	}

	public class MyAdapter extends PagerAdapter {
		@Override
		public CharSequence getPageTitle(int position) {
			return title.get(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(list.get(position));
			// super.destroyItem(container, position, object);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
