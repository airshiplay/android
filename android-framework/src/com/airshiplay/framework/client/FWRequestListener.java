package com.airshiplay.framework.client;

import com.airshiplay.framework.client.api.WLFailResponse;
import com.airshiplay.framework.client.api.WLResponse;

public abstract interface FWRequestListener
{
  public abstract void onSuccess(WLResponse paramWLResponse);

  public abstract void onFailure(WLFailResponse paramWLFailResponse);
}
