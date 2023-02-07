package org.iteration.matrix.core.config;

import org.iteration.matrix.core.restful.APIVersion;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Method;


@Configuration
public class RestfulAPIVersionConfiguration implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new APIVersionRequestMappingHandlerMapping();
    }

    private static class APIVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

        @Override
        protected boolean isHandler(Class<?> beanType) {
            // return false;//关闭
            return AnnotatedElementUtils.hasAnnotation(beanType, RestController.class);
        }

        @Override
        protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
            Class<?> handlerType = method.getDeclaringClass();
            APIVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, APIVersion.class);
            APIVersion methodVersion = AnnotationUtils.findAnnotation(method, APIVersion.class);
            //以方法上的注解优先
            if (methodVersion != null) {
                apiVersion = methodVersion;
            }

            String[] urlPatterns = apiVersion == null ? new String[0] : apiVersion.value();

                /*PatternsRequestCondition apiPattern = new PatternsRequestCondition(urlPatterns);
                PatternsRequestCondition oldPattern = mapping.getPatternsCondition(); // 2.2.x正常，高版本这里返回null，这是不是BUG
                PatternsRequestCondition updatedFinalPattern = apiPattern.combine(oldPattern);
                mapping = new RequestMappingInfo(mapping.getName(), updatedFinalPattern, mapping.getMethodsCondition(),
                        mapping.getParamsCondition(), mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                        mapping.getProducesCondition(), mapping.getCustomCondition());*/

            // 2.6.x或以上高版本combine
            PathPatternsRequestCondition versionPathPattern = new PathPatternsRequestCondition(PathPatternParser.defaultInstance, urlPatterns);
            PathPatternsRequestCondition combinePathPattern = versionPathPattern.combine(mapping.getPathPatternsCondition());
            String[] combinePath = combinePathPattern.getPatternValues().toArray(new String[0]);
            mapping = mapping.mutate().paths(combinePath).build();

            super.registerHandlerMethod(handler, method, mapping);
        }
    }

}
