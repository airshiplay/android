package com.airshiplay.framework.client.api.challengehandler;

import java.util.ArrayList;
import java.util.List;

import com.airshiplay.framework.client.FWRequest;
import com.airshiplay.framework.client.api.WLResponse;

public abstract class BaseChallengeHandler<T> {
	private String realm;
	protected FWRequest activeRequest = null;

	private List<FWRequest> requestWaitingList = new ArrayList();

	public String getRealm() {
		return this.realm;
	}

	public BaseChallengeHandler(String realm) {
		this.realm = realm;
	}

	public void startHandleChallenge(FWRequest request, T challenge) {
		synchronized (this) {
			if (!request.getOptions().isFromChallenge()) {
				if (this.activeRequest != null) {
					this.requestWaitingList.add(request);
					return;
				}

				this.activeRequest = request;
			}

		}

		handleChallenge(challenge);
	}

	protected void submitFailure(WLResponse wlResponse) {
		clearChallengeRequests();
	}

	private void clearChallengeRequests() {
		synchronized (this) {
			this.activeRequest = null;
			clearWaitingList();
		}
	}

	public abstract void handleChallenge(T paramT);

	public synchronized void releaseWaitingList() {
		for (FWRequest request : this.requestWaitingList) {
			request.removeExpectedAnswer(this.realm);
		}

		clearWaitingList();
	}

	public synchronized void clearWaitingList() {
		this.requestWaitingList.clear();
	}
}