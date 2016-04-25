package me.zjh.zframe.net.vo;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于http请求的参数生成通信类
 * <p>
 * Created by zjh on 2015/11/27.
 */
public class BaseCommunication {

    public Map<String, String> exportAsDictionary() {
        Map<String, String> params = new HashMap<String, String>();

        return params;
    }

    public String getJsonParams() {
        String jsonStr = JSON.toJSONString(getMapParams());
        if (TextUtils.isEmpty(jsonStr)) {
            jsonStr = "";
        }

        return jsonStr;
    }

    public Map<String, Object> getMapParams() {
        Class<? extends BaseCommunication> clazz = this.getClass();
        Class<? extends Object> superclass = clazz.getSuperclass();

        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = superclass.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            for (Field field : fields) {
                // 在使用反射过程中，调用Field.setAccessible(true)可以关闭访问控制检查，可以使反射速度变快。
                field.setAccessible(true);
                // 排除请求中包含的header头信息和请求相对路径信息
                String filedName = field.getName();
                if (!"header".equals(filedName) && !"path".equals(filedName)) {
                    params.put(filedName, field.get(this));
                }
            }

            for (Field superField : superFields) {
                // 在使用反射过程中，调用Field.setAccessible(true)可以关闭访问控制检查，可以使反射速度变快。
                superField.setAccessible(true);
                // 排除请求中包含的header头信息和请求相对路径信息
                String filedName = superField.getName();
                if (!"header".equals(filedName) && !"path".equals(filedName)) {
                    params.put(filedName, superField.get(this));
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return params;
    }
}
