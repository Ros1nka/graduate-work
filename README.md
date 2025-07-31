# Платформа объявлений (Ads Platform)
### Общая информация
Проект представляет собой REST API сервис для размещения и управления объявлениями с возможностью комментирования. Система включает аутентификацию пользователей, управление объявлениями и комментариями, загрузку изображений.

## Технологический стек

  - Язык:  Java 11
  - Фреймворк:  Spring Boot 3.5.0
  - База данных:	PostgreSQL (основная), H2 (для тестов)
  - ORM:	Spring Data JPA + Hibernate
  - Безопасность:	Spring Security + JWT
  - Документация:	OpenAPI 3 (SpringDoc)
  - Миграции: БД	Liquibase
  - Логирование:	SLF4J
  - Валидация:	Jakarta Validation
  - Сборка:	Maven
  - Тестирование:  JUnit 5, Mockito, Spring Security Test, Spring Boot Test

## Ключевые функции API
1. Аутентификация и авторизация
 * Регистрация новых пользователей (/register)
 * Вход в систему (/login)
 * Ролевая модель: USER, ADMIN
 * JWT-аутентификация

2. Управление пользователями
  - Смена пароля (/users/set_password)
  - Просмотр/редактирование профиля (/users/me)
  - Загрузка аватара (/users/me/image)

3. Работа с объявлениями

| Метод | Эндпоинт| Функционал |
|-------------|-------------|-------------|
| GET   | /ads   | Получение всех объявлений    |
| POST    | /ads    | Создание объявления с изображением    |
| GET    | /ads/{id}   | Просмотр объявления   |
| PATCH    | /ads/{id}    |  Обновление объявления   |
| DELETE    | /ads/{id}    |  Удаление объявления   |
| PATCH    |  /ads/{id}/image   |  Обновление изображения объявления  |
| GET    |  /ads/me   |  Получение своих объявлений   |


4. Комментарии к объявлениям
- Добавление комментариев (/ads/{id}/comments)
- Редактирование/удаление комментариев
- Проверка прав (только автор или админ)

5. Дополнительные возможности
- Валидация входных данных
- Глобальная обработка исключений
- Логирование операций
- CORS-фильтр для безопасных跨域-запросов

## Архитектура Backend
Слои приложения:
- Controller (REST API endpoints)
- AdsController, UserController, AuthController
- Service (Бизнес-логика)
- AdsServiceImpl, UserServiceImpl, AuthServiceImpl
- Repository (Работа с БД)
- AdRepository, UserRepository, CommentRepository
- DTO (Data Transfer Objects)
- Ad, UserDTO, Comment, etc.
- Entity (Сущности БД)
- AdEntity, UserEntity, CommentEntity
  
Особенности реализации:
- Транзакционность (@Transactional)
- Разделение прав доступа (@PreAuthorize)
- Загрузка файлов (изображений)
- Кастомные исключения
- Логирование через SLF4J

## Инфраструктура и развертывание
Конфигурация:
- Настройки БД и путей в application.properties
- Liquibase для управления миграциями
- Spring Security Config (WebSecurityConfig)

Зависимости (основные):

xml

<dependencies>
  
      <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    
    <!-- Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- Documentation -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.8.9</version>
    </dependency>
    
    <!-- Utils -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    
</dependencies>

## Как запустить проект
Требования:

Java 11

PostgreSQL

Maven

### Настройка БД:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/graduate
spring.datasource.username=
spring.datasource.password=

### Сборка и запуск:

bash
mvn clean install
mvn spring-boot:run

Документация API:

Доступна по адресу: http://localhost:8080/swagger-ui.html
