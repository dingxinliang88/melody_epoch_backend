package io.github.dingxinliang88.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Data
@TableName(value = "user")
@NoArgsConstructor
public class User {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 0 - guest, 1 - admin , 2 - member, 3 - fan
     *
     * @see io.github.dingxinliang88.pojo.enums.UserRoleType
     */
    private Integer type;

    /**
     * 账号
     */
    private String nickname;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 逻辑删除标志 0 - 未删除， 1 - 删除
     */
    @TableLogic
    private Integer isDelete;

    public User(String account, String password, String salt, Integer type) {
        this.type = type;
        this.account = account;
        this.password = password;
        this.salt = salt;
    }

    public User(Integer type, String password, String salt) {
        this.type = type;
        this.password = password;
        this.salt = salt;
    }
}
