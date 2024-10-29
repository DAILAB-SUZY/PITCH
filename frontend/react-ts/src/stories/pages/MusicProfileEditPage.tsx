import styled from 'styled-components';
import { SetStateAction, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import PlaylistCardMini from '../components/PlaylistCardMini';
import AlbumGridEdit from '../components/AlbumGridEdit';
import FavoriteArtistEditCard from '../components/FavoriteArtistEditCard';
import SearchAlbumModal from '../components/SearchAlbumModal';
import SearchArtistModal from '../components/SearchArtistModal';
import SearchTrackModal from '../components/SearchTrackModal';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh;
  width: 100vw;
  overflow-x: hidden;
  background-color: white;
  color: black;
`;

const Header = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Body = styled.div`
  margin-top: 110px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const Blur = styled.div`
  display: flex;
  position: absolute;
  width: 100vw;
  height: 100vh;
  z-index: 90;
  background-color: rgba(30, 30, 30, 0.5);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
`;

const ModalArea = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 100vw;
  height: 100vh;
`;
const Line = styled.div`
  width: 95vw;
  height: 1px;
  background-color: ${colors.Button_deactive};
`;

const PageTitle = styled.div`
  font-size: 30px;
  font-family: 'Bd';
  padding: 20px 0px 10px 0px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: 90vw;
  gap: 2px;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  margin: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  margin: ${props => props.margin};
`;

const EditBtn = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-evenly;
  background-color: ${colors.BG_grey};
  width: 70px;
  height: 30px;
  border-radius: 15px;
  padding: 10px;
  box-sizing: border-box;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

const MusicDnaArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
  padding: 20px;
`;

const ProfileTagArea = styled.div`
  padding: 20px;
  box-sizing: border-box;
  width: 100%;
  height: 100%;
`;

const Tag = styled.div<{ opacity: string }>`
  margin: 0px 10px 12px 0px;
  padding: 5px 12px;
  width: auto;
  height: auto;
  font-size: 15px;
  font-family: 'Rg';
  color: black;
  background-color: ${colors.BG_grey};
  border-radius: 20px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  opacity: ${props => props.opacity};

  /* &:hover {
    background-color: ${colors.Main_Pink};
    color: white;
  } */
`;

const BestAlbumArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
`;

const TitleArea = styled.div<{ margin?: string }>`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  padding-left: 20px;
  margin: ${props => props.margin};
`;

const Title = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
  width: auto;

  font-family: 'Bd';
  font-size: 22px;

  margin-right: 10px;
`;

const FavoriteArtistArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
`;

const PlaylistCardArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: auto;
  margin-bottom: 35px;
`;

const BtnArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 50px;
  margin-bottom: 100px;
`;

const Btn = styled.div<{ bgcolor: string }>`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-evenly;
  background-color: ${props => props.bgcolor};
  width: 100px;
  height: 35px;
  border-radius: 10px;
  padding: 10px;
  box-sizing: border-box;
  margin: 0px 20px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

interface MusicProfileData {
  userDetail: {
    id: number;
    username: string;
    profilePicture: string;
    dnas: {
      dnaKey: number;
      dnaName: string;
    }[];
  };
  favoriteArtist: {
    artistName: string;
    albumName: string;
    trackName: string;
    artistCover: string;
    albumCover: string;
    trackCover: string;
    spotifyArtistId: string;
  };
  bestAlbum: [
    {
      albumId: number;
      albumName: string;
      albumCover: string;
      score: number;
      spotifyId: string;
    },
  ];
  userDna: [
    {
      dnaName: string;
    },
  ];
  playlist: [
    {
      playlistId: number;
      trackId: number;
      title: string;
      artistName: string;
      trackCover: string;
    },
  ];
  followings: [
    {
      userId: number;
    },
  ];
  followers: [
    {
      userId: number;
    },
  ];
}

interface MusicDna {
  dnaKey: number;
  dnaName: string;
}

interface BestAlbum {
  albumId: number;
  albumName: string;
  albumCover: string;
  score: number;
  spotifyId: string;
}

interface FavoriteArtist {
  artistCover: string;
  artistName: string;
  albumCover: string;
  albumName: string;
  trackCover: string;
  trackName: string;
  spotifyArtistId: string;
}

interface FavoriteArtistSpotifyIds {
  spotifyArtistId: string;
  spotifyAlbumId: string;
  spotifyTrackId: string;
}

function MusicProfileEditPage() {
  const location = useLocation();
  const userId = location.state;

  const server = 'http://203.255.81.70:8030';
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

  const reissueTokenUrl = `${server}/api/auth/reissued`;
  let musiProfileUrl = `${server}/api/user/${userId}/musicProfile`;

  const navigate = useNavigate();
  const GoToMusicProfilePage = () => {
    navigate('/MusicProfilePage', { state: musicProfileData?.userDetail.id });
  };

  const [musicProfileData, setMusicProfileData] = useState<MusicProfileData>();
  const [myMusicDna, setMyMusicDna] = useState<MusicDna[]>([]);
  const [allMusicDna, setAllMusicDna] = useState<MusicDna[]>([]);
  const [bestAlbum, setBestAlbum] = useState<BestAlbum[]>();
  const [favoriteArtist, setFavoriteArtist] = useState<FavoriteArtist>();
  const [favoriteArtistSpotifyIds, setFavoriteArtistSpotifyIds] = useState<FavoriteArtistSpotifyIds>();
  const [isSearchModalOpen, setIsSearchModalOpen] = useState(false);
  const [searchingTopic, setSearchingTopic] = useState('');

  useEffect(() => {
    fetchData();
    if (musicProfileData) {
      setMusicProfileData(musicProfileData);
      setMyMusicDna(musicProfileData.userDetail.dnas);
      setBestAlbum(musicProfileData.bestAlbum);
      fetchMusicDNA();
    }
  }, []);

  const fetchData = async () => {
    if (token) {
      try {
        console.log('fetching...');
        const response = await fetch(musiProfileUrl, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log('set PostList');
          const data: MusicProfileData = await response.json();
          console.log(data);
          setMusicProfileData(data);
          setMyMusicDna(data.userDetail.dnas);
          setBestAlbum(data.bestAlbum);
          setFavoriteArtist(data.favoriteArtist);
          const spotifyId = {
            spotifyArtistId: data.favoriteArtist.spotifyArtistId,
            spotifyAlbumId: '',
            spotifyTrackId: '',
          };
          setFavoriteArtistSpotifyIds(spotifyId);
          fetchMusicDNA();
        } else if (response.status === 401) {
          ReissueToken();
          fetchData();
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

  // musicDNA 가져오기
  const MusicDNAUrl = `${server}/api/dna`;
  const fetchMusicDNA = async () => {
    if (token) {
      try {
        console.log(`fetching Playlist...`);
        const response = await fetch(MusicDNAUrl, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setAllMusicDna(data);
        } else if (response.status === 401) {
          ReissueToken();
          fetchMusicDNA();
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

  // DNA 추가 제거
  const addToMyDNA = (dnaKey: number) => {
    console.log('adding DNA...');
    if (allMusicDna && myMusicDna) {
      console.log('start adding DNA...');
      let addDna = allMusicDna?.find(dna => dna.dnaKey === dnaKey);
      if (addDna) {
        setAllMusicDna(allMusicDna.filter(dna => dna.dnaKey !== dnaKey));
        setMyMusicDna([...myMusicDna, addDna]);
        console.log('DNA added');
        console.log(addDna);
      }
    }
  };

  const deleteFromMyDNA = (dnaKey: number) => {
    console.log('deleting DNA...');
    if (allMusicDna && myMusicDna) {
      console.log('start deleting DNA...');
      let deleteDna = myMusicDna?.find(dna => dna.dnaKey === dnaKey);
      if (deleteDna) {
        setMyMusicDna(myMusicDna.filter(dna => dna.dnaKey !== dnaKey));
        setAllMusicDna([...allMusicDna, deleteDna]);
        console.log('DNA deleted');
        console.log(deleteDna);
      }
    }
  };

  // 수정사항 fetch
  const MusicDNAPostUrl = `${server}/api/dna`;
  const BestAlbumPostUrl = `${server}/api/bestAlbum`;
  const FavoriteArtistPostUrl = `${server}/api/favoriteArtist`;
  const postAllEdit = async () => {
    try {
      await postMusicDNA();
      await postBestAlbum();
      await postFavoriteArtist();
    } catch (error) {
      console.error('Post fail :', error);
    } finally {
      GoToMusicProfilePage();
    }
  };
  const postMusicDNA = async () => {
    if (token) {
      try {
        console.log(`updating DNA...`);
        console.log(myMusicDna);
        const response = await fetch(MusicDNAPostUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            dna: myMusicDna.map(dna => dna.dnaKey),
          }),
        });

        if (response.ok) {
          const data = await response.json();
          console.log(data);
        } else if (response.status === 401) {
          ReissueToken();
          postMusicDNA();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('DNA updated');
      }
    }
  };
  const postBestAlbum = async () => {
    console.log('Best ALbum Updating...');
    if (token) {
      try {
        const response = await fetch(BestAlbumPostUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            bestalbum: bestAlbum?.map(album => ({
              spotifyAlbumId: album.spotifyId,
              score: album.score,
            })),
          }),
        });

        if (response.ok) {
          const data = await response.json();
          console.log(data);
          console.log('Best Album 200');
        } else if (response.status === 401) {
          ReissueToken();
          postBestAlbum();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('Best ALbum Updated');
      }
    }
  };
  const postFavoriteArtist = async () => {
    console.log('Favorite Artist Updating...');
    if (token) {
      try {
        const response = await fetch(FavoriteArtistPostUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            spotifyArtistId: favoriteArtistSpotifyIds?.spotifyArtistId,
            spotifyAlbumId: favoriteArtistSpotifyIds?.spotifyAlbumId,
            spotifyTrackId: favoriteArtistSpotifyIds?.spotifyTrackId,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          console.log('favorite artist 200');
          console.log(data);
        } else if (response.status === 401) {
          ReissueToken();
          postFavoriteArtist();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('Favorite Artist Updated');
      }
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

  // searchPage 열기
  const openSearch = (topic: string) => {
    setSearchingTopic(topic);
    setIsSearchModalOpen(true);
  };

  return (
    <Container>
      <Header>
        <Nav page={2}></Nav>
      </Header>
      {isSearchModalOpen && (
        <Blur>
          <ModalArea>
            {searchingTopic === 'Album' || searchingTopic === 'Artist-album' ? (
              <SearchAlbumModal
                searchingTopic={searchingTopic}
                setIsSearchModalOpen={setIsSearchModalOpen}
                bestAlbum={bestAlbum}
                setBestAlbum={setBestAlbum}
                favoriteArtist={favoriteArtist}
                setFavoriteArtist={setFavoriteArtist}
                favoriteArtistSpotifyIds={favoriteArtistSpotifyIds}
                setFavoriteArtistSpotifyIds={setFavoriteArtistSpotifyIds}
              ></SearchAlbumModal>
            ) : searchingTopic === 'Artist' ? (
              <SearchArtistModal
                searchingTopic={searchingTopic}
                setIsSearchModalOpen={setIsSearchModalOpen}
                favoriteArtist={favoriteArtist}
                setFavoriteArtist={setFavoriteArtist}
                favoriteArtistSpotifyIds={favoriteArtistSpotifyIds}
                setFavoriteArtistSpotifyIds={setFavoriteArtistSpotifyIds}
              ></SearchArtistModal>
            ) : searchingTopic === 'Artist-track' ? (
              <SearchTrackModal
                searchingTopic={searchingTopic}
                setIsSearchModalOpen={setIsSearchModalOpen}
                favoriteArtist={favoriteArtist}
                setFavoriteArtist={setFavoriteArtist}
                favoriteArtistSpotifyIds={favoriteArtistSpotifyIds}
                setFavoriteArtistSpotifyIds={setFavoriteArtistSpotifyIds}
              ></SearchTrackModal>
            ) : null}
          </ModalArea>
        </Blur>
      )}
      <Body>
        <PageTitle>
          <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" className="bi bi-pencil-square" viewBox="0 0 16 16" onClick={() => console.log('dna 수정')}>
            <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
            <path
              fillRule="evenodd"
              d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
            />
          </svg>
          <Title></Title>
          MusicProfile 수정
        </PageTitle>
        <MusicDnaArea>
          <TitleArea>
            <Title>Music DNA</Title>
          </TitleArea>
          <ProfileTagArea>
            {myMusicDna.map(dna => (
              <Tag
                key={dna.dnaKey}
                opacity="1"
                onClick={() => {
                  deleteFromMyDNA(dna.dnaKey);
                }}
              >
                {' '}
                #{dna.dnaName}
              </Tag>
            ))}
          </ProfileTagArea>
          <ProfileTagArea>
            {allMusicDna
              .filter((dna: any) => !myMusicDna.some((userDna: any) => userDna.dnaKey === dna.dnaKey))
              .map(dna => (
                <Tag
                  key={dna.dnaKey}
                  opacity="0.3"
                  onClick={() => {
                    addToMyDNA(dna.dnaKey);
                  }}
                >
                  #{dna.dnaName}
                </Tag>
              ))}
          </ProfileTagArea>
        </MusicDnaArea>
        {/* <PlaylistCardArea>
          <TitleArea margin="0px 0px 20px 20px">
            <Title> Playlist</Title>
          </TitleArea>
          {musicProfileData?.playlist && <PlaylistCardMini playlist={musicProfileData?.playlist} userDetail={musicProfileData?.userDetail} />}
        </PlaylistCardArea> */}

        <BestAlbumArea>
          <TitleArea>
            <Title>BestAlbum</Title>
            {/* <svg onClick={() => openSearch('Album')} xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-plus-circle" viewBox="0 0 16 16">
              <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16" />
              <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4" />
            </svg> */}
            <Title onClick={() => openSearch('Album')} color={colors.Button_green}>
              {' '}
              +{' '}
            </Title>
          </TitleArea>
          {bestAlbum && <AlbumGridEdit bestAlbum={bestAlbum} setBestAlbum={setBestAlbum}></AlbumGridEdit>}
        </BestAlbumArea>
        <FavoriteArtistArea>
          <TitleArea>
            <Title>Favorite Artist</Title>
          </TitleArea>
          {favoriteArtist && <FavoriteArtistEditCard FavoriteArtistData={favoriteArtist} openSearch={openSearch}></FavoriteArtistEditCard>}
        </FavoriteArtistArea>
        <BtnArea>
          <Btn bgcolor={colors.Button_green} onClick={() => postAllEdit()}>
            <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
              저장
            </Text>
          </Btn>
          <Btn bgcolor={colors.BG_grey} onClick={() => GoToMusicProfilePage()}>
            <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
              취소
            </Text>
          </Btn>
        </BtnArea>
      </Body>
    </Container>
  );
}

export default MusicProfileEditPage;
