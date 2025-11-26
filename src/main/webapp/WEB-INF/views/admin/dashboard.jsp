<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <div class="bg-dark text-white p-3 vh-100" style="width: 250px;">
        <h4>ADMIN PANEL</h4>
        <hr>
        <ul class="nav flex-column">
            <li class="nav-item mb-2">
                <a href="#" class="nav-link text-white active bg-primary"><i class="bi bi-speedometer2"></i> Dashboard</a>
            </li>
            <li class="nav-item mb-2">
                <a href="#" class="nav-link text-white"><i class="bi bi-file-text"></i> Duyệt Bài Viết <span class="badge bg-danger float-end">3</span></a>
            </li>
            <li class="nav-item mb-2">
                <a href="#" class="nav-link text-white"><i class="bi bi-tags"></i> Quản Lý Danh Mục</a>
            </li>
            <li class="nav-item mb-2">
                <a href="#" class="nav-link text-white"><i class="bi bi-people"></i> Quản Lý User</a>
            </li>
            <li class="nav-item mt-5">
                <a href="${pageContext.request.contextPath}/home" class="nav-link text-warning"><i class="bi bi-arrow-left"></i> Về Trang Chủ</a>
            </li>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Tổng Quan Hệ Thống</h2>

        <div class="row">
            <div class="col-md-3">
                <div class="card text-white bg-primary mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Tổng Bài Viết</h5>
                        <h2 class="card-text">120</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-success mb-3">
                    <div class="card-body">
                        <h5 class="card-title">User Đang Hoạt Động</h5>
                        <h2 class="card-text">45</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-warning mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Bài Chờ Duyệt</h5>
                        <h2 class="card-text">3</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-danger mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Yêu Cầu Gỡ Bài</h5>
                        <h2 class="card-text">1</h2>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bảng bài viết mới chờ duyệt (Demo) -->
        <div class="card mt-4 shadow-sm">
            <div class="card-header bg-white fw-bold">Yêu cầu duyệt bài mới nhất</div>
            <div class="card-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Tiêu đề</th>
                        <th>Tác giả</th>
                        <th>Ngày gửi</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Review iPhone 16 mới ra mắt</td>
                        <td>NguyenVanA</td>
                        <td>26/11/2025</td>
                        <td>
                            <button class="btn btn-sm btn-success">Duyệt</button>
                            <button class="btn btn-sm btn-danger">Từ chối</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>