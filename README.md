# News Parser Application

This is a CRON-based application that parses news from a selected website every 20 minutes and stores the news in a MySQL database. The application also provides a JavaFX interface for displaying the news and allows basic CRUD operations for managing news.

## Project Features
- **News Parsing**: Automatically parses news from a specified website every 20 minutes.
- **MySQL Database Integration**: Stores parsed news in a MySQL database.
- **JavaFX UI**: Displays news items with a simple UI for switching between them using arrow buttons.
- **Time Period Filter**: Allows filtering of news based on a selected time period (morning, day, evening).
- **Daily News Management**: Stores news for the current day only, providing a mechanism for cleaning up old news.
- **CRUD Operations**: Users can manage (create, read, update, delete) news items from the JavaFX interface.

## Technologies Used
- **Java 17**
- **Spring Boot** for backend services
- **JavaFX** for UI
- **MySQL** for database
- **Lombok** for reducing boilerplate code
- **Docker** for containerizing the MySQL database
- **Gradle** as the build tool
- **Springdoc OpenAPI** for documentation and Swagger UI
- **Jackson** for JSON serialization and deserialization

## Module Overview

## `news-parser-service`

### `server`
- **Purpose**: Handles the core logic of news processing, including saving, updating, and deleting news in the database.
- **Features**:
    - CRUD operations for managing news items.
    - Logic for filtering news based on time periods (morning, day, evening).

### `client`
- **Purpose**: Provides a graphical user interface for displaying news and managing news entries.
- **Features**:
    - Scheduled CRON jobs for periodic news fetching.
    - Responsible for fetching news from the external website and saving them to the MySQL database.
    - Simple UI with navigation arrows for browsing through news.
    - CRUD management from the UI.

## Running the Application

### 1. Setup MySQL Database using Docker
Run the following command to start the MySQL database in a Docker container using the provided `docker-compose.yml` file:
```bash
docker-compose up -d
```
## Running the Application

### 2. Running the Server and Client
1) start the server
2) start the client 

### 3. CRON Expression Configuration
The client’s configuration file contains options for modifying the CRON expressions for news parsing and database cleanup. You can change these settings as needed for:

- News Parsing Interval: Adjust the interval at which news is fetched.
  The app schedules scraping every 20 minutes
```bash
  client/src/main/resources/application.yml
  
  client:
    cron:
      parse: "0 */20 * * * *"
```

- Database Cleanup: Set the frequency for removing old news.
  The application is set to delete news every day at 00:00, to store only news for actual day in the database
```bash
  client/src/main/resources/application.yml
  
  client:
    cron:
      parse: "0 0 0 * * ?"
```
