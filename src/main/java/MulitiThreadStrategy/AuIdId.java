package MulitiThreadStrategy;

import Strategy.Strategy;
import Struct.Entity;

import java.util.List;

/**
 * Created by ss on 16-5-13.
 */
public class AuIdId implements Runnable {
    public List<long[]> res;

    private long beginId;

    private long endId;

    private Entity entity;

    private Strategy strategy;

    public AuIdId(long beginId, long endId, Strategy strategy, List<long[]> res) {
        this.beginId = beginId;
        this.endId = endId;
        this.strategy = strategy;
        this.res = res;
    }

    public void run() {
        res.addAll(strategy.AuIdId(beginId, endId));
    }
}
