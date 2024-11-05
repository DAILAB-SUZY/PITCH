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
  fontFamily?: string;
  fontSize?: string;
  color?: string;
  margin?: string;
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
interface TrackSearchResult {
  trackArtist: {
    artistId: string;
    imageUrl: string;
    name: string;
  };
  album: {
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
  };
  trackId: string;
  trackName: string;
  duration: string;
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

interface track {
  albumId: number;
  artistName: string;
  spotifyId: string;
  title: string;
  trackCover: string;
  trackId: number;
  trackOrder: number;
}

interface recommend {
  trackId: number;
  title: string;
  artistName: string;
  albumId: number;
  trackCover: string;
}

interface PlayListData {
  tracks: track[];
  recommends: recommend[];
}

interface SearchTrackModalProps {
  searchingTopic?: string; // optional
  setIsSearchModalOpen: React.Dispatch<React.SetStateAction<boolean>>; // optional
  favoriteArtist?: FavoriteArtist; // optional
  setFavoriteArtist?: React.Dispatch<React.SetStateAction<FavoriteArtist | undefined>>; // optional
  favoriteArtistSpotifyIds?: FavoriteArtistSpotifyIds; // optional
  setFavoriteArtistSpotifyIds?: React.Dispatch<React.SetStateAction<FavoriteArtistSpotifyIds | undefined>>; // optional
  tracks?: track[];
  setPlayListData?: React.Dispatch<React.SetStateAction<PlayListData | undefined>>;
}

function SearchTrackModal(props: SearchTrackModalProps) {
  const { searchingTopic, setIsSearchModalOpen, favoriteArtist, setFavoriteArtist, favoriteArtistSpotifyIds, setFavoriteArtistSpotifyIds, setPlayListData } = props || {};
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchResultTrack, setSearchResultTrack] = useState<TrackSearchResult[]>();
  const [isLoading, setIsLoading] = useState(false);

  const handleTrackUpdate = (newTrack: TrackSearchResult) => {
    console.log(newTrack);
    if (setPlayListData) {
      setPlayListData(prevData => {
        if (!prevData) {
          // prevData가 undefined일 경우 초기값으로 설정
          return {
            tracks: [],
            recommends: [], // 추천도 빈 배열로 초기화
          };
        }

        // newSong 객체 생성
        const newSong = {
          albumId: 0, // 기본값 설정
          artistName: newTrack.trackArtist.name || 'Unknown Artist', // artistName이 없을 경우 기본값 설정
          spotifyId: newTrack.trackId, // 예시로 trackId 사용
          title: newTrack.trackName || 'Unknown Title', // title이 없을 경우 기본값 설정
          trackCover: newTrack.album.imageUrl || '', // trackCover가 없을 경우 기본값 설정
          trackId: 0, // trackId
          trackOrder: prevData.tracks.length + 1, // 새 트랙의 순서 설정
        };

        // 기존 트랙 배열에 새로운 트랙 추가
        const updatedTracks = [...prevData.tracks, newSong];
        console.log(updatedTracks);
        return {
          ...prevData,
          tracks: updatedTracks,
          recommends: prevData.recommends || [], // 추천이 없을 경우 빈 배열로 설정
        };
      });
    }
  };

  const fetchSearch = async (searchingMode: string) => {
    const searchTrackUrl = `/api/searchSpotify/track/${searchKeyword}`;
    const searchArtistTrackUrl = `/api/searchSpotify/artist/${favoriteArtistSpotifyIds?.spotifyArtistId}/track`;
    if (!isLoading) {
      setIsLoading(true);
      console.log('검색시작');
      if (searchingMode === 'track') {
        console.log('검색중');
        Search(searchTrackUrl);
      }
      if (searchingMode === 'Artist-track') {
        console.log('검색중');
        Search(searchArtistTrackUrl);
      }
    }
  };

  const Search = async (URL: string) => {
    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    fetchGET(token, refreshToken, URL, MAX_REISSUE_COUNT).then(data => {
      if (searchingTopic === 'track') setSearchResultTrack(data);
      else setSearchResultTrack(data.filter((track: TrackSearchResult) => track.trackArtist.artistId === favoriteArtistSpotifyIds?.spotifyArtistId));
      setIsLoading(false);
    });
  };

  useEffect(() => {
    console.log(searchingTopic);
    console.log(favoriteArtist);
    const searchArtistTrackUrl = `/api/searchSpotify/artist/${favoriteArtistSpotifyIds?.spotifyArtistId}/track`;
    if (searchingTopic === 'Artist-track') {
      setIsLoading(true);
      Search(searchArtistTrackUrl);
    }
  }, []);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    // 검색 결과 초기화)
    fetchSearch('track'); // 검색 실행
  };

  const addFavoriteArtistTrack = (track: TrackSearchResult) => {
    if (favoriteArtist && favoriteArtistSpotifyIds && setFavoriteArtistSpotifyIds && setFavoriteArtist) {
      const addFavoriteTrack: FavoriteArtist = {
        albumCover: favoriteArtist.albumCover,
        albumName: favoriteArtist.albumName,
        artistCover: favoriteArtist.artistCover,
        artistName: favoriteArtist.artistName,
        trackCover: track.album.imageUrl,
        trackName: track.trackName,
        spotifyArtistId: favoriteArtist.spotifyArtistId,
      };
      const addArtistId: FavoriteArtistSpotifyIds = {
        spotifyArtistId: favoriteArtistSpotifyIds?.spotifyArtistId,
        spotifyAlbumId: favoriteArtistSpotifyIds.spotifyAlbumId,
        spotifyTrackId: track.trackId,
      };
      setFavoriteArtistSpotifyIds(addArtistId);
      setFavoriteArtist(addFavoriteTrack);
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
            <ContentInput placeholder="노래의 제목을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>
      </SearchInputArea>

      <SearchResultArea>
        {!isLoading && searchResultTrack && searchingTopic === 'track'
          ? searchResultTrack.map((track: any) => (
              <SongArea
                key={track.albumId}
                onClick={() => {
                  handleTrackUpdate(track);
                  setIsSearchModalOpen(false);
                }}
              >
                <AlbumCover>
                  <img src={track.album.imageUrl} width="100%" height="100%"></img>
                </AlbumCover>
                <SongTextArea>
                  <Title fontSize={'20px'}>{track.trackName}</Title>
                  <Title fontSize={'15px'}>{track.trackArtist.name}</Title>
                </SongTextArea>
              </SongArea>
            ))
          : null}
        {!isLoading && searchResultTrack && searchingTopic === 'Artist-track' ? (
          searchResultTrack.length === 0 ? (
            <Text fontSize="20px">검색결과가 없습니다.</Text>
          ) : (
            searchResultTrack.map((track: TrackSearchResult) => (
              <SongArea
                key={track.trackName}
                onClick={() => {
                  addFavoriteArtistTrack(track);
                  setIsSearchModalOpen(false);
                }}
              >
                <AlbumCover>
                  <img src={track.album.imageUrl} width="100%" height="100%"></img>
                </AlbumCover>
                <SongTextArea>
                  <Title fontSize={'20px'}>{track.trackName}</Title>
                  <Title fontSize={'15px'}>{track.trackArtist.name}</Title>
                </SongTextArea>
              </SongArea>
            ))
          )
        ) : null}
        {isLoading && <Loader></Loader>}
      </SearchResultArea>
    </Container>
  );
}

export default SearchTrackModal;
