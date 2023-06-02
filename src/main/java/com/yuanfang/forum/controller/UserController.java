package com.yuanfang.forum.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.yuanfang.forum.annotation.LoginRequired;
import com.yuanfang.forum.pojo.User;
import com.yuanfang.forum.service.FollowService;
import com.yuanfang.forum.service.LikeService;
import com.yuanfang.forum.service.UserService;
import com.yuanfang.forum.utils.ForumConstant;
import com.yuanfang.forum.utils.ForumUtil;
import com.yuanfang.forum.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/user")
public class UserController implements ForumConstant {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${forum.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){  //返回账号设置页面
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","您好没有选择图片！");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();  //原始的文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));  //文件的后缀
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确！");
            return "/site/setting";
        }

        //生成随机的文件名
        fileName = ForumUtil.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败！" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务其发生异常！");
        }
        //更新当前用户的头像路径（web访问路径）
        // http://localhost:9090/forum/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){

        // 文件在服务中存放的路径
        fileName = uploadPath + "/" + fileName;
        //文件的后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);  //记得关闭流

            OutputStream os = response.getOutputStream();  //Spring 容器管理不需要手动关闭
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fis.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId")  int userId, Model model){
        User user = userService.getUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在!");
        }
        //用户
        model.addAttribute("user",user);
        //点赞数量
        int likeCount = likeService.getUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        //关注数量
        long followeeCount = followService.getFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //粉丝数量
        long followerCount = followService.getFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        //是否已关注
        boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }




}
