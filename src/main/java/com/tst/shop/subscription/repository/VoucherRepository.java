package com.tst.shop.subscription.repository;

import com.tst.shop.subscription.model.entity.Voucher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    Optional<Voucher> findByCode(String code);
}
