package io.github.dingxinliang88.pojo.dto.fan;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 乐迷修改个人信息请求
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class EditInfoReq implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * 乐队成员ID
     */
    @NotNull
    private Integer fanId;

    /**
     * 乐队成员姓名
     */
    @Length(min = 1, max = 30, message = "姓名长度为1-30位")
    private String name;

    /**
     * 性别
     */
    @Max(value = 1, message = "无效性别")
    @Min(value = 0, message = "无效性别")
    private Integer gender;

    /**
     * 年龄
     */
    @Max(value = 120, message = "年龄不能超过120岁")
    @Min(value = 1, message = "年龄不能小于1岁")
    private Integer age;

    /**
     * 职业
     */
    @Length(min = 1, max = 32, message = "职业长度为1-32位")
    private String career;

    /**
     * 学历
     */
    @Length(min = 1, max = 16, message = "学历长度为1-16位")
    private String education;

}