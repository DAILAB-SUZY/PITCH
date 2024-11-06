import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useNavigate } from 'react-router-dom';
import Lottie from 'lottie-react';
import loadingLottie from '../../img/pitchLogoMove.json';
import { useEffect } from 'react';

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: ${colors.BG_white};
`;

const LottieArea = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 170px;
  height: 170px;
`;

function StartPage() {
  const navigate = useNavigate();
  const GoToLoginPage = () => {
    navigate('/Login');
  };
  const GoToHomePage = () => {
    navigate('/Home');
  };

  const Start = () => {
    if (localStorage.getItem('login-token')) {
      GoToHomePage();
    } else {
      GoToLoginPage();
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      Start();
    }, 4000); // 5초 후에 Start 함수 실행

    return () => clearTimeout(timer); // 컴포넌트가 언마운트될 때 타이머 정리
  }, []);

  return (
    <Container>
      {/* <img
        src={logo}
        width="100px"
        height="100px"
        onClick={() => {
          Start();
        }}
      ></img> */}
      <LottieArea>
        <Lottie animationData={loadingLottie} />
      </LottieArea>
    </Container>
  );
}

export default StartPage;
