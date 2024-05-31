import styled from "styled-components";
import { colors } from "../../styles/color";
import { useRef, useEffect, useState } from "react";
import cover from "../../img/newjeans_cover.png";
import cover2 from "../../img/aespa.webp";

const AlbumPostBG = styled.div<{ height: string }>`
  width: 320px;
  margin: 20px 0px;
  height: ${(props) => props.height};
  background-color: ${colors.BG_grey};
  border-radius: 20px;
  /* overflow: hidden; */
  position: relative;
  z-index: 1;
  line-height: 18px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  transition-property: height;
  transition-duration: 1s;
`;
const ImageContainer = styled.div`
  overflow: hidden;
  height: 320px;
  border-radius: 20px;
`;
const GradientBG = styled.div`
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
  width: 100%;
  height: 320px;
  object-fit: cover;
  display: flex;
  top: 0px;
  left: 0px;
  border-radius: 20px;
  background: linear-gradient(
    0deg,
    rgba(0, 0, 0, 0.7) 0%,
    rgba(0, 0, 0, 0) 100%
  );
  backdrop-filter: blur(0px);
`;

const AlbumPostContainer = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: auto;
  /* overflow: hidden; */
  display: flex;
  align-items: center;
  justify-content: flex-end;
  flex-direction: column;
  z-index: 3;
  box-sizing: border-box;
  border-radius: 20px;
`;

const AlbumTitleContainer = styled.div`
  /* position: static; */
  width: 100%;
  height: 200px;
  padding: 0px 0px 20px 10px;
  /* z-index: 3; */
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  color: white;
  box-sizing: border-box;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  color: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  /* width: 100%; */
  margin-left: 10px;
  height: auto;
  z-index: 3;
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  /* top: 0px;
  left: 0px; */
  color: ${(props) => props.color};
`;

const PostContainer = styled.div<{ height: string }>`
  /* position: absolute;
  z-index: 3; */
  position: relative;
  top: 60%;
  left: 0px;
  display: flex;
  border-radius: 0 0 20px 20px;
  width: 100%;
  /* height: 40%; */
  max-height: ${(props) => props.height};
  overflow: hidden;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
  transition: max-height ease-in-out 0.5s;
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
  width: 100%;
  height: auto;
  overflow: hidden;
  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: "Rg";
  margin: 10px;
  white-space: pre-wrap;
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

interface AlbumPost {
  postClick?: () => void;
}

const AlbumPost = ({ postClick }: AlbumPost) => {
  const [isViewMore, setIsMoreView] = useState(false);
  const [boxHeight, setBoxHeight] = useState("120px");
  const [albumPostBoxheight, setAlbumPostBoxheight] = useState("auto");

  const divRef = useRef<HTMLDivElement>(null);
  const [divHeight, setDivHeight] = useState(320);

  const divRef2 = useRef<HTMLDivElement>(null);

  const changeViewMore = () => {
    setIsMoreView(!isViewMore);
    if (boxHeight == "120px") setBoxHeight("1000px");
    else if (boxHeight == "1000px") setBoxHeight("120px");
    postClick;
  };

  useEffect(() => {
    const divElement = divRef.current;
    const divElement2 = divRef2.current;
    if (divElement && divElement2) {
      const height = divElement.offsetHeight;
      const height2 = divElement2.offsetHeight;
      setDivHeight(height2 + 200);
      console.log("container height: " + (height2 + 200));
      console.log("final1111: " + divHeight);
    }
    console.log("final2: " + divHeight);
  }, [boxHeight]);

  return (
    <AlbumPostBG height={divHeight + "px"}>
      <AlbumPostContainer ref={divRef}>
        <AlbumTitleContainer>
          <Text fontFamily="Bd" fontSize="30px">
            Get Up
          </Text>
          <Text fontFamily="Rg" fontSize="15px">
            NewJeans
          </Text>
        </AlbumTitleContainer>
        <PostContainer height={boxHeight} ref={divRef2}>
          <ProfileContainer>
            <ProfileImage></ProfileImage>
            <ProfileTextContainer>
              <ProfileName>김준호</ProfileName>
              <PostUploadTime> 1시간 전</PostUploadTime>
            </ProfileTextContainer>
          </ProfileContainer>
          <GradientText></GradientText>
          {isViewMore == false ? (
            <PostContent>
              {" "}
              하니 민지 해린 혜인 다니엘{"\n"}뉴진스 짱 뉴진스 짱 뉴진스 짱{" "}
            </PostContent>
          ) : (
            <PostContent>
              {" "}
              뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야
              뉴진스 짜릿해 뉴진스 짱{"\n"}
              {"\n"}
              뉴진스 최고야 뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해
              뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야
              뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야 {"\n"}
              {"\n"}
              뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해 뉴진스 짱
              뉴진스 최고야 뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해
              뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해 {"\n"}
              뉴진스 최고야 뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해
              뉴진스 짱 뉴진스 최고야 뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야
              뉴진스 짜릿해 뉴진스 짱 뉴진스 최고야 {"\n"}
              {"\n"}
              화이팅
            </PostContent>
          )}

          <ButtonContainer>
            <Text
              fontFamily="Rg"
              fontSize="14px"
              color="grey"
              onClick={changeViewMore}
            >
              더보기
            </Text>
          </ButtonContainer>
        </PostContainer>
      </AlbumPostContainer>
      <ImageContainer>
        <img
          src={cover2}
          width="320px"
          height="320px"
          object-fit="cover"
          z-index="1"
        ></img>
      </ImageContainer>
      <GradientBG> </GradientBG>
    </AlbumPostBG>
  );
};

export default AlbumPost;
