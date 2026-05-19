<template>
  <div class="upload-container">
    <!-- Header -->
    <header class="upload-header">
      <h1>Upload a New Track</h1>
      <p>Share your music with the world</p>
    </header>

    <div class="upload-content">
      <!-- Left: Waveform & Audio Upload -->
      <div class="upload-section left-section">
        <!-- Drag & Drop Zone -->
        <div 
          class="drop-zone"
          @dragover.prevent="isDragging = true"
          @dragleave.prevent="isDragging = false"
          @drop.prevent="handleAudioDrop"
          :class="{ active: isDragging }"
        >
          <div v-if="!audioFile" class="drop-content">
            <div class="upload-icon">
              <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                <polyline points="7 10 12 15 17 10"></polyline>
                <line x1="12" y1="15" x2="12" y2="3"></line>
              </svg>
            </div>
            <h3>Drag and drop your audio file here</h3>
            <p>or</p>
            <button @click="$refs.audioInput.click()" class="btn-primary">
              Choose File
            </button>
            <p class="info-text">MP3, WAV, FLAC, OGG (Max 5GB)</p>
          </div>

          <div v-else class="audio-loaded">
            <p class="file-name">{{ audioFile.name }}</p>
            <p class="file-size">{{ formatFileSize(audioFile.size) }}</p>
            <button @click="removeAudio" class="btn-remove">Remove</button>
          </div>

          <input
            ref="audioInput"
            type="file"
            accept=".mp3,.wav,.flac,.ogg"
            @change="handleAudioSelect"
            style="display: none"
          />
        </div>

        <!-- Waveform Display -->
        <div v-if="audioFile" class="waveform-container">
          <div ref="waveformRef" class="waveform"></div>
          <div class="controls">
            <button @click="togglePlayback" class="btn-play">
              {{ isPlaying ? '⏸ Pause' : '▶ Play' }}
            </button>
            <span class="time">{{ formatTime(currentTime) }} / {{ formatTime(duration) }}</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: progressPercent + '%' }"></div>
          </div>
        </div>
      </div>

      <!-- Right: Form Fields -->
      <div class="upload-section right-section">
        <!-- Cover Art -->
        <div class="form-group">
          <label class="label">Cover Art</label>
          <div 
            class="cover-upload"
            @dragover.prevent="isDraggingCover = true"
            @dragleave.prevent="isDraggingCover = false"
            @drop.prevent="handleCoverDrop"
            :class="{ active: isDraggingCover }"
          >
            <img v-if="coverPreview" :src="coverPreview" :alt="trackTitle || 'Cover'" />
            <div v-else class="cover-placeholder">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <rect x="3" y="3" width="18" height="18" rx="2"></rect>
                <circle cx="8.5" cy="8.5" r="1.5"></circle>
                <polyline points="21 15 16 10 5 21"></polyline>
              </svg>
              <span>Click or drop image</span>
            </div>
            <input
              ref="coverInput"
              type="file"
              accept=".jpg,.jpeg,.png,.gif"
              @change="handleCoverSelect"
              style="display: none"
            />
          </div>
          <button @click="$refs.coverInput.click()" class="btn-secondary">
            Upload Cover
          </button>
        </div>

        <!-- Track Title -->
        <div class="form-group">
          <label class="label">Track Title *</label>
          <input
            v-model="trackTitle"
            type="text"
            placeholder="Enter track title"
            class="input-field"
            maxlength="100"
          />
          <span class="char-count">{{ trackTitle.length }}/100</span>
        </div>

        <!-- Description -->
        <div class="form-group">
          <label class="label">Description</label>
          <textarea
            v-model="trackDescription"
            placeholder="Tell us about your track..."
            class="textarea-field"
            maxlength="500"
            rows="4"
          ></textarea>
          <span class="char-count">{{ trackDescription.length }}/500</span>
        </div>

        <!-- Genre & Tags -->
        <div class="form-row">
          <div class="form-group half">
            <label class="label">Genre</label>
            <select v-model="trackGenre" class="input-field">
              <option value="">Select Genre</option>
              <option value="electronic">Electronic</option>
              <option value="hip-hop">Hip Hop</option>
              <option value="pop">Pop</option>
              <option value="rock">Rock</option>
              <option value="jazz">Jazz</option>
              <option value="classical">Classical</option>
              <option value="ambient">Ambient</option>
              <option value="other">Other</option>
            </select>
          </div>
          <div class="form-group half">
            <label class="label">Tags</label>
            <input
              v-model="trackTags"
              type="text"
              placeholder="e.g. remix, chill, beats"
              class="input-field"
            />
          </div>
        </div>

        <!-- Privacy Settings -->
        <div class="form-group">
          <label class="label">Privacy</label>
          <div class="radio-group">
            <label class="radio-label">
              <input v-model="trackPrivacy" type="radio" value="public" />
              <span>Public</span>
            </label>
            <label class="radio-label">
              <input v-model="trackPrivacy" type="radio" value="private" />
              <span>Private</span>
            </label>
            <label class="radio-label">
              <input v-model="trackPrivacy" type="radio" value="unlisted" />
              <span>Unlisted</span>
            </label>
          </div>
        </div>

        <!-- Checkbox for Monetization -->
        <div class="form-group">
          <label class="checkbox-label">
            <input v-model="allowComments" type="checkbox" />
            <span>Allow comments</span>
          </label>
          <label class="checkbox-label">
            <input v-model="allowDownload" type="checkbox" />
            <span>Allow download</span>
          </label>
        </div>

        <!-- Submit Button -->
        <div class="form-actions">
          <button 
            @click="handleSubmit" 
            class="btn-upload"
            :disabled="!isFormValid || isUploading"
          >
            {{ isUploading ? 'Uploading...' : 'Publish Track' }}
          </button>
          <button @click="handleReset" class="btn-cancel">
            Cancel
          </button>
        </div>

        <!-- Progress Bar -->
        <div v-if="uploadProgress > 0" class="upload-progress">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: uploadProgress + '%' }"></div>
          </div>
          <span class="progress-text">{{ uploadProgress }}%</span>
        </div>

        <!-- Success Message -->
        <div v-if="uploadSuccess" class="success-message">
          ✓ Track uploaded successfully!
        </div>

        <!-- Error Message -->
        <div v-if="uploadError" class="error-message">
          {{ uploadError }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import WaveSurfer from 'wavesurfer.js'
import { uploadTrack } from '../services/trackService.js'

// Audio & Waveform
const audioFile = ref(null)
const audioUrl = ref(null)
const waveformRef = ref(null)
let wavesurfer = null
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)

