import styled, { keyframes } from 'styled-components';
import { colors } from '../../styles/color';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import useStore from '../store/store';

const slideIn = keyframes`
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
`;

const slideOut = keyframes`
  from {
    transform: translateX(0);
    opacity: 1;
    visibility: 1;
  }
  to {
    transform: translateX(100%);
    opacity: 0;
    visibility: 0;
  }
`;

const MenuArea = styled.div<{ isexiting: boolean }>`
  position: fixed; // 또는 absolute
  z-index: 11;
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-end;
  padding-right: 20px;

  background-color: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);

  animation: ${props => (props.isexiting ? slideOut : slideIn)} 0.2s forwards;
`;

const MenuCloseButton = styled.div`
  display: flex;
  margin-right: 15px;
`;

const ButtonArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-end;
  gap: 20px;
  margin-right: 20px;
`;

const Button = styled.div`
  width: auto;
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  flex-direction: row;
`;

const Title = styled.div<{ fontSize: string; margin: string; color: string; opacity: number }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  color: ${props => props.color};
  font-family: 'EB';
  white-space: nowrap;
  opacity: ${props => props.opacity};
`;

const Line = styled.div`
  width: 60px;
  height: 1px;
  opacity: 1;
  background-color: ${colors.Font_grey};
  margin: 40px 20px 10px 0px;
`;

const SmallBtnArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 5px;
  margin: 20px 20px 0px 0px;
