

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
        <a href="#" class="nav-item">
          <svg viewBox="0 0 24 24" fill="currentColor"><path d="M3 22a1 1 0 0 1-1-1V3a1 1 0 0 1 2 0v18a1 1 0 0 1-1 1zM15.5 2.134A1 1 0 0 0 14 3v18a1 1 0 0 0 1.5.866l11-6.5a1 1 0 0 0 0-1.732l-11-11.5zM9 3a1 1 0 0 0-1-1 1 1 0 0 0-1 1v18a1 1 0 0 0 1 1 1 1 0 0 0 1-1V3z"/></svg>
          Your Library
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

      <div class="sidebar-playlists">
        <p v-for="playlist in sidebarPlaylists" :key="playlist" class="playlist-link">{{ playlist }}</p>
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
        <!-- Good Morning Section -->
        <section class="greeting-section">
          <h1 class="greeting-title">{{ greeting }}</h1>
          <div class="quick-picks-grid">
            <div
              v-for="item in quickPicks"
              :key="item.id"
              class="quick-pick-card"
              @mouseenter="item.hovered = true"
              @mouseleave="item.hovered = false"
            >
              <img :src="item.cover" :alt="item.name" class="quick-pick-img" />
              <span class="quick-pick-name">{{ item.name }}</span>
              <button v-if="item.hovered" class="quick-pick-play">&#9654;</button>
            </div>
          </div>
        </section>

        <!-- Recently Played -->
        <section class="music-section">
          <div class="section-header">
            <h2 class="section-title">Recently played</h2>
            <a href="#" class="show-all">Show all</a>
          </div>
          <div class="cards-grid">
            <div
              v-for="item in recentlyPlayed"
              :key="item.id"
              class="music-card"
              @mouseenter="item.hovered = true"
              @mouseleave="item.hovered = false"
            >
              <div class="card-cover-wrap">
                <img :src="item.cover" :alt="item.name" class="card-cover" :class="{ round: item.isArtist }" />
                <button v-if="item.hovered" class="card-play-btn">&#9654;</button>
              </div>
              <p class="card-title">{{ item.name }}</p>
              <p class="card-subtitle">{{ item.subtitle }}</p>
            </div>
          </div>
        </section>

        <!-- Made For You -->
        <section class="music-section">
          <div class="section-header">
            <h2 class="section-title">Made for you</h2>
            <a href="#" class="show-all">Show all</a>
          </div>
          <div class="cards-grid">
            <div
              v-for="item in madeForYou"
              :key="item.id"
              class="music-card"
              @mouseenter="item.hovered = true"
              @mouseleave="item.hovered = false"
            >
              <div class="card-cover-wrap">
                <img :src="item.cover" :alt="item.name" class="card-cover" />
                <button v-if="item.hovered" class="card-play-btn">&#9654;</button>
              </div>
              <p class="card-title">{{ item.name }}</p>
              <p class="card-subtitle">{{ item.subtitle }}</p>
            </div>
          </div>
        </section>

        <!-- Trending -->
        <section class="music-section">
          <div class="section-header">
            <h2 class="section-title">Trending now</h2>
            <a href="#" class="show-all">Show all</a>
          </div>
          <div class="cards-grid">
            <div
              v-for="item in trending"
              :key="item.id"
              class="music-card"
              @mouseenter="item.hovered = true"
              @mouseleave="item.hovered = false"
            >
              <div class="card-cover-wrap">
                <img :src="item.cover" :alt="item.name" class="card-cover" />
                <button v-if="item.hovered" class="card-play-btn">&#9654;</button>
              </div>
              <p class="card-title">{{ item.name }}</p>
              <p class="card-subtitle">{{ item.subtitle }}</p>
            </div>
          </div>
        </section>
      </div>
    </main>

    <!-- Now Playing Bar -->
    <footer class="now-playing-bar">
      <div class="now-playing-left">
        <img :src="currentTrack.cover" :alt="currentTrack.name" class="now-playing-cover" />
        <div class="now-playing-info">
          <p class="now-playing-title">{{ currentTrack.name }}</p>
          <p class="now-playing-artist">{{ currentTrack.artist }}</p>
        </div>
        <button class="heart-btn" :class="{ liked: currentTrack.liked }" @click="currentTrack.liked = !currentTrack.liked">
          &#9829;
        </button>
      </div>

      <div class="now-playing-center">
        <div class="player-controls">
          <button class="ctrl-btn" title="Shuffle">
            <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M17.676 3.996a4 4 0 0 1 3.815 2.813 1 1 0 0 1-1.915.57 2 2 0 0 0-1.9-1.383h-.4a4 4 0 0 0-2.799 1.14l-1.738 1.67.707.73 1.028-1.013A5.98 5.98 0 0 1 17.191 7h.5a4 4 0 0 1 4 4 4 4 0 0 1-4 4h-.5a5.981 5.981 0 0 1-4.203-1.73l-4.145-4.266A4 4 0 0 0 6 7.975H5.5a4 4 0 1 0 0 8H7a1 1 0 0 1 0 2H5.5a6 6 0 0 1 0-12H6a6.003 6.003 0 0 1 4.232 1.763l4.145 4.266A3.985 3.985 0 0 0 17.191 13h.5a2 2 0 1 0 0-4h-.5a3.99 3.99 0 0 0-2.754 1.1l-1.028 1.013-.707-.73 1.738-1.67A5.985 5.985 0 0 1 17.276 7h.4a2 2 0 0 0 1.9-1.383 1 1 0 0 1 1.915.57 4 4 0 0 1-3.815 2.809v-5z"/></svg>
          </button>
          <button class="ctrl-btn" title="Previous">
            <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M3.3 1a.7.7 0 0 1 .7.7v8.05L19.196.957a.7.7 0 0 1 1.004.633v21.72a.7.7 0 0 1-1.004.633L4 14.95v8.35a.7.7 0 0 1-.7.7H1.7a.7.7 0 0 1-.7-.7V1.7a.7.7 0 0 1 .7-.7h1.6z"/></svg>
          </button>
          <button class="play-pause-btn" @click="isPlaying = !isPlaying">
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
          <div class="progress-track" @click="seekTrack">
            <div class="progress-fill" :style="{ width: progress + '%' }"></div>
          </div>
          <span class="time-label">{{ totalTime }}</span>
        </div>
      </div>

      <div class="now-playing-right">
        <button class="ctrl-btn" title="Queue">
          <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M15 15H3v-1.5h12V15zm0-4.5H3V9h12v1.5zM3 4.5h18V6H3V4.5zm18 11.25-4.5 4.5V13.5L21 15.75z"/></svg>
        </button>
        <button class="ctrl-btn" title="Devices">
          <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M6 3h15v15.167a3.5 3.5 0 1 1-3.5-3.5H19V5H8v13.167a3.5 3.5 0 1 1-3.5-3.5H6V3zm0 13.667H4.5a1.5 1.5 0 1 0 1.5 1.5v-1.5zm13 0h-1.5a1.5 1.5 0 1 0 1.5 1.5v-1.5z"/></svg>
        </button>
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


