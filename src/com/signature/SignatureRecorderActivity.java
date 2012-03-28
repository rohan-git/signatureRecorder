package com.signature;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class SignatureRecorderActivity extends Activity {
	/** Called when the activity is first created. */

	private SurfaceView surfaceView;
	SignatureView sv;

	// List<Float> xList;
	// List<Float> yList;
	Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.signature);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		sv = (SignatureView) findViewById(R.id.sign);

		// System.out.println("Here");

		Button b1 = (Button) findViewById(R.id.b1);
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveStuff();

			}
		});
		// signatureView = new SignatureView(this);
		// setContentView(R.layout.signature);
		// signatureView.requestFocus();

		// signatureView.getPaintThread();

	}

	// @Override
	// protected void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// saveStuff();
	// }
	//
	// @Override
	// protected void onStop() {
	// // TODO Auto-generated method stub
	// super.onStop();
	// saveStuff();
	// }

	private void saveStuff() {

		this.bitmap = Bitmap.createBitmap(sv.getBitmap());
		
		String locatin = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "STORED FILE", "");

		Toast.makeText(this, "saved to gallery ..hopefully!", Toast.LENGTH_SHORT).show();
		Log.e("SSS", locatin);
		// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		// this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		//
		// FileOutputStream fos;
		// try {
		// fos = openFileOutput("SAVED CANVAS", Context.MODE_PRIVATE);
		// fos.write(bytes.toByteArray());
		// fos.close();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
	}
}