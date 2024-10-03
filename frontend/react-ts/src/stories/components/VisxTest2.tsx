import { useEffect, useMemo, useState } from "react";
import styled from "styled-components";
import { Arc } from "@visx/shape";
import { Group } from "@visx/group";
import { GradientLightgreenGreen } from "@visx/gradient";
import { scaleBand, scaleRadial } from "@visx/scale";
import * as MusicData from "../../data/result2.json";

const barColor = "#ffffff";
const margin = { top: 20, bottom: 20, left: 20, right: 20 };

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
}>`
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  margin-right: 15px;
`;

export type RadialBarsProps = {
  width: number;
  height: number;
};

export default function Visxtest({ width, height }: RadialBarsProps) {
  const [musicData, setMusicData] = useState<number[]>([]);
  const [songName, setSongName] = useState<string>();
  const [artist, setArtist] = useState<string>();
  const [loading, setLoading] = useState(true);
  // useEffect(() => {
  //   setMusicData(MusicData.value);
  // }, [MusicData]);
  useEffect(() => {
    function fetchData() {
      try {
        // 바로 데이터를 설정
        setMusicData(MusicData.value);
        setSongName(MusicData.title);
        setArtist(MusicData.artist);
      } catch (error) {
        console.error("Error loading data:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, []);
  console.log(musicData);

  const xMax = width - margin.left - margin.right;
  const yMax = height - margin.top - margin.bottom;
  const radiusMax = Math.min(xMax, yMax) / 2;

  const innerRadius = radiusMax / 3;

  // const xDomain = useMemo(
  //   () => musicData.map((_, index) => index),
  //   [MusicData]
  // );
  const xDomain = useMemo(() => {
    const domain = musicData.map((_, index) => index);
    return domain;
  }, [musicData]);

  const xScale = useMemo(
    () =>
      scaleBand<number>({
        range: [0, 2 * Math.PI],
        domain: xDomain,
        padding: 0.2,
      }),
    [xDomain]
  );

  const yScale = useMemo(
    () =>
      scaleRadial<number>({
        range: [innerRadius, radiusMax],
        domain: [0, Math.max(...musicData)],
      }),
    [innerRadius, radiusMax]
  );
  // Check if data is still loading
  if (loading) {
    return <div>Loading...</div>;
  }
  return width < 10 ? null : (
    <>
      {" "}
      <Text fontFamily="Bd" fontSize="20px">
        {songName} by {artist}
      </Text>
      <svg width={width} height={height}>
        <GradientLightgreenGreen id="radial-bars-green" />
        <rect
          width={width}
          height={height}
          fill="url(#radial-bars-green)"
          rx={14}
        ></rect>
        <Text fontFamily="Mon" fontSize="20px">
          {songName}
        </Text>
        <Group top={yMax / 2 + margin.top} left={xMax / 2 + margin.left}>
          {musicData.map((d, index) => {
            // const startAngle = xScale(index);
            // const endAngle = startAngle + xScale.bandwidth();
            console.log(d);
            console.log(index);
            console.log(xDomain);
            const startAngle = xScale(index);
            const endAngle =
              startAngle !== undefined ? startAngle + xScale.bandwidth() : 0;

            if (startAngle === undefined) {
              console.warn(`startAngle is undefined for index: ${index}`);
              return null;
            }

            const outerRadius = yScale(d) ?? 0;
            return (
              <>
                <Arc
                  key={`bar-${index}`}
                  cornerRadius={4}
                  startAngle={startAngle}
                  endAngle={endAngle}
                  outerRadius={outerRadius}
                  innerRadius={innerRadius}
                  fill={barColor}
                />
              </>
            );
          })}
        </Group>
      </svg>
    </>
  );
}
