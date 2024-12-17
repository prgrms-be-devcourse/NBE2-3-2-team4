package com.team4.ttukttak_parking.domain.member.repository;


import com.team4.ttukttak_parking.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //int countByEmail(String email);
    boolean existsByEmail(String email);
}
