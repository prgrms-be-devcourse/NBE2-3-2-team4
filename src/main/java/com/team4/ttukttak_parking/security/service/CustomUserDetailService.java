package com.team4.ttukttak_parking.security.service;


import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.DuplicateAccountException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import com.team4.ttukttak_parking.security.dto.SignupRequestDto;
import com.team4.ttukttak_parking.security.dto.TokenDto;
import com.team4.ttukttak_parking.security.exception.JWTCustomException;
import com.team4.ttukttak_parking.security.util.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * member 관련 인증과 인가 담당하는 서비스 계층
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authManagerBuilder;

    private static final Map<MemberRoles,List<GrantedAuthority>> authorityMap = new HashMap<>();

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return getUserDetails(findUserByEmail(email));
    }

    private UserDetails getUserDetails(Member member){
        return new org.springframework.security.core.userdetails.User(member.getEmail(),null,authorityMap.get(member.getRole()));
    }

    public Member findUserByEmail(final String userEmail){
        return memberRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
    public TokenDto issueAllTokens(Authentication authentication){
        return tokenProvider.createAllToken(authentication);
    }

    //accessToken, refreshToken update
    public TokenDto updateToken(String refreshToken){

        try {
            String email =  tokenProvider.findUser(refreshToken);
            Member member = findUserByEmail(email);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(member.getRole().name()));

            User user = new User(email,"password",authorities);
            // Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            return issueAllTokens(authentication);
        }
        //refresh token parsing중 에러 발생(유효하지 않은 토큰인 경우) 처리
        catch (ExpiredJwtException e){
            log.info("refresh token 만료");
            throw new JWTCustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }catch(UnsupportedJwtException|MalformedJwtException|SignatureException| IllegalArgumentException e){
            log.info("유효하지 않은 refresh token");
            throw new JWTCustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public TokenDto login(String email, String password){
        Member member =  memberRepository.findByEmail(email).orElseThrow(()->new NotFoundException(ErrorCode.USER_NOT_FOUND));
        if(passwordEncoder.matches(password,member.getPassword())){

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(member.getRole().name()));

            User user = new User(email,"password",authorities);

            // Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(user,null, authorities);

            TokenDto tokenDto = issueAllTokens(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return tokenDto;
        }
        else{
            throw new NotFoundException(ErrorCode.INVALID_PASSWORD);
        }
    }


    //일반 user 회원가입
    @Transactional
    public void join(SignupRequestDto signupRequestDto) {
        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateAccountException(ErrorCode.USER_ALREADY_EXIST.getMessage());
        }

        Member member = Member.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .name(signupRequestDto.getName())
                .contact(signupRequestDto.getContact())
                .role(MemberRoles.ROLE_USER)
                .loginType(LoginTypes.BASIC)
                .build();

        memberRepository.save(member);
    }



}
