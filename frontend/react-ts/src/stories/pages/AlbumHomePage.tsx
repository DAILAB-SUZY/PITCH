import styled from 'styled-components';
import { colors } from '../../styles/color';

import MostCommentedCard from '../components/MostCommentedCard';
import MostLikedCard from '../components/MostLikedCard';
import { useNavigate } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import Loader from '../components/Loader';
import { fetchGET, MAX_REISSUE_COUNT } from '../utils/fetchData';

import Header from '../components/Header';

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

const Body = styled.div`
  margin-top: 50px;
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
  font-size: 16px;
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
  width: 380px;
  padding: 10px;
`;

const Text = styled.div<{ fontFamily?: string; fontSize?: string; color?: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
  margin: ${props => props.margin};
  line-height: 120%;
`;

const Title = styled.div<{ fontSize: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'EB';
`;

const CenterAlign = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  line-height: 120%;
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
  genre: string | null;
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
    fetchMostCommentedData(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
    //fetchMostLikedData();
  }, []);

  let AlbumSearchUrl = `/api/searchSpotify/album/${searchKeyword}`;

  const MostCommentedDataUrl = `/api/album/albumchat/chat`;
  const fetchMostCommentedData = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, MostCommentedDataUrl, MAX_REISSUE_COUNT).then(data => {
      setMostCommentedAlbumList(data);
    });
  };

  const MostLikedDataUrl = `/api/album/albumchat/like?page=${pageNumber}&limit=6`;
  const fetchMostLikedData = async (token: string, refreshToken: string) => {
    if (!isEnd && !isLoading) {
      setIsLoading(true);
      await fetchGET(token, refreshToken, MostLikedDataUrl, MAX_REISSUE_COUNT).then(data => {
        if (data.length === 0) {
          console.log('list End');
          setIsEnd(true);
        }
        setMostLikedAlbumList(prevList => [...prevList, ...data]);
        setPageNumber(prevPage => prevPage + 1); // 페이지 증가
        setIsLoading(false);
      });
    }
  };

  // 검색 실행 함수
  const fetchAlbum = async () => {
    if (!isLoading) {
      setIsLoading(true);
      console.log('검색시작');
      fetchData(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
    }
  };
  // 검색 결과 가져오기
  const fetchData = async (token: string, refreshToken: string) => {
    await fetchGET(token, refreshToken, AlbumSearchUrl, MAX_REISSUE_COUNT).then(data => {
      setSearchResultTrack(data);
      setIsSearchMode(true);
    });
    setIsLoading(false);
  };

  // Intersection Observer용 ref
  const observerRef = useRef<HTMLDivElement | null>(null);

  // Intersection Observer 설정
  useEffect(() => {
    const observer = new IntersectionObserver(entries => {
      // observerRef가 화면에 보이면 fetch 호출
      if (!isLoading && entries[0].isIntersecting) {
        fetchMostLikedData(localStorage.getItem('login-token') as string, localStorage.getItem('login-refreshToken') as string);
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
      <Header page={3}></Header>
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
        {!isLoading && isSearchMode && (
          <MostLikedArea>
            <AlbumListArea>
              {searchResultTrack && searchResultTrack.length > 0 ? (
                searchResultTrack?.map(album => <MostLikedCard album={album}></MostLikedCard>) ?? <p>No albums found</p>
              ) : (
                <CenterAlign>
                  <Text fontFamily="SB" fontSize="20px" margin="10px" color={colors.Font_black}>
                    검색 결과가 없습니다.
                  </Text>
                  <Text fontFamily="RG" fontSize="15px" margin="10px" color={colors.Font_black}>
                    앨범의 제목을 올바르게 입력했는지 확인해주세요.
                  </Text>
                </CenterAlign>
              )}
            </AlbumListArea>
          </MostLikedArea>
        )}
        {isLoading && <Loader></Loader>}
      </Body>
    </Container>
  );
}

export default AlbumHomePage;
