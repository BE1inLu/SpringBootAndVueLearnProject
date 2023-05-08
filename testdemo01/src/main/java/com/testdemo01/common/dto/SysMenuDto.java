package com.testdemo01.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

// 菜单接口返回数据定义
@Data
public class SysMenuDto implements Serializable{
    private Long id;
    private String title;
    private String icon;
    private String path;
    private String name;
    private String component;
    List<SysMenuDto> children=new ArrayList<>();
}
