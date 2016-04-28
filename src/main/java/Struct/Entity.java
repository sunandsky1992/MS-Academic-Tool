package Struct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 16-4-28.
 */
public class Entity {
    private int logprob;                 //api返回值中有
    private int Id;                     //entity ID     1
    private String Ti;                  //paper title   2
    private int Y;                      //paper year    3
    private Date D;                     //paper date    4
    private int CC;                     //citation count    5

    private List<EntityAA> entityAAs = new ArrayList<EntityAA>();
    private List<EntityC> entityAAC = new ArrayList<EntityC>();
    private List<EntityF> entityAAF = new ArrayList<EntityF>();
    private List<EntityJ> entityAAJ = new ArrayList<EntityJ>();
    private List<EntityR> entityAAR = new ArrayList<EntityR>();
    private List<EntityW> entityAAW = new ArrayList<EntityW>();

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTi() {
        return Ti;
    }

    public void setTi(String ti) {
        Ti = ti;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public Date getD() {
        return D;
    }

    public void setD(Date d) {
        D = d;
    }

    public int getCC() {
        return CC;
    }

    public void setCC(int CC) {
        this.CC = CC;
    }

    public List<EntityAA> getEntityAAs() {
        return entityAAs;
    }

    public void setEntityAAs(List<EntityAA> entityAAs) {
        this.entityAAs = entityAAs;
    }

    public List<EntityC> getEntityAAC() {
        return entityAAC;
    }

    public void setEntityAAC(List<EntityC> entityAAC) {
        this.entityAAC = entityAAC;
    }

    public List<EntityF> getEntityAAF() {
        return entityAAF;
    }

    public void setEntityAAF(List<EntityF> entityAAF) {
        this.entityAAF = entityAAF;
    }

    public List<EntityJ> getEntityAAJ() {
        return entityAAJ;
    }

    public void setEntityAAJ(List<EntityJ> entityAAJ) {
        this.entityAAJ = entityAAJ;
    }

    public List<EntityR> getEntityAAR() {
        return entityAAR;
    }

    public void setEntityAAR(List<EntityR> entityAAR) {
        this.entityAAR = entityAAR;
    }

    public List<EntityW> getEntityAAW() {
        return entityAAW;
    }

    public void setEntityAAW(List<EntityW> entityAAW) {
        this.entityAAW = entityAAW;
    }

    public int getLogprob() {
        return logprob;
    }

    public void setLogprob(int logprob) {
        this.logprob = logprob;
    }
}
