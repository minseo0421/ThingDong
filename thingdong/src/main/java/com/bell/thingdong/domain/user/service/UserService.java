package com.bell.thingdong.domain.user.service;

import java.util.Collections;
import java.util.Random;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bell.thingdong.domain.user.dto.UserRole;
import com.bell.thingdong.domain.user.dto.request.LoginReq;
import com.bell.thingdong.domain.user.dto.request.SignUpReq;
import com.bell.thingdong.domain.user.dto.response.LoginRes;
import com.bell.thingdong.domain.user.dto.response.UserInfoRes;
import com.bell.thingdong.domain.user.entity.User;
import com.bell.thingdong.domain.user.exception.EmailDuplicationException;
import com.bell.thingdong.domain.user.exception.PasswordIsNotMatchedException;
import com.bell.thingdong.domain.user.exception.UserNotFoundException;
import com.bell.thingdong.domain.user.repository.UserRepository;
import com.bell.thingdong.global.config.jwt.JwtTokenProvider;
import com.bell.thingdong.global.config.jwt.TokenInfo;
import com.bell.thingdong.global.redis.RedisRepository;
import com.bell.thingdong.global.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String[] nickHead = {
		"강한", "큰", "작은", "용감한", "명랑한",
		"빠른", "멋진", "예쁜", "행운의", "똑똑한"
	};
	private static final String[] nickBody = {
		"사자", "호랑이", "기린", "팬더", "원숭이",
		"코알라", "팽귄", "호랭이", "토끼", "고릴라"
	};
	private final RedisRepository redisRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;

	public void logout(HttpServletRequest request, HttpServletResponse response, String email) {
		// redis 토큰 삭제
		redisRepository.deleteValues("RT:" + email);
		// 쿠키 삭제
		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
	}

	public LoginRes login(LoginReq loginReq, HttpServletRequest request, HttpServletResponse response) {
		//비밀번호 검증
		User user = userRepository.findByEmail(loginReq.getEmail()).orElseThrow(UserNotFoundException::new);
		if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
			throw new PasswordIsNotMatchedException();
		}
		// Authentication 발급
		UsernamePasswordAuthenticationToken authenticationToken = loginReq.toAuthentication();
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenInfo tokenInfo = jwtTokenProvider.createToken(authentication);
		// 쿠키 값 갱신
		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
		CookieUtil.addCookie(response, REFRESH_TOKEN, tokenInfo.getRefreshToken(),
			JwtTokenProvider.getRefreshTokenExpireTimeCookie());

		// RT 발급
		redisRepository.setValues("RT:" + authentication.getName(), tokenInfo.getRefreshToken(),
			tokenInfo.getExpireTime());
		return LoginRes.builder().accessToken(tokenInfo.getAccessToken()).build();
	}

	@Transactional
	public void signUp(SignUpReq signUpReq) {
		checkDuplicatedEmail(signUpReq.getEmail());

		String nickname = signUpReq.getNickname();
		if (nickname == null || nickname.isEmpty()) {
			nickname = generateRandomNickName();
		}

		User build = User.builder()
			.email(signUpReq.getEmail())
			.password(passwordEncoder.encode(signUpReq.getPassword()))
			.nickname(nickname)
			.roles(Collections.singletonList(UserRole.ROLE_USER.name()))
			.build();

		userRepository.save(build);
	}

	public UserInfoRes readUserInfo(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return UserInfoRes.builder()
			.userId(user.getEmail())
			.nickName(user.getNickname())
			.PAToken(user.getPAToken())
			.thingAmount(user.getThingAmount())
			.build();
	}

	private String generateRandomNickName() {
		Random random = new Random();
		return nickHead[random.nextInt(nickHead.length)] + nickBody[random.nextInt(nickBody.length)];
	}

	public void checkDuplicatedEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new EmailDuplicationException();
		}
	}
}