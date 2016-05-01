package Struct;

/**
 * Created by ss on 16-4-28.
 */
public class EntityAA {
    private String AA_AuN = "";              //author name       6
    private long AA_AuId = 0l;                //author ID         7
    private String AA_AfN = "";              //author affiliation name   8
    private long AA_AfId = 0l;                //author affiliation ID     9

    public String getAA_AuN() {
        return AA_AuN;
    }

    public void setAA_AuN(String AA_AuN) {
        this.AA_AuN = AA_AuN;
    }

    public long getAA_AuId() {
        return AA_AuId;
    }

    public void setAA_AuId(long AA_AuId) {
        this.AA_AuId = AA_AuId;
    }

    public String getAA_AfN() {
        return AA_AfN;
    }

    public void setAA_AfN(String AA_AfN) {
        this.AA_AfN = AA_AfN;
    }

    public long getAA_AfId() {
        return AA_AfId;
    }

    public void setAA_AfId(long AA_AfId) {
        this.AA_AfId = AA_AfId;
    }
}
