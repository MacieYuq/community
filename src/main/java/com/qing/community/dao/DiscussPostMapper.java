package com.qing.community.dao;

import com.qing.community.entity.Comment;
import com.qing.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts (int userId, int offset, int limit);

    //如果只有一个参数，并且该参数为动态参数，即可有可无，此时必须加@Param
    //即mapper文件里的<if>语句
    int selectDiscussPostRows (@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);


}
