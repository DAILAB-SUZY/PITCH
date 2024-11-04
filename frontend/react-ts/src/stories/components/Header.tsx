import styled from 'styled-components';
import { useState } from 'react';

import Menu from './Menu';
import Nav from './Nav';

const Header = ({ page }: { page: number }) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <HeaderContainer>
      <Nav isMenuOpen={isMenuOpen} setIsMenuOpen={setIsMenuOpen} />
      <Menu page={page} isMenuOpen={isMenuOpen} setIsMenuOpen={setIsMenuOpen} />
    </HeaderContainer>
  );
};

const HeaderContainer = styled.header`
  position: absolute;
  z-index: 5;
`;

export default Header;
