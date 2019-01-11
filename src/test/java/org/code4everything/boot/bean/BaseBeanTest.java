package org.code4everything.boot.bean;

import org.junit.Test;

/**
 * @author pantao
 * @since 2019/1/11
 **/
public class BaseBeanTest implements BaseBean {

    @Test
    public void test() {
        User user = new User();
        user.setAge(28);
        user.setPassword("123456");
        user.setUsername("god");
        System.out.println(user);
        UserVO userVO = new UserVO();
        System.out.println(userVO);
        user.copyTo(userVO);
        System.out.println(userVO);
    }
}

class UserVO implements BaseBean {

    private String username;

    private Integer age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserVO{" + "username='" + username + '\'' + ", age=" + age + '}';
    }
}

class User implements BaseBean {

    private String username;

    private Integer age;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", age=" + age + ", password='" + password + '\'' + '}';
    }
}

