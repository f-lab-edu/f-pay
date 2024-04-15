package com.flab.fpay.repository.member;

import com.flab.fpay.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
}
