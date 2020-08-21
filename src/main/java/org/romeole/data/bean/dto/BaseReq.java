package org.romeole.data.bean.dto;

import lombok.Data;
import org.romeole.data.annotation.NotNull;
import org.romeole.data.exception.MissingParamException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gongzhou
 * @title: BaseReq
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2116:36
 */
@Data
public class BaseReq {

    public void nullFieldValidate() throws MissingParamException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = runGetter(field, this);
            boolean isAnnotationNotNull = field.isAnnotationPresent(NotNull.class);
            if (isAnnotationNotNull && fieldValue == null) throw new MissingParamException("请填写参数:" + fieldName);
        }
    }

    public Object runGetter(Field field, Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        return method.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }
                }
            }
        }
        return null;
    }
}
