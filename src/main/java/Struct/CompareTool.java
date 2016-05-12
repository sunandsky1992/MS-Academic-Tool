package Struct;

import org.json.JSONArray;

import java.io.*;

/**
 * Created by ss on 16-5-12.
 */
public class CompareTool {
    public String getJson(String path) throws IOException {
        File filename = new File(path);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";
        line = br.readLine();
        String res = line;
        while (line != null) {
            line = br.readLine(); // 一次读入一行数据
            res +=line;
        }
        return res;
    }

    public void compareTool(String add1,String add2) throws IOException {
        JSONArray js1 = new JSONArray(getJson(add1));
        JSONArray js2 = new JSONArray(getJson(add2));
        System.out.println(js1.length());
        System.out.println(js2.length());
        for (int i = 0;i<js1.length();i++) {
            int flag = 0;
            JSONArray tjs1 = js1.getJSONArray(i);
            for (int j=0;j<js2.length();j++) {
                int flag2 = 0;
                JSONArray tjs2 = js2.getJSONArray(j);
                if (tjs1.length()!=tjs2.length()) {
                    continue;
                }
                for (int k =0; k<tjs1.length();k++) {
                    if (tjs1.getLong(k) != tjs2.getLong(k)) {
                        flag2 = 1;
                        break;
                    }
                }
                if (flag2 == 0)
                    flag = 1;
            }
            if (flag == 0) {
                System.out.println(js1.get(i).toString());
            }
        }
    }
    public static void main(String[] args) throws IOException {
        CompareTool compareTool = new CompareTool();
        compareTool.compareTool("/home/ss/workspace/academicSearch/src/main/java/Struct/max", "/home/ss/workspace/academicSearch/src/main/java/Struct/min");
    }
}
