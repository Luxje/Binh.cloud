# Quick Start: Upload Track Page Integration

## Step 1: Add Route

Add this to your Vue Router configuration (e.g., `src/router/index.js`):

```javascript
import UploadTrack from '@/view/UploadTrack.vue'

const routes = [
  {
    path: '/upload',
    name: 'UploadTrack',
    component: UploadTrack
  }
]
```

## Step 2: Add Navigation Link

Add a link in your navbar or menu:

```vue
<router-link to="/upload">Upload Track</router-link>
```

## Step 3: Configure Backend API

Create `.env.local` in the frontend directory:

```env
VITE_API_URL=http://localhost:3000/api
```

## Step 4: Test the Component

1. Run dev server: `npm run dev`
2. Navigate to `/upload`
3. Try uploading a test audio file

## Component Files Created

```
frontend/src/
├── view/
│   ├── UploadTrack.vue              ← Main component
│   └── UPLOAD_TRACK_README.md       ← Detailed documentation
├── services/
│   └── trackService.js              ← API helper functions
└── .env.local.example               ← Environment template
```

## Key Features Available

| Feature | Status |
|---------|--------|
| Drag & Drop Upload | ✅ Ready |
| Waveform Preview | ✅ Ready (WaveSurfer.js) |
| Play/Pause Controls | ✅ Ready |
| Cover Image Upload | ✅ Ready |
| Metadata Form | ✅ Ready |
| Privacy Settings | ✅ Ready |
| Upload Progress Tracking | ✅ Ready |
| Error Handling | ✅ Ready |
| Responsive Design | ✅ Ready |

## Backend API Contract

Your backend should handle:

```
POST /api/tracks/upload
Content-Type: multipart/form-data

Form Fields:
- file: Binary (audio file)
- title: String (required)
- description: String
- genre: String
- tags: String (comma-separated)
- privacy: String ('public' | 'private' | 'unlisted')
- allowComments: Boolean
- allowDownload: Boolean
- cover: Binary (image file, optional)

Expected Response (201/200):
{
  "trackId": "uuid-string",
  "message": "Track uploaded successfully",
  "url": "/track/uuid-string"
}
```

## Customization

### Colors
Edit `UploadTrack.vue` `<style scoped>`:
- Change `#ff6b00` to your brand color
- Modify gradient colors in `.upload-container`

### File Size Limits
Edit `UploadTrack.vue` in `processCoverFile()` method

### Genre Options
Edit the `<select v-model="trackGenre">` options

### Form Fields
Add new `ref()` state and form inputs as needed

## Environment Variables

```env
# Required
VITE_API_URL=http://localhost:3000/api

# Optional
VITE_APP_NAME=MusicApp
VITE_APP_VERSION=1.0.0
```

## Next Steps

1. ✅ Component is ready to use
2. 🔄 Set up your backend API endpoint
3. 🔄 Update router with the new route
4. 🔄 Customize styling for your brand
5. 🔄 Test with real audio files

## Need Help?

Refer to `UPLOAD_TRACK_README.md` in the same directory for:
- Detailed feature documentation
- Example backend implementation
- Troubleshooting guide
- Performance optimization tips
- Browser compatibility info
