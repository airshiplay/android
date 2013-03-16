/**
 * 
 */
package com.airshiplay.framework.example.wheel;

import java.lang.reflect.TypeVariable;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airshiplay.framework.R;
import com.airshiplay.framework.wheel.WheelView;
import com.airshiplay.framework.wheel.adapters.WheelViewAdapter;

/**
 * @author airshiplay
 * @Create Date 2013-3-15
 * @version 1.0
 * @since 1.0
 */
public class WheelTest extends Activity implements WheelViewAdapter {
	private WheelView wheel;
	private WheelView wheel1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example_wheel_test);
		wheel1 = (WheelView) findViewById(R.id.wheel1);
		wheel1.setViewAdapter(this);
		wheel1.setCyclic(true);
		wheel = (WheelView) findViewById(R.id.wheel);
		wheel.setViewAdapter(this);
		wheel.setCyclic(true);
	}

	@Override
	public int getItemsCount() {
		return 10;
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		TextView view =new TextView(this);
		view.setText(index+"");
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		view.setPadding(10, 0, 10, 0);
		return view;
	}

	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		return new View(this);
	}

	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}
}
