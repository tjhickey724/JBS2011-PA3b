package jbs2011.pa3b;

import jbs2011.pa3.GameModel;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.TextView;

public class AboutActivity extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		


		 
        //  create the view which contqins a game_surface on which the game will be drawn
		setContentView(R.layout.about);
		
		WebView webView = 	(WebView) findViewById(R.id.webview);
		webView.loadUrl("file:///android_asset/index.html" );
	}
	
}
