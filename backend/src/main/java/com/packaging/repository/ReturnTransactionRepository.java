package com.packaging.repository;

import com.packaging.entity.ReturnTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnTransactionRepository extends JpaRepository<ReturnTransaction, Integer> {
    List<ReturnTransaction> findByAssetAssetIdOrderByReturnDateDesc(Integer assetId);
    List<ReturnTransaction> findByCustomerCustomerIdOrderByReturnDateDesc(Integer customerId);
    long countByCustomerCustomerId(Integer customerId);
}
