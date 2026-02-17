package com.example.convergencesoftwarerecruitingbe.domain.locker.repository;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.RentalStatus;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class RentalSpecification {

    private RentalSpecification() {
    }

    public static Specification<Rental> fetchJoins() {
        return (root, query, cb) -> {
            if (Long.class != query.getResultType() && long.class != query.getResultType()) {
                Fetch<Rental, Locker> lockerFetch = root.fetch("locker", JoinType.INNER);
                Fetch<Rental, Application> appFetch = root.fetch("application", JoinType.INNER);
            }
            return null;
        };
    }

    public static Specification<Rental> rentalEndDateAfterOrEqual(LocalDate from) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rentalEndDate"), from);
    }

    public static Specification<Rental> rentalStartDateBeforeOrEqual(LocalDate to) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("rentalStartDate"), to);
    }

    public static Specification<Rental> lockerIdEquals(Long lockerId) {
        return (root, query, cb) -> cb.equal(root.get("lockerId"), lockerId);
    }

    public static Specification<Rental> studentIdHashEquals(String studentIdHash) {
        return (root, query, cb) -> cb.equal(root.get("application").get("studentIdHash"), studentIdHash);
    }

    public static Specification<Rental> statusEquals(RentalStatus status) {
        return (root, query, cb) -> {
            if (status == RentalStatus.ACTIVE) {
                return cb.isNull(root.get("returnedAt"));
            } else {
                return cb.isNotNull(root.get("returnedAt"));
            }
        };
    }
}
