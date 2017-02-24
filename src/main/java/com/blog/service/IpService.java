package com.blog.service;

import java.net.InetAddress;
import java.net.URI;

import org.springframework.stereotype.Service;

import com.blog.comm.annotation.CacheWrite;
import com.blog.comm.avafinal.CacheFinal;

@Service
public class IpService {

	@CacheWrite(key=CacheFinal.SHELL_IP_CACHE ,validTime=60*60*24,fields="url")
	public  String getIp(String url){
		try {
			URI uri = new URI(url);
			String domain=uri.getHost();
			InetAddress address = InetAddress.getByName(domain);
			return address.getHostAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
