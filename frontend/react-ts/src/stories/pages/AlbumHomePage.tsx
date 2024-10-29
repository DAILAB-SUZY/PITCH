import styled from 'styled-components';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import MostCommentedCard from '../components/MostCommentedCard';
import MostLikedCard from '../components/MostLikedCard';
import { useNavigate } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
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

const Title = styled.div<{ fontSize: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Bd';
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

interface AlbumLike extends User {} // User와 동일한 구조 확장

interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
  likes: AlbumLike[];
}

interface CommentAuthor extends User {} // User 구조 확장

interface AlbumChatComment {
  albumChatCommentId: number;
  content: string;
  createAt: string; // ISO 날짜 형식
  updateAt: string; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: CommentAuthor;
}

interface AlbumData {
  albumDetail: AlbumDetail;
  comments: AlbumChatComment[];
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
  const GoToAlbumPage = (spotifyAlbumId: string) => {
    navigate('/AlbumPage', { state: spotifyAlbumId });
  };
  const [MostCommentedAlbumList, setMostCommentedAlbumList] = useState<AlbumData[]>([]);
  const [MostLikedAlbumList, setMostLikedAlbumList] = useState<AlbumData[]>([]);
  const [searchResultTrack, setSearchResultTrack] = useState<AlbumSearchResult[]>([]);
  const [isSearchMode, setIsSearchMode] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [pageNumber, setPageNumber] = useState(0);
  const [isEnd, setIsEnd] = useState(false);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    // 검색 결과 초기화
    setIsSearchMode(true);
    setSearchResultTrack([]);
    fetchAlbum(); // 검색 실행
  };

  useEffect(() => {
    fetchMostCommentedData();
    //fetchMostLikedData();
  }, []);

  const server = 'http://203.255.81.70:8030';
  let MostCommentedDataUrl = `${server}/api/album/albumchat/chat`;

  let AlbumSearchUrl = `${server}/api/searchSpotify/album/${searchKeyword}`;
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

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
          ReissueToken();
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
    const MostLikedDataUrl = `${server}/api/album/albumchat/like?page=${pageNumber}&limit=6`;
    if (token && !isLoading && !isEnd) {
      setIsLoading(true);
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
          if (data.length === 0) {
            console.log('list End');
            setIsEnd(true);
          }

          setMostLikedAlbumList(prevList => [...prevList, ...data]);
          setPageNumber(prevPage => prevPage + 1); // 페이지 증가

          console.log('postPage: ', pageNumber);
          console.log(MostCommentedAlbumList);
        } else if (response.status === 401) {
          ReissueToken();
          fetchMostLikedData();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
        setIsLoading(false);
      }
    }
  };

  const fetchAlbum = async () => {
    if (token && !isLoading) {
      setIsLoading(true);
      console.log('검색시작');
      fetchData(AlbumSearchUrl);
    }
  };

  const fetchData = async (URL: string) => {
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
        fetchAlbum();
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

  // Intersection Observer용 ref
  const observerRef = useRef<HTMLDivElement | null>(null);

  // Intersection Observer 설정
  useEffect(() => {
    const observer = new IntersectionObserver(entries => {
      // observerRef가 화면에 보이면 fetch 호출
      if (!isLoading && entries[0].isIntersecting) {
        // setPageNumber(prevPage => prevPage + 1); // 페이지 증가
        // console.log('page++ => ', pageNumber);
        fetchMostLikedData();
      }
    });

    if (observerRef.current) {
      //console.log('current: ', observerRef.current);
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
      <Header>
        <Nav page={3}></Nav>
      </Header>
      <Body>
        <SearchArea>
          <form onSubmit={handleSearchSubmit}>
            <ContentInput placeholder="앨범의 제목을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>

        {!isSearchMode && (
          <>
            <MostCommentedArea>
              <TitleArea>
                <Title fontSize="30px" margin="0px">
                  Most Commented
                </Title>
              </TitleArea>
              <CardListArea>
                {MostCommentedAlbumList?.map(album => (
                  <div
                    onClick={() => {
                      GoToAlbumPage(album.albumDetail.spotifyId);
                    }}
                  >
                    <MostCommentedCard album={album}></MostCommentedCard>
                  </div>
                )) ?? <p>No albums found</p>}
              </CardListArea>
            </MostCommentedArea>
            <MostLikedArea>
              <TitleArea>
                <Title fontSize="30px" margin="0px">
                  Most Liked
                </Title>
              </TitleArea>
              <AlbumListArea>{MostLikedAlbumList?.map(album => <MostLikedCard album={album}></MostLikedCard>) ?? <p>No albums found</p>}</AlbumListArea>
            </MostLikedArea>
          </>
        )}
        {!isLoading && !isSearchMode && <div ref={observerRef} style={{ height: '100px', backgroundColor: 'transparent' }} />}
        {isSearchMode && (
          <MostLikedArea>
            <AlbumListArea>{searchResultTrack?.map(album => <MostLikedCard album={album}></MostLikedCard>) ?? <p>No albums found</p>}</AlbumListArea>
          </MostLikedArea>
        )}
        {isLoading && <Loader></Loader>}
      </Body>
    </Container>
  );
}

export default AlbumHomePage;
