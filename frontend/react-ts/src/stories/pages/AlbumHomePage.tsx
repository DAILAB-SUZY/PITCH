import styled from 'styled-components';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import AlbumChatCard from '../components/AlbumChatCard';
import { useNavigate } from 'react-router-dom';
import cover1 from '../../img/aespa2.jpg';
import artistProfile from '../../img/aespaProfile.jpg';
import { useState } from 'react';
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh;
  width: 100vw;
  background-color: white;
  color: black;
`;

const Header = styled.div`
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Body = styled.div`
  margin-top: 120px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;
const SearchArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 370px;
  height: 40px;
  margin: 10px;
  background-color: ${colors.BG_lightgrey};
  overflow: hidden;
  border-radius: 10px;
`;

const ContentInput = styled.input`
  width: 370px;
  height: 40px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
  font-size: 15px;
  border: 0;
  //border-radius: 15px;
  outline: none;
  color: ${colors.Font_black};

  &::placeholder {
    opacity: 0.7;
  }
`;

const MostCommentedArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
`;

const TitleArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  height: 40px;
  margin: 10px 0px 10px 30px;
`;

const CommentCardArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
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
  background: linear-gradient(0deg, rgba(0, 0, 0, 1) 10%, rgba(0, 0, 0, 0) 100%);
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
const PostUploadTime = styled.div`
  display: flex;
  font-size: 10px;
  font-family: 'Rg';
  margin: 0px 0px 2px 10px;
  color: ${colors.BG_grey};
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
  background-color: ${colors.Main_Pink};
`;
const ChatCardBody = styled.div`
  margin: 10px 0px 10px 0px;
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const MostLikedArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 20px;
`;

const AlbumListArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
`;
const Album = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  width: 140px;
  height: 180px;
  margin: 10px;
`;
const AlbumCard = styled.div`
  position: relative;
  display: flex;
  width: 140px;
  height: 140px;
  border-radius: 8px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
  overflow: hidden;
`;

const AlbumInfo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
`;

const AlbumImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 140px;
  height: 140px;
  object-fit: cover;
  z-index: 1;
`;

const AlbumGradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 140px;
  height: 140px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(0deg, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 0) 70%);
  backdrop-filter: blur(0px);
`;

const AlbumContentArea = styled.div`
  width: 140px;
  height: 140px;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-end;
  padding: 10px;
  box-sizing: border-box;
  z-index: 3;
`;
const Line = styled.div`
  width: 90vw;
  border-bottom: 1px;
`;

const Title = styled.div<{ fontSize: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Bd';
`;
const Text = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string; opacity?: number; color?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
  opacity: ${props => props.opacity};
  color: ${props => props.color};
`;

function AlbumChatPage() {
  const items = Array.from({ length: 20 }, (_, index) => `Item ${index + 1}`);
  const navigate = useNavigate();

  const GoToPostPage = () => {
    navigate('/AlbumChatPostPage');
  };

  const [searchKeyword, setSearchKeyword] = useState('');

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    // 검색 결과 초기화
    //fetchSearch(); // 검색 실행
  };

  return (
    <Container>
      <Header>
        <Nav page={3}></Nav>
      </Header>
      <Body>
        <SearchArea>
          <form onSubmit={handleSearchSubmit}>
            <ContentInput placeholder="앨범의 제목을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>
        <MostCommentedArea>
          <TitleArea>
            <Title fontSize="30px" margin="0px">
              Most Commented
            </Title>
          </TitleArea>
          <CommentCardArea>
            <CommentCard>
              <ImageArea>
                <img src={cover1} width="100%" height="100%" object-fit="cover"></img>
              </ImageArea>
              <GradientBG> </GradientBG>
              <ContentArea>
                <AlbumTitleArea>
                  <Text fontSize="50px" margin="0px" fontFamily="Bd" color={colors.BG_white}>
                    Girls
                  </Text>
                  <Text fontSize="30px" margin="0px" fontFamily="Bd" opacity={0.7} color={colors.BG_white}>
                    aespa
                  </Text>
                </AlbumTitleArea>
                <CommentNumberArea>
                  <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="white" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
                    <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
                    <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
                  </svg>
                  <Text fontSize="25px" margin="0px 0px 0px 10px" fontFamily="Bd" color={colors.BG_white}>
                    560
                  </Text>
                  <Text fontSize="20px" margin="0px 0px 0px 5px" fontFamily="Rg" color={colors.BG_white}>
                    comments
                  </Text>
                </CommentNumberArea>
                <CommentContentArea>
                  <ProfileArea>
                    <ProfileImage>
                      <img src={artistProfile}></img>
                    </ProfileImage>
                    <ProfileTextArea>
                      <ProfileName>김준호</ProfileName>
                      <PostUploadTime> 1시간전 </PostUploadTime>
                    </ProfileTextArea>
                  </ProfileArea>
                  <ChatCardBody>
                    <Text fontSize="15px" color={colors.BG_white}>
                      이거 맨날 들어요 에스파 짱짱
                    </Text>
                  </ChatCardBody>
                </CommentContentArea>
              </ContentArea>
            </CommentCard>
          </CommentCardArea>
        </MostCommentedArea>
        <MostLikedArea>
          <TitleArea>
            <Title fontSize="30px" margin="0px">
              Most Liked
            </Title>
          </TitleArea>
          <AlbumListArea>
            <Album>
              <AlbumCard>
                <AlbumImageArea>
                  <img src={cover1} width="100%" height="100%" object-fit="cover"></img>
                </AlbumImageArea>
                <AlbumGradientBG> </AlbumGradientBG>
                <AlbumContentArea>
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill={colors.Button_active} className="bi bi-heart-fill" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
                  </svg>
                  <Text fontSize="18px" margin="10px 0px 0px 5px" fontFamily="Bd" opacity={0.8} color={colors.BG_white}>
                    987
                  </Text>
                </AlbumContentArea>
              </AlbumCard>
              <AlbumInfo>
                <Text fontSize="18px" margin="5px 0px 0px 5px" fontFamily="Bd" opacity={1} color={colors.Font_black}>
                  Girls
                </Text>
                <Text fontSize="15px" margin="0px 0px 0px 5px" fontFamily="Bd" opacity={0.8} color={colors.Font_grey}>
                  aespa
                </Text>
              </AlbumInfo>
            </Album>
          </AlbumListArea>
        </MostLikedArea>
      </Body>
    </Container>
  );
}

export default AlbumChatPage;
