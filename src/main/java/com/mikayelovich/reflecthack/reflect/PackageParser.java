package com.mikayelovich.reflecthack.reflect;

import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class PackageParser {


    public static Map<Class<? extends Serializable>, InputParameters> parseCriteriaPackage(String packageFullName) {
        Map<Class<? extends Serializable>, InputParameters> parametersMap = new HashMap<>();
        Reflections reflections = new Reflections(packageFullName);

        Set<Class<? extends Serializable>> allClassesExtendsSearchCriteriaImpl
                = reflections.getSubTypesOf(Serializable.class);

        allClassesExtendsSearchCriteriaImpl.forEach(clazz -> {
            parametersMap.put(clazz, new InputParameters(clazz));
            PackageParser.getAllFieldsOfType(clazz, String.class)
                    .forEach(field -> parametersMap.get(clazz).addStringParameter(field.getName()));

            PackageParser.getAllListsOfGenericType(clazz, String.class)
                    .forEach(field -> parametersMap.get(clazz).addStringListParameter(field.getName()));

        });
        return parametersMap;
    }

    private static Set<Field> getAllFieldsOfType(Class<? extends Serializable> criteriaClass, Class<?> type) {
        Field[] declaredFields = criteriaClass.getDeclaredFields();

        return Arrays.stream(declaredFields)
                .filter(field -> field.getType().equals(type))
                .collect(Collectors.toSet());
    }

    private static Set<Field> getAllListsOfGenericType(Class<? extends Serializable> criteriaClass, Class<?> genericParameterType) {
        Field[] declaredFields = criteriaClass.getDeclaredFields();


        return Arrays.stream(declaredFields)
                .filter(field -> List.class.isAssignableFrom(field.getType()))
                .filter(listField -> {
                    ParameterizedType genericType = (ParameterizedType) listField.getGenericType();
                    Class<?> firstArgumentClass = (Class<?>) genericType.getActualTypeArguments()[0];
                    return firstArgumentClass.equals(genericParameterType);
                })
                .collect(Collectors.toSet());
    }

}
