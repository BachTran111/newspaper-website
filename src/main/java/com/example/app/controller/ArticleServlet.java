package com.example.app.controller;

import com.example.app.dao.ArticleDAO;
import com.example.app.dao.CategoryDAO;
import com.example.app.model.Article;
import com.example.app.model.Category;
import com.example.app.model.User;
import com.example.app.enums.ArticleStatus;
import com.example.app.utils.Upload;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ArticleServlet", urlPatterns = {"/write-article", "/save-article", "/my-articles", "/delete-article"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ArticleServlet extends HttpServlet {

    private ArticleDAO articleDAO = new ArticleDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // Bắt buộc phải đăng nhập mới được vào các trang này
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/write-article":
                // Lấy danh sách category để đổ vào thẻ <select>
                List<Category> categories = categoryDAO.findAll();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/views/user/write-article.jsp").forward(request, response);
                break;

            case "/my-articles":
                // Lấy danh sách bài viết của user hiện tại
                List<Article> myArticles = articleDAO.findByAuthor(currentUser.getId());
                request.setAttribute("myArticles", myArticles);
                request.getRequestDispatcher("/WEB-INF/views/user/my-articles.jsp").forward(request, response);
                break;

            case "/delete-article":
                // Xử lý xóa bài
                int id = Integer.parseInt(request.getParameter("id"));
                // TODO: Nên kiểm tra xem bài này có đúng của User đang login không (bảo mật)
                articleDAO.delete(id);
                response.sendRedirect("my-articles");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");

        if ("/save-article".equals(path)) {
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser == null) return;

            String title = request.getParameter("title");
            String shortDesc = request.getParameter("shortDescription");
            String content = request.getParameter("content");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String action = request.getParameter("action"); // "save_draft" hoặc "submit_pending"

            // Xử lý Upload ảnh
            Part filePart = request.getPart("thumbnail");
            String thumbnailPath = null;
            if (filePart != null && filePart.getSize() > 0) {
                String realPath = request.getServletContext().getRealPath("");
                thumbnailPath = Upload.saveFile(filePart, realPath);
            }

            Article article = new Article();
            article.setTitle(title);
            article.setShortDescription(shortDesc);
            article.setContent(content);
            article.setThumbnail(thumbnailPath);
            article.setCategoryId(categoryId);
            article.setUserId(currentUser.getId());

            // Logic trạng thái
            if ("submit_pending".equals(action)) {
                article.setStatus(ArticleStatus.PENDING);
            } else {
                article.setStatus(ArticleStatus.DRAFT);
            }

            // Gọi DAO lưu
            if (articleDAO.save(article)) {
                response.sendRedirect("my-articles?msg=success");
            } else {
                request.setAttribute("error", "Lỗi khi lưu bài viết");
                request.getRequestDispatcher("/WEB-INF/views/user/write-article.jsp").forward(request, response);
            }
        }
    }
}
