package com.qing.community.controller;

import com.qing.community.annotation.LoginRequired;
import com.qing.community.entity.User;
import com.qing.community.service.FollowService;
import com.qing.community.service.LikeService;
import com.qing.community.service.UserService;
import com.qing.community.utils.CommunityConstant;
import com.qing.community.utils.HostHolder;
import com.qing.community.utils.RandomStr;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.PushBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(path = "/user")
public class UserController implements CommunityConstant {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    @LoginRequired
    public String getSettingPage() {
        return "/site/setting";
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    @LoginRequired
    public String uploadHeader(MultipartFile headerImage, Model model) {

        //判空处理
        if (headerImage == null) {
            model.addAttribute("headerMsg", "请上传图片！");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf('.'));
        int format = 0;
        if(suffix.equalsIgnoreCase(".PNG") ||
                suffix.equalsIgnoreCase(".JPG") || suffix.equalsIgnoreCase(".JPEG") ){
            format = 1;
        }
        if (StringUtils.isBlank(suffix) || format != 1) {
            model.addAttribute("headerMsg", "文件格式不正确");
            return "/site/setting";
        }

        //生成随机文件名
        filename = RandomStr.generateUUID() + suffix;
        File dest = new File(uploadPath + '/' + filename);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传图片失败！" + e.getMessage());
            throw new RuntimeException("上传图片失败！", e);
        }

        //更新用户数据库中的头像链接
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeaderUrl(user.getId(), headerUrl);

        return "redirect:/index";
    }

    //浏览器获取头像
    @RequestMapping(path = "/header/{filename}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response) {
        //服务器存放路径
        filename = uploadPath + "/" + filename;

        //文件后缀
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        //用浏览器输出流和文件输入流传输图片
        response.setContentType("image/" + suffix);
        try (
                //java7语法，放在此括号中，会自动生成finally并调用关闭方法
                FileInputStream fis = new FileInputStream(filename);
        ) {
            OutputStream os = response.getOutputStream();//springmvc会自动关闭这个流
            //创建缓冲区
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("文件传输失败！", e.getMessage());
        }
    }

    //个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    @LoginRequired
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findByUserById(userId);
        if(user == null) {
            throw new RuntimeException("用户不存在");
        }

        model.addAttribute("user", user);
        //点赞数量
        long likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER );//注意不是登录用户的id，而是主页所属用户的id
        model.addAttribute("followeeCount", followeeCount);

        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);//注意不是登录用户的id，而是主页所属用户的id
        model.addAttribute("followerCount", followerCount);

        //是否关注
        boolean hasFollowed = false;
        //key里面的id是登录用户的，value里的id是当前主页用户的
        hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }
}
