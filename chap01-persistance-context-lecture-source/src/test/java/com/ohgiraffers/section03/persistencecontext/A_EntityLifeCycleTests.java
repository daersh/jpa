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
}
