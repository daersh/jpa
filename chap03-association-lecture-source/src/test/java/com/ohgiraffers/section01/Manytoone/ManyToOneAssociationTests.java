package com.ohgiraffers.section01.Manytoone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class ManyToOneAssociationTests {
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

    /* Association Mapping은 엔티티 클래스 간 관계를 매핑하는 것을 의미한다(조인을 위해)
    * 이를 통해 객체를 이용해 데이터베이스의 테이블 간의 관계를 활용해 한번에 객체로 조회할 수 있다.
    *
    * 1. 다중성에 의한 분류
        * 연관 관계가 있는 객체 관계에서 실제로 연관을 가지는 객체의 수에 따라 분류한다.
            * N:1(ManyToOne) 연관관계
            * 1:N(OntToMany) 연관관계
            * 1:1(OneToOne) 연관관계
            * N:N(ManyToMany) 연관관계
    * 2. 방향에 따른 분류
        * 테이블간 연관 관계는 외래키를 이용하여 양방향 연관 관계의 특징을 가진다.
        * 반면, 객체는 참조에 의한 연관관계로 단방향이다!!!!!
        * 객체 간의 연관 관계를 양방향으로 하고 싶은 경우 반대쪽에서도 필드를 추가하여 참조를 보관한다.
        * 이를 통해 단방향 2개로 양항향 관계처럼 보이도록 할 수 있지만 양방향 관계라고 하지는 않는다.
        * 단방향(UniDirectional) 연관 관계
        * 양방향(BiDirectional) 연관 관계
    * */
    @Test
    public void 다대일_연관관계_객체_그래프_탐색을_이용한_조회_테스트(){
        int menuCode = 15;
        MenuAndCategory menuAndCategory = entityManager.find(MenuAndCategory.class, menuCode);
        Category menuCategory = menuAndCategory.getCategory();
        Assertions.assertNotNull(menuCategory);
        System.out.println("menuAndCategory = " + menuAndCategory);
        System.out.println("menuCategory = " + menuCategory);
        System.out.println("menuCategory.getCategoryName() = " + menuCategory.getCategoryName());
    }

}
