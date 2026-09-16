package com.packaging.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_movements")
public class AssetMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Integer movementId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(name = "from_location", nullable = false, length = 150)
    private String fromLocation;

    @Column(name = "to_location", nullable = false, length = 150)
    private String toLocation;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "moved_by", nullable = false)
    private User movedBy;

    @Column(name = "remarks", length = 255)
    private String remarks;

    public AssetMovement() {}

    public AssetMovement(Integer movementId, Asset asset, String fromLocation, String toLocation, LocalDateTime movementDate, User movedBy, String remarks) {
        this.movementId = movementId;
        this.asset = asset;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.movementDate = movementDate;
        this.movedBy = movedBy;
        this.remarks = remarks;
    }

    public Integer getMovementId() { return movementId; }
    public void setMovementId(Integer movementId) { this.movementId = movementId; }

    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }

    public String getFromLocation() { return fromLocation; }
    public void setFromLocation(String fromLocation) { this.fromLocation = fromLocation; }

    public String getToLocation() { return toLocation; }
    public void setToLocation(String toLocation) { this.toLocation = toLocation; }

    public LocalDateTime getMovementDate() { return movementDate; }
    public void setMovementDate(LocalDateTime movementDate) { this.movementDate = movementDate; }

    public User getMovedBy() { return movedBy; }
    public void setMovedBy(User movedBy) { this.movedBy = movedBy; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
