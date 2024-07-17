
# Stage 1: Build stage
FROM maven:3.8.4-openjdk-17 AS build

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file pom.xml và các file cấu hình liên quan
COPY pom.xml ./

# Download các dependencies
RUN mvn dependency:go-offline

# Sao chép mã nguồn
COPY src ./src

# Build ứng dụng
RUN mvn clean package -DskipTests

# Stage 2: Run stage
FROM openjdk:17-jdk-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR từ build stage
COPY --from=build /app/target/myapp.jar /app/myapp.jar

# Mở cổng 8080
EXPOSE 8080

# Định nghĩa lệnh để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "myapp.jar"]
