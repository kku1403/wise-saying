package domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Rq {
    private final String actionName;
    private final Map<String, String> paramsMap;

    public Rq(String cmd) {
        String[] cmdBits = cmd.split("\\?", 2);
        actionName = cmdBits[0]; //물음표 앞에 거 가져오기(목록)
        String queryString = cmdBits.length > 1 ? cmdBits[1].trim() : ""; //뒤에 내용 가져오기, 없으면 ""

        paramsMap = Arrays.stream(queryString.split("&"))
                .map(part -> part.split("=", 2))
                .filter(bits -> bits.length > 0 && !bits[0].trim().isEmpty() && !bits[1].trim().isEmpty())
                .collect(Collectors.toMap(
                        bits -> bits[0].trim(),
                        bits -> bits[1].trim()
                ));

    }

    public String getActionName() {
        return actionName;
    }

    public String getParam(String paramName, String defaultValue) {
        return paramsMap.getOrDefault(paramName, defaultValue);
    }

    public int getParamAsInt(String paramName, int defaultValue) {
        String value = getParam(paramName, "");

        if (value.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}