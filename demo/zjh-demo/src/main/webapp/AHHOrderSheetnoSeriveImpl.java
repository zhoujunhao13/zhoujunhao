package com.efuture.omo.daemon.ahh.general.component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.efuture.omd.common.component.BasicComponentService;
import com.efuture.omd.common.entity.BeanConstant;
import com.efuture.omd.common.entity.ServiceResponse;
import com.efuture.omd.common.entity.ServiceSession;
import com.efuture.omd.common.language.ResponseCode;
import com.efuture.omd.common.util.SpringBeanFactory;
import com.efuture.omd.common.util.UniqueID;
import com.efuture.omo.daemon.ahh.general.intf.AHHOrderSheetnoSerive;
import com.efuture.omo.daemon.ahh.util.MyBeanUtils;
import com.efuture.omo.daemon.aysin.general.component.OrderServiceImpl;
import com.efuture.omo.daemon.aysin.general.entity.BuyerBean;
import com.efuture.omo.daemon.aysin.general.entity.InvoiceBean;
import com.efuture.omo.daemon.aysin.general.entity.OrderDetailBean;
import com.efuture.omo.daemon.aysin.general.entity.OrderHeaderBean;
import com.efuture.omo.daemon.aysin.general.entity.OrderPaymentBean;
import com.efuture.omo.daemon.aysin.general.entity.ReceiverBean;
import com.efuture.omo.online.entity.OrderBean;
import com.future.omni.client.entity.RowMap;
import com.future.omni.client.utils.OmdParameter;
import com.future.omni.client.utils.OmdRestUtils;
import com.future.omni.client.utils.RestUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import freemarker.template.SimpleDate;

public class AHHOrderSheetnoSeriveImpl extends BasicComponentService<OrderHeaderBean> implements AHHOrderSheetnoSerive{
	
	private String remoteURL;
	private String ahhURL;
	private String wsdlUrl;
	private String relateUrl;
	private String dicturl;
	
	
	
	
	public String getDicturl() {
		return dicturl;
	}


	public void setDicturl(String dicturl) {
		this.dicturl = dicturl;
	}


	public String getRelateUrl() {
		return relateUrl;
	}


	public void setRelateUrl(String relateUrl) {
		this.relateUrl = relateUrl;
	}


	public String getWsdlUrl() {
		return wsdlUrl;
	}


	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}


	public String getAhhURL() {
		return ahhURL;
	}


	public void setAhhURL(String ahhURL) {
		this.ahhURL = ahhURL;
	}


	public String getRemoteURL() {
		return remoteURL;
	}


	public void setRemoteURL(String remoteURL) {
		this.remoteURL = remoteURL;
	}
	
	public ServiceResponse generateOrder(ServiceSession session, JSONObject jsonparam) throws Exception {
		
		if (session == null) return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		if (StringUtils.isEmpty(jsonparam)) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY);
		if (!jsonparam.containsKey("order_header")) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY, "order_header is empty");
		if (!jsonparam.containsKey("order_detail")) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY, "order_detail is empty");
//		if (!jsonparam.containsKey("order_payment")) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY, "order_payment is empty");
//		if (!jsonparam.containsKey("buyer")) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY, "buyer is empty");
//		if (!jsonparam.containsKey("invoice")) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY, "invoice is empty");
		//if (!jsonparam.containsKey("receiver")) return ServiceResponse.buildFailure(session, ResponseCode.Exception.PARAM_IS_EMPTY, "receiver is empty");
		
		JSONObject jsonHeader = jsonparam.getJSONObject("order_header");
		JSONArray jsonDetail = jsonparam.getJSONArray("order_detail");
//		JSONArray jsonpay = jsonparam.getJSONArray("order_payment");
		JSONObject jsonbuyer = jsonparam.getJSONObject("buyer");
		JSONObject jsonInvoice = jsonparam.getJSONObject("invoice");
		JSONObject jsonReceiver = jsonparam.getJSONObject("receiver");
		
		/*
		 * 生成中台订单号
		 * */
		Long   orderID=UniqueID.getUniqueID();
//		String sheetno=Long.toHexString(orderID);  //中台订单号
		
		
		/*
		 *正向单 订单传入的参数： 
		 *order_header：会员号buyer.card_code、下单人creator_id、门店org_code  org_name、时间order_time(zijishengcheng )、应付金额payable_value，
		 *				单据总价"total_sale_value" : 249.5,、
		 *			总优惠金额 total_disc_value、商品总金额total_item_value，渠道channel_keyword,已付款金额payment
		 *order_detail(jsonarray):商品编码item_code、名称item_name、条码barcode、数sale_qty、售价sale_price、优惠金额disc_value、
		 *			、总售价sale_value、,成交金额transaction_value、重量weight
		 *
		 *order_payment（jsonarray） : pay_mode_keyword pay_mode_name  payment
		 *
		 *转换成新的json
		 *{
		 *		"buyer": {
					"email": "",
					"account": "18888888888",
					"card_code": "01000123",--cardcode
					"tel": "18888888888",
					"nick": "tiger",
					"note": "需要人工审核",--tradeMemo
					"old_score": 1000,
					"this_time_score": 158
				},
				"invoice": {
					"need": false,
					"title": "",
					"content": ""
				},
			  "order_header": {
			    "creator_id": "0",
			    "total_sale_value": 123,
			    "payable_value": 123,
			    "total_disc_value": 0,
			    "total_item_value": 123,
			    "channel_keyword": "ahh",
			    "payment": 123
			  },
			  "order_detail": [
			    {
			      "barcode": "6935284455595",
			      "item_code": "104221",
			      "item_name": "卫龙亲嘴烧川香风味22g辣条辣片",
			      "sale_price": 0.6,
			      "sale_qty": 2,
			      "sale_value": 1.2,
			      "disc_value": 1.18,
			      "transaction_value": 0.02,
			      "weight": 0.044
			    },
			    {
			      "barcode": "6935284455588",
			      "disc_value": 1.18,
			      "item_code": "102822",
			      "item_name": "卫龙亲嘴烧麦辣鸡汁味22g辣条辣片",
			      "sale_price": 0.6,
			      "sale_qty": 2,
			      "sale_value": 1.2,
			      "transaction_value": 0.02,
			      "weight": 0.044
			    }
			  ],
			  "order_payment": [
			    {
			      "pay_mode_keyword": "wxqrpay",
			      "pay_mode_name": "微信支付",
			      "payment": 0.06
			    }
			  ]
			}
		 * */
		
		
		OrderHeaderBean orderHeader = new OrderHeaderBean();
		
		BuyerBean buyer = new BuyerBean();
		if(jsonbuyer != null){
			
			buyer.setAccount(jsonbuyer.getString("account"));
			buyer.setNick(jsonbuyer.getString("nick"));
			buyer.setTel(jsonbuyer.getString("mobile"));
		}
//		JointInfoBean jointInfo = new JointInfoBean();
//		jointInfo.setJoint_code(jsonHeader.getString("org_code"));
//		jointInfo.setJoint_name(jsonHeader.getString("org_name"));
		
		ReceiverBean receiver = new ReceiverBean();
		if(jsonReceiver != null){
			receiver.setName(jsonReceiver.getString("name"));
			receiver.setMobile(jsonReceiver.getString("mobile"));
			receiver.setAddress(jsonReceiver.getString("address"));
			receiver.setProvince(jsonReceiver.getString("province"));
			receiver.setCity(jsonReceiver.getString("city"));
			receiver.setDistrict(jsonReceiver.getString("district"));
			receiver.setStreet(jsonReceiver.getString("street"));
			
			String tmpDate = jsonReceiver.getString("date");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = format.parse(tmpDate);

			receiver.setDate(date);
			receiver.setSeller_note(jsonReceiver.getString("seller_note"));
			receiver.setFate(jsonReceiver.getInteger("fate")==null? 0: jsonReceiver.getInteger("fate"));
		}
		
		InvoiceBean invoice = new InvoiceBean();
		
		orderHeader.setBuyer(buyer);
//		orderHeader.setJoint_info(jointInfo);
		orderHeader.setReceiver(receiver);
		orderHeader.setInvoice(invoice);
		
		
		JSONArray oids = this.findOrgcodeByUser(session.getEnt_id(), session.getUser_id());
		if(oids == null || oids.size() == 0) 
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "没有数据权限");
		if(oids.size() > 1) 
			return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "非门店用户，没有开单权限");
		
		
		orderHeader.setOrder_content("1");
//		orderHeader.setChannel_keyword(jsonHeader.getString("channel_keyword"));
		orderHeader.setChannel_keyword("std");
		orderHeader.setChannel_sheetno(orderID.toString());
		orderHeader.setEnt_id(session.getEnt_id());
		//orderHeader.setO(jsonHeader.getString("org_name"));
