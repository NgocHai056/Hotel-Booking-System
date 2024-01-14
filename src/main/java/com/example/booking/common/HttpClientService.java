package com.example.booking.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.util.Map;

public class HttpClientService {

	public static <T> T get(String url, Map<String, String> headers, Map<String, String> params, Class<T> objectclass,
			int timeout) {
		try {

			CloseableHttpClient httpclient = loadConfig(url, timeout);

			URIBuilder builder = new URIBuilder(url);

			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.setParameter(entry.getKey().toString(), entry.getValue().toString());
			}

			HttpGet httpGet = new HttpGet(builder.build());
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey().toString(), entry.getValue().toString());
			}

			CloseableHttpResponse response = httpclient.execute(httpGet);

			String jsonString = EntityUtils.toString(response.getEntity());

			ObjectMapper mapper = new ObjectMapper();
			T data = (T) mapper.readValue(jsonString, objectclass);

			return data;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static <T> T post(String url, Map<String, String> headers, Object params, Class<T> objectclass,
			int timeout) {
		try {
			CloseableHttpClient httpclient = loadConfig(url, timeout);

			URIBuilder builder = new URIBuilder(url);

			HttpPost httpPost = new HttpPost(builder.build());
			httpPost.addHeader("Content-Type", "application/json");
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey().toString(), entry.getValue().toString());
			}

			ObjectMapper mapper = new ObjectMapper();
			String jsonParams = mapper.writeValueAsString(params);
			StringEntity requestEntity = new StringEntity(jsonParams, ContentType.APPLICATION_JSON);

			httpPost.setEntity(requestEntity);

			CloseableHttpResponse response = httpclient.execute(httpPost);

			String jsonString = EntityUtils.toString(response.getEntity());

			T data = (T) mapper.readValue(jsonString, objectclass);

			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	private static CloseableHttpClient loadConfig(String url, int timeout) {
		if (timeout <= 0) {
			timeout = 15;
		}
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		HttpClientBuilder httpClientBuilder = HttpClientSingleton.getInstance().setDefaultRequestConfig(config);

		if (url.contains("https://")) {
			try {
				SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
				sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(),
						new HostnameVerifier() {
							@Override
							public boolean verify(String arg0, SSLSession arg1) {
								return true;
							}
						});
				httpClientBuilder.setSSLSocketFactory(sslsf);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return httpClientBuilder.build();
	}
}