import styled from 'styled-components';

const Container = styled.div`
  width: 90vw;
  height: 40px;
  border: 5px solid transparent;
  border-radius: 15px;
  background-image: linear-gradient(#fff, #fff), linear-gradient(to right, #ffdae0 50%, #fb96a5 100%);
  background-origin: border-box;
  background-clip: content-box, border-box;
`;

const InnerArea = styled.div`
  width: 90vw;
  height: 40px;
  border-radius: 10px;
  background-color: lightgrey;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'EB';
`;

const SerchBox = () => {
  return (
    <Container>
      <InnerArea>
        <Title fontSize="20px" margin="15px">
          노래, 아티스트 검색
        </Title>
        <div style={{ marginRight: '15px' }}>
          <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 16 16">
            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
          </svg>
        </div>
      </InnerArea>
    </Container>
  );
};

export default SerchBox;