// UI States
const isDragging = ref(false)
const isDraggingCover = ref(false)
const isUploading = ref(false)
const uploadProgress = ref(0)
const uploadSuccess = ref(false)
const uploadError = ref(null)

// Form Fields
const trackTitle = ref('')
const trackDescription = ref('')
const trackGenre = ref('')
const trackTags = ref('')
const trackPrivacy = ref('public')
const allowComments = ref(true)
const allowDownload = ref(false)
const coverFile = ref(null)
const coverPreview = ref(null)

// Refs
const audioInput = ref(null)
const coverInput = ref(null)

// Computed
const isFormValid = computed(() => {
  return audioFile.value && trackTitle.value.trim().length > 0
})

const progressPercent = computed(() => {
  if (!duration.value) return 0
  return (currentTime.value / duration.value) * 100
})

// Methods
const handleAudioDrop = (e) => {
  isDragging.value = false
  const files = e.dataTransfer.files
  if (files.length > 0) {
    processAudioFile(files[0])
  }
}

const handleAudioSelect = (e) => {
  const files = e.target.files
  if (files.length > 0) {
    processAudioFile(files[0])
  }
}

const processAudioFile = (file) => {
  const validTypes = ['audio/mpeg', 'audio/wav', 'audio/flac', 'audio/ogg']
  if (!validTypes.some(type => file.type.includes(type.split('/')[1]))) {
    uploadError.value = 'Please upload a valid audio file (MP3, WAV, FLAC, OGG)'
    return
  }
  
  audioFile.value = file
  audioUrl.value = URL.createObjectURL(file)
  initializeWaveform()
  uploadError.value = null
}

const removeAudio = () => {
  audioFile.value = null
  if (audioUrl.value) {
    URL.revokeObjectURL(audioUrl.value)
    audioUrl.value = null
  }
  if (wavesurfer) {
    wavesurfer.destroy()
    wavesurfer = null
  }
  isPlaying.value = false
}

const initializeWaveform = () => {
  if (wavesurfer) {
    wavesurfer.destroy()
  }
  
  wavesurfer = WaveSurfer.create({
    container: waveformRef.value,
    waveColor: '#ccc',
    progressColor: '#ff6b00',
    height: 80,
    barWidth: 3,
    barRadius: 2,
    barGap: 2
  })

  wavesurfer.loadBlob(audioFile.value)

  wavesurfer.on('play', () => {
    isPlaying.value = true
  })

  wavesurfer.on('pause', () => {
    isPlaying.value = false
  })

  wavesurfer.on('timeupdate', (time) => {
    currentTime.value = time
  })

  wavesurfer.on('ready', () => {
    duration.value = wavesurfer.getDuration()
  })
}

