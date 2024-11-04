import { useEffect } from 'react';
import { fetchPOST, MAX_REISSUE_COUNT } from '../utils/fetchData';
function PlaylistRedirectPage() {
  useEffect(() => {
    // 현재 URL 가져오기
    const currentUrl = window.location.href;

    // URL 객체로 파싱
    const url = new URL(currentUrl);

    // 'code' 파라미터 추출
    const code = url.searchParams.get('code');

    // 'code' 값을 확인해보기
    console.log(code);

    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    const data = {
      youtubeaccesstoken: code,
    };
    fetchPOST(token, refreshToken, '/api/createPlaylist', data, MAX_REISSUE_COUNT).then(data => {
      console.log(data);
    });
  }, []);
  return (
    <div>
      <h1>Playlist 생성중...</h1>
    </div>
  );
}

export default PlaylistRedirectPage;
