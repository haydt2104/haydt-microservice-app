package database

import (
	"database/sql"
	"fmt"
	"log"
	_ "github.com/go-sql-driver/mysql"
)

var db *sql.DB

func Initialize() {
    dsn := GetDatabaseDSN()
    var err error
    db, err = sql.Open("mysql", dsn)
    if err != nil {
        log.Fatalf("Failed to connect to database: %v", err)
    }

    if err := db.Ping(); err != nil {
        log.Fatalf("Failed to ping database: %v", err)
    }
}

func GetDatabaseDSN() string {

    // user := os.Getenv("DB_USER")
    // password := os.Getenv("DB_PASSWORD")
    // host := os.Getenv("DB_HOST")
    // port := os.Getenv("DB_PORT")
    // dbname := os.Getenv("DB_NAME")

    user := "haydt"
    password := "haydt123"
    host := "localhost"
    port := "3307"
    dbname := "hay_data"

    return fmt.Sprintf("%s:%s@tcp(%s:%s)/%s", user, password, host, port, dbname)
}

func GetDB() *sql.DB {
    return db
}
