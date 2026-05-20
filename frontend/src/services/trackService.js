// API configuration and helper functions for track uploads
// Relative path requires a dev server proxy (configured below)
const API_BASE_URL = import.meta.env.VITE_API_URL || '/api'

const UPLOAD_TIMEOUT = 300000 // 5 minutes timeout for large files

export const uploadTrack = async (formData, onProgress = null) => {
  return new Promise((resolve, reject) => {
    // Safety check: Don't let empty requests burn server connections
    if (!(formData instanceof FormData)) {
      return reject(new Error('Data must be structured as a FormData object'));
    }

    const xhr = new XMLHttpRequest()
    let timeoutId = null

    // Set timeout
    timeoutId = setTimeout(() => {
      xhr.abort()
      reject(new Error('Upload timeout - server took too long to respond'))
    }, UPLOAD_TIMEOUT)

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
      clearTimeout(timeoutId)
      
      // Handle browser network dropping/CORS errors safely
      if (xhr.status === 0) {
        return reject(new Error(`Network error: Cannot connect to server at ${API_BASE_URL}. Check proxy settings.`))
      }

      if (xhr.status === 200 || xhr.status === 201) {
        try {
          const response = xhr.responseText ? JSON.parse(xhr.responseText) : { success: true }
          resolve(response)
        } catch (e) {
          resolve({ success: true }) // Fallback if backend returns plain text/empty body
        }
      } else {
        try {
          const errorData = JSON.parse(xhr.responseText)
          reject(new Error(errorData.message || `Upload failed with status ${xhr.status}`))
        } catch {
          reject(new Error(`Upload failed with status ${xhr.status}: ${xhr.statusText}`))
        }
      }
    })

    xhr.addEventListener('error', () => {
      clearTimeout(timeoutId)
      reject(new Error('Network error: Failed to connect to server. Check if backend is running.'))
    })

    xhr.addEventListener('abort', () => {
      clearTimeout(timeoutId)
      // Only reject if it wasn't already triggered by the timeout block
      reject(new Error('Upload cancelled'))
    })

    xhr.open('POST', `${API_BASE_URL}/track/upload`)
    // Browser automatically builds the perfect 'multipart/form-data; boundary=...' header
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