import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useState } from 'react';
import Loader from './Loader';
import { fetchGET, MAX_REISSUE_COUNT } from '../utils/fetchData';

const Container = styled.div`
  /* position: absolute;
  top: 20px; */
  position: relative;
  z-index: 100;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 600px; //auto;
  width: 350px;
  border-radius: 10px;
  background-color: ${colors.BG_grey};
  color: ${colors.Font_black};
  box-shadow: 0 0px 15px rgba(0, 0, 0, 0.3);
`;

const SearchInputArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 350px;
  height: auto;
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
`;

const ButtonArea = styled.div`
  width: 330px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  margin: 0 10 0 10px;
  /* z-index: 10; */
  background-color: ${colors.BG_grey};
`;

const Line = styled.div`
  width: 320px;
  height: 1px;
  background-color: ${colors.Button_deactive};
`;

const SearchArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 320px;
  height: 40px;
  margin: 10px;
  background-color: ${colors.BG_grey};
  overflow: hidden;
  border-radius: 5px;
`;

const ContentInput = styled.input`
  width: 320px;
  height: 40px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.InputBox};
  font-size: 16px;
  border: 0;
  //border-radius: 15px;
  outline: none;
  color: ${colors.Font_black};

  &::placeholder {
    opacity: 0.7;
  }
`;

const SearchResultArea = styled.div`
  display: flex;
  width: 100%;
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
  width: 260px;
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

interface ArtistSearchResult {
  artistId: string;
  imageUrl: string;
  name: string;
}

interface FavoriteArtist {
  albumCover: string;
  albumName: string;
  artistCover: string;
  artistName: string;
  trackCover: string;
  trackName: string;
  spotifyArtistId: string;
}
interface FavoriteArtistSpotifyIds {
  spotifyArtistId: string;
  spotifyAlbumId: string;
  spotifyTrackId: string;
}

interface SearchAlbumModalProps {
  searchingTopic: string;
  setIsSearchModalOpen: React.Dispatch<React.SetStateAction<boolean>>;
  favoriteArtist: FavoriteArtist | undefined;
  setFavoriteArtist: React.Dispatch<React.SetStateAction<FavoriteArtist | undefined>>;
  favoriteArtistSpotifyIds: FavoriteArtistSpotifyIds | undefined;
  setFavoriteArtistSpotifyIds: React.Dispatch<React.SetStateAction<FavoriteArtistSpotifyIds | undefined>>;
}

function SearchArtistModal({ searchingTopic, setIsSearchModalOpen, favoriteArtist, setFavoriteArtist, setFavoriteArtistSpotifyIds }: SearchAlbumModalProps) {
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchResultArtist, setSearchResultArtist] = useState<ArtistSearchResult[]>();
  const [isLoading, setIsLoading] = useState(false);

  let searchArtistUrl = `/api/searchSpotify/artist/${searchKeyword}`;
  const fetchSearch = async () => {
    if (!isLoading) {
      setIsLoading(true);
      if (searchingTopic === 'Artist') {
        Search(searchArtistUrl);
      }
    }
  };
  const Search = async (URL: string) => {
    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    fetchGET(token, refreshToken, URL, MAX_REISSUE_COUNT).then(data => {
      setSearchResultArtist(data);
      setIsLoading(false);
    });
  };

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    // 검색 결과 초기화
    fetchSearch(); // 검색 실행
  };

  const addFavoriteArtist = (artist: ArtistSearchResult) => {
    if (favoriteArtist) {
      const addingArtist: FavoriteArtist = {
        albumCover: '',
        albumName: '',
        artistCover: artist.imageUrl,
        artistName: artist.name,
        trackCover: '',
        trackName: '',
        spotifyArtistId: artist.artistId,
      };
      const addArtistId: FavoriteArtistSpotifyIds = {
        spotifyArtistId: artist.artistId,
        spotifyAlbumId: '',
        spotifyTrackId: '',
      };
      setFavoriteArtist(addingArtist);
      setFavoriteArtistSpotifyIds(addArtistId);
    }
    setIsSearchModalOpen(false);
  };

  return (
    <Container>
      <SearchInputArea>
        <ButtonArea>
          <Text fontFamily="RG" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black} onClick={() => setIsSearchModalOpen(false)}>
            취소
          </Text>
          <Text fontFamily="EB" fontSize="20px" margin="0px" color={colors.Font_black}>
            {searchingTopic} search
          </Text>
          <Text fontFamily="RG" fontSize="15px" margin="0px 10px 0px 0px" color={colors.BG_grey}>
            저장
          </Text>
        </ButtonArea>
        <Line></Line>
        <SearchArea>
          <form onSubmit={handleSearchSubmit}>
            <ContentInput placeholder="아티스트의 이름을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>
      </SearchInputArea>

      <SearchResultArea>
        {!isLoading && searchResultArtist
          ? searchResultArtist.map((artist: ArtistSearchResult) => (
              <SongArea key={artist.artistId} onClick={() => addFavoriteArtist(artist)}>
                <AlbumCover>
                  <img src={artist.imageUrl} width="100%" height="100%"></img>
                </AlbumCover>
                <SongTextArea>
                  <Title fontSize={'20px'}>{artist.name}</Title>
                </SongTextArea>
              </SongArea>
            ))
          : null}
        {isLoading && <Loader></Loader>}
      </SearchResultArea>
    </Container>
  );
}

export default SearchArtistModal;
