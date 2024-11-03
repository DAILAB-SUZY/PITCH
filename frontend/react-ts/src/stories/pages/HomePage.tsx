import styled from 'styled-components';

import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Nav from '../components/Nav';
import AlbumPostCard from '../components/AlbumPostCard';
import PlaylistPreviewCard from '../components/PlaylistPreviewCard';
import useStore from '../store/store';
import { fetchGET, MAX_REISSUE_COUNT } from '../utils/fetchData';
import Loader from '../components/Loader';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh; //auto;
  width: 100vw;
  background-color: white;
  color: black;
`;

const Header = styled.div`
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Body = styled.div`
  margin-top: 110px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const PlaylistArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: column;
  background-color: white;
`;

const AlbumPostArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: column;
  background-color: white;
`;

const AlbumPostTitleArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-end;
  gap: 8px;
  width: 100vw;
  height: auto;
`;

const RowAlignArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: white;
  overscroll-behavior: none;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Bd';
`;

const Text = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Rg';
`;

interface FriendsPlayList {
  playlistId: number;
  albumCover: string[];
  author: {
    id: number;
    username: string;
    profilePicture: string;
  };
}
interface DNA {
  dnaKey: number;
  dnaName: string;
}

interface User {
  id: number;
  username: string;
  profilePicture: string;
  dnas: DNA[];
}

interface Album {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
  likes: User[];
}

interface PostAuthor extends User {}

interface PostDetail {
  postId: number;
  content: string;
  createAt: string;
  updateAt: string;
  author: PostAuthor;
  album: Album;
}

interface CommentAuthor extends User {}

interface ChildComment {
  id: number;
  content: string;
  author: CommentAuthor;
  createTime: string;
  updateTime: string;
}

interface Comment {
  id: number;
  content: string;
  createAt: string;
  updateAt: string;
  likes: User[];
  childComments: ChildComment[];
  author: CommentAuthor;
}

interface AlbumPost {
  postDetail: PostDetail;
  comments: Comment[];
  likes: User[];
}

function HomePage() {
  const { email, name, id } = useStore();
  // const { albumPosts, setAlbumPosts, clearAlbumPosts } = useAlbumPostStore();

  const navigate = useNavigate();
  const GoToAlbumPostEditPage = () => {
    navigate('/AlbumPostEditPage');
  };

  const [albumPosts, setAlbumPosts] = useState<AlbumPost[]>([]);

  console.log(`${email} / ${name} / ${id}`);
  // const [albumPostList, setAlbumPostList] = useState<AlbumPost[]>([]);
  const [postPage, setPostPage] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [isEnd, setIsEnd] = useState(false);
  const [friendsPlayList, setfriendsPlayList] = useState<FriendsPlayList[]>([]);

  // Intersection Observer용 ref
  const observerRef = useRef<HTMLDivElement | null>(null);

  const PlaylistUrl = `/api/playlist/following`;
  const AlbumPostUrl = `/api/album/post?page=${postPage}&limit=5`;
  const fetchPlaylist = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, PlaylistUrl, MAX_REISSUE_COUNT).then(data => {
      if (data) {
        setfriendsPlayList(prevList => [...prevList, ...data]);
      }
    });
  };

  const fetchAlbumPosts = async (token: string, refreshToken: string) => {
    if (token && !isLoading && !isEnd) {
      setIsLoading(loading => !loading);
      fetchGET(token, refreshToken, AlbumPostUrl, MAX_REISSUE_COUNT).then(data => {
        if (data) {
          console.log('set PostList');
          if (data.length === 0) {
            console.log('list End');
            setIsEnd(true);
          }
          setAlbumPosts(prev => [...prev, ...data]); // 기존 데이터에 새로운 데이터를 추가
          setPostPage(prevPage => prevPage + 1); // 페이지 증가
          console.log('albumpost ', albumPosts);
        }
      });
    }
    setIsLoading(false); // 로딩 상태 해제
  };

  useEffect(() => {
    fetchPlaylist(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  }, []);

  // Intersection Observer 설정
  useEffect(() => {
    const observer = new IntersectionObserver(entries => {
      // observerRef가 화면에 보이면 fetch 호출
      if (!isLoading && entries[0].isIntersecting) {
        fetchAlbumPosts(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
      }
    });

    if (observerRef.current) {
      console.log('current: ', observerRef.current);
      observer.observe(observerRef.current); // ref가 있는 요소를 관찰 시작
    }

    return () => {
      console.log('start clean');
      if (observerRef.current) {
        console.log('clean');
        observer.unobserve(observerRef.current); // 컴포넌트 언마운트 시 관찰 해제
      }
    };
  }, [postPage]);

  return (
    <Container>
      <Header>
        <Nav page={1} />
      </Header>
      <Body>
        <PlaylistArea>
          <Title fontSize="22px" margin="20px 0px 0px 20px">
            Friend's Playlist
          </Title>
          {isLoading ? <Loader /> : <PlaylistPreviewCard playlists={friendsPlayList} />}
        </PlaylistArea>
        <AlbumPostArea>
          <AlbumPostTitleArea>
            <Title fontSize="22px" margin="20px 0px 0px 20px">
              Album Post
            </Title>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="22"
              height="22"
              fill="currentColor"
              className="bi bi-pencil-square"
              viewBox="0 0 16 16"
              onClick={() => {
                GoToAlbumPostEditPage();
              }}
            >
              <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
              <path
                fillRule="evenodd"
                d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
              />
            </svg>
          </AlbumPostTitleArea>
          <RowAlignArea>
            {albumPosts && albumPosts.length > 0 ? albumPosts?.map((albumPost, index) => <AlbumPostCard key={index} albumPost={albumPost} />) : <Text fontSize="15px" margin="150px 0px 0px 0px" />}
          </RowAlignArea>
        </AlbumPostArea>
        {isEnd ? (
          <Text fontSize="16px" margin="20px 0px">
            더이상 게시물이 없습니다
          </Text>
        ) : isLoading ? (
          <Text fontSize="16px" margin="20px 0px">
            <Loader></Loader>로딩 중...
          </Text>
        ) : (
          <div ref={observerRef} style={{ height: '100px', backgroundColor: 'transparent' }} />
        )}
      </Body>
    </Container>
  );
}

export default HomePage;
