package com.zjh.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.alibaba.fastjson.JSONObject;
import com.zjh.dao.server.GoodsDao;
import com.zjh.model.GoodsModel;
import com.zjh.model.ServiceSession;

public class GoodsDaoImpl implements GoodsDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int insert(GoodsModel goodsModel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String itemCode) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(GoodsModel goodsModel) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public GoodsModel get(String itemCode) {
		//String itemCode = json.getString("item_code");
		String sql = "select * from goods where item_code = ?";
		
		GoodsModel model =  this.jdbcTemplate.queryForObject(sql, new RowMapper<GoodsModel>() {

			@Override
			public GoodsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				GoodsModel goodsModel = new GoodsModel();
				goodsModel.setItem_code(rs.getString("item_code"));
				goodsModel.setItem_name(rs.getString("item_name"));
				goodsModel.setPurchase_cost(rs.getDouble("purchase_cost"));
				goodsModel.setAdvise_price(rs.getDouble("advise_price"));
				goodsModel.setStock_QTY(rs.getInt("stock_QTY"));
				goodsModel.setState(rs.getInt("state"));
				goodsModel.setCreate_date(rs.getDate("create_date"));
				goodsModel.setLast_date(rs.getDate("last_date"));
				
				return goodsModel;
			}
			
		}, itemCode);
		
		return model;
	}

	public void test() {
		System.out.println("--------test---------");
	}

}
