package com.example.confirmator.api.repository;

import com.example.confirmator.model.entity.Processed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedRepository extends CrudRepository<Processed, Long> {

    boolean existsByRequest(String request);

}
