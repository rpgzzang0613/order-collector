package com.amiiboroom.ordercollector.repository;

import com.amiiboroom.ordercollector.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    // JpaRepository 안에 기본적인 CRUD가 구현되어 있음
}
