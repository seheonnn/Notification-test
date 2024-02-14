package com.example.notificationtest;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/notification")
@RestController
public class FirebaseTestController {

	private final FirebaseService firebaseService;

	// Body 내용
	// {
	// 	"token": "fcm 토큰",
	// 	"title": "test 제목",
	// 	"body": "test 내용"
	// }

	@PostMapping("/fcm")
	public String testNotification(@RequestBody NotificationTestRequest request) throws IOException {
		firebaseService.sendMessageTo(request.token(), request.title(), request.body());
		return "Notification test is successful !";
	}
}
