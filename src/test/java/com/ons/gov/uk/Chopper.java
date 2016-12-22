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

	public static void main(String[] args) {
		Chopper csv = new Chopper();
		csv.startChopper();
	}

	public String getSplitter(){
	return	config.getSplitter();
	}

	public String getFilepath(){
	return	config.getFilepath();
	}

	public HttpResponse startChopper() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse chopResponse = null;
		HttpPost callChopper = new HttpPost(getSplitter());
		try {
			callChopper.addHeader("content-type", "application/json");
			String entity = "{\"filePath\":\""+getFilepath()+"\"} ";
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

		return chopResponse;

	}

}
