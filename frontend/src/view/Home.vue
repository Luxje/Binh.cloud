<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';

const API_URL = import.meta.env.VITE_API_URL || '';
const tracks = ref([]);
const albums = ref([]);
const loading = ref(true);
const error = ref(null);

// Player state
const currentTrack = ref(null);
const isPlaying = ref(false);
const progress = ref(0);
const volume = ref(65);
const currentTime = ref('0:00');
const totalTime = ref('3:45');
let playTimer = null;

const greeting = computed(() => {
  const h = new Date().getHours();
  return h < 12 ? 'Good morning' : h < 18 ? 'Good afternoon' : 'Good evening';
});

const userInitials = ref('AB');

async function fetchData() {
  loading.value = true;
  error.value = null;
  try {
    const [tracksRes, albumsRes] = await Promise.all([
      fetch(`${API_URL}/api/track`),
      fetch(`${API_URL}/api/album`)
    ]);
    
    if (!tracksRes.ok || !albumsRes.ok) throw new Error('Failed to fetch data');
    
    tracks.value = await tracksRes.json();
    albums.value = await albumsRes.json();
  } catch (e) {
    error.value = e.message || 'Failed to load data';
    console.error('Fetch error:', error.value);
  } finally {
    loading.value = false;
  }
}

function playTrack(track) {
  currentTrack.value = track;
  isPlaying.value = true;
  progress.value = 0;
  currentTime.value = '0:00';
  
  if (playTimer) clearInterval(playTimer);
  playTimer = setInterval(() => {
    progress.value += 1;
    const minutes = Math.floor(progress.value / 60);
    const seconds = (progress.value % 60).toString().padStart(2, '0');
    currentTime.value = `${minutes}:${seconds}`;
    
    if (progress.value >= 225) {
      clearInterval(playTimer);
      isPlaying.value = false;
      currentTrack.value = null;
    }
  }, 1000);
}

function togglePlay() {
  if (!currentTrack.value) return;
  isPlaying.value = !isPlaying.value;
  if (!isPlaying.value && playTimer) clearInterval(playTimer);
}

onMounted(() => {
  fetchData();
});

onUnmounted(() => {
  if (playTimer) clearInterval(playTimer);
});
</script>

