import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useStore from '../store/store';
import { updateTimeAgo } from '../utils/getTimeAgo';
import { fetchPOST, MAX_REISSUE_COUNT } from '../utils/fetchData';

const AlbumPostContainer = styled.div`
  width: 350px;
  margin: 10px 0px;
  height: auto;
  background-color: ${colors.BG_grey};
  border-radius: 10px;
  position: relative;
  z-index: 1;
  line-height: 18px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  /* transition: all ease-out 3s; */
`;

const AlbumTitleArea = styled.div`
  position: relative;
  width: 100%;
  height: 200px;
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
  width: 350px;
  height: 200px;
  /* object-fit: cover; */
  z-index: 1;
  border-radius: 10px 10px 0px 0px;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px 10px 0px 0px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const TitleTextArea = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-end;
  padding: 0px 0px 20px 20px;
  box-sizing: border-box;
  z-index: 3;
`;

const Text = styled.div<{ fontSize?: string; fontFamily?: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
  height: auto;
  width: auto;
  max-width: 230px;
  margin: ${props => props.margin};
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-all;
  line-height: 110%;
`;

const PostArea = styled.div`
  display: flex;
  border-radius: 0 0 10px 10px;
  width: 350px;

  height: auto;
  /* overflow: hidden; */
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
  transition: max-height linear 1s;
`;

const ProfileArea = styled.div`
  display: flex;
  width: auto;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
`;

const PostHeaderArea = styled.div`
  display: flex;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
`;

const PostUploadTime = styled.div`
  display: flex;
  font-size: 10px;
  font-family: 'RG';
  margin-left: 10px;
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
  font-family: 'RG';
  margin-left: 10px;
  color: ${colors.Font_black};
`;
const ProfileImage = styled.div`
  display: flex;
  overflow: hidden;
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background-color: ${colors.BG_grey};
`;
const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const PostContentArea = styled.div`
  display: flex;
  width: 320px;
  height: 40px;
  box-sizing: border-box;

  /* white-space: nowrap; */
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 140%;

  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: 'RG';
  padding: 0px 10px;
  margin: 7px 0;

  transition: height ease 0.7s;
`;

const ButtonArea = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
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

interface Album {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
  likes: User[];
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
interface AlbumPostProps {
  albumPost: {
    postDetail: PostDetail;
    comments: Comment[];
    likes: User[];
  };
}

const AlbumPost = ({ albumPost }: AlbumPostProps) => {
  const { name, id } = useStore();
  const navigate = useNavigate();
  const GoToAlbumPostPage = () => {
    console.log('__postid');
    console.log(albumPost.postDetail.postId);
    navigate('/AlbumPostPage', { state: albumPost.postDetail.postId });
  };

  const GoToMusicProfilePage = () => {
    navigate('/MusicProfilePage', { state: albumPost?.postDetail.author.id });
  };

  // Post 시간 계산
  const CreateTime = albumPost?.postDetail.createAt;
  const UpdatedTime = albumPost?.postDetail.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>('');

  useEffect(() => {
    if (albumPost) {
      const time = updateTimeAgo(CreateTime, UpdatedTime);
      setTimeAgo(time);
    }
  }, [CreateTime, UpdatedTime]);

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

  // 좋아요 상태 변경 함수
  const PostLikeUrl = '/api/album/post/' + (albumPost ? albumPost.postDetail.postId : '') + '/like';

  const changeAlbumLike = async () => {
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

  const fetchLike = async (token: string, refresuToken: string) => {
    fetchPOST(token, refresuToken, PostLikeUrl, {}, MAX_REISSUE_COUNT);
  };

  const GoToAlbumPage = (spotifyAlbumId: string) => {
    navigate('/AlbumPage', { state: spotifyAlbumId });
  };

  return (
    <AlbumPostContainer>
      <AlbumTitleArea
        onClick={() => {
          GoToAlbumPage(albumPost.postDetail.album.spotifyId);
        }}
      >
        <ImageArea>
          <img
            src={albumPost?.postDetail.album.albumCover}
            width="350px"
            height="320px"
            object-fit="cover"
            // z-index="1"
          ></img>
        </ImageArea>
        <GradientBG> </GradientBG>
        <TitleTextArea>
          <Text fontFamily="EB" fontSize="30px">
            {albumPost?.postDetail.album.title}
          </Text>
          <Text fontFamily="RG" fontSize="15px" margin="0px 0px 2px 10px">
            {albumPost?.postDetail.album.artistName}
          </Text>
        </TitleTextArea>
      </AlbumTitleArea>
      <PostArea>
        <PostHeaderArea>
          <ProfileArea onClick={() => GoToMusicProfilePage()}>
            <ProfileImage>
              <ProfileImageCircle src={albumPost?.postDetail.author.profilePicture} alt="Profile" />
            </ProfileImage>
            <ProfileTextArea>
              <ProfileName>{albumPost?.postDetail.author.username}</ProfileName>
            </ProfileTextArea>
          </ProfileArea>
          <PostUploadTime> {timeAgo}</PostUploadTime>
        </PostHeaderArea>
        <PostContentArea onClick={() => GoToAlbumPostPage()}>{albumPost?.postDetail.content}</PostContentArea>
        {/* <PostContentArea ref={contentHeight}>
          <p ref={textHeight}>{albumPost.content}</p>
        </PostContentArea> */}

        <ButtonArea>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            fill={albumPost?.likes.some(like => like.id === id) ? colors.Button_active : colors.Button_deactive}
            className="bi bi-heart-fill"
            viewBox="0 0 16 16"
            onClick={() => changeAlbumLike()}
          >
            <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
          </svg>
          <Text fontFamily="RG" fontSize="14px" color="grey" margin="0px 20px 0px 5px">
            좋아요 {albumPost?.likes.length}개
          </Text>
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
            <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
            <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
          </svg>
          <Text fontFamily="RG" fontSize="14px" color="grey" margin="0px 0px 0px 5px">
            답글 {albumPost?.comments.length}개
          </Text>
        </ButtonArea>
      </PostArea>
    </AlbumPostContainer>
  );
};

export default AlbumPost;
