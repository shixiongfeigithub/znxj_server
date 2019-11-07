package com.niule.znxj.core.util;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import com.niule.znxj.core.util.json.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


public class HttpClientUtils {
	
	private static Log accessLog = LogFactory.getLog("access");
	
	/**
	 * http请求工具类 post方式请求
	 * @param url
	 * @param params value 仅支持 String和List两种类型
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url,Map<String,Object> params)throws Exception
	{
		CloseableHttpClient defaultHttpClient =null;
		BufferedReader bufferedReader = null;
		try
		{
			defaultHttpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
			//传递参数
			if(params != null)
			{
				StringEntity reqEntity = new StringEntity (JsonUtil.toJSON(params), StandardCharsets.UTF_8);
				reqEntity.setContentEncoding("UTF-8");
				httpPost.setEntity(reqEntity);
			}
			
			HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() != 200)
			{
				throw new Exception(url+"请求失败，errorCode="+httpResponse.getStatusLine().getStatusCode());
			}
			
			//读取返回信息
			String output;
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));
			
			StringBuilder dataBuilder = new StringBuilder();
			
			while ((output = bufferedReader.readLine()) != null)
			{
				dataBuilder.append(new String(output.getBytes()));
			}
			
			return dataBuilder.toString();
			
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			throw e;
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(defaultHttpClient != null)
				defaultHttpClient.close();
			if(bufferedReader != null)
				bufferedReader.close();
		}
	}
	
	
	
	/**
	 * http请求工具类 post方式请求
	 * @param url
	 * @param params value 仅支持 String和List两种类型
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url,String params)throws Exception
	{
		CloseableHttpClient defaultHttpClient =null;
		BufferedReader bufferedReader = null;
		try
		{
			defaultHttpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
			httpPost.addHeader("accept","application/json");
			//传递参数
			if(params != null)
			{
				StringEntity reqEntity = new StringEntity (params, StandardCharsets.UTF_8); 
				reqEntity.setContentEncoding("UTF-8");
				httpPost.setEntity(reqEntity);
			}
			
			HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() != 200)
			{
				throw new Exception(url+"请求失败，errorCode="+httpResponse.getStatusLine().getStatusCode());
			}
			
			//读取返回信息
			String output;
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));
			
			StringBuilder dataBuilder = new StringBuilder();
			
			while ((output = bufferedReader.readLine()) != null)
			{
				dataBuilder.append(new String(output.getBytes()));
			}
			
			return dataBuilder.toString();
			
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			throw e;
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(defaultHttpClient != null)
				defaultHttpClient.close();
			if(bufferedReader != null)
				bufferedReader.close();
		}
	}
	
	
	/**
	 * http请求工具类 post方式请求
	 * @param url
	 * @param params value 仅支持 String和List两种类型
	 * @return
	 * @throws Exception
	 */
	public static String httpsPost(String url,String params)throws Exception
	{
		CloseableHttpClient defaultHttpClient =null;
		BufferedReader bufferedReader = null;
		try
		{
			defaultHttpClient = createSSLClientDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
			//传递参数
			if(params != null)
			{
				StringEntity reqEntity = new StringEntity (params, StandardCharsets.UTF_8); 
				reqEntity.setContentEncoding("UTF-8");
				httpPost.setEntity(reqEntity);
			}
			
			HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() != 200)
			{
				throw new Exception(url+"请求失败，errorCode="+httpResponse.getStatusLine().getStatusCode());
			}
			
			//读取返回信息
			String output;
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));
			
			StringBuilder dataBuilder = new StringBuilder();
			
			while ((output = bufferedReader.readLine()) != null)
			{
				dataBuilder.append(new String(output.getBytes()));
			}
			
			return dataBuilder.toString();
			
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			throw e;
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(defaultHttpClient != null)
				defaultHttpClient.close();
