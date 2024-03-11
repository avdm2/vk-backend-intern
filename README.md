# Тестовое задание на позицию Intern Java Developer в ВК. Дмитриев Артем Вадимович.

## Реализовано:
- Эндпоинты **GET, POST, PUT, DELETE**:
    - /api/posts/**
    - ~~/api/users/**~~
    - ~~/api/albums/**~~
    - ~~/api/comments/**~~
- ~~Авторизация со следующими ролями:~~
  - ~~**ROLE_ADMIN**~~
  - ~~**ROLE_POSTS**~~
  - ~~**ROLE_USERS**~~
  - ~~**ROLE_ALBUMS**~~
  - ~~**ROLE_COMMENTS**~~
- ~~Аудит действий пользователей (включая сохранения логов в БД)~~
- ~~Эндпоинты для создания пользователей (включая сохранение данных пользователей в БД)~~
- ~~Юнит (скорее, интеграционные) тесты~~

---

~~Чтобы компенсировать отсутствие inmemory кэша, от себя добавил контейнеризацию и GitHub Actions CI-пайплайн для 
сборки образа. Ну и Postman коллекция для удобства~~

Добавлен helm-chart для развертывания приложения в k8s. Чарт умеет принимать переменные окружения через флаги.
Пример использования:
```
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
    value: postgresql
  - name: DB_PORT
    value: 5432
  - name: DB_NAME
    value: service
  - name: DB_USER
    value: user
  - name: DB_PASSWORD
    value: pass
```