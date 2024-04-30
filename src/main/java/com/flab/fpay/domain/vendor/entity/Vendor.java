package com.flab.fpay.domain.vendor.entity;

import com.flab.fpay.domain.common.entity.BaseEntity;
import com.flab.fpay.domain.vendor.enums.VendorStatus;
import jakarta.persistence.*;

@Entity
@Table(
        name = "vendor"
)
public class Vendor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "member_id")
    private long memberId;

    @Column(name = "biz_reg_no")
    private String bizRegNo;

    @Column(name = "name")
    private String name;

    @Column(name = "rep_name")
    private String repName;

    @Column(name = "phone_no")
    private String phoneNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VendorStatus status;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Vendor() {}

    public Vendor(long memberId, String bizRegNo, String name, String repName, String phoneNo, VendorStatus status, boolean isDeleted) {
        this.memberId = memberId;
        this.bizRegNo = bizRegNo;
        this.name = name;
        this.repName = repName;
        this.phoneNo = phoneNo;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
    }

    public VendorStatus getStatus() {
        return status;
    }

}
