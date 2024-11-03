import styled from 'styled-components';
import { colors } from '../../styles/color';

const CommentCardArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin: 0px 10px 0px 0px;
`;

const CommentCard = styled.div`
  position: relative;
  display: flex;
  width: 360px;
  height: 360px;
  border-radius: 12px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
  overflow: hidden;
`;

const ImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 360px;
  height: 360px;
  object-fit: cover;
  z-index: 1;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 360px;
  height: 360px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(0deg, rgba(0, 0, 0, 1) 10%, rgba(0, 0, 0, 0.2) 100%);
  backdrop-filter: blur(0px);
`;

const ContentArea = styled.div`
  width: 360px;
  height: 360px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 20px;
  box-sizing: border-box;
  z-index: 3;
`;

const AlbumTitleArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  width: 100%;
  height: 210px;
`;

const CommentNumberArea = styled.div`
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-start;
  width: 100%;
  height: 30px;
`;

const CommentContentArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  width: 100%;
  height: 80px;
  opacity: 0.8;
`;

const ProfileArea = styled.div`
  display: flex;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
  margin-top: 20px;
`;

const ProfileTextArea = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
`;
const ProfileName = styled.div`
  display: flex;
  font-size: 20px;
  font-family: 'Rg';
  margin-left: 10px;
  color: ${colors.BG_lightgrey};
`;
const ProfileImage = styled.div`
  display: flex;
  overflow: hidden;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  /* background-color: ${colors.Main_Pink}; */
`;
const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const ChatCardBody = styled.div`
  margin: 10px 0px 10px 0px;
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Text = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string; opacity?: number; color?: string; width?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
  opacity: ${props => props.opacity};
  color: ${props => props.color};
  width: ${props => props.width};

  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
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

interface AlbumLike extends User {} // User와 동일한 구조 확장

interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: null | string;
  spotifyId: string;
  likes: AlbumLike[];
}

interface CommentAuthor extends User {} // User 구조 확장

interface AlbumChatComment {
  albumChatCommentId: number;
  content: string;
  createAt: string; // ISO 날짜 형식
  updateAt: string; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: CommentAuthor;
}

interface AlbumData {
  albumDetail: AlbumDetail;
  comments: AlbumChatComment[];
}

interface AlbumProps {
  album: AlbumData;
}

function MostCommentedCard({ album }: AlbumProps) {
  console.log(album.comments.length);
  return (
    <CommentCardArea>
      <CommentCard>
        <ImageArea>
          <img src={album.albumDetail.albumCover} width="100%" height="100%" object-fit="cover"></img>
        </ImageArea>
        <GradientBG> </GradientBG>
        <ContentArea>
          <AlbumTitleArea>
            <Text fontSize="30px" margin="5px 0px 10px 0px" fontFamily="Bd" color={colors.BG_white} width="100%">
              {album.albumDetail.title}
            </Text>
            <Text fontSize="20px" margin="0px" fontFamily="Bd" opacity={0.7} color={colors.BG_white}>
              {album.albumDetail.artistName}
            </Text>
          </AlbumTitleArea>
          <CommentNumberArea>
            <svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" fill="white" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
              <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
              <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
            </svg>
            <Text fontSize="25px" margin="0px 0px 0px 10px" fontFamily="Bd" color={colors.BG_white}>
              {album.comments.length}
            </Text>
            <Text fontSize="20px" margin="0px 0px 0px 5px" fontFamily="Rg" color={colors.BG_white}>
              comment
            </Text>
          </CommentNumberArea>
          <CommentContentArea>
            {album.comments.length === 0 ? (
              <Text fontSize="15px" color={colors.BG_white} margin="40px 0px 0px 20px">
                아직 댓글이 없습니다.
              </Text>
            ) : (
              <>
                <ProfileArea>
                  <ProfileImage>
                    <ProfileImageCircle src={album.comments[0].author.profilePicture} />
                  </ProfileImage>
                  <ProfileTextArea>
                    <ProfileName>{album.comments[0].author.username}</ProfileName>
                    {/* <PostUploadTime> 1시간전 </PostUploadTime> */}
                  </ProfileTextArea>
                </ProfileArea>
                <ChatCardBody>
                  <Text fontSize="15px" color={colors.BG_white}>
                    {album.comments[0].content}
                  </Text>
                </ChatCardBody>
              </>
            )}
          </CommentContentArea>
        </ContentArea>
      </CommentCard>
    </CommentCardArea>
  );
}

export default MostCommentedCard;
