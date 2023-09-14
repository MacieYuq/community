package com.qing.community.service;

import com.qing.community.dao.UserMapper;
import com.qing.community.entity.User;
import com.qing.community.utils.MailClient;
import com.qing.community.utils.RandomStr;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    //注入域名
    @Value("${community.path.domain}")
    private String domain;
    //注入路径
    @Value("${server.servlet.context-path}")
    private String contextPath;
    public User findByUserById (int id){
        return userMapper.selectById(id);
    }

    //注册功能
    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        //空值处理
        if(user == null){
            throw new IllegalArgumentException("用户参数错误");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        //重复处理
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg","该用户已存在！");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg","该邮箱已存在！");
            return map;
        }

        //注册用户
        user.setSalt(RandomStr.generateUUID().substring(0,5));
        user.setPassword(RandomStr.md5(user.getPassword() + user.getSalt()));
        user.setActivationCode(RandomStr.generateUUID());
        user.setStatus(0);
        user.setType(0);
        user.setCreateTime(new Date());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());//对应activation.html中的动态变量
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + user.getActivationCode();
        context.setVariable("url", url);
        //用模板引擎导入邮件内容
        String content = templateEngine.process("/mail/activation", context);//第一个参数为html文件路径
        //用邮件工具发送
        mailClient.sendMail(user.getEmail(), "激活邮件", content);

        return map;

    }
}
