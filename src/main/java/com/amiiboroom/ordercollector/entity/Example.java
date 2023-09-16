package com.amiiboroom.ordercollector.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * - Entity와 실제 DB의 테이블 컬럼이 매핑되지 않으면 프로젝트가 실행되지 않음 (ddl-auto: validate)
 *
 * - 보통은 Entity 이름을 테이블 이름과 동일하게 해야 제대로 매핑되나,
 *   주석예시처럼 테이블명을 따로 지정해줄 수 있음
 *
 * - 보통은 DB컬럼이 snake_case 라면 Entity의 변수명은 camelCase로 선언해야 제대로 매핑되나,
 *   주석예시처럼 컬럼명을 따로 지정해줄 수 있음
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
//@Table(name = "test_table")
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

//    @Column(name = "user_name")
    private String name;

    @CreationTimestamp
    private LocalDateTime regiDate;

    @Builder
    public Example(String name) {
        this.name = name;
    }

}