const togglePlayback = () => {
  if (wavesurfer) {
    wavesurfer.playPause()
  }
}

const formatTime = (seconds) => {
  if (!seconds || isNaN(seconds)) return '0:00'
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

const handleCoverDrop = (e) => {
  isDraggingCover.value = false
  const files = e.dataTransfer.files
  if (files.length > 0) {
    processCoverFile(files[0])
  }
}

const handleCoverSelect = (e) => {
  const files = e.target.files
  if (files.length > 0) {
    processCoverFile(files[0])
  }
}

const processCoverFile = (file) => {
  if (!file.type.startsWith('image/')) {
    uploadError.value = 'Please upload a valid image file'
    return
  }
  
  coverFile.value = file
  const reader = new FileReader()
  reader.onload = (e) => {
    coverPreview.value = e.target.result
  }
  reader.readAsDataURL(file)
}

const handleSubmit = async () => {
  if (!isFormValid.value) return

  isUploading.value = true
  uploadProgress.value = 0
  uploadError.value = null
  uploadSuccess.value = false

  const formData = new FormData()
  formData.append('trackFile', audioFile.value)
  
  // Append Track entity fields as JSON string
  const trackData = {
    title: trackTitle.value,
    description: trackDescription.value,
    genre: trackGenre.value,
    tags: trackTags.value,
    privacy: trackPrivacy.value,
    allowComments: allowComments.value,
    allowDownload: allowDownload.value
  }
  formData.append('track', JSON.stringify(trackData))
  
  if (coverFile.value) {
    formData.append('trackCoverFile', coverFile.value)
  }

  try {
    const response = await uploadTrack(formData, (progress) => {
      uploadProgress.value = progress
    })

    uploadProgress.value = 100
    uploadSuccess.value = true
    
    setTimeout(() => {
      handleReset()
    }, 2000)
  } catch (error) {
    uploadError.value = error.message || 'Upload failed. Please try again.'
    console.error('Upload error:', error)
  } finally {
    isUploading.value = false
  }
}

const handleReset = () => {
  removeAudio()
  trackTitle.value = ''
  trackDescription.value = ''
  trackGenre.value = ''
  trackTags.value = ''
  trackPrivacy.value = 'public'
  allowComments.value = true
  allowDownload.value = false
  coverFile.value = null
  coverPreview.value = null
  uploadProgress.value = 0
  uploadSuccess.value = false
  uploadError.value = null
}

onBeforeUnmount(() => {
  if (wavesurfer) {
    wavesurfer.destroy()
  }
  if (audioUrl.value) {
    URL.revokeObjectURL(audioUrl.value)
  }
})
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.upload-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
  color: #fff;
  padding: 40px 20px;
}

.upload-header {
  max-width: 1400px;
  margin: 0 auto 40px;
  text-align: center;
}

.upload-header h1 {
  font-size: 2.5rem;
  margin: 0 0 10px 0;
  font-weight: 700;
}

.upload-header p {
  font-size: 1rem;
  color: #b0b0b0;
  margin: 0;
}

.upload-content {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}

@media (max-width: 1024px) {
  .upload-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
}

.upload-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Drag & Drop Zone */
.drop-zone {
  background: rgba(255, 255, 255, 0.05);
  border: 2px dashed #ff6b00;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.drop-zone:hover {
  background: rgba(255, 107, 0, 0.1);
  border-color: #ff8c2e;
}

.drop-zone.active {
  background: rgba(255, 107, 0, 0.2);
  border-color: #ffa44d;
  transform: scale(1.02);
}

.drop-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  width: 100%;
}

.upload-icon {
  color: #ff6b00;
  opacity: 0.8;
}

.drop-content h3 {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
}

.drop-content p {
  margin: 0;
  color: #b0b0b0;
  font-size: 0.9rem;
}

.info-text {
  font-size: 0.8rem !important;
  color: #888 !important;
}

