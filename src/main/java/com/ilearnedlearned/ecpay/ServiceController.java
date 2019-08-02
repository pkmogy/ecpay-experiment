/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.stereotype.Service;

/**
 *
 * @author 李羅
 */
@Service
public class ServiceController {
	/**
	 * 生成廠商編號
	 *
	 * @return
	 */
	public String getMerchantTradeNumber() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);
		return Long.toString(calendar.getTimeInMillis()/1000);
	}

	/**
	 * 生成交易時間
	 *
	 * @return
	 */
	public String getMerchantTradeDate() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(calendar.getTime());
	}
	
	/**
	 * 三天後時間
	 * @return 
	 */
	public String getThreeDate() {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);
		calendar.add(Calendar.DAY_OF_MONTH, +3);
		return new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
	}
	
	/**
	 * 生成檢查碼
	 *
	 * @param HashKey
	 * @param HashIV
	 * @param map
	 * @param Instance
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public String getCheckMacValue(String HashKey, String HashIV, Map<String, String> map, String Instance) throws IOException, NoSuchAlgorithmException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("HashKey=").append(HashKey).append("&");
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
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
