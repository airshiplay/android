package com.airshiplay.framework.client.api;

import com.airshiplay.framework.client.FWRequestListener;

class InvokeProcedureRequestListener
  implements FWRequestListener
{
  public void onFailure(WLFailResponse failResponse)
  {
    WLProcedureInvocationFailResponse pifResponse = new WLProcedureInvocationFailResponse(failResponse);
    failResponse.getOptions().getResponseListener().onFailure(pifResponse);
  }

  public void onSuccess(WLResponse response)
  {
    WLProcedureInvocationResult piResponse = new WLProcedureInvocationResult(response);
    if (piResponse.isSuccessful()) {
      piResponse.getOptions().getResponseListener().onSuccess(piResponse);
    }
    else {
      WLProcedureInvocationFailResponse pifResponse = new WLProcedureInvocationFailResponse(response);
      pifResponse.setErrorCode(WLErrorCode.PROCEDURE_ERROR);
      pifResponse.getOptions().getResponseListener().onFailure(pifResponse);
    }
  }
}