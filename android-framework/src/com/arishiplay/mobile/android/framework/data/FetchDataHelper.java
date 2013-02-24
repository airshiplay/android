/**
 * 
 */
package com.arishiplay.mobile.android.framework.data;


/**
 * 获取数据helper
 * 
 * @author airshiplay
 * @Create Date 2013-2-24
 * @version 1.0
 * @since 1.0
 */
public class FetchDataHelper {

	/**
	 * 异步请求，获取数据
	 * 
	 * @param url
	 * @param iFetchHandler
	 *            处理数据
	 */
	public static void request(String url,
			NetFetcherImp.IFetchHandler iFetchHandler) {
		NetFetcherImp localNetFetcherImp = new NetFetcherImp();
		localNetFetcherImp.setUrl(url);
		localNetFetcherImp.setHandler(iFetchHandler);
		localNetFetcherImp.request();
	}

	/**
	 * 本地异步获取数据
	 * 
	 * @param url
	 * @param iFetchHandler
	 *            处理数据
	 */
	public static void requestLocalData(String url,
			NetFetcherImp.IFetchHandler iFetchHandler) {
		new LocalDataFetcher(url, iFetchHandler).reqeust();
	}

	/**
	 * 同步请求，获取数据
	 * 
	 * @param url
	 * @param iFetchHandler
	 *            处理数据
	 */
	public static void requestSync(String url,
			NetFetcherImp.IFetchHandler iFetchHandler) {
		NetFetcherImp localNetFetcherImp = new NetFetcherImp();
		localNetFetcherImp.setUrl(url);
		localNetFetcherImp.setHandler(iFetchHandler);
		localNetFetcherImp.requestSync();
	}
}
