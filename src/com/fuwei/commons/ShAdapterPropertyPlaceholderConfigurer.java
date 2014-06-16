/**
 * 
 */
package com.fuwei.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 
 * properties 配置文件读取
 * @author zxd
 *
 */
public class ShAdapterPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	private static  Map<String, Object> properties;
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		properties = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			properties.put(keyStr, value);
		}
	}

	public static Object getContextProperty(String name) {
		return properties.get(name);
	}
}
