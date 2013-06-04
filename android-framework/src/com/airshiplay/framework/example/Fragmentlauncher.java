package com.airshiplay.framework.example;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.airshiplay.framework.R;
import com.airshiplay.framework.bean.ListItemBean;

public class Fragmentlauncher extends FragmentActivity {
	FragmentManager fm;
	Fragment fragment;
	List<ListItemBean> data;
	int fragmentId;
	private ArrayAdapter<ListItemBean> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_launcher);
		fm = getSupportFragmentManager();
		FragmentTransaction transation = fm.beginTransaction();
		fragmentId = getIntent().getIntExtra("fragmentId", -1);
		if (fragmentId == -1) {
			fragment = new ListFragment1();
			data = new ArrayList<ListItemBean>();
			data.add(new ListItemBean(1, "text"));
			adapter = new ArrayAdapter<ListItemBean>(this,
					android.R.layout.simple_list_item_1, data);
			((ListFragment) fragment).setListAdapter(adapter);
			getLayoutInflater().inflate(android.R.layout.simple_list_item_1,
					null);
		} else {
			switch (fragmentId) {
			case 1:
				fragment =new ViewGroupFragment();
				break;

			default:
				break;
			}
		}

		transation.add(R.id.fragment, fragment);
		transation.commit();
	}

	private class ListFragment1 extends ListFragment {
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			startActivity(new Intent().setClass(getBaseContext(),
					Fragmentlauncher.class).putExtra("fragmentId",
					data.get(position).id));
		}
	}
}
