package com.mikayelovich.reflecthack.reflect;

import lombok.ToString;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ToString
public class InputParameters {
    Class<? extends Serializable> objectType;

    private List<Invoker<String>> stringParameters;
    private List<Invoker<List<String>>> stringListParameters;

    public List<Invoker<String>> getStringParameters() {
        return Collections.unmodifiableList(stringParameters);
    }

    public List<Invoker<List<String>>> getStringListParameters() {
        return Collections.unmodifiableList(stringListParameters);
    }


    InputParameters(Class<? extends Serializable> objectType) {
        this.objectType = objectType;
        stringParameters = new ArrayList<>();
        stringListParameters = new ArrayList<>();
    }

    public void addStringParameter(String parameterName) {
        stringParameters.add(new Invoker<>(parameterName));
    }

    public void addStringListParameter(String parameterName) {
        stringListParameters.add(new Invoker<>(parameterName));
    }


    /**
     * @param <P> P is parameter type. String, or List, or Collection...
     */
    @ToString
   public class Invoker<P> {

        private final Method getter;
        private final Method setter;

        private String nameForGetter(String fieldName) {
            return "get" + StringUtils.capitalize(fieldName);
        }

        private String nameForSetter(String fieldName) {
            return "set" + StringUtils.capitalize(fieldName);
        }


        Invoker(String fieldName) {
            Method[] declaredMethods = objectType.getDeclaredMethods();
            String getterMethodName = nameForGetter(fieldName);

            Method getterMethod = Arrays.stream(declaredMethods)
                    .filter(method -> method.getName().equals(getterMethodName))
                    .findAny().orElseThrow(() -> new RuntimeException("Getter for " + fieldName +
                            " not found in class " + objectType));

            String setterMethodName = nameForSetter(fieldName);
            Method setterMethod = Arrays.stream(declaredMethods)
                    .filter(method -> method.getName().equals(setterMethodName))
                    .findAny().orElseThrow(() -> new RuntimeException("Setter for " + fieldName +
                            " not found in class " + objectType));

            this.getter = getterMethod;
            this.setter = setterMethod;
        }


        public P invokeGetter(Serializable object) {
            try {
                return (P) getter.invoke(object);
            } catch (IllegalAccessException | InvocationTargetException e) {
                //TODO LOG.error(blablabla);
                throw new RuntimeException("An error occurred when invoking "
                        + getter.getName() + " of " + object.getClass(), e);
            }
        }

        public void invokeSetter(Serializable object, P parameter) {
            try {
                setter.invoke(object, parameter);
            } catch (IllegalAccessException | InvocationTargetException e) {
                //TODO LOG.error(blablabla);
                throw new RuntimeException("An error occurred when invoking "
                        + setter.getName() + " of " + object.getClass(), e);
            }
        }
    }
}
