<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Yêu cầu gỡ bài</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">

<div class="container mt-4">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-flag"></i> Yêu cầu gỡ bài viết</h2>
        <div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary me-2">Về trang chủ</a>
            <a href="${pageContext.request.contextPath}/my-articles" class="btn btn-outline-primary">
                <i class="bi bi-journal-text"></i> Bài viết của tôi
            </a>
        </div>
    </div>

    <c:if test="${article == null}">
        <div class="alert alert-danger">Bài viết không tồn tại hoặc bạn không có quyền với bài viết này.</div>
        <a href="${pageContext.request.contextPath}/my-articles" class="btn btn-primary">Quay lại</a>
    </c:if>

    <c:if test="${article != null}">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <h4 class="card-title">${article.title}</h4>
                <p class="card-subtitle text-muted mb-2">
                    Chuyên mục: ${article.categoryName} |
                    Lượt xem: ${article.views}
                </p>
                <p class="card-text">${article.shortDescription}</p>
            </div>
        </div>

        <div class="card shadow-sm">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/request-remove" method="post">
                    <input type="hidden" name="id" value="${article.id}"/>

                    <div class="mb-3">
                        <label class="form-label">Lý do yêu cầu gỡ bài <span class="text-danger">*</span></label>
                        <textarea name="userMessage" class="form-control" rows="5" required
                                  placeholder="Ví dụ: Bài viết có thông tin chưa chính xác, tôi muốn chỉnh sửa lại..."></textarea>
                    </div>

                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-danger">
                            <i class="bi bi-flag"></i> Gửi yêu cầu gỡ
                        </button>
                        <a href="${pageContext.request.contextPath}/my-articles" class="btn btn-outline-secondary">
                            Hủy
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </c:if>

</div>

</body>
</html>