<template>
  <div class="home">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-logo">
        <svg class="logo-icon" viewBox="0 0 24 24" fill="currentColor">
          <path d="M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.66 0 12 0zm5.521 17.34c-.24.359-.66.48-1.021.24-2.82-1.74-6.36-2.101-10.561-1.141-.418.122-.779-.179-.899-.539-.12-.421.18-.78.54-.9 4.56-1.021 8.52-.6 11.64 1.32.42.18.479.659.301 1.02zm1.44-3.3c-.301.42-.841.6-1.262.3-3.239-1.98-8.159-2.58-11.939-1.38-.479.12-1.02-.12-1.14-.6-.12-.48.12-1.021.6-1.141C9.6 9.9 15 10.561 18.72 12.84c.361.181.54.78.241 1.2zm.12-3.36C15.24 8.4 8.82 8.16 5.16 9.301c-.6.179-1.2-.181-1.38-.721-.18-.601.18-1.2.72-1.381 4.26-1.26 11.28-1.02 15.721 1.621.539.3.719 1.02.419 1.56-.299.421-1.02.599-1.559.3z"/>
        </svg>
        <span class="logo-text">Binh.Cloud</span>
      </div>

      <nav class="sidebar-nav">
        <a href="#" class="nav-item active">
          <svg viewBox="0 0 24 24" fill="currentColor"><path d="M12.5 3.247a1 1 0 0 0-1 0L4 7.577V20h4.5v-6a1 1 0 0 1 1-1h5a1 1 0 0 1 1 1v6H20V7.577l-7.5-4.33zm-2-1.732a3 3 0 0 1 3 0l7.5 4.33A2 2 0 0 1 22 7.577V21a1 1 0 0 1-1 1h-6.5a1 1 0 0 1-1-1v-6h-3v6a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V7.577a2 2 0 0 1 1-1.732l7.5-4.33z"/></svg>
          Home
        </a>
        <a href="#" class="nav-item">
          <svg viewBox="0 0 24 24" fill="currentColor"><path d="M10.533 1.279c-5.18 0-9.407 4.927-9.407 11.111 0 2.695.9 5.209 2.485 7.286l-2.225 3.507a.5.5 0 0 0 .75.621L5.83 21.7a9.1 9.1 0 0 0 4.703 1.29c5.18 0 9.407-4.927 9.407-11.11 0-6.184-4.227-11.11-9.407-11.11zm0 1.5c4.353 0 7.907 4.277 7.907 9.61s-3.554 9.61-7.907 9.61a7.593 7.593 0 0 1-4.045-1.18.5.5 0 0 0-.498-.01l-2.179 1.322 1.759-2.771a.5.5 0 0 0-.048-.584C3.282 17.45 2.626 15.226 2.626 12.39c0-5.333 3.554-9.61 7.907-9.61zm1.96 7.078a.5.5 0 0 0-.5-.5H6.793a.5.5 0 0 0 0 1h5.2a.5.5 0 0 0 .5-.5zm1.4 3.5a.5.5 0 0 0-.5-.5H6.793a.5.5 0 0 0 0 1h6.6a.5.5 0 0 0 .5-.5z"/></svg>
          Search
        </a>
      </nav>

      <div class="sidebar-section">
        <button class="create-playlist-btn">
          <span class="plus-icon">+</span>
          Create Playlist
        </button>
        <button class="liked-songs-btn">
          <span class="heart-icon">&#9829;</span>
          Liked Songs
        </button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      <!-- Top Bar -->
      <header class="topbar">
        <div class="nav-arrows">
          <button class="arrow-btn">&#8592;</button>
          <button class="arrow-btn">&#8594;</button>
        </div>
        <div class="topbar-right">
          <button class="topbar-btn">Premium</button>
          <div class="user-avatar">{{ userInitials }}</div>
        </div>
      </header>

      <!-- Scrollable Content -->
      <div class="content-scroll">
        <!-- Greeting Section -->
        <section class="greeting-section">
          <h1 class="greeting-title">{{ greeting }}</h1>
        </section>

        <!-- Loading State -->
        <div v-if="loading" class="state-message">
          <div class="spinner"></div>
          <p>Loading your music...</p>
        </div>

        <!-- Error State -->
        <div v-if="error && !loading" class="state-message error-state">
          <p>❌ {{ error }}</p>
          <button @click="fetchData" class="retry-btn">Retry</button>
        </div>

        <!-- Albums Section -->
        <section v-if="!loading && !error && albums.length > 0" class="music-section">
          <div class="section-header">
            <h2 class="section-title">Albums</h2>
            <a href="#" class="show-all">Show all</a>
          </div>
          <div class="cards-grid">
            <div
              v-for="album in albums.slice(0, 6)"
              :key="album.id"
              class="music-card"
              @mouseenter="album.hovered = true"
              @mouseleave="album.hovered = false"
            >
              <div class="card-cover-wrap">
                <img 
                  :src="album.imagePath || 'https://picsum.photos/seed/album/180/180'" 
                  :alt="album.title" 
                  class="card-cover" 
                  onerror="this.src='https://picsum.photos/seed/album/180/180'"
                />
                <button v-if="album.hovered" class="card-play-btn">&#9654;</button>
              </div>
              <p class="card-title">{{ album.title }}</p>
              <p class="card-subtitle">{{ album.artist?.name || 'Unknown Artist' }}</p>
            </div>
          </div>
        </section>

        <!-- Tracks Section -->
        <section v-if="!loading && !error && tracks.length > 0" class="music-section">
          <div class="section-header">
            <h2 class="section-title">Trending Tracks</h2>
            <a href="#" class="show-all">Show all</a>
          </div>
          <div class="cards-grid">
            <div
              v-for="track in tracks.slice(0, 6)"
              :key="track.trackId"
              class="music-card"
              @mouseenter="track.hovered = true"
              @mouseleave="track.hovered = false"
              @click="playTrack(track)"
            >
              <div class="card-cover-wrap">
                <img 
                  :src="track.imagePath || 'https://picsum.photos/seed/track/180/180'" 
                  :alt="track.title" 
                  class="card-cover" 
                  onerror="this.src='https://picsum.photos/seed/track/180/180'"
                />
                <button v-if="track.hovered" class="card-play-btn">&#9654;</button>
              </div>
              <p class="card-title">{{ track.title }}</p>
              <p class="card-subtitle">{{ track.artistName }}</p>
            </div>
          </div>
        </section>

        <div v-if="!loading && !error && albums.length === 0 && tracks.length === 0" class="state-message">
          <p>No albums or tracks found</p>
        </div>
      </div>
    </main>

    <!-- Now Playing Bar -->
    <footer class="now-playing-bar">
      <div class="now-playing-left">
        <img 
          v-if="currentTrack"
          :src="currentTrack.imagePath || 'https://picsum.photos/seed/track/56/56'" 
          :alt="currentTrack.title" 
          class="now-playing-cover" 
          onerror="this.src='https://picsum.photos/seed/track/56/56'"
        />
        <img 
          v-else
          src="https://picsum.photos/seed/placeholder/56/56" 
          alt="placeholder" 
          class="now-playing-cover" 
        />
        <div class="now-playing-info">
          <p class="now-playing-title">{{ currentTrack?.title || 'Not Playing' }}</p>
          <p class="now-playing-artist">{{ currentTrack?.artistName || 'Select a track' }}</p>
        </div>
      </div>

      <div class="now-playing-center">
        <div class="player-controls">
          <button class="ctrl-btn" title="Shuffle">
            <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M17.676 3.996a4 4 0 0 1 3.815 2.813 1 1 0 0 1-1.915.57 2 2 0 0 0-1.9-1.383h-.4a4 4 0 0 0-2.799 1.14l-1.738 1.67.707.73 1.028-1.013A5.98 5.98 0 0 1 17.191 7h.5a4 4 0 0 1 4 4 4 4 0 0 1-4 4h-.5a5.981 5.981 0 0 1-4.203-1.73l-4.145-4.266A4 4 0 0 0 6 7.975H5.5a4 4 0 1 0 0 8H7a1 1 0 0 1 0 2H5.5a6 6 0 0 1 0-12H6a6.003 6.003 0 0 1 4.232 1.763l4.145 4.266A3.985 3.985 0 0 0 17.191 13h.5a2 2 0 1 0 0-4h-.5a3.99 3.99 0 0 0-2.754 1.1l-1.028 1.013-.707-.73 1.738-1.67A5.985 5.985 0 0 1 17.276 7h.4a2 2 0 0 0 1.9-1.383 1 1 0 0 1 1.915.57 4 4 0 0 1-3.815 2.809v-5z"/></svg>
          </button>
          <button class="ctrl-btn" title="Previous">
            <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M3.3 1a.7.7 0 0 1 .7.7v8.05L19.196.957a.7.7 0 0 1 1.004.633v21.72a.7.7 0 0 1-1.004.633L4 14.95v8.35a.7.7 0 0 1-.7.7H1.7a.7.7 0 0 1-.7-.7V1.7a.7.7 0 0 1 .7-.7h1.6z"/></svg>
          </button>
          <button class="play-pause-btn" @click="togglePlay" :disabled="!currentTrack">
            <span v-if="!isPlaying">&#9654;</span>
            <span v-else>&#9646;&#9646;</span>
          </button>
          <button class="ctrl-btn" title="Next">
            <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M20.7 1a.7.7 0 0 0-.7.7v8.05L4.804.957A.7.7 0 0 0 3.8 1.59v21.72a.7.7 0 0 0 1.004.633L20 14.95v8.35a.7.7 0 0 0 .7.7h1.6a.7.7 0 0 0 .7-.7V1.7a.7.7 0 0 0-.7-.7H20.7z"/></svg>
          </button>
          <button class="ctrl-btn" title="Repeat">
            <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M17 1l4 4-4 4V6H7a1 1 0 0 0-1 1v3H4V7a3 3 0 0 1 3-3h10V1zm-7 14H3l4 4 4-4H8v-1a1 1 0 0 1 1-1h9v-2h1v2a2 2 0 0 1-2 2H8v1z"/></svg>
          </button>
        </div>
        <div class="progress-bar-wrap">
          <span class="time-label">{{ currentTime }}</span>
          <div class="progress-track">
            <div class="progress-fill" :style="{ width: progress + '%' }"></div>
          </div>
          <span class="time-label">{{ totalTime }}</span>
        </div>
      </div>

      <div class="now-playing-right">
        <div class="volume-wrap">
          <button class="ctrl-btn">&#128266;</button>
          <div class="volume-track">
            <div class="volume-fill" :style="{ width: volume + '%' }"></div>
            <input type="range" min="0" max="100" v-model="volume" class="volume-slider" />
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
@import "/src/styles/style.css";

.state-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #b3b3b3;
  text-align: center;
  gap: 20px;
}

.state-message p {
  font-size: 16px;
  margin: 0;
}

.state-message.error-state {
  color: #e22134;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #282828;
  border-top-color: #1DB954;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.retry-btn {
  padding: 8px 16px;
  background: #1DB954;
  color: #000;
  border: none;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s;
}

.retry-btn:hover {
  background: #1ed760;
}
</style>
