package com.testdemo01.entity;

import java.io.Serializable;
// import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class BaseEntity implements Serializable{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    // private LocalDateTime created;
    // private LocalDateTime updated;
    // private Integer statu;
}
