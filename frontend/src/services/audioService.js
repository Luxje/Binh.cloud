import { ref } from 'vue';

let audioElement = null;
let audioService = null;

// Singleton state - created once and shared across all components
const state = {
  currentTrack: ref(null),
  isPlaying: ref(false),
  progress: ref(0),
  currentTime: ref('0:00'),
  totalTime: ref('0:00'),
  volume: ref(65),
};

const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins}:${secs.toString().padStart(2, '0')}`;
};

const initAudio = (apiUrl) => {
  if (audioElement) {
    return;
  }

  audioElement = new Audio();
  audioElement.volume = state.volume.value / 100;

  audioElement.addEventListener('timeupdate', () => {
    if (audioElement.duration) {
      state.progress.value = (audioElement.currentTime / audioElement.duration) * 100;
      state.currentTime.value = formatTime(audioElement.currentTime);
      state.totalTime.value = formatTime(audioElement.duration);
    }
  });

  audioElement.addEventListener('ended', () => {
    state.isPlaying.value = false;
    state.currentTrack.value = null;
  });

  audioElement.addEventListener('play', () => {
    state.isPlaying.value = true;
  });

  audioElement.addEventListener('pause', () => {
    state.isPlaying.value = false;
  });
};

const playTrack = (track, apiUrl) => {
  if (!audioElement) {
    initAudio(apiUrl);
  }

  state.currentTrack.value = track;

  audioElement.src = `${apiUrl}/track/${track.trackId}/stream`;
  audioElement.play().catch((e) => {
    console.error('Playback failed:', e);
  });
};

const togglePlay = () => {
  if (!state.currentTrack.value || !audioElement) return;

  if (state.isPlaying.value) {
    audioElement.pause();
  } else {
    audioElement.play().catch((e) => {
      console.error('Playback failed:', e);
    });
  }
};

const seekTo = (percentage) => {
  if (!audioElement || !audioElement.duration) return;
  audioElement.currentTime = (percentage / 100) * audioElement.duration;
};

const updateVolume = () => {
  if (audioElement) {
    audioElement.volume = state.volume.value / 100;
  }
};

const stopAudio = () => {
  if (audioElement) {
    audioElement.pause();
    audioElement.src = '';
  }
};

const getAudio = () => audioElement;

export const useAudioService = () => {
  return {
    currentTrack: state.currentTrack,
    isPlaying: state.isPlaying,
    progress: state.progress,
    currentTime: state.currentTime,
    totalTime: state.totalTime,
    volume: state.volume,
    playTrack,
    togglePlay,
    seekTo,
    updateVolume,
    stopAudio,
    getAudio,
    initAudio,
  };
};
