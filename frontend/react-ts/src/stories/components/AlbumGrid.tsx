import styled from "styled-components";

interface AlbumDataProps {
  AlbumData: [
    {
      albumCover: string;
      albumId: number;
      albumName: string;
      score: number;
    },
  ];
}
const BestAlbumArea = styled.div`
  width: 100vw;
  display: flex;
  flex-direction: row;
  overflow-x: scroll;
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
`;

const AlbumImage = styled.img`
  width: 100%;
  height: 100%;
  border-radius: 10px;
  object-fit: cover;
`;

const AlbumArea = styled.div`
  display: flex;
  align-items: center;
`;
function AlbumGrid({ AlbumData }: AlbumDataProps) {
  const midIndex = Math.ceil(AlbumData.length / 2);
  const firstRow = AlbumData.slice(0, midIndex);
  const secondRow = AlbumData.slice(midIndex);
  console.log(firstRow);
  return (
    <BestAlbumArea>
      <AlbumCol>
        <AlbumRow>
          {firstRow.map((Album) => (
            <AlbumCover key={Album.albumId}>
              <AlbumImage src={Album.albumCover} alt={Album.albumName} />
            </AlbumCover>
          ))}
        </AlbumRow>
        <AlbumRow>
          {secondRow.map((Album) => (
            <AlbumCover key={Album.albumId}>
              <AlbumImage src={Album.albumCover} alt={Album.albumName} />
            </AlbumCover>
          ))}
        </AlbumRow>
      </AlbumCol>
    </BestAlbumArea>
  );
}

export default AlbumGrid;
