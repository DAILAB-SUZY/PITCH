import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';

import logo from '../../img/logo.png';
import logoText from '../../img/logo_text.png';

const NavContainer = styled.header`
  width: 100vw;
  height: 55px;

  top: 0px;
  background-color: white;
  display: flex;

  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
`;

const ImageArea = styled.div<{ margin?: string }>`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: auto;
  height: auto;
  margin: ${props => props.margin};
`;

const LogoArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: auto;
  height: auto;
  margin: 0px;
`;

const MenuArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: auto;
  height: auto;
  margin: 0px 20px 0px 0px;
`;

interface NavProps {
  isMenuOpen: boolean;
  setIsMenuOpen: (isMenuOpen: boolean) => void;
}

const Nav = ({ isMenuOpen, setIsMenuOpen }: NavProps) => {
  const navigate = useNavigate();

  const GoToHomePage = () => {
    navigate('/Home');
  };

  return (
    <NavContainer>
      <LogoArea>
        <ImageArea margin="0px 0px 0px 20px">
          <img src={logo} width="60px" onClick={() => GoToHomePage()}></img>
        </ImageArea>
        <ImageArea margin="0px 0px 0px 5px">
          <img src={logoText} width="60px" onClick={() => GoToHomePage()}></img>
        </ImageArea>
      </LogoArea>
      <MenuArea>
        <svg
          onClick={() => {
            setIsMenuOpen(!isMenuOpen);
          }}
          xmlns="http://www.w3.org/2000/svg"
          width="35"
          height="35"
          fill="currentColor"
          className="bi bi-list"
          viewBox="0 0 16 16"
        >
          <path
            fillRule="evenodd"
            d="M2.5 12a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5"
          />
        </svg>
      </MenuArea>
    </NavContainer>
  );
};

export default Nav;
