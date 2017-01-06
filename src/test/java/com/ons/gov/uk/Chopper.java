package com.ons.gov.uk;


import com.ons.gov.uk.core.Config;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class Chopper {


	Config config = new Config();
	HttpClient httpClient = HttpClientBuilder.create().build();
	HttpResponse chopResponse = null;
	HttpPost callChopper = new HttpPost(getSplitter());

	public Chopper() {
		callChopper.addHeader("content-type", "application/json");
	}

	public String getSplitter(){
	return	config.getSplitter();
	}

	public String getFilepath(){
	return	config.getFilepath();
	}

	public HttpResponse startChopper() {
		callChop(getFilepath());
		return chopResponse;
	}

	public void callChop(String fileName) {
		try {
			String entity = "{\"filePath\":\"" + fileName + "\"} ";
			StringEntity paramToSend = new StringEntity(entity);
			callChopper.setEntity(paramToSend);
		} catch (UnsupportedEncodingException encodingError) {
			encodingError.printStackTrace();
		}
		try {
			chopResponse = httpClient.execute(callChopper);
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}


}
