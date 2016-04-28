package Struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 16-4-28.
 */
public class APIResponse {
    private List<Entity> entities = new ArrayList<Entity>();
    private String expr;
    private int logprob;
    private int Id;

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public int getLogprob() {
        return logprob;
    }

    public void setLogprob(int logprob) {
        this.logprob = logprob;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
