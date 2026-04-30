<script setup>
import { onMounted, ref } from 'vue';

const deviceId = ref(null);
const playerInstance = ref(null);

// This prop would come from your Auth logic/Pinia store
const props = defineProps(['token']); 

onMounted(() => {
  // 1. Inject the Spotify Script
  const script = document.createElement("script");
  script.src = "https://sdk.scdn.co/spotify-player.js";
  script.async = true;
  document.body.appendChild(script);

  // 2. Define the global callback
  window.onSpotifyWebPlaybackSDKReady = () => {
    const player = new window.Spotify.Player({
      name: 'Binh.Cloud Web Player',
      getOAuthToken: cb => { cb(props.token); },
      volume: 0.5
    });

    // Handle Errors
    player.addListener('initialization_error', ({ message }) => console.error(message));
    player.addListener('authentication_error', ({ message }) => console.error(message));

    // Handle Ready State
    player.addListener('ready', ({ device_id }) => {
      console.log('Ready with Device ID', device_id);
      deviceId.value = device_id; // Reactive update
    });

    player.connect();
    playerInstance.value = player;
  };
});
</script>

<template>
  <div class="glass-player">
    <!-- Your Glassmorphism UI here -->
    <p v-if="deviceId">Connected to: {{ deviceId }}</p>
    <p v-else>Connecting to Spotify...</p>
  </div>
</template>

<style scoped>
.glass-player {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 20px;
}
</style>