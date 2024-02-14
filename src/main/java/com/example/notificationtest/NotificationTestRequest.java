package com.example.notificationtest;

public record NotificationTestRequest(
	String token,
	String title,
	String body
) {
	public Notification toEntity() {
		return Notification.builder()
			.title(title)
			.body(body)
			.build();
	}
}
