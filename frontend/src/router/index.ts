import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("../views/Home.vue"),
  },
  {
    path: "/essay",
    name: "essay",
    component: () => import("../views/Essay.vue"),
  },
  {
    path: "/prose",
    name: "prose",
    component: () => import("../views/Prose.vue"),
  },
  {
    path: "/album",
    name: "album",
    component: () => import("../views/Album/Album.vue"),
    redirect: "/album/upload",
    children:[
      {
        path: "upload",
        name: "album-upload",
        component: () => import("../views/Album/AlbumUpload.vue"),
      },
      {
        path: "select",
        name: "album-select",
        component: () => import("../views/Album/AlbumSelect.vue"),
      }
    ]
  },
  {
    path: "/calendar",
    name: "calendar",
    component: () => import("../views/Calendar.vue"),
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;