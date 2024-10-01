import styled from "styled-components";
import cover1 from "../../img/newjeans.png";
import cover2 from "../../img/aespa.webp";
import { colors } from "../../styles/color";

const AlbumPostBG = styled.div`
  width: 90vw;
  margin: 20px 0px;
  height: 55vh;
  background-color: ${colors.BG_grey};
  border-radius: 20px;
  /* overflow: hidden; */
  position: relative;
  z-index: 1;
  line-height: 18px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  /* transition: all ease-out 3s; */
`;

const AlbumTitleContainer = styled.div`
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

const ImageContainer = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 90vw;
  height: 200px;
  object-fit: cover;
  z-index: 1;
  border-radius: 20px 20px 0px 0px;
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
  border-radius: 20px 20px 0px 0px;
  background: linear-gradient(
    0deg,
    rgba(0, 0, 0, 0.8) 0%,
    rgba(0, 0, 0, 0) 100%
  );
  backdrop-filter: blur(0px);
`;

const TextContainer = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
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
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  color: ${(props) => props.color};
  margin-right: 15px;
`;

const PostContainer = styled.div<{ height: string }>`
  display: flex;
  border-radius: 0 0 20px 20px;
  width: 320px;

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

const ProfileContainer = styled.div`
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
  margin-left: 10px;
  color: ${colors.Font_grey};
`;
const ProfileTextContainer = styled.div`
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
  width: 26px;
  height: 26px;
  border-radius: 13px;
  background-color: ${colors.Main_Pink};
`;

const PostContent = styled.div`
  display: flex;
  width: 320px;
  height: 36px;
  box-sizing: border-box;

  /* white-space: nowrap; */
  overflow: hidden;
  /* text-overflow: ellipsis; */

  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: "Rg";
  padding: 0px 10px;
  margin: 10px 0;

  transition: height ease 0.7s;
`;

const ButtonContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
`;

const AlignContainer = styled.div<{
  alignItems: string;
  justifyContent: string;
  margin: string;
}>`
  display: flex;
  width: 100%;
  align-items: ${(props) => props.alignItems};
  justify-content: ${(props) => props.justifyContent};
  margin: ${(props) => props.margin};
`;

const GradientText = styled.div`
  position: absolute;
  top: 0px;
  height: 100%;
  width: 100%;
  background: linear-gradient(
    0deg,
    rgba(238, 238, 238, 0.5) 0%,
    rgba(238, 238, 238, 0) 100%
  );
`;

const AlbumPostBox = () => {
  return (
    <AlbumPostBG>
      <AlbumTitleContainer>
        <ImageContainer>
          <img
            src={cover1}
            width="320px"
            height="320px"
            object-fit="cover"
            // z-index="1"
          ></img>
        </ImageContainer>
        <GradientBG> </GradientBG>
        <TextContainer>
          <Text fontFamily="Bd" fontSize="30px">
            Get Up
          </Text>
          <Text fontFamily="Rg" fontSize="15px">
            NewJeans
          </Text>
        </TextContainer>
      </AlbumTitleContainer>
      <PostContainer>
        <ProfileContainer>
          <ProfileImage></ProfileImage>
          <ProfileTextContainer>
            <ProfileName>김준호</ProfileName>
            <PostUploadTime> 1시간 전</PostUploadTime>
          </ProfileTextContainer>
        </ProfileContainer>
        <PostContent>
          <p> 글을 입력해주세요.</p>
        </PostContent>
      </PostContainer>
    </AlbumPostBG>
  );
};

export default AlbumPostBox;
