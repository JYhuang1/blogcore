package com.henry.blog.service.impl;

import com.henry.blog.controller.AbstractController;
import com.henry.blog.dao.ArticleDao;
import com.henry.blog.model.Article;
import com.henry.blog.service.ArticleService;
import com.henry.blog.util.JacksonJsonUtils;
import com.henry.blog.util.LogPool;
import com.henry.blog.util.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by henry on 2016/1/2.
 */
public class ArticleServiceImpl extends AbstractController implements ArticleService {
    @Autowired
    ArticleDao articleDao;
    @Autowired
    private HttpServletRequest request;
    /**
     * 添加文章
     * @param dataJson
     * @return
     */
    public String addArticle(String dataJson){
        String resp = null;
        LogPool.devlogger.info("=================add Blog start===============");
        try {
            Article article = JacksonJsonUtils.DeserializationObjectIgnoreUnkown(dataJson, Article.class);
            Integer artId = articleDao.getLastedId();
            if(artId == null) {artId = 0;}
            String uuid = UuidGenerator.generateUuid(++artId, "d02", "art", 2);
            article.setUuid(uuid);
            article.setCreateTime(System.currentTimeMillis());
            articleDao.addArticle(article);
            return generateResponseContentByObject(article,"chegg",0);
//            resp = generateResponseContent("", "添加博客成功", 0);
        } catch (Exception e) {
            e.printStackTrace();
            resp = generateResponseContent("", "系统开小差啦,正在修复中...", 1);
        }
        LogPool.devlogger.info("------------------add Blog complete--------------------");
        return resp;
    }

    /**
     * 通过文章总类查找文章
     * @param dataJson
     * @return
     */
    public String selectByCategory(String dataJson){
        String resp = null;
        try{
            LogPool.devlogger.info("===========================select CateGory Start======================");
            Integer category = JacksonJsonUtils.getNodeByName(dataJson,"category").asInt();
            List<Article> articles = articleDao.selectByCategory(category);
            System.out.println(articles);
            resp = generateResponseContentByObject(articles, "查询博客成功", 0);
        }catch (Exception e){
            e.getStackTrace();
            LogPool.devlogger.error(e);
            resp = generateResponseContent("", "系统开小差啦,正在修复中...", 1);
        }
        LogPool.devlogger.info("---------------------------select By category complete-------------------");
        return resp;
    }
}
