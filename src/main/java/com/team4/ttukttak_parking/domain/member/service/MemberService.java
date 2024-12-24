package com.team4.ttukttak_parking.domain.member.service;

import com.team4.ttukttak_parking.domain.member.dto.MemberCarResponseDto;
import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.dto.MemberResponse;
import com.team4.ttukttak_parking.domain.member.entity.Car;
import com.team4.ttukttak_parking.domain.member.entity.Member;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import com.team4.ttukttak_parking.domain.member.repository.CarRepository;
import com.team4.ttukttak_parking.domain.member.repository.MemberRepository;
import com.team4.ttukttak_parking.global.exception.BadRequestException;
import com.team4.ttukttak_parking.global.exception.DuplicateAccountException;
import com.team4.ttukttak_parking.global.exception.ErrorCode;
import com.team4.ttukttak_parking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CarRepository carRepository;

    // 일반 user 회원가입
    @Transactional
    public MemberResponse.Join join(MemberRequest.Join dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new DuplicateAccountException(ErrorCode.USER_ALREADY_EXIST);
        }

        return MemberResponse.Join.from(
            memberRepository.save(Member.to(dto, passwordEncoder, MemberRoles.ROLE_USER)));
    }

    // Admin 회원가입
    @Transactional
    public MemberResponse.Join joinAdmin(MemberRequest.Join dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new DuplicateAccountException(ErrorCode.USER_ALREADY_EXIST);
        }

        return MemberResponse.Join.from(
            memberRepository.save(Member.to(dto, passwordEncoder, MemberRoles.ROLE_ADMIN)));
    }

    @Transactional(readOnly = true)
    public MemberResponse.GetMember getMemberInfo(String email) {
        return MemberResponse.GetMember.from(
            memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public Void modifyInfo(MemberRequest.Modify dto, String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        member.updateMember(dto.contact(), dto.name());

        return null;
    }

    public List<MemberCarResponseDto> getMembersCars(String email){
        List<Car> carList = carRepository.findByMemberEmail(email);
        return carList.stream().map(MemberCarResponseDto::from).collect(Collectors.toList());

    }

    @Transactional
    public void registerCar(String email, String carNum){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Car car = Car.createCar(carNum,member);
        carRepository.save(car);
    }

    @Transactional
    public void updatePrimary(String email, Long id, String status){
        if(status.equals("on")||status.equals("off")){
            Car car = carRepository.findById(id).orElseThrow(()-> new NotFoundException(ErrorCode.CAR_NOT_FOUND));
            if(car.getMember().getEmail().equals(email)){
                car.updatePrimaryStatus(status.equals("on"));
            }
            else{throw new BadRequestException(ErrorCode.BAD_REQUEST);}
        }
        else{throw new BadRequestException(ErrorCode.BAD_REQUEST);}
    }


}
