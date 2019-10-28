package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.InputStream;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "QUESTION", schema = "quora")
@NamedQueries({
        @NamedQuery(name = "QuestionEntityByUuid", query = "select quest from QuestionEntity quest where quest.uuid = :uuid"),
        @NamedQuery(name = "QuestionEntityByid", query = "select quest from QuestionEntity quest where quest.id = :id")
})
public class QuestionEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @Size(max = 64)
    private String uuid;


    @Column(name = "content")
    private String content;


    @Column(name = "date")
    private ZonedDateTime date;


    public UserEntity getUser() {
        return user;
    }



    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser_id() {
        return user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}