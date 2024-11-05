import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useRef, useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AlbumPostCommentCard from '../components/AlbumPostCommentCard';
import useStore from '../store/store';
import { updateTimeAgo } from '../utils/getTimeAgo';
import { fetchGET, fetchPOST, fetchDELETE, MAX_REISSUE_COUNT } from '../utils/fetchData';
import ScoreShow from '../components/ScoreShow';
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
  height: auto;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  background-color: ${colors.BG_grey};
`;

const AlbumTitleArea = styled.div`
  position: relative;
  width: 100vw;
  height: 80vw;
  /* padding: 0px 0px 20px 10px; */
  /* z-index: 3; */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  color: white;
  box-sizing: border-box;
`;

const ImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 100vw;
  height: 80vw;
  /* object-fit: cover; */
  z-index: 1;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100vw;
  height: 80vw;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px 10px 0px 0px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const TitleTextArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  position: sticky;
  top: 10px;
  width: 100%;
  height: auto;
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
  padding: 0px 0px 20px 20px;
  box-sizing: border-box;
  z-index: 3;
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
  max-width: 280px;

  // 두 줄 이상일 때 '...' 처리
  display: -webkit-box;
  -webkit-line-clamp: 2; // 2줄까지만 표시
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const Line = styled.div`
  width: 360px;
  height: 1px;
  opacity: 0.5;
  background-color: ${colors.Button_deactive};
`;

const PostArea = styled.div`
  display: flex;
  width: 100vw;
  height: auto;
  /* overflow: hidden; */
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  margin: 0px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
`;

const ProfileArea = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
  flex-direction: row;
  margin-bottom: 10px;
`;
const PostUploadTime = styled.div`
  display: flex;
  font-size: 10px;
  font-family: 'RG';
  margin: 0px 0px 2px 10px;
  color: ${colors.Font_grey};
`;
const ProfileTextArea = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
`;
const ProfileName = styled.div`
  display: flex;
  font-size: 20px;
  font-family: 'SB';
  margin-left: 10px;
  color: ${colors.Font_black};
`;
const ProfileImage = styled.div`
  display: flex;
  overflow: hidden;
  width: 30px;
  height: 30px;
  border-radius: 50%;
`;

const ProfileImgTextArea = styled.div`
  display: flex;
  flex-direction: row;
  width: auto;
  justify-content: flex-start;
  align-items: center;
`;
const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const EditBtn = styled.div`
  display: flex;
  position: relative;
`;

const DropdownMenu = styled.div`
  position: absolute;
  top: 25px;
  right: 0;
  width: 70px;
  height: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background-color: white;
  border-radius: 7px;
  box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.3);
  z-index: 10;
  overflow: hidden;
`;

const DropdownItem = styled.div`
  padding: 10px;
  width: 70px;
  display: flex;
  justify-content: center;
  cursor: pointer;
  &:hover {
    background-color: ${colors.BG_grey};
  }
`;
const RowAlignArea = styled.div`
  display: flex;
  width: 90vw;
  height: auto;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0px;
  margin: 10px;
`;
const PostContentArea = styled.div`
  display: flex;
  width: 100%;
  height: auto;
  box-sizing: border-box;

  /* white-space: nowrap; */
  /* text-overflow: ellipsis; */
  white-space: pre-wrap;
  line-height: 150%;

  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: 'RG';
  padding: 0px 10px;
  margin: 10px 0px 20px 0px;

  transition: height ease 0.7s;
`;

const ButtonArea = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
`;

const ChatArea = styled.div`
  width: 100vw;
  height: auto;
  /* min-height: 70px; */
  /* overflow-y: scroll; */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;

  margin-bottom: 80px;
`;

const CommentCardArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  height: auto;
`;
interface albumPost {
  postDetail: {
    postId: number;
    content: string;
    createAt: string;
    updateAt: string;
    author: Author;
    album: Album;
  };
  comments: Comment[];
  likes: Like[];
}

interface Author {
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
  likes: Like[];
  score: number;
}

interface Like {
  id: number;
  username: string;
  profilePicture: string;
  dnas: DNA[];
}

interface DNA {
  dnaKey: number;
  dnaName: string;
}

interface Comment {
  id: number;
  content: string;
  createAt: string;
  updateAt: string;
  likes: Like[];
  childComments: ChildComment[];
  author: Author;
}

interface ChildComment {
  id: number;
  content: string;
  author: Author;
  createTime: string;
  updateTime: string;
}

function AlbumPostPage() {
  const location = useLocation();
  const [albumPost, setAlbumPost] = useState<albumPost>();
  const [score, setScore] = useState<number>(0);

  const navigate = useNavigate();
  const GoToHomePage = () => {
    navigate('/Home');
  };

  const GoToAlbumPostEditPage = () => {
    let album;
    if (albumPost) {
      album = {
        albumArtist: {
          artistId: null,
          imageUrl: null,
          name: albumPost.postDetail.album.artistName,
        },
        albumId: albumPost.postDetail.album.albumId,
        imageUrl: albumPost.postDetail.album.albumCover,
        name: albumPost.postDetail.album.title,
        total_tracks: null,
        release_date: null,
        postContent: albumPost.postDetail.content,
        postId: albumPost.postDetail.postId,
        score: albumPost.postDetail.album.score,
      };
    }
    navigate('/AlbumPostEditPage', { state: album });
  };

  const GoToAlbumPostCommentPostPage = () => {
    navigate('/AlbumPostCommentPostPage', { state: albumPost?.postDetail.postId });
  };

  const GoToAlbumPage = (spotifyAlbumId: string) => {
    navigate('/AlbumPage', { state: spotifyAlbumId });
  };

  const fetchAlbumPost = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, `/api/album/post/${location.state}`, MAX_REISSUE_COUNT).then(data => {
      setAlbumPost(data);
      console.log('fetched');
      console.log(data);
      setScore(data.postDetail.album.score);
    });
  };

  useEffect(() => {
    fetchAlbumPost(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  }, []);

  // Post 시간 계산
  const CreateTime = albumPost?.postDetail.createAt;
  const UpdatedTime = albumPost?.postDetail.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>('');

  useEffect(() => {
    if (albumPost) {
      if (CreateTime && UpdatedTime) {
        const time = updateTimeAgo(CreateTime, UpdatedTime);
        setTimeAgo(time);
      }
    }
  }, [CreateTime, UpdatedTime]);

  const { name, id } = useStore();

  // Post 좋아요 상태 확인
  const [isPostLiked, setIsPostLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);

  useEffect(() => {
    if (albumPost) {
      setLikesCount(albumPost.likes.length);
      if (albumPost.likes.some((like: any) => like.id === id)) {
        setIsPostLiked(true);
      }
    }
  }, [albumPost]);

  const changePostLike = async () => {
    if (isPostLiked && albumPost) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsPostLiked(false);
      setLikesCount(likesCount - 1);
      albumPost.likes = albumPost.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsPostLiked(true);
      setLikesCount(likesCount + 1);
      albumPost?.likes.push({
        id: id,
        username: name,
        profilePicture: 'string',
        dnas: [],
      });
    }
    fetchLike(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  };

  const PostLikeUrl = `/api/album/post/${albumPost ? albumPost.postDetail.postId : ''}/like`;
  const fetchLike = async (token: string, refresuToken: string) => {
    fetchPOST(token, refresuToken, PostLikeUrl, {}, MAX_REISSUE_COUNT);
  };

  // 수정/삭제 버튼
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement | null>(null);
  const editMenu = () => {
    console.log('edit');
    setIsDropdownOpen(!isDropdownOpen);
  };

  // 삭제요청
  const DeletePostUrl = '/api/album/post/' + (albumPost ? albumPost.postDetail.postId : '');
  const deletePost = async (token: string, refreshToken: string) => {
    setIsDropdownOpen(false);
    await fetchDELETE(token, refreshToken, DeletePostUrl, MAX_REISSUE_COUNT);
    GoToHomePage();
  };
  if (!albumPost) {
    return <div>Loading...</div>;
  }

  const GoToMusicProfilePage = () => {
    console.log('GoToMusicProfilePage');
    navigate('/MusicProfilePage', { state: albumPost?.postDetail.author.id });
  };

  return (
    <Container>
      {albumPost && (
        <>
          <AlbumPostArea>
            {/*  TODO: 스크롤 되다가 일부 남기고 멈추기 */}
            <AlbumTitleArea
              onClick={() => {
                GoToAlbumPage(albumPost.postDetail.album.spotifyId);
              }}
            >
              <ImageArea>
                <img
                  src={albumPost.postDetail.album.albumCover}
                  width="100%"
                  object-fit="cover"
                  // z-index="1"
                ></img>
              </ImageArea>
              <GradientBG> </GradientBG>
              <TitleTextArea>
                <Text fontFamily="EB" fontSize="30px" margin="0px" color={colors.BG_white}>
                  {albumPost.postDetail.album.title}
                </Text>
                <Text fontFamily="RG" fontSize="20px" margin="0px 0px 2px 10px" color={colors.BG_white}>
                  {albumPost.postDetail.album.artistName}
                </Text>
              </TitleTextArea>
            </AlbumTitleArea>

            <PostArea>
              <ProfileArea>
                <ProfileImgTextArea onClick={() => GoToMusicProfilePage()}>
                  <ProfileImage>
                    <ProfileImageCircle src={albumPost.postDetail.author.profilePicture}></ProfileImageCircle>
                  </ProfileImage>
                  <ProfileTextArea>
                    <ProfileName>{albumPost.postDetail.author.username}</ProfileName>
                    <PostUploadTime> {timeAgo} </PostUploadTime>
                  </ProfileTextArea>
                </ProfileImgTextArea>
                {albumPost.postDetail.author.id === id && (
                  <EditBtn ref={dropdownRef}>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="20"
                      height="20"
                      fill={colors.Font_grey}
                      className="bi bi-three-dots"
                      viewBox="0 0 16 16"
                      onClick={() => editMenu()} // 클릭 시 토글
                    >
                      <path d="M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3" />
                    </svg>
                    {isDropdownOpen && (
                      <DropdownMenu>
                        <DropdownItem onClick={() => GoToAlbumPostEditPage()}>수정</DropdownItem>
                        <DropdownItem onClick={() => deletePost(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '')}>삭제</DropdownItem>
                      </DropdownMenu>
                    )}
                  </EditBtn>
                )}
              </ProfileArea>
              <Line></Line>
              <ScoreShow score={score} />
              <Line></Line>
              <PostContentArea>{albumPost.postDetail.content}</PostContentArea>
              <ButtonArea>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="14"
                  height="14"
                  fill={isPostLiked ? colors.Button_active : colors.Button_deactive}
                  className="bi bi-heart-fill"
                  viewBox="0 0 16 16"
                  onClick={() => changePostLike()}
                >
                  <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
                </svg>
                <Text fontFamily="RG" fontSize="14px" color="grey" margin="0px 20px 0px 3px">
                  좋아요 {albumPost.likes.length}개
                </Text>
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
                  <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
                  <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
                </svg>
                <Text fontFamily="RG" fontSize="14px" color="grey" margin="0px 0px 0px 3px">
                  답글 {albumPost.comments.length}개
                </Text>
              </ButtonArea>
            </PostArea>
          </AlbumPostArea>
          <ChatArea>
            <RowAlignArea>
              <Text fontFamily="EB" fontSize="30px" margin="0px" color="black">
                Comment
              </Text>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="22"
                height="22"
                fill="currentColor"
                className="bi bi-pencil-square"
                viewBox="0 0 16 16"
                onClick={() => {
                  GoToAlbumPostCommentPostPage();
                }}
              >
                <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                <path
                  fillRule="evenodd"
                  d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
                />
              </svg>
            </RowAlignArea>
            <CommentCardArea>
              {albumPost.comments.map((comment: any, index: number) => (
                <>
                  <Line></Line>
                  <AlbumPostCommentCard key={index} comment={comment} postId={albumPost.postDetail.postId} fetchAlbumPost={fetchAlbumPost}></AlbumPostCommentCard>
                </>
              ))}
            </CommentCardArea>
          </ChatArea>
        </>
      )}
    </Container>
  );
}

export default AlbumPostPage;
