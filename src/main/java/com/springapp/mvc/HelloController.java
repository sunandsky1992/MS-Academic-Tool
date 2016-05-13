package com.springapp.mvc;

import MulitiThreadStrategy.MultiThreadStrategy;
import Path.PathTools;
import Strategy.Strategy;
import Struct.Entity;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {
	@RequestMapping(method = RequestMethod.GET, value = "/getpath")
    @ResponseBody
	public String printWelcome(HttpServletRequest request) {
        PathTools pathTools = new PathTools();
		String id1str = request.getParameter("id1");
		String id2str = request.getParameter("id2");
        System.out.println(id1str+" "+id2str);
        long id1 = Long.parseLong(id1str);
        long id2 = Long.parseLong(id2str);
        boolean id1ISAuId = true;
        boolean id2ISAuId = true;
        List<Entity> id1AuIdRes = pathTools.getByAuId(id1);
        List<Entity> id2AuIdRes = pathTools.getByAuId(id2);
        if (id1AuIdRes.isEmpty()) id1ISAuId = false;
        if (id2AuIdRes.isEmpty()) id2ISAuId = false;
        System.out.println("id1= "+id1 +"  id2= "+id2);

        Strategy strategy = new Strategy();
        List<long[]> res = new ArrayList<long[]>();
        if (id1ISAuId && id2ISAuId) {
            System.out.println("AUIDAUID");
            res = strategy.findAllAuIdAuId(id1,id2);
        } else if (id1ISAuId && !id2ISAuId) {
            System.out.println("AuIdId");
            res = strategy.findAllAuIdId(id1,id2);
        } else if (!id1ISAuId && id2ISAuId){
            System.out.println("IDAUID");
            res = strategy.findAllIdAuId(id1,id2);
        } else if (!id1ISAuId && !id2ISAuId) {
            System.out.println("IDID");
            res = strategy.findAllIdId(id1,id2);
        }

        System.out.println(res.size());

        JSONArray jsonArray = new JSONArray();
        for (long[] larr:res){
            JSONArray array = new JSONArray();
            for (long l:larr) {
                array.put(l);
            }
            jsonArray.put(array);
        }
        return jsonArray.toString();
	}

    @RequestMapping(method = RequestMethod.GET, value = "/getpathmulti")
    @ResponseBody
    public String printWelcome2(HttpServletRequest request) {
        PathTools pathTools = new PathTools();
        String id1str = request.getParameter("id1");
        String id2str = request.getParameter("id2");
        System.out.println(id1str+" "+id2str);
        long id1 = Long.parseLong(id1str);
        long id2 = Long.parseLong(id2str);
        boolean id1ISAuId = true;
        boolean id2ISAuId = true;
        List<Entity> id1AuIdRes = pathTools.getByAuId(id1);
        List<Entity> id2AuIdRes = pathTools.getByAuId(id2);
        if (id1AuIdRes.isEmpty()) id1ISAuId = false;
        if (id2AuIdRes.isEmpty()) id2ISAuId = false;
        System.out.println("id1= "+id1 +"  id2= "+id2);

        MultiThreadStrategy strategy = new MultiThreadStrategy();
        List<long[]> res = new ArrayList<long[]>();
        if (id1ISAuId && id2ISAuId) {
            System.out.println("AUIDAUID");
            res = strategy.findAllAuIdAuId(id1,id2);
        } else if (id1ISAuId && !id2ISAuId) {
            System.out.println("AuIdId");
            res = strategy.findAllAuIdId(id1,id2);
        } else if (!id1ISAuId && id2ISAuId){
            System.out.println("IDAUID");
            res = strategy.findAllIdAuId(id1,id2);
        } else if (!id1ISAuId && !id2ISAuId) {
            System.out.println("IDID");
            res = strategy.findAllIdId(id1,id2);
        }

        System.out.println(res.size());

        JSONArray jsonArray = new JSONArray();
        for (long[] larr:res){
            JSONArray array = new JSONArray();
            for (long l:larr) {
                array.put(l);
            }
            jsonArray.put(array);
        }
        return jsonArray.toString();
    }
}