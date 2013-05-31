package com.airshiplay.framework.example;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airshiplay.framework.R;

public class TouchDelegateUI extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_delegate_ui);
        // Get the parent view
        View parentView = findViewById(R.id.parent_layout);
        
        parentView.post(new Runnable() {
            // Post in the parent's message queue to make sure the parent
            // lays out its children before you call getHitRect()
            @Override
            public void run() {
                // The bounds for the delegate view (an ImageButton
                // in this example)
                Rect delegateArea = new Rect();
                ImageButton myButton = (ImageButton) findViewById(R.id.button);
                myButton.setEnabled(true);
                myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(TouchDelegateUI.this, 
                                "Touch occurred within ImageButton touch region.", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
     
                // The hit rectangle for the ImageButton
                myButton.getHitRect(delegateArea);
            
                // Extend the touch area of the ImageButton beyond its bounds
                // on the right and bottom.
                delegateArea.right += 100;
                delegateArea.bottom += 100;
            
                // Instantiate a TouchDelegate.
                // "delegateArea" is the bounds in local coordinates of 
                // the containing view to be mapped to the delegate view.
                // "myButton" is the child view that should receive motion
                // events.
                TouchDelegate touchDelegate = new TouchDelegate(delegateArea, 
                        myButton);
     
                // Sets the TouchDelegate on the parent view, such that touches 
                // within the touch delegate bounds are routed to the child.
                if (View.class.isInstance(myButton.getParent())) {
                    ((View) myButton.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
}
