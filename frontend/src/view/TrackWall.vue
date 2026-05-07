<template>
  <div id="app">
    <div class="app">
      <header>
        <p class="header-tag">// your collection</p>
        <h1>MY<br/><span>TRACKS</span></h1>
        <div class="header-line"></div>
        <p class="track-count">{{ filtered.length }} tracks · {{ totalDuration }}</p>
      </header>

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

const API_URL = import.meta.env.VITE_API_URL || '/api/track';

const currentTrack = ref(null);
const activeFilter = ref('All');
const elapsed = ref(0);
const tracks = ref([]);
const loading = ref(true);
const error = ref(null);
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

<style>
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

    :root {
      --bg: #0a0a0c;
      --surface: #111115;
      --surface2: #18181f;
      --border: rgba(255,255,255,0.06);
      --accent: #e8c97a;
      --accent2: #c46e3a;
      --text: #f0ede6;
      --muted: #7a7870;
      --playing: #e8c97a;
    }

    html { scroll-behavior: smooth; }

    body {
      background: var(--bg);
      color: var(--text);
      font-family: 'DM Mono', monospace;
      min-height: 100vh;
      overflow-x: hidden;
    }

    /* Grain overlay */
    body::before {
      content: '';
      position: fixed;
      inset: 0;
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.04'/%3E%3C/svg%3E");
      pointer-events: none;
      z-index: 999;
      opacity: 0.5;
    }

    .app {
      max-width: 900px;
      margin: 0 auto;
      padding: 60px 24px 120px;
    }

    /* Header */
    header {
      margin-bottom: 64px;
      position: relative;
    }

    .header-tag {
      font-family: 'DM Mono', monospace;
      font-size: 10px;
      letter-spacing: 0.3em;
      color: var(--accent);
      text-transform: uppercase;
      margin-bottom: 12px;
      opacity: 0.8;
    }

    header h1 {
      font-family: 'Bebas Neue', sans-serif;
      font-size: clamp(64px, 12vw, 120px);
      line-height: 0.88;
      letter-spacing: 0.02em;
      color: var(--text);
      mix-blend-mode: normal;
    }

    header h1 span {
      color: var(--accent);
    }

    .header-line {
      width: 100%;
      height: 1px;
      background: linear-gradient(90deg, var(--accent) 0%, transparent 70%);
      margin-top: 32px;
      opacity: 0.4;
    }

    .track-count {
      font-size: 11px;
      color: var(--muted);
      letter-spacing: 0.15em;
      margin-top: 16px;
      font-style: italic;
    }

    /* Filter bar */
    .filters {
      display: flex;
      gap: 8px;
      margin-bottom: 40px;
      flex-wrap: wrap;
    }

    .filter-btn {
      background: transparent;
      border: 1px solid var(--border);
      color: var(--muted);
      font-family: 'DM Mono', monospace;
      font-size: 10px;
      letter-spacing: 0.2em;
      text-transform: uppercase;
      padding: 8px 16px;
      cursor: pointer;
      transition: all 0.2s ease;
      position: relative;
      overflow: hidden;
    }

    .filter-btn::before {
      content: '';
      position: absolute;
      inset: 0;
      background: var(--accent);
      transform: scaleX(0);
      transform-origin: left;
      transition: transform 0.25s ease;
      z-index: -1;
    }

    .filter-btn:hover, .filter-btn.active {
      color: var(--bg);
      border-color: var(--accent);
    }

    .filter-btn:hover::before, .filter-btn.active::before {
      transform: scaleX(1);
    }

    /* Tracks list */
    .tracks-header {
      display: grid;
      grid-template-columns: 40px 1fr 160px 80px 80px;
      gap: 16px;
      padding: 0 20px 12px;
      border-bottom: 1px solid var(--border);
      margin-bottom: 4px;
    }

    .tracks-header span {
      font-size: 9px;
      letter-spacing: 0.25em;
      text-transform: uppercase;
      color: var(--muted);
    }

    .tracks-header span:nth-child(3),
    .tracks-header span:nth-child(4),
    .tracks-header span:nth-child(5) {
      text-align: right;
    }

    .track-row {
      display: grid;
      grid-template-columns: 40px 1fr 160px 80px 80px;
      gap: 16px;
      align-items: center;
      padding: 16px 20px;
      border-bottom: 1px solid var(--border);
      cursor: pointer;
      position: relative;
      transition: background 0.2s ease;
      animation: fadeUp 0.4s ease both;
    }

    .track-row:nth-child(1) { animation-delay: 0.05s; }
    .track-row:nth-child(2) { animation-delay: 0.10s; }
    .track-row:nth-child(3) { animation-delay: 0.15s; }
    .track-row:nth-child(4) { animation-delay: 0.20s; }
    .track-row:nth-child(5) { animation-delay: 0.25s; }

    @keyframes fadeUp {
      from { opacity: 0; transform: translateY(16px); }
      to { opacity: 1; transform: translateY(0); }
    }

    .track-row::after {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 0;
      background: var(--accent);
      transition: width 0.2s ease;
      opacity: 0.15;
    }

    .track-row:hover { background: var(--surface2); }
    .track-row:hover::after { width: 3px; }
    .track-row.playing { background: rgba(232, 201, 122, 0.05); }
    .track-row.playing::after { width: 3px; opacity: 0.6; }

    /* Track number */
    .track-num {
      font-size: 12px;
      color: var(--muted);
      text-align: center;
      position: relative;
      font-style: italic;
    }

    .track-row.playing .track-num { color: var(--accent); }

    .bars {
      display: flex;
      gap: 2px;
      align-items: flex-end;
      height: 14px;
      justify-content: center;
    }

    .bar {
      width: 3px;
      background: var(--accent);
      border-radius: 1px;
      animation: bounce 0.8s ease-in-out infinite alternate;
    }

    .bar:nth-child(2) { animation-delay: 0.15s; }
    .bar:nth-child(3) { animation-delay: 0.30s; }

    @keyframes bounce {
      from { height: 3px; }
      to { height: 14px; }
    }

    /* Track info */
    .track-info { min-width: 0; }

    .track-title {
      font-family: 'Instrument Serif', serif;
      font-size: 16px;
      color: var(--text);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      margin-bottom: 2px;
    }

    .track-row.playing .track-title { color: var(--accent); }

    .track-artist {
      font-size: 11px;
      color: var(--muted);
      letter-spacing: 0.05em;
    }

    /* Album */
    .track-album {
      font-size: 11px;
      color: var(--muted);
      text-align: right;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-style: italic;
    }

    /* Date */
    .track-date {
      font-size: 11px;
      color: var(--muted);
      text-align: right;
      letter-spacing: 0.05em;
    }

    /* Duration */
    .track-dur {
      font-size: 12px;
      color: var(--muted);
      text-align: right;
      letter-spacing: 0.08em;
    }

    .track-row.playing .track-dur { color: var(--accent); }

    /* Now playing bar */
    .now-playing {
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      background: rgba(10, 10, 12, 0.95);
      backdrop-filter: blur(20px);
      border-top: 1px solid var(--border);
      padding: 20px 32px;
      display: flex;
      align-items: center;
      gap: 32px;
      z-index: 100;
      transform: translateY(100%);
      transition: transform 0.4s cubic-bezier(0.16, 1, 0.3, 1);
    }

    .now-playing.visible { transform: translateY(0); }

    .np-label {
      font-size: 9px;
      letter-spacing: 0.3em;
      text-transform: uppercase;
      color: var(--accent);
      white-space: nowrap;
    }

    .np-info { flex: 1; min-width: 0; }

    .np-title {
      font-family: 'Instrument Serif', serif;
      font-size: 18px;
      color: var(--text);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .np-artist {
      font-size: 11px;
      color: var(--muted);
      margin-top: 2px;
    }

    .np-progress {
      flex: 2;
      max-width: 300px;
    }

    .progress-bar {
      width: 100%;
      height: 2px;
      background: var(--surface2);
      position: relative;
      overflow: hidden;
    }

    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, var(--accent2), var(--accent));
      transition: width 0.3s linear;
    }

    .progress-times {
      display: flex;
      justify-content: space-between;
      font-size: 9px;
      color: var(--muted);
      margin-top: 6px;
      letter-spacing: 0.1em;
    }

    .np-dur {
      font-size: 12px;
      color: var(--accent);
      letter-spacing: 0.1em;
      white-space: nowrap;
    }

    /* State boxes */
    .state-box {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 16px;
      padding: 80px 24px;
      color: var(--muted);
      font-size: 12px;
      letter-spacing: 0.15em;
      text-transform: uppercase;
    }

    .error-box { color: #c46e3a; }
    .error-icon {
      width: 36px; height: 36px;
      border: 1px solid #c46e3a;
      border-radius: 50%;
      display: flex; align-items: center; justify-content: center;
      font-family: 'Bebas Neue', sans-serif;
      font-size: 20px;
    }

    .retry-btn { margin-top: 8px; border-color: #c46e3a; color: #c46e3a; }
    .retry-btn:hover { color: var(--bg); }
    .retry-btn:hover::before { background: #c46e3a; transform: scaleX(1); }

    .spinner {
      width: 28px; height: 28px;
      border: 1px solid var(--border);
      border-top-color: var(--accent);
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }

    @keyframes spin { to { transform: rotate(360deg); } }

    /* Responsive */
    @media (max-width: 640px) {
      .tracks-header { grid-template-columns: 32px 1fr 80px; }
      .tracks-header span:nth-child(3),
      .tracks-header span:nth-child(4) { display: none; }
      .tracks-header span:nth-child(5) { display: block; }

      .track-row { grid-template-columns: 32px 1fr 80px; }
      .track-album, .track-date { display: none; }
    }
</style>