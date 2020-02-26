package com.github.myproject.util;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * 使用Gson把json字符串转成Map
 *
 * @author lianqiang
 * @date 2014/06/12
 */
public class JsonToMap {

    public static Gson defaultGson = new Gson();

    /**
     * 获取JsonObject
     *
     * @param json
     * @return
     */
    public static JsonObject parseJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    /**
     * 根据json字符串返回Map对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        return JsonToMap.toMap(JsonToMap.parseJson(json));
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray) {
                map.put((String) key, toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                map.put((String) key, toMap((JsonObject) value));
            } else {
                map.put((String) key, value);
            }
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * 根据json字符串返回指定属性
     *
     * @param responseStr 传入要解析的json字符串
     * @param field       要获取的字符对应的值
     * @return String
     */
    public static String getField(String responseStr, String field) {
        if (null != responseStr && !"".equals(responseStr)) {
            Map<String, Object> map = JsonToMap.toMap(responseStr);
            if (null != map) {
                Object object = map.get(field);
                if (null != object) {
                    String value = object.toString();
                    if (!"".equals(value) && value.length() > 2) {
                        value = value.substring(1, value.length() - 1);
                        return value;
                    }
                }
            }
        }
        return "";
    }

    /**
     * 根据json字符串返回指定属性
     *
     * @param responseStr 传入要解析的json字符串
     * @param field       要获取的字符对应的值
     * @return String
     */
    public static String getNumber(String responseStr, String field) {
        String value = "";
        if (null != responseStr && !"".equals(responseStr)) {
            Map<String, Object> map = JsonToMap.toMap(responseStr);
            if (null != map) {
                Object object = map.get(field);
                if (null != object) {
                    value = object.toString();
                }
            }
        }
        return value;
    }

    public static String getField(Map<String, Object> map, String field) {
        if (null != map) {
            Object object = map.get(field);
            if (null != object) {
                String value = object.toString();
                if (!"".equals(value) && value.length() > 2) {
                    value = value.substring(1, value.length() - 1);
                    return value;
                }
            }
        }
        return "";
    }

    public static String getNumber(Map<String, Object> map, String field) {
        String value = "";
        if (null != map) {
            Object object = map.get(field);
            if (null != object) {
                value = object.toString();
            }
        }
        return value;
    }

    public static String getStringField(String result, String field1, String field2) {
        String value = "";
        if (StringUtils.isNotEmpty(result)) {
            JsonObject jsonObject = defaultGson.fromJson(result, JsonObject.class);

            value = Optional.ofNullable(jsonObject).map(jsonObject1 -> jsonObject.getAsJsonObject(field1))
                    .map(jsonObject1 -> jsonObject1.get(field2))
                    .map(jsonObject1 -> jsonObject1.getAsString())
                    .orElse("");
        }
        return value;
    }

    public static String getField(String result, String field1, String field2, String field3) {
        String value = "";
        if (StringUtils.isNotEmpty(result)) {
            JsonObject jsonObject = defaultGson.fromJson(result, JsonObject.class);

            value = Optional.ofNullable(jsonObject).map(jsonObject1 -> jsonObject1.getAsJsonObject(field1))
                    .map(jsonObject1 -> jsonObject1.getAsJsonObject(field2))
                    .map(jsonObject1 -> jsonObject1.get(field3))
                    .map(jsonObject1 -> jsonObject1.getAsString())
                    .orElse("");
        }
        return value;
    }
}