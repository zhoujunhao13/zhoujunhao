import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class Test {

	public static void main(String[] args) {
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		map.put("f", 4);
		map.put("e", 5);
		map.put("d", 6);
		map.put("g", 7); 
		
		System.out.println("a======="+map);
		System.out.println("-----------------------------------------");
		
		//有序json输出，输出为String类型
		JSONObject json = new JSONObject(16,true);
		json.put("a", 1);
		json.put("b", 2);
		json.put("c", 3);
		json.put("f", 4);
		json.put("e", 5);
		json.put("d", 6);
		json.put("g", 7); 
		System.out.println(json);
		System.out.println("-----------------------------------------");
		//有序输出
		String str = JSON.toJSONStringZ(json, SerializeConfig.getGlobalInstance(), SerializerFeature.QuoteFieldNames);
		System.out.println(str);
		System.out.println("-----------------------------------------");
		JSONObject jo = (JSONObject) JSONObject.parse(str);
		System.out.println(jo);
	}

}
