package MulitiThreadStrategy;

import Strategy.Strategy;
import Struct.Entity;
import Struct.EntityAA;
import Struct.EntityR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ss on 16-5-13.
 */
public class MultiThreadStrategy {
    public List<long[]> findAllAuIdAuId(long beginId, long endId) {
        Strategy strategy = new Strategy();
        List<Long> afIds = strategy.getAfIdByAuId(beginId);
        List<long[]> res = Collections.synchronizedList(new ArrayList<long[]>());

        ExecutorService executor = Executors.newFixedThreadPool(100);
        //List<long[]> resAuIdIdAuId = this.AuIdIdAuId(beginId, endId);
        executor.execute(new AuIdIdAuId(beginId, endId, strategy, -1, res));

        //List<long[]> resAuIdAfIdAuId = AuIdAfIdAuId(beginId, endId, afIds);
        executor.execute(new AuIdAfIdAuId(beginId, endId, strategy, -1, res));

        List<Entity> entities = strategy.getByAuId(beginId);
        List<long[]> resIdIdAuIdtem = new ArrayList<long[]>();

        for (Entity entity :entities) {
            //resIdIdAuIdtem.addAll(this.IdIdAuId(entity.getId(),endId,entity));
            executor.execute(new IdIdAuId(entity.getId(), endId, entity, strategy, beginId, res));
        }
        executor.shutdown();
        while(!executor.isTerminated());
        return  res;
    }

    public List<long[]> findAllIdAuId(long beginId, long endId) {
        List<long[]> res = Collections.synchronizedList(new ArrayList<long[]>());
        Strategy strategy = new Strategy();
        Entity entity = strategy.getById(beginId);
        if (entity==null) return res;
        ExecutorService executor = Executors.newFixedThreadPool(100);

        //List<long[]> resdIdAuId = IdAuId(beginId, endId, entity);
        executor.execute(new IdAuId(beginId, endId, strategy, entity, res));

        //List<long[]> resIdIdAuId = IdIdAuId(beginId, endId, entity);
        executor.execute(new IdIdAuId(beginId, endId, entity, strategy,-1,res));

        //List<long[]> resIdFIdIdAuId = IdFIdIdAuId(beginId, endId, entity);
        executor.execute(new IdFIdIdAuId(beginId,endId,entity,strategy,res));

        //List<long[]> resIdCIdIdAuId = IdCIdIdAuId(beginId, endId, entity);
        executor.execute(new IdCIdIdAuId(beginId,endId,entity,strategy,res));

        //List<long[]> resIdJIdIdAuId = IdJIdIdAuId(beginId, endId, entity);
        executor.execute(new IdJIdIdAuId(beginId,endId,entity,strategy,res));

        //List<long[]> resIdAuIdIdAuId = IdAuIdIdAuId(beginId, endId, entity);
        executor.execute(new IdAuIdIdAuId(beginId,endId,entity,strategy,res));

        for (EntityAA entityAA:entity.getEntityAA()) {
            //List<Long> AfIds = strategy.getAfIdByAuId(entityAA.getAA_AuId());
            //resIdAuIdAfIdAuIdTem.addAll(AuIdAfIdAuId(entityAA.getAA_AuId(), endId, AfIds));
            executor.execute(new AuIdAfIdAuId(entityAA.getAA_AuId(),endId,strategy,beginId,res));

        }


        Map<Long,Entity> entityMap = strategy.getByRId(entity.getEntityR());
        for (EntityR entityR:entity.getEntityR()) {
            long Id = entityR.getRId();
            Entity entity1 = entityMap.get(Id);
            //resIdIdIdAuIdTem.addAll(IdIdAuId(entityR.getRId(),endId,entity1));
            executor.execute(new IdIdAuId(entityR.getRId(), endId, entity1, strategy, beginId, res));
        }

        executor.shutdown();
        while(!executor.isTerminated());
        return res;
    }

