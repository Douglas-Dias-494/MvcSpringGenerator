package ${packageName};

import java.util.List;
import ${modelPackage}.${className};


public interface ${className}Service {
List<${className}> findAll();
${className} save(${className} entity);
void delete (${className} entity);
${className} findById(int id);
}