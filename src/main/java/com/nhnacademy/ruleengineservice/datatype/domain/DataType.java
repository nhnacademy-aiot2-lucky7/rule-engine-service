package com.nhnacademy.ruleengineservice.datatype.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "datatypes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class DataType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("식별번호")
    private Long id;

    @Column(name = "type_name")
    @Comment("데이터타입-이름")
    private String typeName;

    @Column(name = "type_desc")
    @Comment("데이터타입-설명")
    private String typeDesc;

    private DataType(String typeName, String typeDesc) {
        this.typeName = typeName;
        this.typeDesc = typeDesc;
    }

    public static DataType ofNewDataType(String typeName, String typeDesc) {
        return new DataType(typeName, typeDesc);
    }
}
