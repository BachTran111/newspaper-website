package com.example.app.controller;

import com.example.app.dao.ArticleDAO;
import com.example.app.model.Article;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", ""})
public class HomeServlet extends HttpServlet {

    private ArticleDAO articleDAO = new ArticleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy danh sách bài viết từ DB
        List<Article> articles = articleDAO.findAllPublished();

        // 2. Đẩy dữ liệu sang JSP
        request.setAttribute("articles", articles);

        // 3. Hiển thị trang chủ
        request.getRequestDispatcher("/WEB-INF/views/web/home.jsp").forward(request, response);
    }
}
