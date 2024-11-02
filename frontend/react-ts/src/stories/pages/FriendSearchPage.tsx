import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
import Loader from '../components/Loader';
import FollowBox from '../components/FollowBox';
import useStore from '../store/store';
import { fetchGET, fetchPOST, MAX_REISSUE_COUNT } from '../utils/fetchData';
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

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  color: string;
  margin: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
  margin: ${props => props.margin};
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
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 30px;
  margin: 10px;
  background-color: ${colors.BG_grey};
  overflow: scroll;
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
  height: 600px;
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

interface FollowData {
  userId: number;
  username: string;
  profilePicture: string;
}

interface UserDNA {
  dnaKey: number;
  dnaName: string;
}

interface UserProfile {
  id: number;
  username: string;
  profilePicture: string;
  dnas: UserDNA[];
}

interface FollowerFollowing {
  userId: number;
  username: string;
  profilePicture: string;
}

interface UserData {
  user: UserProfile;
  followings: FollowerFollowing[];
  followers: FollowerFollowing[];
}

function FriendSearchPage() {
  const [searchKeyword, setSearchKeyword] = useState('');
  const [searchStarted, setSearchStarted] = useState(false);
  const [searchResult, setSearchResult] = useState<UserData[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const { id } = useStore();
  console.log('기존 배열');
  console.log(searchResult);

  useEffect(() => {
    fetchFollow();
  }, []);

  const SetFollowUrl = `/api/user/follow/`;
  const ChangeFollow = async (user: number) => {
    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    fetchPOST(token, refreshToken, `${SetFollowUrl}${user}`, {}, MAX_REISSUE_COUNT).then(() => fetchFollow());
  };

  const [followings, setFollowings] = useState<FollowData[]>([]);

  const GetFollowingURL = `/api/user/${id}/following`;
  const fetchFollow = async () => {
    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    fetchGET(token, refreshToken, GetFollowingURL, MAX_REISSUE_COUNT).then(data => {
      setFollowings(data);
    });
  };

  const fetchSearch = async () => {
    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    setIsLoading(true);
    fetchGET(token, refreshToken, `/api/search/user/${searchKeyword}`, MAX_REISSUE_COUNT).then(data => {
      setSearchStarted(true);
      setSearchResult(data);
      setIsLoading(false);
    });
  };

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    setSearchResult([]); // 검색 결과 초기화
    fetchSearch(); // 검색 실행
  };

  return (
    <Container>
      <AlbumPostArea>
        <ButtonArea>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black}></Text>
          <Text fontFamily="Bd" fontSize="20px" margin="0px" color={colors.Font_black}>
            search friends
          </Text>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 10px 0px 0px" color={colors.BG_grey}></Text>
        </ButtonArea>
        <Line></Line>
        <SearchArea>
          <form onSubmit={handleSearchSubmit}>
            <ContentInput placeholder="검색할 이름을 입력하세요" onChange={e => setSearchKeyword(e.target.value)}></ContentInput>
          </form>
        </SearchArea>

        <SearchResultArea>
          {!isLoading &&
            (searchResult.length !== 0 ? (
              searchResult.map((follow: UserData) => {
                return (
                  <FollowBox
                    name={follow.user.username}
                    profile={follow.user.profilePicture}
                    userId={follow.user.id}
                    ChangeFollow={ChangeFollow}
                    isFollowing={followings.some(user => user.userId === follow.user.id) ? true : false}
                  ></FollowBox>
                );
              })
            ) : searchStarted ? (
              <Text fontFamily="Rg" fontSize="17px" margin="10px 0px 0px 0px" color={colors.Font_black}>
                없는 사용자 입니다.
              </Text>
            ) : (
              <Text fontFamily="Rg" fontSize="17px" margin="10px 0px 0px 0px" color={colors.Font_black}>
                사용자를 검색해주세요.
              </Text>
            ))}
          {isLoading && <Loader></Loader>}
        </SearchResultArea>
      </AlbumPostArea>
    </Container>
  );
}

export default FriendSearchPage;
