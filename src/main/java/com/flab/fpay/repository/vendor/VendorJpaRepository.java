package com.flab.fpay.repository.vendor;

import com.flab.fpay.domain.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorJpaRepository extends JpaRepository<Vendor, Long> {
}
