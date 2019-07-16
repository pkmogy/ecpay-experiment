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

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 *
	 * @return @throws IOException
	 */
	@GetMapping(path = "/", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	String index() throws IOException, NoSuchAlgorithmException {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", "2000933");//廠商編號
		map.put("MerchantTradeDate", getMerchantTradeDate());//廠商交易時間
		map.put("LogisticsType", "CVS");//物流類型
		map.put("LogisticsSubType", "FAMIC2C");//物流子類型
		map.put("GoodsAmount", "1000");//商品金額
		map.put("IsCollection", "N");//是否代收貨款
		map.put("GoodsName", "測試訂單");//商品名稱
		map.put("SenderName", "寄件人");//寄件人姓名
		map.put("SenderCellPhone", "0909000000");//寄件人手機
		map.put("ReceiverName", "收件人");//收件人姓名
		map.put("ReceiverCellPhone", "0909090000");//收件人手機
		map.put("ServerReplyURL", "https://www.ecpay.com.tw/ServerReplyURL");//Server端回覆網址
		map.put("ReceiverStoreID", "001779");//ReceiverStoreID
		map.put("CheckMacValue", getCheckMacValue("XBERn1YOvpM9nfZc", "h1ONHk4P4yqbl5LK", map));//檢查碼
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
	 *
	 * @param HashKey
	 * @param HashIV
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private String getCheckMacValue(String HashKey, String HashIV, Map<String, String> map) throws IOException, NoSuchAlgorithmException {
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
		String step1 = stringBuilder.toString();
		System.out.println("step1 = " + step1);

		String urlEncoded = URLEncoder.encode(step1, "UTF-8").toLowerCase();
		String netUrlEncode = urlEncoded.replaceAll("%21", "\\!").replaceAll("%28", "\\(").replaceAll("%29", "\\)");
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] bytes = messageDigest.digest(netUrlEncode.getBytes());
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
//		messageDigest.update(urlencide.getBytes());
//		return new BigInteger(1, messageDigest.digest()).toString(16).toUpperCase();
		System.out.println(hexChars);
		return new String(hexChars);
	}
}
