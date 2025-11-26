package com.example.app.utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class Upload {

    // Thư mục lưu ảnh (Trong thực tế nên để đường dẫn tuyệt đối ra ngoài project)
    // Nhưng để demo chạy được ngay, ta lưu vào folder 'uploads' trong webapp
    private static final String UPLOAD_DIR = "uploads";

    /**
     * Xử lý lưu file từ Part (Servlet 3.0)
     * @param part đối tượng Part từ request.getPart("name")
     * @param realPath đường dẫn thực của thư mục gốc webapp (request.getServletContext().getRealPath(""))
     * @return Tên file đã lưu (để lưu vào database) hoặc null nếu lỗi
     */
    public static String saveFile(Part part, String realPath) {
        try {
            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            if (fileName == null || fileName.isEmpty()) {
                return null;
            }

            // Đổi tên file thành chuỗi ngẫu nhiên để tránh trùng tên (VD: avatar.jpg -> 123e4567-avatar.jpg)
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            // Tạo thư mục uploads nếu chưa có
            String uploadPath = realPath + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // Lưu file
            String fullPath = uploadPath + File.separator + uniqueFileName;
            part.write(fullPath);

            // Trả về đường dẫn tương đối để lưu DB (VD: uploads/123-anh.jpg)
            return UPLOAD_DIR + "/" + uniqueFileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
