import styled from 'styled-components';

interface FavoriteArtistProps {
  FavoriteArtistData: {
    albumCover: string;
    albumName: string;
    artistCover: string;
    artistName: string;
    trackCover: string;
    trackName: string;
  };
}

const FavoriteArtistArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  margin-bottom: 50px;
  width: 90vw;
  max-width: 600px;
`;

const BG = styled.div`
  position: relative;
  display: flex;
  width: 100%;
  max-width: 600px;
  min-width: 360px;
  height: 360px;
  border-radius: 12px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
`;

const ImageArea = styled.div`
  position: absolute;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  overflow: hidden;
  top: 0px;
  left: 0px;

  width: 100%;
  max-width: 600px;
  height: 360px;
  /* object-fit: cover; */
  z-index: 1;
  border-radius: 12px;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100%;
  max-width: 600px;
  height: 100%;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 0.3) 100%);
  backdrop-filter: blur(0px);
`;

const ContentArea = styled.div`
  width: 320px;
  height: 320px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
`;

const ArtistTitleTextArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  width: 320px;
  height: auto;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  box-sizing: border-box;
  z-index: 3;
`;

const ArtistAlbumTrackArea = styled.div`
  display: flex;
  width: 100%;
  height: auto;
  flex-direction: column;
`;

const FavoriteAlbumArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  width: 320px;
  height: auto;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  box-sizing: border-box;
  z-index: 3;
  margin-bottom: 10px;
`;

const FavoriteAlbumCover = styled.div`
  width: 70px;
  height: 70px;
  object-fit: cover;
  overflow: hidden;
  border-radius: 10%;
`;

const FavoriteAlbumTextArea = styled.div`
  width: 250px;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-evenly;
  margin-left: 10px;
  /* white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시 */
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  color?: string;
  opacity?: string;
  marginTop?: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
  margin-right: 15px;
  margin-top: ${props => props.marginTop};
  opacity: ${props => props.opacity};

  display: -webkit-box;
  -webkit-line-clamp: 2; /* 텍스트를 2줄로 제한 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis; /* 넘칠 때 말줄임표(...) 표시 */
`;

function FavoriteArtistCard({ FavoriteArtistData }: FavoriteArtistProps) {
  const data = FavoriteArtistData;
  return (
    <FavoriteArtistArea>
      {FavoriteArtistData.artistName === null ? (
        <Text fontFamily="Rg" fontSize="15px">
          아직 설정하지 않았습니다.
        </Text>
      ) : (
        <BG>
          <ImageArea>
            <img src={data.artistCover} width="100%" height="auto" object-fit="cover"></img>
          </ImageArea>
          <GradientBG> </GradientBG>
          <ContentArea>
            <ArtistTitleTextArea>
              <Text fontFamily="Bd" fontSize={data.artistName.length > 10 ? '40px' : '50px'} color="white">
                {data.artistName}
              </Text>
            </ArtistTitleTextArea>
            <ArtistAlbumTrackArea>
              {' '}
              <FavoriteAlbumArea>
                <FavoriteAlbumCover>
                  <img src={data.albumCover} width="100%" height="100%" object-fit="cover"></img>
                </FavoriteAlbumCover>
                <FavoriteAlbumTextArea>
                  <Text fontFamily="Rg" fontSize="15px" color="white" opacity="0.7">
                    Favorite Album
                  </Text>
                  <Text fontFamily="Bd" fontSize="22px" color="white">
                    {data.albumName}
                  </Text>
                </FavoriteAlbumTextArea>
              </FavoriteAlbumArea>
              <FavoriteAlbumArea>
                <FavoriteAlbumCover>
                  <img src={data.trackCover} width="100%" height="100%" object-fit="cover"></img>
                </FavoriteAlbumCover>
                <FavoriteAlbumTextArea>
                  <Text fontFamily="Rg" fontSize="15px" color="white" opacity="0.7">
                    Favorite Song
                  </Text>
                  <Text fontFamily="Bd" fontSize="22px" color="white">
                    {data.trackName}
                  </Text>
                </FavoriteAlbumTextArea>
              </FavoriteAlbumArea>
            </ArtistAlbumTrackArea>
          </ContentArea>
        </BG>
      )}
    </FavoriteArtistArea>
  );
}

export default FavoriteArtistCard;
