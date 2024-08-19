import styled from "styled-components";
import { Routes, Route, BrowserRouter, useNavigate } from "react-router-dom";
import LoginPage from "./stories/pages/LoginPage";
import StartPage from "./stories/pages/StartPage";
import SignupPage from "./stories/pages/SignupPage";
import HomePage from "./stories/pages/HomePage";
import PlayListPage from "./stories/pages/PlayListPage";
import AlbumChatPage from "./stories/pages/AlbumChatPage";
import AlbumChatPostPage from "./stories/pages/AlbumChatPostPage";
import MusicProfilePage from "./stories/pages/MusicProfilePage";
import "./styles/reset.css";

type titleType = {
  fontSize: string;
  margin: string;
};

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="" element={<StartPage />} />
        <Route path="/Login" element={<LoginPage />} />
        <Route path="/Signup" element={<SignupPage />} />
        <Route path="/Home" element={<HomePage />} />
        <Route path="/PlayListPage" element={<PlayListPage />} />
        <Route path="/AlbumChatPage" element={<AlbumChatPage />} />
        <Route path="/AlbumChatPostPage" element={<AlbumChatPostPage />} />
        <Route path="/MusicProfile" element={<MusicProfilePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
