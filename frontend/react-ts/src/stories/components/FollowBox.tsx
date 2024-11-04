import styled from 'styled-components';
import { colors } from '../../styles/color';
import useStore from '../store/store';
import { useNavigate } from 'react-router-dom';

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

interface FollowProps {
  name: string;
  profile: string;
  userId: number;
  ChangeFollow: (value: number) => void;
  isFollowing: boolean;
}

const FollowBox = ({ name, profile, userId, ChangeFollow, isFollowing }: FollowProps) => {
  const { id } = useStore();
  const navigate = useNavigate();
  const GoToMusicProfilePage = (userId: number) => {
    navigate('/MusicProfilePage', { state: userId });
  };
  return (
    <Container>
      <LeftItems
        onClick={() => {
          GoToMusicProfilePage(userId);
        }}
      >
        <Circle bgcolor="white">
          <img src={profile} width="100%" height="100%" object-fit="cover"></img>
        </Circle>
        <NameContainer>{name}</NameContainer>
      </LeftItems>
      {userId !== id ? (
        <Btn
          onClick={() => {
            ChangeFollow(userId);
          }}
          bgcolor={isFollowing ? 'white' : `${colors.Button_active}`}
        >
          {isFollowing ? '팔로잉' : '언팔로우'}
        </Btn>
      ) : (
        <></>
      )}
    </Container>
  );
};

export default FollowBox;
