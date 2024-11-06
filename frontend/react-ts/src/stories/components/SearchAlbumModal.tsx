import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
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

const CenterAlign = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  line-height: 120%;
`;

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

interface BestAlbum {
  albumCover: string;
  albumId: number;
  albumName: string;
  score: number;
  spotifyId: string;
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
  bestAlbum: BestAlbum[] | undefined;
  setBestAlbum: React.Dispatch<React.SetStateAction<BestAlbum[] | undefined>>;
  favoriteArtist: FavoriteArtist | undefined;
  setFavoriteArtist: React.Dispatch<React.SetStateAction<FavoriteArtist | undefined>>;
  favoriteArtistSpotifyIds: FavoriteArtistSpotifyIds | undefined;
  setFavoriteArtistSpotifyIds: React.Dispatch<React.SetStateAction<FavoriteArtistSpotifyIds | undefined>>;
}

function SearchModal({
  searchingTopic,
  setIsSearchModalOpen,
  bestAlbum,
  setBestAlbum,
  favoriteArtist,
  setFavoriteArtist,
  favoriteArtistSpotifyIds,
  setFavoriteArtistSpotifyIds,
}: SearchAlbumModalProps) {
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchResultAlbum, setSearchResultAlbum] = useState<AlbumSearchResult[]>();
  const [isLoading, setIsLoading] = useState(false);

  let searchAlbumUrl = `/api/searchSpotify/album/${searchKeyword}`;
  let ArtistAlbumUrl = `/api/searchSpotify/artist/${favoriteArtistSpotifyIds?.spotifyArtistId}/album`;
  let searchArtistAlbumUrl = `/api/searchSpotify/artist/${favoriteArtistSpotifyIds?.spotifyArtistId}/album`;

  const fetchSearch = async (searchingMode: string) => {
    if (!isLoading) {
      setIsLoading(true);
      console.log('검색시작');
      if (searchingMode === 'Album') {
        console.log('검색중');
        Search(searchAlbumUrl);
      }
      if (searchingMode === 'Artist-album') {
        console.log('검색중');
        Search(searchArtistAlbumUrl);
      }
    }
  };

  // const SearchArtistAlbum = (searchKeyword) =>{
  //   // 해당 아티스트의 곡 중에서 유사 검색
  // }

  const Search = async (URL: string) => {
    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    fetchGET(token, refreshToken, URL, MAX_REISSUE_COUNT).then(data => {
      if (searchingTopic === 'Album') setSearchResultAlbum(data);
      else setSearchResultAlbum(data.filter((album: AlbumSearchResult) => album.albumArtist.artistId === favoriteArtist?.spotifyArtistId));
      setIsLoading(false);
    });
  };

  useEffect(() => {
    console.log(searchingTopic);
    console.log(favoriteArtist);
    if (searchingTopic === 'Artist-album') {
      setIsLoading(true);
      Search(ArtistAlbumUrl);
    }
  }, []);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    // 검색 결과 초기화
    fetchSearch('Album'); // 검색 실행
  };

  const addBestAlbum = (album: AlbumSearchResult) => {
    const addingAlbum: BestAlbum = {
      albumCover: album.imageUrl,
      albumId: 0,
      albumName: album.name,
      score: 0,
      spotifyId: album.albumId,
    };
    if (bestAlbum) {
      setBestAlbum([...bestAlbum, addingAlbum]);
    }
    setIsSearchModalOpen(false);
  };

  const addFavoriteArtistAlbum = (album: AlbumSearchResult) => {
    if (favoriteArtist && favoriteArtistSpotifyIds) {
      const addFavoriteAlbum: FavoriteArtist = {
        albumCover: album.imageUrl,
        albumName: album.name,
        artistCover: favoriteArtist.artistCover,
        artistName: favoriteArtist.artistName,
        trackCover: favoriteArtist.trackCover,
        trackName: favoriteArtist.trackName,
        spotifyArtistId: favoriteArtist.spotifyArtistId,
      };
      const addArtistId: FavoriteArtistSpotifyIds = {
        spotifyArtistId: favoriteArtistSpotifyIds?.spotifyArtistId,
        spotifyAlbumId: album.albumId,
        spotifyTrackId: favoriteArtistSpotifyIds?.spotifyTrackId,
      };
      setFavoriteArtistSpotifyIds(addArtistId);
      setFavoriteArtist(addFavoriteAlbum);
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
            <ContentInput placeholder="앨범의 제목을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>
      </SearchInputArea>

      <SearchResultArea>
        {!isLoading &&
          (searchingTopic === 'Album' ? (
            searchResultAlbum && searchResultAlbum.length > 0 ? (
              searchResultAlbum.map((album: any) => (
                <SongArea key={album.albumId} onClick={() => addBestAlbum(album)}>
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
              <CenterAlign>
                <Text fontFamily="SB" fontSize="20px" margin="10px" color={colors.Font_black}>
                  검색 결과가 없습니다.
                </Text>
                <Text fontFamily="RG" fontSize="15px" margin="10px" color={colors.Font_black}>
                  앨범의 제목을 올바르게 입력했는지 확인해주세요.
                </Text>
              </CenterAlign>
            )
          ) : searchResultAlbum && searchResultAlbum.length > 0 && searchingTopic === 'Artist-album' ? (
            searchResultAlbum.map((album: any) => (
              <SongArea key={album.albumId} onClick={() => addFavoriteArtistAlbum(album)}>
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
            <CenterAlign>
              <Text fontFamily="SB" fontSize="20px" margin="10px" color={colors.Font_black}>
                검색 결과가 없습니다.
              </Text>
              <Text fontFamily="RG" fontSize="15px" margin="10px" color={colors.Font_black}>
                앨범의 제목을 올바르게 입력했는지 확인해주세요.
              </Text>
            </CenterAlign>
          ))}
        {isLoading && <Loader></Loader>}
      </SearchResultArea>
    </Container>
  );
}

export default SearchModal;
