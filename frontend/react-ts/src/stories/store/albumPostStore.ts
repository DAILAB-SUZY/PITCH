import create from "zustand";
import { persist } from "zustand/middleware";

// AlbumPost 인터페이스 정의
interface AlbumPost {
  postDetail: {
    postId: number;
    content: string;
    createAt: number;
    updateAt: number;
    author: {
      id: number;
      username: string;
      profilePicture: string;
      dnas: [
        {
          dnaKey: number;
          dnaName: string;
        }[],
      ];
    };
    album: {
      id: number;
      title: string;
      albumCover: string;
      artistName: string;
      genre: string;
    };
  };
  comments: {
    id: number;
    content: string;
    createdAt: number;
    updatedAt: number;
    likes: {
      id: number;
      username: string;
      profilePicture: string;
    }[];
    childComments: {
      id: number;
      content: string;
      author: {
        id: number;
        username: string;
        profilePicture: string;
      };
    }[];
    author: {
      id: number;
      username: string;
      profilePicture: string;
    };
  }[];
  likes: {
    id: number;
    username: string;
    profilePicture: string;
  }[];
}

// Zustand 스토어 타입 정의
interface AlbumPostStore {
  albumPosts: AlbumPost[];
  setAlbumPosts: (posts: AlbumPost[]) => void;
  getAlbumPostById: (postId: number) => AlbumPost | undefined;
  addAlbumPost: (post: AlbumPost) => void;
  replaceAlbumPostById: (postId: number, newPost: AlbumPost) => void;
  clearAlbumPosts: () => void;
}

// Zustand 스토어 생성
const useAlbumPostStore = create<AlbumPostStore>()(
  persist(
    (set, get) => ({
      albumPosts: [],

      setAlbumPosts: (posts) => set((state) => ({ albumPosts: [...state.albumPosts, ...posts] })),

      getAlbumPostById: (postId) => get().albumPosts.find((post) => post.postDetail.postId === postId),

      addAlbumPost: (post) => set((state) => ({ albumPosts: [...state.albumPosts, post] })),

      replaceAlbumPostById: (postId, newPost) =>
        set((state) => ({
          albumPosts: state.albumPosts.map((post) => (post.postDetail.postId === postId ? newPost : post)),
        })),

      clearAlbumPosts: () => set({ albumPosts: [] }),
    }),
    {
      name: "album-posts", // 로컬 스토리지에 저장될 키 이름
      getStorage: () => localStorage, // 로컬 스토리지 사용
    }
  )
);

export default useAlbumPostStore;
