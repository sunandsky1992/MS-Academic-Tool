package NetworkTool;

import Common.Constants;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by ss on 16-4-28.
 */
public class CommunicationTool {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String send(String url, String paramsStr) throws IOException {
        return this.send(url, paramsStr, Constants.DEFAULT_REQUEST_METHOD);
    }

    public String send(String url, String paramsStr, RequestMethod method) throws IOException {
        return this.send(url, paramsStr, method, Constants.NEED_FEEDBACK_OR_NOT);
    }

    public String send(String url, String paramsStr, boolean needFeedback) throws IOException {
        return this.send(url, paramsStr, Constants.DEFAULT_REQUEST_METHOD, needFeedback);
    }

    public String send(String url, String paramsStr, RequestMethod method, boolean needFeedback) throws IOException {
        return this.send(url, paramsStr, method, needFeedback, null);
    }

    public String send(String url, String paramsStr, RequestMethod method, boolean needFeedback, Map<String,String> header) throws IOException {
        if (paramsStr == null || paramsStr.equals(Constants.EMPTY_STRING)) {
            paramsStr = "Without parameters";
        }

        HttpURLConnection connection = this.getConnection(url, method, header);

        // Write JSON parameters to the output stream
        connection = passParameters(connection, paramsStr);

        // If the caller don't need any feedback, send the request and return the empty string
        if (needFeedback) {
            InputStream is = connection.getInputStream();
            String feedBack = this.getTextFromInputStream(is);
            is.close();
            connection.disconnect();

            return feedBack;
        }

        return Constants.EMPTY_STRING;
    }

    private HttpURLConnection getConnection(String urlStr, RequestMethod method, Map<String,String> header) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method.name());
        connection.setDoOutput(Constants.OUTPUT_ALLOWED);
        connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
        connection.setReadTimeout(Constants.READ_TIMEOUT);
        connection.setRequestProperty("Content-Type", "application/json");
//        if (header!=null) {
//            for (Map.Entry<String,String> entry:header.entrySet()) {
//                connection.setRequestProperty(entry.getKey(),entry.getValue());
//            }
//        }

        return connection;
    }

    private HttpURLConnection passParameters(HttpURLConnection connection, String paramsStr) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(paramsStr);
        osw.close(); // Close the resource before leaving, otherwise there would be problems of memory leak.

        return connection;
    }


    private String getTextFromInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Constants.ENCODING));

        String response = Constants.EMPTY_STRING;
        String line;
        while ((line = br.readLine()) != null) {
            response += line;
        }

        return response; // This is only a single line message
    }

    public static void main(String[] args) throws IOException {

        CommunicationTool communicationTool = new CommunicationTool();
        String response = communicationTool.send("http://10.108.122.134:8000/subtask/transport/5346850b1517a9f8254a9fc4791e193d65aa60baa82a507ef63471c19e000123", "{\"appName\":\"欢乐西游-手机游戏助手\",\"appVersion\":\"1.0\",\"status\":4,\"appUrlBin\":\"app4/M00/03/E9/Cmx6nlYwGySAZSn4AD8z3xW_AMI1563806\",\"appHash\":\"aaacfb67daaab92591618a1defbb2df84e5336bca506584f5bd2cc1edffc39\",\"appLogoUrl\":\"img1/M00/03/77/Cmx6nlYwGySAdDyUAACVOWq6Cvk429.png\",\"agentId\":\"ed61cf67073429b9a7c0\",\"appPackageName\":\"com.lanjing.hlxytools\",\"msg\":\"Apk is successfully transported\"}",RequestMethod.PUT, Constants.NEED_FEEDBACK_OR_NOT);
        System.out.println(response.toString());
    }
}
