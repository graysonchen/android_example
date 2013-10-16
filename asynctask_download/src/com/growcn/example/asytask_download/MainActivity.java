package com.growcn.example.asytask_download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private Button button;
	private ImageView imageView;
	private String image_path = "http://g.hiphotos.baidu.com/album/h%3D800%3Bcrop%3D0%2C0%2C1280%2C800/sign=80b2de3bb21c8701c9b6bfe61744fd5d/1f178a82b9014a9058c22662a8773912b21bee75.jpg";
	//private String image_path ="https://www.google.com/images/srpr/logo4w.png";
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在下载，请稍后... ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		button = (Button) this.findViewById(R.id.button1);
		imageView = (ImageView) this.findViewById(R.id.imageView1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new MyTask().execute(image_path);
			}
		});

	}

	// <传入的值,进程刻度,返回的值>
	public class MyTask extends AsyncTask<String, Integer, Bitmap> {

		// 第一步
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		// 第三步
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			button.setText("测试");
			imageView.setImageBitmap(result);
			dialog.dismiss();			
		}

		// 第四步
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.setProgress(values[0]);
		}

		// 第二步，完成对图片下载的功能
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			InputStream inputStream = null;
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse httpRespone = httpClient.execute(httpGet);
				if (httpRespone.getStatusLine().getStatusCode() == 200) {
					
					inputStream = httpRespone.getEntity().getContent();
					// 先要获取得文件的总长度
					long file_length = httpRespone.getEntity()
							.getContentLength();
					int len = 0;
					byte[] data = new byte[1024];
					int total_length = 0;
					while ((len = inputStream.read(data)) != -1) {
						total_length += len;
						int value = (int) ((total_length / (float) file_length) * 100);
						Log.e("file_length", "-----------" + value);						
						publishProgress(value);// 发布刻度上
						outputStream.write(data, 0, len);// 写进流数组中
					}
					byte[] result = outputStream.toByteArray();
					bitmap = BitmapFactory.decodeByteArray(result, 0,
							result.length);
					
//					HttpEntity httpEntity = httpRespone.getEntity();
//					byte[] datapic = EntityUtils.toByteArray(httpEntity);
//					bitmap = BitmapFactory.decodeByteArray(datapic, 0,
//							datapic.length);						
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return bitmap;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
