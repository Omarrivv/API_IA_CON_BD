package pe.edu.vallegrande.spring_webflux.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("gemini")
public class Gemini {

    @Id
    private Long id;

    @Column("content")
    private String content;

    @Column("answer")
    private String answer;

    @Column("status")
    private String status = "A";

    public Gemini() {
    }

    public Gemini(Long id, String content, String answer, String status) {
        this.id = id;
        this.content = content;
        this.answer = answer;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 