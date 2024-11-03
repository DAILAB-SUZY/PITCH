import { useNavigate } from 'react-router-dom';

function ErrorPage() {
  const navigate = useNavigate();
  const GoToLoginPage = () => {
    navigate('/Login');
  };
  localStorage.clear();
  console.log('ErrorPage');
  return (
    <div>
      <div>에러가 발생했습니다.</div>
      <div onClick={() => GoToLoginPage()}>다시 로그인하기 </div>
    </div>
  );
}

export default ErrorPage;
