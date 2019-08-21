package com.hframework.springext.requestmapping;

import com.hframework.smartweb.annotation.SmartApi;
import com.hframework.smartweb.annotation.SmartHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;
import org.springframework.web.servlet.mvc.condition.CompositeRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/16.
 */
public class SmartRequestMappingHandlerMapping extends RequestMappingInfoHandlerMapping
        implements EmbeddedValueResolverAware {
    private Map<String, Set<Integer>> methodHolderParameterCache = new HashMap<>();

    public SmartRequestMappingHandlerMapping() {
    }
    public SmartRequestMappingHandlerMapping(RequestMappingHandlerMapping super1, Object[] interceptors) {
        this.setOrder(Integer.MIN_VALUE);
        this.setInterceptors(interceptors);
        this.setContentNegotiationManager(super1.getContentNegotiationManager());
        this.setCorsConfigurations(super1.getCorsConfigurations());
        this.setUseSuffixPatternMatch(super1.useSuffixPatternMatch());
        this.setUseRegisteredSuffixPatternMatch(super1.useRegisteredSuffixPatternMatch());
        this.setUseTrailingSlashMatch(super1.useTrailingSlashMatch());
        this.setPathMatcher(super1.getPathMatcher());
        this.setUrlPathHelper(super1.getUrlPathHelper());
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return  (AnnotationUtils.findAnnotation(beanType, SmartApi.class) != null);
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = createRequestMappingInfo(method);
        if (info != null) {
            RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }
    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestCondition<?> condition = (element instanceof Class<?> ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));

        boolean containParentPath = false;
        if (element instanceof Method && ((Method) element).getDeclaringClass().isAnnotationPresent(SmartApi.class)) {
            String[] parentPath = ((Method) element).getDeclaringClass().getAnnotation(SmartApi.class).path();
            String[] parentValue = ((Method) element).getDeclaringClass().getAnnotation(SmartApi.class).value();
            if(parentPath.length != 0 && StringUtils.isNotBlank(parentPath[0])){
                containParentPath = true;
            }
            if(parentValue.length != 0 && StringUtils.isNotBlank(parentValue[0])){
                containParentPath = true;
            }

        }
        final SmartApi smartApi = AnnotatedElementUtils.findMergedAnnotation(element, SmartApi.class);
        if(smartApi != null) {
            if(element instanceof Method) {
                Method method = (Method) element;
                String methodString = method.getReturnType().getSimpleName() + " " + method.getDeclaringClass().getName() + "." + method.getName() + "(";
                List<Integer> holderParameterIndex = new ArrayList<>();


                Class<?>[] parameterTypes = method.getParameterTypes();
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (int i = 0; i < parameterTypes.length; i++) {
                    methodString += parameterTypes[i].getSimpleName();
                    if(i != parameterTypes.length -1) {
                        methodString += ",";
                    }
                    for (Annotation annotation : parameterAnnotations[i]) {
                        if(annotation.equals(SmartHolder.class)) {
                            holderParameterIndex.add(i);
                            break;
                        }
                    }
                }
                //jdk1.8 -> jdk1.7
//                Parameter[] parameters = method.getParameters();
//                for (int i = 0; i < parameters.length; i++) {
//                    methodString += parameters[i].getType().getSimpleName();
//                    if(i != parameters.length -1) {
//                        methodString += ",";
//                    }
//                    if(parameters[i].getAnnotation(SmartHolder.class) != null) {
//                        holderParameterIndex.add(i);
//                    }
//                }
                if(methodString.endsWith(",")) methodString = methodString.substring(0, methodString.length()-1);
                methodString +=")";
                methodHolderParameterCache.put(methodString,new HashSet(holderParameterIndex));
            }

            final boolean finalContainParentPath = containParentPath;
            return createRequestMappingInfo(new RequestMapping() {
                @Override
                public String name() {
                    return "";
                }

                @Override
                public String[] value() {
                    return path();
                }

                @Override
                public String[] path() {
                    if(smartApi.path().length == 0) {
                        return smartApi.path();
                    }
                    String originPath = smartApi.path()[0];
                    return new String[]{(finalContainParentPath ? "": "/api") + (originPath.startsWith("/") ? "" : "/") + originPath};
                }

                @Override
                public RequestMethod[] method() {
                    return smartApi.method();
                }

                @Override
                public String[] params() {
                    return smartApi.params();
                }

                @Override
                public String[] headers() {
                    return smartApi.headers();
                }

                @Override
                public String[] consumes() {
                    return smartApi.consumes();
                }

                @Override
                public String[] produces() {
                    return smartApi.produces();
                }
                @Override
                public boolean equals(Object obj) {
                    return this.toString().equals(obj.toString());
                }

                @Override
                public int hashCode() {
                    return toString().hashCode();
                }

                @Override
                public String toString() {
                    return "name = " + this.name()
                            + "; path = " + Arrays.toString(this.path())
                            + "; method = " + Arrays.toString(this.method())
                            + "; params = " + Arrays.toString(this.params())
                            + "; headers = " + Arrays.toString(this.headers())
                            + "; consumes = " + Arrays.toString(this.consumes())
                            + "; produces = " + Arrays.toString(this.produces());
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return SmartApi.class;
                }


            }, condition);
        }

        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        if(requestMapping != null) {
            return createRequestMappingInfo(requestMapping, condition);
        }
        return null;
    }

    public Set<Integer> getMethodHolderParameterCache(String methodString) {
        return methodHolderParameterCache.get(methodString);
    }

    private boolean useSuffixPatternMatch = true;

    private boolean useRegisteredSuffixPatternMatch = false;

    private boolean useTrailingSlashMatch = true;

    private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();

    private StringValueResolver embeddedValueResolver;

    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();


    /**
     * Whether to use suffix pattern match (".*") when matching patterns to
     * requests. If enabled a method mapped to "/users" also matches to "/users.*".
     * <p>The default value is {@code true}.
     * <p>Also see {@link #setUseRegisteredSuffixPatternMatch(boolean)} for
     * more fine-grained control over specific suffixes to allow.
     */
    public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
        this.useSuffixPatternMatch = useSuffixPatternMatch;
    }

    /**
     * Whether suffix pattern matching should work only against path extensions
     * explicitly registered with the {@link ContentNegotiationManager}. This
     * is generally recommended to reduce ambiguity and to avoid issues such as
     * when a "." appears in the path for other reasons.
     * <p>By default this is set to "false".
     */
    public void setUseRegisteredSuffixPatternMatch(boolean useRegisteredSuffixPatternMatch) {
        this.useRegisteredSuffixPatternMatch = useRegisteredSuffixPatternMatch;
        this.useSuffixPatternMatch = (useRegisteredSuffixPatternMatch || this.useSuffixPatternMatch);
    }

    /**
     * Whether to match to URLs irrespective of the presence of a trailing slash.
     * If enabled a method mapped to "/users" also matches to "/users/".
     * <p>The default value is {@code true}.
     */
    public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
        this.useTrailingSlashMatch = useTrailingSlashMatch;
    }

    /**
     * Set the {@link ContentNegotiationManager} to use to determine requested media types.
     * If not set, the default constructor is used.
     */
    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        Assert.notNull(contentNegotiationManager, "ContentNegotiationManager must not be null");
        this.contentNegotiationManager = contentNegotiationManager;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver  = resolver;
    }

    @Override
    public void afterPropertiesSet() {
        this.config = new RequestMappingInfo.BuilderConfiguration();
        this.config.setPathHelper(getUrlPathHelper());
        this.config.setPathMatcher(getPathMatcher());
        this.config.setSuffixPatternMatch(this.useSuffixPatternMatch);
        this.config.setTrailingSlashMatch(this.useTrailingSlashMatch);
        this.config.setRegisteredSuffixPatternMatch(this.useRegisteredSuffixPatternMatch);
        this.config.setContentNegotiationManager(getContentNegotiationManager());

        super.afterPropertiesSet();
    }


    /**
     * Whether to use suffix pattern matching.
     */
    public boolean useSuffixPatternMatch() {
        return this.useSuffixPatternMatch;
    }

    /**
     * Whether to use registered suffixes for pattern matching.
     */
    public boolean useRegisteredSuffixPatternMatch() {
        return this.useRegisteredSuffixPatternMatch;
    }

    /**
     * Whether to match to URLs irrespective of the presence of a trailing slash.
     */
    public boolean useTrailingSlashMatch() {
        return this.useTrailingSlashMatch;
    }

    /**
     * Return the configured {@link ContentNegotiationManager}.
     */
    public ContentNegotiationManager getContentNegotiationManager() {
        return this.contentNegotiationManager;
    }

    /**
     * Return the file extensions to use for suffix pattern matching.
     */
    public List<String> getFileExtensions() {
        return this.config.getFileExtensions();
    }


    /**
     * Provide a custom type-level request condition.
     * The custom {@link RequestCondition} can be of any type so long as the
     * same condition type is returned from all calls to this method in order
     * to ensure custom request conditions can be combined and compared.
     * <p>Consider extending {@link AbstractRequestCondition} for custom
     * condition types and using {@link CompositeRequestCondition} to provide
     * multiple custom conditions.
     * @param handlerType the handler type for which to create the condition
     * @return the condition, or {@code null}
     */
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return null;
    }

    /**
     * Provide a custom method-level request condition.
     * The custom {@link RequestCondition} can be of any type so long as the
     * same condition type is returned from all calls to this method in order
     * to ensure custom request conditions can be combined and compared.
     * <p>Consider extending {@link AbstractRequestCondition} for custom
     * condition types and using {@link CompositeRequestCondition} to provide
     * multiple custom conditions.
     * @param method the handler method for which to create the condition
     * @return the condition, or {@code null}
     */
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return null;
    }

    /**
     * Create a {@link RequestMappingInfo} from the supplied
     * {@link RequestMapping @RequestMapping} annotation, which is either
     * a directly declared annotation, a meta-annotation, or the synthesized
     * result of merging annotation attributes within an annotation hierarchy.
     */
    protected RequestMappingInfo createRequestMappingInfo(
            RequestMapping requestMapping, RequestCondition<?> customCondition) {

        return RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name())
                .customCondition(customCondition)
                .options(this.config)
                .build();
    }

    /**
     * Resolve placeholder values in the given array of patterns.
     * @return a new array with updated patterns
     */
    protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
        if (this.embeddedValueResolver == null) {
            return patterns;
        }
        else {
            String[] resolvedPatterns = new String[patterns.length];
            for (int i = 0; i < patterns.length; i++) {
                resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
            }
            return resolvedPatterns;
        }
    }

    @Override
    protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mappingInfo) {
        HandlerMethod handlerMethod = createHandlerMethod(handler, method);
        CrossOrigin typeAnnotation = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), CrossOrigin.class);
        CrossOrigin methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, CrossOrigin.class);

        if (typeAnnotation == null && methodAnnotation == null) {
            return null;
        }

        CorsConfiguration config = new CorsConfiguration();
        updateCorsConfig(config, typeAnnotation);
        updateCorsConfig(config, methodAnnotation);

        if (CollectionUtils.isEmpty(config.getAllowedOrigins())) {
            config.setAllowedOrigins(Arrays.asList(CrossOrigin.DEFAULT_ORIGINS));
        }
        if (CollectionUtils.isEmpty(config.getAllowedMethods())) {
            for (RequestMethod allowedMethod : mappingInfo.getMethodsCondition().getMethods()) {
                config.addAllowedMethod(allowedMethod.name());
            }
        }
        if (CollectionUtils.isEmpty(config.getAllowedHeaders())) {
            config.setAllowedHeaders(Arrays.asList(CrossOrigin.DEFAULT_ALLOWED_HEADERS));
        }
        if (config.getAllowCredentials() == null) {
            config.setAllowCredentials(CrossOrigin.DEFAULT_ALLOW_CREDENTIALS);
        }
        if (config.getMaxAge() == null) {
            config.setMaxAge(CrossOrigin.DEFAULT_MAX_AGE);
        }
        return config;
    }

    private void updateCorsConfig(CorsConfiguration config, CrossOrigin annotation) {
        if (annotation == null) {
            return;
        }
        for (String origin : annotation.origins()) {
            config.addAllowedOrigin(origin);
        }
        for (RequestMethod method : annotation.methods()) {
            config.addAllowedMethod(method.name());
        }
        for (String header : annotation.allowedHeaders()) {
            config.addAllowedHeader(header);
        }
        for (String header : annotation.exposedHeaders()) {
            config.addExposedHeader(header);
        }

        String allowCredentials = annotation.allowCredentials();
        if ("true".equalsIgnoreCase(allowCredentials)) {
            config.setAllowCredentials(true);
        }
        else if ("false".equalsIgnoreCase(allowCredentials)) {
            config.setAllowCredentials(false);
        }
        else if (!allowCredentials.isEmpty()) {
            throw new IllegalStateException("@CrossOrigin's allowCredentials value must be \"true\", \"false\", "
                    + "or an empty string (\"\"); current value is [" + allowCredentials + "].");
        }

        if (annotation.maxAge() >= 0 && config.getMaxAge() == null) {
            config.setMaxAge(annotation.maxAge());
        }
    }

}
