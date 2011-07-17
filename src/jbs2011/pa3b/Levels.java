
package jbs2011.pa3b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;




public class Levels extends Activity implements OnClickListener{
	
	private Button playBtn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level);
        
        playBtn = (Button) findViewById(R.id.button1);
        playBtn.setOnClickListener(this);
        
        
    }
    
    public void onClick(View v){
    	if (v==playBtn)
    		finish();
    		;
    }
}