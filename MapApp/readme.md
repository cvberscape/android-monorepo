# Running data visualizer application

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)

An Android application demonstrating real-time data integration with Google Maps. This app showcases dynamic marker display, REST API communication for elevation data, and custom polyline rendering based on live sensor inputs from an accompanying Wear OS app which pushes the data to a firebase database.

---

## Overview

This Android app fuses several mobile development techniques into a single interactive map experience. The application fetches real-time sensor data from Firebase Firestore, retrieves elevation information via the Google Maps Elevation API, and visualizes locations on a Google Map. Markers display combined heart rate and elevation data, while connecting polylines are color-coded based on deviations from the average heart rate. This app is a part of a school project in collaboration with Polar, we recieved smart watches and were tasked to develop an app for the Wear OS watch and an accompanying mobile app. The watch app worked as an exercise tracker which pushed the exact location and heartrate into firebase, this app works by visualizing the workouts and relevant data for the user.

---

## Technologies Used

**Core**  
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=flat&logo=kotlin&logoColor=white)  
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=flat&logo=android-studio&logoColor=white)

**Networking & Data**  
![Volley](https://img.shields.io/badge/Volley-EF2D5E?style=flat)  
![Gson](https://img.shields.io/badge/Gson-FF6D01?style=flat)  
![Firebase Firestore](https://img.shields.io/badge/Firebase%20Firestore-FFCA28?style=flat&logo=firebase)

**Mapping & UI**  
![Google Maps](https://img.shields.io/badge/Google_Maps-4285F4?style=flat&logo=google-maps&logoColor=white)  
![Material Design](https://img.shields.io/badge/Material_Design-757575?style=flat&logo=material-design&logoColor=white)

---

## Project Components & Features

### 🗺 Maps Integration Activity

The heart of the application is the Maps Integration Activity, which merges data retrieval, API communication, and map rendering into one cohesive flow.

- **Features:**
  - **Real-time Data Fetching:** Retrieves sensor data (including latitude, longitude, and heart rate) from Firebase Firestore.
  - **Elevation API Integration:** Constructs a dynamic request to the Google Maps Elevation API to obtain elevation data for each location.
  - **Dynamic Marker Display:** Displays map markers with titles that combine heart rate and elevation values.
  - **Marker Toggle:** Includes a button to show or hide markers on demand.
  - **Color-coded Polylines:** Draws connecting lines between locations with colors based on heart rate comparisons (green, amber, red).

- **Tech Stack:**
  - **Kotlin** for application logic and UI.
  - **Google Maps SDK** for map rendering.
  - **Volley** for REST API requests.
  - **Gson** for JSON parsing.
  - **Firebase Firestore** for real-time data.

---

### 🔢 Data Processing & Visualization

This module handles the data computation and visualization aspects that enhance the mapping experience.

- **Components:**
  - **Data Model (WData):** Represents sensor data including timestamp, heart rate, and geographical coordinates.
  - **Elevation Response Handling:** Processes JSON responses from the elevation API using custom data classes.
  - **Dynamic Polyline Coloring:** Computes the average heart rate from the fetched data and applies conditional styling to polyline segments to reflect deviations.

- **Tech:**
  - Kotlin data classes for modeling.
  - Custom logic for data aggregation and visual updates.
  - Integration of elevation data with location markers for enhanced user insights.

---
