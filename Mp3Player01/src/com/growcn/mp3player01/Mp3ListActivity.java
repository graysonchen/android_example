package com.growcn.mp3player01;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.growcn.mp3player01.R;
import com.growcn.mp3player01.download.*;
import com.growcn.mp3player01.model.*;
import com.growcn.mp3player01.service.DownloadService;
import com.growcn.mp3player01.xml.Mp3ListContentHandler;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import com.growcn.mp3player01.Definition;;
 

public class Mp3ListActivity extends ListActivity implements Definition{
	
	private static final int UPDATE =1;
	private static final int ABOUT  =2;
	private static final int EXIT   =3;
	private List<Mp3Info> mp3Infos  =null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"start..........init");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_mp3_list);
		updateListView();

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,UPDATE,1,R.string.mp3list_update);
		menu.add(0,ABOUT,2,R.string.mp3list_about);
		menu.add(0,EXIT,2,R.string.mp3list_exit);		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG,"item--------:"+item.getItemId());
		
		if(item.getItemId()==UPDATE){
			updateListView();
		}else if(item.getItemId()==ABOUT){
			
		}else if(item.getItemId()==EXIT){
		    //finish this Application
            finish();
            //kill this process to ensure the second time entering game center OK when the first time network is unavailable
            System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateListView() {
		String urlStr = URL_RESOURCES_XML;
		String xml = downloadXML(urlStr);
		mp3Infos = parse(xml);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
		setListAdapter(simpleAdapter);
		
	}

	
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[] { R.id.mp3_name, R.id.mp3_size });

		return simpleAdapter;
	}


	private String downloadXML(String urlStr) {
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlStr);
		//Log.e(TAG,"xml--------:"+result);		
		return result;
	}
	
	
	
	
	private List<Mp3Info> parse(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(
					infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
				Log.i(TAG,mp3Info.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//Log.i(TAG,"---"+mp3Infos.get(position));
		Mp3Info mp3info = mp3Infos.get(position);
		Intent intent = new Intent();
		intent.putExtra("mp3info", mp3info);
		intent.setClass(this, DownloadService.class);
		startService(intent);
		
		super.onListItemClick(l, v, position, id);
	}	



}
