import styled from "styled-components";
import { colors } from "../../styles/color";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import useStore from "../store/store";

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
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
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

const Line = styled.div`
  width: 95vw;
  height: 1px;
  background-color: ${colors.Button_deactive};
`;

const SearchArea = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: auto;
  margin: 10px;
  background-color: ${colors.BG_grey};
`;

const ContentInput = styled.input`
  width: 90vw;
  height: 30px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.InputBox};
  font-size: 15px;
  border: 0;
  border-radius: 7px;
  outline: none;
  color: ${colors.Font_black};

  &::placeholder {
    opacity: 0.7;
  }
`;

const SearchResultArea = styled.div`
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

const SongArea = styled.div`
  width: 100%;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: start;
  flex-direction: row;
  margin: 10px 0px 10px 0px;
`;

const AlbumCover = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 8px;
  background-color: black;
  margin: 10px;
  overflow: hidden;
`;

const SongTextArea = styled.div`
  height: 80%;
  display: flex;
  align-items: start;
  justify-content: space-between;
  flex-direction: column;
`;

const Title = styled.div<{ fontSize?: string; margin?: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
  color: white;
`;

interface SearchResult {
  postId: number;
  content: string;
  createAt: number;
  updateAt: number;
  author: {
    id: number;
    username: string;
    profilePicture: string;
  };
  album: {
    id: number;
    title: string;
    albumCover: string;
    artistName: string;
    genre: string;
  };
  comments: [
    {
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
    },
  ];
  likes: [
    {
      id: number;
      username: string;
      profilePicture: string;
    },
  ];
}

function SearchPage() {
  //   const [albumPost, setAlbumPost] = useState<AlbumPost | null>(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const { email, setEmail, name, setName, id, setId } = useStore();

  let albumPost = {
    postId: "post01",
    content: "",
    createAt: "",
    updateAt: "",
    author: {
      id: 1,
      username: name,
      profilePicture:
        "https://i.namu.wiki/i/-s0neKOBTEboNgx8tbXrz2ZQ-qt4S4rfX0ztS1mk2bqYPdI5ALlatQok3HoAvRq30J79s9xv_5J7N4MSEdt6Nw.webp",
    },
    album: {
      id: 12,
      title: "1989",
      albumCover: "https://i.scdn.co/image/ab67616d00001e0252b2a3824413eefe9e33817a",
      artistName: "taylor swift",
      genre: "RnB",
    },
  };

  const navigate = useNavigate();
  const GoToSearchPage = () => {
    navigate("/Signup");
  };

  const server = "http://203.255.81.70:8030";
  let searchUrl = `${server}/api/searchSpotify/${searchKeyword}`;
  const reissueTokenUrl = `${server}/api/auth/reissued`;

  const [isLoading, setIsLoading] = useState(false);
  const [searchResult, setSearchResult] = useState<SearchResult[]>();

  const fetchSearch = async () => {
    const token = localStorage.getItem("login-token");
    const refreshToken = localStorage.getItem("login-refreshToken");

    if (token && !isLoading) {
      setIsLoading(true);
      try {
        console.log(`searching ${searchKeyword}...`);
        const response = await fetch(searchUrl, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setSearchResult(data);
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
          fetchSearch();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        setIsLoading(false);
        console.log("finished");
      }
    }
  };

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    fetchSearch(); // 검색 실행
  };

  return (
    <Container>
      <AlbumPostArea>
        <ButtonArea>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black}>
            취소
          </Text>
          <Text fontFamily="Bd" fontSize="20px" margin="0px" color={colors.Font_black}>
            search
          </Text>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 10px 0px 0px" color={colors.Font_black}>
            저장
          </Text>
        </ButtonArea>
        <Line></Line>
        <SearchArea>
          <form onSubmit={handleSearchSubmit}>
            <ContentInput
              placeholder="앨범의 제목을 입력하세요"
              onChange={(e) => setSearchKeyword(e.target.value)}
            ></ContentInput>
          </form>
        </SearchArea>
        {!isLoading &&
          searchResult &&
          searchResult.map((song: any) => (
            <SongArea>
              <AlbumCover>
                <img src={song.imageLUrl} width="100%" height="100%"></img>
              </AlbumCover>
              <SongTextArea>
                <Title fontSize={"20px"}>{song.name}</Title>
                <Title fontSize={"15px"}>{song.albumArist.name}</Title>
              </SongTextArea>
            </SongArea>
          ))}
        <SearchResultArea></SearchResultArea>
      </AlbumPostArea>
    </Container>
  );
}

export default SearchPage;
