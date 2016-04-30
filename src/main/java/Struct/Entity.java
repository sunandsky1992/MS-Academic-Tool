package Struct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ss on 16-4-28.
 */
public class Entity {
    private double logprob = 0;                 //api返回值中有
    private int Id = 0;                     //entity ID     1
    private String Ti = "";                  //paper title   2
    private int Y = 0;                      //paper year    3
    private Date D = null;                     //paper date    4
    private int CC = 0;                     //citation count    5

    private List<EntityAA> entityAA = new ArrayList<EntityAA>();
    private EntityC entityC = new EntityC();
    private List<EntityF> entityF = new ArrayList<EntityF>();
    private EntityJ entityJ = new EntityJ();
    private List<EntityR> entityR = new ArrayList<EntityR>();
    private List<EntityW> entityW = new ArrayList<EntityW>();

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

    public List<EntityAA> getEntityAA() {
        return entityAA;
    }

    public void setEntityAA(List<EntityAA> entityAA) {
        this.entityAA = entityAA;
    }

    public EntityC getEntityC() {
        return entityC;
    }

    public void setEntityC(EntityC entityC) {
        this.entityC = entityC;
    }

    public List<EntityF> getEntityF() {
        return entityF;
    }

    public void setEntityF(List<EntityF> entityF) {
        this.entityF = entityF;
    }

    public EntityJ getEntityJ() {
        return entityJ;
    }

    public void setEntityJ(EntityJ entityJ) {
        this.entityJ = entityJ;
    }

    public List<EntityR> getEntityR() {
        return entityR;
    }

    public void setEntityR(List<EntityR> entityR) {
        this.entityR = entityR;
    }

    public List<EntityW> getEntityW() {
        return entityW;
    }

    public void setEntityW(List<EntityW> entityW) {
        this.entityW = entityW;
    }

    public double getLogprob() {
        return logprob;
    }

    public void setLogprob(double logprob) {
        this.logprob = logprob;
    }
}
