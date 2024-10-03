import create from "zustand";

interface UserInfo {
  email: string;
  name: string;
  id: number;
  setEmail: (Email: string) => void;
  setName: (Name: string) => void;
  setId: (Id: number) => void;
}

const useStore = create<UserInfo>((set) => ({
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
}));

export default useStore;
