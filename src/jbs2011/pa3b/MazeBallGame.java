package jbs2011.pa3b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.media.AudioManager;




public class MazeBallGame extends Activity implements OnClickListener{
	
	private Button playBtn,aboutBtn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mazeball);
        
        playBtn = (Button) findViewById(R.id.playbutton);
        playBtn.setOnClickListener(this);
        
        aboutBtn = (Button) findViewById(R.id.aboutbutton);
        aboutBtn.setOnClickListener(this);


        
        
    }
    
    public void onClick(View v){
    	if (v==playBtn)
    		startActivity(new Intent(this, GameActivity.class));
    	else if (v==aboutBtn)
    		startActivity(new Intent(this, AboutActivity.class));
    		
    }
 
}