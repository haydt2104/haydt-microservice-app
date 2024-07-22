package routes

import (
	"product-service/internal/handlers"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
)

// SetupRoutes đăng ký các route cho ứng dụng
func SetupRoutes(r *gin.Engine) {
	r.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))
	r.GET("/user", gin.HandlerFunc(handlers.GetAllUsersHandler))
	r.GET("/test", gin.HandlerFunc(handlers.TestHandler))
}