`;

interface MenuProps {
  // onClick?: () => void;
  page: number;
  isMenuOpen: boolean;
  setIsMenuOpen: (isMenuOpen: boolean) => void;
}

function Menu({ page, isMenuOpen, setIsMenuOpen }: MenuProps) {
  console.log(page);
  const navigate = useNavigate();
  const { id } = useStore();

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

  const GoToFriendSearchPage = () => {
    navigate('/FriendSearchPage');
  };

  const LogOut = () => {
    localStorage.removeItem('login-token');
    localStorage.removeItem('login-refreshToken');
    localStorage.removeItem('user-info');
    GoToStartPage();
  };
  const [activeNav, setActiveNav] = useState(page);
  return (
    <MenuArea isexiting={!isMenuOpen}>
      <MenuCloseButton
        onClick={() => {
          setIsMenuOpen(!isMenuOpen);
        }}
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor" className="bi bi-x" viewBox="0 0 16 16">
          <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708" />
        </svg>
      </MenuCloseButton>
      <ButtonArea>
        <Button
          onClick={() => {
            setActiveNav(1);
            GoToHomePage();
          }}
        >
          <svg
            color={colors.Main_Pink}
            opacity={activeNav === 1 ? 1 : 0.5}
            width={activeNav === 1 ? '40px' : '25px'}
            height={activeNav === 1 ? '40px' : '25px'}
            xmlns="http://www.w3.org/2000/svg"
            fill="currentColor"
            className="bi bi-house-door-fill"
            viewBox="0 0 16 16"
          >
            <path d="M6.5 14.5v-3.505c0-.245.25-.495.5-.495h2c.25 0 .5.25.5.5v3.5a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5" />
          </svg>
          <Title color={colors.Main_Pink} opacity={activeNav === 1 ? 1 : 0.5} fontSize={activeNav === 1 ? '35px' : '20px'} margin="0px 5px">
            Home
          </Title>
        </Button>
        <Button
          onClick={() => {
            setActiveNav(2);
            GoToMusicProfilePage();
          }}
        >
          <svg
            color={colors.Main_Pink}
            opacity={activeNav === 2 ? 1 : 0.5}
            width={activeNav === 2 ? '40px' : '25px'}
            height={activeNav === 2 ? '40px' : '25px'}
            xmlns="http://www.w3.org/2000/svg"
            fill="currentColor"
            className="bi bi-person-fill"
            viewBox="0 0 16 16"
          >
            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6" />
          </svg>
          <Title color={colors.Main_Pink} opacity={activeNav === 2 ? 1 : 0.5} fontSize={activeNav === 2 ? '35px' : '20px'} margin="0px 5px">
            Music Profile
          </Title>
        </Button>
        <Button
          onClick={() => {
            setActiveNav(2);
            GoToAlbumHomePage();
          }}
        >
          {/* <svg
            color={colors.Main_Pink}
            opacity={activeNav === 3 ? 1 : 0.5}
            width={activeNav === 3 ? '40px' : '25px'}
            height={activeNav === 3 ? '40px' : '25px'}
            xmlns="http://www.w3.org/2000/svg"
            fill="currentColor"
            className="bi bi-union"
            viewBox="0 0 16 16"
          >
            <path d="M0 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v2h2a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-2H2a2 2 0 0 1-2-2z" />
          </svg> */}
          <svg
            color={colors.Main_Pink}
            opacity={activeNav === 3 ? 1 : 0.5}
            width={activeNav === 3 ? '40px' : '25px'}
            height={activeNav === 3 ? '40px' : '25px'}
            xmlns="http://www.w3.org/2000/svg"
            fill="currentColor"
            className="bi bi-vinyl"
            // viewBox="0 0 16 16"
            viewBox="0 0 32 32"
          >
            <g data-name="Layer 9">
              <path d="M26,3H10A3,3,0,0,0,7,6V7H6a3,3,0,0,0-3,3V26a3,3,0,0,0,3,3H22a3,3,0,0,0,3-3V25h1a3,3,0,0,0,3-3V6A3,3,0,0,0,26,3ZM23,26a1,1,0,0,1-1,1H6a1,1,0,0,1-1-1V10A1,1,0,0,1,6,9H22a1,1,0,0,1,1,1V26Zm4-4a1,1,0,0,1-1,1H25V10a3,3,0,0,0-3-3H9V6a1,1,0,0,1,1-1H26a1,1,0,0,1,1,1Z" />
              <path d="M14,15a3,3,0,1,0,3,3A3,3,0,0,0,14,15Zm0,4a1,1,0,1,1,1-1A1,1,0,0,1,14,19Z" />
              <path d="M14,10a7.91,7.91,0,0,0-5,1.77,7.73,7.73,0,0,0-2,2.37,7.95,7.95,0,0,0,0,7.72A8,8,0,0,0,10.14,25a7.95,7.95,0,0,0,7.72,0,7.73,7.73,0,0,0,2.37-2A7.91,7.91,0,0,0,22,18,8,8,0,0,0,14,10Zm3.31,13A6,6,0,0,1,9,14.69,6,6,0,1,1,17.31,23Z" />{' '}
            </g>
          </svg>

          <Title color={colors.Main_Pink} opacity={activeNav === 3 ? 1 : 0.5} fontSize={activeNav === 3 ? '35px' : '20px'} margin="0px 5px">
            Albums
          </Title>
        </Button>
      </ButtonArea>
      <Line></Line>

      <SmallBtnArea
        onClick={() => {
          GoToFriendSearchPage();
        }}
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill={colors.Font_grey} className="bi bi-person-plus-fill" viewBox="0 0 16 16">
          <path d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6" />
          <path fill-rule="evenodd" d="M13.5 5a.5.5 0 0 1 .5.5V7h1.5a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0V8h-1.5a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5" />
        </svg>
        <Title color={colors.Font_grey} opacity={1} fontSize="15px" margin="0px">
          Search friend
        </Title>
      </SmallBtnArea>
      <SmallBtnArea
        onClick={() => {
          LogOut();
        }}
      >
        <svg color={colors.Font_grey} xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill={colors.Font_grey} className="bi bi-box-arrow-right" viewBox="0 0 16 16">
          <path
            fillRule="evenodd"
            d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"
          />
          <path fillRule="evenodd" d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z" />
        </svg>
        <Title color={colors.Font_grey} opacity={1} fontSize="15px" margin="0px">
          Log Out
        </Title>
      </SmallBtnArea>
    </MenuArea>
  );
}

export default Menu;
