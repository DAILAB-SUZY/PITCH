import styled from 'styled-components';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import MostCommentedCard from '../components/MostCommentedCard';
import MostLikedCard from '../components/MostLikedCard';
import { useNavigate } from 'react-router-dom';
import cover from '../../img/Blonde.webp';
import artistProfile from '../../img/frankOcean.jpg';
import cover2 from '../../img/aespa2.jpg';
import artistProfile2 from '../../img//aespaProfile.jpg';
import { useEffect, useState } from 'react';
import Loader from '../components/Loader';

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
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const CommentCard = styled.div`
  position: relative;
  display: flex;
  width: 100vw;
  aspect-ratio: 1 / 1; /* 1:1 비율 유지 */
  border-radius: 0px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  /* box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px; */
  overflow: hidden;
`;

const ImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const CoverImageArea = styled.div`
  position: relative;
  /* top: 0px;
  left: 0px; */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 3;
`;

const CoverImage = styled.div`
  /* position: absolute;
  top: 0px;
  left: 0px; */
  overflow: hidden;
  width: 200px;
  height: 200px;
  object-fit: cover;
  z-index: 4;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(0deg, rgba(255, 255, 255, 1) 0%, rgba(255, 255, 255, 0) 80%);
  backdrop-filter: blur(0px);
`;

const AlbumInfoArea = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 0px 20px 20px 20px;
  box-sizing: border-box;
  z-index: 3;
`;

const AlbumTitleArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  height: auto;
`;

const LikeNumberArea = styled.div`
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: center;
  width: 100%;
  height: 40px;
`;

const ContentArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
`;

const Line = styled.div`
  width: 320px;
  height: 1px;
  background-color: ${colors.Button_deactive};
`;

const Title = styled.div<{ fontSize: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Bd';

  width: 100%;
  box-sizing: border-box;
  padding: 10px 10px 0px 30px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;

  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
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

interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
}

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

interface AlbumChatComment {
  albumChatCommentId: number;
  content: string;
  createAt: string; // ISO date string
  updateAt: string; // ISO date string
  likes: User[];
  comments: AlbumChatComment[]; // Recursive structure
  author: User;
}

interface AlbumLike {
  id: number;
  username: string;
  profilePicture: string;
  dnas: DNA[];
}

interface AlbumData {
  albumDetail: AlbumDetail;
  comments: AlbumChatComment[];
  albumLike: AlbumLike[];
}

interface AlbumSearchResult {
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

function AlbumHomePage() {
  const navigate = useNavigate();
  const [MostCommentedAlbumList, setMostCommentedAlbumList] = useState<AlbumData[] | undefined>();
  const [MostLikedAlbumList, setMostLikedAlbumList] = useState<AlbumData[] | undefined>();

  const [isLoading, setIsLoading] = useState(false);

  // useEffect(() => {
  //   fetchMostCommentedData();
  //   fetchMostLikedData();
  // }, []);

  const server = 'http://203.255.81.70:8030';
  let MostCommentedDataUrl = `${server}/api/albumchat/chat`;
  let MostLikedDataUrl = `${server}/api/albumchat/like?page=0&limit=5`;
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const token = localStorage.getItem('login-token');
  const refreshToken = localStorage.getItem('login-refreshToken');

  // const fetchSearch = async () => {
  //   if (token && !isLoading) {
  //     setIsLoading(true);
  //     console.log('검색시작');
  //     Search(AlbumSearchUrl);
  //   }
  // };
  // const Search = async (URL: string) => {
  //   console.log('searching...');
  //   try {
  //     console.log(`searching Album : ${searchKeyword}...`);
  //     const response = await fetch(URL, {
  //       method: 'GET',
  //       headers: {
  //         'Content-Type': 'application/json',
  //         Authorization: `Bearer ${token}`,
  //       },
  //     });
  //     if (response.ok) {
  //       const data = await response.json();
  //       setSearchResultTrack(data);
  //       console.log(data);
  //       setIsSearchMode(true);
  //     } else if (response.status === 401) {
  //       ReissueToken();
  //       fetchSearch();
  //     } else {
  //       console.error('Failed to fetch data:', response.status);
  //     }
  //   } catch (error) {
  //     console.error('Error fetching the JSON file:', error);
  //   } finally {
  //     setIsLoading(false);
  //     console.log('finished');
  //   }
  // };
  // const ReissueToken = async () => {
  //   console.log('reissuing Token');
  //   const reissueToken = await fetch(reissueTokenUrl, {
  //     method: 'POST',
  //     headers: {
  //       'Content-Type': 'application/json',
  //       'Refresh-Token': `${refreshToken}`,
  //     },
  //   });
  //   const data = await reissueToken.json();
  //   localStorage.setItem('login-token', data.token);
  //   localStorage.setItem('login-refreshToken', data.refreshToken);
  // };

  return (
    <Container>
      <Header>
        <Nav page={3}></Nav>
      </Header>
      <Body>
        {!isLoading && (
          <>
            <CommentCard>
              <ImageArea>
                <img src={cover2} width="120%" height="120%" object-fit="cover"></img>
              </ImageArea>
              <GradientBG> </GradientBG>
              <CoverImageArea>
                <CoverImage>
                  <img src={cover2} width="100%" height="100%" object-fit="cover"></img>
                </CoverImage>
              </CoverImageArea>
            </CommentCard>
            <AlbumInfoArea>
              <AlbumTitleArea>
                <Text fontSize="30px" margin="0px 0px 5px 0px" fontFamily="Bd" color={colors.Font_black}>
                  Girls
                </Text>
                <Text fontSize="20px" margin="0px 0px 5px 0px" fontFamily="Bd" opacity={1} color={colors.Font_black}>
                  aespa
                </Text>
                <Text fontSize="12px" margin="0px" fontFamily="Bd" opacity={0.5} color={colors.Font_black}>
                  K-Pop. 2022.
                </Text>
              </AlbumTitleArea>
              <LikeNumberArea>
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill={colors.Button_active} className="bi bi-heart-fill" viewBox="0 0 16 16">
                  <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
                </svg>
                <Text fontSize="15px" margin="0px 0px 0px 10px" fontFamily="Bd" color={colors.Font_black}>
                  123
                </Text>
                <Text fontSize="15px" margin="0px 0px 0px 5px" fontFamily="Rg" color={colors.Font_black}>
                  likes
                </Text>
              </LikeNumberArea>
            </AlbumInfoArea>
            <Line></Line>
            <ContentArea>
              <Title fontSize="30px" margin="0px 0px 0px 0px">
                Chats
              </Title>
            </ContentArea>
          </>
        )}
        {isLoading ? <Loader></Loader> : null}
      </Body>
    </Container>
  );
}

export default AlbumHomePage;
