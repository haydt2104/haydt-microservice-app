package handlers

import (
	"net/http"
	"product-service/internal/models"
	"product-service/internal/services"

	"github.com/gin-gonic/gin"
)

// GetAllUsersHandler godoc
// @Summary Get all users
// @Description Retrieve all users from the database
// @Tags Users
// @Produce json
// @Success 200 {array} models.User
// @Failure 500 {object} map[string]interface{}
// @Router /user [get]
func GetAllUsersHandler(c *gin.Context) {
	var users []*models.User
	users, err := services.GetAllUsers()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Internal Server Error"})
		return
	}

	c.JSON(http.StatusOK, users)
}
