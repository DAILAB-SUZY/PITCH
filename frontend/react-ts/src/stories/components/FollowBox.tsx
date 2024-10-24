import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useRef, useState } from 'react';

const Container = styled.div`
  width: 95vw;
  height: 80px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

const LeftItems = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
`;

const NameContainer = styled.div`
  display: flex;
  flex-direction: column;
  font-size: 20px;
  color: ${colors.Font_black};
  width: auto;
  height: 100%;
  margin-left: 10px;
`;

const Circle = styled.div<{ bgcolor?: string }>`
  border: 1px solid ${colors.BG_lightgrey};
  width: 65px;
  height: 65px;
  border-radius: 100%;
  overflow: hidden;
  margin-right: 10px;
  background-color: ${props => props.bgcolor};
  object-fit: cover;
`;

const Btn = styled.div<{ bgcolor: string }>`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-evenly;
  background-color: ${props => props.bgcolor};
  color: ${colors.Font_black};
  width: 100px;
  height: 30px;
  border-radius: 5px;
  padding: 10px;
  box-sizing: border-box;
  margin: 0px 20px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

const Name = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${props => props.fontSize};
  font-family: 'Bd';
`;

interface FollowProps {
  name: string;
  profile: string;
  userId: number;
}

const FollowBox = ({ name, profile, userId }: FollowProps) => {
  const [isFollowed, setIsFollowed] = useState<boolean>(true);

  const server = 'http://203.255.81.70:8030';
  let followUrl = `${server}/api/user/follow/${userId}`;
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const token = localStorage.getItem('login-token');
  const refreshToken = localStorage.getItem('login-refreshToken');
  const fetchFollow = async () => {
    if (token) {
      try {
        console.log('fetching...');
        const response = await fetch(followUrl, {
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
          console.log('reissuing Token');
          const reissueToken = await fetch(reissueTokenUrl, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Refresh-Token': `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem('login-token', data.token);
          localStorage.setItem('login-refreshToken', data.refreshToken);
          fetchFollow();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
      }
    }
  };

  const OnClickFollow = async () => {
    setIsFollowed(!isFollowed);
  };

  return (
    <Container>
      <LeftItems>
        <Circle bgcolor="white">
          <img src={profile} width="100%" height="100%" object-fit="cover"></img>
        </Circle>
        <NameContainer>{name}</NameContainer>
      </LeftItems>
      <Btn
        onClick={() => {
          fetchFollow();
          OnClickFollow();
        }}
        bgcolor={isFollowed ? 'white' : `${colors.Button_active}`}
      >
        {isFollowed ? '언팔로잉' : '팔로우'}
      </Btn>
    </Container>
  );
};

export default FollowBox;
