package com.muyh.util;

import com.hzsun.easytong.commons.spring.SpringUtils;
import com.hzsun.easytong.commons.utils.DateUtil;
import com.hzsun.easytong.commons.utils.FileUtil;
import com.hzsun.easytong.commons.utils.StringUtil;
import com.hzsun.easytong.utils.rsa.RSAUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @Author: liujian
 * @Description: 工具类
 * @Date: 2015-7-3
 */
public class WebUtil extends WebUtils {

	private WebUtil() {}
	
	/**
	 * 下载文件
	 * @param is 输入流
	 * @param name 下载文件名
	 * @param response
	 */
	public static final void downLoadFile(InputStream is, String name, HttpServletResponse response) {
		OutputStream sos = null;
		try {
			if (null != is) {
				name = URLEncoder.encode(name, "UTF-8");
				response.reset();
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.addHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
				byte[] buf = new byte[4096];
				sos = response.getOutputStream();
				int readLength;
				while (-1 != ((readLength = is.read(buf)))) {
					sos.write(buf, 0, readLength);
				}
				sos.flush();
			}
		} catch (Exception e) {
		} finally {
			try {
				if (null != sos) {
					sos.close();
				}
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
	public static final String getTmpPath(String path) {
		try {
			path = URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		path = path.replace("/classes/", "");
		path = path + FileUtil.DOWNLOADSFILEPATH + DateUtil.getNowDateStr(DateUtil.SHOW_FORMAT_DATE) + "/";
		FileUtil.createFile(path, false);
		return path;
	}
	
	public static final String getKeyPath(String path) {
		try {
			path = URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		path = path.replace("/classes/", "");
		path = path + "/static/files/keys/icbc/";
		return path;
	}
	
	/**
	 * 通过密钥获得真实密码
	 * @param uuid 密钥
	 * @param password 加密过的密码
	 * @return
	 */
	public static final String getRealPassword(Object uuid, String password) {
		String pwdRandomUUID = (String) uuid;
    	RSAUtil rsaUtil = SpringUtils.getBean("rsaUtil");
		rsaUtil.initKeyFile(pwdRandomUUID);
    	return rsaUtil.decryptStringByJs(password);
	}
	
	/**
	 * 清空密钥
	 * @param session
	 */
	public static final void removeCache(Session session) {
    	Object uuid = session.getAttribute(SessionManager.PORTALPWDKEY);
    	if (null != uuid) {
    		String pwdRandomUUID = (String) uuid;
    		if (StringUtil.isNotBlank(pwdRandomUUID)) {
    			RSAUtil rsaUtil = SpringUtils.getBean("rsaUtil");
    			Cache<String, KeyPair> cache = rsaUtil.getCURRENT_USER_RSAINFO();
    			cache.remove(pwdRandomUUID);
    		}
    	}
		session.removeAttribute(SessionManager.PORTALPWDKEY);
	}
	
	/**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数是指定的形式
     * @return 所代表远程资源的响应结果
     */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			//conn.setRequestProperty("Header", "Content-Type=text/xml");
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			
			String tempLine = in.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");

			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = in.readLine();
			}
			result = tempStr.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 发post请求
	 * @param url 
	 * @param params 参数
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String post(String url, Map<String, String> params) throws ParseException, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		HttpPost post = postForm(url, params);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	private static HttpPost postForm(String url, Map<String, String> params) throws UnsupportedEncodingException {

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		return httpost;
	}
	
	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) throws ParseException, IOException {

		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);

		return body;
	}
	
	private static String paseResponse(HttpResponse response) throws ParseException, IOException {
		HttpEntity entity = response.getEntity();

		String charset = EntityUtils.getContentCharSet(entity);

		String body = EntityUtils.toString(entity);
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) throws ClientProtocolException, IOException {
		HttpResponse response = httpclient.execute(httpost);
		return response;
	}
}
