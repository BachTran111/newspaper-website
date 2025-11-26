package com.example.app.controller;

import com.example.app.dao.ArticleDAO;
import com.example.app.dao.UserDAO;
import com.example.app.model.Article;
import com.example.app.model.User;
import com.example.app.enums.ArticleStatus;
import com.example.app.enums.UserRole;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin", "/admin/approve", "/admin/reject"})
public class AdminServlet extends HttpServlet {

    private ArticleDAO articleDAO = new ArticleDAO();
    private UserDAO userDAO = new UserDAO(); // Cần thêm hàm count Users trong UserDAO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Kiểm tra quyền Admin (Bảo mật)
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            // Nếu không phải admin -> đá về trang chủ hoặc trang lỗi
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // 2. Lấy số liệu thống kê (Giả định bạn sẽ viết thêm các hàm count trong DAO)
        // int totalArticles = articleDAO.countAll();
        // int totalUsers = userDAO.countAll();
        // request.setAttribute("totalArticles", totalArticles);

        // 3. Lấy danh sách bài viết để Admin duyệt (hoặc xem tất cả)
        List<Article> allArticles = articleDAO.findAllForAdmin();
        request.setAttribute("adminArticles", allArticles);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền Admin lần nữa cho chắc
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) return;

        String path = request.getServletPath();
        int articleId = Integer.parseInt(request.getParameter("id"));

        if ("/admin/approve".equals(path)) {
            // Duyệt bài
            articleDAO.updateStatus(articleId, ArticleStatus.PUBLISHED, null);
        } else if ("/admin/reject".equals(path)) {
            // Từ chối bài (kèm lý do)
            String reason = request.getParameter("reason");
            articleDAO.updateStatus(articleId, ArticleStatus.REJECTED, reason);
        }

        // Xử lý xong thì quay lại trang Admin
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}