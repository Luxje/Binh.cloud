<script setup>
import { ref, computed, onMounted, onUnmounted, inject } from 'vue';
import '../styles/TrackWall.css';
import '../styles/TrackWallScoped.css';
import '../styles/UIverse-animations.css';
import { useAudioService } from '../services/audioService.js';

const API_URL = import.meta.env.VITE_API_URL || '';
const tracks = ref([]);
const loading = ref(true);
const error = ref(null);
const searchQuery = ref('');
const searchSuggestions = ref([]);
const activeFilter = ref('All');

const audioService = useAudioService();
const { currentTrack, isPlaying, progress, currentTime, totalTime, volume } = audioService;

const userInitials = ref('AB');

function getImageUrl(track) {
  if (!track) {
    return 'https://picsum.photos/seed/track/180/180';
  }
  
  // Try track's own image first
  if (track.image_path) {
    return `${API_URL}${track.image_path}`;
  }
  
  // Fall back to album cover if available
  if (track.album?.image_path) {
    return `${API_URL}${track.album.image_path}`;
  }
  
  // Fall back to placeholder
  return 'https://picsum.photos/seed/track/180/180';
}

async function fetchTracks() {
  loading.value = true;
  error.value = null;
  try {
    const res = await fetch(`${API_URL}/track`, {
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
    const searchURL = `${API_URL}/track/search/${encodeURIComponent(query)}`;
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
  audioService.playTrack(track, API_URL);
}

function togglePlay() {
  audioService.togglePlay();
}

function handleProgressClick(e) {
  const progressTrack = e.currentTarget;
  const rect = progressTrack.getBoundingClientRect();
  const clickX = e.clientX - rect.left;
  const percentage = (clickX / rect.width) * 100;
  audioService.seekTo(percentage);
}

function toggleFilter(artist) {
  activeFilter.value = activeFilter.value === artist ? 'All' : artist;
}

onMounted(() => {
  fetchTracks();
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
                  :src="getImageUrl(track)" 
                  :alt="track.title" 
                  class="card-cover"
                  @error="e => e.target.src = 'https://picsum.photos/seed/track/180/180'"
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
          :src="getImageUrl(currentTrack)" 
          :alt="currentTrack.title" 
          class="now-playing-cover"
          @error="e => e.target.src = 'https://picsum.photos/seed/track/56/56'"
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
          <div class="progress-track" @click="handleProgressClick">
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
            <input type="range" min="0" max="100" v-model.number="volume" @input="audioService.updateVolume" class="volume-slider" />
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>


