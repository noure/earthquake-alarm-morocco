# Earthquake Alarm Application

## Overview

The Earthquake Alarm is a Spring Boot application designed to monitor and notify users about significant seismic activities, specifically in Morocco. This application integrates with the USGS Earthquake Data API to fetch live information and leverages the Twilio SMS API to notify users. It has built-in support for rate-limiting, caching, and automatic notification.

![GitHub Workflow Status](https://github.com/noure/earthquake-alarm-morocco/actions/workflows/spring-boot.yaml/badge.svg)

---


## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Development](#development)
- [Support](#support)
- [License](#license)

---

## Features

- **Live Monitoring**: Fetches real-time earthquake information from the USGS Earthquake Data API.
- **Notifications**: Sends SMS notifications to registered users when a significant earthquake is detected.
- **Rate Limiting**: Ensures no more than a specific number of SMS are sent per second, minute, or hour.
- **Dynamic Updates**: Users can text "update" to receive the latest earthquake information.
- **Localization**: Messages can be sent in both English and Arabic.
- **Caching**: Remembers last sent notification to avoid spamming users.
- **Database Support**: Uses an H2 database to keep track of sent notifications and phone numbers.

---

## Requirements

- Java 17 or higher
- Maven 3.6.x
- Twilio Account
- USGS Earthquake API access (free and public)

---

## Quick Start

1. **Clone the repository:**

    ```bash
    git clone https://github/noure/earthquake-alarm-morocco.git
    ```

2. **Navigate to the project folder and open `application.properties`. Replace placeholders for Twilio Account SID, Auth Token, and Phone numbers.**

3. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

4. **The application will start, and you can now test the functionalities.**

---

## API Endpoints

- **GET /api/earthquake**: Fetches the latest earthquake information.
- **POST /sms-webhook**: Twilio webhook to handle incoming SMS.

---

## Configuration

- You can update the rate limits and the caching time in the `application.properties` file.
- You can add or remove the phone numbers in the `application.properties` file.

---

## Development

For development, you can access the H2 database console at `http://localhost:8080/h2-console`.

---

## Support

For any issues or enhancements, please open an issue on the GitHub repository or contact the maintainer at nour (Dot) labihi (AT) gmail (dot) com

---

## License

MIT License. See `LICENSE` for more information.

---