.audio-loaded {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.file-name {
  font-weight: 600;
  margin: 0;
  word-break: break-word;
}

.file-size {
  font-size: 0.9rem;
  color: #b0b0b0;
  margin: 0;
}

/* Waveform */
.waveform-container {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.waveform {
  width: 100%;
  height: 100px;
  margin-bottom: 15px;
}

.controls {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
}

.btn-play {
  background: #ff6b00;
  color: #fff;
  border: none;
  padding: 8px 20px;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.btn-play:hover {
  background: #ff8c2e;
  transform: scale(1.05);
}

.time {
  font-size: 0.9rem;
  color: #b0b0b0;
}

.progress-bar {
  height: 4px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
  overflow: hidden;
  cursor: pointer;
}

.progress-fill {
  height: 100%;
  background: #ff6b00;
  transition: width 0.1s linear;
}

/* Form Groups */
.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.label {
  font-weight: 600;
  font-size: 0.95rem;
  color: #fff;
}

.input-field,
.textarea-field {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 8px;
  padding: 12px;
  color: #fff;
  font-size: 0.95rem;
  font-family: inherit;
  transition: all 0.3s ease;
}

.input-field:focus,
.textarea-field:focus {
  outline: none;
  border-color: #ff6b00;
  background: rgba(255, 255, 255, 0.12);
  box-shadow: 0 0 0 3px rgba(255, 107, 0, 0.1);
}

.input-field::placeholder,
.textarea-field::placeholder {
  color: #888;
}

.textarea-field {
  resize: vertical;
  font-family: inherit;
}

.char-count {
  font-size: 0.8rem;
  color: #888;
  text-align: right;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.form-group.half {
  width: 100%;
}

/* Cover Art */
.cover-upload {
  background: rgba(255, 255, 255, 0.05);
  border: 2px dashed rgba(255, 107, 0, 0.5);
  border-radius: 12px;
  aspect-ratio: 1;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  transition: all 0.3s ease;
}

.cover-upload:hover {
  border-color: #ff6b00;
  background: rgba(255, 107, 0, 0.05);
}

.cover-upload.active {
  border-color: #ffa44d;
  background: rgba(255, 107, 0, 0.15);
}

.cover-upload img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  color: #b0b0b0;
  text-align: center;
  font-size: 0.85rem;
}

.cover-placeholder svg {
  opacity: 0.6;
}

/* Radio & Checkbox */
.radio-group {
  display: flex;
  gap: 20px;
  margin-top: 8px;
}

.radio-label,
.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 0.95rem;
  color: #b0b0b0;
  transition: color 0.3s ease;
}

.radio-label:hover,
.checkbox-label:hover {
  color: #fff;
}

.radio-label input,
.checkbox-label input {
  cursor: pointer;
  width: 18px;
  height: 18px;
  accent-color: #ff6b00;
}

/* Buttons */
.btn-primary,
.btn-secondary,
.btn-cancel,
.btn-upload,
.btn-remove {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: #ff6b00;
  color: #fff;
}

.btn-primary:hover {
  background: #ff8c2e;
}

.btn-secondary {
  background: rgba(255, 107, 0, 0.2);
  color: #ff6b00;
  border: 1px solid #ff6b00;
  width: 100%;
}

.btn-secondary:hover {
  background: rgba(255, 107, 0, 0.3);
}

.btn-remove {
  background: rgba(255, 107, 0, 0.2);
  color: #ff6b00;
  padding: 6px 12px;
  font-size: 0.85rem;
}

.btn-remove:hover {
  background: rgba(255, 107, 0, 0.3);
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

.btn-upload,
.btn-cancel {
  flex: 1;
  padding: 14px;
  font-size: 1rem;
}

.btn-upload {
  background: #ff6b00;
  color: #fff;
}

.btn-upload:hover:not(:disabled) {
  background: #ff8c2e;
  transform: translateY(-2px);
}

.btn-upload:disabled {
  background: #666;
  cursor: not-allowed;
  opacity: 0.7;
}

.btn-cancel {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.btn-cancel:hover {
  background: rgba(255, 255, 255, 0.15);
}

/* Messages & Progress */
.upload-progress,
.success-message,
.error-message {
  border-radius: 8px;
  padding: 15px;
  margin-top: 15px;
  font-weight: 500;
}

.upload-progress {
  background: rgba(255, 107, 0, 0.1);
  border: 1px solid rgba(255, 107, 0, 0.3);
}

.progress-text {
  display: block;
  margin-top: 10px;
  font-size: 0.9rem;
  color: #ff6b00;
}

.success-message {
  background: rgba(76, 175, 80, 0.2);
  border: 1px solid rgba(76, 175, 80, 0.5);
  color: #4caf50;
  text-align: center;
  animation: slideIn 0.3s ease;
}

.error-message {
  background: rgba(244, 67, 54, 0.2);
  border: 1px solid rgba(244, 67, 54, 0.5);
  color: #f44336;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
