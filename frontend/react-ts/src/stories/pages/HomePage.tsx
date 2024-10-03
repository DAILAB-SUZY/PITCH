import styled from "styled-components";
import { colors } from "../../styles/color";
import profile from "../../img/happy.webp";
import cover1 from "../../img/aespa.webp";
import cover2 from "../../img/newjeans.png";
import cover3 from "../../img/daftpunk.png";
import cover4 from "../../img/weeknd.jpg";
import cover5 from "../../img/oasis.jpeg";

import Nav from "../components/Nav";
import AlbumPostCard from "../components/AlbumPostCard";
import PlaylistPreviewCard from "../components/PlaylistPreviewCard";
import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { userInfo } from "os";
import useStore from "../store/store";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh; //auto;
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
  margin-top: 110px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const PlaylistArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: column;
  background-color: white;
`;

const AlbumPostArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: column;
  background-color: white;
`;

const AlbumPostTitleArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-end;
  gap: 8px;
  width: 100vw;
  height: auto;
`;

const RowAlignArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: white;
  overscroll-behavior: none;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

const Text = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Rg";
`;

function HomePage() {
  interface FriendsPlayList {
    playlistId: number;
    albumCover: string[];
    author: {
      id: number;
      username: string;
      profilePicture: string;
    };
  }
  // const playlists = [
  //   {
  //     id: 1,
  //     userName: "junho1231",
  //     profileImage: profile,
  //     albumCovers: [cover1, cover4, cover2],
  //     bgColor: "#b5a1f7",
  //   },
  //   {
  //     id: 2,
  //     userName: "hello123",
  //     profileImage: profile,
  //     albumCovers: [cover4, cover5, cover3],
  //     bgColor: "#f7d5a1",
  //   },
  //   {
  //     id: 3,
  //     userName: "junho1231",
  //     profileImage: profile,
  //     albumCovers: [cover3, cover2, cover1],
  //     bgColor: "#b8f7a1",
  //   },
  //   {
  //     id: 4,
  //     userName: "hello123",
  //     profileImage: profile,
  //     albumCovers: [cover3, cover4, cover5],
  //     bgColor: "#f7a1e7",
  //   },
  // ];

  const { email, setEmail, name, setName, id, setId } = useStore();

  const navigate = useNavigate();
  const GoToAlbumPostEditPage = () => {
    navigate("/AlbumPostEditPage");
  };

  console.log(`${email} / ${name} / ${id}`);
  const [albumPostList, setAlbumPostList] = useState<AlbumPost[]>([]);
  const [postPage, setPostPage] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [isEnd, setIsEnd] = useState(false);
  const [friendsPlayList, setfriendsPlayList] = useState<FriendsPlayList[]>([]);

  console.log("render-----------------------------");
  console.log(`albumpost: `, albumPostList);
  console.log(`postPage: `, postPage);

  const server = "http://203.255.81.70:8030";

  const reissueTokenUrl = `${server}/api/auth/reissued`;

  // Intersection Observer용 ref
  const observerRef = useRef<HTMLDivElement | null>(null);
  const token = localStorage.getItem("login-token");
  const refreshToken = localStorage.getItem("login-refreshToken");
  interface AlbumPost {
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
  const PlaylistUrl = `${server}/api/FollowerPlaylist/user/1`;
  // TODO: playlist fetching
  const fetchPlaylist = async () => {
    if (token) {
      try {
        console.log(`fetching Playlist...`);
        const response = await fetch(PlaylistUrl, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setfriendsPlayList((prevList) => [...prevList, ...data]);
          console.log("fetched Playlist :");
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
          fetchPlaylist();
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

  // 무한 스크롤 데이터를 가져오는 함수
  const fetchAlbumPosts = async () => {
    console.log("start fetching");
    console.log(`isLoading: ${isLoading}`);
    let albumPostUrl = `${server}/api/album/post?page=${postPage}&limit=5`;
    console.log(albumPostUrl);
    if (token && !isLoading && !isEnd) {
      setIsLoading((loading) => !loading);
      console.log(`fetching ${postPage} page Album Posts...`);
      try {
        console.log("fetching...");
        const response = await fetch(albumPostUrl, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        console.log("fetching...complete");

        if (response.ok) {
          console.log("set PostList");
          const data: AlbumPost[] = await response.json();
          console.log(data);
          if (data.length === 0) {
            console.log("list End");
            setIsEnd(true);
          }
          setAlbumPostList((prevList) => [...prevList, ...data]); // 기존 데이터에 새로운 데이터를 추가
          setPostPage((prevPage) => prevPage + 1); // 페이지 증가
          console.log("albumpost ", albumPostList);
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
          fetchAlbumPosts();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        setIsLoading(false); // 로딩 상태 해제
        console.log("fetching Complete: ", albumPostList);
        console.log(`postPage : ${postPage}`);
      }
    }
  };

  useEffect(() => {
    fetchPlaylist();
  }, []);

  // Intersection Observer 설정
  useEffect(() => {
    const observer = new IntersectionObserver((entries) => {
      // observerRef가 화면에 보이면 fetch 호출
      if (!isLoading && entries[0].isIntersecting) {
        // setPostPage((prevPage) => prevPage + 1); // 페이지 증가
        // console.log("page++ => ", postPage);
        fetchAlbumPosts();
      }
    });

    if (observerRef.current) {
      console.log("current: ", observerRef.current);
      observer.observe(observerRef.current); // ref가 있는 요소를 관찰 시작
    }

    return () => {
      console.log("start clean");
      if (observerRef.current) {
        console.log("clean");
        observer.unobserve(observerRef.current); // 컴포넌트 언마운트 시 관찰 해제
      }
    };
  }, [postPage]);

  // useEffect(() => {
  //   console.log("effect");
  //   if (!isLoading) {
  //     console.log("fetch call");
  //     fetchAlbumPosts();
  //   }
  // }, [postPage]);

  // useEffect(() => {
  //   const fetchData = async () => {
  //     const token = localStorage.getItem("login-token");
  //     const refreshToken = localStorage.getItem("login-refreshToken");
  //     if (token) {
  //       try {
  //         const response = await fetch(HomeUrl, {
  //           method: "GET",
  //           headers: {
  //             "Content-Type": "application/json",
  //             Authorization: `Bearer ${token}`,
  //           },
  //         });

  //         // fetch 성공하면 데이터 json 변환 후 저장
  //         if (response.ok) {
  //           const data = await response.json();
  //           setAlbumPostList(data);
  //           console.log("Fetched data:", albumPostList);
  //         }
  //         // token 만료 시 refreshToken으로 재발급 후 다시 fetch 시도
  //         else if (response.status === 401) {
  //           try {
  //             const reissueToken = await fetch(reissueTokenUrl, {
  //               method: "POST",
  //               headers: {
  //                 "Content-Type": "application/json",
  //                 "Refresh-Token": `${refreshToken}`,
  //               },
  //             });
  //             const data = await reissueToken.json();
  //             console.log("Token reissued");
  //             localStorage.setItem("login-token", data.token);
  //             localStorage.setItem("login-refreshToken", data.refreshToken);
  //             fetchData();
  //           } catch (error) {
  //             console.error("Failed to reissueToken:", response.status);
  //           }
  //         }
  //         // fetch 실패 시 에러 코드
  //         else {
  //           console.error("Failed to fetch data:", response.status);
  //         }
  //       } catch (error) {
  //         console.error("Error fetching the JSON file:", error);
  //       }
  //     } else {
  //       console.log("No token found");
  //     }
  //   };
  //   fetchData();
  // }, []);

  return (
    <Container>
      <Header>
        <Nav page={1}></Nav>
      </Header>
      <Body>
        <PlaylistArea>
          <Title fontSize="22px" margin="20px 0px 0px 20px">
            Friend's Playlist
          </Title>
          <PlaylistPreviewCard playlists={friendsPlayList}></PlaylistPreviewCard>
        </PlaylistArea>
        <AlbumPostArea>
          <AlbumPostTitleArea>
            <Title fontSize="22px" margin="20px 0px 0px 20px">
              Album Post
            </Title>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="22"
              height="22"
              fill="currentColor"
              className="bi bi-pencil-square"
              viewBox="0 0 16 16"
              //  TODO: 댓글 작성 기능 구현
              onClick={() => {
                GoToAlbumPostEditPage();
              }}
            >
              <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
              <path
                fill-rule="evenodd"
                d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
              />
            </svg>
          </AlbumPostTitleArea>
          <RowAlignArea>
            {albumPostList.length > 0 ? (
              albumPostList.map((albumPost) => (
                <AlbumPostCard key={albumPost.postId} albumPost={albumPost}></AlbumPostCard>
              ))
            ) : (
              <Text fontSize="15px" margin="150px 0px 0px 0px">
                게시물이 없습니다
              </Text>
            )}
          </RowAlignArea>
        </AlbumPostArea>
        {/* TODO: 무한 스크롤 구현 */}
        {/* Loading Indicator */}
        {isLoading ? (
          <Text fontSize="16px" margin="20px 0px">
            로딩 중...
          </Text>
        ) : (
          <div ref={observerRef} style={{ height: "100px", backgroundColor: "transparent" }} />
        )}
      </Body>
    </Container>
  );
}

export default HomePage;
