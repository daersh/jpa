package com.ohgiraffers.section01;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
/**
 * <h2>1. 엔티티 매니저 팩토리(EntityManagerFactory)</h2>
 * <al>
 * 엔티티 매니저를 생성할 수 있는 기능을 제공하는 팩토리 클래스이다.<br>
 * thread-safe하기 때문에 여러 스레드가 동시에 접근해도 안전하여 서로 다른 스레드 간 공유해서 사용한다.<br>
 * thread-safe한 기능을 요청 스코프마다 생성하기에는 비용(시간,메모리) 부담이 크므로 appalcation scope와 동일하게 싱글톤으로
 * 생성해서 관리하는 것이 효율적이다.<br>
 * 따라서 데이터베이스를 사용하는 애플리케이션 하나당 하나의 EntityManagerFactory를 생성한다.<br><br><br>
 * </al>
 * <h2>2. 엔티티매니저(EntityManager)</h2>
 * <al>
 * 엔티티 매니저는 엔티티를 저장한는 메모리 상의 데이터베이스를 관리하는 인스턴스이다.<br>
 * 엔티티를 저장, 수정, 삭제, 조회 등 엔티티와 관련된 모든 일을 담당한다.<br>
 * 엔티티 메니저는 thread-safe하지 않기 때문에 동시성 문제가 발생할 수 있다.<br>
 * 따라서 스레드 간 공유를 하지 않고, web의 경우 일반적으로 request scope와 일치시킨다.<br><br><br>
 * </al>
 * <h2>3. 영속성컨테스트(PersistenceContext)</h2>
 * <al>
 * 엔티티 매니저를 통해 엔티티를 저장하거나 조회하면 엔티티 매니저는 영속성 컨텍스트에 엔티티를 보관하고 관리한다.<br>
 * 영속성 컨텍스트는 엔티티를 key-value 방식으로 저장하는 저장소이다.<br>
 * 영속성 컨텍스트느 엔티티 매니저를 생성할 때 같이 하나 만들어진다.<br>
 * 그리고 엔티티 매니저를 통해서 영속성 컨텍스트에 접근할 수 있고 관리할 수 있다.<br>
 * </al>
 * */
public class A_EntityManagerLifeCycleTests {
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

    @Test
    public void 엔티티_매니저_팩토리와_엔티티_매니저_생명주기_확인1(){/*displayName 귀찮으면 이런 예산ㄹ 방식 사용*/
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode());
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode());
    }
    @Test
    public void 엔티티_매니저_팩토리와_엔티티_매니저_생명주기_확인2(){/*displayName 귀찮으면 이런 예산ㄹ 방식 사용*/
        /* 1 과 2 테스트 둘 다 같은 해시코드 뽑는 것을 알 수 있다.*/
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode());
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode());
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }
    @AfterEach
    public void closeManager(){
        entityManager.close();
    }
}
