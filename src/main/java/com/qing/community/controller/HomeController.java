package com.qing.community.controller;

import com.qing.community.dao.DiscussPostMapper;
import com.qing.community.dao.UserMapper;
import com.qing.community.entity.DiscussPost;
import com.qing.community.entity.Page;
import com.qing.community.entity.User;
import com.qing.community.service.DiscussPostService;
import com.qing.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    //返回html，不用加responseBody
    public String getIndexPage(Model model, Page page) {
        //方法调用前，springmvc会自动实例化model和page，并将page注入到model
        //所以thymeleaf可以直接访问page对象的数据
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        //新建map来存放帖子和对应，并将其userId替换为真正的user
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<Map<String, Object>>();
        if(list != null){
            for(DiscussPost post : list){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("post",post);
                User user = userService.findByUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";

    }

}