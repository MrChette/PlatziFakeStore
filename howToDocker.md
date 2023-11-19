1. **Create a Dockerfile in Each Microservice**

2. **Build Microservices:**
   - Build all microservices: `docker-compose build`
   - Build a specific microservice: `docker-compose build <service-name>` (e.g., `docker-compose build eureka-server`)

3. **Run Microservices:**
   - Run all microservices: `docker-compose up`
   - Run a specific microservice: `docker-compose up <service-name>` (e.g., `docker-compose up eureka-server`)

4. **Stop and Delete:**
   - Stop and delete all containers: `docker-compose down`
   - Stop and delete a specific container: `docker-compose down <service-name>` (e.g., `docker-compose down eureka-server`)
