package com.kirby.languagetester.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping({ "", "/", "index", "index.html" })
	public String getIndexPage() {
		return "index";
	}

}

