package com.ohgiraffers.section05.compositekey.subsection02.idclass;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class IdClassTests {
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
    public void 아이디_클래스_사용한_복합키_테이블_매핑_테스트(){
        Member member= new Member();
        member.setMemberNo(1);
        member.setMemberId("user1");
        member.setPhone("010-0000-0000");
        member.setAddress("인천시 서구");

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);
        entityTransaction.commit();
        Member found = entityManager.find(Member.class,new MemberPK(1,"user1"));
        Assertions.assertEquals(member,found);
    }
}
