package com.wt.mention.bean;

import com.wt.mention.edit.listener.InsertData;

import java.io.Serializable;
import java.util.Objects;

/**
 * 话题
 */
public class MentionTopic implements Serializable, InsertData {

    private final CharSequence topicName;
    private final CharSequence topicId;

    public MentionTopic(CharSequence topicId, CharSequence topicName) {
        this.topicName = topicName;
        this.topicId = topicId;
    }

    public CharSequence getTopicName() {
        return topicName;
    }

    public CharSequence getTopicId() {
        return topicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MentionTopic tag = (MentionTopic) o;

        if (!Objects.equals(topicName, tag.topicName))
            return false;
        if (!Objects.equals(topicId, tag.topicId)) return false;
        return Objects.equals(topicId, tag.topicId);
    }

    @Override
    public int hashCode() {
        int result = topicName != null ? topicName.hashCode() : 0;
        result = 31 * result + (topicId != null ? topicId.hashCode() : 0);
        return result;
    }

    @Override
    public CharSequence charSequence() {
        return "#" + topicName + " ";
    }

    @Override
    public FormatRange.FormatData formatData() {
        return new TopicConvert(this);
    }

    @Override
    public int color() {
        return 0xFF14D0B4;
    }

    private class TopicConvert implements FormatRange.FormatData {
        private final MentionTopic tag;

        public TopicConvert(MentionTopic tag) {
            this.tag = tag;
        }

        @Override
        public FormatItemResult formatResult() {
            FormatItemResult userResult = new FormatItemResult();
            userResult.setId(tag.getTopicId().toString());
            userResult.setName(tag.getTopicName().toString());
            return userResult;
        }
    }
}
