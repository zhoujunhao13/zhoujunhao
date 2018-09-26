package com.zjh.dao.server;

import com.zjh.model.GoodsModel;

public interface GoodsDao {
	
	public int insert(GoodsModel goodsModel);
	
	public int delete(String itemCode);
	
	public int update(GoodsModel goodsModel);
	
	public GoodsModel get(String itemCode);

}
