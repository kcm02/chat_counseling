package com.project.securelogin.global.oauth.handler;

import com.project.securelogin.global.oauth.domain.CustomOAuth2User;
import com.project.securelogin.user.domain.CustomUserDetails;
import com.project.securelogin.user.domain.User;
import com.project.securelogin.global.dto.UserJsonResponse;
import com.project.securelogin.user.dto.UserResponseDTO;
import com.project.securelogin.global.jwt.JwtTokenProvider;
import com.project.securelogin.global.oauth.userInfo.OAuth2UserInfo;
import com.project.securelogin.global.oauth.userInfo.OAuth2UserInfoFactory;
import com.project.securelogin.global.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // CustomOAuth2User 객체를 생성
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String socialType = customOAuth2User.getSocialType();

        // OAuth2UserInfo를 사용하여 사용자 정보를 가져옴
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, customOAuth2User.getAttributes());

        User user = oAuth2UserService.getUserByOAuth2UserInfo(userInfo, socialType);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // CustomUserDetails를 Authentication으로 변환
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = jwtTokenProvider.createToken(auth);
        String refreshToken = jwtTokenProvider.createRefreshToken(auth);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        UserResponseDTO userResponseDTO = UserResponseDTO.from(user); // UserResponseDTO로 변환
        UserJsonResponse userJsonResponse = new UserJsonResponse(
                HttpServletResponse.SC_OK,
                "로그인 성공",
                userResponseDTO
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(userJsonResponse.toJson());
    }
}
