/**
 * 
 */
package com.yuhen.saleapp.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuhen.saleapp.domain.ClassifyInfo;
import com.yuhen.saleapp.domain.MerchInfo;

/**
 * json工具类
 * 
 * @author ps
 * 
 */
public class JsonUtil {

	/**
	 * 把json字符串转换为一个Map
	 * @param json json字符串
	 * @return
	 */
	public static HashMap<String, String> parse2Map(String json) {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
		HashMap<String, String> map = null;
		try {
			map = mapper.readValue(json, typeRef);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}
	
	/**
	 * 把json字符串转换为一个Map
	 * @param json json字符串
	 * @return
	 */
	public static List<Map<String, String>> parse2ListMap(String json) {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<List<Map<String, String>>> typeRef = new TypeReference<List<Map<String, String>>>() {};
		List<Map<String, String>> list = null;
		try {
			list = mapper.readValue(json, typeRef);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 把json字符串转换为一个List&lt;MerchInfo&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<MerchInfo> parse2ListMerchInfo(String json) {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<List<MerchInfo>> typeRef = new TypeReference<List<MerchInfo>>() {};
		List<MerchInfo> list = null;
		try {
			list = mapper.readValue(json, typeRef);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	} 
	/**
	 * 把json字符串转换为一个List&lt;ClassifyInfo&gt;
	 * @param json json字符串
	 * @return
	 */
	public static List<ClassifyInfo> parse2ListClassifyInfo(String json) {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<List<ClassifyInfo>> typeRef = new TypeReference<List<ClassifyInfo>>() {};
		List<ClassifyInfo> list = null;
		try {
			list = mapper.readValue(json, typeRef);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	} 
}
