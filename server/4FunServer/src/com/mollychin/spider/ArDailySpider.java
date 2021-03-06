﻿package com.mollychin.spider;

import java.io.IOException;
import java.sql.Connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mollychin.bean.ArDaily;
import com.mollychin.dao.JDBCUtil;

//把爬虫爬出的每日一文数据结合JDBC写进数据库中
public class ArDailySpider {
	public static void main(String[] args) {
		// 作者
		try {
			ArDailySpider arDailySpider = new ArDailySpider();
			String url = "http://meiriyiwen.com/random";
			Document document = Jsoup.connect(url).get();
			Elements author = document.select("p.article_author > span");
			ArDaily articleDaily = new ArDaily();
			// 作者
			articleDaily.setArticleAuthor(author.get(0).text());

			// 标题
			Elements title = document.select("h1");
			articleDaily.setArticleName(title.get(0).text());

			// 文章内容
			Elements article = document.select("div.article_text > p");
			articleDaily.setArticleContent(article.get(0).text());

			// 数据库操作
			Connection conn = JDBCUtil.getConnection();
			JDBCUtil.insertData(conn, arDailySpider.getSql(articleDaily));
			JDBCUtil.closeConnection(conn);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSql(ArDaily article) {
		return "insert into articlefromdaily(articleName,articleAuthor,articleContent) values('"
				+ article.getArticleName() + "','" + article.getArticleAuthor() + "','" + article.getArticleContent()
				+ "');";
	}
}
