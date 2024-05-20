import create from "zustand";

interface UserInfo {
  email: string;
  name: string;
  password: string;
  token: string;
  setEmail: (Email: string) => void;
  setName: (Name: string) => void;
  setPassword: (Password: string) => void;
  setToken: (Token: string) => void;
}

const useStore = create<UserInfo>((set) => ({
  email: "",
  name: "",
  password: "",
  token: "",
  setEmail: (Email) => {
    set({ email: Email });
  },
  setName: (Name) => {
    set({ name: Name });
  },
  setPassword: (Password) => {
    set({ password: Password });
  },
  setToken: (Token) => {
    set({ token: Token });
  },
}));

export default useStore;
