import create from "zustand";
import { persist } from "zustand/middleware";

interface UserInfo {
  email: string;
  name: string;
  id: number;
  setEmail: (Email: string) => void;
  setName: (Name: string) => void;
  setId: (Id: number) => void;
}

const useStore = create<UserInfo>()(
  persist(
    (set) => ({
      email: "",
      name: "",
      id: 0,
      setEmail: (Email) => {
        set({ email: Email });
      },
      setName: (Name) => {
        set({ name: Name });
      },
      setId: (Id) => {
        set({ id: Id });
      },
    }),
    {
      name: "user-info", // 로컬 스토리지에 저장될 키 이름
      getStorage: () => localStorage, // 로컬 스토리지 사용
    }
  )
);

export default useStore;
