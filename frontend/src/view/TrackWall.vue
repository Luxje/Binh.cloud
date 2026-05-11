<template>
  <div id="app">
    <nav class="menu-bar">
      <div class="menu-container">
        <div class="menu-logo">
          <span class="logo-icon">♫</span>
          <span class="logo-text">SOUNDWAVE</span>
        </div>
        <ul class="menu-items">
          <li><a href="#" class="menu-link active" @click.prevent="currentPage = 'tracks'">Tracks</a></li>
          <li><a href="#" class="menu-link" @click.prevent="currentPage = 'favorites'">Likes</a></li>
          <li><a href="#" class="menu-link" @click.prevent="currentPage = 'playlists'">Playlists</a></li>
          <li><a href="#" class="menu-link" @click.prevent="currentPage = 'settings'">Settings</a></li>
        </ul>
        <div class="menu-user">
          <button class="user-btn">👤</button>
        </div>
      </div>
    </nav>
    <div class="app">
      <header>
        <p class="header-tag">// discover music</p>
        <h1>YOUR<br/><span>LIBRARY</span></h1>
        <div class="header-line"></div>
        <p class="track-count">{{ filtered.length }} tracks · {{ totalDuration }}</p>
      </header>

      <div class="search-box">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search tracks, artists, albums..."
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

      <div class="filters" v-if="!loading && !error">
        <button
          v-for="artist in artists"
          :key="artist"
          class="filter-btn"
          :class="{ active: activeFilter === artist }"
          @click="toggleFilter(artist)"
        >{{ artist }}</button>
      </div>

      <div class="tracks-header" v-if="!loading && !error && filtered.length > 0">
        <span>#</span>
        <span>Title</span>
        <span style="text-align:right">Album</span>
        <span style="text-align:right">Year</span>
        <span style="text-align:right">Duration</span>
      </div>

      <div v-if="loading" class="state-box">
        <div class="spinner"></div>
        <span>Fetching tracks...</span>
      </div>

      <div v-else-if="error" class="state-box error-box">
        <span class="error-icon">!</span>
        <span>{{ error }}</span>
        <button class="filter-btn retry-btn" @click="fetchTracks">Retry</button>
      </div>

      <div v-else-if="filtered.length === 0" class="state-box">
        <span>No tracks found.</span>
      </div>

      <div v-else class="track-list">
        <div
          v-for="(track, i) in filtered"
          :key="track.trackId"
          class="track-row"
          :class="{ playing: currentTrack && currentTrack.trackId === track.trackId }"
          @click="play(track)"
        >
          <div class="track-num">
            <div v-if="currentTrack && currentTrack.trackId === track.trackId" class="bars">
              <div class="bar"></div>
              <div class="bar"></div>
              <div class="bar"></div>
            </div>
            <span v-else>{{ String(i + 1).padStart(2, '0') }}</span>
          </div>

          <div class="track-info">
            <div class="track-title">{{ track.title }}</div>
            <div class="track-artist">{{ track.artistName }}</div>
          </div>

          <div class="track-album">{{ track.album.title }}</div>
          <div class="track-date">{{ track.release_date.split('-')[0] }}</div>
          <div class="track-dur">{{ formatDur(track.duration_seconds) }}</div>
        </div>
      </div>
    </div>

    <div class="now-playing" :class="{ visible: currentTrack }">
      <span class="np-label">Now Playing</span>
      <div class="np-info" v-if="currentTrack">
        <div class="np-title">{{ currentTrack.title }}</div>
        <div class="np-artist">{{ currentTrack.artistName }}</div>
      </div>
      <div class="np-progress" v-if="currentTrack">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: progressPct + '%' }"></div>
        </div>
        <div class="progress-times">
          <span>{{ formatDur(elapsed) }}</span>
          <span>{{ formatDur(currentTrack.duration_seconds) }}</span>
        </div>
      </div>
      <div class="np-dur" v-if="currentTrack">{{ formatDur(currentTrack.duration_seconds) }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue';
import '../styles/TrackWall.css';
import '../styles/UIverse-animations.css';

const API_URL = import.meta.env.VITE_API_URL || '/api/track';

const currentTrack = ref(null);
const currentPage = ref('tracks');
const activeFilter = ref('All');
const elapsed = ref(0);
const tracks = ref([]);
const loading = ref(true);
const error = ref(null);
const searchQuery = ref('');
const searchSuggestions = ref([]);
let timer = null;

async function fetchTracks() {
  loading.value = true;
  error.value = null;
  try {
    const res = await fetch(API_URL, {
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

fetchTracks();

async function searchTracks(query) {
  if (!query.trim()) {
    searchSuggestions.value = [];
    return;
  }
  try {
    const searchURL = `${API_URL}/search?q=${encodeURIComponent(query)}`;
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
  play(track);
}

const artists = computed(() => ['All', ...new Set(tracks.value.map(t => t.artistName))]);

const filtered = computed(() =>
  activeFilter.value === 'All' ? tracks.value : tracks.value.filter(t => t.artistName === activeFilter.value)
);

const totalDuration = computed(() => {
  const total = filtered.value.reduce((s, t) => s + t.duration_seconds, 0);
  const m = Math.floor(total / 60);
  return `${m} min`;
});

const progressPct = computed(() =>
  currentTrack.value ? (elapsed.value / currentTrack.value.duration_seconds) * 100 : 0
);

function formatDur(s) {
  const m = Math.floor(s / 60);
  const sec = Math.floor(s % 60);
  return `${m}:${String(sec).padStart(2, '0')}`;
}

function play(track) {
  if (currentTrack.value && currentTrack.value.trackId === track.trackId) return;
  currentTrack.value = track;
  elapsed.value = 0;
  
  // Clear any existing timer before starting a new one
  if (timer) clearInterval(timer);
  
  timer = setInterval(() => {
    elapsed.value += 1;
    if (elapsed.value >= track.duration_seconds) {
      clearInterval(timer);
      timer = null;
      currentTrack.value = null;
      elapsed.value = 0;
    }
  }, 1000);
}

function toggleFilter(artist) {
  activeFilter.value = activeFilter.value === artist ? 'All' : artist;
}

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>
