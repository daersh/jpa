package com.ohgiraffers.section01.problem;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * <h1>JDBC API의 문제점 </h1>
 * <al>
 *     <li>1. data 변환, SQL 작성, JDBC API 코드 등 중복 작성 -> 개발 시간 증가, 유지보수성 저하</li>
 *     <li>2. SQL에 의존하여 개발</li>
 *     <li>3. 패러다임 불일치(상속,연관관계,객체 그래프 탐색, 방향성)</li>
 *     <li>4. 동일성 보장 문제 - 담길 떄마다 새로운 객체가 필요함 </li>
 * </al>
 * */
public class ProblemOfUsingDirectSQLTests {
    private Connection con;

    /*목차 1. data 변환, SQL 작성, JDBC API 코드 등 중복 작성 -> 개발 시간 증가, 유지보수성 저하*/
    @BeforeEach
    void setConnection() throws ClassNotFoundException, SQLException {
        /* 1. DB 연결 (기존 jdbcTemplate의 getConnection 메소드 역할)*/
        String driver="com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/menudb";
        String user ="root";
        String password = "mariadb";
        Class.forName(driver);
        con = DriverManager.getConnection(url,user,password);
        con.setAutoCommit(false);   /*수동 커밋 off*/
    }
    @DisplayName("직접 SQL 작성하여 메뉴 조회할 때 발생하는 문제 확인")
    @Test
    void testDirectSelectSql() throws SQLException {
        //given
        String query = "SELECT MENU_CODE, MENU_NAME, MENU_PRICE, CATEGORY_CODE" +
                ", ORDERABLE_STATUS FROM TBL_MENU";
        // when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);
        List<Menu> menuList = new ArrayList<>();
        while(rset.next()){
            Menu menu = new Menu();
            menu.setMenuCode(rset.getInt("MENU_CODE"));
            menu.setMenuName(rset.getString("MENU_NAME"));
            menu.setMenuPrice(rset.getInt("MENU_PRICE"));
            menu.setCategoryCode(rset.getInt("CATEGORY_CODE"));
            menu.setOrderableStatus(rset.getString("ORDERABLE_STATUS"));
            menuList.add(menu);
        }
        // then
        Assertions.assertTrue(!menuList.isEmpty());
        menuList.forEach(System.out::println);
    }
    @AfterEach
    void closeConnection() throws SQLException {
        con.rollback();/*test만 하고 db적용 안하기 위함*/
        con.close();
    }
    @DisplayName("직접 SQL을 작성하여 신규 메뉴를 추가할 때 발생하는 문제 확인")
    @Test
    void testDirectInsertSQL() throws SQLException {
        //given
        Menu menu = new Menu();
        menu.setMenuName("민트초코짜장면");
        menu.setMenuPrice(10000);
        menu.setCategoryCode(1);
        menu.setOrderableStatus("Y");
        String query = "INSERT INTO TBL_MENU(MENU_NAME,MENU_PRICE,CATEGORY_CODE, ORDERABLE_STATUS) VALUES (?,?,?,?)";
        //when
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1,menu.getMenuName());
        pstmt.setInt(2,menu.getMenuPrice());
        pstmt.setInt(3,menu.getCategoryCode());
        pstmt.setString(4,menu.getOrderableStatus());
        int result = pstmt.executeUpdate();
        //then
        Assertions.assertEquals(1,result);
        pstmt.close();
    }



    /*목차 2. SQL에 의존하여 개발*/
    /*목차 2-1. 조회 항목 변경에 따른 의존성 확인 조회할 컬럼 변경되면 코드도 다 변경된다.*/
    @DisplayName("조회 항목 변경에 따른 의존성 확인")
    @Test
    void testChangeSelectColumns() throws SQLException {
        //given
        String query = "SELECT MENU_CODE, MENU_NAME,MENU_PRICE FROM TBL_MENU";

        //when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);
        List<Menu> menuList = new ArrayList<>();
        while(rset.next()){
            Menu menu = new Menu();
            menu.setMenuCode(rset.getInt("MENU_CODE"));
            menu.setMenuName(rset.getString("MENU_NAME"));
            menu.setMenuPrice(rset.getInt("MENU_PRICE"));
            menuList.add(menu);
        }
        //then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
        rset.close();
        stmt.close();
    }

    /*목차 2-2. 연관된 객체 문제*/
    /* 조인 하는 경우 코드가 매우 복잡해지는 것을 확인할 수 있다!!*/
    @DisplayName("JOIN 객체 문제")
    @Test
    void testAssociationObject() throws SQLException {
        //given
        String query = "SELECT A.menu_code, A.menu_name, A.menu_price, " +
                "b.category_code, b.category_name, A.orderable_status " +
                "FROM tbl_menu A "
                +"JOIN tbl_category b ON (A.category_code = b.category_code)";
        //when
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);
        List<MenuAndCategory> menuAndCategories = new ArrayList<>();
        while (rset.next()){
            MenuAndCategory menuAndCategory = new MenuAndCategory();
            menuAndCategory.setMenuCode(rset.getInt("menu_code"));
            menuAndCategory.setMenuName(rset.getString("menu_name"));
            menuAndCategory.setMenuPrice(rset.getInt("menu_price"));
            menuAndCategory.setCategory(new Category(rset.getInt("category_code"), rset.getString("category_name")));
            menuAndCategory.setOrderableStatus(rset.getString("orderable_status"));
            menuAndCategories.add(menuAndCategory);
        }
        Assertions.assertTrue(!menuAndCategories.isEmpty());
        menuAndCategories.forEach(System.out::println);
        //then
        rset.close();
        stmt.close();
    }

    /*목차 3. 패러다임 불일치(상속,연관관계,객체그래프탐색,방향성)
        *   
    * */
}
