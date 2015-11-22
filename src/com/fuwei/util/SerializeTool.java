package com.fuwei.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//import java.nio.file.Path;
//import java.nio.file.StandardOpenOption;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @author canghailan 2011-12-10 07:09
 */
public class SerializeTool {

	private static final String DEFAULT_CHARSET_NAME = "UTF-8";

	public static <T> String serialize(T object) {
		return JSON.toJSONString(object);
	}

	public static <T> T deserialize(String string, Class<T> clz) {
		return JSON.parseObject(string, clz);
	}
	
	public static Map<String, Object> deserializeMap(String string) throws Exception {
		try {
			JSONObject jsonObject = JSONObject.fromObject(string);  
			
	        Map<String, Object> map = new HashMap<String,Object>();  
	        Iterator it = jsonObject.keys();   
	        while(it.hasNext()){  
	            String key = String.valueOf(it.next());  
	            Object value = jsonObject.get(key);  
	            map.put(key, value); 
	        }  
	        return map;  
		} catch (Exception e) {
			throw e;
		}
	}

	public static <T> List<T> deserializeList(String string, Class<T> clz) throws Exception {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(string, clz);
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	public static <T> List<Map<String, T>> deserializeListMap(String string,
			Class<T> clz) {
		List<Map<String, T>> list = new ArrayList<Map<String, T>>();
		try {
			list = JSON.parseObject(string,
					new TypeReference<List<Map<String, T>>>() {
					});
		} catch (Exception e) {
		}
		return list;
	}
	// public static <T> T load(Path path, Class<T> clz) throws IOException {
	// return deserialize(
	// new String(Files.readAllBytes(path), DEFAULT_CHARSET_NAME), clz);
	// }
	//
	// public static <T> void save(Path path, T object) throws IOException {
	// if (Files.notExists(path.getParent())) {
	// Files.createDirectories(path.getParent());
	// }
	// Files.write(path,
	// serialize(object).getBytes(DEFAULT_CHARSET_NAME),
	// StandardOpenOption.WRITE,
	// StandardOpenOption.CREATE,
	// StandardOpenOption.TRUNCATE_EXISTING);
	// }
}
