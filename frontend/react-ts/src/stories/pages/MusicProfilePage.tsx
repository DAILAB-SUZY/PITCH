import styled from 'styled-components';
import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import PlaylistCardMini from '../components/PlaylistCardMini';
import AlbumGrid from '../components/AlbumGrid';
import FavoriteArtistCard from '../components/FavoriteArtistCard';
import AlbumPostCard from '../components/AlbumPostCard';
import AlbumChatCardWithAlbum from '../components/AlbumChatCardWithAlbum';
import useStore from '../store/store';
import { fetchGET, fetchPOST, MAX_REISSUE_COUNT } from '../utils/fetchData';
import RatedAlbumCard from '../components/RatedAlbumCard';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh;
  width: 100vw;
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

const Text = styled.div<{ fontSize: string; fontFamily: string; margin: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
`;
const ProfileHeaderArea = styled.div`
  width: 360px;
  height: 150px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 20px 0px 0px 20px;
  margin-bottom: 20px;
`;

const ProfileLeftArea = styled.div`
  margin-top: 5vh;
  width: 100px;
  height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const Circle = styled.div<{ bgcolor?: string }>`
  width: 100px;
  height: 100px;
  border-radius: 100%;
  overflow: hidden;
  margin-bottom: 10px;
  background-color: ${props => props.bgcolor};
  object-fit: cover;
`;

const ProfileImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const FollowBtn = styled.div<{ bgcolor: string; color: string }>`
  width: 90px;
  height: 25px;
  font-size: 13px;
  font-family: 'Rg';
  color: ${props => props.color};
  background-color: ${props => props.bgcolor};
  border-color: ${colors.Main_Pink};
  border-style: solid;
  border-width: 2px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;

  /* &:hover {
    background-color: ${colors.Main_Pink};
    color: white;
  } */
`;

const ProfileRightArea = styled.div`
  width: 65vw;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  padding: 10px 0px 0px 10px;
`;

const ProfileNameArea = styled.div`
  width: 100%;
  height: 40px;
`;

const MusicDnaArea = styled.div`
  width: 100%;
  height: 110px;
`;

const Tag = styled.div<{ color: string }>`
  margin: 0px 10px 12px 0px;
  padding: 5px 12px;
  width: auto;
  height: auto;
  font-size: 15px;
  font-family: 'Rg';
  color: ${props => props.color};
  background-color: ${colors.BG_grey};
  /* border-color: ${colors.Tag};
  border-style: solid;
  border-width: 2px; */
  border-radius: 20px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
`;

const MenuArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 50px;
`;

const TabArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 350px;
  margin-bottom: 20px;
`;
const TabBtn = styled.div<{ bgcolor: string; color: string }>`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-family: 'Bd';
  width: 170px;
  height: 40px;
  border-radius: 5px;
  background-color: ${props => props.bgcolor};
  color: ${props => props.color};
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const FollowArea = styled.div`
  width: 320px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  margin-bottom: 20px;
  background-color: ${colors.BG_grey};
  color: black;
  padding: 10px 15px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const FollowBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: auto;
  height: 100%;
`;

const BestAlbumArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
`;

const Title = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
  width: 390px;
  box-sizing: border-box;
  padding-left: 20px;
  font-family: 'Bd';
  font-size: 22px;
`;

const FavoriteArtistArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
`;

const TwoColumnArea = styled.div`
  width: 360px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  flex-wrap: wrap;
  align-items: center;
`;
const RatedAlbumArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
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

const PlaylistCardSmall = styled.div`
  background: linear-gradient(to top right, #989898, #f3f3f3);
  border-radius: 12px;
  padding: 15px;
  /* margin-top: 20px; */
  width: 320px;
  height: 190px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: white;
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
`;

interface MusicProfileData {
  userDetail: {
    id: number;
    username: string;
    profilePicture: string;
    dnas: DNA[];
  };
  favoriteArtist: {
    artistName: string;
    albumName: string;
    trackName: string;
    artistCover: string;
    albumCover: string;
    trackCover: string;
  };
  bestAlbum: {
    albumId: number;
    albumName: string;
    albumCover: string;
    score: number;
    spotifyId: string;
  }[];
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

interface Album {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
  likes: User[];
  score: number;
}

interface PostAuthor extends User {}

interface PostDetail {
  postId: number;
  content: string;
  createAt: string;
  updateAt: string;
  author: PostAuthor;
  album: Album;
}

interface CommentAuthor extends User {}

interface ChildComment {
  id: number;
  content: string;
  author: CommentAuthor;
  createTime: string;
  updateTime: string;
}

interface Comment {
  id: number;
  content: string;
  createAt: string;
  updateAt: string;
  likes: User[];
  childComments: ChildComment[];
  author: CommentAuthor;
}
interface PostData {
  type: string;
  postDetail: PostDetail;
  comments: Comment[];
  likes: User[];
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
  createAt: string; // ISO 날짜 형식
  updateAt: string; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: User;
}
interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
}
interface ChatData {
  type: string;
  albumDetail: AlbumDetail;
  albumChatCommentDetail: AlbumChatComment;
}

type mergeActivityData = PostData | ChatData;

type ActivityData = mergeActivityData[];

const EditBtn = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 3px;
  background-color: ${colors.BG_grey};
  width: 90px;
  height: 25px;
  border-radius: 15px;
  /* padding: 10px; */
  box-sizing: border-box;
  /* margin-bottom: 80px; */
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

function MusicProfilePage() {
  const [tabBtn, setTabBtn] = useState(1);
  const [musicProfileData, setMusicProfileData] = useState<MusicProfileData>();
  const [activityData, setActivityData] = useState<ActivityData>([]);
  const [RatedAlbumList, setRatedAlbumList] = useState<Album[]>([]);
  const navigate = useNavigate();
  const location = useLocation();
  const profileId = location.state;

  // 페이지 이동 함수
  const GoToEditPage = (musicProfileData: MusicProfileData) => {
    navigate('/MusicProfileEditPage', { state: musicProfileData.userDetail.id });
  };
  const GoToFollowPage = (musicProfileData: MusicProfileData) => {
    navigate('/FollowPage', {
      state: {
        userId: musicProfileData.userDetail.id,
        userName: musicProfileData.userDetail.username,
      },
    });
  };
  // const GoToAlbumPage = (spotifyAlbumId: string) => {
  //   navigate('/AlbumPage', { state: spotifyAlbumId });
  // };

  const [isFollowed, setIsFollowed] = useState<boolean>();

  const { name, id } = useStore();
  console.log(`id: ${id} / name: ${name}`);

  let musiProfileUrl = `/api/user/${profileId}/musicProfile`;
  let activityUrl = `/api/user/${profileId}/musicProfile/activity`;
  let followUrl = `/api/user/follow/${profileId}`;

  const fetchData = async (token: string, refreshToken: string, URL: string) => {
    fetchGET(token, refreshToken, URL, MAX_REISSUE_COUNT).then(data => {
      if (data) {
        setMusicProfileData(data);
        // 내가 현재 musicprofile 사용자 follow 되어 있는지 판단
        if (data.followers.some((user: any) => user.userId === id)) {
          setIsFollowed(true);
          console.log('following this user');
        } else {
          console.log('NOT following this user');
          console.log(id);
          console.log(musicProfileData?.followers);
        }
      }
    });
    fetchStaredAlbum();
  };

  useEffect(() => {
    fetchData(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '', musiProfileUrl);
  }, []);

  const fetchActivityData = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, activityUrl, MAX_REISSUE_COUNT).then(data => {
      if (data) {
        const mergedData = [...data.albumPostList.map((post: any) => ({ ...post, type: 'post' })), ...data.albumCommentList.map((chat: any) => ({ ...chat, type: 'chat' as const }))];
        // createAt 기준으로 내림차순 정렬
        mergedData.sort((a, b) => {
          let stA: Date = new Date(0);
          let stB: Date = new Date(0);

          if (a.type === 'post') {
            const dateStr = a.postDetail.updateAt === null ? a.postDetail.createAt : a.postDetail.updateAt;
            stA = dateStr ? new Date(dateStr) : new Date(0);
          } else if (a.type === 'chat') {
            const dateStr = a.albumChatCommentDetail.updateAt === null ? a.albumChatCommentDetail.createAt : a.albumChatCommentDetail.updateAt;
            stA = dateStr ? new Date(dateStr) : new Date(0);
          }

          if (b.type === 'post') {
            const dateStr = b.postDetail.updateAt === null ? b.postDetail.createAt : b.postDetail.updateAt;
            stB = dateStr ? new Date(dateStr) : new Date(0);
          } else if (b.type === 'chat') {
            const dateStr = b.albumChatCommentDetail.updateAt === null ? b.albumChatCommentDetail.createAt : b.albumChatCommentDetail.updateAt;
            stB = dateStr ? new Date(dateStr) : new Date(0);
          }

          return stB.getTime() - stA.getTime();
        });
        setActivityData(mergedData);
        console.log('merged');
        console.log(mergedData);
      }
    });
  };

  const fetchFollow = async (token: string, refreshToken: string) => {
    fetchPOST(token, refreshToken, followUrl, {}, MAX_REISSUE_COUNT).then(data => {
      if (data) {
        setIsFollowed(!isFollowed);
      }
    });
  };

  const fetchStaredAlbum = async () => {
    const token = localStorage.getItem('login-token') || '';
    const refreshToken = localStorage.getItem('login-refreshToken') || '';
    const StaredAlbumUrl = `/api/user/${id}/score`;
    fetchGET(token, refreshToken, StaredAlbumUrl, MAX_REISSUE_COUNT).then(data => {
      if (data) {
        const sortedData = data.sort((a: Album, b: Album) => b.score - a.score);
        setRatedAlbumList(sortedData);
      }
    });
  };

  return (
    <Container>
      <Header>
        <Nav page={2}></Nav>
      </Header>
      <Body>
        <ProfileHeaderArea>
          <ProfileLeftArea>
            {musicProfileData ? (
              <Circle>
                <ProfileImage src={musicProfileData?.userDetail.profilePicture} alt="Profile" />
              </Circle>
            ) : (
              <Circle bgcolor={colors.BG_grey}></Circle>
            )}
            {id !== musicProfileData?.userDetail.id ? (
              isFollowed ? (
                <FollowBtn color={colors.BG_white} bgcolor={colors.Main_Pink} onClick={() => fetchFollow(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '')}>
                  Following
                </FollowBtn>
              ) : (
                <FollowBtn color={colors.Main_Pink} bgcolor={colors.BG_white} onClick={() => fetchFollow(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '')}>
                  Follow
                </FollowBtn>
              )
            ) : (
              <EditBtn onClick={() => GoToEditPage(musicProfileData)}>
                <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" fill="currentColor" className="bi bi-pencil-square" viewBox="0 0 16 16">
                  <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                  <path
                    fillRule="evenodd"
                    d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
                  />
                </svg>
                <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
                  수정
                </Text>
              </EditBtn>
            )}
          </ProfileLeftArea>
          <ProfileRightArea>
            <ProfileNameArea>
              <Text fontFamily="Bd" fontSize="30px" margin="0px 15px 0px 0px ">
                {musicProfileData?.userDetail.username}
              </Text>
            </ProfileNameArea>
            <MusicDnaArea>
              {musicProfileData ? (
                musicProfileData?.userDetail.dnas.map(dna => (
                  <Tag key={dna.dnaKey} color={colors.Font_black}>
                    #{dna.dnaName}
                  </Tag>
                ))
              ) : (
                <>
                  <Tag color={colors.BG_grey}>dnadna</Tag>
                  <Tag color={colors.BG_grey}>dnadnadna</Tag>
                  <Tag color={colors.BG_grey}>dnadnadna</Tag>
                  <Tag color={colors.BG_grey}>dnadna</Tag>
                </>
              )}
            </MusicDnaArea>
          </ProfileRightArea>
        </ProfileHeaderArea>
        {musicProfileData?.playlist ? (
          <PlaylistCardArea>
            <PlaylistCardMini playlist={musicProfileData.playlist} userDetail={musicProfileData.userDetail} />
          </PlaylistCardArea>
        ) : (
          <PlaylistCardArea>
            <PlaylistCardSmall></PlaylistCardSmall>
          </PlaylistCardArea>
        )}
        <MenuArea>
          <TabArea>
            <TabBtn bgcolor={tabBtn === 1 ? colors.Main_Pink : colors.BG_grey} color={tabBtn === 1 ? colors.BG_white : colors.Font_black} onClick={() => setTabBtn(1)}>
              Profile
            </TabBtn>
            <TabBtn
              bgcolor={tabBtn === 2 ? colors.Main_Pink : colors.BG_grey}
              color={tabBtn === 2 ? colors.BG_white : colors.Font_black}
              onClick={() => {
                setTabBtn(2);
                fetchActivityData(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
              }}
            >
              Activity
            </TabBtn>
          </TabArea>
        </MenuArea>
        {tabBtn === 1 && musicProfileData ? (
          <>
            <FollowArea onClick={() => GoToFollowPage(musicProfileData)}>
              <FollowBox>
                <Text fontFamily="Bd" fontSize="15px" margin="5px">
                  Follower
                </Text>
                <Text fontFamily="Rg" fontSize="15px" margin="5px">
                  {musicProfileData?.followers !== null ? musicProfileData?.followers.length : 0}
                </Text>
              </FollowBox>
              <FollowBox>
                <Text fontFamily="Bd" fontSize="15px" margin="5px">
                  Following
                </Text>
                <Text fontFamily="Rg" fontSize="15px" margin="5px">
                  {musicProfileData?.followings !== null ? musicProfileData?.followings.length : 0}
                </Text>
              </FollowBox>
            </FollowArea>
            <BestAlbumArea>
              <Title>BestAlbum</Title>
              {musicProfileData?.bestAlbum.length !== 0 ? (
                <AlbumGrid AlbumData={musicProfileData?.bestAlbum}></AlbumGrid>
              ) : (
                <Text fontFamily="Rg" fontSize="15px" margin="5px">
                  아직 설정하지 않았습니다.
                </Text>
              )}
            </BestAlbumArea>
            <FavoriteArtistArea>
              <Title>Favorite Artist</Title>
              {musicProfileData?.favoriteArtist && <FavoriteArtistCard FavoriteArtistData={musicProfileData.favoriteArtist}></FavoriteArtistCard>}
            </FavoriteArtistArea>
            <RatedAlbumArea>
              <Title>Rated Album</Title>
              {RatedAlbumList.length !== 0 ? (
                <TwoColumnArea>
                  {RatedAlbumList.map(Album => (
                    <RatedAlbumCard album={Album}></RatedAlbumCard>
                  ))}
                </TwoColumnArea>
              ) : (
                <Text fontFamily="Rg" fontSize="15px" margin="5px">
                  별점을 매긴 앨범이 없습니다.
                </Text>
              )}
            </RatedAlbumArea>
          </>
        ) : (
          <>
            {' '}
            {activityData?.length > 0 ? (
              activityData?.map((item, index) => {
                const { type, ...props } = item;
                if (type === 'post') {
                  const albumPostProps = props as PostData; // 타입 캐스팅
                  console.log(albumPostProps);
                  return <AlbumPostCard key={index} albumPost={albumPostProps} />;
                } else if (type === 'chat') {
                  const albumChatProps = props as ChatData; // 타입 캐스팅
                  return <AlbumChatCardWithAlbum key={index} comment={albumChatProps} />;
                }
              })
            ) : (
              <Text fontSize="15px" fontFamily="Rg" margin="150px 0px 0px 0px">
                {' '}
                작성한 글이 없습니다.
              </Text>
            )}
          </>
        )}
      </Body>
    </Container>
  );
}

export default MusicProfilePage;
