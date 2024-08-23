import styled from "styled-components";
import { colors } from "../../styles/color";
import { useRef, useEffect, useState } from "react";
import cover1 from "../../img/newjeans.png";
import cover2 from "../../img/aespa.webp";

const AlbumPostContainer = styled.div`
  width: 320px;
  margin: 20px 0px;
  height: auto;
  background-color: ${colors.BG_grey};
  border-radius: 20px;
  /* overflow: hidden; */
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
  width: 320px;
  height: 200px;
  /* object-fit: cover; */
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
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const TitleTextArea = styled.div`
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

const PostArea = styled.div<{ height: string }>`
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

const PostContentArea = styled.div`
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

const ButtonArea = styled.div`
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
  background: linear-gradient(0deg, rgba(238, 238, 238, 0.5) 0%, rgba(238, 238, 238, 0) 100%);
`;

interface AlbumPostProps {
  postClick: () => void;
  song: string;
}

const AlbumPost = (props: AlbumPostProps) => {
  const contentHeight = useRef<HTMLDivElement>(null);
  const textHeight = useRef<HTMLDivElement>(null);

  const { postClick, song } = props;
  // const [songname, setSongname] = useState("");
  // setSongname(song);
  const getCover = (name: string) => {
    try {
      return require(`../../img/${name}.png`); // 확장자에 맞게 변경
    } catch (error) {}
  };
  console.log("song:" + song);

  const changeViewMore = () => {
    const contentHeightStyle = window.getComputedStyle(contentHeight.current as HTMLDivElement);
    const textHeightStyle = window.getComputedStyle(textHeight.current as HTMLDivElement);

    if (contentHeightStyle.getPropertyValue("height") === "36px") {
      contentHeight.current?.style.setProperty("height", textHeightStyle.getPropertyValue("height"));
    } else if (contentHeightStyle.getPropertyValue("height") === textHeightStyle.getPropertyValue("height"))
      contentHeight.current?.style.setProperty("height", "36px");
    postClick();
    console.log("id:" + song);
  };

  return (
    <AlbumPostContainer>
      <AlbumTitleArea>
        <ImageArea>
          <img
            src={cover1}
            width="320px"
            height="320px"
            object-fit="cover"
            // z-index="1"
          ></img>
        </ImageArea>
        <GradientBG> </GradientBG>
        <TitleTextArea>
          <Text fontFamily="Bd" fontSize="30px">
            Get Up
          </Text>
          <Text fontFamily="Rg" fontSize="15px">
            NewJeans
          </Text>
        </TitleTextArea>
      </AlbumTitleArea>
      <PostArea>
        <ProfileArea>
          <ProfileImage></ProfileImage>
          <ProfileTextArea>
            <ProfileName>김준호</ProfileName>
            <PostUploadTime> 1시간 전</PostUploadTime>
          </ProfileTextArea>
        </ProfileArea>
        <PostContentArea ref={contentHeight}>
          <p ref={textHeight}>
            {" "}
            뉴진스의 "Get Up" 앨범은 그들의 음악적 성장과 변화를 잘 보여주는 작품입니다.
            <br />
            <br />
            앨범 전반에 걸쳐 뉴진스만의 청량하고 에너제틱한 매력이 잘 드러나며, 듣는 이에게 긍정적인 에너지를
            전달합니다.
            <br />
            <br />
            각 곡마다 멤버들의 개성과 실력이 돋보이며, K-pop 팬들에게 새로운 즐거움을 선사합니다.
            <br />
            <br />
            앞으로 뉴진스가 어떤 음악을 선보일지 기대하게 만드는, 매력 넘치는 앨범입니다. <br />
            <br />
            뉴진스 화이팅🔥
          </p>
        </PostContentArea>

        <ButtonArea>
          <Text fontFamily="Rg" fontSize="14px" color="grey" onClick={changeViewMore}>
            더보기
          </Text>
        </ButtonArea>
      </PostArea>
    </AlbumPostContainer>
  );
};

export default AlbumPost;
