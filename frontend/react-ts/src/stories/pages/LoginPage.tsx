import styled from 'styled-components';
import Btn from '../inputs/btn';
import { colors } from '../../styles/color';
import logo from '../../img/logo_withText.png';
import { useNavigate } from 'react-router-dom';
import InputBox from '../inputs/InputBox';
import useStore from '../store/store';
import { useState } from 'react';
// @ts-ignore
//import base64, { decode } from "js-base64";

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Bd';
`;
const Text = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Rg';
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: ${colors.BG_lightpink};
  color: ${colors.Font_black};
`;

const LeftAlignArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  width: 270px;
  margin: 10px;
  height: 10px;
`;

const LoginArea = styled.div<{
  flex_direction: string;
  justify_content: string;
}>`
  display: flex;
  flex-direction: ${props => props.flex_direction};
  justify-content: ${props => props.justify_content};
  align-items: center;
  width: 70vw;
`;

const StackConatiner = styled.div`
  position: relative;
`;

function LoginPage() {
  const navigate = useNavigate();
  const GoToSignupPage = () => {
    navigate('/Signup');
  };
  const GoToHomePage = () => {
    navigate('/Home');
  };
  const [isVisible, setIsVisible] = useState(false);
  const [inputType, setInputType] = useState('password');
  const passwordVisible = () => {
    setIsVisible(!isVisible);
    if (isVisible == true) setInputType('password');
    else setInputType('text');
  };

  const [typedEmail, setTypedEmail] = useState('');
  const [typedPassword, setTypedPassword] = useState('');

  const server = 'https://pitches.social';
  let loginUrl = server + '/api/auth/signin';
  const { setEmail, setName, setId } = useStore();
  const Login = () => {
    const fetchDatas = async () => {
      console.log('로그인');
      try {
        const response = await fetch(loginUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            refreshToken: null,
            token: null,
            email: typedEmail,
            password: typedPassword,
            id: 0,
          }),
        });
        const data = await response.json();

        if (response.status == 200) {
          localStorage.setItem('login-token', data.token);
          console.log(localStorage.getItem('login-token'));
          localStorage.setItem('login-refreshToken', data.refreshToken);
          console.log(localStorage.getItem('login-refreshToken'));
          setEmail(data.email);
          setId(data.id);
          setName(data.username);
          console.log(data);
          // // setName();

          console.log('로그인 완료');
          GoToHomePage();
        } else {
          console.log('로그인 실패');
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      }
    };
    fetchDatas();
  };

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 동작 방지
    Login();
  };

  return (
    <Container>
      <LoginArea flex_direction="column" justify_content="center">
        <img src={logo} width="150px" height="150px"></img>
        <Title fontSize="30px" margin="10px">
          {' '}
          로그인{' '}
        </Title>
        <InputBox placeholder="E-mail" onChange={e => setTypedEmail(e.target.value)}></InputBox>
        <form onSubmit={handleSearchSubmit}>
          <InputBox placeholder="Password" onChange={e => setTypedPassword(e.target.value)} type={inputType}></InputBox>
        </form>
        <LeftAlignArea>
          <Text fontSize="16px" margin="10px">
            {' '}
            비밀번호 보기
          </Text>
          {isVisible ? (
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-eye" viewBox="0 0 16 16" onClick={passwordVisible}>
              <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8M1.173 8a13 13 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5s3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5s-3.879-1.168-5.168-2.457A13 13 0 0 1 1.172 8z" />
              <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5M4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0" />
            </svg>
          ) : (
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-eye-slash" viewBox="0 0 16 16" onClick={passwordVisible}>
              <path d="M13.359 11.238C15.06 9.72 16 8 16 8s-3-5.5-8-5.5a7 7 0 0 0-2.79.588l.77.771A6 6 0 0 1 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755q-.247.248-.517.486z" />
              <path d="M11.297 9.176a3.5 3.5 0 0 0-4.474-4.474l.823.823a2.5 2.5 0 0 1 2.829 2.829zm-2.943 1.299.822.822a3.5 3.5 0 0 1-4.474-4.474l.823.823a2.5 2.5 0 0 0 2.829 2.829" />
              <path d="M3.35 5.47q-.27.24-.518.487A13 13 0 0 0 1.172 8l.195.288c.335.48.83 1.12 1.465 1.755C4.121 11.332 5.881 12.5 8 12.5c.716 0 1.39-.133 2.02-.36l.77.772A7 7 0 0 1 8 13.5C3 13.5 0 8 0 8s.939-1.721 2.641-3.238l.708.709zm10.296 8.884-12-12 .708-.708 12 12z" />
            </svg>
          )}
        </LeftAlignArea>

        <StackConatiner>
          <LeftAlignArea></LeftAlignArea>
          <Btn width="100px" height="40px" fontSize="20px" text="로그인" onClick={Login}></Btn>
        </StackConatiner>
        <LeftAlignArea></LeftAlignArea>
      </LoginArea>
      <Title fontSize="15px" margin="10px" onClick={GoToSignupPage}>
        회원가입
      </Title>
    </Container>
  );
}

export default LoginPage;
