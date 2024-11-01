import styled from 'styled-components';
import AlbumPostCard from './AlbumPostCard';
import { useEffect, useState } from 'react';
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

///

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
  createAt: number;
  updateAt: number;
  author: PostAuthor;
  album: Album;
}

interface CommentAuthor extends User {}

interface ChildComment {
  id: number;
  content: string;
  author: CommentAuthor;
  createAt: number;
  updateAt: number;
}

interface Comment {
  id: number;
  content: string;
  createAt: number;
  updateAt: number;
  likes: User[];
  childComments: ChildComment[];
  author: CommentAuthor;
}

interface PostData {
  postDetail: PostDetail;
  comments: Comment[];
  likes: User[];
}

///

interface AlbumProps {
  spotifyAlbumId: string;
}

function AlbumPagePostTab({ spotifyAlbumId }: AlbumProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [albumPostList, setaAlbumPostList] = useState<PostData[]>([]);

  useEffect(() => {
    fetchPosts();
  }, []);

  const fetchPosts = async () => {
    const AlbumPostUrl = `/api/album/${spotifyAlbumId}/post?sorted=recent&page=0&limit=5`;
    if (!isLoading) {
      setIsLoading(true);
      const token = localStorage.getItem('login-token') as string;
      const refreshToken = localStorage.getItem('login-refreshToken') as string;
      await fetchGET(token, refreshToken, AlbumPostUrl).then(data => {
        setaAlbumPostList(data);
        setIsLoading(false);
      });
    }
  };

  return (
    <Container>
      {albumPostList && albumPostList.length > 0 ? (
        albumPostList?.reverse().map((post: any, index: number) => <AlbumPostCard key={index} albumPost={post}></AlbumPostCard>)
      ) : (
        <Text margin="40px 0px 0px 0px"> Post가 없습니다.</Text>
      )}
    </Container>
  );
}

export default AlbumPagePostTab;
