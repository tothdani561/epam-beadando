package com.epam.training.ticketservice.core.screening.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, ScreeningId> {
}
