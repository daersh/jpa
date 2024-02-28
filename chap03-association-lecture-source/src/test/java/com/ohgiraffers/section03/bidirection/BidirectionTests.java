package com.ohgiraffers.section03.bidirection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class BidirectionTests {
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
    public void 양방향_연관관계_매핑_조회_테스트(){
        int menuCode = 10;
        int categoryCode = 10;
        /*설명. 연관관계의 주인은 바로 조인 시키는 것을 알 수 있다.*/
        Menu menu = entityManager.find(Menu.class,menuCode);
        /*설명. 양방향은 부모에 해당하는 엔티티는 가짜 연관관계이고, */
        Category category = entityManager.find(Category.class,categoryCode);
        /*설명. getMenuList()시점에야 관계를 맺은 메뉴 엔티티가 필요해 select문이 날라간다(지연로딩과 같은 느낌적인 느낌)*/
        category.getMenuList().forEach(System.out::println);

    }
}
