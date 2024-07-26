package main

import (
	_ "product-service/docs"
	"product-service/internal/database"
	"product-service/internal/routes"

	"github.com/gin-gonic/gin"
)

// @title Todo Application
// @description This is product management application
// @version 1.0
// @host localhost:7070
// @BasePath /
func main() {
	// Initialize the database
	database.Initialize()

	r := gin.Default()

	// Đăng ký các route
	routes.SetupRoutes(r)

	// Chạy server trên cổng 8080
	r.Run(":8082")
}
