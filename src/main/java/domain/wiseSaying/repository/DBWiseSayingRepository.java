package domain.wiseSaying.repository;

import domain.wiseSaying.entity.WiseSaying;
import util.ConfigUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DBWiseSayingRepository implements WiseSayingRepository {

    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    public DBWiseSayingRepository() {
        DB_URL = ConfigUtil.get("db.url");
        DB_USER = ConfigUtil.get("db.user");
        DB_PASSWORD = ConfigUtil.get("db.password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            System.out.println("MySQL 드라이버 로드 실패");
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public int save(String author, String content) {
        String sql = "INSERT INTO wisesaying (author, content) VALUES (?, ?)";

        try {
            //연결 및 sql문 준비
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //?에 변수 넣기
            pstmt.setString(1, author);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("저장 실패: " + e.getMessage());
        }

        return -1;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM wisesaying WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        }

        return false;
    }

    @Override
    public void update(int id, String author, String content) {
        String sql = "UPDATE wisesaying SET author = ?, content = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, author);
            pstmt.setString(2, content);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("수정 실패: " + e.getMessage());
        }
    }

    @Override
    public List<WiseSaying> getList(String keywordType, String keyword) {
        List<WiseSaying> list = new ArrayList<>();
        String sql = "SELECT * FROM wisesaying";

        if (keyword != null && !keyword.isEmpty()) {
            if ("content".equalsIgnoreCase(keywordType)) {
                sql += " WHERE content LIKE ?";
            } else if ("author".equalsIgnoreCase(keywordType)) {
                sql += " WHERE author LIKE ?";
            }
        }

        sql += " ORDER BY id DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (sql.contains("LIKE")) {
                pstmt.setString(1, "%" + keyword + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new WiseSaying(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("content")
                ));
            }

        } catch (SQLException e) {
            System.out.println("목록 조회 실패: " + e.getMessage());
        }

        return list;
    }

    @Override
    public WiseSaying findById(int id) {
        String sql = "SELECT * FROM wisesaying WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new WiseSaying(
                        rs.getInt("id"),
                        rs.getString("author"),
                        rs.getString("content")
                );
            }

        } catch (SQLException e) {
            System.out.println("id 조회 실패: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean buildJsonFile() {
        String path = "db/wiseSaying/data.json";
        StringBuilder sb = new StringBuilder();

        List<WiseSaying> list = getList(null, null);

        if (list.isEmpty()) {
            sb.append("[\n").append("]");
        } else {
            sb.append("[\n");
            for (WiseSaying saying : list) {
                sb.append(saying.toJson()).append(",\n");
            }
            sb.setLength(sb.length() - 2); // 마지막 콤마와 줄바꿈 제거
            sb.append("\n]");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(sb.toString());
            return true;
        } catch (IOException e) {
            System.out.println("data.json 파일 빌드 실패: " + e.getMessage());
            return false;
        }
    }
}
