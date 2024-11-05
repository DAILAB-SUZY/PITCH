import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import Loader from '../components/Loader';
import { MAX_REISSUE_COUNT, fetchGET } from '../utils/fetchData';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh; //auto;
  width: 100vw;
  background-color: ${colors.BG_grey};
  color: ${colors.Font_black};
`;

const AlbumPostArea = styled.div`
  width: 100vw;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  color: string;
  margin: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
  margin: ${props => props.margin};
  line-height: 120%;
`;

const ButtonArea = styled.div`
  width: 100vw;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  margin: 0 10 0 10px;
  z-index: 10;
  background-color: ${colors.BG_grey};
`;

const Line = styled.div`
  width: 95vw;
  height: 1px;
  background-color: ${colors.Button_deactive};
`;

const SearchArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 40px;
  margin: 10px;
  background-color: ${colors.BG_grey};
  overflow: scroll;
`;

const ContentInput = styled.input`
  width: 90vw;
  height: 35px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.InputBox};
  font-size: 16px;
  border: 0;
  border-radius: 7px;
  outline: none;
  color: ${colors.Font_black};

  &::placeholder {
    opacity: 0.7;
  }
`;

const SearchResultArea = styled.div`
  display: flex;
  width: 100vw;
  height: 600px;
  /* overflow: hidden; */
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  margin: 0px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
  z-index: 10;
`;

const SongArea = styled.div`
  width: 100%;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: start;
  flex-direction: row;
  margin: 10px 0px 10px 0px;
`;

const AlbumCover = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 8px;
  background-color: black;
  margin: 10px;
  overflow: hidden;
`;

const SongTextArea = styled.div`
  height: 80%;
  /* width: 100%; */
  width: 300px;
  display: flex;
  align-items: start;
  justify-content: space-between;
  flex-direction: column;
  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
`;

const Title = styled.div<{ fontSize?: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'EB';
  color: ${colors.Font_black};
  width: 100%;
  height: 100%;
  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
`;

interface SearchResult {
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

interface SearchData {
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
  postId: number;
}

function SearchPage() {
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchResult, setSearchResult] = useState<SearchResult[]>();
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();

  const GoToAlbumPostEditPage = (album: SearchData | null = null) => {
    navigate('/AlbumPostEditPage', { state: album });
  };

  const fetchSearch = async (token: string, refreshToken: string) => {
    let searchUrl = `/api/searchSpotify/album/${searchKeyword}`;

    if (token && !isLoading) {
      setIsLoading(true);
      fetchGET(token, refreshToken, searchUrl, MAX_REISSUE_COUNT).then(data => {
        setSearchResult(data);
        setIsLoading(false);
      });
    }
  };

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    setSearchResult([]); // 검색 결과 초기화
    fetchSearch(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || ''); // 검색 실행
  };

  return (
    <Container>
      <AlbumPostArea>
        <ButtonArea>
          <Text fontFamily="RG" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black} onClick={() => GoToAlbumPostEditPage()}>
            취소
          </Text>
          <Text fontFamily="EB" fontSize="23px" margin="0px" color={colors.Font_black}>
            Search
          </Text>
          <Text fontFamily="RG" fontSize="15px" margin="0px 10px 0px 0px" color={colors.BG_grey}>
            저장
          </Text>
        </ButtonArea>
        <Line></Line>
        <SearchArea>
          <form onSubmit={handleSearchSubmit}>
            <ContentInput placeholder="앨범의 제목을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>

        <SearchResultArea>
          {!isLoading && searchResult?.length && searchResult.length > 0 ? (
            searchResult.map((album: any) => (
              <SongArea key={album.albumId} onClick={() => GoToAlbumPostEditPage({ ...album, postId: null })}>
                <AlbumCover>
                  <img src={album.imageUrl} width="100%" height="100%"></img>
                </AlbumCover>
                <SongTextArea>
                  <Title fontSize={'20px'}>{album.name}</Title>
                  <Title fontSize={'15px'}>{album.albumArtist.name}</Title>
                </SongTextArea>
              </SongArea>
            ))
          ) : (
            <>
              <Text fontFamily="SB" fontSize="20px" margin="10px" color={colors.Font_black}>
                검색 결과가 없습니다.
              </Text>
              <Text fontFamily="RG" fontSize="15px" margin="10px" color={colors.Font_black}>
                앨범의 제목을 올바르게 입력했는지 확인해주세요.
              </Text>
            </>
          )}
          {isLoading && <Loader></Loader>}
        </SearchResultArea>
      </AlbumPostArea>
    </Container>
  );
}

export default SearchPage;
