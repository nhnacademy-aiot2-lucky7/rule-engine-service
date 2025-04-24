package com.nhnacademy.ruleengineservice.datatype.repository;

import com.nhnacademy.ruleengineservice.datatype.domain.DataType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataTypeRepository extends JpaRepository<DataType, Long> {
    Optional<DataType> findByTypeNameAndTypeDesc(String typeName, String typeDesc);
}
