spring.application.name=${applicationName}

# H2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Configuração do banco H2
spring.datasource.url=jdbc:h2:mem:testedb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Criação automática das tabelas
spring.jpa.hibernate.ddl-auto=update
