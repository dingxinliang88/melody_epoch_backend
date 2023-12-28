package io.github.dingxinliang88.pojo.vo.fan;

import io.github.dingxinliang88.constants.CommonConstant;
import io.github.dingxinliang88.pojo.po.Fan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class FanInfoVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;

    /**
     * id
     */
    private Integer fanId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 职业
     */
    private String career;

    /**
     * 学历
     */
    private String education;

    public static FanInfoVO fanToVO(Fan fan) {
        FanInfoVO fanInfoVO = new FanInfoVO();
        fanInfoVO.setFanId(fan.getFanId());
        fanInfoVO.setName(fan.getName());
        fanInfoVO.setGender(fan.getGender());
        fanInfoVO.setAge(fan.getAge());
        fanInfoVO.setCareer(fan.getCareer());
        fanInfoVO.setEducation(fan.getEducation());
        return fanInfoVO;
    }
}
