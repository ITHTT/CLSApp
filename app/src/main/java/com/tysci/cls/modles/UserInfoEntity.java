package com.tysci.cls.modles;

/**
 * Created by Administrator on 2016/7/5.
 */
public class UserInfoEntity {

    /**
     * account : 18773161648
     * cellPhone : 18773161648
     * profile : {"nickname":"老烟枪","portrait":"images/users/portrait/default/1.png"}
     */

    private String account;
    private String cellPhone;
    /**
     * nickname : 老烟枪
     * portrait : images/users/portrait/default/1.png
     */

    private UserProfileEntity profile;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void setProfile(UserProfileEntity profile) {
        this.profile = profile;
    }

    public String getAccount() {
        return account;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public UserProfileEntity getProfile() {
        return profile;
    }

    public static class UserProfileEntity {
        private String nickname;
        private String portrait;

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getNickname() {
            return nickname;
        }

        public String getPortrait() {
            return portrait;
        }
    }
}
