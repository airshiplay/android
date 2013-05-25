package com.airshiplay.framework.example;

import com.airshiplay.framework.R;
import com.airshiplay.framework.text.TextView;

import android.app.Activity;
import android.os.Bundle;

public class TextViewUI extends Activity {
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textview_ui);
		text = (TextView) findViewById(R.id.textview);
		text.setText("Misslog是一个支持多blog、支持多用户的小型内容发布和管理系统，使用ASP3、VBScript5语言编写，底层结构采用面向对象设 计，并集成缓存功能。 主要功能 ： 支持建立多个Blog 编 辑和发布文章，可以再次编辑。文章.");
	}
}
