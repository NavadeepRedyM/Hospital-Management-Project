package com.cg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

	@GetMapping("/redirect")
	public String redirect(Authentication auth) {

		String role = auth.getAuthorities().iterator().next().getAuthority();

		if (role.equals("ROLE_ADMIN"))
			return "redirect:/admin/dashboard";
		else if (role.equals("ROLE_DOCTOR"))
			return "redirect:/doctor/dashboard";
		else
			return "redirect:/patient/dashboard";
	}
}