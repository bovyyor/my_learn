package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.LoggerUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bang on 2018/5/11.
 */
@RestController
@RequestMapping(value = "/index",method = RequestMethod.GET)
public class IndexController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public JSONObject login(HttpServletRequest request, String name)throws Exception{
        JSONObject obj = new JSONObject();
        obj.put("msg","用户:"+name+",登录成功");
        //将返回值写入到请求对象中
        request.setAttribute(LoggerUtils.LOGGER_RETURN,obj);
        return obj;
    }


}

