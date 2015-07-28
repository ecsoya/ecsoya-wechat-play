package org.ecsoya.wechat.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonUtils {
	private JsonUtils() {
	}

	public static String getStringValue(JsonNode node, String name) {
		if (node == null || name == null) {
			return null;
		}
		JsonNode child = node.get(name);
		if (child != null){
			return child.asText();
		}
		return null;
	}
}
