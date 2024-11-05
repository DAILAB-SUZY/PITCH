import styled from 'styled-components';

import { colors } from '../../styles/color';
import { useNavigate, useParams } from 'react-router-dom';
import logo from '../../img/logo_withText.png';
import InputBox from '../inputs/InputBox';
import { useEffect, useState } from 'react';
import { fetchEMAILPOST, fetchPOST } from '../utils/fetchData';
const Title = styled.div<{ fontSize?: string; margin?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Bd';
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
  font-family: 'Bd';
`;

function PasswordResetPage({}) {
  const [confirm, setConfirm] = useState('');
  const [messageColor, setMessageColor] = useState('red');
  const [passwordError, setPasswordError] = useState('비밀번호를 입력해주세요.');
  const [confirmError, setConfirmError] = useState('');

  const [password, setPassword] = useState('');
  const { authCode } = useParams();

  const navigate = useNavigate();
  const GoToLoginPage = () => {
    navigate('/Login');
  };

  const handleReset = () => {
    // // 현재 URL 가져오기
    // const currentUrl = window.location.href;

    // // URL 객체로 파싱
    // const authCode = currentUrl.split('passwordchange/')[1];

    const data = {
      authCode: authCode,
      password: password,
    };
    console.log(data);

    const resetURL = `/api/auth/passwordchange`;
    fetchEMAILPOST(data, resetURL).then(res => {
      if (res.ok) {
        GoToLoginPage();
      } else {
        setConfirmError('다시 시도하세요.');
      }
    });
  };

  // 비밀번호 확인
  const onChangePasswordHandler = (e: any) => {
    const { name, value } = e.target;
    if (name === 'password') {
      setPassword(value);
      passwordCheckHandler(value, confirm);
    } else {
      setConfirm(value);
      passwordCheckHandler(password, value);
    }
  };
  const passwordCheckHandler = (password: string, confirm: string) => {
    const passwordRegex = /^[a-z\d!@*&-_]{8,16}$/;
    if (password === '') {
      setPasswordError('비밀번호를 입력해주세요.');
      return false;
    } else if (!passwordRegex.test(password)) {
      setPasswordError('비밀번호는 8~16자의 영소문자, 숫자, !@*&-_만 입력 가능합니다.');
      return false;
    } else if (confirm !== password) {
      setPasswordError('');
      setConfirmError('비밀번호가 일치하지 않습니다.');
      return false;
    } else {
      setPasswordError('');
      setConfirmError('');
      return true;
    }
  };

  return (
    <Container>
      <SignupArea flex_direction="column" justify_content="center">
        <img onClick={() => GoToLoginPage()} src={logo} width="50px" height="50px"></img>
        <Title fontSize="30px" margin="10px 0px 30px 0px">
          비밀번호 재설정
        </Title>
        <RightAlignArea>
          <Title fontSize="20px">Password</Title>
        </RightAlignArea>

        <InputBox name="password" placeholder="Password" onChange={onChangePasswordHandler}></InputBox>
        <RightAlignArea>{passwordError && <small style={{ color: messageColor }}>{passwordError}</small>}</RightAlignArea>
        <InputBox name="confirm" placeholder="Password 확인" onChange={onChangePasswordHandler}></InputBox>
        <RightAlignArea>{confirmError && <small style={{ color: messageColor }}>{confirmError}</small>}</RightAlignArea>

        <LeftAlignArea>
          <Btn onClick={handleReset}> 재설정 </Btn>
        </LeftAlignArea>
      </SignupArea>
    </Container>
  );
}

export default PasswordResetPage;
