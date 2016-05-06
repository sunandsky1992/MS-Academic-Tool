package Strategy;

import Common.Constants;
import NetworkTool.SendApi;
import Path.PathTools;
import Struct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss on 16/5/1.
 */
/*可能路径
*
* AUID to AUID
* auid -> id -> id -> auid
* auid -> afid -> auid
*
* ID to ID
* id -> id -> id -> id
* id -> fid -> id
* id -> cid -> id
* id -> jid -> id
* id -> auid ->id
*
* id to auid
* id -> id -> id ->auid
* id -> fid -> id -> auid
* id -> cid -> id -> auid
* id -> jid -> id -> auid
* id -> auid -> afid -> auid
*
* auid to id
* auid -> id -> id -> id
* auid -> afid -> auid -> id
* auid -> id -> fid -> id
* auid -> id -> cid -> id
* auid -> id -> jid -> id
* auid -> id -> auid -> id
*
* */
//TODO 考虑搜索方式方法
public class Strategy {
    public List<long[]> res = new ArrayList<long[]>();
    public String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";

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

    public void findPath(long id, String type, Entity entity, long endId, String endType, long[] path, int step) {
        if (step > 3) {
            System.out.println(res.size());
            return;
        }

        //下一个为ID
        //TODO 可以优化
        if ((endType.equals(Constants.ID_TYPE) ||
                endType.equals(Constants.AUID_TYPE)) && step <= 2) {
            List<EntityR> entityRs = entity.getEntityR();
            for (EntityR entityR : entityRs) {
                path[step] = entityR.getRId();
                Entity newEntity = getById(entityR.getRId());
                if (endType.equals(Constants.ID_TYPE) && entityR.getRId()==endId){
                    path[step+1] = endId;
                    res.add(path.clone());
                }
                findPath(entityR.getRId(), Constants.ID_TYPE, newEntity, endId, endType, path, step + 1);
            }
        }

        //下一个为AuID
        if (type.equals(Constants.ID_TYPE) && ((endType.equals(Constants.ID_TYPE) && step <= 2) ||
                (endType.equals(Constants.AUID_TYPE) && step != 2))) {
            List<EntityAA> entityAAs = entity.getEntityAA();
            for (EntityAA entityAA : entityAAs) {
                path[step] = entityAA.getAA_AuId();
                if (endType.equals(Constants.AUID_TYPE) && entityAA.getAA_AuId() == endId) {
                    path[step+1] = endId;
                    res.add(path.clone());
                    //TODO 需要继续么？
                    //continue;
                }
                String query = "Composite(AA.AuId=" + entityAA.getAA_AuId() + ")";
                String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
                SendApi sendApi = new SendApi();
                String jsonStr = sendApi.send(query, 1000, 0, attribute);
                APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
                List<Entity> entities = apiResponse.getEntities();
                for (Entity entityByAuId : entities) {
                    findPath(entityAA.getAA_AuId(), Constants.AUID_TYPE, entityByAuId, endId, endType, path, step + 1);
                }
            }
        }

        //下一个为AfID

        if (type.equals(Constants.AUID_TYPE) && ((endType.equals(Constants.AUID_TYPE) && step<=2))) {
            List<EntityAA> entityAAs = entity.getEntityAA();
            for (EntityAA entityAA:entityAAs) {
                path[step] = entityAA.getAA_AfId();
                String query = "Composite(And(AA.AfId="+entityAA.getAA_AfId()+",AA.AuId="+endId+"))";
                String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
                SendApi sendApi = new SendApi();
                String jsonStr = sendApi.send(query, 1000, 0, attribute);
                APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
                List<Entity> entities = apiResponse.getEntities();
                if (!entities.isEmpty()) {
                    path[step+1] = endId;
                    res.add(path.clone());
                }
            }
        }

        if (type.equals(Constants.AUID_TYPE) && ((endType.equals(Constants.ID_TYPE) && step<2))) {
            List<EntityAA> entityAAs = entity.getEntityAA();
            for (EntityAA entityAA:entityAAs) {
                path[step] = entityAA.getAA_AfId();
                String query = "And(Composite(AA.AfId="+entityAA.getAA_AfId()+"),Id="+endId+")";
                String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
                SendApi sendApi = new SendApi();
                String jsonStr = sendApi.send(query, 1000, 0, attribute);
                APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
                List<Entity> entities = apiResponse.getEntities();
                if (!entities.isEmpty()) {
                    for (Entity entity1:entities) {
                        for (EntityAA entityAA1:entity1.getEntityAA()) {
                            if (entityAA1.getAA_AfId() == entityAA.getAA_AfId()) {
                                path[step + 1] = entityAA1.getAA_AuId();
                                path[step + 2] = endId;
                                res.add(path.clone());
                            }
                        }
                    }

                }
            }
        }

        //下一个为FId
        //fid to id
        //如果有 id to fid 则反过来
        if (type.equals(Constants.ID_TYPE) && ((endType.equals(Constants.ID_TYPE) && step<=2))) {
            List<EntityF> entityFs = entity.getEntityF();
            for (EntityF entityF:entityFs) {
                path[step] = entityF.getF_FId();
                String query = "And(Composite(F.FId="+entityF.getF_FId()+"),Id="+endId+")";
                String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
                SendApi sendApi = new SendApi();
                String jsonStr = sendApi.send(query, 1000, 0, attribute);
                APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
                List<Entity> entities = apiResponse.getEntities();
                if (!entities.isEmpty()) {
                    path[step+1] = endId;
                    res.add(path.clone());
                }
            }
        }

        //下一个为CID
        //CID to id
        //如果有 id to cid 则反过来
        if (entity.getEntityC()!=null && type.equals(Constants.ID_TYPE) && ((endType.equals(Constants.ID_TYPE) && step<=2))) {
                EntityC entityC = entity.getEntityC();
                path[step] = entityC.getC_Id();
                String query = "And(Composite(C.CId="+entityC.getC_Id()+"),Id="+endId+")";
                String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
                SendApi sendApi = new SendApi();
                String jsonStr = sendApi.send(query, 1000, 0, attribute);
                APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
                List<Entity> entities = apiResponse.getEntities();
                if (!entities.isEmpty()) {
                    path[step+1] = endId;
                    res.add(path.clone());
                }
        }

        //下一个为JID
        //JID to id
        //如果有 id to cid 则反过来
        if (entity.getEntityJ()!=null && type.equals(Constants.ID_TYPE) && ((endType.equals(Constants.ID_TYPE) && step<=2))) {
            EntityJ entityJ = entity.getEntityJ();
            path[step] = entityJ.getJ_Id();
            String query = "And(Composite(J.JId="+entityJ.getJ_Id()+"),Id="+endId+")";
            String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
            SendApi sendApi = new SendApi();
            String jsonStr = sendApi.send(query, 1000, 0, attribute);
            APIResponse apiResponse = sendApi.analyzeResponse(jsonStr);
            List<Entity> entities = apiResponse.getEntities();
            if (!entities.isEmpty()) {
                path[step+1] = endId;
                res.add(path.clone());
            }
        }
    }


