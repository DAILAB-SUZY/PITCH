import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import Loader from './Loader';

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

const AlbumPostArea = styled.div`
  width: 80vw;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
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
  font-size: 15px;
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
  font-family: 'Bd';
  color: ${colors.Font_black};
  width: 100%;
  height: 100%;
  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
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

interface ArtistSearchResult {
  artistId : string;
  imageUrl : string;
  name : string;
}

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
interface BestAlbum {
  albumCover: string;
  albumId: number;
  albumName: string;
  score: number;
  spotifyId: string;
}
interface favoriteArtist {
  artistName: string;
  albumName: string;
  trackName: string;
  artistCover: string;
  albumCover: string;
  trackCover: string;
}

interface SearchAlbumModalProps {
  isAlbumSearchOpen: boolean;
  searchingTopic: string;
  setIsAlbumSearchOpen: React.Dispatch<React.SetStateAction<boolean>>;
  bestAlbum: BestAlbum[] | undefined;
  setBestAlbum: React.Dispatch<React.SetStateAction<BestAlbum[] | undefined>>;
  favoriteArtist: favoriteArtist | undefined;
  setFavoriteArtist: React.Dispatch<React.SetStateAction<favoriteArtist | undefined>>;
}

function SearchModal({ isAlbumSearchOpen, searchingTopic, setIsAlbumSearchOpen, bestAlbum, setBestAlbum, favoriteArtist, setFavoriteArtist }: SearchAlbumModalProps) {
  
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchResultAlbum, setSearchResultAlbum] = useState<AlbumSearchResult[]>();
  const [searchResultArtist, setSearchResultArtist] = useState<ArtistSearchResult[]>();
  const [searchResultArtistAlbum, setSearchResultArtistAlbum] = useState<AlbumSearchResult[]>();
  const [searchResultArtistTrack, setSearchResultArtistTrack] = useState<TrackSearchResult[]>();
  const [isLoading, setIsLoading] = useState(false);

  const [searchedFavoriteArtist, setSearchedFavoriteArtist] = useState<favoriteArtist | null>();

  const navigate = useNavigate();

  const GoToAlbumPostEditPage = (album: SearchData | null = null) => {
    navigate('/AlbumPostEditPage', { state: album });
  };

  const server = 'http://203.255.81.70:8030';

  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const token = localStorage.getItem('login-token');
  const refreshToken = localStorage.getItem('login-refreshToken');
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

  let searchAlbumUrl = `${server}/api/searchSpotify/album/${searchKeyword}`;
  let searchArtistUrl = `${server}/api/searchSpotify/album/${searchKeyword}`;
  let searchArtistAlbumUrl = `${server}/api/searchSpotify/album/${searchKeyword}`;
  let searchArtistTrackUrl = `${server}/api/searchSpotify/album/${searchKeyword}`;
  const fetchSearch = async () => {
    if (token && !isLoading) {
      setIsLoading(true);
      if (searchingTopic === 'album') {
        Search(searchAlbumUrl);
      }
      if (searchingTopic === 'artist') {
        Search(searchArtistUrl);
      }
      if (searchingTopic === 'artist-album') {
        Search(searchArtistAlbumUrl);
      }
      if (searchingTopic === 'artist-track') {
        Search(searchArtistTrackUrl);
      }
    }
  };

  const Search = async (URL : string) => {
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
        setSearchResultAlbum(data);
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

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
     // 검색 결과 초기화
    fetchSearch(); // 검색 실행
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
    setIsAlbumSearchOpen(false);
  };

  const addFavoriteArtistArtist = (artist: ArtistSearchResult) =>{
    const addingFavoriteArtistArtist: 
  };

  return (
    <Container>
      <SearchInputArea>
        <ButtonArea>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black} onClick={() => setIsAlbumSearchOpen(false)}>
            취소
          </Text>
          <Text fontFamily="Bd" fontSize="20px" margin="0px" color={colors.Font_black}>
            {searchingTopic} search
          </Text>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 10px 0px 0px" color={colors.BG_grey}>
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
          searchResult &&
            (searchingTopic === "album" ?
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
            : searchingTopic === "artist" ? 
              searchResult.map((artist: ArtistSearchResult) => (
                <SongArea key={artist.artistId} onClick={() => addFavoriteArtistArtist(artist)}>
                  <AlbumCover>
                    <img src={artist.imageUrl} width="100%" height="100%"></img>
                  </AlbumCover>
                  <SongTextArea>
                    <Title fontSize={'25px'}>{artist.name}</Title>
                  </SongTextArea>
                </SongArea>
              )) 
            : searchingTopic === "artist-album" ? 
            searchResult.map((album: any) => (
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
            : null
            )
        }
        {isLoading && <Loader></Loader>}
      </SearchResultArea>
    </Container>
  );
}

export default SearchModal;
