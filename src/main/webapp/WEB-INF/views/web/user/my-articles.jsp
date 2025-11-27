<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bài Viết Của Tôi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-journal-text"></i> Quản Lý Bài Viết</h2>
        <div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary me-2">Về trang chủ</a>
            <a href="${pageContext.request.contextPath}/write-article" class="btn btn-primary">
                <i class="bi bi-plus-lg"></i> Viết bài mới
            </a>
        </div>
    </div>

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success">${param.msg}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">${param.error}</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Tiêu đề</th>
                    <th>Chuyên mục</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>

                <tbody>

                <!-- ⚠️ Nếu không có bài viết -->
                <c:if test="${empty myArticles}">
                    <tr>
                        <td colspan="5" class="text-center text-muted">
                            <i>Chưa có bài viết nào.</i>
                        </td>
                    </tr>
                </c:if>

                <!-- Render danh sách bài viết -->
                <c:forEach var="a" items="${myArticles}" varStatus="loop">

                    <tr>
                        <td>${loop.index + 1}</td>

                        <!-- Tiêu đề -->
                        <td>${a.title}</td>

                        <!-- Chuyên mục -->
                        <td>${a.categoryName}</td>

                        <!-- Trạng thái -->
                        <td>
                            <c:choose>
                                <c:when test="${a.status == 'DRAFT'}">
                                    <span class="badge bg-secondary">DRAFT</span>
                                </c:when>

                                <c:when test="${a.status == 'PENDING'}">
                                    <span class="badge bg-warning text-dark">PENDING</span>
                                </c:when>

                                <c:when test="${a.status == 'PUBLISHED'}">
                                    <span class="badge bg-success">PUBLISHED</span>
                                </c:when>

                                <c:when test="${a.status == 'REJECTED'}">
                                    <span class="badge bg-danger">REJECTED</span>
                                    <div class="small text-danger mt-1">
                                        Lý do: ${a.adminMessage}
                                    </div>
                                </c:when>

                                <c:when test="${a.status == 'REMOVE_PENDING'}">
                                    <span class="badge bg-dark">REMOVE REQUESTED</span>
                                    <div class="small text-muted mt-1">
                                        Đang chờ Admin xử lý
                                    </div>
                                </c:when>
                            </c:choose>
                        </td>

                        <!-- Các thao tác -->
                        <td>
                            <!-- Nếu có quyền sửa -->
                            <c:if test="${a.status == 'DRAFT' || a.status == 'PENDING' || a.status == 'REJECTED'}">
                                <a href="${pageContext.request.contextPath}/edit-article?id=${a.id}"
                                   class="btn btn-sm btn-warning">
                                    <i class="bi bi-pencil"></i> Sửa
                                </a>

                                <a href="${pageContext.request.contextPath}/delete-article?id=${a.id}"
                                   onclick="return confirm('Bạn chắc chắn muốn xóa bài viết này?')"
                                   class="btn btn-sm btn-danger">
                                    <i class="bi bi-trash"></i> Xóa
                                </a>
                            </c:if>

                            <!-- Nếu PUBLISHED thì là yêu cầu gỡ -->
                            <c:if test="${a.status == 'PUBLISHED'}">
                                <a href="${pageContext.request.contextPath}/request-remove?id=${a.id}"
                                   class="btn btn-sm btn-secondary">
                                    <i class="bi bi-flag"></i> Yêu cầu gỡ
                                </a>
                            </c:if>

                            <!-- Nút xem bài -->
                            <a href="${pageContext.request.contextPath}/article-detail?id=${a.id}"
                               class="btn btn-sm btn-info text-white">
                                <i class="bi bi-eye"></i> Xem
                            </a>
                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>
        </div>
    </div>
</div>

</body>
</html>
