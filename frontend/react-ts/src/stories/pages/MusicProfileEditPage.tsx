import styled from 'styled-components';
import { useEffect, useState, ChangeEvent } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import AlbumGridEdit from '../components/AlbumGridEdit';
import FavoriteArtistEditCard from '../components/FavoriteArtistEditCard';
import SearchAlbumModal from '../components/SearchAlbumModal';
import SearchArtistModal from '../components/SearchArtistModal';
import SearchTrackModal from '../components/SearchTrackModal';
import { fetchGET, fetchPOST, fetchPOSTFile } from '../utils/fetchData';

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

const Label = styled.label`
  font-weight: bold;
  font-size: 15px;
  border-radius: 15px;
  color: ${colors.Font_black};
  background-color: ${colors.BG_grey};
  padding: 5px 10px 5px 10px;
`;

const Profile = styled.input`
  display: none;
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

const MusicDnaArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
  padding: 20px;
`;

const ProfilePhotoArea = styled.div`
  display: flex;
  width: 100vw;
  height: 150px;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 0px 20px 10px 20px;
`;

const PhotoEditArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const Circle = styled.div<{ bgcolor?: string }>`
  width: 80px;
  height: 80px;
  border-radius: 100%;
  overflow: hidden;
  margin-bottom: 10px;
  background-color: ${props => props.bgcolor};
`;
const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
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

  let musiProfileUrl = `/api/user/${userId}/musicProfile`;

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
  const [profile, setProfile] = useState('');

  useEffect(() => {
    fetchData(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
    if (musicProfileData) {
      setMusicProfileData(musicProfileData);
      setMyMusicDna(musicProfileData.userDetail.dnas);
      setBestAlbum(musicProfileData.bestAlbum);
      fetchMusicDNA(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
      setProfile(musicProfileData.userDetail.profilePicture);
    }
  }, []);

  const fetchData = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, musiProfileUrl).then(data => {
      setMusicProfileData(data);
      setMyMusicDna(data.userDetail.dnas);
      setBestAlbum(data.bestAlbum);
      setFavoriteArtist(data.favoriteArtist);
      setProfile(data.userDetail.profilePicture);
      const spotifyId = {
        spotifyArtistId: data.favoriteArtist.spotifyArtistId,
        spotifyAlbumId: '',
        spotifyTrackId: '',
      };
      setFavoriteArtistSpotifyIds(spotifyId);
      fetchMusicDNA(token, refreshToken);
    });
  };

  // musicDNA 가져오기
  const MusicDNAUrl = `/api/dna`;
  const fetchMusicDNA = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, MusicDNAUrl).then(data => {
      setAllMusicDna(data);
    });
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
  const MusicDNAPostUrl = `/api/dna`;
  const ProfilePostUrl = `/api/file/`;
  const BestAlbumPostUrl = `/api/bestAlbum`;
  const FavoriteArtistPostUrl = `/api/favoriteArtist`;
  const postAllEdit = async () => {
    const token = localStorage.getItem('login-token');
    const refreshToken = localStorage.getItem('login-refreshToken');
    try {
      await postMusicDNA(token || '', refreshToken || '');
      await postBestAlbum(token || '', refreshToken || '');
      await postFavoriteArtist(token || '', refreshToken || '');
      await postProfileImg(token || '', refreshToken || '');
    } catch (error) {
      console.error('Post fail :', error);
    } finally {
      GoToMusicProfilePage();
    }
  };

  const postMusicDNA = async (token: string, refreshToken: string) => {
    const data = {
      dna: myMusicDna.map(dna => dna.dnaKey),
    };
    await fetchPOST(token, refreshToken, MusicDNAPostUrl, data).then(data => {
      console.log(data);
    });
  };

  const postBestAlbum = async (token: string, refreshToken: string) => {
    const data = {
      bestalbum: bestAlbum?.map(album => ({
        spotifyAlbumId: album.spotifyId,
        score: album.score,
      })),
    };
    await fetchPOST(token, refreshToken, BestAlbumPostUrl, data).then(data => {
      console.log(data);
    });
  };

  const postFavoriteArtist = async (token: string, refreshToken: string) => {
    const data = {
      spotifyArtistId: favoriteArtistSpotifyIds?.spotifyArtistId,
      spotifyAlbumId: favoriteArtistSpotifyIds?.spotifyAlbumId,
      spotifyTrackId: favoriteArtistSpotifyIds?.spotifyTrackId,
    };
    await fetchPOST(token, refreshToken, FavoriteArtistPostUrl, data).then(data => {
      console.log(data);
    });
  };

  const postProfileImg = async (token: string, refreshToken: string) => {
    const data = formData;
    await fetchPOSTFile(token, refreshToken, ProfilePostUrl, data).then(data => {
      console.log(data);
    });
  };

  // searchPage 열기
  const openSearch = (topic: string) => {
    setSearchingTopic(topic);
    setIsSearchModalOpen(true);
  };

  const [formData, setFormData] = useState<FormData>();
  // 파일 선택 시 실행되는 핸들러
  const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]; // 첫 번째 파일 선택
    console.log('put img here');
    console.log(file);
    if (file) {
      setProfile(URL.createObjectURL(file)); // 미리보기 URL 생성
      const formData = new FormData(); //formData 새성
      console.log(URL.createObjectURL(file));

      formData.append('profileImage', file); // 이미지 값 할당

      const obj2: { [key: string]: any } = {};
      formData.forEach((value, key) => {
        obj2[key] = value;
      });

      console.log(obj2);
      setFormData(formData);
    }
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
          MusicProfile 수정
        </PageTitle>
        <ProfilePhotoArea>
          <TitleArea>
            <Title>Profile Photo</Title>
          </TitleArea>
          <PhotoEditArea>
            <Circle>
              <ProfileImageCircle src={profile} alt="Profile" />
            </Circle>
            <div>
              <Label htmlFor="profileimg"> 프로필 이미지 업로드</Label>
              <Profile type="file" accept="image/*" id="profileimg" onChange={handleFileChange} />
            </div>
          </PhotoEditArea>
        </ProfilePhotoArea>
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
