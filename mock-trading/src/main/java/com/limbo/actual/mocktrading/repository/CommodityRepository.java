package com.limbo.actual.mocktrading.repository;

import com.limbo.actual.mocktrading.model.Commodity;
import com.limbo.actual.mocktrading.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity,Long> {
}
