package NetworkTool;

import Struct.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 16-4-28.
 */
//TODO 有空格会出错?? 没有orderby F是个数组??
public class SendApi {

    private static String key = "f7cc29509a8443c5b3a5e56b0e38b5a6";

    public  static String send(String query, int count,int offset,String attributes) {
         CommunicationTool communicationTool = new CommunicationTool();
        String request = "http://oxfordhk.azure-api.net/academic/v1.0/evaluate?"
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

    public static List<Entity> generateEntities(JSONArray entities) {
        List<Entity> res = new ArrayList<Entity>();
        int number = entities.length();
        for (int i=0;i<number;i++) {
            JSONObject jsonEntity = entities.getJSONObject(i);
            Entity entity = new Entity();
            if (jsonEntity.has("logprob")) {
                entity.setLogprob(jsonEntity.getDouble("logprob"));
            }

            if (jsonEntity.has("Id")) {
                entity.setId(jsonEntity.getLong("Id"));
            }

            if (jsonEntity.has("Ti")) {
                entity.setTi(jsonEntity.getString("Ti"));
            }

            if (jsonEntity.has("Y")) {
                entity.setY(jsonEntity.getInt("Y"));
            }

            if (jsonEntity.has("D")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = jsonEntity.getString("D");
                try {
                    entity.setD(simpleDateFormat.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (jsonEntity.has("CC")) {
                entity.setCC(jsonEntity.getInt("CC"));
            }

            if (jsonEntity.has("AA")) {
                List<EntityAA> AAs = new ArrayList<EntityAA>();
                JSONArray JSONAAs = jsonEntity.getJSONArray("AA");
                int AALength = JSONAAs.length();
                for (int j=0;j<AALength;j++) {
                    EntityAA entityAA = new EntityAA();
                    JSONObject jsonAA = JSONAAs.getJSONObject(j);
                    if (jsonAA.has("AuN")) {
                        entityAA.setAA_AuN(jsonAA.getString("AuN"));
                    }
                    if (jsonAA.has("AuId")) {
                        entityAA.setAA_AuId(jsonAA.getLong("AuId"));
                    }
                    if (jsonAA.has("AfN")) {
                        entityAA.setAA_AfN(jsonAA.getString("AfN"));
                    }
                    if (jsonAA.has("AfId")) {
                        entityAA.setAA_AfId(jsonAA.getLong("AfId"));
                    }
                    AAs.add(entityAA);
                }
                entity.setEntityAA(AAs);
            }

            if (jsonEntity.has("F")) {
                List<EntityF> Fs = new ArrayList<EntityF>();
                JSONArray JSONFs = jsonEntity.getJSONArray("F");
                int FLength = JSONFs.length();
                for (int j=0;j<FLength;j++) {
                    EntityF entityF = new EntityF();
                    JSONObject jsonF = JSONFs.getJSONObject(j);
                    if (jsonF.has("FN")) {
                        entityF.setF_FN(jsonF.getString("FN"));
                    }
                    if (jsonF.has("FId")) {
                        entityF.setF_FId(jsonF.getLong("FId"));
                    }

                    Fs.add(entityF);
                }
                entity.setEntityF(Fs);
            }

            if (jsonEntity.has("J")) {
                JSONObject JSONJ = jsonEntity.getJSONObject("J");
                    EntityJ entityJ = new EntityJ();
                    if (JSONJ.has("JN")) {
                        entityJ.setJ_JN(JSONJ.getString("JN"));
                    }
                    if (JSONJ.has("JId")) {
                        entityJ.setJ_Id(JSONJ.getLong("JId"));
                    }

                entity.setEntityJ(entityJ);
            }

            if (jsonEntity.has("C")) {
                EntityC entityC = new EntityC();
                JSONObject JSONC = jsonEntity.getJSONObject("C");

                if (JSONC.has("CN")) {
                    entityC.setC_CN(JSONC.getString("CN"));
                }
                if (JSONC.has("CId")) {
                    entityC.setC_Id(JSONC.getLong("CId"));
                }

                entity.setEntityC(entityC);
            }

            if (jsonEntity.has("RId")) {
                List<EntityR> Rs = new ArrayList<EntityR>();
                JSONArray JSONRs = jsonEntity.getJSONArray("RId");
                int RLength = JSONRs.length();
                for (int j=0;j<RLength;j++) {
                    EntityR entityR = new EntityR();
                    entityR.setRId(JSONRs.getLong(j));
                    Rs.add(entityR);
                }
                entity.setEntityR(Rs);
            }

            if (jsonEntity.has("W")) {
                List<EntityW> Ws = new ArrayList<EntityW>();
                JSONArray JSONWs = jsonEntity.getJSONArray("W");
                int WLength = JSONWs.length();
                for (int j=0;j<WLength;j++) {
                    EntityW entityW = new EntityW();
                    entityW.setW(JSONWs.getString(j));
                    Ws.add(entityW);
                }
                entity.setEntityW(Ws);
            }
            res.add(entity);
        }
        return res;
    }

    public static APIResponse analyzeResponse(String res) {
        APIResponse apiResponse = new APIResponse();
        JSONObject jsonObject = new JSONObject(res);

        if (jsonObject.has("expr")) {
            String expr = jsonObject.getString("expr");
            apiResponse.setExpr(expr);
        }

        if (jsonObject.has("entities")) {
            List<Entity> entities = generateEntities(jsonObject.getJSONArray("entities"));
            apiResponse.setEntities(entities);
        }

        return apiResponse;
    }

    public static void main(String args[]) {
        SendApi sendApi = new SendApi();
            String res = sendApi.send("Y>2005",100,0,"attributes=Id,J.JN,J.ID");
        System.out.println(res);
        APIResponse apiResponse = new APIResponse();
        apiResponse = sendApi.analyzeResponse(res);
        System.out.println(apiResponse.getEntities().get(0).getEntityAA().get(0).getAA_AfN());
    }
}
