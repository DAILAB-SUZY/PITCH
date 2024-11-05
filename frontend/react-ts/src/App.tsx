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
import AlbumChatCommentPostPage from './stories/pages/AlbumChatCommentPostPage';
import MusicProfilePage from './stories/pages/MusicProfilePage';
import MusicProfileEditPage from './stories/pages/MusicProfileEditPage';
import AlbumPostPage from './stories/pages/AlbumPostPage';
import AlbumPostEditPage from './stories/pages/AlbumPostEditPage';
import AlbumPostCommentPostPage from './stories/pages/AlbumPostCommentPostPage';
import FollowPage from './stories/pages/FollowPage';
import FriendSearchPage from './stories/pages/FriendSearchPage';
import SearchPage from './stories/pages/SearchPage';
import ErrorPage from './stories/pages/ErrorPage';
import PlaylistRedirectPage from './stories/pages/PlaylistRedirectPage';
import './styles/reset.css';
import PasswordFindPage from './stories/pages/PasswordFindPage';
import PasswordResetPage from './stories/pages/PasswordResetPage';

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
        <Route path="/AlbumChatCommentPostPage" element={<AlbumChatCommentPostPage />} />
        <Route path="/MusicProfilePage" element={<MusicProfilePage />} />
        <Route path="/MusicProfileEditPage" element={<MusicProfileEditPage />} />
        <Route path="/AlbumPostPage" element={<AlbumPostPage />} />
        <Route path="/AlbumPostEditPage" element={<AlbumPostEditPage />} />
        <Route path="/AlbumPostCommentPostPage" element={<AlbumPostCommentPostPage />} />
        <Route path="/SearchPage" element={<SearchPage />} />
        <Route path="/FollowPage" element={<FollowPage />} />
        <Route path="/FriendSearchPage" element={<FriendSearchPage />} />
        <Route path="/ErrorPage" element={<ErrorPage />} />
        <Route path="/PasswordFindPage" element={<PasswordFindPage />} />
        <Route path="/user/passwordchange/:authCode" element={<PasswordResetPage />} />
        <Route path="/oauth2/callback/google" element={<PlaylistRedirectPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
