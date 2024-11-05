import { styled } from 'styled-components';
import { useEffect, useState } from 'react';
import { colors } from '../../styles/color';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: auto;
  background-color: ${colors.BG_grey};
`;
const StarCard = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: auto;
  height: auto;

  margin: 20px;
`;

const ScoreBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;
const StarsArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 5px;
`;

const Text = styled.div<{
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
  color?: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  margin: ${props => props.margin};
  color: ${props => props.color};
`;

interface StarEditProps {
  score: number;
  stars: string[];
  setScore: (score: number) => void;
  setStars: (stars: string[]) => void;
}

const STARINFO: { [key: number]: string } = {
  0: '아직 평가하지 않았습니다.',
  1: '" 💀 최악이에요 "',
  2: '" 🤬 싫어요 "',
  3: '" 😡 별로에요 "',
  4: '" 😕 그저 그래요 "',
  5: '" 😐 보통이에요 "',
  6: '" 😀 좋아요 "',
  7: '" 😄 진짜 좋아요 "',
  8: '" 😆 훌륭해요 "',
  9: '" 🫨 환상적이에요 "',
  10: '" 🤯 완벽해요 "',
};

function ScoreEditAlbumPage({ stars, score, setScore, setStars }: StarEditProps) {
  const [startext, setStartext] = useState<string>('아직 평가하지 않았습니다.');
  const scoreToStar = (score: number) => {
    let stars: string[] = [];

    // 별 5개를 기준으로 0부터 10까지의 score를 5단위로 변환
    for (let i = 0; i < 5; i++) {
      if (score >= (i + 1) * 2) {
        stars.push('full'); // 완전히 채워진 별
      } else if (score >= i * 2 + 1) {
        stars.push('half'); // 반 채워진 별
      } else {
        stars.push('empty'); // 빈 별
      }
    }

    return stars;
  };

  useEffect(() => {
    if (score === null) {
      setStars(scoreToStar(0));
      setStartext(STARINFO[0]);
    } else {
      setStars(scoreToStar(score));
      setStartext(STARINFO[score]);
    }
  }, [score]);

  const handleClick = (event: any, key: any) => {
    console.log(event.clientX);
    const { left, width } = event.currentTarget.getBoundingClientRect();
    const clickX = event.clientX;
    const middleX = left + width / 2;

    if (clickX < middleX) {
      handleLeftClick(key);
    } else {
      handleRightClick(key);
    }
  };

  const handleLeftClick = (key: any) => {
    const score = key * 2 + 1;
    setStars(scoreToStar(score));
    setScore(score);
    //fetchScore(score);
  };

  const handleRightClick = (key: any) => {
    const score = key * 2 + 2;
    setStars(scoreToStar(score));
    setScore(score);
    //fetchScore(score);
  };

  //   const fetchScore = async (score: number) => {
  //     const token = localStorage.getItem('login-token') as string;
  //     const refreshToken = localStorage.getItem('login-rrefreshToken') as string;
  //     const url = `/api/album/${albumId}/score`;
  //     console.log('adding score');
  //     fetchPOST(token, refreshToken, url, { score: score }, MAX_REISSUE_COUNT).then(data => {
  //       console.log(data);
  //     });
  //   };

  return (
    <Container>
      {' '}
      <StarCard>
        <ScoreBox>
          <Text fontSize="15px" fontFamily="SB" margin="0px 0 10px 0" color={colors.Font_grey}>
            {startext}
          </Text>
          <StarsArea>
            {stars?.map((star, index) =>
              star === 'full' ? (
                <svg
                  style={{ filter: 'drop-shadow(0px 0px 5px rgba(255, 135, 185, 0.5))' }}
                  xmlns="http://www.w3.org/2000/svg"
                  onClick={e => handleClick(e, index)}
                  width="30"
                  height="30"
                  fill={colors.Main_Pink}
                  className="bi bi-star-fill"
                  viewBox="0 0 16 16"
                >
                  <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z" />
                </svg>
              ) : star === 'half' ? (
                <svg
                  style={{ filter: 'drop-shadow(0px 0px 5px rgba(255, 135, 185, 0.5))' }}
                  xmlns="http://www.w3.org/2000/svg"
                  onClick={e => handleClick(e, index)}
                  width="30"
                  height="30"
                  fill={colors.Main_Pink}
                  className="bi bi-star-half"
                  viewBox="0 0 16 16"
                >
                  <path d="M5.354 5.119 7.538.792A.52.52 0 0 1 8 .5c.183 0 .366.097.465.292l2.184 4.327 4.898.696A.54.54 0 0 1 16 6.32a.55.55 0 0 1-.17.445l-3.523 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256a.5.5 0 0 1-.146.05c-.342.06-.668-.254-.6-.642l.83-4.73L.173 6.765a.55.55 0 0 1-.172-.403.6.6 0 0 1 .085-.302.51.51 0 0 1 .37-.245zM8 12.027a.5.5 0 0 1 .232.056l3.686 1.894-.694-3.957a.56.56 0 0 1 .162-.505l2.907-2.77-4.052-.576a.53.53 0 0 1-.393-.288L8.001 2.223 8 2.226z" />
                </svg>
              ) : star === 'empty' ? (
                <svg xmlns="http://www.w3.org/2000/svg" onClick={e => handleClick(e, index)} width="30" height="30" fill={colors.Main_Pink} className="bi bi-star" viewBox="0 0 16 16">
                  <path d="M2.866 14.85c-.078.444.36.791.746.593l4.39-2.256 4.389 2.256c.386.198.824-.149.746-.592l-.83-4.73 3.522-3.356c.33-.314.16-.888-.282-.95l-4.898-.696L8.465.792a.513.513 0 0 0-.927 0L5.354 5.12l-4.898.696c-.441.062-.612.636-.283.95l3.523 3.356-.83 4.73zm4.905-2.767-3.686 1.894.694-3.957a.56.56 0 0 0-.163-.505L1.71 6.745l4.052-.576a.53.53 0 0 0 .393-.288L8 2.223l1.847 3.658a.53.53 0 0 0 .393.288l4.052.575-2.906 2.77a.56.56 0 0 0-.163.506l.694 3.957-3.686-1.894a.5.5 0 0 0-.461 0z" />
                </svg>
              ) : null,
            )}
          </StarsArea>
        </ScoreBox>
      </StarCard>
    </Container>
  );
}

export default ScoreEditAlbumPage;