//		orderHeader.setLogistics_store_name(logistics_store_name);
		orderHeader.setOrg_code((String)oids.get(0));
		orderHeader.setSource_type_keyword("ZT");
		orderHeader.setRegion_code((String)oids.get(0));
		orderHeader.setBill_type("1");
		orderHeader.setOrder_type("7"); //订单类型：安华汇订单，注意要在字典中添加一条
		orderHeader.setIs_exchange_goods(false);
		orderHeader.setLogistics_mode_keyword("2");
		orderHeader.setLogistics_store((String)oids.get(0));
		orderHeader.setPayable_logistics_value(0.0); //应付快递费默认0
		orderHeader.setLogistics_value(0.0);
		orderHeader.setPayable_value( jsonHeader.getDouble("payable_value")
										+ (jsonHeader.getDouble("design_cost")==null?0:jsonHeader.getDouble("design_cost")) 
										+ (jsonHeader.getDouble("install_cost")==null?0:jsonHeader.getDouble("install_cost")) 
										+ (jsonHeader.getDouble("others_cost")==null?0:jsonHeader.getDouble("others_cost")) 
									);//应付金额
		orderHeader.setRound_value(0.0);
		orderHeader.setTotal_sale_value(jsonHeader.getDouble("total_sale_value"));
		orderHeader.setTotal_disc_value(jsonHeader.getDouble("total_disc_value"));
		orderHeader.setTotal_item_value(jsonHeader.getDouble("total_item_value"));
		
		orderHeader.setPayment(orderHeader.getPayable_value());//已付款额
		
		if("2".equals(jsonHeader.getString("cash_type"))){
			orderHeader.setPay_over(false);
		}else{
			orderHeader.setPay_over(true);
		}
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss");
//		String date = format.format(new Date());
		orderHeader.setOrder_time(new Date());
		orderHeader.setDiscount_fee(0.0);
		orderHeader.setU_voucher(0.0);
		orderHeader.setShop_discount_fee(0.0);
		orderHeader.setAdmin_discount_fee(0.0);
		orderHeader.setTimestamp(new Date());
//		orderHeader.setSheetno(sheetno);
		orderHeader.setPay_disc_value(0.0);
		orderHeader.setDelivery_time(new Date());
		orderHeader.setModifier_id(jsonHeader.getLongValue("creator_id"));
		orderHeader.setPickup_org_code(jsonHeader.getString("org_code"));
		orderHeader.setCreator_id(jsonHeader.getLongValue("creator_id"));
		
		orderHeader.setDesign(jsonHeader.getString("design"));
		orderHeader.setInstall(jsonHeader.getString("install"));
		orderHeader.setOthers(jsonHeader.getString("others"));
		orderHeader.setDesign_cost(jsonHeader.getDouble("design_cost")==null?0.0:jsonHeader.getDouble("design_cost"));
		orderHeader.setInstall_cost(jsonHeader.getDouble("install_cost")==null?0.0:jsonHeader.getDouble("install_cost"));
		orderHeader.setOthers_cost(jsonHeader.getDouble("others_cost")==null?0.0:jsonHeader.getDouble("others_cost"));
		orderHeader.setConvention(jsonHeader.getString("convention"));
		orderHeader.setIs_send("0"); //是否向X-Store传订单成功
		//获取自主收银的付款信息
		MultivaluedMap<String, String> urlParams = new MultivaluedMapImpl();
		urlParams.add("method", "omd.dict.search");
		urlParams.add("session",URLEncoder.encode(JSON.toJSONString(session),"UTF-8"));
		JSONObject paramJson= new JSONObject();
		paramJson.put("dict_group_code", "pay_mode");
		paramJson.put("page_size", 9999);
		JSONObject res = OmdRestUtils.doPost(dicturl, urlParams, paramJson.toJSONString());
		JSONArray dicts = res.getJSONArray("dict");
		
		JSONArray jsonpaymoney = jsonparam.getJSONArray("pay_money");
		JSONArray jsonpayacc = jsonparam.getJSONArray("pay_account");
		List<JSONObject> paystyle = new ArrayList<JSONObject>();
		if(jsonpaymoney != null && jsonpaymoney.size()>0){
			for(int i=0; i<jsonpaymoney.size(); i++){
				JSONObject payinfo = new JSONObject();
				JSONObject paymoney = jsonpaymoney.getJSONObject(i);
				//付款方式编码
				String code = paymoney.getString("code");
				payinfo.put("code", code);
				//付款金额
				String money = paymoney.getString("money");
				payinfo.put("money", money);
				for(int k=0; k<dicts.size(); k++){
					JSONObject dict = dicts.getJSONObject(k);
					String d_code = dict.getString("code");
					if(code.equals(d_code)){
						String tendtype = dict.getString("sname");
						String tendid = dict.getString("scode");
						payinfo.put("tendtype", tendtype);
						payinfo.put("tendid", tendid);
					}
				}
				//获取付款账号
				if(jsonpayacc != null && jsonpayacc.size()>0){
					for(int j=0; j<jsonpayacc.size(); j++){
						JSONObject acc = jsonpayacc.getJSONObject(j);
						String acc_code = acc.getString("code");
						if(code.equals(acc_code)){
							String account = acc.getString("account");
							payinfo.put("account", account);
						}
					}
				}
				
				paystyle.add(payinfo);
			}
			orderHeader.setPaystyle(paystyle);
		}
		//收银方式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(new Date());
		String cashtype = jsonHeader.getString("cash_type");
		String cash_type = cashtype + "-" +today;
		orderHeader.setCash_type(cash_type);
		
		
		List<OrderDetailBean> orderDetailList = new ArrayList<OrderDetailBean>();
		orderHeader.setOrder_detail(orderDetailList);
		
		for (int i = 0; i < jsonDetail.size(); i++) {
			JSONObject json = jsonDetail.getJSONObject(i);
			OrderDetailBean orderDetail = new OrderDetailBean();
			orderDetail.setBarcode(json.getString("barcode"));
			orderDetail.setChannel_detail_sheetno(orderID.toString());
			orderDetail.setCreator_id(jsonHeader.getLongValue("creator_id"));
			orderDetail.setDisc_value(json.getDouble("disc_value"));
			orderDetail.setEnt_id(session.getEnt_id());
			orderDetail.setErp_item_code(json.getString("erp_item_code"));
			orderDetail.setItem_code(json.getString("code"));
			orderDetail.setItem_name(json.getString("name"));
			orderDetail.setOrder_no(i);
			orderDetail.setPartMjzDiscount(0.0);
			orderDetail.setSale_price(json.getDouble("sale_price"));
			orderDetail.setSale_qty(json.getDouble("sale_qty"));
			orderDetail.setSale_value(json.getDouble("sale_value"));
			orderDetail.setSku_code(json.getString("erp_item_code"));
			orderDetail.setTransaction_value(json.getDouble("transaction_value"));
			orderDetail.setTransaction_price(json.getDouble("transaction_price")==null?json.getDouble("sale_price"):json.getDouble("transaction_price"));
			orderDetail.setWeight(json.getDouble("weight")==null?0.0:json.getDouble("weight"));
			orderDetail.setDescribe(json.getString("describe") == null? "无" :json.getString("describe"));
			orderDetailList.add(orderDetail);
		}	
		
		List<OrderPaymentBean> payList = new ArrayList<OrderPaymentBean>();
		orderHeader.setOrder_payment(payList);
		
		
		if("1".equals(jsonparam.getString("paytype"))){
			OrderPaymentBean orderPay = new OrderPaymentBean();
			orderPay.setChannel_payment_sheetno(orderID.toString());
			orderPay.setPay_mode_keyword(jsonparam.getString("paymode"));
			orderPay.setPay_mode_name(jsonparam.getString("payname") == null ? "": jsonparam.getString("payname"));
			orderPay.setPayment(jsonHeader.getDouble("payable_value"));
			orderPay.setOther_payment(jsonparam.getDouble("other_payment")==null?0.0:jsonparam.getDouble("other_payment"));
			orderPay.setPaytype(jsonparam.getString("paytype"));
			
			
			orderPay.setOrder_no(1);
			payList.add(orderPay);
			
		}else if("2".equals(jsonparam.getString("paytype"))){
			
			int count = 1;
			
			OrderPaymentBean orderPay = new OrderPaymentBean();
			orderPay.setChannel_payment_sheetno(orderID.toString());
			orderPay.setPay_mode_keyword(jsonparam.getString("paymode"));
			orderPay.setPay_mode_name(jsonparam.getString("payname") == null ? "": jsonparam.getString("payname"));
			orderPay.setPaytype(jsonparam.getString("paytype"));
			orderPay.setPayment(jsonparam.getDouble("firstpay"));
			orderPay.setOther_payment(jsonparam.getDouble("firstpay_other"));

			
			orderPay.setOrder_no(count++);
			payList.add(orderPay);
			OrderPaymentBean orderPayClone = new OrderPaymentBean();
			
			if(jsonparam.getDouble("middlepay")!=0.0){
				MyBeanUtils.copyProperties(orderPay, orderPayClone);
				orderPayClone.setOrder_no(count++);
				orderPayClone.setPayment(jsonparam.getDouble("middlepay"));
				orderPayClone.setOther_payment(jsonparam.getDouble("middlepay_other"));

				payList.add(orderPayClone);
			}
			
			if(jsonparam.getDouble("lastpay")!=0.0){
				MyBeanUtils.copyProperties(orderPay, orderPayClone);
				orderPayClone.setOrder_no(count++);
				orderPayClone.setPayment(jsonparam.getDouble("lastpay"));
				orderPayClone.setOther_payment(jsonparam.getDouble("lastpay_other"));

				payList.add(orderPayClone);
			}
			
		}

		
		List<OrderHeaderBean> olist = new ArrayList<OrderHeaderBean>();
		olist.add(orderHeader);
		
		JSONObject returnparam = new JSONObject();
		if(olist.size()>0){
			returnparam.put("order",olist);
		}
		returnparam.put("extraInformation", jsonparam.getJSONObject("extraInformation"));//获取JDE数据
		
		OrderServiceImpl orderrevice = SpringBeanFactory.getBean("daemon.omo.order",OrderServiceImpl.class);

		/*
		 * 返回单号
		 * */
		System.out.println(returnparam);
		
		ServiceResponse response = orderrevice.receive(session, returnparam.toJSONString());
		
		//--------------
		@SuppressWarnings("unchecked")
		ArrayList<String> ordernos = (ArrayList<String>)response.getData();
		String orderno = ordernos.get(0);
		Criteria criteria1 = new Criteria();
		Query query1 = new Query();
		Update update1 = new Update();
		criteria1.and("sheetno").is(orderno);
		query1.addCriteria(criteria1);
		update1.set("is_send", "0");
		update1.set("cash_type", cash_type);
		update1.set("paystyle", paystyle);
		this.getTemplate().updateFirst(query1, update1, OrderHeaderBean.class);
		//------------------
		
		System.out.println(response.getData());
		
		if("0".equals(response.getReturncode()) && "2".equals(jsonHeader.getString("cash_type"))){
			@SuppressWarnings("unchecked")
			ArrayList<String> jarr = (ArrayList<String>)response.getData();
			String sheetno = jarr.get(0);
			
			returnparam.put("sheetno", sheetno);
			
			System.out.println("~returnparam:~"+returnparam);
			
			//异步方式调用接口向X-Store传递订单-----------------
			final ServiceSession session1 = session;
			final JSONObject returnparam1 = returnparam;
			final String sheetno1 = sheetno;
			Thread t= new Thread(){
				public void run(){
					ServiceResponse rep = sendAhhOrder(session1, returnparam1);
					if(!"0".equals(rep.getReturncode())){
						Criteria criteria = new Criteria();
						Query query = new Query();
						Update update = new Update();
						criteria.and("sheetno").is(sheetno1);
						query.addCriteria(criteria);
						update.set("is_send", "1");
						AHHOrderSheetnoSeriveImpl ahh = new AHHOrderSheetnoSeriveImpl();
						ahh.getTemplate().updateFirst(query, update, OrderHeaderBean.class);
						System.out.println("订单传递失败！");
					}
					System.out.println("订单传递成功！");
					
				}
			};
			t.start();
			//-----------------------------------------
			/*
			ServiceResponse rep = sendAhhOrder(session, returnparam);
			
			if(!"0".equals(rep.getReturncode())){
				JSONObject result = new JSONObject();
				result.put("data1", response.getData());
				result.put("data2", rep.getData());
				
				response.setData(result);
				
			}
			*/
		}
		
		
		return response;
		
	}
	
