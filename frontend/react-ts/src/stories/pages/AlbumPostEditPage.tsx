import styled from "styled-components";
import { colors } from "../../styles/color";
import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import useStore from "../store/store";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh; //auto;
  width: 100vw;
  background-color: white;
  color: ${colors.Font_black};
`;

const AlbumPostArea = styled.div`
  width: 100vw;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  background-color: white;
`;

const AlbumTitleArea = styled.div`
  position: relative;
  width: 100vw;
  height: 100vw;
  /* padding: 0px 0px 20px 10px; */
  /* z-index: 3; */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  color: white;
  box-sizing: border-box;
  overflow: hidden;
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
  overflow: hidden;
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
  position: absolute;
  bottom: 10px;
  left: 10px;
  /* position: sticky;
  top: 10px; */
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
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  color: ${(props) => props.color};
  margin: ${(props) => props.margin};
`;

const ButtonArea = styled.div`
  width: 100vw;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  margin: 0 10 0 10px;
  z-index: 10;
  background-color: ${colors.BG_grey};
`;

const PostArea = styled.div`
  display: flex;
  width: 100vw;
  height: 100%;
  /* overflow: hidden; */
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  margin: 0px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
  z-index: 10;
`;

const ProfileArea = styled.div`
  display: flex;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
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

const PostContentArea = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
  box-sizing: border-box;

  /* white-space: nowrap; */
  /* text-overflow: ellipsis; */

  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: "Rg";
  padding: 0px 10px;
  margin: 10px 0px 20px 0px;

  transition: height ease 0.7s;
`;
const Line = styled.div`
  width: 95vw;
  height: 1px;
  background-color: ${colors.Button_deactive};
`;
const ContentInput = styled.textarea`
  width: 100%;
  height: 100%;
  padding: 10px;
  background-color: ${colors.BG_grey};
  font-size: 15px;
  border: 0;
  outline: none;
  color: ${colors.Font_black};
`;

interface AlbumPost {
  albumArtist: {
    artistId: string;
    imageUrl: string;
    name: string;
  };
  albumId: string;
  imageUrl: string;
  name: string;
  total_tracks: number;
  release_date: string;
}

function AlbumPostEditPage() {
  const [albumPost, setAlbumPost] = useState<AlbumPost | null>(null);
  //   const [postContent, setPostContent] = useState("내용을 입력해주세요");
  //   const { email, setEmail, name, setName, id, setId } = useStore();
  const location = useLocation();
  const [postContent, setPostContent] = useState("");
  const server = "http://203.255.81.70:8030";
  const reissueTokenUrl = `${server}/api/auth/reissued`;

  useEffect(() => {
    if (!location.state) {
      setAlbumPost(null);
    } else {
      setAlbumPost(location.state);
      console.log(location.state);
    }
  }, [location.state]);

  //   const albumPost = {
  //     postId: "post01",
  //     content: "",
  //     createAt: "",
  //     updateAt: "",
  //     author: {
  //       id: 1,
  //       username: name,
  //       profilePicture:
  //         "https://i.namu.wiki/i/-s0neKOBTEboNgx8tbXrz2ZQ-qt4S4rfX0ztS1mk2bqYPdI5ALlatQok3HoAvRq30J79s9xv_5J7N4MSEdt6Nw.webp",
  //     },
  //     album: {
  //       id: 12,
  //       title: "1989",
  //       albumCover: "https://i.scdn.co/image/ab67616d00001e0252b2a3824413eefe9e33817a",
  //       artistName: "taylor swift",
  //       genre: "RnB",
  //     },
  //   };

  const navigate = useNavigate();
  const GoToSearchPage = () => {
    navigate("/SearchPage");
  };
  const GoToHomePage = () => {
    navigate("/HomePage");
  };

  const fetchPost = async () => {
    const token = localStorage.getItem("login-token");
    const refreshToken = localStorage.getItem("login-refreshToken");
    let PostUrl = `${server}/api/album/post`;
    if (token && albumPost) {
      try {
        console.log(`Posting...`);
        console.log(albumPost);
        const response = await fetch(PostUrl, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            content: postContent,
            title: albumPost.name,
            albumId: albumPost.albumId,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          console.log("Post Success");
          console.log(data);
        } else if (response.status === 401) {
          console.log("reissuing Token");
          const reissueToken = await fetch(reissueTokenUrl, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Refresh-Token": `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem("login-token", data.token);
          localStorage.setItem("login-refreshToken", data.refreshToken);
          fetchPost();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        console.log("finished");
      }
    }
  };

  return (
    <Container>
      <AlbumPostArea>
        {/*  TODO: 스크롤 되다가 일부 남기고 멈추기 */}
        {albumPost && (
          <AlbumTitleArea
            onClick={() => {
              GoToSearchPage();
            }}
          >
            <ImageArea>
              <img
                src={albumPost.imageUrl}
                width="100%"
                object-fit="cover"
                // z-index="1"
              ></img>
            </ImageArea>
            <GradientBG> </GradientBG>
            <TitleTextArea>
              <Text fontFamily="Bd" fontSize="40px" margin="0px" color={colors.BG_white}>
                {albumPost.name}
              </Text>
              <Text fontFamily="Rg" fontSize="20px" margin="0px 0px 2px 10px" color={colors.BG_white}>
                {albumPost.albumArtist.name}
              </Text>
            </TitleTextArea>
          </AlbumTitleArea>
        )}
        {!albumPost && (
          <AlbumTitleArea
            onClick={() => {
              GoToSearchPage();
            }}
          >
            <GradientBG> </GradientBG>
            <TitleTextArea>
              <Text fontFamily="Bd" fontSize="40px" margin="0px" color="white">
                앨범을 선택해주세요
              </Text>
            </TitleTextArea>
          </AlbumTitleArea>
        )}
        <ButtonArea>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black}>
            취소
          </Text>
          <Text fontFamily="Bd" fontSize="20px" margin="0px" color={colors.Font_black}>
            Album Post
          </Text>
          <Text
            fontFamily="Rg"
            fontSize="15px"
            margin="0px 10px 0px 0px"
            color={colors.Font_black}
            onClick={fetchPost}
          >
            저장
          </Text>
        </ButtonArea>
        <Line></Line>
        <PostArea>
          {/* <ProfileArea>
            <ProfileTextArea>
              <ProfileName>{name}</ProfileName>
            </ProfileTextArea>
          </ProfileArea> */}
          <PostContentArea>
            <form>
              <ContentInput
                placeholder="앨범에 대한 자유로운 감상평을 남겨주세요."
                onChange={(e) => setPostContent(e.target.value)}
              ></ContentInput>
            </form>
          </PostContentArea>
          <ButtonArea></ButtonArea>
        </PostArea>
      </AlbumPostArea>
    </Container>
  );
}

export default AlbumPostEditPage;
