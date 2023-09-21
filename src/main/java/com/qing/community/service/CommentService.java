package com.qing.community.service;

import com.qing.community.dao.CommentMapper;
import com.qing.community.entity.Comment;
import com.qing.community.utils.ActivationStatus;
import com.qing.community.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService  implements ActivationStatus {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;
    public List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    public int findCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));//转义html字符
        comment.setContent(sensitiveFilter.filter(comment.getContent()));//过滤敏感词
        int rows = commentMapper.insertComment(comment);//插入成功返回1

        //更新评论数量
        if(comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = findCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);//更新的是发帖人的数据
        }
        return rows;
    }

}
