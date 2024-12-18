import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
import Loader from '../components/Loader';
import FollowBox from '../components/FollowBox';
import useStore from '../store/store';
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
  const [searchResult, setSearchResult] = useState<UserData[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));
  const { id } = useStore();
  console.log('기존 배열');
  console.log(searchResult);

  // const { email, setEmail, name, setName, id, setId } = useStore();

  // const navigate = useNavigate();

  const server = 'http://203.255.81.70:8030';

  useEffect(() => {
    fetchFollow();
  }, []);
  const reissueTokenUrl = `${server}/api/auth/reissued`;

  const SetFollowUrl = `${server}/api/user/follow/`;
  const ChangeFollow = async (user: number) => {
    if (token) {
      try {
        console.log('fetching...');
        const response = await fetch(SetFollowUrl + `${user}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log('following complete');
          const data = await response.json();
          console.log(data);
        } else if (response.status === 401) {
          ReissueToken();
          ChangeFollow(user);
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
        fetchFollow();
      }
    }
  };
  const [followings, setFollowings] = useState<FollowData[]>([]);

  const GetFollowingURL = `${server}/api/user/${id}/following`;
  const fetchFollow = async () => {
    if (token) {
      console.log('fetching Like Data');
      try {
        const response = await fetch(GetFollowingURL, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setFollowings(data);
        } else if (response.status === 401) {
          await ReissueToken();
          fetchFollow();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Fetched Follower', error);
      }
    }
  };

  const fetchSearch = async () => {
    let searchUrl = `${server}/api/search/user/${searchKeyword}`;

    if (token && !isLoading) {
      setIsLoading(true);
      try {
        console.log(`searching ${searchKeyword}...`);
        const response = await fetch(searchUrl, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setSearchResult(data);
        } else if (response.status === 401) {
          ReissueToken();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        setIsLoading(false);
        console.log('finished');
      }
    }
  };
  const ReissueToken = async () => {
    console.log('reissuing Token');
    try {
      const response = await fetch(reissueTokenUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Refresh-Token': `${refreshToken}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('login-token', data.token);
        localStorage.setItem('login-refreshToken', data.refreshToken);
        setToken(data.token);
        setRefreshToken(data.refreshToken);
        fetchSearch();
      } else {
        throw new Error(`failed to reissue token : ${response.status}`);
      }
    } catch (error) {
      console.error(error);
    }
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
            ) : (
              <Text fontFamily="Rg" fontSize="17px" margin="10px 0px 0px 0px" color={colors.Font_black}>
                없는 사용자 입니다.
              </Text>
            ))}
          {isLoading && <Loader></Loader>}
        </SearchResultArea>
      </AlbumPostArea>
    </Container>
  );
}

export default FriendSearchPage;
