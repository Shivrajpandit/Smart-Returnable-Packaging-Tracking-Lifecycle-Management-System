package com.packaging.service;

import com.packaging.dto.AssetHistoryDTO;
import com.packaging.dto.TransactionDTOs.*;
import com.packaging.entity.*;
import com.packaging.exception.BadRequestException;
import com.packaging.exception.ResourceNotFoundException;
import com.packaging.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final IssueTransactionRepository issueRepository;
    private final ReturnTransactionRepository returnRepository;
    private final DamageRecordRepository damageRepository;
    private final RepairRecordRepository repairRepository;
    private final AssetMovementRepository movementRepository;

    @Autowired
    public TransactionService(AssetRepository assetRepository,
                              CustomerRepository customerRepository,
                              UserRepository userRepository,
                              IssueTransactionRepository issueRepository,
                              ReturnTransactionRepository returnRepository,
                              DamageRecordRepository damageRepository,
                              RepairRecordRepository repairRepository,
                              AssetMovementRepository movementRepository) {
        this.assetRepository = assetRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
        this.returnRepository = returnRepository;
        this.damageRepository = damageRepository;
        this.repairRepository = repairRepository;
        this.movementRepository = movementRepository;
    }

    // =========================================================================
    // 1. ISSUE ASSET LIFECYCLE
    // =========================================================================
    @Transactional
    public IssueTransaction issueAsset(IssueRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + request.getAssetId()));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new BadRequestException("Cannot issue asset. Current status is '" + asset.getStatus() + "'. Only 'AVAILABLE' assets can be issued.");
        }

        if (request.getExpectedReturnDate().isBefore(request.getIssueDate())) {
            throw new BadRequestException("Expected return date cannot be earlier than issue date");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        User user = userRepository.findById(request.getIssuedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getIssuedBy()));

        // Create Issue Record
        IssueTransaction issue = new IssueTransaction();
        issue.setAsset(asset);
        issue.setCustomer(customer);
        issue.setIssuedBy(user);
        issue.setIssueDate(request.getIssueDate());
        issue.setExpectedReturnDate(request.getExpectedReturnDate());
        issue.setPurpose(request.getPurpose() != null ? request.getPurpose().trim() : "Standard dispatch");

        IssueTransaction savedIssue = issueRepository.save(issue);

        // Rule 4: Update Asset Status to ISSUED
        asset.setStatus(AssetStatus.ISSUED);
        assetRepository.save(asset);

        // Log Movement automatically
        AssetMovement movement = new AssetMovement();
        movement.setAsset(asset);
        movement.setFromLocation(asset.getWarehouse().getWarehouseName());
        movement.setToLocation(customer.getCompanyName());
        movement.setMovementDate(LocalDateTime.now());
        movement.setMovedBy(user);
        movement.setRemarks("Outward dispatch: Issue #" + savedIssue.getIssueId());
        movementRepository.save(movement);

        return savedIssue;
    }

    public List<IssueTransaction> getAllIssues() {
        return issueRepository.findAll();
    }

    public IssueTransaction getIssueById(Integer issueId) {
        return issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue transaction not found with id: " + issueId));
    }

    // =========================================================================
    // 2. RETURN ASSET LIFECYCLE
    // =========================================================================
    @Transactional
    public ReturnTransaction returnAsset(ReturnRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + request.getAssetId()));

        if (asset.getStatus() != AssetStatus.ISSUED) {
            throw new BadRequestException("Cannot process return. Asset status is '" + asset.getStatus() + "'. Only 'ISSUED' assets can be returned.");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        User user = userRepository.findById(request.getReceivedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getReceivedBy()));

        ReturnTransaction returnTx = new ReturnTransaction();
        returnTx.setAsset(asset);
        returnTx.setCustomer(customer);
        returnTx.setReceivedBy(user);
        returnTx.setReturnDate(request.getReturnDate());
        returnTx.setConditionStatus(request.getConditionStatus());
        returnTx.setRemarks(request.getRemarks() != null ? request.getRemarks().trim() : null);

        ReturnTransaction savedReturn = returnRepository.save(returnTx);

        // State Machine Rules for Returns:
        if (request.getConditionStatus() == ConditionStatus.GOOD) {
            asset.setStatus(AssetStatus.AVAILABLE);
        } else if (request.getConditionStatus() == ConditionStatus.MINOR_DAMAGE || request.getConditionStatus() == ConditionStatus.MAJOR_DAMAGE) {
            asset.setStatus(AssetStatus.DAMAGED);
        } else if (request.getConditionStatus() == ConditionStatus.UNUSABLE) {
            asset.setStatus(AssetStatus.RETIRED);
        }

        assetRepository.save(asset);

        // Auto-Log Movement back to warehouse
        AssetMovement movement = new AssetMovement();
        movement.setAsset(asset);
        movement.setFromLocation(customer.getCompanyName());
        movement.setToLocation(asset.getWarehouse().getWarehouseName());
        movement.setMovementDate(LocalDateTime.now());
        movement.setMovedBy(user);
        movement.setRemarks("Inward return: Return #" + savedReturn.getReturnId() + " (" + request.getConditionStatus() + ")");
        movementRepository.save(movement);

        return savedReturn;
    }

    public List<ReturnTransaction> getAllReturns() {
        return returnRepository.findAll();
    }

    public ReturnTransaction getReturnById(Integer returnId) {
        return returnRepository.findById(returnId)
                .orElseThrow(() -> new ResourceNotFoundException("Return transaction not found with id: " + returnId));
    }

    // =========================================================================
    // 3. DAMAGE RECORDING
    // =========================================================================
    @Transactional
    public DamageRecord recordDamage(DamageRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + request.getAssetId()));

        User user = userRepository.findById(request.getReportedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getReportedBy()));

        DamageRecord record = new DamageRecord();
        record.setAsset(asset);
        record.setReportedBy(user);
        record.setDamageType(request.getDamageType().trim());
        record.setSeverity(request.getSeverity());
        record.setDamageDate(request.getDamageDate());
        record.setEstimatedCost(request.getEstimatedCost());
        record.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);

        DamageRecord savedDamage = damageRepository.save(record);

        // Change asset status to DAMAGED
        asset.setStatus(AssetStatus.DAMAGED);
        assetRepository.save(asset);

        return savedDamage;
    }

    public List<DamageRecord> getAllDamages() {
        return damageRepository.findAll();
    }

    public DamageRecord getDamageById(Integer damageId) {
        return damageRepository.findById(damageId)
                .orElseThrow(() -> new ResourceNotFoundException("Damage record not found with id: " + damageId));
    }

    // =========================================================================
    // 4. REPAIR MANAGEMENT
    // =========================================================================
    @Transactional
    public RepairRecord createRepair(RepairRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + request.getAssetId()));

        User user = userRepository.findById(request.getRepairedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getRepairedBy()));

        DamageRecord damage = null;
        if (request.getDamageId() != null) {
            damage = damageRepository.findById(request.getDamageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Damage record not found with id: " + request.getDamageId()));
        }

        RepairRecord repair = new RepairRecord();
        repair.setAsset(asset);
        repair.setDamageRecord(damage);
        repair.setRepairedBy(user);
        repair.setRepairDate(request.getRepairDate());
        repair.setRepairCost(request.getRepairCost());
        repair.setRepairStatus(request.getRepairStatus());
        repair.setRemarks(request.getRemarks() != null ? request.getRemarks().trim() : null);

        RepairRecord savedRepair = repairRepository.save(repair);

        // Update asset status based on repair status
        if (request.getRepairStatus() == RepairStatus.IN_PROGRESS || request.getRepairStatus() == RepairStatus.PENDING) {
            asset.setStatus(AssetStatus.UNDER_REPAIR);
        } else if (request.getRepairStatus() == RepairStatus.COMPLETED) {
            asset.setStatus(AssetStatus.AVAILABLE);
        }
        assetRepository.save(asset);

        return savedRepair;
    }

    @Transactional
    public RepairRecord updateRepair(Integer repairId, RepairRequest request) {
        RepairRecord repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new ResourceNotFoundException("Repair record not found with id: " + repairId));

        repair.setRepairDate(request.getRepairDate());
        repair.setRepairCost(request.getRepairCost());
        repair.setRepairStatus(request.getRepairStatus());
        repair.setRemarks(request.getRemarks() != null ? request.getRemarks().trim() : null);

        RepairRecord updated = repairRepository.save(repair);

        Asset asset = repair.getAsset();
        if (request.getRepairStatus() == RepairStatus.COMPLETED) {
            asset.setStatus(AssetStatus.AVAILABLE);
        } else if (request.getRepairStatus() == RepairStatus.IN_PROGRESS || request.getRepairStatus() == RepairStatus.PENDING) {
            asset.setStatus(AssetStatus.UNDER_REPAIR);
        }
        assetRepository.save(asset);

        return updated;
    }

    public List<RepairRecord> getAllRepairs() {
        return repairRepository.findAll();
    }

    public RepairRecord getRepairById(Integer repairId) {
        return repairRepository.findById(repairId)
                .orElseThrow(() -> new ResourceNotFoundException("Repair record not found with id: " + repairId));
    }

    // =========================================================================
    // 5. ASSET MOVEMENTS
    // =========================================================================
    @Transactional
    public AssetMovement recordMovement(MovementRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + request.getAssetId()));

        User user = userRepository.findById(request.getMovedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getMovedBy()));

        AssetMovement movement = new AssetMovement();
        movement.setAsset(asset);
        movement.setFromLocation(request.getFromLocation().trim());
        movement.setToLocation(request.getToLocation().trim());
        movement.setMovementDate(LocalDateTime.now());
        movement.setMovedBy(user);
        movement.setRemarks(request.getRemarks() != null ? request.getRemarks().trim() : null);

        return movementRepository.save(movement);
    }

    public List<AssetMovement> getAllMovements() {
        return movementRepository.findAllByOrderByMovementDateDesc();
    }

    // =========================================================================
    // 6. 360-DEGREE ASSET HISTORY
    // =========================================================================
    public AssetHistoryDTO getAssetHistory(Integer assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));

        return new AssetHistoryDTO(
                asset,
                issueRepository.findByAssetAssetIdOrderByIssueDateDesc(assetId),
                returnRepository.findByAssetAssetIdOrderByReturnDateDesc(assetId),
                damageRepository.findByAssetAssetIdOrderByDamageDateDesc(assetId),
                repairRepository.findByAssetAssetIdOrderByCreatedAtDesc(assetId),
                movementRepository.findByAssetAssetIdOrderByMovementDateDesc(assetId)
        );
    }
}