    //单一的搜索路径
    public List<long[]> IdId(long beginId, long endId, Entity entity){
        List<long[]> res = new ArrayList<long[]>();
        List<EntityR> entityRs = entity.getEntityR();
        for (EntityR entityR: entityRs){
            if (entityR.getRId() == endId) {
                long[] ls = {beginId,endId};
                res.add(ls);
            }
        }
        return res;
    }

    public List<long[]> IdIdId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityR> entityRs = entity.getEntityR();
        String query = "";
        query = "And(Or(";
        for (EntityR entityR : entityRs) {
            query+="Id="+entityR.getRId()+",";
        }
        query = query.substring(0,query.length()-1);
        query += "),RId="+endId+")";
        String jsonStr = SendApi.send(query,1000,0,attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entity1 : entities) {
            long[] l = {beginId,entity1.getId(),endId};
            res.add(l);
        }
        return res;
    }

    public List<long[]> IDMULTIID(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        return res;
    }

    public static void main(String args[]) {
        Strategy strategy = new Strategy();
        Entity entity = strategy.getById(1502768748);
        long[] a = {1502768748,0,0,0};
        strategy.findPath(1502768748l, Constants.ID_TYPE, entity, 2122841972l, Constants.ID_TYPE, a, 1);
        System.out.println(strategy.res.size());
        for (long[] l:strategy.res) {
            for (long t :l){
                System.out.print(t+" ");
            }
            System.out.println();
        }
    }
}
