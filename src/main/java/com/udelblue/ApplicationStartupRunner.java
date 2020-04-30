package com.udelblue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.udelblue.entities.Redirect;
import com.udelblue.repositories.RedirectRepository;
import com.udelblue.services.RedirectService;

public class ApplicationStartupRunner implements CommandLineRunner {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private RedirectService redirectService;

	@Override
	public void run(String... args) throws Exception {
		Redirect redirect = redirectService.createRedirect("https://www.youtube.com/watch?edufilter=NULL&v=ub82Xb1C8os",
				true);
		logger.warn("Seed redirect created visit:");
		logger.warn("/redirect/" + redirect.getGuid());

	}
}