package com.airshiplay.framework.download;

import android.content.Context;

import com.airshiplay.framework.bean.AppBean;

public class SoftDownFlowImp extends AbstractFlowImp<AppBean>{

	public SoftDownFlowImp(Context context,AppBean bean) {
		super(context,bean);
	}

	@Override
	protected boolean isDownLoading() {
		int type=mBean.actionType;
		return false;
	}

	@Override
	protected void showTip(int i) {
		
	}

}
