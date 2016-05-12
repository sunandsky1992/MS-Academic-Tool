package MulitiThreadStrategy;

import Strategy.Strategy;
import Struct.Entity;

import java.util.List;

/**
 * Created by ss on 16-5-12.
 */
public class IdJIdIdId extends Thread {
    public List<long[]> res;

    private long beginId;

    private long endId;

    private Entity entity;

    private Strategy strategy;

    public IdJIdIdId(long beginId, long endId, Entity entity, Strategy strategy, List<long[]> res) {
        this.beginId = beginId;
        this.endId = endId;
        this.entity = entity;
        this.strategy = strategy;
        this.res = res;
    }

    public void run() {
        res.addAll(strategy.IdJIdIdId(beginId, endId, entity));
    }
}
