package com.airshiplay.mobile.chart;

import com.airshiplay.mobile.chart.shape.BarShape;
import com.airshiplay.mobile.chart.view.R;
import com.airshiplay.mobile.chart.view.R.layout;
import com.airshiplay.mobile.chart.view.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

/**
 * @author airshiplay
 * @Create Date 2013-2-3
 * @version 1.0
 * @since 1.0
 */
public class MainUI extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SharedPreferences prefs=getSharedPreferences("test.prefs",MODE_MULTI_PROCESS+MODE_APPEND+ MODE_WORLD_WRITEABLE+MODE_WORLD_READABLE);
       boolean result= prefs.edit().putString("key", " key values---------").commit();
       System.out.println("key writer----"+result);
       System.out.println(prefs.getString("other", "other default"));
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(new BarShape());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
}
