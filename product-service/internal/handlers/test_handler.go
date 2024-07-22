package handlers

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

// TestHandler godoc
// @Summary Test API
// @Description Test API
// @Tags Test
// @Produce json
// @Success 200 {object} map[string]interface{}
// @Failure 500 {object} map[string]interface{}
// @Router /test [get]
func TestHandler(c *gin.Context) {
	response := map[string]interface{}{
		"status":  "success",
		"message": "Hello, World!",
	}

	c.JSON(http.StatusOK, response)
}
