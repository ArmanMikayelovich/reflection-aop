package com.mikayelovich.reflecthack.aspect;

import com.mikayelovich.reflecthack.dtUtils.StringEscape;
import com.mikayelovich.reflecthack.reflect.InputParameters;
import com.mikayelovich.reflecthack.reflect.PackageParser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Service
public class UserServiceAspect {

    @Value("${package.to.scan}")
    private String packageToScan;

    private Map<Class<? extends Serializable>, InputParameters> parametersMap;


    @Autowired
    private StringEscape stringEscape;

    @PostConstruct
    public void init() {
        parametersMap = PackageParser.parseCriteriaPackage(packageToScan);
    }

    @Around(value = "execution(* com.mikayelovich.reflecthack.service.*.*(..))")
    public Object beforeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> arg instanceof Serializable)
                .filter(arg -> parametersMap.containsKey(arg.getClass()))
                .forEach(arg -> updateFields((Serializable) arg));
        return joinPoint.proceed(joinPoint.getArgs());
    }

    private void updateFields(Serializable object) {
        InputParameters inputParameters = parametersMap.get(object.getClass());
        if (inputParameters != null) {

            inputParameters.getStringParameters().forEach(invoker -> {
                String value = invoker.invokeGetter(object);
                String escapedString = stringEscape.escapeRestrictedChars(value);
                invoker.invokeSetter(object, escapedString);
            });


            inputParameters.getStringListParameters().forEach(invoker -> {
                List<String> strings = invoker.invokeGetter(object);
                List<String> escapedStringsList = strings.stream()
                        .map(stringEscape::escapeRestrictedChars).collect(Collectors.toList());
                invoker.invokeSetter(object, escapedStringsList);
            });
        }
    }
}
