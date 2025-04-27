/*
package com.nhnacademy.ruleengineservice.sensorrule.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "analysis_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SensorAnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("식별번호")
    private Long id;

    @Column(name = "sensor_id", nullable = false, length = 50)
    @Comment("센서_아이디")
    private String sensorId;

    @Column(name = "data_type", nullable = false, length = 50)
    @Comment("데이터_타입")
    private String dataType;

    @Column(name = "sensor_id", nullable = false, length = 50)
    @Comment("값_타입(min, max, avg)")
    private String valueType;

    @Column(name = "sensor_id", nullable = false)
    @Comment("값")
    private Double value;
}
*/
