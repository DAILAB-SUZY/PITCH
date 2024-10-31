import styled from 'styled-components';
import AlbumChatCard from './AlbumChatCard';
import { useEffect, useRef, useState } from 'react';
import { fetchGET } from '../utils/fetchData';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Text = styled.div<{ fontSize?: string; margin?: string; color?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Rg';
  color: ${props => props.color};
`;

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

interface AlbumChatComment {
  albumChatCommentId: number;
  content: string;
  createAt: string; // ISO 형식의 날짜 문자열
  updateAt: string; // ISO 형식의 날짜 문자열
  author: User;
  comments: AlbumChatComment[]; // 재귀적 구조
  likes: User[];
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

interface AlbumChatComment {
  albumChatCommentId: number;
  content: string;
  createAt: string; // ISO 날짜 형식
  updateAt: string; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: User;
}

interface AlbumProps {
  spotifyAlbumId: string;
}

function AlbumPageChatTab({ spotifyAlbumId }: AlbumProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [albumChatList, setaAlbumChatList] = useState<AlbumChatComment[]>([]);
  // Intersection Observer용 ref
  const observerRef = useRef<HTMLDivElement | null>(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [isEnd, setIsEnd] = useState(false);
  console.log('postlist');
  console.log(albumChatList);

  let AlbumChatUrl = `/api/album/${spotifyAlbumId}/albumchat?sorted=recent&page=${pageNumber}&limit=5`;
  const fetchAlbumChat = async (token: string, refreshToken: string) => {
    if (token && !isLoading && !isEnd) {
      setIsLoading(true);
      await fetchGET(token, refreshToken, AlbumChatUrl).then(data => {
        if (data.length === 0) {
          console.log('list End');
          setIsEnd(true);
        }
        setaAlbumChatList(prevList => [...prevList, ...data]);
        setPageNumber(prevPage => prevPage + 1); // 페이지 증가
      });
      setIsLoading(false);
    }
  };

  // Intersection Observer 설정
  useEffect(() => {
    const observer = new IntersectionObserver(entries => {
      // observerRef가 화면에 보이면 fetch 호출
      if (!isLoading && entries[0].isIntersecting) {
        fetchAlbumChat(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
      }
    });

    if (observerRef.current) {
      observer.observe(observerRef.current); // ref가 있는 요소를 관찰 시작
    }

    return () => {
      console.log('start clean');
      if (observerRef.current) {
        console.log('clean');
        observer.unobserve(observerRef.current); // 컴포넌트 언마운트 시 관찰 해제
      }
    };
  }, [pageNumber]);
  return (
    <Container>
      {albumChatList && albumChatList?.length > 0 ? (
        albumChatList?.map((chat: any, index: number) => <AlbumChatCard key={index} comment={chat} spotifyAlbumId={spotifyAlbumId}></AlbumChatCard>)
      ) : (
        <Text margin="40px 0px 0px 0px"> Chat이 없습니다.</Text>
      )}
      <div ref={observerRef} style={{ height: '100px', backgroundColor: 'transparent' }} />
    </Container>
  );
}

export default AlbumPageChatTab;
