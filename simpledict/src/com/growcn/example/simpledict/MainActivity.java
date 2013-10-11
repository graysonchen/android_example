package com.growcn.example.simpledict;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	//定义数据库的存放路径
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/dict";
	//定义数据库的名字
	private final String DATABASE_FILENAME = "dict.db";
	private SQLiteDatabase database;
	
	//用户显示查询结果
	private TextView showResult;
	
	private Button searchButton;
	
	//用户输入文本框
	private AutoCompleteTextView word;
	
	String GAME[]=new String[]{"game1","gam2","gamm3","gamek3"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//打开数据库
		database = openDatabase();		
		
		word = (AutoCompleteTextView) findViewById(R.id.word);
		//绑定监听器
		word.addTextChangedListener(new textChangedListen());
		
		searchButton = (Button) findViewById(R.id.searchWord);
		//绑定监听器
		searchButton.setOnClickListener(new buttonClickListener());	

		
		showResult=(TextView)findViewById(R.id.result);		
		
	}
	
	private class buttonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//查询指定的单词
			String sql = "select chinese from t_words where english=?";		
			Cursor cursor = database.rawQuery(sql, new String[]
			{word.getText().toString()});
				String result = "未找到该单词.";
				//  如果查找单词，显示其中文的意思
				if (cursor.getCount() > 0)
				{
					//  必须使用moveToFirst方法将记录指针移动到第1条记录的位置
					cursor.moveToFirst();
					result = cursor.getString(cursor.getColumnIndex("chinese")).replace("&amp;", "&");
				}
				//将结果显示到TextView中
				showResult.setText(word.getText()+"\n"+result.toString());			
		}
		
	}
	
	
	private class textChangedListen implements TextWatcher {		
		@Override
		public void afterTextChanged(Editable s) {
	        //  必须将english字段的别名设为_id 
			Cursor cursor = database.rawQuery(
					"select english as _id from t_words where english like ?",
					new String[]
					{ s.toString() + "%" });
			//新建新的Adapter
			DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(MainActivity.this,cursor, true);
			//绑定适配器
			word.setAdapter(dictionaryAdapter);			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}

	}
	
	

	private SQLiteDatabase openDatabase()
	{
		try
		{
			// 获得dictionary.db文件的绝对路径
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			// 如果/sdcard/dictionary目录中存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			// 如果在/sdcard/dictionary目录中不存在
			// dictionary.db文件，则从res\raw目录中复制这个文件到
			// SD卡的目录（/sdcard/dictionary）
			if (!(new File(databaseFilename)).exists())
			{
				// 获得封装dictionary.db文件的InputStream对象
				InputStream is = getResources().openRawResource(
						R.raw.dict);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// 开始复制dictionary.db文件
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}
				//关闭文件流
				fos.close();
				is.close();
			}
			// 打开/sdcard/dictionary目录中的dictionary.db文件
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return database;
		}
		catch (Exception e)
		{
			Log.e("error_db","DB IS ERRORS!");
		}
		//如果打开出错，则返回null
		return null;
	}	
	
}
