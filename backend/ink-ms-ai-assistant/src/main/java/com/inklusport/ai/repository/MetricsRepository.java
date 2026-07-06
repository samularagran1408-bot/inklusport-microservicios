package com.inklusport.ai.repository;

import com.inklusport.ai.model.MetricsDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricsRepository extends MongoRepository<MetricsDashboard, String> {
}