    public List<long[]> findAllAuIdId(long beginId, long endId) {
        List<long[]> res = Collections.synchronizedList(new ArrayList<long[]>());
        Strategy strategy = new Strategy();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Entity> entities = strategy.getByAuId(beginId);
        //List<long[]> resAuIdId = AuIdId(beginId, endId);
        executor.execute(new AuIdId(beginId,endId,strategy,res));

        //List<long[]> resAuIdIdId = AuIdIdId(beginId, endId);
        executor.execute(new AuIdIdId(beginId,endId,strategy,-1,res));

        //List<long[]> resAuIdAfIdAuIdId = AuIdAfIdAuIdId(beginId, endId, AfIds);
        executor.execute(new AuIdAfIdAuIdId(beginId,endId,strategy,res));

        for (Entity entity:entities) {
            if (entity == null)
                continue;
            //resIdFIdId.addAll(IdFIdId(entity.getId(), endId, entity));
            executor.execute(new IdFIdId(entity.getId(), endId, entity, strategy, beginId, res));

            //resIdCIdId.addAll(IdCIdId(entity.getId(),endId,entity));
            executor.execute(new IdCIdId(entity.getId(),endId,entity,strategy,beginId,res));

            //resIdJIdId.addAll(IdJIdId(entity.getId(),endId,entity));
            executor.execute(new IdJIdId(entity.getId(),endId,entity,strategy,beginId,res));

            //resIdAuIdId.addAll(IdAuIdId(entity.getId(),endId,entity));
            executor.execute(new IdAuIdId(entity.getId(), endId, entity, strategy, beginId, res));

            //resIdIdId.addAll(IdIdId(entity.getId(),endId,entity));
            executor.execute(new IdIdId(entity.getId(), endId, entity, strategy, beginId, res));

        }

        executor.shutdown();
        while(!executor.isTerminated());
        return res;
    }

    public List<long[]> findAllIdId(long beginId, long endId) {
        List<long[]> res = Collections.synchronizedList(new ArrayList<long[]>());
        Strategy strategy = new Strategy();
        ExecutorService executor = Executors.newFixedThreadPool(100);

        Entity entity = strategy.getById(beginId);
        if (entity==null) return res;

        Map<Long,Entity> entityMap = strategy.getByRId(entity.getEntityR());

        //List<long[]> resIdId = IdId(beginId, endId, entity);
        executor.execute(new IdId(beginId,endId,entity,strategy,res));

        //List<long[]> resIdIdId = IdIdId(beginId, endId, entity);
        executor.execute(new IdIdId(beginId,endId,entity,strategy,-1,res));

        //List<long[]> resIdFIdId = IdFIdId(beginId, endId, entity);
        executor.execute(new IdFIdId(beginId,endId,entity,strategy,-1,res));

        //List<long[]> resIdCIdId = IdCIdId(beginId, endId, entity);
        executor.execute(new IdCIdId(beginId,endId,entity,strategy,-1,res));

        //List<long[]> resIdJIdId = IdJIdId(beginId, endId, entity);
        executor.execute(new IdJIdId(beginId,endId,entity,strategy,-1,res));

        //List<long[]> resIdAuIdId = IdAuIdId(beginId, endId, entity);
        executor.execute(new IdAuIdId(beginId,endId,entity,strategy,-1,res));

        //List<long[]> resIdFIdIdId = IdFIdIdId(beginId,endId,entity);
        executor.execute(new IdFIdIdId(beginId,endId,entity,strategy,res));

        //List<long[]> resIdCIdIdId = IdCIdIdId(beginId,endId,entity);
        executor.execute(new IdCIdIdId(beginId,endId,entity,strategy,res));

        //List<long[]> resIdJIdIdId = IdJIdIdId(beginId, endId, entity);
        executor.execute(new IdJIdIdId(beginId,endId,entity,strategy,res));

        //List<long[]> resIdAuIdIdId = IdAuIdIdId(beginId, endId, entity);
        executor.execute(new IdAuIdIdId(beginId,endId,entity,strategy,res));


        for (EntityR entityR:entity.getEntityR()) {
           // resIdFIdIdTem.addAll(IdFIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
            executor.execute(new IdFIdId(entityR.getRId(), endId, entityMap.get(entityR.getRId()), strategy, beginId, res));

           // resIdCIdIdTem.addAll(IdCIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
            executor.execute(new IdCIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId()),strategy,beginId,res));

            // resIdJIdIdTem.addAll(IdJIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId())));
            executor.execute(new IdJIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId()),strategy,beginId,res));

            // resIdAuIdIdTem.addAll(IdAuIdId(entityR.getRId(), endId, entityMap.get(entityR.getRId())));
            executor.execute(new IdAuIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId()),strategy,beginId,res));

            // resIdIdIdTem.addAll(IdIdId(entityR.getRId(), endId, entityMap.get(entityR.getRId())));
            executor.execute(new IdIdId(entityR.getRId(),endId,entityMap.get(entityR.getRId()),strategy,beginId,res));
        }
        executor.shutdown();
        while(!executor.isTerminated());
        return res;
    }
}
