package com.nhnacademy.ruleengineservice.datatype.cache;

import com.nhnacademy.ruleengineservice.datatype.domain.DataType;
import com.nhnacademy.ruleengineservice.datatype.repository.DataTypeRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataCache {

    private final Set<String> existingTypes = ConcurrentHashMap.newKeySet();
    private final DataTypeRepository repository;

    public DataCache(DataTypeRepository repository) {
        this.repository = repository;

        // 어플리케이션 시작 시 DB에서 모든 조합을 로컬 Set에 로드
        repository.findAll().forEach(dataType ->
                existingTypes.add(buildKey(dataType.getTypeName(), dataType.getTypeDesc()))
        );
    }

    private String buildKey(String datatype, String desc) {
        return datatype.toLowerCase() + "::" + desc.toLowerCase();
    }

    public void saveIfAbsent(String datatype, String desc) {
        String key = buildKey(datatype, desc);
        if (!existingTypes.contains(key)) {
            if (repository.findByTypeNameAndTypeDesc(datatype, desc).isEmpty()) {
                repository.save(DataType.ofNewDataType(datatype, desc));
            }
            existingTypes.add(key);
        }
    }
}
