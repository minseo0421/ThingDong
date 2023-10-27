package com.bell.thingdong.domain.room.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bell.thingdong.domain.room.dto.response.UserRoomRes;
import com.bell.thingdong.domain.room.service.UserRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "room", description = "방 관련 컨트롤러")
public class UserRoomController {
	private final UserRoomService userRoomService;

	@Operation(summary = "방 추가", description = "자신에게 새로운 방을 추가한다.")
	@PostMapping
	public ResponseEntity<?> createRoom(Principal principal) {
		String email = principal.getName();

		userRoomService.createRoom(email);

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "나의 방 불러오기", description = "토큰으로 나의 첫번째 방을 불러온다.")
	@GetMapping
	public ResponseEntity<UserRoomRes> loadMyRoom(Principal principal) {
		String email = principal.getName();

		UserRoomRes userRoomRes = userRoomService.loadRoom(email, null, null);

		return ResponseEntity.ok(userRoomRes);
	}

	@Operation(summary = "띵구 방 불러오기", description = "userID로 띵구의 첫번째 방을 불러온다.")
	@GetMapping("/thinggus")
	public ResponseEntity<UserRoomRes> loadThingguRoom(@RequestParam Long userId) {
		UserRoomRes userRoomRes = userRoomService.loadRoom(null, userId, null);

		return ResponseEntity.ok(userRoomRes);
	}

	@Operation(summary = "방 불러오기", description = "roomID로 다음이나 이전 방을 불러온다.")
	@GetMapping("/detail")
	public ResponseEntity<UserRoomRes> loadNextRoom(@RequestParam Long roomId) {
		UserRoomRes userRoomRes = userRoomService.loadRoom(null, null, roomId);

		return ResponseEntity.ok(userRoomRes);
	}
}
