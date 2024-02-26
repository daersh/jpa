package com.ohgiraffers.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerCRUDTests {
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
    public void 메뉴코드로_메뉴_조회_테스트(){
        //given
        int menuCode=2;
        //when
        Menu foundMenu = entityManager.find(Menu.class,menuCode);
        //then
        Assertions.assertNotNull(foundMenu);
        Assertions.assertEquals(menuCode,foundMenu.getMenuCode());
        System.out.println("foundMenu = " + foundMenu);
    }

    @Test
    public void 새로운_메뉴_추가_테스트(){
        //given
        Menu menu = new Menu();
        menu.setMenuName("꿀발린추어탕");
        menu.setMenuPrice(7000);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");
        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {
            entityManager.persist(menu);
            entityTransaction.commit();
        }catch (Exception e){
            entityTransaction.rollback();
        }
        //then
        Assertions.assertTrue(entityManager.contains(menu));/*현재 메뉴 객체가 영속 상태로 관리되는지 확인하기 위함*/
    }
    @Test
    public void 메뉴_이름_수정_테스트(){
        //given
        Menu menu = entityManager.find(Menu.class,2);
        System.out.println("menu = " + menu);
        String menuNameToChange = "갈치스무디";
        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try{
            menu.setMenuName(menuNameToChange);
            entityTransaction.commit();
        }catch(Exception e){
            entityTransaction.rollback();
        }
        //then
        Assertions.assertEquals(menuNameToChange,entityManager.find(Menu.class,2).getMenuName());
    }
    @Test
    public void 메뉴_삭제_테스트(){
        //given
        Menu menuToRemove = entityManager.find(Menu.class,1);
        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {
            entityManager.remove(menuToRemove);
            entityTransaction.commit();
        }catch (Exception e){
            entityTransaction.rollback();
        }
        //then
        Menu remobeMenu = entityManager.find(Menu.class,1);
        Assertions.assertNull(remobeMenu);
    }
}
