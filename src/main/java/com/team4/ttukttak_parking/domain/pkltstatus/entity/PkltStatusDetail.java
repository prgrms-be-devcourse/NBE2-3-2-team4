package com.team4.ttukttak_parking.domain.pkltstatus.entity;

import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "pklt_status_details")
public class PkltStatusDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkltStatusDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pklt_id", nullable = false)
    private Pklt pklt;

    @CreatedDate
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public static PkltStatusDetail to(Pklt pklt) {
        return PkltStatusDetail.builder().pklt(pklt).build();
    }

}
