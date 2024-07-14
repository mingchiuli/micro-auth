package org.chiu.micro.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author limingjiu
 * @Date 2024/4/20 18:17
 **/
@Data
public class MenusAndButtonsVo {

    private List<MenuVo> menus;

    private List<ButtonVo> buttons;

}
