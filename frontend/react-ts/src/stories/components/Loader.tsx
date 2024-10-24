import styled, { keyframes } from "styled-components";
import { colors } from "../../styles/color";
// 애니메이션 정의
const mulShdSpin = keyframes`
  0%, 100% {
    box-shadow: 0 -3em 0 0.2em, 
    2em -2em 0 0em, 
    3em 0 0 -1em, 
    2em 2em 0 -1em, 
    0 3em 0 -1em, 
    -2em 2em 0 -1em, 
    -3em 0 0 -1em, 
    -2em -2em 0 0;
  }
  12.5% {
    box-shadow: 0 -3em 0 0, 
    2em -2em 0 0.2em, 
    3em 0 0 0, 
    2em 2em 0 -1em, 
    0 3em 0 -1em, 
    -2em 2em 0 -1em, 
    -3em 0 0 -1em, 
    -2em -2em 0 -1em;
  }
  25% {
    box-shadow: 0 -3em 0 -0.5em, 
    2em -2em 0 0, 
    3em 0 0 0.2em, 
    2em 2em 0 0, 
    0 3em 0 -1em, 
    -2em 2em 0 -1em, 
    -3em 0 0 -1em, 
    -2em -2em 0 -1em;
  }
  37.5% {
    box-shadow: 0 -3em 0 -1em, 
    2em -2em 0 -1em, 
    3em 0em 0 0, 
    2em 2em 0 0.2em, 
    0 3em 0 0em, 
    -2em 2em 0 -1em, 
    -3em 0em 0 -1em, 
    -2em -2em 0 -1em;
  }
  50% {
    box-shadow: 0 -3em 0 -1em, 
    2em -2em 0 -1em, 
    3em 0 0 -1em, 
    2em 2em 0 0em, 
    0 3em 0 0.2em, 
    -2em 2em 0 0, 
    -3em 0em 0 -1em, 
    -2em -2em 0 -1em;
  }
  62.5% {
    box-shadow: 0 -3em 0 -1em, 
    2em -2em 0 -1em, 
    3em 0 0 -1em, 
    2em 2em 0 -1em, 
    0 3em 0 0, 
    -2em 2em 0 0.2em, 
    -3em 0 0 0, 
    -2em -2em 0 -1em;
  }
  75% {
    box-shadow: 0em -3em 0 -1em, 
    2em -2em 0 -1em, 
    3em 0em 0 -1em, 
    2em 2em 0 -1em, 
    0 3em 0 -1em, 
    -2em 2em 0 0, 
    -3em 0em 0 0.2em, 
    -2em -2em 0 0;
  }
  87.5% {
    box-shadow: 0em -3em 0 0, 
    2em -2em 0 -1em, 
    3em 0 0 -1em, 
    2em 2em 0 -1em, 
    0 3em 0 -1em, 
    -2em 2em 0 0, 
    -3em 0em 0 0, 
    -2em -2em 0 0.2em;
  }
`;

// 로더 스타일 정의

const LoaderWrapper = styled.div`
  color: ${colors.Main_Pink};
  margin-top: 40px;
  font-size: 10px;
  width: 1em;
  height: 1em;
  border-radius: 50%;
  /* position: relative; */
  text-indent: -9999em;
  animation: ${mulShdSpin} 1.3s infinite linear;
  transform: translateZ(0);
`;

// Loader 컴포넌트 정의

function Loader() {
  return <LoaderWrapper></LoaderWrapper>;
}

export default Loader;
