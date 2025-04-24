package pe.edu.vallegrande.spring_webflux.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@AllArgsConstructor
@ToString
@Table(name = "aito_human")
public class AitoHuman {
    @Id
    private Long id;

    @Column(value = "text")
    private String text;

    @Column(value = "result")
    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
