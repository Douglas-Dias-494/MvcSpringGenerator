package ${packageName};

import java.util.List;
import java.util.Optional;
import ${modelPackage}.${className};


public interface ${className}Service {
List<${className}> findAll();
${className} save(${className} entity);
void delete (Long id);
Optional<${className}> findById(Long id);
}