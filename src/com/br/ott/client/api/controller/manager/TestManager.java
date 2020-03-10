package com.br.ott.client.api.controller.manager;

import com.br.ott.common.exception.OTTException;

public class TestManager extends BaseManager {

	@Override
	public String execute() throws OTTException {
		String name = request.getParameter("name");
		System.out.println(name);
		return "{\"hello\":123}";
	}

	

	

}
