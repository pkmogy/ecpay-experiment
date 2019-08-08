package com.ilearnedlearned.ecpay;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author 李羅
 */
@Controller
@PropertySource("classpath:data.properties")
@RequestMapping("/")
public class WelcomeController {
	/**
	 * 注入serves
	 */
	@Autowired
	ServiceController serviceController;
	
	/**
	 * B2C MerchantID
	 */
	@Value("${data.B2C.MerchantID}")
	private String b2cMerchantID;
	
	/**
	 * B2C HashKey
	 */
	@Value("${data.B2C.HashKey}")
	private String b2cHashKey;
	
	/**
	 * B2C HashIV
	 */
	@Value("${data.B2C.HashIV}")
	private String b2cHashIV;
	
	/**
	 * C2C MerchantID
	 */
	@Value("${data.C2C.MerchantID}")
	private String c2cMerchantID;
	
	/**
	 * C2C HashKey
	 */
	@Value("${data.C2C.HashKey}")
	private String c2cHashKey;
	
	/**
	 * C2C HashIV
	 */
	@Value("${data.C2C.HashIV}")
	private String c2cHashIV;
	
	/**
	 * 電子發票 HashKey
	 */
	@Value("${data.Invoice.HashKey}")
	private String InvoiceHashKey;
	
	/**
	 * 電子發票 HashIV
	 */
	@Value("${data.Invoice.HashIV}")
	private String InvoiceHashIV;
	
	/**
	 * 金流訂單生成-信用卡
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws ParserConfigurationException 
	 */
	@GetMapping(path = "/", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	ModelAndView index(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, ParserConfigurationException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		//JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", b2cMerchantID);//廠商編號
		map.put("MerchantTradeNo", serviceController.getMerchantTradeNumber());//交易編號
		map.put("MerchantTradeDate", serviceController.getMerchantTradeDate());//交易時間
		map.put("PaymentType", "aio");//交易類型
		map.put("TotalAmount", "1000");//交易金額
		map.put("TradeDesc", URLEncoder.encode("test", "UTF-8"));//交易描述
		map.put("ItemName", "item1 x 2#item2 x 1");//商品名稱
		map.put("ReturnURL", "http://127.0.0.1/index.html");//付款完成通知回傳網址
		map.put("ChoosePayment", "Credit");//付款方式
		map.put("ClientBackURL", "http://127.0.0.1/index.html");//Client 端返回特店的按鈕連結
		map.put("EncryptType", "1");//CheckMacValue 加密類型
		map.put("CreditInstallment", "3,6,12,18,24");//刷卡分期期數
		map.put("InvoiceMark", "Y");//電子發票開立
		map.put("RelateNumber", "A" + serviceController.getMerchantTradeNumber());//自訂編號
		map.put("CustomerPhone", "0912345678");//物流子類型
		map.put("CustomerEmail", URLEncoder.encode("abc@ecpay.com.tw", "UTF-8"));//物流子類型
		map.put("Print", "0");//列印註記
		map.put("Donation", "0");//捐贈註記
		map.put("CarruerType", "1");//載具類別
		map.put("CarruerNum", "");//載具編號
		map.put("TaxType", "1");//課稅類別
		map.put("InvoiceItemCount", "1");//商品數量
		map.put("InvoiceItemPrice", "1000");//商品價格
		map.put("DelayDay", "0");//延遲天數
		map.put("InvType", "07");//商品合計
		map.put("InvoiceItemName", URLEncoder.encode("好玩遊戲", "UTF-8"));//商品名稱
		map.put("InvoiceItemWord", URLEncoder.encode("單位多", "UTF-8"));//商品單位
		map.put("CheckMacValue", serviceController.getCheckMacValue(b2cHashKey, b2cHashIV, map, "SHA-256"));//檢查碼 MD5 or SHA-256

		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		
		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5");
		while (iterator.hasNext()) {
			String key = iterator.next();
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
			//jsonObject.put(key, map.get(key));
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
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));

		return modelAndView;
	}
	/**
	 * 金流訂單查詢
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws ParserConfigurationException 
	 */
	@GetMapping(path = "/info", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	ModelAndView info(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, ParserConfigurationException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		//JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", b2cMerchantID);//廠商編號
		map.put("MerchantTradeNo", "2019062405403854");//交易編號
		map.put("TimeStamp",  serviceController.getMerchantTradeNumber());//時間戳記
		map.put("CheckMacValue", serviceController.getCheckMacValue(b2cHashKey, b2cHashIV, map, "SHA-256"));//檢查碼 MD5 or SHA-256

		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		
		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://payment-stage.ecpay.com.tw/Cashier/QueryTradeInfo/V5");
		while (iterator.hasNext()) {
			String key = iterator.next();
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
		}
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));

