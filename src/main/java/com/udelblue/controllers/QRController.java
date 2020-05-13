package com.udelblue.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.udelblue.entities.Redirect;

import com.udelblue.repositories.RedirectRepository;
import com.udelblue.services.QRService;
import com.udelblue.services.RedirectService;

@Controller
public class QRController {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private RedirectService redirectService;
	
	@Autowired
	private RedirectRepository redirectRepository;

	@Autowired
	private QRService qrService;
	
	@Value("${host:})")
	private String host;

	@GetMapping("/qr/{guid}")
	public ResponseEntity<byte[]> QRredirect(HttpServletRequest req, @PathVariable("guid") String guid)
			throws WriterException, IOException {

	       InetAddress ip = null;
			try {
				ip = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
			if(StringUtils.isAnyEmpty(host))
			{
	        host = ip.getHostName();
			}
			
		System.out.println(guid);
	    Redirect redirect = redirectService.findRedirect(guid.trim());
	    
		byte[] bytes = null;
		BitMatrix matrix = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		String url = "";
		if (redirect != null) {
			url = host + "/redirect/" + redirect.getGuid();
			matrix = qrService.generateQRCodeImage( url , 300, 300);

		} else {
			url = host + "/noredirectfound";
			matrix = qrService.generateQRCodeImage(url , 300, 300);
		}

		MatrixToImageWriter.writeToStream(matrix, "png", stream);
		stream.flush();

		bytes = stream.toByteArray();
		stream.close();

		// Set headers
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);

	}

	@GetMapping("/qrcode/{id}")
	public String qrcode(@PathVariable("id") String id, Model model) {
		model.addAttribute("id", id);
		return "qrcode";
	}

}
