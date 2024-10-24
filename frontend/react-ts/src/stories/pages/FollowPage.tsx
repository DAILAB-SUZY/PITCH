import styled from 'styled-components';
import logo from '../../img/logo_withText.png';
import { colors } from '../../styles/color';
import { useEffect, useRef, useState } from 'react';
import FollowBox from '../components/FollowBox';
import { useNavigate, useLocation } from 'react-router-dom';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: ${colors.BG_white};
`;

const Header = styled.div`
  height: 10vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: ${colors.BG_grey};
`;

const HeaderTitle = styled.div`
  height: 50%;
  width: 50vw;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const HeaderBackBtn = styled.div`
  height: 5vh;
  width: 10vw;
  position: absolute;
  top: 0px;
  left: 0px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Body = styled.div`
  height: 95vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  align-items: center;
  justify-content: start;
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
  height: 50%;
  width: 100%;
`;

const TabBtn = styled.div<{ color: string; border: string }>`
  border-bottom: ${props => props.border} solid grey;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-family: 'Bd';
  width: 50%;
  height: 40px;
  color: ${props => props.color};
`;

function FollowPage() {
  const [tabBtn, setTabBtn] = useState(1);
  const navigate = useNavigate();
  const location = useLocation();
  const { userId, userName, followings, followers } = location.state || { userId: 0, userName: '', followings: [], followers: [] };
  const GoToMusicProfilePage = () => {
    navigate('/MusicProfilePage', { state: userId });
  };

  return (
    <Container>
      <Header>
        <HeaderBackBtn onClick={() => GoToMusicProfilePage()}>
          <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill={colors.Font_black} viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0" />
          </svg>
        </HeaderBackBtn>
        <HeaderTitle>
          <Title>{userName}</Title>
        </HeaderTitle>
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
      </Header>
      <Body>
        {tabBtn === 1
          ? followers.map((follow: any) => {
              return <FollowBox name={follow.username} profile={follow.profilePicture} userId={follow.userId}></FollowBox>;
            })
          : followings.map((follow: any) => {
              return <FollowBox name={follow.username} profile={follow.profilePicture} userId={follow.userId}></FollowBox>;
            })}
      </Body>
    </Container>
  );
}

export default FollowPage;
