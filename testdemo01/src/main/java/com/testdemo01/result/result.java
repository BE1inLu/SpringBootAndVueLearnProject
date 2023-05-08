package com.testdemo01.result;

import java.io.Serializable;

import lombok.Data;

@Data
public class result implements Serializable {

    // 字段定义
    public int code;
    public String msg;
    Object obj;


    /**
     * @param obj
     * @return
     */
    public static result succ(Object obj) {
        return succ(200, "success", obj);
    }

    /**
     * @param code
     * @param string
     * @param obj
     * @return
     */
    public static result succ(int code, String s, Object obj) {
        result r = new result();
        r.setCode(code);
        r.setMsg(s);
        r.setObj(obj);
        return r;
    }


    /**
     * @param obj
     * @return
     */
    public static result fail(Object obj) {
        return fail(400, "fail", obj);
    }

    /**
     * @param code
     * @param string
     * @param obj
     * @return
     */
    public static result fail(int code, String s, Object obj) {
        result r = new result();
        r.setCode(code);
        r.setMsg(s);
        r.setObj(obj);
        return r;
    }

}
