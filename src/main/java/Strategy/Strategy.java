package Strategy;

import NetworkTool.SendApi;
import Struct.APIResponse;
import Struct.Entity;
import Struct.EntityR;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by ss on 16/5/1.
 */
//TODO 考虑搜索方式方法
public class Strategy {

    public List<List<Long>> idAndId(long beginId, long endId) {
        List<List<Long>> res = new ArrayList<List<Long>>();
        String query = "Id="+beginId;
        String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
        SendApi sendApi = new SendApi();
        String jsonStr = sendApi.send(query,100,0,attribute);
        APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
        if (apiResponse.getEntities().size()==0)
            return new ArrayList<List<Long>>();
        Entity entity = apiResponse.getEntities().get(0);

        List<EntityR> entityRs = entity.getEntityR();
        for (EntityR entityR : entityRs) {
            if (entityR.getRId() == endId) {
                List<Long> path = new ArrayList<Long>();
                path.add(beginId);
                path.add(endId);
                res.add(path);
            } res
        }
        System.out.println(res);
        return res;
    }

    public static void main(String args[]) {
        Strategy strategy = new Strategy();
        strategy.idAndId(2143981217,0);
    }
}