//			if(bufferedReader != null)
//				bufferedReader.close();
		}
	}
	
	
	/**
	 * http请求工具类 get方式请求
	 * @param url
	 * @param params value 仅支持 String和List两种类型
	 * @return
	 * @throws Exception
	 */
	public static String httpGet(String url,Map<String,Object> params,String ...resonseCharset)throws Exception
	{
		CloseableHttpClient defaultHttpClient =null;
		BufferedReader bufferedReader = null;
		try
		{
			
			defaultHttpClient = HttpClients.createDefault();
			
			
			//传递参数
			if(params != null)
			{
				StringBuilder paramStr = new StringBuilder();
				Iterator<String> keyItr = params.keySet().iterator();
				String key ="";
				while(keyItr.hasNext())
				{
					key = keyItr.next();
					Object val = params.get(key);
					if(val instanceof List)
					{
						List tmp = (List)val;
						for(Object o : tmp)
						{
							paramStr.append(key).append("=").append(URLEncoder.encode(o.toString(),"UTF-8")).append("&");
						}
					}
					else
						paramStr.append(key).append("=").append(URLEncoder.encode(val.toString(),"UTF-8")).append("&");
				}
				paramStr.deleteCharAt(paramStr.length()-1);
				url = url + "?" +paramStr.toString();
				System.out.println(url+"------------------");
			}
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
			HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
			
			if (httpResponse.getStatusLine().getStatusCode() != 200)
			{
				throw new Exception(url+"请求失败，errorCode="+httpResponse.getStatusLine().getStatusCode());
			}
			
			//读取返回信息
			String charset="UTF-8";
			if(resonseCharset!=null&&resonseCharset.length>0)
				charset=resonseCharset[0];
			String output;
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),charset));
			
			StringBuilder dataBuilder = new StringBuilder();
			
			while ((output = bufferedReader.readLine()) != null)
			{
				dataBuilder.append(new String(output.getBytes()));
			}
			
			return dataBuilder.toString();
			
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			throw e;
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(defaultHttpClient != null)
				defaultHttpClient.close();
			if(bufferedReader != null)
				bufferedReader.close();
		}
	}
	
	
	
	/**
	 * http请求工具类 post方式请求(Servlet请求)
	 * @param url
	 * @param params value 仅支持 String和List两种类型
	 * @return
	 * @throws Exception
	 */
	public static String httpPostServlet(String url,Map<String,Object> params)throws Exception
	{
		CloseableHttpClient defaultHttpClient =null;
		BufferedReader bufferedReader = null;
		try
		{
			defaultHttpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			accessLog.info("url : " + url);
			//传递参数
			if(params != null)
			{
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();  
				for(Iterator i=params.entrySet().iterator();i.hasNext();){   
					Map.Entry entry=(Map.Entry)i.next();  
					accessLog.info(entry.getKey().toString() + " : " + entry.getValue().toString());
					paramsList.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue().toString()));
				}
				
				httpPost.setEntity(new UrlEncodedFormEntity(paramsList, StandardCharsets.UTF_8));
			}
			HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() != 200)
			{
				throw new Exception(url+"请求失败，errorCode="+httpResponse.getStatusLine().getStatusCode());
			}
			
			//读取返回信息
			String output;
			bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));
			
			StringBuilder dataBuilder = new StringBuilder();
			
			while ((output = bufferedReader.readLine()) != null)
			{
				dataBuilder.append(new String(output.getBytes()));
			}
			
			return dataBuilder.toString();
			
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			throw e;
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(defaultHttpClient != null)
				defaultHttpClient.close();
			if(bufferedReader != null)
				bufferedReader.close();
		}
	}
	
	/**
	 * http请求工具类 get方式请求(Servlet请求)
	 * @param url
	 * @param params value 仅支持 String和List两种类型
	 * @return
	 * @throws Exception
	 */
	public static byte[] httpGetServletByte(String url,Map<String,Object> params)throws Exception
	{
		CloseableHttpClient defaultHttpClient =null;
		BufferedReader bufferedReader = null;
		try
		{
			defaultHttpClient = HttpClients.createDefault();
			//传递参数
			if(params != null)
			{
				StringBuilder paramStr = new StringBuilder();
				Iterator<String> keyItr = params.keySet().iterator();
				String key ="";
				while(keyItr.hasNext())
				{
					key = keyItr.next();
					Object val = params.get(key);
					if(val instanceof List)
					{
						List tmp = (List)val;
						for(Object o : tmp)
						{
							paramStr.append(key).append("=").append(o.toString()).append("&");
						}
					}
					else
						paramStr.append(key).append("=").append(val.toString()).append("&");
				}
				paramStr.deleteCharAt(paramStr.length()-1);
				url = url + "?" +paramStr.toString();
			}
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() != 200)
			{
				throw new Exception(url+"请求失败，errorCode="+httpResponse.getStatusLine().getStatusCode());
			}
			byte[] retByte=transformInputstream(httpResponse.getEntity().getContent());
			return retByte;	
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			throw e;
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(defaultHttpClient != null)
				defaultHttpClient.close();
			if(bufferedReader != null)
				bufferedReader.close();
		}
	}
	   /**
     * inputStream转换byte数组
     * @param input
     * @return 流是从当前位置开始读取的
     * @throws Exception
     */
    private static byte[] transformInputstream(InputStream input)throws Exception
    {
        byte[] byt= null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b=0;       
        b = input.read();
        while( b != -1)
        {
            baos.write(b);
            b = input.read();            
        }
        byt = baos.toByteArray();
        return byt;
    }
    
    public static CloseableHttpClient createSSLClientDefault(){
        try {
             SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                 //信任所有
                 public boolean isTrusted(X509Certificate[] chain,
                                 String authType) throws CertificateException {
                     return true;
                 }
             }).build();
             SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
             return HttpClients.custom().setSSLSocketFactory(sslsf).build();
         } catch (KeyManagementException e) {
             e.printStackTrace();
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         } catch (KeyStoreException e) {
             e.printStackTrace();
         }
        return  HttpClients.createDefault();
    }
    
    
    
    public static void main(String args[])
    {
    	String str;
		try {
			String url = "http://58.211.129.134:8866/appupload/uploadfile";
			Map<String,Object> map = new HashMap<String,Object>();
			File file = new File("F:\\600000.png");
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
			map.put("file", multipartFile.getBytes());
			str = HttpClientUtils.httpPost(url,JsonUtil.toJSON(map));
			System.out.println(str);


			/*String url1 = "http://58.211.129.134:8866/system/hotsync/xjjl";
            Xjjl xjjl1 = new Xjjl("123","一班","测试111","","一号检查点","1","2019-11-07 16:24:56","备注");
            Xjjl xjjl2 = new Xjjl("124","二班","测试222","","二号检查点","0","2019-11-07 16:24:56","备注");
            Xjjl xjjl3 = new Xjjl("125","是班","测试233","","仨号检查点","1","2019-11-07 16:24:56","备注");
			List<Xjjl> list = new ArrayList<Xjjl>();
			list.add(xjjl1);
			list.add(xjjl2);
			str = HttpRequestUtils.httpPost(url1,JsonUtil.toJSON(list));
			System.out.println(str);*/


            /*String url2 = "http://58.211.129.134:8866/system/hotsync/yhjl";
            Yhjl yhjl = new Yhjl("123","111","111","1","张某某 ","2019-11-07 16:24:56","2019-11-07 16:24:56","危险隐患",
                    "/upload/image/20191105/20191105110142_75.jpg","放荡","fdgfgf","2019-11-07 16:24:56",
                    "/upload/image/20191105/20191105110142_75.jpg","大范甘迪发给发");
            List<Yhjl> list = new ArrayList<Yhjl>();
            list.add(yhjl);
            str = HttpRequestUtils.httpPost(url2,JsonUtil.toJSON(list));
            System.out.println(str);*/


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    

}