package com.udelblue.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.udelblue.entities.Redirect;
import com.udelblue.repositories.RedirectRepository;

@Service
public class RedirectService {

	@Autowired
	private RedirectRepository redirectRepository;

	public Redirect findRedirect(String guid) {
		return redirectRepository.findByGuid(guid);

	}

	public Redirect createRedirect(String url, boolean appendQueryString) {
		Redirect redirect = new Redirect(url, appendQueryString);
		redirect.setGuid(UUID.randomUUID().toString());
		return redirectRepository.save(redirect);
	}

	public Iterable<Redirect> findAll() {
		return redirectRepository.findAll();
	}

	public Redirect save(Redirect redirect) {
		redirect.setGuid(UUID.randomUUID().toString());
		return redirectRepository.save(redirect);
	}

	public Long redirectAllCount() {
		return redirectRepository.count();
	}

	public String process(String id, HttpServletRequest req) {

		// get ipaddress and root domain

		String rootdomain = req.getServerName().replaceAll(".*\\.(?=.*\\.)", "");
		String ipAddress = req.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = req.getRemoteAddr();
		}

		// query parameters

		String queryString = req.getQueryString();

		if (StringUtils.isEmpty(queryString)) {
			queryString = "";
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();

		JsonNode childNode1 = mapper.createObjectNode();
		((ObjectNode) childNode1).put("rootdomain", rootdomain);
		((ObjectNode) childNode1).put("ipaddress", ipAddress);
		((ObjectNode) childNode1).put("formID", id);
		((ObjectNode) childNode1).put("UID", UUID.randomUUID().toString());
		((ObjectNode) childNode1).put("datetime UTC", LocalDateTime.now(ZoneOffset.UTC).toString());
		((ObjectNode) childNode1).put("queryString", queryString);

		((ObjectNode) rootNode).set("_metadata", childNode1);

		String json = "";
		try {
			json = mapper.writeValueAsString(rootNode);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return json;

	}

}