		return modelAndView;
	}

	/**
	 * 物流訂單生成
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException 
	 */
	@GetMapping(path = "/index2", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public ModelAndView index2(HttpServletResponse response) throws IOException, NoSuchAlgorithmException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		//JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", b2cMerchantID);//廠商編號
		map.put("MerchantTradeDate", serviceController.getMerchantTradeDate());//廠商交易時間
		map.put("LogisticsType", "Home");//物流類型
		map.put("LogisticsSubType", "TCAT");//物流子類型
		map.put("GoodsAmount", "1000");//商品金額
		map.put("IsCollection", "N");//是否代收貨款
		map.put("GoodsName", "測試訂單");//商品名稱
		map.put("SenderName", "寄件人");//寄件人姓名
		map.put("SenderCellPhone", "0909000000");//寄件人手機
		map.put("ReceiverName", "收件人");//收件人姓名
		map.put("ReceiverCellPhone", "0909090000");//收件人手機
		map.put("ServerReplyURL", "https://www.ecpay.com.tw/ServerReplyURL");//Server端回覆網址
		//map.put("ReceiverStoreID", "001779");//收件人門市代號
		map.put("SenderZipCode","811"); //寄件人郵遞區號
		map.put("SenderAddress","高雄市左營區"); //寄件人地址
		map.put("ReceiverZipCode","811"); //收件人郵遞區號
		map.put("ReceiverAddress","高雄市左營區"); //收件人地址
		map.put("Temperature","0001"); //溫層
		map.put("Distance","00"); //距離)
		map.put("Specification","0001"); //規格
		map.put("ScheduledPickupTime","4"); //預定取件時段
		map.put("ScheduledDeliveryTime","4"); //預定送達時段
		map.put("ScheduledDeliveryDate",serviceController.getThreeDate()); //指定送達日
		map.put("PackageCount","1"); //包裹件數
		map.put("CheckMacValue", serviceController.getCheckMacValue(b2cHashKey, b2cHashIV, map, "MD5"));//檢查碼 MD5 or SHA-256
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();

		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://logistics-stage.ecpay.com.tw/Express/Create");
		while (iterator.hasNext()) {
			String key = iterator.next();
			//jsonObject.put(key, map.get(key));
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
		}

//		try {
//			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(response.getOutputStream()));
//		} catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
////			LOGGER.info(transformerFactoryConfigurationError.getLocalizedMessage());
//		} catch (TransformerException transformerException) {
//		} catch (IOException ioException) {
//		}
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));

		return modelAndView;
	}
	
	/**
	 * 物流訂單查詢
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws ParserConfigurationException 
	 */
	@GetMapping(path = "index2/info", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	ModelAndView logisticsInfo(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, ParserConfigurationException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		//JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", c2cMerchantID);//廠商編號
		map.put("AllPayLogisticsID", "196196");//綠界編號
		map.put("TimeStamp",  serviceController.getMerchantTradeNumber());//時間戳記
		map.put("CheckMacValue", serviceController.getCheckMacValue(c2cHashKey, c2cHashIV, map, "MD5"));//檢查碼 MD5 or SHA-256

		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		
		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://logistics-stage.ecpay.com.tw/Helper/QueryLogisticsTradeInfo/V2");
		while (iterator.hasNext()) {
			String key = iterator.next();
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
		}
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));

		return modelAndView;
	}
	
	/**
	 * 產生托運單(宅配)/一段標(超商取貨)
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws ParserConfigurationException 
	 */
	@GetMapping(path = "index2/print", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	ModelAndView logisticsPrint(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, ParserConfigurationException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		//JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", b2cMerchantID);//廠商編號
		map.put("AllPayLogisticsID", "196455");//綠界編號
		map.put("CheckMacValue", serviceController.getCheckMacValue(b2cHashKey, b2cHashIV, map, "MD5"));//檢查碼 MD5 or SHA-256

		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		
		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://logistics-stage.ecpay.com.tw/helper/printTradeDocument");
		while (iterator.hasNext()) {
			String key = iterator.next();
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
		}
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));

		return modelAndView;
	}

	/**
	 * 電子發票生成
	 *
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	@GetMapping(path = "/index3", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	ModelAndView index3() throws IOException, NoSuchAlgorithmException, ParserConfigurationException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Map<String, String> map = new HashMap();
		map.put("TimeStamp",  serviceController.getMerchantTradeNumber());//時間戳記
		map.put("MerchantID", b2cMerchantID);//廠商編號
		map.put("RelateNumber", "A" + serviceController.getMerchantTradeNumber());//自訂編號
		map.put("CustomerPhone", "0912345678");//物流子類型
		map.put("Print", "0");//列印註記
		map.put("Donation", "0");//捐贈註記
		map.put("CarruerType", "1");//載具類別
		map.put("CarruerNum", "");//載具編號
		map.put("TaxType", "1");//課稅類別
		map.put("SalesAmount", "1000");//發票總金額(含稅)
		map.put("ItemCount", "1");//商品數量
		map.put("ItemPrice", "1000");//商品價格
		map.put("ItemAmount", "1000");//商品合計
		map.put("InvType", "07");//商品合計
		map.put("vat", "1");//商品合計
		map.put("CheckMacValue", serviceController.getCheckMacValue(InvoiceHashKey, InvoiceHashIV, map, "MD5"));//檢查碼 MD5 or SHA-256
		map.put("ItemName", URLEncoder.encode("測試", "UTF-8"));//商品名稱
		map.put("ItemWord", URLEncoder.encode("test", "UTF-8"));//商品單位

		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		
		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://einvoice-stage.ecpay.com.tw/Invoice/Issue");
		while (iterator.hasNext()) {
			String key = iterator.next();
			//jsonObject.put(key, map.get(key));
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
		}
		
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
	
	/**
	 * 查詢發票
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws ParserConfigurationException 
	 */
	@GetMapping(path = "/index3/Issue", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	ModelAndView Issue() throws IOException, NoSuchAlgorithmException, ParserConfigurationException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Map<String, String> map = new HashMap();
		map.put("TimeStamp",  serviceController.getMerchantTradeNumber());//時間戳記
		map.put("MerchantID", b2cMerchantID);//廠商編號
		map.put("RelateNumber", "A1564025998");//自訂編號
		map.put("CheckMacValue", serviceController.getCheckMacValue(InvoiceHashKey, InvoiceHashIV, map, "MD5"));//檢查碼 MD5 or SHA-256
		
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		
		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://einvoice-stage.ecpay.com.tw/Query/Issue");
		while (iterator.hasNext()) {
			String key = iterator.next();
			//jsonObject.put(key, map.get(key));
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
		}
		
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
	
	/**
	 * 電子地圖
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws ParserConfigurationException 
	 */
	@GetMapping(path = "/index2/map", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	ModelAndView map() throws IOException, NoSuchAlgorithmException, ParserConfigurationException {
		Document document;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Map<String, String> map = new HashMap();
		map.put("MerchantID", b2cMerchantID);//廠商編號
		map.put("MerchantTradeNo", "A" + serviceController.getMerchantTradeNumber());//廠商交易編號
		map.put("LogisticsType", "CVS");//物流類型
		map.put("LogisticsSubType", "UNIMART");//物流子類型
		map.put("IsCollection", "N");//是否代收款
		map.put("ServerReplyURL", "http://127.0.0.1/index2/map/return");//Server端回傳網址
		map.put("ExtraData", "測試資料");//額外資訊
		map.put("Device", "0");//使用設備
		
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys);
		Iterator<String> iterator = keys.iterator();
		
		Element parametersElement = document.createElement("parameters");
		document.appendChild(parametersElement);
		Element actionElement = document.createElement("action");
		parametersElement.appendChild(actionElement);
		actionElement.setTextContent("https://logistics-stage.ecpay.com.tw/Express/map");
		while (iterator.hasNext()) {
			String key = iterator.next();
			//jsonObject.put(key, map.get(key));
			Element trElement = document.createElement("tr");
			parametersElement.appendChild(trElement);
			Element keyElement = document.createElement("key");
			trElement.appendChild(keyElement);
			Element valueElement = document.createElement("value");
			trElement.appendChild(valueElement);
			keyElement.setTextContent(key);
			valueElement.setTextContent(map.get(key));
		}
		
		ModelAndView modelAndView = new ModelAndView("XSLTView");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
	
	/**
	 * 地圖資訊回傳
	 * @param request
	 * @return 
	 */
	@PostMapping (path = "/index2/map/return", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	String map(HttpServletRequest request){
		StringBuilder stringBuilder = new StringBuilder();
		List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
		Iterator<String> iterator=keys.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			stringBuilder.append(key).append("=").append(request.getParameter(key)).append("\n");
		}
		return stringBuilder.toString();
	}
}
