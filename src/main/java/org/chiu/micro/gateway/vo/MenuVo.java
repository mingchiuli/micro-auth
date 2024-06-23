package org.chiu.micro.gateway.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVo {

    private Long menuId;

    private Long parentId;

    private String title;

    private String name;

    private String url;

    private String component;

    private Integer type;

    private String icon;

    private Integer orderNum;

    private Integer status;

    private List<MenuVo> children = new ArrayList<>();
}
