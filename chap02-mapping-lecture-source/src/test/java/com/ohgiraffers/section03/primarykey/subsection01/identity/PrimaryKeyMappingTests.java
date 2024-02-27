package com.ohgiraffers.section03.primarykey.subsection01.identity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

public class PrimaryKeyMappingTests {
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

    /*
    * strategy: 자동 생성 전략 지정
    *  - GenerationType.IDENTITY : 기본 키 생성을 데이터베이스에 위임(mysql or mariadb의 auto_increment)
    *  - GenerationType.SEQUENCE: 데이터베이스 시퀀스 객체 사용(ORACLE의 SEQUENCE)
    *  - GenerationType.TABLE: 키 생성 테이블 사용
    *  - GenerationType.AUTO: 자동 선택(MySQL의 IDENTITY or ORACLE의 SEQUENCE)
    * */

    @Test
    public void 식별자_매핑_테스트(){
        //given
        Member member = new Member();
//        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setNickname("홍길동");
        member.setPhone("010-1234-5678");
        member.setEmail("hong@gmail.com");
        member.setAddress("서울시 서초구");
        member.setEnrollDate(new java.util.Date());
        member.setMemberRole("ROLE_MEMBER");
        member.setStatus("Y");
        //given
        Member member2 = new Member();
//        member2.setMemberNo(2);
        member2.setMemberId("user02");
        member2.setMemberPwd("pass02");
        member2.setNickname("유관순");
        member2.setPhone("010-1234-1234");
        member2.setEmail("guansoon@gmail.com");
        member2.setAddress("인천시 서구");
        member2.setEnrollDate(new java.util.Date());
        member2.setMemberRole("ROLE_MEMBER");
        member2.setStatus("Y");

        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);
        entityManager.persist(member2);
        entityTransaction.commit();

        //then
        Member select1 = entityManager.find(Member.class,1);
        Member select2 = entityManager.find(Member.class,2);

        System.out.println("select1.getNickname() = " + select1.getNickname());
        System.out.println("select2.getNickname() = " + select2.getNickname());

        String jpql = "SELECT A.memberNo FROM member_section03_subsection01 A";
        List<Integer> memberNoList = entityManager.createQuery(jpql, Integer.class).getResultList();
        memberNoList.forEach(System.out::println);


    }
}
