<script setup>
import { ref } from 'vue';
import '../styles/login.css';

const isSignUp = ref(false);
const showPassword = ref(false);

const form = ref({
  email: '',
  password: '',
  confirmPassword: '',
  name: '',
});

const errors = ref({});

const validateEmail = (email) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
};

const validate = () => {
  errors.value = {};

  if (!form.value.email) {
    errors.value.email = 'Email is required';
  } else if (!validateEmail(form.value.email)) {
    errors.value.email = 'Invalid email format';
  }

  if (!form.value.password) {
    errors.value.password = 'Password is required';
  } else if (form.value.password.length < 6) {
    errors.value.password = 'Password must be at least 6 characters';
  }

  if (isSignUp.value) {
    if (!form.value.name) {
      errors.value.name = 'Name is required';
    }
    if (!form.value.confirmPassword) {
      errors.value.confirmPassword = 'Please confirm your password';
    } else if (form.value.password !== form.value.confirmPassword) {
      errors.value.confirmPassword = 'Passwords do not match';
    }
  }

  return Object.keys(errors.value).length === 0;
};

const handleSubmit = () => {
  if (validate()) {
    console.log(isSignUp.value ? `Sign up with: ${JSON.stringify(form.value)}` : `Login with: ${JSON.stringify(form.value)}`);
    // Handle authentication here
  }
};

const handleSocialLogin = (provider) => {
  console.log(`Login with ${provider}`);
  // Handle social login here
};

const toggleMode = () => {
  isSignUp.value = !isSignUp.value;
  errors.value = {};
  form.value = { email: '', password: '', confirmPassword: '', name: '' };
};
</script>

<template>
  <div class="login-container">
    <!-- Left Side - Brand -->
    <div class="login-left">
      <div class="login-brand">
        <div class="brand-icon">♪</div>
        <h1 class="brand-name">Binh.Cloud</h1>
      </div>
      <p class="brand-tagline">Your Music, Your Way</p>
    </div>

    <!-- Right Side - Form -->
    <div class="login-right">
      <div class="login-card">
        <h2 class="login-title">{{ isSignUp ? 'Create Account' : 'Welcome Back' }}</h2>
        <p class="login-subtitle">{{ isSignUp ? 'Sign up to get started' : 'Login to your account' }}</p>

        <!-- Social Login -->
        <div class="social-login">
          <button class="social-btn google-btn" @click="handleSocialLogin('Google')">
            <span class="social-icon">G</span>
            <span>Google</span>
          </button>
          <button class="social-btn github-btn" @click="handleSocialLogin('GitHub')">
            <span class="social-icon">⚡</span>
            <span>GitHub</span>
          </button>
        </div>

        <div class="divider">
          <span>or</span>
        </div>

        <!-- Form -->
        <form @submit.prevent="handleSubmit" class="login-form">
          <!-- Name Field (Sign Up Only) -->
          <div class="form-group" v-if="isSignUp">
            <label for="name" class="form-label">Full Name</label>
            <input
              id="name"
              v-model="form.name"
              type="text"
              class="form-input"
              :class="{ 'has-error': errors.name }"
              placeholder="Your name"
            />
            <span class="error-message" v-if="errors.name">{{ errors.name }}</span>
          </div>

          <!-- Email Field -->
          <div class="form-group">
            <label for="email" class="form-label">Email</label>
            <input
              id="email"
              v-model="form.email"
              type="email"
              class="form-input"
              :class="{ 'has-error': errors.email }"
              placeholder="your@email.com"
            />
            <span class="error-message" v-if="errors.email">{{ errors.email }}</span>
          </div>

          <!-- Password Field -->
          <div class="form-group">
            <label for="password" class="form-label">Password</label>
            <div class="password-input-wrap">
              <input
                id="password"
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                class="form-input"
                :class="{ 'has-error': errors.password }"
                placeholder="••••••••"
              />
              <button
                type="button"
                class="toggle-password"
                @click="showPassword = !showPassword"
              >
                {{ showPassword ? '👁️' : '👁️‍🗨️' }}
              </button>
            </div>
            <span class="error-message" v-if="errors.password">{{ errors.password }}</span>
          </div>

          <!-- Confirm Password Field (Sign Up Only) -->
          <div class="form-group" v-if="isSignUp">
            <label for="confirm-password" class="form-label">Confirm Password</label>
            <input
              id="confirm-password"
              v-model="form.confirmPassword"
              type="password"
              class="form-input"
              :class="{ 'has-error': errors.confirmPassword }"
              placeholder="••••••••"
            />
            <span class="error-message" v-if="errors.confirmPassword">{{ errors.confirmPassword }}</span>
          </div>

          <!-- Remember Me / Forgot Password (Login Only) -->
          <div class="form-options" v-if="!isSignUp">
            <label class="remember-me">
              <input type="checkbox" />
              <span>Remember me</span>
            </label>
            <a href="#" class="forgot-password">Forgot password?</a>
          </div>

          <!-- Submit Button -->
          <button type="submit" class="submit-btn">
            {{ isSignUp ? 'Create Account' : 'Login' }}
          </button>
        </form>

        <!-- Toggle Mode -->
        <div class="toggle-mode">
          <p>{{ isSignUp ? 'Already have an account?' : "Don't have an account?" }}</p>
          <button @click="toggleMode" class="toggle-btn">
            {{ isSignUp ? 'Login here' : 'Sign up here' }}
          </button>
        </div>
      </div>

      <!-- Footer -->
      <p class="login-footer">
        By continuing, you agree to our <a href="#">Terms</a> and <a href="#">Privacy Policy</a>
      </p>
    </div>
  </div>
</template>
