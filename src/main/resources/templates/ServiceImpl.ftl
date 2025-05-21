package ${packageName};

import org.springframework.stereotype.Service;
import java.util.List;
import ${modelPackage}.${className};
import ${repositoryPackage}.${className}Repository;
import ${packageName}.${className}Service;

@Service
public class ${className}ServiceImpl implements ${className}Service {

private final ${className}Repository repository;

public ${className}ServiceImpl(${className}Repository repository) {
this.repository = repository;
}

@Override
public List<${className}> findAll() {
return repository.findAll();
}

@Override
public ${className} save(${className} entity) {
return repository.save(entity);
}
}
