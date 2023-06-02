package com.yuanfang.forum.controller;

import com.yuanfang.forum.pojo.Comment;
import com.yuanfang.forum.pojo.DiscussPost;
import com.yuanfang.forum.pojo.Page;
import com.yuanfang.forum.pojo.User;
import com.yuanfang.forum.service.CommentService;
import com.yuanfang.forum.service.DiscussPostService;
import com.yuanfang.forum.service.LikeService;
import com.yuanfang.forum.service.UserService;
import com.yuanfang.forum.utils.ForumConstant;
import com.yuanfang.forum.utils.ForumUtil;
import com.yuanfang.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements ForumConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;   //只有登录后才能进行帖子的发布，所有吧hostholder注入进来

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){   //页面只提交标题和正文内容到服务器

        User user = hostHolder.getUser();
        if(user == null){
            return ForumUtil.getJSONString(403,"您还没有登录哦！");  //403表示没有权限
        }

        //没有设置的属性默认为0
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        //报错的情况将来统一处理
        return ForumUtil.getJSONString(0,"发布成功！");
    }



//    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
//    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model){
//
//        //帖子
//        DiscussPost post = discussPostService.getDiscussPostById(discussPostId);
//        model.addAttribute("post",post);
//        //根据作者id查询到作者的名字
//        User user = userService.getUserById(post.getUserId());
//        model.addAttribute("user",user);
//
//        return "/site/discuss-detail";
//
//    }

    //帖子详情页相较于上面需要多添加评论相关的数据业务

    /**
     * 处理帖子详情页，包括了评论的分页显示，以及评论的评论的显示
     * @param discussPostId
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){

        //帖子
        DiscussPost post = discussPostService.getDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        //根据作者id查询到作者的名字
        User user = userService.getUserById(post.getUserId());
        model.addAttribute("user",user);
        //点赞数量
        long likeCount = likeService.getEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount",likeCount);
        //点赞的状态
        int likeStatus = hostHolder.getUser() == null ? 0 :   //检查用户的登录状态
                likeService.getEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus",likeStatus);

        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        //评论：给帖子的评论
        //回复：给评论的评论
        //评论列表
        List<Comment> commentList =   //查询到当前页的所有评论，每条评论都是需要显示用户的，所以还需要查询每条评论的用户
                commentService.getCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        //评论的map列表(包含评论及评论的用户信息)
        List<Map<String, Object>> commentMapList = new ArrayList<>();
        if(commentList != null){
            for(Comment comment : commentList){
                //评论的map
                Map<String, Object> commentMap = new HashMap<>();
                //评论
                commentMap.put("comment",comment);
                //评论的作者
                commentMap.put("user",userService.getUserById(comment.getUserId()));
                //点赞数量
                likeCount = likeService.getEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("likeCount",likeCount);
                //点赞的状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.getEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentMap.put("likeStatus",likeStatus);

                //针对每条评论查询回复列表
                List<Comment> replyList =
                        commentService.getCommentsByEntity(ENTITY_TYPE_COMMENT,comment.getId(),0,Integer.MAX_VALUE);
                //回复也有用户的信息，也就是说哪个用户评论的,同理将其也装入map的集合中
                List<Map<String, Object>> replyMapList = new ArrayList<>();
                if(replyList != null){
                    for(Comment reply : replyList){
                        Map<String, Object> replyMap = new HashMap<>();
                        //回复
                        replyMap.put("reply",reply);
                        //作者
                        replyMap.put("user",userService.getUserById(reply.getUserId()));
                        //回复的目标（谁回复谁）
                        User target = reply.getTargetId() == 0 ? null : userService.getUserById(reply.getTargetId());
                        //点赞的数量
                        likeCount = likeService.getEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyMap.put("likeCount",likeCount);

                        //点赞的状态
                        likeStatus = likeService.getEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyMap.put("likeStatus",likeStatus);

                        replyMap.put("target",target);
                        replyMapList.add(replyMap);
                    }
                }
                commentMap.put("replys",replyMapList);
                //回复的数量
                int replyCount = commentService.getCommentCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("replyCount", replyCount);
                commentMapList.add(commentMap);

            }
        }

        model.addAttribute("comments",commentMapList);

        return "/site/discuss-detail";

    }

}
