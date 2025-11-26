package com.example.app.dao;

import com.example.app.model.Article;
import com.example.app.enums.ArticleStatus;
import com.example.app.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    // Lấy danh sách bài viết ĐÃ ĐĂNG (PUBLISHED) cho trang chủ
    // Sắp xếp bài mới nhất lên đầu
    public List<Article> findAllPublished() {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name, c.name as category_name " +
                "FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN categories c ON a.category_id = c.id " +
                "WHERE a.status = 'PUBLISHED' " +
                "ORDER BY a.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setShortDescription(rs.getString("short_description"));
                a.setThumbnail(rs.getString("thumbnail"));
                a.setViews(rs.getInt("views"));
                a.setCreatedAt(rs.getTimestamp("created_at"));

                // Các trường bổ sung (Join từ bảng khác)
                a.setAuthorName(rs.getString("full_name"));
                a.setCategoryName(rs.getString("category_name"));

                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách bài viết của một tác giả (theo user_id)
    // Bao gồm tất cả trạng thái: DRAFT, PENDING, PUBLISHED, REJECTED
    public List<Article> findByAuthor(int userId) {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name, c.name as category_name " +
                "FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN categories c ON a.category_id = c.id " +
                "WHERE a.user_id = ? " +
                "ORDER BY a.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setShortDescription(rs.getString("short_description"));
                a.setContent(rs.getString("content"));
                a.setThumbnail(rs.getString("thumbnail"));
                a.setViews(rs.getInt("views"));
                a.setCategoryId(rs.getInt("category_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Chuyển đổi string sang enum
                String statusStr = rs.getString("status");
                a.setStatus(ArticleStatus.valueOf(statusStr));

                // Các trường bổ sung
                a.setAuthorName(rs.getString("full_name"));
                a.setCategoryName(rs.getString("category_name"));

                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lưu bài viết mới vào database
    // Trả về true nếu thành công, false nếu thất bại
    public boolean save(Article article) {
        String sql = "INSERT INTO articles (title, short_description, content, thumbnail, " +
                "category_id, user_id, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp now = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, article.getTitle());
            stmt.setString(2, article.getShortDescription());
            stmt.setString(3, article.getContent());
            stmt.setString(4, article.getThumbnail());
            stmt.setInt(5, article.getCategoryId());
            stmt.setInt(6, article.getUserId());
            stmt.setString(7, article.getStatus().name()); // Convert enum to String
            stmt.setTimestamp(8, now);
            stmt.setTimestamp(9, now);

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật bài viết đã có
    public boolean update(Article article) {
        String sql = "UPDATE articles SET title = ?, short_description = ?, content = ?, " +
                "thumbnail = ?, category_id = ?, status = ?, updated_at = ? " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp now = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, article.getTitle());
            stmt.setString(2, article.getShortDescription());
            stmt.setString(3, article.getContent());
            stmt.setString(4, article.getThumbnail());
            stmt.setInt(5, article.getCategoryId());
            stmt.setString(6, article.getStatus().name());
            stmt.setTimestamp(7, now);
            stmt.setInt(8, article.getId());

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa bài viết theo ID
    public boolean delete(int articleId) {
        String sql = "DELETE FROM articles WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, articleId);
            int result = stmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy chi tiết một bài viết theo ID (để xem chi tiết hoặc chỉnh sửa)
    public Article findById(int id) {
        String sql = "SELECT a.*, u.full_name, c.name as category_name " +
                "FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN categories c ON a.category_id = c.id " +
                "WHERE a.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setShortDescription(rs.getString("short_description"));
                a.setContent(rs.getString("content"));
                a.setThumbnail(rs.getString("thumbnail"));
                a.setViews(rs.getInt("views"));
                a.setCategoryId(rs.getInt("category_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));

                String statusStr = rs.getString("status");
                a.setStatus(ArticleStatus.valueOf(statusStr));

                a.setAuthorName(rs.getString("full_name"));
                a.setCategoryName(rs.getString("category_name"));

                return a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Kiểm tra xem bài viết có thuộc về user này không (dùng cho bảo mật)
    public boolean isOwner(int articleId, int userId) {
        String sql = "SELECT COUNT(*) as cnt FROM articles WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, articleId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tăng lượt xem bài viết
    public void incrementViews(int articleId) {
        String sql = "UPDATE articles SET views = views + 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, articleId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả bài viết cho Admin quản lý
    // Bao gồm tất cả trạng thái, sắp xếp theo thời gian tạo mới nhất
    public List<Article> findAllForAdmin() {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name, c.name as category_name " +
                "FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN categories c ON a.category_id = c.id " +
                "ORDER BY a.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setShortDescription(rs.getString("short_description"));
                a.setContent(rs.getString("content"));
                a.setThumbnail(rs.getString("thumbnail"));
                a.setViews(rs.getInt("views"));
                a.setCategoryId(rs.getInt("category_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Chuyển đổi string sang enum
                String statusStr = rs.getString("status");
                a.setStatus(ArticleStatus.valueOf(statusStr));

                // Các trường bổ sung
                a.setAuthorName(rs.getString("full_name"));
                a.setCategoryName(rs.getString("category_name"));

                // Lý do từ chối (nếu có)
                a.setAdminMessage(rs.getString("admin_message"));
                a.setUserMessage(rs.getString("user_message"));

                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách bài viết đang chờ duyệt (PENDING) - dùng riêng cho Admin
    public List<Article> findPendingArticles() {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name, c.name as category_name " +
                "FROM articles a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN categories c ON a.category_id = c.id " +
                "WHERE a.status = 'PENDING' " +
                "ORDER BY a.created_at ASC"; // Bài cũ nhất lên đầu (xử lý theo thứ tự)

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setShortDescription(rs.getString("short_description"));
                a.setContent(rs.getString("content"));
                a.setThumbnail(rs.getString("thumbnail"));
                a.setViews(rs.getInt("views"));
                a.setCategoryId(rs.getInt("category_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));

                String statusStr = rs.getString("status");
                a.setStatus(ArticleStatus.valueOf(statusStr));

                a.setAuthorName(rs.getString("full_name"));
                a.setCategoryName(rs.getString("category_name"));

                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái bài viết (Admin duyệt hoặc từ chối)
    public boolean updateStatus(int articleId, ArticleStatus status, String rejectionReason) {
        String sql = "UPDATE articles SET status = ?, admin_message = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp now = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, status.name());
            stmt.setString(2, rejectionReason); // Lưu vào admin_message (có thể null nếu approve)
            stmt.setTimestamp(3, now);
            stmt.setInt(4, articleId);

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Đếm tổng số bài viết (dùng cho thống kê Admin)
    public int countAll() {
        String sql = "SELECT COUNT(*) as total FROM articles";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Đếm số bài viết theo trạng thái
    public int countByStatus(ArticleStatus status) {
        String sql = "SELECT COUNT(*) as total FROM articles WHERE status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Đếm số bài viết của một tác giả
    public int countByAuthor(int userId) {
        String sql = "SELECT COUNT(*) as total FROM articles WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}