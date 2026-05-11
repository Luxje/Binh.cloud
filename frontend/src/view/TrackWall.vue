<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import '../styles/TrackWall.css';
import '../styles/UIverse-animations.css';

const API_URL = import.meta.env.VITE_API_URL || '';
const tracks = ref([]);
const loading = ref(true);
const error = ref(null);
const searchQuery = ref('');
const searchSuggestions = ref([]);
const activeFilter = ref('All');
const currentTrack = ref(null);
const isPlaying = ref(false);
const progress = ref(0);
const volume = ref(65);
const currentTime = ref('0:00');
const totalTime = ref('3:45');
let playTimer = null;

const userInitials = ref('AB');

async function fetchTracks() {
  loading.value = true;
  error.value = null;
  try {
    const res = await fetch(`${API_URL}/api/track`, {
      headers: { 'Accept': 'application/json' }
    });
    if (!res.ok) throw new Error(`Server responded with ${res.status}`);
    tracks.value = await res.json();
  } catch (e) {
    error.value = e.message || 'Failed to load tracks';
  } finally {
    loading.value = false;
  }
}

async function searchTracks(query) {
  if (!query.trim()) {
    searchSuggestions.value = [];
    return;
  }
  try {
    const searchURL = `${API_URL}/api/track/search/${encodeURIComponent(query)}`;
    const res = await fetch(searchURL, {
      headers: { 'Accept': 'application/json' }
    });
    if (!res.ok) throw new Error(`Search failed with ${res.status}`);
    searchSuggestions.value = await res.json();
  } catch (e) {
    console.error('Search error:', e.message);
    searchSuggestions.value = [];
  }
}

function handleSearch() {
  searchTracks(searchQuery.value);
}

function applySuggestion(track) {
  activeFilter.value = track.artistName;
  searchQuery.value = '';
  searchSuggestions.value = [];
  playTrack(track);
}

const artists = computed(() => ['All', ...new Set(tracks.value.map(t => t.artistName))]);

const filtered = computed(() =>
  activeFilter.value === 'All' ? tracks.value : tracks.value.filter(t => t.artistName === activeFilter.value)
);

const totalDuration = computed(() => {
  const total = filtered.value.reduce((s, t) => s + (t.durationSeconds || 0), 0);
  const m = Math.floor(total / 60);
  return `${m} min`;
});

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

function toggleFilter(artist) {
  activeFilter.value = activeFilter.value === artist ? 'All' : artist;
}

onMounted(() => {
  fetchTracks();
});

onUnmounted(() => {
  if (playTimer) clearInterval(playTimer);
});
</script>

<template>
  <div class="home">
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
        <!-- Header Section -->
        <section class="greeting-section">
          <h1 class="greeting-title">Your Tracks</h1>
          <p class="track-count" v-if="!loading && !error">{{ filtered.length }} tracks · {{ totalDuration }}</p>
        </section>

        <!-- Search Box -->
        <div class="search-box-wrapper">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Search tracks..."
            class="search-input"
            @input="handleSearch"
          />
          <div v-if="searchSuggestions.length > 0" class="search-suggestions">
            <div
              v-for="suggestion in searchSuggestions"
              :key="suggestion.trackId"
              class="suggestion-item"
              @click="applySuggestion(suggestion)"
            >
              <div class="suggestion-title">{{ suggestion.title }}</div>
              <div class="suggestion-artist">{{ suggestion.artistName }}</div>
            </div>
          </div>
        </div>

        <!-- Filter Buttons -->
        <div v-if="!loading && !error" class="filters">
          <button
            v-for="artist in artists"
            :key="artist"
            class="filter-btn"
            :class="{ active: activeFilter === artist }"
            @click="toggleFilter(artist)"
          >{{ artist }}</button>
        </div>

        <!-- Loading State -->
        <div v-if="loading" class="state-message">
          <div class="spinner"></div>
          <p>Loading tracks...</p>
        </div>

        <!-- Error State -->
        <div v-if="error && !loading" class="state-message error-state">
          <p>❌ {{ error }}</p>
          <button @click="fetchTracks" class="retry-btn">Retry</button>
        </div>

        <!-- Tracks Grid (Card Style) -->
        <section v-if="!loading && !error && filtered.length > 0" class="music-section">
          <div class="cards-grid">
            <div
              v-for="track in filtered"
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

        <div v-if="!loading && !error && filtered.length === 0" class="state-message">
          <p>No tracks found.</p>
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

.search-box-wrapper {
  position: relative;
  margin: 20px 0;
  max-width: 100%;
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #282828;
  background: rgba(255, 255, 255, 0.07);
  color: #fff;
  border-radius: 24px;
  font-size: 14px;
  transition: all 0.2s;
}

.search-input::placeholder {
  color: #535353;
}

.search-input:focus {
  outline: none;
  border-color: #1DB954;
  background: rgba(29, 185, 84, 0.1);
}

.search-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  margin-top: 8px;
  background: #282828;
  border-radius: 8px;
  max-height: 300px;
  overflow-y: auto;
  z-index: 10;
  border: 1px solid #404040;
}

.suggestion-item {
  padding: 12px 16px;
  color: #fff;
  cursor: pointer;
  transition: background 0.2s;
}

.suggestion-item:hover {
  background: #383838;
}

.suggestion-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
}

.suggestion-artist {
  font-size: 12px;
  color: #b3b3b3;
}

.filters {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 24px;
}

.filter-btn {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.07);
  color: #b3b3b3;
  border: 1px solid #282828;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.filter-btn.active {
  background: #1DB954;
  color: #000;
  border-color: #1DB954;
}

.music-section {
  margin-top: 20px;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
}

.music-card {
  background: #181818;
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: background 0.2s;
}

.music-card:hover {
  background: #282828;
}

.card-cover-wrap {
  position: relative;
  margin-bottom: 16px;
}

.card-cover {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  border-radius: 6px;
  display: block;
}

.card-play-btn {
  position: absolute;
  bottom: 8px;
  right: 8px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #1DB954;
  border: none;
  color: #000;
  font-size: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(0,0,0,0.5);
  transition: transform 0.1s, background 0.15s;
  animation: fadeInUp 0.15s ease;
}

.card-play-btn:hover {
  background: #1ed760;
  transform: scale(1.06);
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(6px); }
  to { opacity: 1; transform: translateY(0); }
}

.card-title {
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-subtitle {
  font-size: 12px;
  color: #b3b3b3;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

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

.track-count {
  font-size: 14px;
  color: #b3b3b3;
  margin-top: 12px;
}

@media (max-width: 1200px) {
  .cards-grid { grid-template-columns: repeat(4, 1fr); }
}

@media (max-width: 900px) {
  .cards-grid { grid-template-columns: repeat(3, 1fr); }
}
</style>
