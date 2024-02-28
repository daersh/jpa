package com.ohgiraffers.springdatajpa.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PagingButtonInfo {
    /*페이지 정보 처리를 위한 것*/
    private int currentPage;
    private int startPage;
    private int endPage;
}
