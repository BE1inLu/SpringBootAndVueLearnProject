package com.testdemo01.common.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class PassDto implements Serializable {
    
    // @NotBlank(message = "新密码不能为空")
	private String password;

	// @NotBlank(message = "旧密码不能为空")
	private String currentPass;

}
