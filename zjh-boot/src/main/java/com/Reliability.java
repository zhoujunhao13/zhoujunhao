package com;

public class Reliability {
	
	static {
		try {
			System.loadLibrary("Reliability"); // call dll
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
			System.out.println("The load problem");
		}
	}
	
	//后面增加的2个函数
	public native long QGetPartUnit(String privinceName);
	public native long QGetPart0Unit(String privinceName);
	
	//确定金额的最小单元函数, partIndex只能为1，2，3
	public native long QGetUnit(String privinceName, long partIndex);
	
	//确定比率的最小单元
	public native double QGetUbv(double r1, double r2);
	
	//段值数据处理函数
	public native long QGetDzsjMin(String str);
	public native long QGetDzsjMax(String str);
	
	//根据省的名称， 城市的级别，涉案的金额， 返回报价
	public native long QGetPayBySheAn(String privinceName, long cityLevel, long sheAnMoneyAmount);
	
	
	//p1: 获取案件难度unit, nanDuInfo的值: 0, 1, 2, 3, 4
	public native long QGetPartNanduU(String privinceName, long nanDuInfo);
	public native long QGetPart0NanduU(String privinceName, long nanDuInfo);
	public native long QGetNanduU(String privinceName, long nanDuInfo, long partIndex);
	
	//p2: 获取报案unit，baoAnShiZhuInfo的值：1：个人，2：企业
	public native long QGetPartBaoAnShiZhuU(String privinceName, long baoAnShiZhuInfo);
	public native long QGetPart0BaoAnShiZhuU(String privinceName, long baoAnShiZhuInfo);
	public native long QGetBaoAnShiZhuU(String privinceName, long baoAnShiZhuInfo, long partIndex);
	
	//p3: 获取当事人数unit, peopleNum从0至8，大于8后，返回值固定
	public native long QGetPartOccurePeopleU(String privinceName, long peopleNum);
	public native long QGetPart0OccurePeopleU(String privinceName, long peopleNum);
	public native long QGetOccurePeopleU(String privinceName, long peopleNum, long partIndex);
	
	//p4: 获取诉求项数unit， shuQiuXiangShuInfo从0到7，大于7后，返回值固定
	public native long QGetPartShuQiuXiangShuU(String privinceName, long shuQiuXiangShuInfo);
	public native long QGetPart0ShuQiuXiangShuU(String privinceName, long shuQiuXiangShuInfo);
	public native long QGetShuQiuXiangShuU(String privinceName, long shuQiuXiangShuInfo, long partIndex);
	
	//p5: 获取地区
	public native long QGetPartAreaU(String privinceName, long areaInfo);
	public native long QGetPart0AreaU(String privinceName, long areaInfo);
	public native long QGetAreaU(String privinceName, long areaInfo, long partIndex);
	
	//p6: 是否涉外, partIndex值为1，2，3 表示当前案件进行到第几步
	public native long QGetPartSheWaiU(String privinceName, long sheWaiInfo);
	public native long QGetPart0SheWaiU(String privinceName, long sheWaiInfo);
	public native long QGetSheWaiU(String privinceName, long sheWaiInfo, long partIndex);
		
	//p7: 获取取证unit, partIndex值为1，2，3 表示当前案件进行到第几步
	public native long QGetPartQuZhengU(String privinceName, long quZhengInfo);
	public native long QGetPart0QuZhengU(String privinceName, long quZhengInfo);
	public native long QGetQuZhengU(String privinceName, long quZhengInfo, long partIndex);
	
	//p8: 检查院, partIndex值为1，2，3 表示当前案件进行到第几步
	//public native long QGetPartJianChaYuanU(String privinceName, long jianChaYuan);
	//public native long QGetPart0JianChaYuanU(String privinceName, long jianChaYuan);
	public native long QGetJianChaYuanU(String privinceName, long jianChaYuan, long partIndex);
	
	//p9: 审院, partIndex值为1，2，3 表示当前案件进行到第几步
	public native long QGetPartShenYuanU(String privinceName, long shenYuanInfo);
	public native long QGetPart0ShenYuanU(String privinceName, long shenYuanInfo);
	public native long QGetShenYuanU(String privinceName, long shenYuanInfo, long partIndex);
	
	
	
	//根据基本收费金额的三步，返回每步的收费
	public native long QGetPartPay(String privinceName);
	public native long QGetPart0Pay(String privinceName);
	public native long QGetPart1Pay(String privinceName);
	public native long QGetPart2Pay(String privinceName);
	public native long QGetPart3Pay(String privinceName);
		
	
	public static void main(String[] args) {
		Reliability reliability = new Reliability();
		//System.out.print(reliability.shanfei(6));
		
		String showValue = ("　QGetUnit:") + String.valueOf(reliability.QGetUnit("500", 1000));
		System.out.print(showValue);
		
		showValue = ("　QGetUbv:") + String.valueOf(reliability.QGetUbv(0.053, 0.032));
		System.out.print(showValue);
		
		showValue = ("　QGetDzsjMin:") + String.valueOf(reliability.QGetDzsjMin("20-150"));
		System.out.print(showValue);
		showValue = ("　QGetDzsjMax:") + String.valueOf(reliability.QGetDzsjMax("20-150"));
		System.out.print(showValue);
		
		showValue = ("　QGetPayBySheAn:") + String.valueOf(reliability.QGetPayBySheAn("GUANGDONG", 1, 1000));
		System.out.print(showValue);
		
		showValue = ("　QGetNanduU:") + String.valueOf(reliability.QGetNanduU(null,1,0));
		System.out.print(showValue);
		
		showValue = ("　QGetPart3Pay:") + String.valueOf(reliability.QGetPart3Pay("GUANGDONG"));
		System.out.print(showValue);
	}

}
