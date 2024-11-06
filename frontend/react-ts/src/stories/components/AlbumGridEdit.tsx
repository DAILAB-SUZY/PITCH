import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';

const BestAlbumArea = styled.div`
  width: 100vw;
  display: flex;
  flex-direction: row;
  overflow-x: scroll;
  box-sizing: border-box;
  padding: 20px;
`;

const AlbumCol = styled.div`
  display: flex;
  flex-direction: column;
  width: auto;
`;

const AlbumRow = styled.div`
  display: flex;
  width: auto;
  margin-bottom: 20px;
`;

const AlbumCover = styled.div`
  display: flex;
  border-radius: 10px;
  width: 150px;
  height: 150px;
  align-items: center;
  justify-content: center;
  /* overflow: hidden; */
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
  margin-right: 10px;

  position: relative;
`;

const AlbumImage = styled.img`
  width: 100%;
  height: 100%;
  border-radius: 10px;
  object-fit: cover;
`;

const DeleteBtn = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 22px;
  position: absolute;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  top: -7px;
  right: -7px;
  background-color: ${colors.Main_Pink};
  color: white;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
`;

interface BestAlbum {
  albumCover: string;
  albumId: number;
  albumName: string;
  score: number;
  spotifyId: string;
}
interface AlbumDataProps {
  bestAlbum: BestAlbum[];
  setBestAlbum: React.Dispatch<React.SetStateAction<BestAlbum[] | undefined>>;
}

function AlbumGrid({ bestAlbum, setBestAlbum }: AlbumDataProps) {
  const [firstRow, setFirstRow] = useState<BestAlbum[]>([]);
  const [secondRow, setSecondRow] = useState<BestAlbum[]>([]);
  useEffect(() => {
    if (bestAlbum.length <= 2) {
      setFirstRow(bestAlbum);
      setSecondRow([]);
    } else {
      const midIndex = Math.ceil(bestAlbum.length / 2);
      setFirstRow(bestAlbum.slice(0, midIndex));
      setSecondRow(bestAlbum.slice(midIndex));
    }
  }, [bestAlbum]);

  // 앨범 삭제
  const DeleteAlbum = (albumId: number) => {
    setBestAlbum(bestAlbum.filter(album => album.albumId !== albumId));
  };
  return (
    <BestAlbumArea>
      {bestAlbum.length === 0 && <div>추가된 앨범이 없습니다.</div>}
      <AlbumCol>
        <AlbumRow>
          {firstRow.map(Album => (
            <AlbumCover key={Album.albumId}>
              <DeleteBtn
                onClick={() => {
                  DeleteAlbum(Album.albumId);
                }}
              >
                -
              </DeleteBtn>
              <AlbumImage src={Album.albumCover} alt={Album.albumName} />
            </AlbumCover>
          ))}
        </AlbumRow>
        <AlbumRow>
          {secondRow.map(Album => (
            <AlbumCover key={Album.albumId}>
              <DeleteBtn
                onClick={() => {
                  DeleteAlbum(Album.albumId);
                }}
              >
                -
              </DeleteBtn>
              <AlbumImage src={Album.albumCover} alt={Album.albumName} />
            </AlbumCover>
          ))}
        </AlbumRow>
      </AlbumCol>
    </BestAlbumArea>
  );
}

export default AlbumGrid;
