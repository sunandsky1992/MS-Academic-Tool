package Path;

import NetworkTool.SendApi;
import Struct.*;

import java.util.List;

/**
 * Created by ss on 16/4/30.
 */
//这些代码没用了。。
public class  PathTools {
    public boolean hasPath(Entity a, Entity b) {
        boolean res = idAndId(a,b);
        res = res || FIdAndFId(a,b);
        res = res || CIdAndCId(a,b);
        res = res || JIdAndJId(a,b);
        res = res || AAFAndAAF(a,b);
        return res;
    }

    //rule1
    public boolean idAndId(Entity a, Entity b) {
        List<EntityR> entityRs = a.getEntityR();
        if (entityRs == null) return false;
        if (entityRs.contains(b)) {
            return true;
        }
        return false;
    }

    //rule2 and rule3
    public boolean FIdAndFId(Entity a, Entity b) {
        if (a== null || b == null || a.getEntityF() == null || b.getEntityF()==null) {
            return false;
        }

        for (EntityF entityFa:a.getEntityF())
            for (EntityF entityFb:b.getEntityF())
                if (entityFa.getF_FId() == entityFb.getF_FId())
                    return true;
//        if (a.getEntityF().contains(b.getEntityF()))
//            return true;
        return false;
    }

    //rule4 and rule5
    public boolean CIdAndCId(Entity a, Entity b) {

        if (a==null || b==null) {
            return false;
        }
        if (a.getEntityC().getC_Id() == b.getEntityC().getC_Id())
            return true;
        return false;
    }

    //rule6 and rule7
    public boolean JIdAndJId(Entity a,Entity b) {
        if (a==null || b==null) {
            return false;
        }
        if (a.getEntityJ().getJ_Id() == b.getEntityJ().getJ_Id())
            return true;
        return false;
    }
    //rule 8,9,10,11
    public boolean AAFAndAAF(Entity a, Entity b) {
        if (a==null || b==null) {
            return false;
        }
        for (EntityAA entityAAa:a.getEntityAA())
            for (EntityAA entityAAb:b.getEntityAA()) {
                if (entityAAa.getAA_AfId() == entityAAb.getAA_AfId() || entityAAa.getAA_AuId() == entityAAb.getAA_AuId())
                    return true;
            }
        return false;
    }

    public List<Entity> getByAuId(long AuId) {
        String query = "Composite(AA.AuId="+AuId+")";
        String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
        SendApi sendApi = new SendApi();
        String jsonStr = sendApi.send(query, 1, 0, attribute);
        APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
        return apiResponse.getEntities();
    }

    public Entity getById(long beginId) {
        String query = "Id=" + beginId;
        String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
        SendApi sendApi = new SendApi();
        String jsonStr = sendApi.send(query, 1000, 0, attribute);
        APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
        if (apiResponse.getEntities().size() == 0)
            return null;
        return apiResponse.getEntities().get(0);
    }
}
