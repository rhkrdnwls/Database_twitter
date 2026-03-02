package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper; // JSON 파싱을 위해 필요합니다 (라이브러리 추가 필요)
import dao.UserDAO;
import model.User;
// TODO: UserDto가 있다면 UserDto도 import 해야 합니다.

@WebServlet("/register") // ⭐️ 경로 수정 완료 ⭐️
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 응답 설정: JSON 응답 및 UTF-8 인코딩
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ObjectMapper mapper = new ObjectMapper();

            User user = mapper.readValue(request.getInputStream(), User.class);
            

            if (user == null || user.getUserId() == null || user.getPwd() == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON data or missing ID/PWD");
                return;
            }

            UserDAO dao = new UserDAO();
            boolean success = dao.insertUser(user);

            // 4. 응답 전송
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK); // 200 OK (회원가입 성공)
                response.getWriter().write("{\"message\": \"회원가입 성공\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict (ID 중복 등)
                response.getWriter().write("{\"message\": \"회원가입 실패\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 5. 서버 내부 오류 응답
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error: " + e.getMessage()); // 500 오류
        }
    }

    // doGet은 POST 요청이므로 필요하지 않습니다.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405 Method Not Allowed
    }
}