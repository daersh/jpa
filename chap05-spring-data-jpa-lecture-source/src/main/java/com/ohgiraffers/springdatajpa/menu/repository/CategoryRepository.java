package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    /*finall 메서드를 사용할 수 있지만 jpql 또는 native sqlfh wkrtjdgkf tneh dlTdmadmf ghkrdlsgkr.
    * */
//    @Query(value = "SELECT m From Category  m order by m.categoryCode ASC")
    @Query(value = "SELECT category_code, category_name, ref_category_code FROM tbl_category "
    + "ORDER BY category_code asc", nativeQuery = true)
    List<Category> findAllCategory();
}
