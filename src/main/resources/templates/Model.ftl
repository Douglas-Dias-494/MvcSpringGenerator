package ${packageName};
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@Entity
public class ${className} {
private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

<#list fields?keys as fieldName>
    ${fields[fieldName]} ${fieldName};
</#list>
}