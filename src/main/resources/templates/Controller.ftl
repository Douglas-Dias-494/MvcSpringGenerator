package ${packageName};

import org.springframework.web.bind.annotation.*;
import java.util.List;
import ${modelPackage}.${className};
import ${servicePackage}.${className}Service;

@RestController
@RequestMapping("/api/${className?lower_case}")
public class ${className}Controller {

private final ${className}Service service;

public ${className}Controller(${className}Service service) { this.service = service; }

@GetMapping("/suaUrl")
public List<${className}> getAll() { return service.findAll(); }

@PostMapping("/suaUrl")
public ${className} create(@RequestBody ${className} entity) { return service.save(entity); }

@DeleteMapping("/suaUrl")
public void delete(@RequestBody Long id) { service.delete(id); }

}