//	public ServiceResponse aa(ServiceSession session, JSONObject json) throws Exception{
//		testGenerator(session, json);
//		return null;
//	}
//	public void testGenerator(ServiceSession session, JSONObject json) throws Exception{
//		
//		
//		OrderServiceImpl orderrevice = SpringBeanFactory.getBean("daemon.omo.order",OrderServiceImpl.class);
//		 
//		
//		ServiceResponse response = orderrevice.receive(session, json.toJSONString());
//		
//		if("0".equals(response.getReturncode())){
//			@SuppressWarnings("unchecked")
//			ArrayList<String> jarr = (ArrayList<String>)response.getData();
//			String sheetno = jarr.get(0);
//			
//			 
//			json.put("sheetno", sheetno);
//			System.out.println(json);
//			sendAhhOrder(session, json);
//		}
//	}
	
	
	public JSONArray findOrgcodeByUser(long ent_id,long user_id)
			throws Exception {
		
		List<Object> temp_oid_list1 = new ArrayList<Object>();//存储，从data_permission查询出来Data_id，即org_id
		
		ServiceSession session=new ServiceSession();
		session.setEnt_id(ent_id);
		session.setUser_id(user_id);
		session.setUser_name("订单中心");
		
		MultivaluedMap<String, String> urlParams = new MultivaluedMapImpl();
		urlParams.add("method", "omd.permission.data.search");
		urlParams.add("session",URLEncoder.encode(JSON.toJSONString(session),"UTF-8"));
		
		JSONObject params = new JSONObject();
		params.put("employee_id", user_id);
		params.put("data_type", "org");
		params.put(BeanConstant.QueryField.PARAMKEY_PAGESIZE, 999);
		params.put(BeanConstant.QueryField.PARAMKEY_PAGENO, 1);
		params.put(BeanConstant.QueryField.PARAMKEY_FIELDS, "data_code");
		
		try {
			//保留空值
			String data = JSON.toJSONString(params, SerializerFeature.WriteMapNullValue);
			JSONObject response=OmdRestUtils.doPost(this.remoteURL, urlParams, data);
			//System.out.println(response);
			
			JSONArray result=response.getJSONArray("data_permission");
			if(result != null && result.size()>0){
				for(Object value :result){
					Map<String,Object> dataMap = (Map<String,Object>)value;
					temp_oid_list1.add(dataMap.get("data_code"));
				}
				return this.converList2JsonArray(temp_oid_list1);
			} 
			return null;
		} catch(Exception e) {
			//e.printStackTrace();
			return null;
		}
		
	}
	
	private JSONArray converList2JsonArray(List<?> list){
		JSONArray jsonArray = new JSONArray();
		for(Object obj : list){
			jsonArray.add(obj);
		}
		return jsonArray;
	}
	
	public ServiceResponse getAhhCustomerOnSoapByTel(ServiceSession session, JSONObject jsonparam) {
		try {
			if (session == null)
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.SESSION_IS_EMPTY);
			if (StringUtils.isEmpty(jsonparam))
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.PARAM_IS_EMPTY);

			String ns = "http://v2_3.customer.webservices.csx.dtv.com/";
			// 1、创建服务(Service)
			URL url = new URL(wsdlUrl);
			QName sname = new QName(ns, "CustomerServicesApiService");
			Service service = Service.create(url, sname);

			// 2、创建Dispatch
			Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(
					ns, "CustomerServicesApiPort"), SOAPMessage.class,
					Service.Mode.MESSAGE);

			// 3、创建SOAPMessage
			SOAPMessage msg = MessageFactory.newInstance(
					SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();
			SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
			// envelope.setPrefix("S");
			// envelope.removeAttribute("xmlns:SOAP-ENV");
			// envelope.getHeader();
			// envelope.removeChild(envelope.getHeader());
			SOAPBody body = envelope.getBody();
			// body.setPrefix("S");
			// 4、创建QName来指定消息中传递数据
			QName ename = new QName(ns, "searchCustomers", "ns2");// <nn:add
																	// xmlns="xx"/>
			SOAPBodyElement ele = body.addBodyElement(ename);
			// 传递参数
			// =============================================
			
			
			SOAPElement TelephoneE = SOAPFactory.newInstance().createElement(
					"Telephone");
			TelephoneE.setAttribute("TypeCode", "MOBILE");
			
			if(jsonparam.containsKey("mobile")){
				String mobile = jsonparam.getString("mobile");
				SOAPElement phoneElement = SOAPFactory.newInstance()
						.createElement("PhoneNumber").addTextNode(mobile);
				TelephoneE.addChildElement(phoneElement);
			}
			
			SOAPElement ContactInformationE = SOAPFactory.newInstance()
					.createElement("ContactInformation");
			ContactInformationE.addChildElement("Address");
			ContactInformationE.addChildElement("EMail");
			ContactInformationE.addChildElement(TelephoneE);

			SOAPElement IndividualE = SOAPFactory.newInstance().createElement(
					"Individual");

			IndividualE.addChildElement("Name");
			IndividualE.addChildElement(ContactInformationE);

			SOAPElement EntityInformationE = SOAPFactory.newInstance()
					.createElement("EntityInformation");
			EntityInformationE.addChildElement(IndividualE);
			
			
		
			EntityInformationE.addChildElement(IndividualE);

			
			SOAPElement customerDataE = SOAPFactory.newInstance()
					.createElement("customerData");
			customerDataE.addChildElement("Instrument");
			customerDataE.addChildElement(EntityInformationE);
			
			if(jsonparam.containsKey("account")){
				String account = jsonparam.getString("account");
				SOAPElement AlternateKeyE = SOAPFactory.newInstance()
						.createElement("AlternateKey");
				AlternateKeyE.setAttribute("TypeCode", "XSTORE_CUST_ID");
				SOAPElement AlternateIDE = SOAPFactory.newInstance()
						.createElement("AlternateID").addTextNode(account);
				
				AlternateKeyE.addChildElement(AlternateIDE);
				customerDataE.addChildElement(AlternateKeyE);
			}

			ele.addChildElement(customerDataE);

			ele.addChildElement("securityUserId").setValue("100");

			// ===============================================
			msg.writeTo(System.out);

			System.out.println("\n invoking.....");

			// 5、通过Dispatch传递消息,会返回响应消息
			SOAPMessage response;

			response = dispatch.invoke(msg);
			response.writeTo(System.out);
			System.out.println();
			SOAPBody resbody = response.getSOAPPart().getEnvelope().getBody();

			
			String account = "";
			NodeList nodelist = resbody.getElementsByTagName("AlternateKey");
			
			for (int i = 0; i < nodelist.getLength(); i++) {
				for (int j = 0; j < nodelist.item(i).getAttributes().getLength(); j++) {
					if("XSTORE_CUST_ID".equals(nodelist.item(i).getAttributes().item(j).getTextContent())){
						account = nodelist.item(i).getTextContent();
					}
				}
			}
			

			String customerName = resbody.getElementsByTagName("Name").item(0)
					.getTextContent();
			
			//-----计算积分获取CustomerID和AlternateID
			//获取CustomerID
			String CustomerID = resbody.getElementsByTagName("CustomerID").item(0)
					.getTextContent();;
			
			//获取AlternateID
			String AlternateID = "";
			List<String> AlternateIDs = new ArrayList<String>();
			for (int i = 0; i < nodelist.getLength(); i++) {
				for (int j = 0; j < nodelist.item(i).getAttributes().getLength(); j++) {
					if("XSTORE_ID".equals(nodelist.item(i).getAttributes().item(j).getTextContent())){
						AlternateID = nodelist.item(i).getTextContent();
						AlternateIDs.add(AlternateID);
					}
				}
			}
			
			//-----------------------------------
						
			JSONObject returnJson = new JSONObject();
			JSONObject result = new JSONObject();
			
			result.put("account", account);
			result.put("nick", customerName);
			//----
			result.put("customerID", CustomerID);
			result.put("alternateIDs", AlternateIDs);
 		
			return ServiceResponse.buildSuccess(result);
		
		} catch (Exception ex) {
			this.getLogger().error(ex.getMessage(),ex);
            return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,ex.getMessage());
		}

	}
	
	public ServiceResponse getAhhTransactions(ServiceSession session, JSONObject jsonparam){
		String METHOD = "/transaction/submitTransactionForDeals";

		try{
			if (session == null)
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.SESSION_IS_EMPTY);
			if (StringUtils.isEmpty(jsonparam))
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.PARAM_IS_EMPTY);
			
			OmdParameter op=new OmdParameter();
			JSONObject json = new JSONObject();
			JSONObject jsonTransaction = new JSONObject();
			JSONObject jsonSaleLines = new JSONObject();
			
			JSONObject buyer = jsonparam.getJSONObject("buyer");
			
			jsonTransaction.put("operatorId", "100");
			jsonTransaction.put("customerId", buyer.getString("account"));
			jsonTransaction.put("customerName", buyer.getString("nick"));
			jsonTransaction.put("customerPhone", buyer.getString("mobile"));
			
			json.put("transaction", jsonTransaction);
			
			JSONArray arr = new JSONArray();
			for (Object o : jsonparam.getJSONArray("order_detail")) {
				JSONObject jsonSaleDetail = new JSONObject();
				JSONObject jo = (JSONObject)o;
				
				jsonSaleDetail.put("itemId", jo.getString("code"));
				jsonSaleDetail.put("quantity", jo.getDouble("sale_qty") == null?0:jo.getDouble("sale_qty"));
				jsonSaleDetail.put("price", jo.getDouble("unit_price") == null?0:jo.getDouble("unit_price"));
				jsonSaleDetail.put("commissionId", "100");
				
				arr.add(jsonSaleDetail);
			}
			
			json.put("saleLines", arr);
			//促销商品号码，目前是写死的
			/*
			JSONObject couponid = new JSONObject();
			JSONArray coupons = new JSONArray();
			couponid.put("couponId", "12345");
			coupons.add(couponid);
			json.put("coupons", coupons);
			*/
			op.parseObject(json);
			JSONObject jo = op.parseObject();
			jo.remove("page_no");
			jo.remove("page_size");
			String value = jo.toJSONString();
			
			System.out.println(value);
			MultivaluedMap<String, String> paraMap = new MultivaluedMapImpl();
			
			paraMap.add("JSON", value);
			
			value = URLEncoder.encode(value);
			
			value= value.replace("\"", "\'");
			String url = ahhURL+METHOD+"?JSON="+value;
			System.out.println(url);
			Client client = null;
			client = RestUtils.getClientPool().borrowObject();
			WebResource webResource = client.resource(url);
			String response=webResource.get(String.class);
			System.out.println(response);
			if(JSON.parseObject(response).getBoolean("success")){
				JSON.parseObject(response).get("data");
				return ServiceResponse.buildSuccess(JSON.parseObject(response).get("data"));
			}else{
				return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION, JSON.parseObject(response).getString("err_message"));
			}
		}catch(Exception ex){
			this.getLogger().error(ex.getMessage(),ex);
            return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,ex.getMessage());
		}
	}
	/**
	 * 获取X-store积分计算规则，计算出自主收银的会员积分
	 * @param session
	 * @param jsonparam
	 * @return
	 */
	public ServiceResponse getPoints(ServiceSession session, JSONObject jsonparam) throws Exception {
		try{
			if (session == null)
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.SESSION_IS_EMPTY);
			if (StringUtils.isEmpty(jsonparam))
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.PARAM_IS_EMPTY);
			String customerID = jsonparam.getString("customerID");
			List<String> alternateIDs = (List<String>) jsonparam.get("alternateIDs");
			
			String ns = "http://v2_3.customer.webservices.csx.dtv.com/";
			// 1、创建服务(Service)
			URL url = new URL(wsdlUrl);
			QName sname = new QName(ns, "CustomerServicesApiService");
			Service service = Service.create(url, sname);
			
			// 2、创建Dispatch
			Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(
					ns, "CustomerServicesApiPort"), SOAPMessage.class,
					Service.Mode.MESSAGE);
			
			// 3、创建SOAPMessage
			SOAPMessage msg = MessageFactory.newInstance(
					SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();
			SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
			SOAPBody body = envelope.getBody();
			envelope.setPrefix("S");
			envelope.removeAttribute("xmlns:SOAP-ENV");
			//envelope.getHeader();
			envelope.removeChild(envelope.getHeader());
			body.setPrefix("S");
			// 4、创建QName来指定消息中传递数据
			QName ename = new QName(ns, "retrieveCustomer", "ns2");
			
			SOAPBodyElement ele = body.addBodyElement(ename);
			
			//添加会员id
			SOAPElement customer = SOAPFactory.newInstance()
					.createElement("customerId").addTextNode(customerID);
			
			//添加AlternateID
			SOAPElement alternateID = SOAPFactory.newInstance().createElement(
					"alternateKeyList");
			alternateID.setAttribute("TypeCode", "XSTORE_ID");
			if(alternateIDs.size()>0){
				for(int i=0; i<alternateIDs.size(); i++){
					String aid = alternateIDs.get(i);
					SOAPElement alternateElement = SOAPFactory.newInstance()
							.createElement("AlternateID").addTextNode(aid);
					alternateID.addChildElement(alternateElement);
				}
			}
			
			ele.addChildElement(customer);
			ele.addChildElement(alternateID);
			ele.addChildElement("securityUserId").setValue("100");
			msg.writeTo(System.out);
			
			System.out.println("\n invoking.....");
			
			SOAPMessage response;

			response = dispatch.invoke(msg);
			response.writeTo(System.out);
			
			SOAPBody resbody = response.getSOAPPart().getEnvelope().getBody();
			//NodeList nodelist = resbody.getElementsByTagName("CustomerCards");

			String cardNumber = resbody.getElementsByTagName("CardNumber").item(0).getTextContent();
			String cardSerialNumber = resbody.getElementsByTagName("CardSerialNumber").item(0).getTextContent();
			String accountId = resbody.getElementsByTagName("AccountId").item(0).getTextContent();
		
			System.out.println("\n"+cardNumber+"/"+cardSerialNumber+"/"+accountId);
			JSONObject result = new JSONObject();
			result.put("cardNumber", cardNumber);
			result.put("cardSerialNumber", cardSerialNumber);
			result.put("accountId", accountId);
			
			return ServiceResponse.buildSuccess(result);
			
		}catch( Exception e){
			e.printStackTrace();
		}
		return ServiceResponse.buildSuccess("success");
	}
	/**
	 * 向relate传递从X-store获取到的积分计算规则
	 * @param session
	 * @param jsonparam
	 * @return
	 * @throws Exception
	 */

	public ServiceResponse toPostTransaction(ServiceSession session, JSONObject jsonparam) throws Exception {
		try{
			if (session == null)
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.SESSION_IS_EMPTY);
			if (StringUtils.isEmpty(jsonparam))
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.PARAM_IS_EMPTY);
			
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss.SSS");
			//String date1 = format1.format(new Date());
			//String date2 = format2.format(new Date());
			
			String ns = "http://v1_0.poslog.webservices.csx.dtv.com/";
			// 1、创建服务(Service)
			URL url = new URL(relateUrl);
			QName sname = new QName(ns, "PoslogServicesApiService");
			Service service = Service.create(url, sname);
			
			// 2、创建Dispatch
			Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(
					ns, "PoslogServicesApiPort"), SOAPMessage.class,
					Service.Mode.MESSAGE);
			
			// 3、创建SOAPMessage
			SOAPMessage msg = MessageFactory.newInstance(
					SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();
			msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-8");
			SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
			
			envelope.addNamespaceDeclaration("v1", "http://v1_0.poslog.webservices.csx.dtv.com/");
			
			SOAPBody body = envelope.getBody();
			envelope.setPrefix("soapenv");
			envelope.removeAttribute("xmlns:SOAP-ENV");
			envelope.getHeader().setPrefix("soapenv");
			//envelope.removeChild(envelope.getHeader());
			body.setPrefix("soapenv");
			// 4、创建QName来指定消息中传递数据
			QName ename = new QName(ns, "postTransaction", "v1");
			SOAPBodyElement ele = body.addBodyElement(ename);
			
			SOAPElement transaction = SOAPFactory.newInstance().createElement("transaction");
			transaction.setAttribute("CancelFlag", "false");
			transaction.setAttribute("Action", "PostTransaction,CalcProformaPoints,PointRecovery");
			transaction.setAttribute("TransactionType", "RETAIL_SALE");
			transaction.addChildElement("RetailStoreID").setValue("102");
			
			String orderId = jsonparam.getString("sheetno");	//订单号
			System.out.println(orderId);
			//查出订单的信息
			StringBuffer total = new StringBuffer();
			JSONObject json = new JSONObject();
			json.put("sheetno", orderId);
			List<OrderHeaderBean> orderlist = null;
			List<OrderDetailBean> detiallist = null;
			List<JSONObject> paystyle = null;
			String time = "";
			String time2 = "";
			orderlist = doSearch(json,OrderHeaderBean.class,null,true,total);
			if(orderlist.size() > 0){
				for(int i=0; i<orderlist.size(); i++){
					OrderHeaderBean order = orderlist.get(i);
					String org_code = order.getOrg_code();
					Date order_time = order.getOrder_time();
					String oid = String.valueOf(order.getOid());
					oid = oid.substring(oid.length()-8, oid.length());
					paystyle = order.getPaystyle();
					time = format1.format(order_time);
					String time1 = format2.format(order_time);
					time2 = time + "T" + time1;
					System.out.println(time2);
					
					transaction.addChildElement("WorkstationID").setValue(org_code);	//org_code
					transaction.addChildElement("SequenceNumber").setValue(oid);	//id
					//transaction.addChildElement("SequenceNumber").setValue("117");	//id
					transaction.addChildElement("BusinessDayDate").setValue(time);	//下单时间
					transaction.addChildElement("BeginDateTime").setValue(time2);
					transaction.addChildElement("EndDateTime").setValue(time2);
				}
			}
		
			transaction.addChildElement("OperatorID").setValue("100");
			transaction.addChildElement("CurrencyCode").setValue("CNY");
			
			SOAPElement retailTransaction = SOAPFactory.newInstance().createElement("RetailTransaction");
			retailTransaction.setAttribute("Version", "2.1");
			retailTransaction.setAttribute("TypeCode", "Transaction");
			retailTransaction.setAttribute("TransactionStatus", "Delivered");
			//transaction.addChildElement(retailTransaction);
			
			int num = 0;
			//商品明细
			detiallist = doSearch(json, OrderDetailBean.class,null,true,total);
			if(detiallist.size() > 0){
				for(int i=0; i<detiallist.size(); i++){
					num = i + 1;
					OrderDetailBean detail = detiallist.get(i);
					String item_code = detail.getItem_code();
					String sale_price = String.valueOf(detail.getSale_price());	//元单价
					double transaction_price = detail.getTransaction_price();
					String zongjia = String.valueOf(transaction_price);
					double sale_qty = detail.getSale_qty();
					String unit_price = String.valueOf(transaction_price/sale_qty);	//现单价
					String qty = String.valueOf(sale_qty);	//数量
					
					SOAPElement lineItem = SOAPFactory.newInstance().createElement("LineItem");
					lineItem.setAttribute("VoidFlag", "false");
					lineItem.addChildElement("SequenceNumber").setValue(String.valueOf(i+1));
					lineItem.addChildElement("BeginDateTime").setValue(time2);
					lineItem.addChildElement("EndDateTime").setValue(time2);
					
					SOAPElement sale = SOAPFactory.newInstance().createElement("Sale");
					sale.setAttribute("ItemType", "Stock");
					sale.setAttribute("Action", "Completed");
					sale.addChildElement("ItemID").setValue(item_code);	//商品编码
					sale.addChildElement("UnitCostPrice").setValue(unit_price);
					sale.addChildElement("RegularSalesUnitPrice").setValue(sale_price);	//原单价
					sale.addChildElement("ActualSalesUnitPrice").setValue(unit_price);	//实际单价
					sale.addChildElement("ExtendedAmount").setValue(zongjia);	//总价
					sale.addChildElement("Quantity").setValue(qty);	//数量
					
					SOAPElement associate = SOAPFactory.newInstance().createElement("Associate");
					associate.addChildElement("AssociateID").setValue("100");
					sale.addChildElement(associate);
					
					SOAPElement percentageOfItem = SOAPFactory.newInstance().createElement("PercentageOfItem");
					percentageOfItem.addChildElement("AssociateID").setValue("100");
					percentageOfItem.addChildElement("Percentage").setValue("1");
					sale.addChildElement(percentageOfItem);
					
					lineItem.addChildElement(sale);
					retailTransaction.addChildElement(lineItem);
	
				}
			}
			
			//付款信息
			if(paystyle.size()>0){
				for(int i=0; i<paystyle.size(); i++){
					JSONObject pay = paystyle.get(i);
					String money = pay.getString("money");	//支付金额
					String tendtype = pay.getString("tendtype");	//付款类型
					String tendid = pay.getString("tendid");
					SOAPElement lineItem = SOAPFactory.newInstance().createElement("LineItem");
					lineItem.addChildElement("SequenceNumber").setValue(String.valueOf(num+i+1));
					lineItem.addChildElement("BeginDateTime").setValue(time2);
					lineItem.addChildElement("EndDateTime").setValue(time2);
					
					SOAPElement tender = SOAPFactory.newInstance().createElement("Tender");
					tender.setAttribute("TenderType", tendtype);
					tender.addChildElement("TenderID").setValue(tendid);
					tender.addChildElement("Amount").setValue(money);
					lineItem.addChildElement(tender);
					
					retailTransaction.addChildElement(lineItem);
				}
			}
			
			//总金额
			JSONObject jsonHeader = jsonparam.getJSONObject("order_header");
			double payable_value = jsonHeader.getDoubleValue("payable_value");
			double design_cost = jsonHeader.getDoubleValue("design_cost");
			double install_cost = jsonHeader.getDoubleValue("install_cost");
			double others_cost = jsonHeader.getDoubleValue("others_cost");
			double totalValue = payable_value + design_cost + install_cost + others_cost;
			String total_value = String.valueOf(totalValue);
			SOAPElement Total = SOAPFactory.newInstance().createElement("Total");
			Total.setAttribute("TotalType", "TransactionGrandAmount");
			Total.setValue(total_value);
			retailTransaction.addChildElement(Total);
			
			
			//会员信息
			JSONObject customer = jsonparam.getJSONObject("customer");
			String customerid = customer.getString("customerId");
			String accountid = customer.getString("accountId");
			List<String> alternateids = (List<String>)customer.get("alternateIds");
			String cardnumber = customer.getString("cardNumber");
			
			SOAPElement Customer = SOAPFactory.newInstance().createElement("Customer");
			Customer.addChildElement("CustomerID").setValue(customerid);
			Customer.addChildElement("AccountNumber").setValue(accountid);
			for(int i=0; i<alternateids.size(); i++){
				String alternateID = alternateids.get(i);
				SOAPElement alternateKey = SOAPFactory.newInstance().createElement("AlternateKey");
				alternateKey.setAttribute("TypeCode", "XSTORE_ID");
				alternateKey.addChildElement("AlternateID").setValue(alternateID);
				Customer.addChildElement(alternateKey);
			}
			retailTransaction.addChildElement(Customer);
			
			SOAPElement customerAccount = SOAPFactory.newInstance().createElement("CustomerAccount");
			customerAccount.addChildElement("CardNumber").setValue(cardnumber);
			SOAPElement loyaltyAccount = SOAPFactory.newInstance().createElement("LoyaltyAccount");
			loyaltyAccount.setAttribute("TypeCode", "LOYALTY");
			loyaltyAccount.addChildElement("LoyaltyAccountID").setValue(accountid);
			customerAccount.addChildElement(loyaltyAccount);
			
			retailTransaction.addChildElement(customerAccount);
			
			transaction.addChildElement(retailTransaction);
			
			//获取机构信息
			JSONObject extraInformation = jsonparam.getJSONObject("extraInformation");
			//classCode
			String classCode = extraInformation.getString("classCode");
			SOAPElement classCodeEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			classCodeEl.addChildElement("PosTransactionPropertyCode").setValue("ClassCode");
			classCodeEl.addChildElement("PosTransactionPropertyValue").setValue(classCode);
			transaction.addChildElement(classCodeEl);
			
			//closeTag
			String closeTag = extraInformation.getString("closeTag");
			SOAPElement closeTagEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			closeTagEl.addChildElement("PosTransactionPropertyCode").setValue("CloseTag");
			closeTagEl.addChildElement("PosTransactionPropertyValue").setValue(closeTag);
			transaction.addChildElement(closeTagEl);
			
			//contractNo
			String contractNo = extraInformation.getString("contractNo");
			SOAPElement contractNoEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			contractNoEl.addChildElement("PosTransactionPropertyCode").setValue("ContractNo");
			contractNoEl.addChildElement("PosTransactionPropertyValue").setValue(contractNo);
			transaction.addChildElement(contractNoEl);
			
			//marketName
			String marketName = extraInformation.getString("marketName");
			SOAPElement marketNameEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			marketNameEl.addChildElement("PosTransactionPropertyCode").setValue("MarketName");
			marketNameEl.addChildElement("PosTransactionPropertyValue").setValue(marketName);
			transaction.addChildElement(marketNameEl);
			
			//marketNo
			String marketNo = extraInformation.getString("marketNo");
			SOAPElement marketNoEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			marketNoEl.addChildElement("PosTransactionPropertyCode").setValue("MarketNo");
			marketNoEl.addChildElement("PosTransactionPropertyValue").setValue(marketNo);
			transaction.addChildElement(marketNoEl);
			
			//orderId
			SOAPElement orderIdEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			orderIdEl.addChildElement("PosTransactionPropertyCode").setValue("OrderId");
			orderIdEl.addChildElement("PosTransactionPropertyValue").setValue(orderId);
			transaction.addChildElement(orderIdEl);
			
			//shopNo
			String shopNo = extraInformation.getString("shopNo");
			SOAPElement shopNoEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			shopNoEl.addChildElement("PosTransactionPropertyCode").setValue("ShopNo");
			shopNoEl.addChildElement("PosTransactionPropertyValue").setValue(shopNo);
			transaction.addChildElement(shopNoEl);
			
			//tenantCode
			String tenantCode = extraInformation.getString("tenantCode");
			SOAPElement tenantCodeEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			tenantCodeEl.addChildElement("PosTransactionPropertyCode").setValue("TenantCode");
			tenantCodeEl.addChildElement("PosTransactionPropertyValue").setValue(tenantCode);
			transaction.addChildElement(tenantCodeEl);
			
			//tenantName
			String tenantName = extraInformation.getString("tenantName");
			SOAPElement tenantNameEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			tenantNameEl.addChildElement("PosTransactionPropertyCode").setValue("TenantName");
			tenantNameEl.addChildElement("PosTransactionPropertyValue").setValue(tenantName);
			transaction.addChildElement(tenantNameEl);
			
			//tenderType
			String tenderType = extraInformation.getString("tenderType");
			SOAPElement tenderTypeEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			tenderTypeEl.addChildElement("PosTransactionPropertyCode").setValue("TenderType");
			tenderTypeEl.addChildElement("PosTransactionPropertyValue").setValue(tenderType);
			transaction.addChildElement(tenderTypeEl);
			
			//bizCode
			String bizCode = extraInformation.getString("bizCode");
			SOAPElement bizCodeEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			bizCodeEl.addChildElement("PosTransactionPropertyCode").setValue("BizCode");
			bizCodeEl.addChildElement("PosTransactionPropertyValue").setValue(bizCode);
			transaction.addChildElement(bizCodeEl);
			
			//bizDescription
			String bizDescription = extraInformation.getString("bizDescription");
			SOAPElement bizDescriptionEl = SOAPFactory.newInstance().createElement("PosTransactionProperties");
			bizDescriptionEl.addChildElement("PosTransactionPropertyCode").setValue("BizDescription");
			bizDescriptionEl.addChildElement("PosTransactionPropertyValue").setValue(bizDescription);
			transaction.addChildElement(bizDescriptionEl);
			
			
			ele.addChildElement(transaction);
			
			msg.writeTo(System.out);
			System.out.println("\n invoking.....");
			
			SOAPMessage response;

			response = dispatch.invoke(msg);
			response.writeTo(System.out);
			System.out.println();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return ServiceResponse.buildSuccess("success");
	}
	//安华的说：Mr.Su：
	/* 计算方式：
	 * 商品A     数量：3   价格：648.15（四舍五入）
		商品B      数量：5  价格：311.12
		其它费用       数量：1  价格 500
		计算规则例如：商品A的价格= (2500x3)除以总金额（14000-其它费用）x首付（4000-其它费用）除以商品数量3
		
		我明白你的意思，你可以就是定义一个商品ID用来专门记载其它费用的（不把它弄到正常的商品分类里面就可以了），传给我们。
		
		使用商品99999999代替其他费用
	 */
	public ServiceResponse sendAhhOrder(ServiceSession session, JSONObject jsonparam){
		
		String METHOD = "/transaction/submitTransaction";
		try{
			if (session == null)
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.SESSION_IS_EMPTY);
			if (StringUtils.isEmpty(jsonparam))
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.PARAM_IS_EMPTY);
			
			
			String sheetno = jsonparam.getString("sheetno");
			
			OmdParameter op=new OmdParameter();
			JSONObject json = new JSONObject();
			JSONObject jdeinform = jsonparam.getJSONObject("extraInformation");
			
			
			jsonparam = (JSONObject)jsonparam.getJSONArray("order").get(0);
			
			/*
			 * 封装会员信息
			 */
			JSONObject jsonTransaction = new JSONObject();
			JSONObject buyer = jsonparam.getJSONObject("buyer");
			
			jsonTransaction.put("operatorId", "100");
			if(buyer != null && buyer.size() > 0){
				jsonTransaction.put("customerId", buyer.getString("account"));
				jsonTransaction.put("customerName", buyer.getString("nick"));
				jsonTransaction.put("customerPhone", buyer.getString("tel"));
				
			}
			jsonTransaction.put("orderId", sheetno);
			
			json.put("transaction", jsonTransaction);
			
			/*
			 * 封装商品明细信息
			 */
			JSONArray arr = new JSONArray();
			
			JSONArray detailArr = jsonparam.getJSONArray("order_detail");
			
			//得到单据应付总价
			Double payableValue = jsonparam.getDouble("payable_value");
			
			//判断是分期付款还是一次性付款
			String payType = "";
			JSONArray payArr = jsonparam.getJSONArray("order_payment");
			
			//分期付款（一次性不需要）
			Double firstPay = 0.0;
			Double firstOtherPay = 0.0;
			//其他费用总计
			Double totalOtherCost = 0.0;
			for(int i=0; i< payArr.size(); i++){
				JSONObject paymentJo = payArr.getJSONObject(i);
				payType = paymentJo.getString("paytype");
				
				if("1".equals(payType)){
					firstPay = paymentJo.getDouble("payment");//不含其他费用
					firstOtherPay = paymentJo.getDouble("other_payment");//其他费用
					totalOtherCost = paymentJo.getDouble("other_payment");
					break;
				}
				
				if("2".equals(payType)){
					totalOtherCost += paymentJo.getDouble("other_payment");
					if(paymentJo.getInteger("order_no").equals(1)){
						firstPay = paymentJo.getDouble("payment");//不含其他费用
						firstOtherPay = paymentJo.getDouble("other_payment");//其他费用
					}
				}
				
			}
			
			//计算明细价格
			Double newTotals = 0.0;
			for(int i=0; i < detailArr.size(); i++){
				
				JSONObject jo =  detailArr.getJSONObject(i);
				Double saleQty = jo.getDouble("sale_qty");
				Double price = jo.getDouble("transaction_value") / (payableValue - totalOtherCost) * firstPay / saleQty;
				//四舍五入
				BigDecimal bd = new BigDecimal(price);
				price = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				//统计四舍五入后总价，方便最后计算尾差
				newTotals += price * jo.getDouble("sale_qty");
				
				JSONObject jsonSaleDetail = new JSONObject();
				jsonSaleDetail.put("itemId", jo.getString("item_code"));
				jsonSaleDetail.put("quantity", saleQty);
				jsonSaleDetail.put("price", price);
				jsonSaleDetail.put("commissionId", "100");
				
				arr.add(jsonSaleDetail);
				
			}
			/*
			 * 添加其他费用商品
			 */
			//计算其他费用商品单价
			Double otherPrice = firstOtherPay - (newTotals - firstPay);
			BigDecimal bd = new BigDecimal(otherPrice);
			otherPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			JSONObject otherSale = new JSONObject();
			otherSale.put("itemId", "99999999");
			otherSale.put("quantity", 1);
			otherSale.put("price", otherPrice);
			otherSale.put("commissionId", "100");
			arr.add(otherSale);
			
			
			json.put("saleLines", arr);
			json.put("extraInformation", jdeinform);//JDE数据
			
			op.parseObject(json);
			JSONObject jo = op.parseObject();
			jo.remove("page_no");
			jo.remove("page_size");
			String value = jo.toJSONString();
			
			System.out.println(value);
			MultivaluedMap<String, String> paraMap = new MultivaluedMapImpl();
			
			paraMap.add("JSON", value);
			
			value = URLEncoder.encode(value,"UTF-8");
			
			value= value.replace("\"", "\'");
			String url = ahhURL+METHOD+"?JSON="+value;
			System.out.println(url);
			Client client = null;
			client = RestUtils.getClientPool().borrowObject();
			WebResource webResource = client.resource(url);
			String response=webResource.get(String.class);
			
			if(JSON.parseObject(response).getBoolean("success")){
				JSON.parseObject(response).get("data");
				
				return ServiceResponse.buildSuccess(JSON.parseObject(response).get("data"));
			}else{
				return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,JSON.parseObject(response).getString("err_message"));
			}
		}catch(Exception ex){
			this.getLogger().error(ex.getMessage(),ex);
            return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,ex.getMessage());
		}
	}
	
	//接收安华会集中收银返回的付款信息
	public ServiceResponse receiveAhhOrderPayment(ServiceSession session, JSONObject jsonparam){
		/*
		 *单据编号、付款金额、收款人、收款机号、付款方式（	"1001": "现金"
									"1002": "POS"
									"1003": "商联卡"
									"1004": "微信支付"
									"1007": "银联支付"
									"1009": "美团"
									"1014": "快钱"
									"1015": "支付宝"
									"1016": "汇付天下"
									"1017": "聚划算"
									"1018": "工行直连"
									"1019": "建行直连"
									"1020": "招行直连"
									"1022": "第三方微信支付"
									"1023": "糯米团"
									"1024": "财付通"
									"1025": "货到付款"
									"1027": "虚拟卷"
									"1028": "积分支付"
									"9997": "补现"
									"3": "支票"） 
			格式：
			{
				"sheetno":"15b3ca3f9d80755c",
				"payment":100,
				"payee":"unw",
				"cashmashine_no":"00000001",
				"pay_mode_keyword":"1001",
				"pay_mode_name":"现金"
			}
		 */
		try{
			if (session == null)
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.SESSION_IS_EMPTY);
			if (StringUtils.isEmpty(jsonparam))
				return ServiceResponse.buildFailure(session,
						ResponseCode.Exception.PARAM_IS_EMPTY);
			
			String sheetno = jsonparam.getString("sheetno");
			MongoTemplate template=this.getTemplate();
			
			/*
			 * 记录日志
			 */
			long lroid = UniqueID.getUniqueID();
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("data", jsonparam);
			map.put("timestamp", new Date());
			map.put("user_id", session.getUser_id());
			map.put("ent_id", session.getEnt_id());
			map.put("flag", "N");
			map.put("lroid", lroid);
			template.insert(map, "log_receive_order");
			
			
			Criteria c = Criteria.where("sheetno").is(sheetno);
			Query query = new Query(c);
			OrderBean ob = template.findOne(query, OrderBean.class);
			
			boolean isNeed = false;
			boolean isFind = false;
			Integer orderNo = 1;
			//确认为集中收款
			if(!ob.getPay_over()){
				isNeed = true;
				//查找一个付款类型：一次性还是分期
				List<RowMap> list = template.find(query, RowMap.class, "order_payment");
				for(RowMap rm: list){
					Double totalPay = (Double)rm.get("payment") + (Double)rm.get("other_payment");
					
					if(rm.get("order_no").equals(1) && totalPay.equals(jsonparam.getDouble("payment"))){
						isFind = true; 
					}
					
				}
				
			}
			
			if(isNeed && isFind){
				Update update = new Update();
				//先更新单头
				update.set("pay_over", true);
				template.updateFirst(query, update, "order");
				
				//在更新付款信息
				c.and("order_no").is(orderNo);
				query = new Query(c);
				
				update = new Update();
				
				update.set("payee", jsonparam.getString("payee")); //收款人
				update.set("cashmashine_no", jsonparam.getString("cashmashine_no")); //收款机号
				update.set("paydetail", jsonparam.get("paydetail"));//支付明细
				
				template.upsert(query, update, "order_payment");
				
				//更新日志信息
				update = new Update();
				update.set("flag", "Y");
				query = new Query(Criteria.where("lroid").is(lroid));
				template.updateFirst(query, update, "log_receive_order");
				
				JSONObject jo = new JSONObject();
				jo.put("sheetno", sheetno);
				jo.put("lroid", lroid);
				
				return ServiceResponse.buildSuccess(jo);
			}else{
				
				return ServiceResponse.buildFailure(session, ResponseCode.EXCEPTION,"支付信息与原单支付信息不符！");
				
			}
			
		}catch(Exception ex){
			this.getLogger().error(ex.getMessage(),ex);
            return ServiceResponse.buildFailure(session,ResponseCode.EXCEPTION,ex.getMessage());
		}
		 
	}
	
	/**
	 * 定时任务调用的传递当天订单的方法
	 */
	public void twiceSendAhh() {
		ServiceSession session = null;
		JSONObject jsonparam = new JSONObject();
		try {
			session = new ServiceSession();
			sendTodayOrder(session, jsonparam);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session != null){
				session = null;
			}
		}
		
	}
	/**
	 * 找出当天没有传递成功的订单，向X-Store再传一次
	 * @param session
	 * @param jsonparam
	 * @return
	 * @throws Exception
	 */
	public ServiceResponse sendTodayOrder(ServiceSession session, JSONObject jsonparam) throws Exception{
		if(jsonparam == null){
			jsonparam = new JSONObject();
		}
		jsonparam.put("is_send", "1");
		jsonparam.put("page_size", 9999);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today_time = format.format(new Date());	//当天时间
		/*ServiceResponse response = super.search(session, jsonparam);
		JSONObject data = (JSONObject)response.getData();
		JSONArray orders = data.getJSONArray("order");*/
		StringBuffer total = new StringBuffer();
		List<OrderHeaderBean> orderlist = null;
		List<OrderDetailBean> detiallist = null;
		List<OrderPaymentBean> paylist = null;
		try{
			//查询出未传成功的所有订单
			orderlist = doSearch(jsonparam,OrderHeaderBean.class,null,true,total);
			for(int i=0; i<orderlist.size(); i++){
				OrderHeaderBean order = orderlist.get(i);
				Date order_time = order.getOrder_time();
				String time = format.format(order_time);	//下订单的时间
				//找出当天的订单
				if(today_time.equals(time)){
					JSONObject json = new JSONObject();
					String sheetno = order.getSheetno();
					json.put("sheetno", sheetno);
					//查出订单的支付信息
					paylist = doSearch(json, OrderPaymentBean.class,null,true,total);
					if(paylist != null && paylist.size() > 0){
						order.setOrder_payment(paylist);
					}
					//查出订单的明细
					detiallist = doSearch(json, OrderDetailBean.class,null,true,total);
					if(detiallist != null && detiallist.size() > 0){
						order.setOrder_detail(detiallist);
					}
					//将订单放在一个集合中
					List<OrderHeaderBean> olist = new ArrayList<OrderHeaderBean>();
					olist.add(order);
					
					JSONObject returnparam = new JSONObject();
					returnparam.put("sheetno", sheetno);
					if(olist.size()>0){
						returnparam.put("order",olist);
					}
					//放入从JDE获取的机构信息
					String org_code = order.getOrg_code();
					JSONObject orgjson = new JSONObject();
					orgjson.put("code", org_code);
					JSONObject data = getOrg(session, orgjson);
					JSONArray orgs = data.getJSONArray("organization");
					JSONObject extraInformation  = new JSONObject();
					for(int j=0; j<orgs.size(); j++){
						JSONObject org = orgs.getJSONObject(j);
						String contractNo = org.getString("contractNo");
						String dataType = "1";
						String closeTag = "Y";
						String marketNo = org.getString("marketNo");
						String marketName = org.getString("marketName");
						String shopNo = org.getString("shopNo");
						String classCode = org.getString("classCode");
						String tenantCode = org.getString("tenantCode");
						String tenantName = org.getString("tenantName");
						String unit = org.getString("unit");
						String TenderType = org.getString("cash_type");
						String BizCode = org.getString("industry_code");
						String BizDescription = org.getString("industry");
						//将参数放进extraInformation中
						extraInformation.put("contractNo", contractNo);
						extraInformation.put("marketNo", marketNo);
						extraInformation.put("marKetName", marketName);
						extraInformation.put("shopNo", shopNo);
						extraInformation.put("classCode", classCode);
						extraInformation.put("dataType", dataType);
						extraInformation.put("closeTag", closeTag);
						extraInformation.put("tenantCode", tenantCode);
						extraInformation.put("tenantName", tenantName);
						extraInformation.put("unit", unit);
						extraInformation.put("tenderType", TenderType);
						extraInformation.put("bizCode", BizCode);
						extraInformation.put("bizDescription", BizDescription);
						returnparam.put("extraInformation", extraInformation);
					}
					System.out.println(returnparam);
					//开始传单
					ServiceResponse rep = sendAhhOrder(session, returnparam);
					if("0".equals(rep.getReturncode())){
						order.setIs_send("0");
						Set<String> key = new HashSet<String>();
						key.add("is_send");
						doUpdate(order, key, "oid");
						System.out.println("------------订单传递成功----------");
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
 		
		return ServiceResponse.buildSuccess("success");
	}
	
	/*
	 * 定时调用生成MNT文件
	 */
	public void productmnt(){
		ServiceSession session = null;
		JSONObject jsonparam = new JSONObject();
		try{
			session = new ServiceSession();
			getOrderMnt(session, jsonparam);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(session != null){
				session = null;
			}
		}
	}
	/**
	 * 生成自主收银mnt文件
	 * @param session
	 * @param jsonparam
	 * @return
	 * @throws Exception
	 */
	public ServiceResponse getOrderMnt(ServiceSession session, JSONObject jsonparam) throws Exception {
		if(session == null){
			return ServiceResponse.buildFailure(session, ResponseCode.Exception.SESSION_IS_EMPTY);
		}
		if(jsonparam == null){
			jsonparam = new JSONObject();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat orderformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String today_time = format.format(new Date());
		String cash_type = "1-" + today_time;
		jsonparam.put("cash_type", cash_type);
		StringBuffer total = new StringBuffer();
		List<OrderHeaderBean> orderlist = null;
		List<OrderDetailBean> detiallist = null;
		List<OrderPaymentBean> paylist = null;
		FileWriterWithEncoding fw = null;
		try{
			//查出当天的自主收银的订单
			orderlist = doSearch(jsonparam,OrderHeaderBean.class,null,true,total);
			for(int i=0; i<orderlist.size(); i++){
				OrderHeaderBean orderHeader = orderlist.get(i);
				JSONObject json = new JSONObject();
				String sheetno = orderHeader.getSheetno();
				String bill_type = orderHeader.getBill_type();//获取单据类型，1-销售单，2-退货单
				String org_code = orderHeader.getOrg_code();	//机构编码
				String order_time = orderformat.format(orderHeader.getOrder_time());
				double tatol_value = orderHeader.getTotal_sale_value();//订单总额（实付）=应付总额+四舍五入损益金额
				long orderid = orderHeader.getOid();
				String oid = String.valueOf(orderid);
				oid = oid.substring(6, oid.length());
				json.put("sheetno", sheetno);
				//文件名
				String filename = today_time + sheetno + ".mnt";	//文件名日期加上订单号
				String pathname = "D:\\mnt\\TRANS_MNT\\"+filename;
				//获取订单支付信息
				paylist = doSearch(json, OrderPaymentBean.class,null,true,total);
				for(int b=0; b<paylist.size(); b++){
					OrderPaymentBean payment = paylist.get(b);
					double pay = payment.getPayment();
					double other = payment.getOther_payment();
					tatol_value = tatol_value + pay + other;	//计算付款总额
				}
				
				File file = new File(pathname);
				if(!file.exists()){
					file.createNewFile();
					fw = new FileWriterWithEncoding(pathname, "UTF-8", true);
					if(bill_type.equals("1")){//销售单
						fw.write("INSERT|TRANS_HEADER|102|"+today_time+"|99|"+oid+"|"+order_time+"||RETAIL_SALE|COMPLETE|0|||100|"+tatol_value+"|0|"+tatol_value+"||| \r\n");
						fw.flush();
						fw.close();
					}else{//退货单
						fw.write("INSERT|TRANS_HEADER|102|"+today_time+"|99|"+oid+"|"+order_time+"||RETAIL_SALE|COMPLETE|0|||100|-"+tatol_value+"|0|-"+tatol_value+"||| \r\n");
						fw.flush();
						fw.close();
					}
				}
				//获取订单明细
				int num = 0;
				detiallist = doSearch(json, OrderDetailBean.class,null,true,total);
				for(int j=0; j<detiallist.size(); j++){
					OrderDetailBean detial = detiallist.get(j);
					double sale_price = detial.getSale_price();//单价
					String item_code = detial.getItem_code();//商品code
					double qty = detial.getSale_qty();	//数量
					double sale_value = detial.getSale_value();//售价金额
					double transaction_value = detial.getTransaction_value();//成交金额
					
					fw = new FileWriterWithEncoding(pathname, "UTF-8", true);
					// INSERT|TRANS_LINE_ITEM|门店号|营业日|几号机|销售单号|行号|开始销售时间|结束时间|SALE|0||CNY|商品ID||数量|单价|净额|含税价|退货标记1为退货0为销售
					if(bill_type.equals("1")){//销售单
						fw.write("INSERT|TRANS_LINE_ITEM|102|"+today_time+"|99|"+oid+"|"+(j+1)+"|"+order_time+"|"+order_time+"|SALE|0||CNY|"+item_code+"||"+qty+"|"+sale_price+"|"+transaction_value+"|"+transaction_value+"|0 \r\n");
						fw.flush();
						fw.close();
					}else{//退货单
						fw.write("INSERT|TRANS_LINE_ITEM|102|"+today_time+"|99|"+oid+"|"+(j+1)+"|"+order_time+"|"+order_time+"|SALE|0||CNY|"+item_code+"||"+qty+"|"+sale_price+"|"+transaction_value+"|"+transaction_value+"|1 \r\n");
						fw.flush();
						fw.close();
					}
				
					num = j+1;
				}
				System.out.println("num"+num);
				
				for(int k=0; k<paylist.size(); k++){
					OrderPaymentBean payment = paylist.get(k);
					String paytype = payment.getPaytype();	//支付方式
					String payname = "";
					if(paytype.equals("1")){
						payname = "一次性付款";
					}else{
						payname = "分期付款";
					}
					fw = new FileWriterWithEncoding(pathname, "UTF-8", true);
					if(bill_type.equals("1")){//销售单
						fw.write("INSERT|TRANS_LINE_TENDER|102|"+today_time+"|99|"+oid+"|"+(k+num+1)+"|"+order_time+"|"+order_time+"||0||CNY|CNY_CURRENCY|"+tatol_value+"||Tender|0|0|||| \r\n");
						fw.flush();
						fw.close();
					}else{//退货单
						fw.write("INSERT|TRANS_LINE_TENDER|102|"+today_time+"|99|"+oid+"|"+(k+num+1)+"|"+order_time+"|"+order_time+"||0||CNY|CNY_CURRENCY|-"+tatol_value+"||Refund|0|0|||| \r\n");
						fw.flush();
						fw.close();
					}
				}
				
				//获取对应的机构信息
				JSONObject orgparam = new JSONObject();
				orgparam.put("code", org_code);
				JSONObject data = getOrg(session, orgparam);
				JSONArray orgs = data.getJSONArray("organization");
				for(int a=0; a<orgs.size(); a++){
					JSONObject org = orgs.getJSONObject(a);
					String contractNo = org.getString("contractNo");
					String dataType = "1";
					String closeTag = "Y";
					String marketNo = org.getString("marketNo");
					String marketName = org.getString("marketName");
					String shopNo = org.getString("shopNo");
					String classCode = org.getString("classCode");
					String tenantCode = org.getString("tenantCode");
					String tenantName = org.getString("tenantName");
					String unit = org.getString("unit");
					String TenderType = org.getString("cash_type");
					String BizCode = org.getString("industry_code");
					String BizDescription = org.getString("industry");
					fw = new FileWriterWithEncoding(pathname, "UTF-8", true);
					fw.write("INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|OrderId|STRING|"+sheetno+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|ContractNo|STRING|"+contractNo+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|DataType|STRING|"+dataType+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|MarketName|STRING|"+marketName+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|MarketNo|STRING|"+marketNo+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|ShopNo|STRING|"+shopNo+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|ClassCode|STRING|"+classCode+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|CloseTag|STRING|"+closeTag+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|TenantCode|STRING|"+tenantCode+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|TenantName|STRING|"+tenantName+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|Unit|STRING|"+unit+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|TenderType|STRING|"+TenderType+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|BizCode|STRING|"+BizCode+"|| \r\n"+
								"INSERT|TRNS_PROPERTIES|102|"+today_time+"|99|"+oid+"|BizDescription|STRING|"+BizDescription+"|| \r\n");
					fw.flush();
					fw.close();
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			return ServiceResponse.buildFailure(session, "faile");
		}finally{
			if(fw != null){
				fw.close();
			}
		}
		
		return ServiceResponse.buildSuccess("success");
	}
	
	/**
	 * 将当天向X-Store传单成功，但是没有完成结算的订单的传递状态 "is_send" 改为 "2"
	 */
	public void setSendStatus(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today_time = format.format(new Date());	//当天时间
		StringBuffer total = new StringBuffer();
		List<OrderHeaderBean> orderlist = null;
		try {
			JSONObject json = new JSONObject();
			json.put("pay_over", false);
			json.put("is_send", "0");
			orderlist = doSearch(json,OrderHeaderBean.class,null,true,total);
			for(int i=0; i<orderlist.size(); i++){
				OrderHeaderBean order = orderlist.get(i);
				Date order_time = order.getOrder_time();
				String time = format.format(order_time);	//下订单的时间
				if(today_time.equals(time)){
					order.setIs_send("2");
					Set<String> key = new HashSet<String>();
					key.add("is_send");
					doUpdate(order, key, "oid");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取订单对应的机构信息
	 * @param session
	 * @param jsonparam
	 * @return
	 */
	public JSONObject getOrg(ServiceSession session, JSONObject jsonparam){
		JSONObject result = null;
		try {
			MultivaluedMap<String, String> urlParams = new MultivaluedMapImpl();
			urlParams.add("method", "omd.org.search");
			urlParams.add("session",URLEncoder.encode(JSON.toJSONString(session),"UTF-8"));
			String orgURL = "http://localhost:8080/omd-organization-webin/rest";
			result=OmdRestUtils.doPost(orgURL, urlParams, jsonparam.toJSONString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
