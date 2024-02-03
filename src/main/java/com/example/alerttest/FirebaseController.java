package com.example.alerttest;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/alert/")
@RestController
public class FirebaseController {
	private final FirebaseCloudMessageService firebaseCloudMessageService;
	@PostMapping("fcm")
	public String fcmTest(@RequestBody RequestDto requestDto) throws IOException {
		firebaseCloudMessageService.sendMessageTo(requestDto.getToken(), requestDto.getTitle(), requestDto.getContent());
		return "OK";
	}
}
