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
            <a href="${pageContext.request.contextPath}/write-article" class="btn btn-primary"><i class="bi bi-plus-lg"></i> Viết bài mới</a>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th style="width: 5%">#</th>
                    <th style="width: 40%">Tiêu đề</th>
                    <th style="width: 15%">Chuyên mục</th>
                    <th style="width: 15%">Trạng thái</th>
                    <th style="width: 25%">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <%-- Ví dụ dữ liệu tĩnh, sau này dùng c:forEach để loop dữ liệu thật --%>
                <tr>
                    <td>1</td>
                    <td>Cách học Java Web hiệu quả cho người mới</td>
                    <td>Giáo dục</td>
                    <td><span class="badge bg-secondary">DRAFT</span></td>
                    <td>
                        <a href="#" class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i> Sửa</a>
                        <a href="#" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i> Xóa</a>
                    </td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>Tương lai của AI trong năm 2025</td>
                    <td>Công nghệ</td>
                    <td><span class="badge bg-success">PUBLISHED</span></td>
                    <td>
                        <%-- Bài đã đăng chỉ được yêu cầu gỡ --%>
                        <button class="btn btn-sm btn-secondary" onclick="alert('Đã gửi yêu cầu gỡ bài!')">
                            <i class="bi bi-flag"></i> Yêu cầu gỡ
                        </button>
                        <a href="#" class="btn btn-sm btn-info text-white"><i class="bi bi-eye"></i> Xem</a>
                    </td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>Bài viết vi phạm nội dung</td>
                    <td>Xã hội</td>
                    <td>
                        <span class="badge bg-danger">REJECTED</span>
                        <div class="small text-danger mt-1">Lý do: Nội dung quá ngắn</div>
                    </td>
                    <td>
                        <a href="#" class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i> Sửa lại</a>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>