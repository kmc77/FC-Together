package com.project.together.config.oauth;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.config.oauth.provider.GoogleUserInfo;
import com.project.together.config.oauth.provider.NaverUserInfo;
import com.project.together.config.oauth.provider.OAuth2UserInfo;
import com.project.together.domain.User;
import com.project.together.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserMapper userMapper;

    //OAuth2 userRequest 데이터 후 처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccess Token : " + userRequest.getAccessToken().getTokenValue());


        OAuth2User oauth2User = super.loadUser((userRequest));
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필 받아준다.
        System.out.println("getAttributes: " + oauth2User.getAttributes());

        //회원가입 강제 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        String userPhone = null; // 사용자 전화번호를 저장할 변수
        String userBirth = null; // 사용자 생일을 저장할 변수
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");


            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            Map<String, Object> attributes = (Map<String, Object>) oauth2User.getAttributes().get("response");
            oAuth2UserInfo = new NaverUserInfo(attributes);
            userPhone = (String) attributes.get("mobile"); // 네이버 로그인의 경우, mobile 값을 추출
            userBirth = (String) attributes.get("birthday"); // 네이버 로그인의 경우, birthday 값을 추출
        } else {
            System.out.println("투게더는 구글과 네이버만 지원합니다.");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";
        String userRealName = oAuth2UserInfo.getName(); // 사용자의 실제 이름을 가져옵니다.


        User userE = userMapper.findByUsername(username);

        if (userE == null) {
            System.out.println("소셜 로그인 최초입장");
            userE = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .roles(role)
                    .provider(provider)
                    .providerId(providerId)
                    .user_real_name(userRealName) // 사용자의 실제 이름 저장.
                    .user_phone(userPhone)
                    .user_birth(userBirth)
                    .build();
            userMapper.joinUser(userE);
        } else {
            System.out.println("로그인을 이미 한적이 있습니다. 당신은 자동 회원가입이 되어 있습니다.");
        }

        return new PrincipalDetails(userE, oauth2User.getAttributes());
    }

}
