package ${packageName};
import lombok.Data;

@Data
public class ${className} {
    <#list fields?keys as fieldName>
        private ${fields[fieldName]} ${fieldName};
    </#list>
}