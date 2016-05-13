package MulitiThreadStrategy;

import Strategy.Strategy;
import Struct.Entity;

import java.util.List;

/**
 * Created by ss on 16-5-13.
 */
public class AuIdIdId implements Runnable {
    public List<long[]> res;

    private long beginId;

    private long endId;

    private Entity entity;

    private Strategy strategy;

    private long preId;

    public AuIdIdId(long beginId, long endId, Strategy strategy, long preId, List<long[]> res) {
        this.beginId = beginId;
        this.endId = endId;
        this.strategy = strategy;
        this.preId = preId;
        this.res = res;
    }

    public void run() {
        if (preId==-1 || preId==0)
            res.addAll(strategy.AuIdIdId(beginId, endId));
        else {
            List<long[]> tem = strategy.AuIdIdId(beginId, endId);
            for (long[] l :tem) {
                long[] teml = new long[4];
                teml[0] = preId;
                System.arraycopy(l,0,teml,1,3);
                res.add(teml);
            }
        }
    }
}
