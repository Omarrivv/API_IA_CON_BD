package pe.edu.vallegrande.spring_webflux.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "meta_llama")
public class MetaLlama {

    @Id
    private Long id;

    @Column(value = "content")
    private String content;  // Lo que envías como pregunta (content del role "user")

    @Column(value = "answer")
    private String answer;    // Lo que recibes como respuesta (content del role "assistant")

    public MetaLlama() {
    }

    public MetaLlama(Long id, String content, String answer) {
        this.id = id;
        this.content = content;
        this.answer = answer;
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
}
