package com.ilearnedlearned.ecpay;

import java.io.IOException;
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
	 * @return
	 * @throws IOException 
	 */
	@GetMapping(path = "/", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	String index() throws IOException {
		Map<String, String> map = new HashMap();
		map.put("MerchantID", "");//廠商編號
		map.put("MerchantTradeNo", getMerchantTradeNumber());//廠商交易編號
		map.put("MerchantTradeDate", getMerchantTradeDate());//廠商交易時間
		map.put("LogisticsType", "CVS");//物流類型
		map.put("LogisticsSubType", "UNIMART");//物流子類型
		map.put("GoodsAmount", "1000");//商品金額
		map.put("CollectionAmount", "1000");//代收金額
		map.put("IsCollection", "Y");//是否代收貨款
		map.put("GoodsName", "測試訂單");//商品名稱
		map.put("SenderName", "寄件人");//寄件人姓名
		map.put("SenderPhone", "073603600");//寄件人電話
		map.put("SenderCellPhone", "0909000000");//寄件人手機
		map.put("ReceiverName", "收件人");//收件人姓名
		map.put("ReceiverPhone", "073603600");//收件人電話
		map.put("ReceiverCellPhone", "0909090000");//收件人手機
		map.put("ReceiverEmail", "receiver@gmail.com");//收件人email
		map.put("TradeDesc", "描述");//交易描述
		map.put("ServerReplyURL", "http://yahoo.com.tw");//Server端回覆網址
		map.put("ClientReplyURL", "http://yahoo.com.tw");//Client端回覆網
		map.put("LogisticsC2CReplyURL", "http://yahoo.com.tw");//Server端物流回傳網址
		map.put("Remark", "");//備註
		map.put("ReceiverStoreID", "991182");//ReceiverStoreID
		map.put("ReturnStoreID", "991182");//退貨門市代號

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("HashKey=XBERn1YOvpM9nfZc").append("&");

		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			//jsonObject.put(key, map.get(key));
			//System.out.println(key + "=" + map.get(key));
			stringBuilder.append(key).append("=").append(map.get(key)).append("&");
		}
		stringBuilder.append("HashKey=XBERn1YOvpM9nfZc");
		
//		map.put("CheckMacValue", "");//檢查碼
//		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet("https://logistics-stage.ecpay.com.tw/Express/Create");
//		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
//		try {
//
//		} catch (Exception e) {
//		}
		return stringBuilder.toString();
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
}
