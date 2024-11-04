import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
import FollowBox from '../components/FollowBox';
import { useNavigate, useLocation } from 'react-router-dom';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: ${colors.BG_white};
`;

const Header = styled.div`
  height: 60px;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  background-color: ${colors.BG_grey};
`;

const HeaderTitle = styled.div`
  height: 100%;
  width: auto;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
`;

const HeaderBtn = styled.div`
  height: 100%;
  width: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
`;

const Body = styled.div`
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: scroll;
  align-items: center;
  justify-content: flex-start;
`;

const Title = styled.div`
  font-family: 'Rg';
  font-size: 22px;
  color: ${colors.Font_black};
`;

const TabBar = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  height: 50px;
  width: 100%;
  background-color: ${colors.BG_white};
`;

const TabBtn = styled.div<{ color: string; border: string }>`
  border-bottom: ${props => props.border} solid grey;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-family: 'Bd';
  width: 50%;
  height: 50px;
  color: ${props => props.color};
`;

interface FollowData {
  userId: number;
  username: string;
  profilePicture: string;
}

function FollowPage() {
  const [tabBtn, setTabBtn] = useState(1);
  const navigate = useNavigate();
  const location = useLocation();
  const [followers, setFollowers] = useState<FollowData[]>([]);
  const [followings, setFollowings] = useState<FollowData[]>([]);

  const { userId, userName } = location.state;
  const GoToMusicProfilePage = () => {
    navigate('/MusicProfilePage', { state: userId });
  };

  const GoToFriendSearchPage = () => {
    navigate('/FriendSearchPage');
  };

  useEffect(() => {
    fetchFollow();
  }, []);

  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));
  const GetFollowerURL = `${server}/api/user/${userId}/follower`;
  const GetFollowingURL = `${server}/api/user/${userId}/following`;
  const SetFollowUrl = `${server}/api/user/follow/`;
  const fetchFollow = async () => {
    if (token) {
      console.log('fetching Like Data');
      try {
        const response = await fetch(GetFollowerURL, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const data = await response.json();
          setFollowers(data);
        } else if (response.status === 401) {
          await ReissueToken();
          fetchFollow();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Fetched Follower', error);
      }
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
      } else {
        console.error('failed to reissue token', response.status);
      }
    } catch (error) {
      console.error('Refresh Token 재발급 실패', error);
    }
  };

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

  return (
    <Container>
      <Header>
        <HeaderBtn onClick={() => GoToMusicProfilePage()}>
          <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill={colors.Font_black} viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0" />
          </svg>
        </HeaderBtn>
        <HeaderTitle>
          <Title>{userName}</Title>
        </HeaderTitle>
        <HeaderBtn
          onClick={() => {
            GoToFriendSearchPage();
          }}
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill={colors.Font_black} className="bi bi-person-plus" viewBox="0 0 16 16">
            <path d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0m4 8c0 1-1 1-1 1H1s-1 0-1-1 1-4 6-4 6 3 6 4m-1-.004c-.001-.246-.154-.986-.832-1.664C9.516 10.68 8.289 10 6 10s-3.516.68-4.168 1.332c-.678.678-.83 1.418-.832 1.664z" />
            <path fill-rule="evenodd" d="M13.5 5a.5.5 0 0 1 .5.5V7h1.5a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0V8h-1.5a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5" />
          </svg>
        </HeaderBtn>
      </Header>
      <Body>
        <TabBar>
          <TabBtn border={tabBtn === 1 ? '1px' : '0px'} color={tabBtn === 1 ? colors.Font_black : 'grey'} onClick={() => setTabBtn(1)}>
            팔로워
          </TabBtn>
          <TabBtn
            border={tabBtn === 2 ? '1px' : '0px'}
            color={tabBtn === 2 ? colors.Font_black : 'grey'}
            onClick={() => {
              setTabBtn(2);
            }}
          >
            팔로잉
          </TabBtn>
        </TabBar>
        {tabBtn === 1 && followers && followings
          ? followers.map((follow: any) => {
              return (
                <FollowBox
                  name={follow.username}
                  profile={follow.profilePicture}
                  userId={follow.userId}
                  ChangeFollow={ChangeFollow}
                  isFollowing={followings.some(user => user.userId === follow.userId) ? true : false}
                ></FollowBox>
              );
            })
          : followings.map((follow: any) => {
              return (
                <FollowBox
                  name={follow.username}
                  profile={follow.profilePicture}
                  userId={follow.userId}
                  ChangeFollow={ChangeFollow}
                  isFollowing={followings.some(user => user.userId === follow.userId) ? true : false}
                ></FollowBox>
              );
            })}
      </Body>
    </Container>
  );
}

export default FollowPage;
