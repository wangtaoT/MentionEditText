package com.wt.mention.bean;

import com.wt.mention.edit.listener.InsertData;

import java.io.Serializable;
import java.util.Objects;

/**
 * 艾特用户
 */
public class MentionUser implements Serializable, InsertData {

    private final CharSequence userId;
    private final CharSequence userName;

    public MentionUser(CharSequence userId, CharSequence userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public CharSequence getUserId() {
        return userId;
    }

    public CharSequence getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MentionUser user = (MentionUser) o;

        if (!Objects.equals(userId, user.userId)) return false;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    @Override
    public CharSequence charSequence() {
        return "@" + userName + " ";
    }

    @Override
    public FormatRange.FormatData formatData() {
        return new UserConvert(this);
    }

    @Override
    public int color() {
        return 0xFF1977FD;
    }

    private class UserConvert implements FormatRange.FormatData {

        private final MentionUser user;

        public UserConvert(MentionUser user) {
            this.user = user;
        }

        @Override
        public FormatItemResult formatResult() {
            FormatItemResult userResult = new FormatItemResult();
            userResult.setId(user.getUserId().toString());
            userResult.setName(user.getUserName().toString());
            return userResult;
        }
    }
}
