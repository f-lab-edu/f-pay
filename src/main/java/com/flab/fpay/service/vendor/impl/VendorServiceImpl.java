package com.flab.fpay.service.vendor.impl;

import com.flab.fpay.domain.vendor.entity.Vendor;
import com.flab.fpay.repository.vendor.VendorJpaRepository;
import com.flab.fpay.service.vendor.VendorService;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorJpaRepository vendorJpaRepository;

    public VendorServiceImpl(VendorJpaRepository vendorJpaRepository) {
        this.vendorJpaRepository = vendorJpaRepository;
    }

    @Override
    public Vendor getVendorById(long id) {
        return vendorJpaRepository.findById(id).orElse(null);
    }
}
