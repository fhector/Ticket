/**
 * 工具包
 */
package com.railway.ticket.client.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.railway.ticket.client.config.Config;

/**
 * 发送请求工具.
 * 对请求做一些特定的设置操作
 */
public class HttpUtils {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	private static final java.text.DecimalFormat spentsDF = new java.text.DecimalFormat("#0.00"); 
	
	public static HttpClient createClient() {
		HttpClient client = new DefaultHttpClient();
		if(Config.HOST.startsWith("https")) {
			//使用安全链接
			client = new DefaultHttpClient(getSafeClientConnManager());
		}
		((DefaultHttpClient)client).setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException e, int executionCount, HttpContext context) {
				if (executionCount >= 5) {
					return false;
				}
				logger.debug("当前请求重试次数:" + executionCount);
				if (e instanceof NoHttpResponseException) {
		            return true;
		        }
		        if (e instanceof SSLHandshakeException) {
		            return false;
		        }
		        HttpRequest request = (HttpRequest) context.getAttribute(
		                ExecutionContext.HTTP_REQUEST);
		        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest); 
		        if (idempotent) {
		            return true;
		        }
				return false;
			}
		});
		((DefaultHttpClient)client).setRedirectStrategy(new DefaultRedirectHandler());
		agentClient(client);	//检查代理设置
		return client;
	}
	
	/** 获取安全链接管理(https)
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	protected static ThreadSafeClientConnManager getSafeClientConnManager() {
		SSLContext ssl = null;
		try {
			ssl = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("网络环境初始化错误", e);
		}
		
		X509TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			ssl.init(new KeyManager[0],
					new TrustManager[] { tm },
					new SecureRandom());
		} catch (KeyManagementException e) {
			logger.debug("Https环境初始化错误!");
		}
		
		SSLContext.setDefault(ssl);
		SSLSocketFactory socketFactory = new SSLSocketFactory(ssl, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		
		Scheme sch = new Scheme("https", 443, socketFactory);
		ThreadSafeClientConnManager tcm = new ThreadSafeClientConnManager();
		tcm.setMaxTotal(5);
		tcm.getSchemeRegistry().register(sch);
		
		return tcm;
	}
	
	/**
	 * 发送POST请求
	 * @param client
	 * @param url url路径,不含Host信息
	 * @param parameters 参数列表
	 */
	public static String post(HttpClient client, String url, List<NameValuePair> parameters) {
		HttpPost request = new HttpPost(Config.HOST + url);
		if(null != parameters) {
			try {
				request.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				logger.debug("参数设置失败!{}-URL:{}",e.getMessage(), url);
			}
		}
		return execute(client, request);
	}
	
	/** 发送GET请求
	 * @param client
	 * @param url 路径,不含Host信息
	 * @param parameters 参数列表
	 */
	public static String get(HttpClient client, String url, List<NameValuePair> parameters) {
		return execute(client, new HttpGet(Config.HOST + url + URLEncodedUtils.format(parameters, HTTP.UTF_8)));
	}
	
	/** 发送GET请求
	 * @param client
	 * @param url 路径,不含Host信息
	 */
	public static HttpResponse get(HttpClient client, String url) {
		return (HttpResponse)execute(client, new HttpGet(Config.HOST + url), null);
	}
	
	/** 获取响应内容的byte数组数据
	 * @param client
	 * @param url
	 * @return 返回响应内容的{@link Byte}数组
	 */
	public static byte[] get2byte(HttpClient client, String url) {
		byte[] bytes = new byte[]{};
		try {
			HttpResponse response = (HttpResponse)execute(client, new HttpGet(Config.HOST + url), null);
			bytes = EntityUtils.toByteArray(response.getEntity());
		} catch (IOException e) {
			logger.error("数据读取失败!{}",e.getMessage());
		}
		return bytes;
	}
	
	/**
	 * 执行请求
	 * @param client
	 * @param request
	 * @return 响应内容
	 */
	public static String execute(HttpClient client, HttpRequestBase request) {
		return (String)execute(client, request, new BasicResponseHandler());
	}
	
	/** 执行
	 * @param client
	 * @param request
	 * @param handler
	 * @return handler不为空时返回内容为{@link String}.handler为<code>null</code>时返回{@link HttpResponse}
	 */
	public static Object execute(HttpClient client, HttpRequestBase request, BasicResponseHandler handler) {
		Object response = "";
		try {
			agentRequest(request);
			logger.debug("网络请求:{}", request.getRequestLine());
			long start = System.currentTimeMillis();
			response = client.execute(request);
			if(null != handler) {
				response = handler.handleResponse((HttpResponse)response);
			}
			logger.debug("响应类型:{},用时 {} 秒", response.getClass().getSimpleName(),
					spentsDF.format((System.currentTimeMillis() - start)/1000f));
		} catch (ClientProtocolException e) {
			if(!e.getMessage().contains("Moved Temporarily")) {
				response = (null == handler) ? null : "";
				logger.error("客户端协议或URL错误!请检查环境设置");
				logger.debug("ClientProtocolException: {}", e.getMessage());
			}
		} catch (IOException e) {
			response = (null == handler) ? null : "";
			logger.error("读取数据失败!请检查网络连接");
			logger.debug("IOException: {}", e.getMessage());
		}
		return response;
	}
	
	/**
	 * 设置HttpClient参数。
	 * 设置代理
	 * @param client
	 */
	protected static void agentClient(HttpClient client) {
		//设置超时
		HttpConnectionParams.setConnectionTimeout(client.getParams(), Config.SOCKET_TIMEOUT * 1000);
		logger.debug("连接超时设置: {} 秒",HttpConnectionParams.getConnectionTimeout(client.getParams())/1000);
		HttpConnectionParams.setSoTimeout(client.getParams(), (Config.SOCKET_TIMEOUT - 5) * 1000);
		logger.debug("请求超时设置: {} 秒",HttpConnectionParams.getSoTimeout(client.getParams())/1000);
		//设置Cookie为IE处理方式
		HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
		if(Config.PROXY_ENABLE) {
			HttpHost proxy = new HttpHost(Config.PROXY_HOST, Config.PROXY_PORT);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
			logger.info("设置代理服务器,主机:{} 端口:{}", proxy.getHostName(), proxy.getPort());
			if(Config.PROXY_AUTH_ENABLE) {
				//实例化验证  
				CredentialsProvider credsProvider = new BasicCredentialsProvider();  
				//设定验证内容  
				UsernamePasswordCredentials creds = new UsernamePasswordCredentials(Config.PROXY_AUTH_USERNAME, Config.PROXY_AUTH_PASSWORD);  
				//创建验证  
				credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);  
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
				((DefaultHttpClient)client).setCredentialsProvider(credsProvider);
				
				logger.info("代理服务器开启认证:用户名:{},密码:{}",Config.PROXY_AUTH_USERNAME, Config.PROXY_AUTH_PASSWORD.replaceAll(".{1}", "*"));
			}
		}
	}
	
	/**
	 * 设置请求的其它参数
	 * @param client
	 * @param request 
	 */
	protected static void agentRequest(HttpRequestBase request) {
		request.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
		request.setHeader("Connection", "Keep-Alive");
		request.setHeader("Cache-Control", "no-cache");
	}
}

/**
 * Http请求跳转处理
 * @author ChenXiaohong
 */
class DefaultRedirectHandler implements RedirectStrategy {
	/* (non-Javadoc)
	 * @see org.apache.http.client.RedirectStrategy#getRedirect(org.apache.http.HttpRequest, org.apache.http.HttpResponse, org.apache.http.protocol.HttpContext)
	 */
	public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response,
			HttpContext context) throws ProtocolException {
		Header[] headers = response.getHeaders("Location");
		if(null != headers && headers.length > 0) {
			String location = headers[0].getValue();
			return new HttpGet(location);
		}
		return null;
	}

	/* 是否为跳转的请求
	 * @see org.apache.http.client.RedirectStrategy#isRedirected(org.apache.http.HttpRequest, org.apache.http.HttpResponse, org.apache.http.protocol.HttpContext)
	 */
	public boolean isRedirected(HttpRequest request, HttpResponse response,
			HttpContext context) throws ProtocolException {
		boolean redirect = false;
		int statusCode = response.getStatusLine().getStatusCode();
		redirect = (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY);
		return redirect;
	}
}