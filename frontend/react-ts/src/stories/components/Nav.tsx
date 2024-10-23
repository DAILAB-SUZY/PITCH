import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import home_active from '../../img/home_on.png';
import home_deactive from '../../img/home.png';
import Profile_active from '../../img/profile_on.png';
import Profile_deactive from '../../img/profile.png';
import albumChat_active from '../../img/albumChat_on.png';
import albumChat_deactive from '../../img/albumChat.png';
import logo from '../../img/logo.png';
import useStore from '../store/store';

interface NavProps {
  // onClick?: () => void;
  page: number;
}

const NavContainer = styled.div`
  width: 100vw;
  height: 70px;
  position: absolute;
  top: 0px;
  background-color: white;
  display: flex;
  overflow-x: hidden;
  padding: 20px 0px 30px 00px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 5;
  /* box-sizing: border-box; */
  // box-shadow: rgba(0, 0, 0, 0.3) 0px 1px 4px;
  background-color: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
`;

const ButtonArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: flex-end;
  padding-left: 10px;
  width: 100vw;
`;

const Button = styled.div`
  width: auto;
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  flex-direction: row;
`;

const Title = styled.div<{ fontSize: string; margin: string; color: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  color: ${props => props.color};
  font-family: 'Bd';
  white-space: nowrap;
`;

const Nav = ({ page }: NavProps) => {
  const { email, setEmail, name, setName, id, setId } = useStore();

  const navigate = useNavigate();

  const GoToHomePage = () => {
    navigate('/Home');
  };
  const GoToMusicProfilePage = () => {
    navigate('/MusicProfilePage', { state: id });
  };
  const GoToAlbumHomePage = () => {
    navigate('/AlbumHomePage');
  };
  const GoToStartPage = () => {
    navigate('/');
  };
  const [activeNav, setActiveNav] = useState(page);

  return (
    <NavContainer>
      <img src={logo} width="70px" onClick={() => GoToStartPage()}></img>
      <ButtonArea>
        <Button
          onClick={() => {
            setActiveNav(1);
            GoToAlbumHomePage();
          }}
        >
          {activeNav === 1 ? <img src={home_active} width="35px" height="35px"></img> : <img src={home_deactive} width="20px" height="20px"></img>}
          <Title color={activeNav === 1 ? colors.Main_Pink : colors.Button_deactive} fontSize={activeNav === 1 ? '25px' : '13px'} margin="0px 5px">
            Home
          </Title>
        </Button>
        <Button
          onClick={() => {
            setActiveNav(2);
            GoToMusicProfilePage();
          }}
        >
          {activeNav === 2 ? <img src={Profile_active} width="35px" height="35px"></img> : <img src={Profile_deactive} width="20px" height="20px"></img>}

          <Title color={activeNav === 2 ? colors.Main_Pink : colors.Button_deactive} fontSize={activeNav === 2 ? '25px' : '13px'} margin="0px 5px">
            Music Profile
          </Title>
        </Button>
        <Button
          onClick={() => {
            setActiveNav(2);
            GoToAlbumHomePage();
          }}
        >
          {activeNav === 3 ? <img src={albumChat_active} width="35px" height="35px"></img> : <img src={albumChat_deactive} width="20px" height="20px"></img>}

          <Title color={activeNav === 3 ? colors.Main_Pink : colors.Button_deactive} fontSize={activeNav === 3 ? '25px' : '13px'} margin="0px 5px">
            Album Chat
          </Title>
        </Button>
      </ButtonArea>
    </NavContainer>
  );
};

export default Nav;
