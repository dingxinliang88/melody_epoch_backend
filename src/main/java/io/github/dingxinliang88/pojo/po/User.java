package io.github.dingxinliang88.pojo.po;

/**
 * 用户信息
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public class User {

    /**
     * 用户ID
     */
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
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
