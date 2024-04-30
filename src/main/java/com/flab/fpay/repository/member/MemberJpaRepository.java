package com.flab.fpay.repository.member;

import com.flab.fpay.domain.member.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Member findByIdAndIsDeletedIsFalse(long id);
    Member findByEmailAndIsDeletedIsFalse(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.id = :id")
    Member findByIdWithPessimisticLock(long id);
}
