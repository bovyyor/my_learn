package com.example.demo.Interceptors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.LoggerEntity;
import com.example.demo.jpa.LoggerJPA;
import com.example.demo.util.LoggerUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bang on 2018/5/11.
 */
@Component
public class LoggerInterceptor implements HandlerInterceptor {
    //请求开始时间标识
    private static final String LOGGER_SEND_TIME="_send_time";
    //请求日志实体标识
    private static final String LOGGER_ENTITY="_logger_entity";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //创建日志实体
        LoggerEntity logger = new LoggerEntity();
        //获取请求的sessionid;
        String sessionId = request.getRequestedSessionId();
        //请求路径
        String uri = request.getRequestURI();
        //请求参数信息
        String paramData = JSON.toJSONString(request.getParameterMap(),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue);
        //设置客户端ip
        logger.setClientIp(LoggerUtils.getCliectIp(request));
        //设置请求方法
        logger.setMethod(request.getMethod());
        //设置请求类型(json普通请求)
        logger.setType(LoggerUtils.getRequestType(request));
        //设置请求参数内容json字符串
        logger.setParamData(paramData);
        //设置请求地址
        logger.setUri(uri);
        //设置sessionId
        logger.setSessionId(sessionId);
        //设置请求开始时间
        request.setAttribute(LOGGER_SEND_TIME,System.currentTimeMillis());
        //设置请求实体到request内,方面afterCompletion方法调用
        request.setAttribute(LOGGER_ENTITY,logger);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //获取请求错误码
        int status=response.getStatus();
        //当前时间
        long currentTime=System.currentTimeMillis();
        //请求开始时间
        long time=Long.valueOf(request.getAttribute(LOGGER_SEND_TIME).toString());
        //获取本次请求日志实体
        LoggerEntity loggerEntity = (LoggerEntity)request.getAttribute(LOGGER_ENTITY);
        //设置请求时间差
        loggerEntity.setTimeConsuming(Integer.valueOf((currentTime-time)+""));
        //设置返回时间
        loggerEntity.setReturnTime(currentTime+"");
        //设置返回错误码
        loggerEntity.setHttpStatusCode(status+"");
        //设置返回值
        loggerEntity.setReturnData(JSON.toJSONString(request.getAttribute(LoggerUtils.LOGGER_RETURN),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue));
        //执行将日志写入数据库
        LoggerJPA loggeDao = getDAO(LoggerJPA.class, request);
        loggeDao.save(loggerEntity);
    }

    /**
     * 根据传入的类型获取spring管理的对应的dao
     * @param clazz
     * @param request
     * @param <T>
     * @return
     */
    private <T> T getDAO(Class<T> clazz,HttpServletRequest request){
        WebApplicationContext factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);

    }
}