<script>
export default {
  name: 'HomeView',
  data() {
    return {
      // --- Player UI State ---
      isPlaying: false,
      progress: 0,
      volume: 65,
      currentTime: '0:00',
      totalTime: '0:00',
      currentTrack: {
        name: 'Not Playing',
        artist: 'Select a track',
        cover: 'https://picsum.photos/seed/placeholder/56/56',
        liked: false,
      },

      // --- Your Existing UI Data ---
      quickPicks: [
        { id: 1, name: 'Liked Songs', cover: 'https://picsum.photos/seed/liked/48/48', hovered: false },
        { id: 2, name: 'Daily Mix 1', cover: 'https://picsum.photos/seed/mix1/48/48', hovered: false },
        { id: 3, name: 'Chill Vibes', cover: 'https://picsum.photos/seed/chill/48/48', hovered: false },
        { id: 4, name: 'Top Hits', cover: 'https://picsum.photos/seed/top/48/48', hovered: false },
        { id: 5, name: 'Late Night', cover: 'https://picsum.photos/seed/night/48/48', hovered: false },
        { id: 6, name: 'Hip-Hop Mix', cover: 'https://picsum.photos/seed/hiphop/48/48', hovered: false },
      ],
      recentlyPlayed: [
        { id: 1, name: 'Daily Mix 1', subtitle: 'Based on your recent listening', cover: 'https://picsum.photos/seed/r1/160/160', hovered: false, isArtist: false },
        { id: 2, name: 'The Weeknd', subtitle: 'Artist', cover: 'https://picsum.photos/seed/r2/160/160', hovered: false, isArtist: true },
        { id: 3, name: 'After Hours', subtitle: 'The Weeknd', cover: 'https://picsum.photos/seed/r3/160/160', hovered: false, isArtist: false },
        { id: 4, name: 'Chill Mix', subtitle: 'Based on your mood', cover: 'https://picsum.photos/seed/r4/160/160', hovered: false, isArtist: false },
        { id: 5, name: 'Drake', subtitle: 'Artist', cover: 'https://picsum.photos/seed/r5/160/160', hovered: false, isArtist: true },
        { id: 6, name: 'Certified Lover', subtitle: 'Drake', cover: 'https://picsum.photos/seed/r6/160/160', hovered: false, isArtist: false },
      ],
      madeForYou: [
        { id: 1, name: 'Daily Mix 2', subtitle: 'The Weeknd, Drake, Post Malone and more', cover: 'https://picsum.photos/seed/m1/160/160', hovered: false },
        { id: 2, name: 'Daily Mix 3', subtitle: 'Kendrick Lamar, J. Cole and more', cover: 'https://picsum.photos/seed/m2/160/160', hovered: false },
        { id: 3, name: 'Discover Weekly', subtitle: 'Your weekly mixtape of fresh music', cover: 'https://picsum.photos/seed/m3/160/160', hovered: false },
        { id: 4, name: 'Release Radar', subtitle: 'Catch all the latest music from artists you follow', cover: 'https://picsum.photos/seed/m4/160/160', hovered: false },
        { id: 5, name: 'Time Capsule', subtitle: 'We made you a personalized playlist', cover: 'https://picsum.photos/seed/m5/160/160', hovered: false },
        { id: 6, name: 'On Repeat', subtitle: 'Songs you love right now', cover: 'https://picsum.photos/seed/m6/160/160', hovered: false },
      ],
      trending: [
        { id: 1, name: 'Top 50 Global', subtitle: 'Your daily update of the most played tracks', cover: 'https://picsum.photos/seed/t1/160/160', hovered: false },
        { id: 2, name: 'Viral 50', subtitle: 'Your daily update of the most viral tracks', cover: 'https://picsum.photos/seed/t2/160/160', hovered: false },
        { id: 3, name: 'New Music Friday', subtitle: 'The best new music, every Friday', cover: 'https://picsum.photos/seed/t3/160/160', hovered: false },
        { id: 4, name: 'Rap Caviar', subtitle: "Music's biggest names and top hits in hip-hop", cover: 'https://picsum.photos/seed/t4/160/160', hovered: false },
        { id: 5, name: 'Hot Hits', subtitle: 'The hottest tracks right now', cover: 'https://picsum.photos/seed/t5/160/160', hovered: false },
        { id: 6, name: 'Pop Rising', subtitle: 'The future of pop, right now', cover: 'https://picsum.photos/seed/t6/160/160', hovered: false },
      ],
      sidebarPlaylists: [
        'My Playlist #1', 'Chill Evening', 'Workout Bangers',
        'Late Night Drive', 'Study Mode', 'Weekend Vibes',
      ],
    };
  },
  computed: {
    greeting() {
      const h = new Date().getHours();
      return h < 12 ? 'Good morning' : h < 18 ? 'Good afternoon' : 'Good evening';
    },
    userInitials() { return 'AB'; },
  },
  methods: {
    formatTime(ms) {
      const minutes = Math.floor(ms / 60000);
      const seconds = ((ms % 60000) / 1000).toFixed(0);
      return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
    },

    seekTrack(e) {
      const rect = e.currentTarget.getBoundingClientRect();
      const percent = (e.clientX - rect.left) / rect.width;
    }
  }
};


</script>

<style scoped>
@import "/src/styles/style.css";
</style>