package com.udelblue.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.udelblue.entities.Redirect;

import com.udelblue.repositories.RedirectRepository;
import com.udelblue.services.RedirectService;

@Controller
public class RedirectController {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private RedirectService redirectService;

	@Autowired
	private RedirectRepository redirectRepository;

	@GetMapping("/redirect/{id}")
	public ModelAndView redirect(HttpServletRequest req, @PathVariable("id") String id) {

		String queryString = req.getQueryString();
		Redirect redirect = redirectService.findRedirect(id);

		String url = "";
		if (redirect != null) {
			url = redirect.getUrl();
			if (redirect.isAppendQueryString()) {

				url = url + "?" + queryString;
			}

			if (redirect.isRecordQueryString()) {

				logger.info(redirectService.process(id, req));
				;
			}

			return new ModelAndView(new RedirectView(url));
		} else {// url = "https://news.ycombinator.com" ;
			return new ModelAndView(new RedirectView("../noredirectfound"));

		}
		
	}

	@GetMapping("/editredirect/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Redirect redirect = redirectRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid redirect Id:" + id));
		model.addAttribute("redirect", redirect);
		return "update-redirect";
	}

	@PostMapping("/updateredirect/{id}")
	public String updateRedirect(@PathVariable("id") long id, @Valid Redirect redirect, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			redirect.setId(id);
			return "update-redirect";
		}

		Redirect oldredirect = redirectRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid redirect Id:" + id));
		redirect.setGuid(oldredirect.getGuid());
		redirectRepository.save(redirect);
		return "redirect:../";
	}

	@GetMapping("/addredirect")
	public String addRedirect(Redirect redirect) {
		return "add-redirect";
	}

	@GetMapping("/")
	public String index(Model model) {

        InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String host = "";
   
        host = ip.getHostName();
		model.addAttribute("host", host);
		model.addAttribute("redirects", redirectService.findAll());
		return "indexRedirect";
	}

	@PostMapping("/addredirect")
	public String addRedirect(@Valid Redirect redirect, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-redirect";
		}

		redirectService.save(redirect);
		return "redirect:./";
	}

	@GetMapping("/deleteredirect/{id}")
	public String deleteRedirect(@PathVariable("id") long id, Model model) {
		Redirect redirect = redirectRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid redirect Id:" + id));
		redirectRepository.delete(redirect);
		return "redirect:../";
	}

	@GetMapping("/noredirectfound")
	public String noredirectfound() {
		return "noredirectfound";
	}

}
