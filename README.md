# Aplicação fullstack para o gerenciamento de servidores

# Sobre o projeto

É uma aplicação para realizar o gerenciamento de servidores, que são compostos pelos seguintes campos:

- Id
- Name
- IpAdress
- Memory
- Type
- ImageUrl
- Status

A aplicação consiste em um sistema onde o usuário, após a autenticação, tem a possibilidade de gerenciar seus servidores e verificar se eles estão rodando (Server_up) ou não (Server_down). O usuário pode inserir, deletar , acessar os servidores e gerar uma planilha no excel com as informações dos servidores, entretanto apenas administradores poderam realizar o crud de usuários. Estamos utilizando para autenticação e autorização de acesso aos recursos o token JWT

# Tecnologias utilizadas
## Back-End
- Java
- Spring Boot
- Spring Security
- Jpa/Hibernate
- Postgresql
- Maven
- Docker

## Front-End
- Html / CSS / JS
- React.Js