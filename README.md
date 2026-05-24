# 🎵 Music App

A full-stack music library application with a modern Vue 3 frontend and Spring Boot backend.

## 📋 Features

- **User Authentication** - Secure login and sign-up functionality
- **Music Library Management** - Browse and manage your track collection
- **Track Filtering** - Filter tracks by artist
- **Upload Tracks** - Upload new audio files with metadata
- **Audio Visualization** - Waveform display with WaveSurfer.js
- **Now Playing Bar** - Real-time track playback with progress tracking
- **Audio Streaming** - Stream audio files from the backend
- **Album Management** - Organize tracks by albums
- **Responsive Design** - Works seamlessly on desktop and mobile devices
- **Modern UI** - Dark theme with elegant typography and smooth animations
- **RESTful API** - Backend API for track data management

## 🏗️ Architecture

```
MusicApp/
├── backend/          # Spring Boot REST API (Java 21)
│   ├── pom.xml
│   └── src/
│       └── main/java/com/example/MusicApp/
│           ├── service/        # Business logic (Track, Auth, Album, Streaming)
│           ├── controller/      # API endpoints
│           └── security/        # Security configuration
└── frontend/         # Vue 3 + Vite SPA
    ├── package.json
    └── src/
        ├── components/     # Reusable UI components
        ├── services/       # API & audio services
        ├── styles/         # Global styles
        ├── guide/          # User guides
        └── view/
            ├── Home.vue            # Home page
            ├── Login.vue           # Authentication view
            ├── TrackWall.vue       # Main music library view
            └── UploadTrack.vue     # Track upload view
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
- **WaveSurfer.js** (Audio visualization)

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
- `POST /api/track/upload` - Upload a new track
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration
- `GET /api/album` - Fetch all albums
- `GET /api/stream/{trackId}` - Stream audio file

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

### Login.vue
User authentication component featuring:
- Email and password validation
- Sign-up and login toggle
- Password visibility toggle
- Form error handling

### UploadTrack.vue
Track upload component with:
- Drag-and-drop file upload zone
- Audio file input handling
- Waveform preview
- Track metadata form

### TrackWall.vue
Main music library component featuring:
- Track list with metadata (album, year, duration)
- Artist filter buttons
- Playback simulation with progress bar
- Animated now-playing indicator
- Audio visualization

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
