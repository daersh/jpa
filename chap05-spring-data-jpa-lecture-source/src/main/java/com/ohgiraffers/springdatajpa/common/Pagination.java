package com.ohgiraffers.springdatajpa.common;

import org.springframework.data.domain.Page;

public class Pagination {

    public static PagingButtonInfo getPagingButtonInfo(Page page){
        /*매개 변수로 넘어오는 Page 객체는 인덱스 개념(0부터 시작)을 가지고 있어 +1 함*/
        int currentPage = page.getNumber()+1; // 인덱스 개념
        int defaultButtonCount=10; /*한 페이지에 보일 버튼 최대 갯수*/
        int startPage;                        // 한 페이지에 보여질 첫 버튼
        int endPage;                          // 한 페이지에 보여질 마지막 버튼
        startPage = (int)(Math.ceil((double)currentPage/defaultButtonCount)-1) * defaultButtonCount+1;
        endPage = startPage+defaultButtonCount+1;
        if(page.getTotalPages()<endPage)      // 총 페이지 수가 보여질 마지막 버튼보다 작으면
            endPage=page.getTotalPages();     // 곧 총 페이지 수가 마지막 버튼이 된다.
        if(page.getTotalPages()==0)           // 총 페이지가 0이더라도 1페이지는 나오도록 설정
            endPage=startPage;

        return new PagingButtonInfo(currentPage,startPage,endPage);
    }
}
