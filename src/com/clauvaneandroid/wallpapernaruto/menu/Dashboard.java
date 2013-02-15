package com.clauvaneandroid.wallpapernaruto.menu;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.airpush.android.Airpush;
import com.clauvaneandroid.wallpapernaruto.menu.Dashboard;
import com.clauvaneandroid.wallpapernaruto.quiz.SplashActivity;
import com.clauvaneandroid.wallpapernaruto.R;
import com.clauvaneandroid.wallpapernaruto.contato.Contato;
import com.clauvaneandroid.wallpapernaruto.puzzle.ListarPuzzles;
import com.clauvaneandroid.wallpapernaruto.util.Conexao;
import com.clauvaneandroid.wallpapernaruto.wallpaper.ListarWallpapers;

public class Dashboard extends Activity {
	/** Called when the activity is first created. */
	private Handler handler = new Handler();
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	InputStream aux = null;
	StringBuilder sb=null;
	ArrayList<NameValuePair> nameValuePairs;
	ArrayList<NameValuePair> nameValuePairsUpdate;
	int visitas;
	private static final String NOME = "FanClub";
	Conexao conectado;
	Airpush airpush;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Recupera o valor do contador, salvo nas preferencias
		SharedPreferences pref = getSharedPreferences(NOME, 0);
//		boolean primeira = pref.getBoolean("primeira", false);
//		if(!primeira){
//			startActivity(new Intent(this, Bemvindo.class));
//			finish();
//		}
		setContentView(R.layout.dashboard);

		//Teste de conexão
		conectado = new Conexao(this);
//		if(primeira){
			if(conectado.conectado()){
				handler.post(new Runnable(){
					public void run() {							
						airpush = new Airpush(Dashboard.this);
						airpush.startSmartWallAd(); //launch smart wall on App start
						airpush.startPushNotification(false);
						airpush.startIconAd();					
					}});
			}
//		}

		visitas = pref.getInt("visitas", 0 );
		if(visitas % 30 == 15){
			/** AlertDialog com Sim e N�o **/
			AlertDialog.Builder alerta = new AlertDialog.Builder(Dashboard.this);
			alerta.setMessage("Hi Naruto  lover! Do you like this app? Please help grow up our Fan Club!");
			// M�todo executado se escolher Sim
			alerta.setPositiveButton("Share", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String msg = "I love Naruto ! If you love Naruto  too, join our Naruto  Live Wallpaper on Android! \n https://play.google.com/store/apps/details?id=com.clauvaneandroid.wallpapernaruto";
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_TEXT, msg);
					startActivity(Intent.createChooser(intent, "Share with"));
				}
			});
			// M�todo executado se escolher N�o
			alerta.setNegativeButton("Rate", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.clauvaneandroid.wallpapernaruto");
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
				}
			});
			// Exibe o alerta de confirma��o
			alerta.show();

		}
		salvarVisitas();		

		//Wallpaper
		Button wallpaper = (Button) findViewById(R.id.home_btn_wallpaper);
		wallpaper.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Dashboard.this,ListarWallpapers.class));
			}
		}); 

		//Support
		Button contato = (Button) findViewById(R.id.home_btn_support);
		contato.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Dashboard.this, Contato.class));
			}
		}); 

		//Rate
		Button get = (Button) findViewById(R.id.home_btn_get);
		get.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.clauvaneandroid.wallpapernaruto");
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		}); 
		
		Button puzzle = (Button) findViewById(R.id.home_btn_puzzle);
		puzzle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Dashboard.this, ListarPuzzles.class));
			}
		}); 
		
		Button quiz = (Button) findViewById(R.id.home_btn_quiz);
		quiz.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Dashboard.this, SplashActivity.class));
			}
		});
	}

	public void salvarVisitas(){
		SharedPreferences pref = getSharedPreferences(NOME, 0);
		int visita = pref.getInt("visitas",0);
		visita++;
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("visitas", visita);
		editor.commit();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(conectado.conectado()){
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {			
				//use smart wall on app exit. 
				airpush.startSmartWallAd();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}