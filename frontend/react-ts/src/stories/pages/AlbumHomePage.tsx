import styled from 'styled-components';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import MostCommentedCard from '../components/MostCommentedCard';
import MostLikedCard from '../components/MostLikedCard';
import { useNavigate } from 'react-router-dom';
import cover1 from '../../img/aespa2.jpg';
import artistProfile from '../../img/aespaProfile.jpg';
import { useEffect, useState } from 'react';
import Loader from '../components/Loader';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh;
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
  margin-top: 120px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;
const SearchArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 370px;
  height: 40px;
  margin: 10px;
  background-color: ${colors.BG_lightgrey};
  overflow: hidden;
  border-radius: 10px;
`;

const ContentInput = styled.input`
  width: 370px;
  height: 40px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
  font-size: 15px;
  border: 0;
  //border-radius: 15px;
  outline: none;
  color: ${colors.Font_black};

  &::placeholder {
    opacity: 0.7;
  }
`;

const MostCommentedArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
`;

const TitleArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  height: 40px;
  margin: 10px 0px 10px 30px;
`;

const CardListArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  overflow-x: scroll;
  box-sizing: border-box;
  width: 100vw;
  padding: 10px;
`;

const MostLikedArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 20px;
`;

const AlbumListArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  flex-wrap: wrap;
  align-items: center;
  overflow-x: scroll;
  box-sizing: border-box;
  width: 100vw;
  padding: 10px;
`;
const Album = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  width: 140px;
  height: 180px;
  margin: 10px;
`;
const AlbumCard = styled.div`
  position: relative;
  display: flex;
  width: 140px;
  height: 140px;
  border-radius: 8px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
  overflow: hidden;
`;

const AlbumInfo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
`;

const AlbumImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 140px;
  height: 140px;
  object-fit: cover;
  z-index: 1;
`;

const AlbumGradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 140px;
  height: 140px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(0deg, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 0) 70%);
  backdrop-filter: blur(0px);
`;

const AlbumContentArea = styled.div`
  width: 140px;
  height: 140px;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-end;
  padding: 10px;
  box-sizing: border-box;
  z-index: 3;
`;
const Line = styled.div`
  width: 90vw;
  border-bottom: 1px;
`;

const Title = styled.div<{ fontSize: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Bd';
`;
const Text = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string; opacity?: number; color?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
  opacity: ${props => props.opacity};
  color: ${props => props.color};
`;

interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
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
  createAt: string; // ISO date string
  updateAt: string; // ISO date string
  likes: User[];
  comments: AlbumChatComment[]; // Recursive structure
  author: User;
}

interface AlbumLike {
  id: number;
  username: string;
  profilePicture: string;
  dnas: DNA[];
}

interface AlbumData {
  albumDetail: AlbumDetail;
  comments: AlbumChatComment[];
  albumLike: AlbumLike[];
}

interface AlbumSearchResult {
  albumArtist: {
    artistId: string;
    imageUrl: string;
    name: string;
  };
  albumId: string;
  imageUrl: string;
  name: string;
  total_tracks: number;
  release_date: string;
}

function AlbumHomePage() {
  const navigate = useNavigate();
  const [MostCommentedAlbumList, setMostCommentedAlbumList] = useState<AlbumData[] | undefined>();
  const [MostLikedAlbumList, setMostLikedAlbumList] = useState<AlbumData[] | undefined>();
  const [searchResultTrack, setSearchResultTrack] = useState<AlbumSearchResult[]>();
  const [isSearchMode, setIsSearchMode] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState('');

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    // 검색 결과 초기화
    fetchSearch(); // 검색 실행
  };

  useEffect(() => {
    fetchMostCommentedData();
    fetchMostLikedData();
  }, []);

  const server = 'http://203.255.81.70:8030';
  let MostCommentedDataUrl = `${server}/api/albumchat/chat`;
  let MostLikedDataUrl = `${server}/api/albumchat/like?page=0&limit=5`;
  let AlbumSearchUrl = `${server}/api/searchSpotify/album/${searchKeyword}`;
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const token = localStorage.getItem('login-token');
  const refreshToken = localStorage.getItem('login-refreshToken');

  const fetchMostCommentedData = async () => {
    if (token) {
      try {
        console.log('fetching Most Commented Albums...');
        const response = await fetch(MostCommentedDataUrl, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log('set data');
          const data = await response.json();
          console.log(data);
          setMostCommentedAlbumList(data);
        } else if (response.status === 401) {
          console.log('reissuing Token');
          const reissueToken = await fetch(reissueTokenUrl, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Refresh-Token': `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem('login-token', data.token);
          localStorage.setItem('login-refreshToken', data.refreshToken);
          fetchMostCommentedData();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
      }
    }
  };

  const fetchMostLikedData = async () => {
    if (token) {
      try {
        console.log('fetching Most Liked Albums...');
        const response = await fetch(MostLikedDataUrl, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log('set data');
          const data = await response.json();
          console.log(data);
          setMostLikedAlbumList(data);
        } else if (response.status === 401) {
          console.log('reissuing Token');
          const reissueToken = await fetch(reissueTokenUrl, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Refresh-Token': `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem('login-token', data.token);
          localStorage.setItem('login-refreshToken', data.refreshToken);
          fetchMostLikedData();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
      }
    }
  };

  const fetchSearch = async () => {
    if (token && !isLoading) {
      setIsLoading(true);
      console.log('검색시작');
      Search(AlbumSearchUrl);
    }
  };
  const Search = async (URL: string) => {
    console.log('searching...');
    try {
      console.log(`searching Album : ${searchKeyword}...`);
      const response = await fetch(URL, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setSearchResultTrack(data);
        console.log(data);
        setIsSearchMode(true);
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
    const reissueToken = await fetch(reissueTokenUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Refresh-Token': `${refreshToken}`,
      },
    });
    const data = await reissueToken.json();
    localStorage.setItem('login-token', data.token);
    localStorage.setItem('login-refreshToken', data.refreshToken);
  };

  return (
    <Container>
      <Header>
        <Nav page={3}></Nav>
      </Header>
      <Body>
        <SearchArea>
          <form onSubmit={handleSearchSubmit}>
            <ContentInput placeholder="앨범의 제목을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>

        {!isSearchMode && !isLoading && (
          <>
            <MostCommentedArea>
              <TitleArea>
                <Title fontSize="30px" margin="0px">
                  Most Commented
                </Title>
              </TitleArea>
              <CardListArea>{MostCommentedAlbumList?.map((album, index) => <MostCommentedCard key={index} album={album}></MostCommentedCard>) ?? <p>No albums found</p>}</CardListArea>
            </MostCommentedArea>
            <MostLikedArea>
              <TitleArea>
                <Title fontSize="30px" margin="0px">
                  Most Liked
                </Title>
              </TitleArea>
              <AlbumListArea>{MostLikedAlbumList?.map((album, index) => <MostLikedCard key={index} album={album}></MostLikedCard>) ?? <p>No albums found</p>}</AlbumListArea>
            </MostLikedArea>
          </>
        )}
        {isLoading ? <Loader></Loader> : isSearchMode ? <>wow</> : null}
      </Body>
    </Container>
  );
}

export default AlbumHomePage;
