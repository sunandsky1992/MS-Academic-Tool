package Strategy;

import NetworkTool.SendApi;
import Struct.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";

    public Entity getById(long beginId) {
        String query = "Id=" + beginId;
        String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
        String jsonStr = SendApi.send(query, 1000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        if (apiResponse.getEntities().size() == 0)
            return null;
        return apiResponse.getEntities().get(0);
    }

    public List<Long> getAfIdByAuId(long AuId) {
        List<Long> res = new ArrayList<Long>();
        String query = "Composite(AA.AuId="+AuId+")";
        String jsonStr = SendApi.send(query, 500, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        if (entities.isEmpty())
            return res;
        for (Entity entity :entities) {
            List<EntityAA> entityAAs = entity.getEntityAA();
            for (EntityAA entityAA : entityAAs) {
                if (AuId == entityAA.getAA_AuId() &&  entityAA.getAA_AfId()!=0 && !res.contains(entityAA.getAA_AfId()))
                   res.add(entityAA.getAA_AfId());
            }
        }
        return res;
    }

    public List<Entity> getByAuId(long AuId) {
        String query = "Composite(AA.AuId="+AuId+")";
        String attribute = "attributes=Id,F.FId,J.JId,C.CId,AA.AuId,AA.AfId,RId";
        String jsonStr = SendApi.send(query, 1000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        return apiResponse.getEntities();
    }

    public Map<Long,Entity> getByRId(List<EntityR> entityRs) {
        Map<Long,Entity> res = new HashMap<Long,Entity>();
        String query = "";
        if (entityRs.size()>=2) {
            int beginPosition = 0;
            int endPosition = 0;
            while (beginPosition<entityRs.size()) {
                query = "";
                endPosition = endPosition+30;
                if (endPosition>entityRs.size()-1) {
                    endPosition = entityRs.size() - 1;
                }

                if (endPosition - beginPosition == 0) {
                    query = "Id="+entityRs.get(beginPosition).getRId();
                    String jsonStr = SendApi.send(query, 10000, 0, attribute);
                    APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
                    List<Entity> entities = apiResponse.getEntities();
                    for (Entity entity1 : entities) {
                        res.put(entity1.getId(), entity1);
                    }
                } else {
                    for (int i = beginPosition; i <= endPosition - 1; i++) {
                        query += "Or(";
                    }
                    query += "Id=" + entityRs.get(beginPosition).getRId() + ",";
                    for (int i = beginPosition + 1; i <= endPosition; i++) {
                        query += "Id=" + entityRs.get(i).getRId() + "),";
                    }
                    query = query.substring(0, query.length() - 1);
                    String jsonStr;
                    APIResponse apiResponse;
                    try {
                        jsonStr = SendApi.send(query, 10000, 0, attribute);
                        apiResponse = SendApi.analyzeResponse(jsonStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return res;
                    }
                    List<Entity> entities = apiResponse.getEntities();
                    for (Entity entity1 : entities) {
                        res.put(entity1.getId(), entity1);
                    }
                }
                beginPosition = endPosition +1;
            }
        } else if (entityRs.size()==1) {
            query = "Id="+entityRs.get(0).getRId();
            String jsonStr = SendApi.send(query, 10000, 0, attribute);
            APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entity1 : entities) {
                res.put(entity1.getId(), entity1);
            }
        }
        return res;
    }

    //单一的搜索路径
    public List<long[]> IdId(long beginId, long endId, Entity entity){
        List<long[]> res = new ArrayList<long[]>();
        List<EntityR> entityRs = entity.getEntityR();
        for (EntityR entityR: entityRs){
            if (entityR.getRId() == endId) {
                long[] ls = {beginId,endId};
                res.add(ls);
                break;
            }
        }
        return res;
    }

    public List<long[]> IdIdId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityR> entityRs = entity.getEntityR();
        String query = "";
        if (entityRs.size()>=2) {
            int beginPosition = 0;
            int endPosition = 0;
            while (beginPosition<entityRs.size()) {
                query = "";
                endPosition = endPosition + 30;
                if (endPosition>entityRs.size()-1) {
                    endPosition = entityRs.size() - 1;
                }

                if (endPosition - beginPosition == 0) {
                    query = "And(Id="+entityRs.get(beginPosition).getRId()+",RId="+endId+")";
                    String jsonStr = SendApi.send(query, 10000, 0, attribute);
                    APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
                    List<Entity> entities = apiResponse.getEntities();
                    for (Entity entity1 : entities) {
                        long[] l = {beginId, entity1.getId(), endId};
                        res.add(l);
                    }
                } else {
                    query = "And(";
                    for (int i = beginPosition; i <= endPosition - 1; i++) {
                        query += "Or(";
                    }
                    query += "Id=" + entityRs.get(beginPosition).getRId() + ",";
                    for (int i = beginPosition + 1; i <= endPosition; i++) {
                        query += "Id=" + entityRs.get(i).getRId() + "),";
                    }

                    query += "RId=" + endId + ")";
                    String jsonStr;
                    APIResponse apiResponse;
                    try {
                        jsonStr = SendApi.send(query, 10000, 0, attribute);
                        apiResponse = SendApi.analyzeResponse(jsonStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return res;
                    }
                    List<Entity> entities = apiResponse.getEntities();
                    for (Entity entity1 : entities) {
                        long[] l = {beginId, entity1.getId(), endId};
                        res.add(l);
                    }
                }
                beginPosition = endPosition+1;
            }
        } else if (entityRs.size()==1){
            query = "And(Id="+entityRs.get(0).getRId()+",RId="+endId+")";
            String jsonStr = SendApi.send(query, 10000, 0, attribute);
            APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entity1 : entities) {
                long[] l = {beginId, entity1.getId(), endId};
                res.add(l);
            }
        }
        return res;
    }

    public List<long[]> IdFIdId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityF> entityFs = entity.getEntityF();
        List<Long> FIds = new ArrayList<Long>();
        for (EntityF entityF:entityFs) {
            FIds.add(entityF.getF_FId());
        }
        Entity endEntity = getById(endId);
        if (endEntity==null)
            return res;
        for (EntityF entityF:endEntity.getEntityF()) {
            if (FIds.contains(entityF.getF_FId())) {
                long[] tem = {beginId,entityF.getF_FId(),endId};
                res.add(tem);
            }
        }
        return res;
    }

    public List<long[]> IdFIdIdId(long beginId, long endId,Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityF> entityFs = entity.getEntityF();
        List<Long> Fs = new ArrayList<Long>();
        for (EntityF entityF :entityFs) {
            Fs.add(entityF.getF_FId());
        }
        if (entityFs.size()>=2) {
            String query = "And(";
            for (int i=0;i<entityFs.size()-1;i++)
                query+="Or(";
            query+="Composite(F.FId="+entityFs.get(0).getF_FId()+"),";
            for (int i=1;i<entityFs.size();i++) {
                query+="Composite(F.FId="+entityFs.get(i).getF_FId()+")),";
            }
            query += "RId="+endId+")";
            String jsonStr;
            APIResponse apiResponse;
            try {
                jsonStr = SendApi.send(query, 10000, 0, attribute);
                apiResponse = SendApi.analyzeResponse(jsonStr);
            }catch (Exception e) {
                e.printStackTrace();
                return res;
            }
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityF entityF :entityRes.getEntityF()) {
                    if (Fs.contains(entityF.getF_FId())) {
                        long[] tem = {beginId, entityF.getF_FId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        } else if (entityFs.size()==1) {
            String query = "And(";
            query+="Composite(F.FId="+entityFs.get(0).getF_FId()+"),";
            query += "RId="+endId+")";
            String jsonStr = SendApi.send(query, 10000, 0, attribute);
            APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityF entityF :entityRes.getEntityF()) {
                    if (Fs.contains(entityF.getF_FId())) {
                        long[] tem = {beginId, entityF.getF_FId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        }
        return res;
    }

    public List<long[]> IdFIdIdAuId(long beginId, long endId, Entity entity) {

        List<long[]> res = new ArrayList<long[]>();
        List<EntityF> entityFs = entity.getEntityF();
        List<Long> Fs = new ArrayList<Long>();
        for (EntityF entityF :entityFs) {
            Fs.add(entityF.getF_FId());
        }
        if (entityFs.size()>=2) {
            String query = "And(";
            for (int i=0;i<entityFs.size()-1;i++)
                query+="Or(";
            query+="Composite(F.FId="+entityFs.get(0).getF_FId()+"),";
            for (int i=1;i<entityFs.size();i++) {
                query+="Composite(F.FId="+entityFs.get(i).getF_FId()+")),";
            }
            query += "Composite(AA.AuId="+endId+"))";
            String jsonStr;
            APIResponse apiResponse;
            try {
                jsonStr = SendApi.send(query, 10000, 0, attribute);
                apiResponse = SendApi.analyzeResponse(jsonStr);
            }catch (Exception e) {
                e.printStackTrace();
                return res;
            }
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityF entityF :entityRes.getEntityF()) {
                    if (Fs.contains(entityF.getF_FId())) {
                        long[] tem = {beginId, entityF.getF_FId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        } else if (entityFs.size()==1) {
            String query = "And(";
            query+="Composite(F.FId="+entityFs.get(0).getF_FId()+"),";
            query += "Composite(AA.AuId="+endId+"))";
            String jsonStr = SendApi.send(query, 10000, 0, attribute);
            APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityF entityF :entityRes.getEntityF()) {
                    if (Fs.contains(entityF.getF_FId())) {
                        long[] tem = {beginId, entityF.getF_FId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        }
        return res;
    }

    public List<long[]> IdCIdId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        EntityC entityC = entity.getEntityC();
        if (entityC == null)
            return res;
        String query = "And(Composite(C.CId="+entity.getEntityC().getC_Id()+"),Id="+endId+")";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entityRes : entities) {
            long[] tem = {beginId,entity.getEntityC().getC_Id(),endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> IdCIdIdId(long beginId, long endId,Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        if (entity.getEntityC() == null)
            return res;
        String query = "And(Composite(C.CId="+entity.getEntityC().getC_Id()+"),RId="+endId+")";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entityRes : entities) {
            long[] tem = {beginId,entity.getEntityC().getC_Id(),entityRes.getId(),endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> IdCIdIdAuId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        if (entity.getEntityC() == null)
            return res;
        String query = "And(Composite(C.CId="+entity.getEntityC().getC_Id()+"),Composite(AA.AuId="+endId+"))";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entityRes : entities) {
            long[] tem = {beginId,entity.getEntityC().getC_Id(),entityRes.getId(),endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> IdJIdId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        EntityJ entityJ = entity.getEntityJ();
        if (entityJ == null)
            return res;
        String query = "And(Composite(J.JId="+entity.getEntityJ().getJ_Id()+"),Id="+endId+")";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entityRes : entities) {
            long[] tem = {beginId,entity.getEntityJ().getJ_Id(),endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> IdJIdIdId(long beginId, long endId,Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        if (entity.getEntityJ() == null)
            return res;
        String query = "And(Composite(J.JId="+entity.getEntityJ().getJ_Id()+"),RId="+endId+")";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entityRes : entities) {
            long[] tem = {beginId,entity.getEntityJ().getJ_Id(),entityRes.getId(),endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> IdJIdIdAuId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        if (entity.getEntityJ() == null)
            return res;
        String query = "And(Composite(J.JId="+entity.getEntityJ().getJ_Id()+"),Composite(AA.AuId="+endId+"))";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entityRes : entities) {
            long[] tem = {beginId,entity.getEntityJ().getJ_Id(),entityRes.getId(),endId};
            res.add(tem);
        }
        return res;
    }


    public List<long[]> IdAuIdId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityAA> entityAAs = entity.getEntityAA();
        List<Long> auids = new ArrayList<Long>();
        for (EntityAA entityAA:entityAAs) {
            auids.add(entityAA.getAA_AuId());
        }

        Entity endEntity = getById(endId);
        if (endEntity==null)
            return res;
        for (EntityAA entityAA:endEntity.getEntityAA()) {
            if (auids.contains(entityAA.getAA_AuId())) {
                long[] tem = {beginId,entityAA.getAA_AuId(),endId};
                res.add(tem);
            }
        }
        return res;
    }

    public List<long[]> IdAuIdIdId(long beginId, long endId,Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityAA> entityAAs = entity.getEntityAA();
        List<Long> AuIds = new ArrayList<Long>();
        for (EntityAA entityAA :entityAAs) {
            AuIds.add(entityAA.getAA_AuId());
        }
        if (AuIds.size()>=2) {
            String query = "And(";
            for (int i=0;i<AuIds.size()-1;i++)
                query+="Or(";
            query+="Composite(AA.AuId="+AuIds.get(0)+"),";
            for (int i=1;i<AuIds.size();i++) {
                query+="Composite(AA.AuId="+AuIds.get(i)+")),";
            }
            query += "RId="+endId+")";
            String jsonStr;
            APIResponse apiResponse;
            try {
                jsonStr = SendApi.send(query, 10000, 0, attribute);
                apiResponse = SendApi.analyzeResponse(jsonStr);
            }catch (Exception e) {
                e.printStackTrace();
                return res;
            }
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityAA entityAA :entityRes.getEntityAA()) {
                    if (AuIds.contains(entityAA.getAA_AuId())) {
                        long[] tem = {beginId, entityAA.getAA_AuId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        } else if (AuIds.size()== 1) {
            String query = "And(";
            query+="Composite(AA.AuId="+AuIds.get(0)+"),";
            query += "RId="+endId+")";
            String jsonStr = SendApi.send(query, 10000, 0, attribute);
            APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityAA entityAA :entityRes.getEntityAA()) {
                    if (AuIds.contains(entityAA.getAA_AuId())) {
                        long[] tem = {beginId, entityAA.getAA_AuId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        }
        return res;
    }

    public List<long[]> IdAuIdIdAuId(long beginId, long endId, Entity entity) {

        List<long[]> res = new ArrayList<long[]>();
        List<EntityAA> entityAAs = entity.getEntityAA();
        List<Long> AuIds = new ArrayList<Long>();
        for (EntityAA entityAA :entityAAs) {
            AuIds.add(entityAA.getAA_AuId());
        }
        if (AuIds.size()>=2) {
            String query = "And(";
            for (int i=0;i<AuIds.size()-1;i++)
                query+="Or(";
            query+="Composite(AA.AuId="+AuIds.get(0)+"),";
            for (int i=1;i<AuIds.size();i++) {
                query+="Composite(AA.AuId="+AuIds.get(i)+")),";
            }
            query += "Composite(AA.AuId="+endId+"))";
            String jsonStr;
            APIResponse apiResponse;
            try {
                jsonStr = SendApi.send(query, 10000, 0, attribute);
                apiResponse = SendApi.analyzeResponse(jsonStr);
            }catch (Exception e) {
                e.printStackTrace();
                return res;
            }
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityAA entityAA :entityRes.getEntityAA()) {
                    if (AuIds.contains(entityAA.getAA_AuId())) {
                        long[] tem = {beginId, entityAA.getAA_AuId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        } else if (AuIds.size()==1) {
            String query = "And(";
            query+="Composite(AA.AuId="+AuIds.get(0)+"),";
            query += "Composite(AA.AuId="+endId+"))";
            String jsonStr = SendApi.send(query, 10000, 0, attribute);
            APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
            List<Entity> entities = apiResponse.getEntities();
            for (Entity entityRes : entities) {
                for (EntityAA entityAA :entityRes.getEntityAA()) {
                    if (AuIds.contains(entityAA.getAA_AuId())) {
                        long[] tem = {beginId, entityAA.getAA_AuId(),entityRes.getId(), endId};
                        res.add(tem);
                    }
                }
            }
        }
        return res;
    }



    public List<long[]> AuIdId(long beginId, long endId) {
        List<long[]> res = new ArrayList<long[]>();
        String query = "And(Composite(AA.AuId="+beginId+"),Id="+endId+")";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        if (!entities.isEmpty()){
            long[] tem = {beginId,endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> AuIdIdId(long beginId, long endId) {
        List<long[]> res = new ArrayList<long[]>();
        String query = "And(Composite(AA.AuId="+beginId+"),RId="+endId+")";
        String jsonStr;
        APIResponse apiResponse;
        try {
            jsonStr = SendApi.send(query, 10000, 0, attribute);
            apiResponse = SendApi.analyzeResponse(jsonStr);
        }catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entity1:entities) {
            long[] tem = {beginId,entity1.getId(),endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> IdIdAuId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityR> entityRs = entity.getEntityR();
        String query = "";
        if (entityRs.size()>=2) {
            int beginPosition = 0;
            int endPosition = 0;
            while (beginPosition<entityRs.size()) {
                query = "";
                endPosition = endPosition + 30;
                if (endPosition > entityRs.size() - 1) {
                    endPosition = entityRs.size() - 1;
                }

                if (endPosition - beginPosition == 0) {
                    query = "And(";
                    query += "Id=" + entityRs.get(beginPosition).getRId() + ",";
                    query += "Composite(AA.AuId=" + endId + "))";
                    String jsonStr;
                    APIResponse apiResponse;
                    try {
                        jsonStr = SendApi.send(query, 10000, 0, attribute);
                        apiResponse = SendApi.analyzeResponse(jsonStr);
                    }catch (Exception e) {
                        e.printStackTrace();
                        return res;
                    }

                    List<Entity> entities = apiResponse.getEntities();
                    for (Entity entity1 : entities) {
                        long[] l = {beginId, entity1.getId(), endId};
                        res.add(l);
                    }
                } else {
                    query = "And(";
                    for (int i = beginPosition; i <= endPosition - 1; i++) {
                        query += "Or(";
                    }
                    query += "Id=" + entityRs.get(beginPosition).getRId() + ",";
                    for (int i = beginPosition + 1; i <= endPosition; i++) {
                        query += "Id=" + entityRs.get(i).getRId() + "),";
                    }
                    query += "Composite(AA.AuId=" + endId + "))";
                    String jsonStr;
                    APIResponse apiResponse;
                    try {
                        jsonStr = SendApi.send(query, 10000, 0, attribute);
                        apiResponse = SendApi.analyzeResponse(jsonStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return res;
                    }

                    List<Entity> entities = apiResponse.getEntities();
                    for (Entity entity1 : entities) {
                        long[] l = {beginId, entity1.getId(), endId};
                        res.add(l);
                    }
                }
                beginPosition = endPosition + 1;
            }
        } else if (entityRs.size() == 1){
            query = "And(";
            query += "Id=" + entityRs.get(0).getRId() + ",";
            query += "Composite(AA.AuId=" + endId + "))";
            String jsonStr;
            APIResponse apiResponse;
            try {
                jsonStr = SendApi.send(query, 10000, 0, attribute);
                apiResponse = SendApi.analyzeResponse(jsonStr);
            }catch (Exception e) {
                e.printStackTrace();
                return res;
            }

            List<Entity> entities = apiResponse.getEntities();
            for (Entity entity1 : entities) {
                long[] l = {beginId, entity1.getId(), endId};
                res.add(l);
            }
        }
        return res;
    }

    public List<long[]> IdAuId(long beginId, long endId, Entity entity) {
        List<long[]> res = new ArrayList<long[]>();
        List<EntityAA> entityAAs = entity.getEntityAA();
        for (EntityAA entityAA: entityAAs) {
            if (entityAA.getAA_AuId() == endId) {
                long[] tem = {beginId,endId};
                res.add(tem);
                break;
            }
        }
        return res;
    }

    public List<long[]> AuIdIdAuId(long beginId, long endId) {
        List<long[]> res = new ArrayList<long[]>();
        String query = "And("+"Composite(AA.AuId="+beginId+"),Composite(AA.AuId="+endId+"))";
        String jsonStr = SendApi.send(query, 10000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entity1:entities) {
            long[] tem = {beginId,entity1.getId(),endId};
            res.add(tem);
        }
        return res;
    }

    public List<long[]> AuIdAfIdAuId(long beginId, long endId, List<Long> AfId) {
        List<long[]> res = new ArrayList<long[]>();
        if (AfId.isEmpty())
            return  res;

       // String query = "Composite(And(AA.AfId="+AfId+",AA.AuId="+endId+"))";
        String query = "";
        if (AfId.size()>=2) {
            query = "Composite(And(";
            for (int i=1;i<AfId.size();i++){
                query+="Or(";
            }
            query+="AA.AfId="+AfId.get(0)+",";
            for (int i=1;i<AfId.size();i++) {
                query+="AA.AfId="+AfId.get(i)+"),";
            }
            query+="AA.AuId="+endId+"))";
        } else if (AfId.size()==1){
            query = "Composite(And(AA.AfId="+AfId.get(0)+",AA.AuId="+endId+"))";
        }
        String jsonStr = SendApi.send(query, 1000, 0, attribute);
        APIResponse apiResponse = SendApi.analyzeResponse(jsonStr);
        List<Entity> entities = apiResponse.getEntities();
        if (!entities.isEmpty()) {
            for(Entity entity:entities) {
                for (EntityAA entityAA:entity.getEntityAA()) {
                        if (AfId.contains(entityAA.getAA_AfId())) {
                            long[] tem = {beginId, entityAA.getAA_AfId(), endId};
                            AfId.remove(entityAA.getAA_AfId());
                            res.add(tem);
                        }
                }
            }
        }
        return res;
    }

    public List<long[]> AuIdAfIdAuIdId(long beginId, long endId, List<Long> AfId) {
        List<long[]> res = new ArrayList<long[]>();
        if (AfId.isEmpty())
            return  res;

//        String query = "And(Composite(AA.AfId="+AfId+"),Id="+endId+")";
        String query = "";
        if (AfId.size()>=2) {
            query = "And(";
            for (int i=1;i<AfId.size();i++){
                query+="Or(";
            }
            query+="Composite(AA.AfId="+AfId.get(0)+"),";
            for (int i=1;i<AfId.size();i++) {
                query+="Composite(AA.AfId="+AfId.get(i)+")),";
            }
            query+="Id="+endId+")";
        } else if (AfId.size()==1){
            query = "And(Composite(AA.AfId="+AfId.get(0)+"),Id="+endId+")";
        }
        APIResponse apiResponse;
        try {
            String jsonStr = SendApi.send(query, 1000, 0, attribute);
            apiResponse = SendApi.analyzeResponse(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        List<Entity> entities = apiResponse.getEntities();
        for (Entity entity:entities){
            for (EntityAA entityAA:entity.getEntityAA()) {
                if (AfId.contains(entityAA.getAA_AfId())) {
                    long tem[] = {beginId,entityAA.getAA_AfId(),entityAA.getAA_AuId(),endId};
                    res.add(tem);
                }
            }
        }
        return res;
    }

    //list版本的搜索


    // 总策略
    public List<long[]> findAllAuIdAuId(long beginId, long endId) {
        List<Long> afIds = this.getAfIdByAuId(beginId);
        List<long[]> res = new ArrayList<long[]>();
        List<long[]> resAuIdIdAuId = this.AuIdIdAuId(beginId, endId);
        List<long[]> resAuIdAfIdAuId = AuIdAfIdAuId(beginId, endId, afIds);

        List<Entity> entities = getByAuId(beginId);
        List<long[]> resIdIdAuIdtem = new ArrayList<long[]>();

        for (Entity entity :entities) {
            resIdIdAuIdtem.addAll(this.IdIdAuId(entity.getId(),endId,entity));
        }
        List<long[]> resIdIdAuId = new ArrayList<long[]>();
        for (long[] l: resIdIdAuIdtem) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resIdIdAuId.add(tem);
        }
        res.addAll(resAuIdIdAuId);
        res.addAll(resIdIdAuId);
        res.addAll(resAuIdAfIdAuId);
        return  res;
    }

    public List<long[]> findAllIdAuId(long beginId, long endId) {
        List<long[]> res = new ArrayList<long[]>();
        Entity entity = getById(beginId);
        if (entity==null) return res;
        List<long[]> resdIdAuId = IdAuId(beginId, endId, entity);
        List<long[]> resIdIdAuId = IdIdAuId(beginId, endId, entity);
        List<long[]> resIdFIdIdAuId = IdFIdIdAuId(beginId, endId, entity);
        List<long[]> resIdCIdIdAuId = IdCIdIdAuId(beginId, endId, entity);
        List<long[]> resIdJIdIdAuId = IdJIdIdAuId(beginId, endId, entity);
        List<long[]> resIdAuIdIdAuId = IdAuIdIdAuId(beginId, endId, entity);
        List<long[]> resIdAuIdAfIdAuIdTem =  new ArrayList<long[]>();
        List<long[]> resIdIdIdAuIdTem = new ArrayList<long[]>();

        for (EntityAA entityAA:entity.getEntityAA()) {
            List<Long> AfIds = this.getAfIdByAuId(entityAA.getAA_AuId());
            resIdAuIdAfIdAuIdTem.addAll(AuIdAfIdAuId(entityAA.getAA_AuId(), endId, AfIds));

        }
        List<long[]> resIdAuIdAfIdAuId = new ArrayList<long[]>();
            for (long[] l: resIdAuIdAfIdAuIdTem) {
                long[] tem = new long[4];
                tem[0] = beginId;
                System.arraycopy(l,0,tem,1,3);
                resIdAuIdAfIdAuId.add(tem);
            }


        Map<Long,Entity> entityMap = getByRId(entity.getEntityR());
        for (EntityR entityR:entity.getEntityR()) {
            long Id = entityR.getRId();
            Entity entity1 = entityMap.get(Id);
            resIdIdIdAuIdTem.addAll(IdIdAuId(entityR.getRId(),endId,entity1));
        }

        List<long[]> resIdIdIdAuId = new ArrayList<long[]>();
        for (long[] l: resIdIdIdAuIdTem) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resIdIdIdAuId.add(tem);
        }
        res.addAll(resdIdAuId);
        System.out.println("IdAuId " + resdIdAuId.size());
        res.addAll(resIdIdAuId);
        System.out.println("IdIdAuId " + resIdIdAuId.size());
        res.addAll(resIdFIdIdAuId);
        System.out.println("IdFIdIdAuId " + resIdFIdIdAuId.size());
        res.addAll(resIdCIdIdAuId);
        System.out.println("IdCIdIdAuId " + resIdCIdIdAuId.size());
        res.addAll(resIdJIdIdAuId);
        System.out.println("IdJIdIdAuId " + resIdJIdIdAuId.size());
        res.addAll(resIdAuIdIdAuId);
        System.out.println("IdAuIdIdAuId " + resIdAuIdIdAuId.size());
        res.addAll(resIdAuIdAfIdAuId);
        System.out.println("IdAuIdAfIdAuId " + resIdAuIdAfIdAuId.size());
        res.addAll(resIdIdIdAuId);
        System.out.println("IdIdIdAuId " + resIdIdIdAuId.size());
        return res;
    }

    public List<long[]> findAllAuIdId(long beginId, long endId) {
        List<long[]> res = new ArrayList<long[]>();
        List<Long> AfIds = getAfIdByAuId(beginId);
        List<Entity> entities = getByAuId(beginId);
        List<long[]> resAuIdId = AuIdId(beginId, endId);
        List<long[]> resAuIdIdId = AuIdIdId(beginId, endId);
        List<long[]> resAuIdAfIdAuIdId = AuIdAfIdAuIdId(beginId, endId, AfIds);

        List<long[]> resIdFIdId = new ArrayList<long[]>();
        List<long[]> resIdCIdId = new ArrayList<long[]>();
        List<long[]> resIdJIdId = new ArrayList<long[]>();
        List<long[]> resIdAuIdId = new ArrayList<long[]>();
        List<long[]> resIdIdId = new ArrayList<long[]>();
        for (Entity entity:entities) {
            if (entity == null)
                continue;
            resIdFIdId.addAll(IdFIdId(entity.getId(), endId, entity));
            resIdCIdId.addAll(IdCIdId(entity.getId(),endId,entity));
            resIdJIdId.addAll(IdJIdId(entity.getId(),endId,entity));
            resIdAuIdId.addAll(IdAuIdId(entity.getId(),endId,entity));
            resIdIdId.addAll(IdIdId(entity.getId(),endId,entity));
        }

        List<long[]> resAuIdIdFIdId = new ArrayList<long[]>();
        List<long[]> resAuIdIdCIdId = new ArrayList<long[]>();
        List<long[]> resAuIdIdJIdId = new ArrayList<long[]>();
        List<long[]> resAuIdIdAuIdId = new ArrayList<long[]>();
        List<long[]> resAuIdIdIdId = new ArrayList<long[]>();
        for (long[] l: resIdFIdId) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resAuIdIdFIdId.add(tem);
        }
        for (long[] l: resIdCIdId) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resAuIdIdCIdId.add(tem);
        }
        for (long[] l: resIdJIdId) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resAuIdIdJIdId.add(tem);
        }
        for (long[] l: resIdAuIdId) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resAuIdIdAuIdId.add(tem);
        }
        for (long[] l: resIdIdId) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resAuIdIdIdId.add(tem);
        }

        res.addAll(resAuIdId);
        System.out.println("AuIdId " + resAuIdId.size());
        res.addAll(resAuIdIdId);
        System.out.println("AuIdIdId " + resAuIdIdId.size());
        res.addAll(resAuIdAfIdAuIdId);
        System.out.println("AuIdAfIdAuIdId " + resAuIdAfIdAuIdId.size());
        res.addAll(resAuIdIdFIdId);
        System.out.println("AuIdIdFIdId " + resAuIdIdFIdId.size());
        res.addAll(resAuIdIdCIdId);
        System.out.println("AuIdIdCIdId " + resAuIdIdCIdId.size());
        res.addAll(resAuIdIdJIdId);
        System.out.println("AuIdIdJIdId " + resAuIdIdJIdId.size());
        res.addAll(resAuIdIdAuIdId);
        System.out.println("AuIdIdAuIdId " + resAuIdIdAuIdId.size());
        res.addAll(resAuIdIdIdId);
        System.out.println("AuIdIdIdId " + resAuIdIdIdId.size());
        return res;
    }

    public List<long[]> findAllIdId(long beginId, long endId) {
        List<long[]> res = new ArrayList<long[]>();
        Entity entity = getById(beginId);
        if (entity==null) return res;

        Map<Long,Entity> entityMap = getByRId(entity.getEntityR());

        List<long[]> resIdId = IdId(beginId, endId, entity);
        List<long[]> resIdIdId = IdIdId(beginId, endId, entity);
        List<long[]> resIdFIdId = IdFIdId(beginId, endId, entity);
        List<long[]> resIdCIdId = IdCIdId(beginId, endId, entity);
        List<long[]> resIdJIdId = IdJIdId(beginId, endId, entity);
        List<long[]> resIdAuIdId = IdAuIdId(beginId, endId, entity);

        List<long[]> resIdFIdIdId = IdFIdIdId(beginId,endId,entity);
        List<long[]> resIdCIdIdId = IdCIdIdId(beginId,endId,entity);
        List<long[]> resIdJIdIdId = IdJIdIdId(beginId, endId, entity);
        List<long[]> resIdAuIdIdId = IdAuIdIdId(beginId, endId, entity);

        List<long[]> resIdFIdIdTem = new ArrayList<long[]>();
        List<long[]> resIdCIdIdTem = new ArrayList<long[]>();
        List<long[]> resIdJIdIdTem = new ArrayList<long[]>();
        List<long[]> resIdAuIdIdTem = new ArrayList<long[]>();
        List<long[]> resIdIdIdTem = new ArrayList<long[]>();
        for (EntityR entityR:entity.getEntityR()) {
            resIdFIdIdTem.addAll(IdFIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
            resIdCIdIdTem.addAll(IdCIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
            resIdJIdIdTem.addAll(IdJIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
            resIdAuIdIdTem.addAll(IdAuIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
            resIdIdIdTem.addAll(IdIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
        }

        List<long[]> resIdIdFIdId = new ArrayList<long[]>();
        List<long[]> resIdIdCIdId = new ArrayList<long[]>();
        List<long[]> resIdIdJIdId = new ArrayList<long[]>();
        List<long[]> resIdIdAuIdId = new ArrayList<long[]>();
        List<long[]> resIdIdIdId = new ArrayList<long[]>();

        for (long[] l: resIdFIdIdTem) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resIdIdFIdId.add(tem);
        }
        for (long[] l: resIdCIdIdTem) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resIdIdCIdId.add(tem);
        }
        for (long[] l: resIdJIdIdTem) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resIdIdJIdId.add(tem);
        }
        for (long[] l: resIdAuIdIdTem) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l,0,tem,1,3);
            resIdIdAuIdId.add(tem);
        }
        for (long[] l: resIdIdIdTem) {
            long[] tem = new long[4];
            tem[0] = beginId;
            System.arraycopy(l, 0, tem, 1, 3);
            resIdIdIdId.add(tem);
        }

        res.addAll(resIdId);
        System.out.println("resIDID " + resIdId.size());

        res.addAll(resIdIdId);
        System.out.println("resIdIdId " + resIdIdId.size());
        res.addAll(resIdFIdId);
        System.out.println("resIdFIdId " + resIdFIdId.size());
        res.addAll(resIdCIdId);
        System.out.println("resIdCIdId " + resIdCIdId.size());
        res.addAll(resIdJIdId);
        System.out.println("resIdJIdId " + resIdJIdId.size());
        res.addAll(resIdAuIdId);
        System.out.println("resIdAuIdId " + resIdAuIdId.size());

        res.addAll(resIdFIdIdId);
        System.out.println("resIdFIdIdId " + resIdFIdIdId.size());
        res.addAll(resIdCIdIdId);
        System.out.println("resIdCIdIdId " + resIdCIdIdId.size());
        res.addAll(resIdJIdIdId);
        System.out.println("resIdJIdIdId " + resIdJIdIdId.size());
        res.addAll(resIdAuIdIdId);
        System.out.println("resIdAuIdIdId " + resIdAuIdIdId.size());
        res.addAll(resIdIdFIdId);
        System.out.println("resIdIdFIdId " + resIdIdFIdId.size());
        res.addAll(resIdIdCIdId);
        System.out.println("resIdIdCIdId " + resIdIdCIdId.size());
        res.addAll(resIdIdJIdId);
        System.out.println("resIdIdJIdId " + resIdIdJIdId.size());
        res.addAll(resIdIdAuIdId);
        System.out.println("resIdIdAuIdId " + resIdIdAuIdId.size());
        res.addAll(resIdIdIdId);
        System.out.println("resIdIdIdId " + resIdIdIdId.size());

        return res;
    }
    public static void main(String args[]) {
        Strategy strategy = new Strategy();
        Entity entity = strategy.getById(2042486495);
        List<Long> afids = strategy.getAfIdByAuId(2100760765);
        List<long[]> res = strategy.AuIdAfIdAuIdId(2100760765, 2085261163,afids);
        for (long[] l:res) {
            for (long t :l){
                System.out.print(t+" ");
            }
            System.out.println();
        }
        System.out.println(res.size());

    }

    //idid测试 2042486495 2118168041 get
    //ididid测试 2042486495 2118168041 1982896842 get
    //IdFIdId测试 2118168041 166052673 2114535528 get
    //IdCIdId测试 2042486495 1163450153 2141282920 get
    //IdJIdId测试 2010945382 25538012 2151103935 get
    //IdAuIdId测试 2042463931 2044331516 2085261163 get
    //AuIdId测试 2044331516 2085261163 get
    //AuIdId测试 2044331516 2085261163 2170081783 get
    //IdAuId测试 2085261163 2100760765 get
    //AuIdIdAuId测试 2100760765 2044331516 get
    //AuIdAfIdAuId测试 2100760765 1298353152 2044331516 get
    //AuIdAfIdAuIdId测试 2100760765 1298353152 2044331516 2085261163 get
    //IdIdAuId测试 2118168041 2026739782 1969551387 get
    //IdFIdIdId测试 2042486495 185261750 2054810228 1965464747 get
    //IdFIdIdAuId测试 2042486495 185261750 2054810228 1967640570 get
    //IdCIdIdId测试 2042486495 1163450153 2141282920 1666447063 get
    //IdCIdIdAuId测试 2042486495 1163450153 2141282920 1231469998 get
    //IdJIdIdId测试 2118168041 129573059 1992176519 2041280856 get
    //IdJIdIdAuId测试 2118168041 129573059 1992176519 367044581 get
    //IdAuIdIdId测试 2118168041 1969551387 2026739782 1982896842 get
    //IdAuIdIdAuId测试 2118168041 1969551387 2026739782 2120666348 get
}
