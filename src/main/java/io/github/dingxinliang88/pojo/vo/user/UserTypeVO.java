package io.github.dingxinliang88.pojo.vo.user;

import io.github.dingxinliang88.constants.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
@Setter
public class UserTypeVO implements Serializable {

    private static final long serialVersionUID = CommonConstant.SYS_SERIALIZABLE_ID;


    private Boolean isLeader;

    private Boolean isAdmin;

    private Boolean isMember;

    private Boolean isFan;


}
