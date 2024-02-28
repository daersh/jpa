package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class OneToManyAssociationTests {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        /*1. 환경 생성*/
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }
    @BeforeEach
    public void initManager(){
        /*2. manager 연결*/
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }
    @AfterEach
    public void closeManager(){
        entityManager.close();  // 설명. close 수행 시 flush, commit이 일어나 실제 디비에 영향을 주게 된다.
    }


    @Test
    public void 일대다_연관관계_객체_그래프_탐색을_이용한_조회_테스트()
    {
        int categoryCode =10;
        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class,categoryCode);
        Assertions.assertNotNull(categoryAndMenu);


    }


}
