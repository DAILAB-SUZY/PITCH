import { Routes, Route, BrowserRouter } from 'react-router-dom';
import LoginPage from './stories/pages/LoginPage';
import StartPage from './stories/pages/StartPage';
import SignupPage from './stories/pages/SignupPage';
import HomePage from './stories/pages/HomePage';
import PlayListPage from './stories/pages/PlayListPage';
import PlayListEditPage from './stories/pages/PlayListEditPage';
import AlbumChatPage from './stories/pages/AlbumChatPage';
import AlbumHomePage from './stories/pages/AlbumHomePage';
import AlbumPage from './stories/pages/AlbumPage';
import AlbumChatPostPage from './stories/pages/AlbumChatPostPage';
import MusicProfilePage from './stories/pages/MusicProfilePage';
import MusicProfileEditPage from './stories/pages/MusicProfileEditPage';
import AlbumPostPage from './stories/pages/AlbumPostPage';
import AlbumPostEditPage from './stories/pages/AlbumPostEditPage';
import CommentPostPage from './stories/pages/CommentPostPage';
import FollowPage from './stories/pages/FollowPage';
import SearchPage from './stories/pages/SearchPage';
import './styles/reset.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="" element={<StartPage />} />
        <Route path="/Login" element={<LoginPage />} />
        <Route path="/Signup" element={<SignupPage />} />
        <Route path="/Home" element={<HomePage />} />
        <Route path="/PlayListPage" element={<PlayListPage />} />
        <Route path="/PlayListEditPage" element={<PlayListEditPage />} />
        <Route path="/AlbumChatPage" element={<AlbumChatPage />} />
        <Route path="/AlbumHomePage" element={<AlbumHomePage />} />
        <Route path="/AlbumPage" element={<AlbumPage />} />
        <Route path="/AlbumChatPostPage" element={<AlbumChatPostPage />} />
        <Route path="/MusicProfilePage" element={<MusicProfilePage />} />
        <Route path="/MusicProfileEditPage" element={<MusicProfileEditPage />} />
        <Route path="/AlbumPostPage" element={<AlbumPostPage />} />
        <Route path="/AlbumPostEditPage" element={<AlbumPostEditPage />} />
        <Route path="/CommentPostPage" element={<CommentPostPage />} />
        <Route path="/SearchPage" element={<SearchPage />} />
        <Route path="/FollowPage" element={<FollowPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
