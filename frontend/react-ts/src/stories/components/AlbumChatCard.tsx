import styled from "styled-components";
import { colors } from "../../styles/color";
import cover1 from "../../img/cat.webp";
const ChatCardContainer = styled.div`
  width: 90vw;
  height: 100%;
  padding: 10px;
  margin-bottom: 20px;
  background-color: ${colors.BG_grey};
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const ChatCardHeader = styled.div`
  margin-top: 10px;
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const ChatCardBody = styled.div`
  margin-top: 10px;
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const ReactsArea = styled.div`
  margin-top: 10px;
  width: 90%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: end;
`;

const ReactsBody = styled.div`
  margin-top: 10px;
  width: 45%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
`;

const CardHeader = styled.div`
  width: 40%;
  display: flex;
  flex-direction: row;
  align-items: end;
  justify-content: space-between;
`;

const Text = styled.div<{ fontSize?: string; margin?: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Rg";
`;

const ProfileArea = styled.div`
  display: flex;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
`;
const PostUploadTime = styled.div`
  display: flex;
  font-size: 10px;
  font-family: "Rg";
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
  font-family: "Rg";
  margin-left: 10px;
  color: ${colors.Font_black};
`;
const ProfileImage = styled.div`
  display: flex;
  overflow: hidden;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: ${colors.Main_Pink};
`;

const ButtonArea = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
`;

interface commentProps {
  comment: {
    id: number;
    content: string;
    createdAt: number;
    updatedAt: number;
    likes: [
      {
        id: number;
        username: string;
        profilePicture: string;
      },
    ];
    childComments: [
      {
        id: number;
        content: string;
        author: {
          id: number;
          username: string;
          profilePicture: string;
        };
      },
    ];
    author: {
      id: number;
      username: string;
      profilePicture: string;
    };
  };
}

const AlbumChatBox = ({ comment }: commentProps) => {
  return (
    <ChatCardContainer>
      <ProfileArea>
        <ProfileImage>
          <img src={cover1}></img>
        </ProfileImage>
        <ProfileTextArea>
          <ProfileName>{comment.author.username}</ProfileName>
          <PostUploadTime> 1시간 전 </PostUploadTime>
        </ProfileTextArea>
      </ProfileArea>
      <ChatCardBody>
        <Text fontSize="15px">{comment.content}</Text>
      </ChatCardBody>
      <ButtonArea>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="14"
          height="14"
          fill={
            comment.likes.some((like: any) => like.id === comment.author.id)
              ? colors.Button_active
              : colors.Button_deactive
          }
          className="bi bi-heart-fill"
          viewBox="0 0 16 16"
        >
          <path
            fill-rule="evenodd"
            d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"
          />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 20px 0px 3px">
          좋아요 {comment.likes.length}개
        </Text>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="14"
          height="14"
          fill="grey"
          className="bi bi-chat-right-text-fill"
          viewBox="0 0 16 16"
          style={{ strokeWidth: 6 }}
        >
          <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
          <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 0px 0px 3px">
          답글 {comment.childComments.length}개
        </Text>
      </ButtonArea>
    </ChatCardContainer>
  );
};

export default AlbumChatBox;
