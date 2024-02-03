package com.example.alerttest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

	@Value("${firebase.fcmUrl}")
	private String fcmUrl;

	@Value("${firebase.firebaseConfigPath}")
	private String firebaseConfigPath;

	@Value("${firebase.scope}")
	private String scope;
	private final ObjectMapper objectMapper;
	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
		String message = makeMessage(targetToken, title, body);
		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
		Request request = new Request.Builder()
			.url(fcmUrl)
			.post(requestBody)
			.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
			.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
			.build();
		Response response = client.newCall(request)
			.execute();
		log.info(response.body().string());
	}
	private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
		FcmMessage fcmMessage = FcmMessage.builder()
			.message(FcmMessage.Message.builder()
				.token(targetToken)
				.notification(FcmMessage.Notification.builder()
					.title(title)
					.body(body)
					.image(null)
					.build()
				)
				.build()
			)
			.validateOnly(false)
			.build();
		return objectMapper.writeValueAsString(fcmMessage);
	}
	private String getAccessToken() throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath)
			.getInputStream()).createScoped(List.of(scope));
		googleCredentials.refreshIfExpired();
		log.info("AccessToken : {}", googleCredentials.getAccessToken().getTokenValue());
		return googleCredentials.getAccessToken().getTokenValue();
	}
}
