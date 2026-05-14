// API configuration and helper functions for track uploads
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3000/api'

export const uploadTrack = async (formData, onProgress = null) => {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest()

    // Track upload progress
    if (onProgress) {
      xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable) {
          const progress = Math.round((e.loaded / e.total) * 100)
          onProgress(progress)
        }
      })
    }

    xhr.addEventListener('load', () => {
      if (xhr.status === 200 || xhr.status === 201) {
        resolve(JSON.parse(xhr.responseText))
      } else {
        reject(new Error(`Upload failed with status ${xhr.status}`))
      }
    })

    xhr.addEventListener('error', () => {
      reject(new Error('Network error during upload'))
    })

    xhr.addEventListener('abort', () => {
      reject(new Error('Upload cancelled'))
    })

    xhr.open('POST', `${API_BASE_URL}/tracks/upload`)
    xhr.send(formData)
  })
}

export const fetchTrackDetails = async (trackId) => {
  const response = await fetch(`${API_BASE_URL}/tracks/${trackId}`)
  if (!response.ok) throw new Error('Failed to fetch track')
  return response.json()
}

export const updateTrackMetadata = async (trackId, metadata) => {
  const response = await fetch(`${API_BASE_URL}/tracks/${trackId}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(metadata)
  })
  if (!response.ok) throw new Error('Failed to update track')
  return response.json()
}
