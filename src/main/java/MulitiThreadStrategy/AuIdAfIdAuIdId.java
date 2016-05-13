package MulitiThreadStrategy;

import Strategy.Strategy;
import Struct.Entity;

import java.util.List;

/**
 * Created by ss on 16-5-13.
 */
public class AuIdAfIdAuIdId implements Runnable{
    public List<long[]> res;

    private long beginId;

    private long endId;

    private Strategy strategy;

    public AuIdAfIdAuIdId(long beginId, long endId, Strategy strategy, List<long[]> res) {
        this.beginId = beginId;
        this.endId = endId;
        this.strategy = strategy;
        this.res = res;
    }

    public void run() {
        List<Long> afids = strategy.getAfIdByAuId(beginId);
        res.addAll(strategy.AuIdAfIdAuIdId(beginId, endId,afids));
    }
}
