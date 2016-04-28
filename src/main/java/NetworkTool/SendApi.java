package NetworkTool;

import Struct.APIResponse;
import Struct.Entity;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ss on 16-4-28.
 */
public class SendApi {
    private CommunicationTool communicationTool = new CommunicationTool();

    private String key = "f7cc29509a8443c5b3a5e56b0e38b5a6";

    public  String send(String query, int count,int offset,String attributes) {
        String request = "https://oxfordhk.azure-api.net/academic/v1.0/evaluate?"
                +"expr="+query+"&count="+count+"&offset="+offset+"&"+attributes
                + "&subscription-key="+key;
        String res = "";
        try {
             res = communicationTool.send(request,"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Entity analyzeResponse(String res) {
        APIResponse apiResponse = new APIResponse();
        JSONObject jsonObject = new JSONObject(res);

        if (jsonObject.has("expr")) {
            String expr = jsonObject.getString("expr");
            apiResponse.setExpr(expr);
        }

        
    }

    public static void main(String args[]) {
        SendApi sendApi = new SendApi();
        String res = sendApi.send("Id=2140251882",100,0,"attributes=Id,AA.AuId,AA.AfId");
        System.out.println(res);
    }
}
