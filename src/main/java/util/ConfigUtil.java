package util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("db.properties 파일을 찾을 수 없습니다.");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("설정 파일 로드 실패: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
