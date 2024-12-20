package com.team4.ttukttak_parking.domain.member.entity;

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
    private LocalDateTime updateAt;

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



    public void updateMember(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }
}
