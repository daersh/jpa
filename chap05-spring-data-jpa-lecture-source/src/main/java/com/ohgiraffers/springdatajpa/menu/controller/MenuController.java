package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.common.Pagination;
import com.ohgiraffers.springdatajpa.common.PagingButtonInfo;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    private final MenuService menuService;

    /* 설명. 로그 생성해 보기 */
//    Logger logger = LoggerFactory.getLogger(MenuController.class);
//    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model) {
//        logger.info("menuCode: {}", menuCode);
        log.info("menuCode: {}", menuCode);

        MenuDTO menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);

        return "menu/detail";
    }

    /*paging 전*/
//    @GetMapping("/list")
//    public String findMenuList(Model model){
//        List<MenuDTO> menuList = menuService.findMenuList();
//        model.addAttribute("menuList",menuList);
//        return "menu/list";
//    }

    /*페이징 후*/
    @GetMapping("/list")
    public String findMenuList(@PageableDefault Pageable pageable, Model model){
        log.info("pageable: {}",pageable);
        Page<MenuDTO> menuList = menuService.findMenuList(pageable);
        log.info("조회한 내용 목록: {}", menuList.getContent());
        log.info("총 페이지: {}", menuList.getTotalPages());
        log.info("총 메뉴 수: {}", menuList.getTotalElements());
        log.info("해당 페이지에 표시될 요소 수: {}", menuList.getSize());
        log.info("해당 페이지에 표시된 요소 수: {}", menuList.getNumberOfElements());
        log.info("첫 페이지 여부: {}", menuList.isFirst());
        log.info("마지막 페이지 여부: {}", menuList.isLast());
        log.info("정렬 방식: {}", menuList.getSort());
        log.info("여러 페이지 중 현제 인덱스: {}", menuList.getNumber());

//        model.addAttribute("menuList",menuList);

        /*화면에서 버튼을 그리기 위한 재료 준비(클래스(모듈화) 두 개 추가)*/
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(menuList);
        model.addAttribute("paging",paging);
        model.addAttribute("menuList",menuList);

        return "menu/list";
    }

}
