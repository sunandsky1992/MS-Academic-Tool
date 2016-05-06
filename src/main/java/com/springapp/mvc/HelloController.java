package com.springapp.mvc;

import Path.PathTools;
import Struct.Entity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HelloController {
	@RequestMapping(method = RequestMethod.GET, value = "/getpath")
    @ResponseBody
	public String printWelcome(HttpServletRequest request) {
        PathTools pathTools = new PathTools();
		String id1str = request.getParameter("id1");
		String id2str = request.getParameter("id2");
        long id1 = Long.parseLong(id1str);
        long id2 = Long.parseLong(id2str);
        boolean id1ISAuId = true;
        boolean id2ISAuId = true;
        List<Entity> id1AuIdRes = pathTools.getByAuId(id1);
        List<Entity> id2AuIdRes = pathTools.getByAuId(id2);
        if (id1AuIdRes.isEmpty()) id1ISAuId = false;
        if (id2AuIdRes.isEmpty()) id2ISAuId = false;
        return null;
	}
}