// import { useNavigate } from 'react-router-dom';
const server = 'http://203.255.81.70:8030';
// const server = 'http://pitches.social';
const reissueTokenUrl = `${server}/api/auth/reissued`;

export const MAX_REISSUE_COUNT = 3;
// 토큰 재발급 함수
export const reissueToken = async (refreshToken: string): Promise<string | null> => {
  console.log('Reissuing Token...');
  //   const navigate = useNavigate();
  //   const GoToErrorPage = () => {
  //     navigate('/ErrorPage');
  // };
  try {
    const response = await fetch(reissueTokenUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Refresh-Token': refreshToken,
      },
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('login-token', data.token);
      localStorage.setItem('login-refreshToken', data.refreshToken);
      return data.token;
    } else {
      console.error('Failed to reissue token:', response.status);
      return null;
    }
  } catch (error) {
    console.error('Refresh Token 재발급 실패:', error);
    return null;
  }
};

// 데이터 fetch 함수 - GET
export const fetchGET = async (token: string, refreshToken: string, URL: string, retryCount: number): Promise<any | undefined> => {
  if (!token) return;

  try {
    console.log('Fetching GET...');
    const response = await fetch(`${server}${URL}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      const data = await response.json();
      console.log('Data fetched:', data);
      return data;
    } else if ((response.status === 401 && retryCount > 0) || (response.status === 500 && retryCount > 0) || (response.status === 405 && retryCount > 0)) {
      console.log('토큰 만료... 재발급 시도');
      const newToken = await reissueToken(refreshToken);
      if (newToken) {
        return fetchGET(newToken, refreshToken, URL, retryCount - 1); // 재귀 호출로 새 토큰으로 재요청
      }
    } else {
      console.error('Failed to fetch data:', response.status);
    }
  } catch (error) {
    console.error('Error fetching the JSON file:', error);
  } finally {
    console.log('Finished fetching');
  }
};

// 데이터 fetch 함수 - POST
export const fetchPOST = async (token: string, refreshToken: string, URL: string, Data: any, retryCount: number): Promise<any | undefined> => {
  if (!token) {
    console.log('Token is null');
    return;
  }

  try {
    console.log('Fetching POST...');
    console.log(Data);
    const response = await fetch(`${server}${URL}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(Data),
    });

    if (response.ok) {
      const data = await response.json();
      console.log('Data fetched:', data);
      return data;
    } else if ((response.status === 401 && retryCount > 0) || (response.status === 500 && retryCount > 0) || (response.status === 405 && retryCount > 0)) {
      console.log('토큰 만료... 재발급 시도');
      const newToken = await reissueToken(refreshToken);
      if (newToken) {
        return fetchPOST(newToken, refreshToken, URL, Data, retryCount - 1); // 재귀 호출로 새 토큰으로 재요청
      }
    } else {
      console.error('Failed to fetch data:', response.status);
    }
  } catch (error) {
    console.error('Error fetching the JSON file:', error);
  } finally {
    console.log('Finished fetching');
  }
};

// 데이터 fetch 함수 - POST_file
export const fetchPOSTFile = async (token: string, refreshToken: string, URL: string, Data: any, retryCount: number): Promise<any | undefined> => {
  if (!token) return;

  try {
    console.log('Fetching POST File...');
    console.log(Data);
    const obj2: { [key: string]: any } = {};
    Data.forEach((value: any, key: string | number) => {
      obj2[key] = value;
    });
    console.log(obj2);
    const response = await fetch(`${server}${URL}`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: Data,
    });

    if (response.ok) {
      const data = await response;
      console.log('Data fetched:', data);
      return data;
    } else if ((response.status === 401 && retryCount > 0) || (response.status === 500 && retryCount > 0) || (response.status === 405 && retryCount > 0)) {
      console.log('토큰 만료... 재발급 시도');
      const newToken = await reissueToken(refreshToken);
      if (newToken) {
        return fetchPOST(newToken, refreshToken, URL, Data, retryCount - 1); // 재귀 호출로 새 토큰으로 재요청
      }
    } else {
      console.error('Failed to fetch data:', response.status);
    }
  } catch (error) {
    console.error('Error fetching the JSON file:', error);
  } finally {
    console.log('Finished fetching');
  }
};

// 데이터 fetch 함수 - DELETE
export const fetchDELETE = async (token: string, refreshToken: string, URL: string, retryCount: number): Promise<any | undefined> => {
  if (!token) return;

  try {
    console.log('Deleting POST...');
    const response = await fetch(`${server}${URL}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      const data = await response.json();
      console.log('Data fetched:', data);
      return data;
    } else if ((response.status === 401 && retryCount > 0) || (response.status === 500 && retryCount > 0) || (response.status === 405 && retryCount > 0)) {
      console.log('토큰 만료... 재발급 시도');
      const newToken = await reissueToken(refreshToken);
      if (newToken) {
        return fetchDELETE(newToken, refreshToken, URL, retryCount - 1); // 재귀 호출로 새 토큰으로 재요청
      }
    } else {
      console.error('Failed to fetch data:', response.status);
    }
  } catch (error) {
    console.error('Error fetching the JSON file:', error);
  } finally {
    console.log('Finished fetching');
  }
};
