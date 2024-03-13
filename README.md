# Тестовое задание на позицию Intern Java Developer в ВК. Дмитриев Артем Вадимович.

---

## Реализовано:
- Эндпоинты **GET, POST, PUT, DELETE**:
    - /api/posts/**
    - /api/users/**
    - /api/albums/**
- Bearer авторизация со следующими ролями:
  - **ROLE_ADMIN**
  - **ROLE_POSTS**
  - **ROLE_USERS**
  - **ROLE_ALBUMS**
  - **ROLE_POSTS_VIEWER**
  - **ROLE_USERS_VIEWER**
  - **ROLE_ALBUMS_VIEWER**
- Аудит действий пользователей (включая сохранения логов в БД)
- Эндпоинты для создания пользователей (включая сохранение данных пользователей в БД)
- In-memory cache (можно было через @Cacheable, но я решил попробовать написать свою мини-версию кеша :) )
- Юнит-тесты
---

## От себя:
- Добавлена Postman коллекция для удобства локального тестирования
- Добавлена контейнеризация
- Добавлен Github Action CI пайплайн для автоматизации процесса сборки контейнера и отправки на удаленный репозиторий
- Добавлен docker-compose для развертывания приложения. Для запуска приложения требуется в корне проекта выполнить в cmd:
```
docker-compose up --build
```
- Добавлен helm-chart для развертывания приложения в k8s. Чарт умеет принимать переменные окружения через флаг,
в котором передается путь до файла **.yml** с переменными окружения. Пример использования:
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install postgresql bitnami/postgresql -f postgres-values.yml --wait --timeout 180s
helm upgrade --install proxy-service ./helm-chart-k8s \
    --set fullnameOverride=proxy-service \
    --set image.repository=avdm2/vk_backend_intern \
    --set image.tag=latest \
    -f values.yml \
    --wait --timeout 180s
```
где **postgres-values**:
```yaml
auth:
  enablePostgresUser: true
  postgresPassword: "postgresPassword"
  username: "username"
  password: "password"
  database: "database"
```
а **values.yml**:

```yaml
replicaCount: 1

extraEnv:
  - name: DB_HOST
    value: "postgresql"
  - name: DB_PORT
    value: "5432"
  - name: DB_NAME
    value: "service"
  - name: DB_USER
    value: "user"
  - name: DB_PASSWORD
    value: "pass"
  - name: CACHE_SIZE
    value: "100"
  - name: JWT_DURATION_MINUTES
    value: "60"
  - name: JWT_SECRET
    value: "secret"
  - name: ...
    value: ...
```