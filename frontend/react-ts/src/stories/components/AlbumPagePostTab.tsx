import styled from 'styled-components';
import { colors } from '../../styles/color';
import AlbumPostCard from './AlbumPostCard';
import { useEffect, useState } from 'react';

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

function AlbumPageChatTab({ spotifyAlbumId }: AlbumProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [albumPostList, setaAlbumPostList] = useState<PostData[]>([]);

  console.log('postlist');
  console.log(albumPostList);
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

  useEffect(() => {
    fetchSearch();
  }, []);
  let AlbumPostUrl = `${server}/api/album/${spotifyAlbumId}/post?sorted=recent&page=0&limit=5`;
  const fetchSearch = async () => {
    if (token && !isLoading) {
      setIsLoading(true);
      console.log('검색시작');
      Search(AlbumPostUrl);
    }
  };
  const Search = async (URL: string) => {
    console.log('fetching album chat list...');
    try {
      const response = await fetch(URL, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setaAlbumPostList(data);
        console.log(data);
      } else if (response.status === 401) {
        ReissueToken();
        fetchSearch();
      } else {
        console.error('Failed to fetch data:', response.status);
      }
    } catch (error) {
      console.error('Error fetching the JSON file:', error);
    } finally {
      setIsLoading(false);
      console.log('finished');
    }
  };

  const ReissueToken = async () => {
    console.log('reissuing Token');
    try {
      const response = await fetch(reissueTokenUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Refresh-Token': `${refreshToken}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('login-token', data.token);
        localStorage.setItem('login-refreshToken', data.refreshToken);
        setToken(data.token);
        setRefreshToken(data.refreshToken);
      } else {
        console.error('failed to reissue token', response.status);
      }
    } catch (error) {
      console.error('Refresh Token 재발급 실패', error);
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

export default AlbumPageChatTab;
