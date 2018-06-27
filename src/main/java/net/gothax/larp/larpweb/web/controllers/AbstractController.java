package net.gothax.larp.larpweb.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstractController {
	private static Logger log = Logger.getLogger(AbstractController.class);

	protected String getCurrentUserLogin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails details = (UserDetails) auth.getPrincipal();
		String login = details.getUsername();
		log.debug("Get current user[" + login + "]");

		return login;
	}
}
