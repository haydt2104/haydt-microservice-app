package services

import (
	"log"
	"product-service/internal/database"
	"product-service/internal/models"
)

func GetAllUsers() ([]*models.User, error) {
	var users []*models.User
	db := database.GetDB()

	rows, err := db.Query("SELECT id, name, email, password, role FROM users")
	if err != nil {
		log.Printf("Error querying users: %v", err)
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var user models.User
		if err := rows.Scan(&user.ID, &user.Name, &user.Email, &user.Password, &user.Role); err != nil {
			log.Printf("Error scanning user: %v", err)
			return nil, err
		}
		users = append(users, &user)
	}

	if err := rows.Err(); err != nil {
		log.Printf("Error iterating over rows: %v", err)
		return nil, err
	}

	return users, nil
}
