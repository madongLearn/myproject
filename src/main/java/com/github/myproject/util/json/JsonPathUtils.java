package com.github.myproject.util.json;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author ：madong
 * @date ：Created in 19-9-4 下午5:45
 */
@Slf4j
public class JsonPathUtils {

    //https://blog.csdn.net/lwg_1540652358/article/details/84111339
    //String json = "{\"objs\" : [{\"obj\" : 1411455611975}]}";
    //Object read = JsonPath.read(json, "$.objs[0].obj");
    public static String parse(String path, String data, String defaultValue) {
        try {
            if (StringUtils.isEmpty(path)) {
                return defaultValue;
            }
            Object value = JsonPath.read(data, path);
            if(value instanceof String){
                return (String)value;
            }else {
                return JSON.toJSONString(value);
            }
        } catch (Exception ex) {
            log.info("json path parse is error,path is{},data is {}", path, JSON.toJSONString(data), ex);
        }
        return defaultValue;
    }

    /*public static void main(String[] args) {
        String str = "{\"objs\" : [{\"obj\" : 1411455611975}]}";
        System.out.println(parse("$", str, null));
    }*/

}
