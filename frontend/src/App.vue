<script setup>
import { ref } from 'vue';
import TrackWall from './view/TrackWall.vue';
import Login from './view/Login.vue';
import Home from './view/Home.vue';

const isLoggedIn = ref(true);
const currentPage = ref('home');

</script>

<template>
  <Login v-if="!isLoggedIn"></Login>
  <div v-else class="app-container">
    <sidebar>
      <div class="sidebar-header">🎵 Binh.Cloud</div>
      <nav class="nav-menu">
        <button 
          class="nav-btn" 
          :class="{ active: currentPage === 'home' }"
          @click="currentPage = 'home'"
        >
          🏠 Home
        </button>
        <button 
          class="nav-btn" 
          :class="{ active: currentPage === 'trackwall' }"
          @click="currentPage = 'trackwall'"
        >
          🎵 Tracks
        </button>
      </nav>
    </sidebar>
    <main class="main-content">
      <Home v-if="currentPage === 'home'" />
      <TrackWall v-else-if="currentPage === 'trackwall'" />
    </main>
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  height: 100vh;
  background: #000;
}

sidebar {
  width: 240px;
  background: #0f0f0f;
  border-right: 1px solid #282828;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.sidebar-header {
  font-size: 20px;
  font-weight: bold;
  color: #1DB954;
  padding: 0 16px 30px;
  text-align: center;
  border-bottom: 1px solid #282828;
  margin-bottom: 20px;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 0 12px;
}

.nav-btn {
  padding: 12px 16px;
  background: transparent;
  border: none;
  color: #b3b3b3;
  font-size: 16px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.3s ease;
  text-align: left;
  font-weight: 500;
}

.nav-btn:hover {
  color: #fff;
  background: #282828;
}

.nav-btn.active {
  background: #1DB954;
  color: #fff;
}

.main-content {
  flex: 1;
  overflow-y: auto;
}
</style>
