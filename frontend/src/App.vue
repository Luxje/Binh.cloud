<script setup>
import { ref } from 'vue';
import TrackWall from './view/TrackWall.vue';
import Login from './view/Login.vue';

const isLoggedIn = ref(false);

</script>

<template>
  <Login v-if="!isLoggedIn"></Login>
  <TrackWall v-else></TrackWall>
</template>
