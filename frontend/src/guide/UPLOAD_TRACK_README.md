# Upload Track Page - SoundCloud Clone

A feature-rich audio track upload page inspired by SoundCloud, built with Vue 3 and Vite.

## Features

✅ **Drag & Drop Upload** - Drag audio files or click to browse
✅ **Waveform Preview** - Visual audio waveform using WaveSurfer.js
✅ **Playback Controls** - Play/pause with time display
✅ **Cover Art Upload** - Drag & drop or click to upload cover images
✅ **Track Metadata** - Title, description, genre, tags
✅ **Privacy Settings** - Public, private, or unlisted tracks
✅ **Permissions** - Control comments and downloads
✅ **Real-time Progress** - Upload progress tracking
✅ **Responsive Design** - Mobile and desktop optimized
✅ **Error Handling** - User-friendly error messages

## Installation

### 1. Install Dependencies

```bash
npm install
npm install wavesurfer.js
```

### 2. Setup Router

Add the upload page to your router configuration:

```javascript
// In your router/index.js or main router file
import UploadTrack from '@/view/UploadTrack.vue'

const routes = [
  // ... other routes
  {
    path: '/upload',
    name: 'UploadTrack',
    component: UploadTrack
  }
]
```

### 3. Configure API Endpoint

Set your backend API URL in `.env.local`:

```env
VITE_API_URL=http://localhost:3000/api
```

Or it will default to `http://localhost:3000/api`

## Usage

### Basic Import

```vue
<template>
  <UploadTrack />
</template>

<script setup>
import UploadTrack from '@/view/UploadTrack.vue'
</script>
```

### Backend API Integration

The upload component sends data to: `POST /api/tracks/upload`

**Expected FormData:**
```
- file: Audio file (mp3, wav, flac, ogg)
- title: Track title (required)
- description: Track description
- genre: Genre selection
- tags: Space or comma-separated tags
- privacy: 'public' | 'private' | 'unlisted'
- allowComments: boolean
- allowDownload: boolean
- cover: Cover image file (optional)
```

### Using the Track Service

```javascript
import { uploadTrack } from '@/services/trackService.js'

const formData = new FormData()
formData.append('file', audioFile)
formData.append('title', 'My Track')
// ... add more fields

try {
  const response = await uploadTrack(formData, (progress) => {
    console.log(`Upload progress: ${progress}%`)
  })
  console.log('Track uploaded:', response)
} catch (error) {
  console.error('Upload failed:', error)
}
```

## Supported Audio Formats

- MP3 (.mp3)
- WAV (.wav)
- FLAC (.flac)
- OGG (.ogg)

**Max File Size:** 5GB (configurable)

## Supported Cover Image Formats

- JPG/JPEG (.jpg, .jpeg)
- PNG (.png)
- GIF (.gif)

## Styling

The component uses a modern dark theme with orange accents (SoundCloud-inspired):

- **Primary Color:** #ff6b00 (Orange)
- **Background:** Dark gradient (#0f0c29, #302b63, #24243e)
- **Text:** Light colors with good contrast

### Customize Colors

Edit the CSS variables in `UploadTrack.vue` `<style>` section:

```css
/* Change primary color */
.drop-zone {
  border-color: #your-color; /* Change orange to your color */
}

.btn-upload {
  background: #your-color;
}
```

## Component Props & State

### Reactive Data
- `audioFile` - Selected audio file
- `coverFile` - Selected cover image
- `trackTitle` - Track name
- `trackDescription` - Track description
- `trackGenre` - Selected genre
- `trackTags` - Tags
- `trackPrivacy` - Privacy level
- `allowComments` - Comments enabled
- `allowDownload` - Download enabled
- `isPlaying` - Playback state
- `currentTime` - Current playback time
- `duration` - Total duration
- `uploadProgress` - Upload progress (0-100)

## Example Backend Implementation (Node.js/Express)

```javascript
import express from 'express'
import multer from 'multer'
import fs from 'fs'

const app = express()
const upload = multer({ dest: 'uploads/' })

app.post('/api/tracks/upload', upload.fields([
  { name: 'file', maxCount: 1 },
  { name: 'cover', maxCount: 1 }
]), async (req, res) => {
  try {
    const { file, cover } = req.files
    const { title, description, genre, tags, privacy, allowComments, allowDownload } = req.body

    // Validate
    if (!file || !title) {
      return res.status(400).json({ error: 'Missing required fields' })
    }

    // Process upload (move files, save metadata to DB, etc.)
    const trackId = generateUUID()
    
    // Save to database
    await db.tracks.create({
      id: trackId,
      title,
      description,
      genre,
      tags: tags?.split(',').map(t => t.trim()),
      privacy,
      allowComments,
      allowDownload,
      audioPath: file[0].path,
      coverPath: cover ? cover[0].path : null,
      uploadedAt: new Date()
    })

    res.json({ 
      trackId, 
      message: 'Track uploaded successfully',
      url: `/track/${trackId}`
    })
  } catch (error) {
    res.status(500).json({ error: error.message })
  }
})
```

## Responsive Design

The layout adapts automatically:
- **Desktop (>1024px):** Two-column layout (upload + waveform on left, form on right)
- **Tablet/Mobile (<1024px):** Single column layout (stacked vertically)

## Accessibility

- Proper semantic HTML
- ARIA labels on form fields
- Keyboard navigation support
- Focus management

## Performance Tips

1. **Large Files:** Consider chunked uploads for files > 100MB
2. **Image Optimization:** Compress cover images before upload
3. **Progress Tracking:** Use XMLHttpRequest for accurate progress (implemented in trackService.js)

## Troubleshooting

**Waveform not showing:**
- Ensure audio file is valid and supported
- Check browser console for errors
- Verify WaveSurfer.js is installed

**Upload fails:**
- Check CORS settings on backend
- Verify API endpoint is correct
- Ensure formData is properly formatted

**Drag & Drop not working:**
- Check for CSS that might prevent events
- Ensure dropzone is visible and clickable

## Browser Support

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Mobile browsers (iOS Safari, Chrome Mobile)

## License

MIT - Feel free to use and customize!

## Next Steps

1. Set up your backend API at `/api/tracks/upload`
2. Configure your `.env.local` with correct API URL
3. Add router link to navigate to upload page
4. Test with sample audio files
5. Customize colors and styling to match your brand
