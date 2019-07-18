package com.ilearnedlearned.ecpay;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author user
 */
@Controller
@RequestMapping("/")
public class WelcomeController {
	/**
	 *
	 * @return @throws IOException
	 */
	@GetMapping(path = "/", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	String index() throws IOException, NoSuchAlgorithmException {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", "2000132");//廠商編號
		map.put("MerchantTradeNo", getMerchantTradeNumber());//特店交易編號
		map.put("MerchantTradeDate",getMerchantTradeDate());//交易時間
		map.put("PaymentType", "aio");//交易類型
		map.put("TotalAmount", "1000");//交易金額
		map.put("TradeDesc", URLEncoder.encode("測試", "UTF-8"));//交易描述
		map.put("ItemName", "手機X1#相機X1");//商品清單
		map.put("ReturnURL", "http://your.web.site/receive.php");//付款完成通知回傳網址
		map.put("ChoosePayment", "Credit");//付款方式
		map.put("ClientBackURL", "http://yahoo.com");//返回特店的按鈕連結
		map.put("EncryptType", "1");//CheckMacValue加密類型
		map.put("CheckMacValue", getCheckMacValue("5294y06JbISpM5x9", "v77hoKGq4kWxNNIS", map,"SHA-256"));//檢查碼 MD5 or SHA-256
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			jsonObject.put(key, map.get(key));
			//System.out.println(key + "=" + map.get(key));
		}
//		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
//		HttpPost httpPost = new HttpPost("https://logistics-stage.ecpay.com.tw/Express/Create");
//		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
//		StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
//		stringEntity.setContentEncoding("UTF-8");
//		httpPost.setEntity(stringEntity);
//		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
//		try {
//
//		} catch (Exception e) {
//		}
		return jsonObject.toString();
	}
	/**
	 * 生成廠商編號
	 *
	 * @return
	 */
	private String getMerchantTradeNumber() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);
		return Long.toString(calendar.getTimeInMillis());
	}

	/**
	 * 生成交易時間
	 *
	 * @return
	 */
	private String getMerchantTradeDate() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(calendar.getTime());
	}

	/**
	 * 生成檢查碼
	 * @param HashKey
	 * @param HashIV
	 * @param map
	 * @param Instance
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	private String getCheckMacValue(String HashKey, String HashIV, Map<String, String> map ,String Instance) throws IOException, NoSuchAlgorithmException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("HashKey=").append(HashKey).append("&");
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			stringBuilder.append(key).append("=").append(map.get(key)).append("&");
			//System.out.println(key + "=" + map.get(key));
		}
		stringBuilder.append("HashIV=").append(HashIV);
		String urlEncoded = URLEncoder.encode(stringBuilder.toString(), "UTF-8").toLowerCase();
		String netUrlEncode = urlEncoded.replaceAll("%21", "\\!").replaceAll("%28", "\\(").replaceAll("%29", "\\)");
		MessageDigest messageDigest = MessageDigest.getInstance(Instance);
//		byte[] bytes = messageDigest.digest(netUrlEncode.getBytes());
//		char[] hexChars = new char[bytes.length * 2];
//		for (int j = 0; j < bytes.length; j++) {
//			int v = bytes[j] & 0xFF;
//			hexChars[j * 2] = hexArray[v >>> 4];
//			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//		}
		messageDigest.update(netUrlEncode.getBytes());
		return new BigInteger(1, messageDigest.digest()).toString(16).toUpperCase();
//		System.out.println(hexChars);
//		return new String(hexChars);
	}
}
