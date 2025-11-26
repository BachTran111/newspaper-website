package com.example.app.controller;

import com.example.app.dao.UserDAO;
import com.example.app.model.User;
import com.example.app.enums.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Servlet này xử lý 3 việc: Login, Register, Logout
@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/login":
                request.getRequestDispatcher("/WEB-INF/views/web/login.jsp").forward(request, response);
                break;
            case "/register":
                request.getRequestDispatcher("/WEB-INF/views/web/register.jsp").forward(request, response);
                break;
            case "/logout":
                HttpSession session = request.getSession();
                session.invalidate(); // Xóa sạch session (đăng xuất)
                response.sendRedirect(request.getContextPath() + "/login"); // Quay về trang login
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(request, response);
        } else if ("/register".equals(path)) {
            handleRegister(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String u = request.getParameter("username");
        String p = request.getParameter("password");

        User user = userDAO.checkLogin(u, p);

        if (user != null) {
            // Đăng nhập thành công -> Lưu vào Session
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);

            // Cập nhật lần đăng nhập cuối
            userDAO.updateLastLogin(user.getId());

            // Điều hướng dựa trên Role
            if (user.getRole() == UserRole.ADMIN) {
                // Tạm thời về trang chủ, sau này sẽ về /admin/dashboard
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } else {
            // Đăng nhập thất bại
            request.setAttribute("errorMessage", "Sai tài khoản hoặc mật khẩu (hoặc tài khoản bị khóa)!");
            request.getRequestDispatcher("/WEB-INF/views/web/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String u = request.getParameter("username");
        String p = request.getParameter("password");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");

        // Validate cơ bản
        if (userDAO.isUsernameExists(u)) {
            request.setAttribute("errorMessage", "Username này đã có người dùng!");
            request.getRequestDispatcher("/WEB-INF/views/web/register.jsp").forward(request, response);
            return;
        }

        User newUser = new User();
        newUser.setUsername(u);
        newUser.setPassword(p);
        newUser.setEmail(email);
        newUser.setFullName(fullName);

        if (userDAO.register(newUser)) {
            // Đăng ký thành công -> Chuyển sang trang login và báo
            request.setAttribute("successMessage", "Đăng ký thành công! Hãy đăng nhập.");
            request.getRequestDispatcher("/WEB-INF/views/web/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại!");
            request.getRequestDispatcher("/WEB-INF/views/web/register.jsp").forward(request, response);
        }
    }
}
