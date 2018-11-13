package com.lowyer.model;

import java.util.Locale;

public class ServiceResponse 
{
	String returncode;
	Object data;
	
	/**
	 * @return returncode
	 */
	public String getReturncode()
	{
		return returncode;
	}
	/**
	 * @param returncode 要设置的 returncode
	 */
	public void setReturncode(String returncode)
	{
		this.returncode = returncode;
	}
	
	/**
	 * @return data
	 */
	public Object getData()
	{
		return data;
	}
	
	/**
	 * @param respdata 要设置的 data
	 */
	public void setData(Object data)
	{
		this.data = data;
	}
	
	//////////////////////////////////////////////////////////////
	public static ServiceResponse buildSuccess(Object obj)
    {
        ServiceResponse response = new ServiceResponse();
        response.setReturncode(ResponseCode.SUCCESS);
        response.setData(obj);
        return response;
    }

	public static ServiceResponse buildFailure(ServiceSession session, String returncode)
	{
	    return buildFailure(session,returncode,null);
	}
	
	public static ServiceResponse buildFailure(ServiceSession session, String returncode, String msg)
    {
        ServiceResponse response = new ServiceResponse();
        response.setReturncode(returncode);
        response.setData(msg);
        return response;
    }
}
