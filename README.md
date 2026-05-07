# 🎵 Music App

A full-stack music library application with a modern Vue 3 frontend and Spring Boot backend.

## 📋 Features

- **Music Library Management** - Browse and manage your track collection
- **Track Filtering** - Filter tracks by artist
- **Now Playing Bar** - Real-time track playback with progress tracking
- **Responsive Design** - Works seamlessly on desktop and mobile devices
- **Modern UI** - Dark theme with elegant typography and smooth animations
- **RESTful API** - Backend API for track data management

## 🏗️ Architecture

```
MusicApp/
├── backend/          # Spring Boot REST API (Java 21)
│   ├── pom.xml
│   └── src/
└── frontend/         # Vue 3 + Vite SPA
    ├── package.json
    └── src/
        └── view/
            ├── TrackWall.vue    # Main music library view
            └── Home.vue         # Home page
```

## 🛠️ Tech Stack

### Backend
- **Java 21**
- **Spring Boot 4.0.3**
- **Spring Web MVC**
- **Maven**

### Frontend
- **Vue 3** (Composition API)
- **Vite** (Build tool)
- **CSS Grid** (Layout)

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Node.js 16+ & npm
- Maven

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend will start on `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
Frontend will start on `http://localhost:5173`

### Build for Production
```bash
# Frontend
cd frontend
npm run build

# Backend
cd backend
mvn package
```

## 📡 API Endpoints

- `GET /api/track` - Fetch all tracks

### Expected Track Structure
```json
{
  "trackId": "string",
  "title": "string",
  "artistName": "string",
  "album": {
    "title": "string"
  },
  "release_date": "YYYY-MM-DD",
  "duration_seconds": number
}
```

## 🎨 Components

### TrackWall.vue
Main music library component featuring:
- Track list with metadata (album, year, duration)
- Artist filter buttons
- Playback simulation with progress bar
- Animated now-playing indicator

## ⚙️ Configuration

### Frontend Environment Variables
Create a `.env` file in the `frontend` directory:
```env
VITE_API_URL=http://localhost:8080/api/track
```

## 📱 Features Breakdown

### Track Display
- Sortable columns: #, Title, Album, Year, Duration
- Responsive grid layout (adapts to mobile)
- Real-time filtering by artist

### Playback
- Click any track to play
- Visual feedback with animated bars
- Progress tracking in fixed bottom bar
- Auto-stop at track end

### UI/UX
- Smooth fade-up animations
- Hover effects on rows
- Dark theme with accent colors
- Grain texture overlay

## 📸 Screenshots

*Coming soon*

## 🤝 Contributing

Contributions are welcome! Feel free to submit issues and pull requests.

## 📝 License

This project is open source.

## 👨‍💻 Author

**Binh** - [GitHub](https://github.com/Luxje)

---

**Note:** This is a personal project for learning and development purposes.
