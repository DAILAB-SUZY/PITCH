import styled from 'styled-components';

import { colors } from '../../styles/color';
import logo from '../../img/logo_withText.png';
import InputBox from '../inputs/InputBox';
import { useEffect, useState } from 'react';
import { fetchEMAILPOST } from '../utils/fetchData';
const Title = styled.div<{ fontSize?: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'EB';
`;
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 100vh;
  width: 100vw;
  padding-top: 50px;
  background-color: ${colors.BG_grey};
  color: ${colors.Font_black};
`;
const RightAlignArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
`;

const SignupArea = styled.div<{
  flex_direction: string;
  justify_content: string;
}>`
  display: flex;
  flex-direction: ${props => props.flex_direction};
  justify-content: ${props => props.justify_content};
  align-items: center;
  width: 70vw;
`;
const LeftAlignArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  width: 270px;
  margin: 10px;
  padding: 0;
`;
const Btn = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 90px;
  height: 35px;
  background-color: ${colors.Main_Pink};
  color: ${colors.Font_white};
  border-radius: 10px;
  font-family: 'EB';
`;

function PasswordFindPage() {
  const [email, setEmail] = useState('');
  const [idError, setIdError] = useState('');
  const [messageColor, setMessageColor] = useState('red');

  useEffect(() => {}, []);

  const idCheckHandler = async (email: string) => {
    const check = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/;
    if (email === '') {
      setIdError('이메일를 입력해주세요.');
      return false;
    } else if (!check.test(email)) {
      setIdError('이메일의 형식이 아닙니다.');
      return false;
    } else {
      setIdError('');
      return true;
    }
  };

  const handleFind = () => {
    if (!idCheckHandler(email)) return;
    const data = {
      email: email,
    };
    const findURL = `/api/auth/reset-request`;
    fetchEMAILPOST(data, findURL).then(res => {
      if (res.ok) {
        setMessageColor('blue');
        setIdError('이메일을 확인해주세요.');
      } else {
        setIdError('가입된 이메일이 아닙니다.');
      }
    });
  };

  return (
    <Container>
      <SignupArea flex_direction="column" justify_content="center">
        <img src={logo} width="50px" height="50px"></img>
        <Title fontSize="30px" margin="10px 0px 30px 0px">
          비밀번호 찾기
        </Title>
        <RightAlignArea>
          <Title fontSize="18px">이메일 주소를 입력하세요.</Title>
        </RightAlignArea>
        <InputBox name="name" placeholder="E-Mail" onChange={e => setEmail(e.target.value)}></InputBox>
        <RightAlignArea>{idError && <small style={{ color: messageColor }}>{idError}</small>}</RightAlignArea>
        <LeftAlignArea>
          <Btn onClick={handleFind}> 찾기</Btn>
        </LeftAlignArea>
      </SignupArea>
    </Container>
  );
}

export default PasswordFindPage;
