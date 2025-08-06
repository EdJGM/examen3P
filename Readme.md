# AgroFlow - Microservicios para Gestión Agrícola

## Descripción
Sistema de microservicios para gestión agrícola que incluye:
- **Microservicio Central**: Gestión de agricultores y cosechas
- **Microservicio Facturación**: Generación y gestión de facturas
- **Microservicio Inventario**: Gestión de insumos agrícolas y control de stock

## Tecnologías
- Spring Boot 3.2.0
- Java 17
- PostgreSQL 14
- MySQL 8.0
- RabbitMQ 3.12
- Docker & Docker Compose

## Configuración y Ejecución

### 1. Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose

### 2. Levantar dependencias
```bash
# Iniciar PostgreSQL y RabbitMQ
docker-compose up -d

# Verificar que los servicios estén corriendo
docker-compose ps
```

### 3. Configurar bases de datos
```bash
# Conectar a PostgreSQL y ejecutar el script de inicialización
psql -h localhost -p 5432 -U postgres -d postgres -f init-databases.sql
```

### 4. Compilar y ejecutar los microservicios

#### Microservicio Central
```bash
cd central-service
mvn clean install
mvn spring-boot:run
```
El servicio estará disponible en: http://localhost:8080

#### Microservicio Facturación
```bash
cd facturacion-service
mvn clean install
mvn spring-boot:run
```
El servicio estará disponible en: http://localhost:8082

#### Microservicio Inventario
```bash
cd ms-inventario
mvn clean install
mvn spring-boot:run
```
El servicio estará disponible en: http://localhost:8081

### 5. Verificar funcionamiento

#### Acceso a RabbitMQ Management
- URL: http://localhost:15672
- Usuario: guest
- Contraseña: guest

#### Flujo de prueba completo
1. Crear un agricultor: `POST /api/agricultores`
2. Crear una cosecha: `POST /api/cosechas`
3. Verificar que se generó la factura: `GET /api/facturas`
4. Verificar que el estado de la cosecha cambió a "FACTURADA"

## Endpoints Principales

### Microservicio Central (Puerto 8080)
- `GET /api/agricultores` - Listar agricultores
- `POST /api/agricultores` - Crear agricultor
- `GET/PUT/DELETE /api/agricultores/{id}` - CRUD agricultor
- `GET /api/cosechas` - Listar cosechas
- `POST /api/cosechas` - Crear cosecha (dispara evento RabbitMQ)
- `PUT /api/cosechas/{id}/estado` - Actualizar estado de cosecha

### Microservicio Facturación (Puerto 8082)
- `GET /api/facturas` - Listar facturas
- `GET /api/facturas/pendientes` - Facturas pendientes de pago
- `GET /api/facturas/{id}` - Obtener factura por ID
- `PUT /api/facturas/{id}/pagar` - Marcar factura como pagada
- `POST /api/facturas/manual` - Crear factura manual (para pruebas)

### Microservicio Inventario (Puerto 8081)
- `GET /api/insumos` - Listar insumos
- `GET /api/insumos/{id}` - Obtener insumo por ID
- `POST /api/insumos` - Crear insumo
- `PUT /api/insumos/{id}` - Actualizar insumo
- `DELETE /api/insumos/{id}` - Eliminar insumo
- `GET /actuator/health` - Estado del servicio

## Flujo del Sistema
1. **App móvil** → POST cosecha al **Microservicio Central**
2. **Central** → Guarda en PostgreSQL y publica evento en RabbitMQ
3. **RabbitMQ** → Enruta mensaje a cola_facturacion e inventario
4. **Microservicio Facturación** → Consume mensaje, calcula monto, crea factura
5. **Microservicio Inventario** → Escucha eventos de cosecha, actualiza stock de insumos
6. **Facturación** → Notifica al Central vía REST API el cambio de estado
7. **Inventario** → Envía notificaciones de stock bajo via RabbitMQ

## Estructura de Bases de Datos

### PostgreSQL Central (Puerto 5432)
- **agricultores**: ID, nombre, finca, ubicación, correo
- **cosechas**: ID, agricultor_id, producto, toneladas, estado, factura_id

### PostgreSQL Facturación (Puerto 5433)
- **facturas**: ID, cosecha_id, monto_total, pagado, fecha_emisión

### MySQL Inventario (Puerto 3306)
- **insumos**: ID, nombre, descripción, cantidad_stock, precio_unitario, categoria

## Precios de Referencia (USD/tonelada)
- Arroz/Arroz Oro: $120
- Café Premium: $300
- Café: $250
- Maíz: $180
- Banano: $90
- Otros productos: $100 (default)