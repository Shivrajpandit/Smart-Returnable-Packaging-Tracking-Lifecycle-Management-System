package com.packaging.repository;

import com.packaging.entity.AssetStatus;
import com.packaging.entity.IssueTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssueTransactionRepository extends JpaRepository<IssueTransaction, Integer> {

    List<IssueTransaction> findByAssetAssetIdOrderByIssueDateDesc(Integer assetId);

    List<IssueTransaction> findByCustomerCustomerIdOrderByIssueDateDesc(Integer customerId);

    Optional<IssueTransaction> findTopByAssetAssetIdOrderByIssueIdDesc(Integer assetId);

    @Query("SELECT it FROM IssueTransaction it " +
           "WHERE it.asset.status = :status " +
           "AND it.expectedReturnDate < :today " +
           "ORDER BY it.expectedReturnDate ASC")
    List<IssueTransaction> findOverdueIssues(
            @Param("today") LocalDate today,
            @Param("status") AssetStatus status
    );

    @Query("SELECT COUNT(it) FROM IssueTransaction it " +
           "WHERE it.asset.status = 'ISSUED' AND it.expectedReturnDate < :today")
    long countOverdueIssues(@Param("today") LocalDate today);
}
