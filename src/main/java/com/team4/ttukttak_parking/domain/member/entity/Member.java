package com.team4.ttukttak_parking.domain.member.entity;

import com.team4.ttukttak_parking.domain.member.dto.MemberRequest;
import com.team4.ttukttak_parking.domain.member.entity.enums.LoginTypes;
import com.team4.ttukttak_parking.domain.member.entity.enums.MemberRoles;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String email;
    private String password;
    private String name;
    private String contact;

    @Enumerated(EnumType.STRING)
    private MemberRoles role;

    @Enumerated(EnumType.STRING)
    private LoginTypes loginType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Member to(MemberRequest.Join dto) {
        return Member.builder()
            .email(dto.email())
            .password(dto.password())
            .name(dto.name())
            .contact(dto.contact())
            .role(dto.role())
            .loginType(dto.loginType())
            .build();
    }


    public void updateMember(String name, String email, String contact, String password) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
    }

}
