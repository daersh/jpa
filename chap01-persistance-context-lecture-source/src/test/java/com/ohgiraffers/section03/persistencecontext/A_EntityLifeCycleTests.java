package com.ohgiraffers.section03.persistencecontext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class A_EntityLifeCycleTests {
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
    /**
     * <h2>영속성 컨텍스트</h2>
     * 영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 저장하는 공간으로 엔티티 객체를 보관하고 관리한다.<br>
     * 엔티티 매니저가 생성될 때 하나의 영속성 컨텍스트가 만들어진다.<br><br>
     *
     * 엔티티의 생명 주기<br>
     * 비영속, 영속, 준영속, 삭제 상태<br>
     * */
    @Test
    public void 비영속성_테스트(){
        Menu foundmenu = entityManager.find(Menu.class,2);
        /* 영속 상태의 객체에서 값을 추출해 담더라도 새로 생성되어 영속성 컨텍스트와 관련 없는 객체는 비영속상태이다/*/
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundmenu.getMenuCode());
        newMenu.setMenuName(foundmenu.getMenuName());
        newMenu.setMenuPrice(foundmenu.getMenuPrice());
        newMenu.setCategoryCode(foundmenu.getCategoryCode());
        newMenu.setOrderableStatus(foundmenu.getOrderableStatus());
        boolean isTrue = (foundmenu == newMenu);
        assertFalse(isTrue);
    }
    @Test
    public void 영속성_연속_조회_테스트(){
        Menu foundmenu1 = entityManager.find(Menu.class,2);
        Menu foundmenu2 = entityManager.find(Menu.class,2);
        boolean isTrue = (foundmenu1 == foundmenu2);
        assertTrue(isTrue);
    }


    @Test
    public void 영속성_객체_추가_테스트(){
        /**
         * 이 예제에서는 menuCode 필드 값에 직접 값을 주고자 한다.(auto_increment 안쓸 예정 )
         * */
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(1000);
        menuToRegist.setCategoryCode(10);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist);
        Menu foundMenu = entityManager.find(Menu.class, 500);
        boolean isTrue = (menuToRegist == foundMenu);
        assertTrue(isTrue);
    }

    @Test
    public void 영속성_객체_추가_값_변경_테스트(){
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(1000);
        menuToRegist.setCategoryCode(10);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist);
        menuToRegist.setMenuName("메론죽");
        Menu foundMenu = entityManager.find(Menu.class,500);

        assertEquals(menuToRegist,foundMenu);
    }

    @Test
    public void 준영속성_detach_테스트(){
        Menu foundMenu1 = entityManager.find(Menu.class,11);
        Menu foundMenu2 = entityManager.find(Menu.class,12);
        /*
          준영속 상태란?
            영속성 컨텍스트가 관리하던 엔티티 객체를 관리하지 않는 상태가 되는 것이다.!
            detach가 준영속 상태를 만들기 위한 메서드이다.
        */
        entityManager.detach(foundMenu2);   //준영속상태 : 매니저가 관리하지 않는 상태
        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(50000);
        assertEquals(5000,entityManager.find(Menu.class,11).getMenuPrice());
        assertNotEquals(50000,entityManager.find(Menu.class,12).getMenuPrice());
    }

    @Test
    public void 준영속성_clear_test(){
        Menu foundMenu1 = entityManager.find(Menu.class,11);
        Menu foundMenu2 = entityManager.find(Menu.class,12);

        entityManager.detach(foundMenu2);
        /*clear : 영속성 컨텍스트로 관리되던 엔티티 객체들을 모두 비영속 상태로 바꿈*/
        entityManager.clear();
        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(50000);
//        entityManager.merge(foundMenu1);
//        entityManager.merge(foundMenu2);
        /*디비에서 새로 조회 해온 객체를 영속 상태로 두기 때문에 전혀 다른 결과가 나온다.*/
        assertNotEquals(5000,entityManager.find(Menu.class,11).getMenuPrice());
        assertNotEquals(50000,entityManager.find(Menu.class,12).getMenuPrice());
    }

    @Test
    public void 준영속성_close_test(){
        Menu foundMenu1 = entityManager.find(Menu.class,11);
        Menu foundMenu2 = entityManager.find(Menu.class,12);

        entityManager.detach(foundMenu2);
        /*close : 영속성 컨텍스트 및 엔티티 매니저까지 종료하여 사용할 수 없는 상태!*/
        entityManager.close();
        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(50000);
//        entityManager.merge(foundMenu1);
//        entityManager.merge(foundMenu2);
        /*디비에서 새로 조회 해온 객체를 영속 상태로 두기 때문에 전혀 다른 결과가 나온다.*/
        assertEquals(5000,entityManager.find(Menu.class,11).getMenuPrice());
        assertEquals(50000,entityManager.find(Menu.class,12).getMenuPrice());
    }

    @Test
    public void 병합_merge_test(){
       Menu menuToDetach = entityManager.find(Menu.class,2);
       entityManager.detach(menuToDetach);
       menuToDetach.setMenuName("수박죽");
       Menu refoundMenu = entityManager.find(Menu.class,2);
       System.out.println("refoundMenu.hashCode() = " + refoundMenu.hashCode());
       System.out.println("refoundMenu.hashCode() = " + refoundMenu.hashCode());
       entityManager.merge(menuToDetach);
       Menu mergedMenu = entityManager.find(Menu.class,2);
       assertEquals("수박죽", mergedMenu.getMenuName());
       assertEquals("수박죽", refoundMenu.getMenuName());
       assertEquals("수박죽", menuToDetach.getMenuName());
    }

    @Test
    public void 병합_merge_삽입_test(){
        Menu menuToDetach = entityManager.find(Menu.class,2);
        entityManager.detach(menuToDetach);
        menuToDetach.setMenuCode(999);
        menuToDetach.setMenuName("수박죽");

        entityManager.merge(menuToDetach);
        assertEquals(999,entityManager.find(Menu.class,999).getMenuCode());
        Menu newMenu = entityManager.find(Menu.class,2);
        Menu mergedMenu = entityManager.find(Menu.class,999);
        assertNotEquals(newMenu.getMenuCode(), mergedMenu.getMenuCode());
    }
}
