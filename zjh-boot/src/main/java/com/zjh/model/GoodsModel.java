package com.zjh.model;

import java.util.Date;

public class GoodsModel {
	
	private String item_code;			//商品编码
	private String item_name;			//商品名称
	private Double purchase_cost;		//采购成本价
	private Double advise_price;		//建议售价
	private Integer stock_QTY;			//库存数量
	private Integer state;				//商品状态 1/0 可用/停用
	private Date create_date;			//建档日期
	private Date last_date;				//最后修改日期
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public Double getPurchase_cost() {
		return purchase_cost;
	}
	public void setPurchase_cost(Double purchase_cost) {
		this.purchase_cost = purchase_cost;
	}
	public Double getAdvise_price() {
		return advise_price;
	}
	public void setAdvise_price(Double advise_price) {
		this.advise_price = advise_price;
	}
	public Integer getStock_QTY() {
		return stock_QTY;
	}
	public void setStock_QTY(Integer stock_QTY) {
		this.stock_QTY = stock_QTY;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}
	@Override
	public String toString() {
		return "GoodsModel [item_code=" + item_code + ", item_name=" + item_name + ", purchase_cost=" + purchase_cost
				+ ", advise_price=" + advise_price + ", stock_QTY=" + stock_QTY + ", state=" + state + ", create_date="
				+ create_date + ", last_date=" + last_date + "]";
	}
}
