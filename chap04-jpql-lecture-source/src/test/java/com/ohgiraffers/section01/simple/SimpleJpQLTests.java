package com.ohgiraffers.section01.simple;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleJpQLTests {
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
     * jpql의 기본 문법
        * 1. SELECT, UPDATE, DELETE등의 키워드 사용은 sql과 동일
        * 2. insert는 persist() 메서드 사용하면 된다.
        * 3. 키워드는 대소문자를 구분하지 않지만, 엔티티와 속성은 대소문자를 구분해야한다.
     * jpql 사용법
        * 1. 작성한 jpql은 'entityManager,createQuery()' 메서드를 통해 쿼리 객체로 만든다.
        * 2. 쿼리 객체는 'TypedQuery','Query' 두가지 있다.
            * 2-1. TypedQuery: 반환할 타입을 명확하게 한 경우 사용, 쿼리 객체의 메소드 실행 결과로 지정한 타입이 반환 된다!
            * 2-2. Query: 반환할 타입을 명확하게 지정할 수 없을 때 사용하며 쿼리 객체 메소드의 실행 결과로 Object 또는 Object[]로 반환된다.
        * 3. 쿼리 객체에서 제공하는 메소드를 호출해서 쿼리를 실행하고 데이터베이스를 조회한다.
            * 3-1. getSingleResult(): 결과가 정확히 한 행인 경우 사용. 없거나 여러개면 예외 발생!!!!
            * 3-2. getResultList(): 결과가 2행 이상일 경우 사용하며 컬렉션으로 반환. 없으면 빈 컬렉션 반환.
     * */
    @Test
    public void TypeQuery를_이용한_단일행_단일열_조회_테스트(){
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode=7";
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
        System.out.println("query = " + query);//주소만나옴
        String resultMenuName = query.getSingleResult();
        System.out.println("resultMenuName = " + resultMenuName);
        assertEquals("민트미역국",resultMenuName);
    }
    @Test
    public void 쿼리를_이용한_다일행_단일열_테스트(){
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode=7";
        Query query = entityManager.createQuery(jpql);

        Object resultMenuName = query.getSingleResult();
        assertTrue(resultMenuName instanceof String);
        assertEquals("민트미역국",resultMenuName);
    }

    @Test
    public void TypedQuery를_이용한_다중행_다중열_조회_테스트(){
        /*jpql에서는 entitiy의 별칭을 적으면 모든 속성을 조회하는 것이다.*/
        String jpql = "SELECT m FROM menu_section01 as m";
        TypedQuery<Menu> query = entityManager.createQuery(jpql,Menu.class);
        List<Menu> MenuList = query.getResultList();
        assertTrue(!MenuList.isEmpty());
        MenuList.forEach(System.out::println);
    }

    @Test
    public void distinct를_활용한_중복제거_여러_행_조회_테스트(){
        /*메뉴로 존재하는 카테고리의 종류(중복제거)민 조회*/
        String jpql = "SELECT DISTINCT m.categoryCode FROM menu_section01 as m";
        TypedQuery<Integer> query = entityManager.createQuery(jpql,Integer.class);
        List<Integer> list = query.getResultList();
        assertTrue(!list.isEmpty());
        list.forEach(System.out::println);
    }

    @Test
    public void in_연산자를_활용한_조회__테스트(){
        /*6 또는 10만 조회*/
        String jpql = "SELECT m FROM menu_section01 as m WHERE m.categoryCode IN (6,10)";
        TypedQuery<Menu> query = entityManager.createQuery(jpql,Menu.class);
        List<Menu> list = query.getResultList();
        assertTrue(!list.isEmpty());
        list.forEach(System.out::println);
    }

    @Test
    public void in_연산자를_활용한_조회_테스트(){
        String jpql = "SELECT m FROM menu_section01 as m WHERE m.menuName LIKE '%마늘%'";
        List<Menu> menuList = entityManager.createQuery(jpql,Menu.class).getResultList();
        assertTrue(!menuList.isEmpty());
        menuList.forEach(System.out::println);
    }
}
