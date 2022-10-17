import { useEffect, useState } from "react";
import { Pagination, Row } from "react-bootstrap";
import VideoContent from "../components/VideoContent";
import { useParams } from "react-router-dom";
import axios from "../api/axios";
const Videos = () => {
  const [videos, setVideos] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const { id } = useParams();
  const [totalPage, setTotalPage] = useState(null);
  const [nextPage, setNextPage] = useState("");
  const [prevPage, setPrevPage] = useState("");
  useEffect(() => {
    const getVideos = async (count = 0) => {
      try {
        if (count !== 0) {
          count = count - 1;
        }
        const response = await axios.get(`video/?page=${count}`, {
          headers: { "Content-Type": "application/json" },
        });

        if (response?.data && response.data?.httpStatusCode === 200) {
          response.data.data.videoContents &&
          response.data.data.videoContents.length > 0
            ? setVideos(response.data.data.videoContents)
            : setErrMsg("Not enough video");
          setTotalPage(response.data.data.totalPages);
          response.data.data.nextPage !== null &&
            setNextPage(response.data.data.nextPage);
          response.data.data.prevPage !== null &&
            setPrevPage(response.data.data.prevPage);
          setIsLoading(false);
        }
      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg(err.response.data.message);
        } else if (err.response?.status === 401) {
          setErrMsg("Login Failed");
        } else {
          setErrMsg("No Server Response");
        }
        setIsLoading(false);
      }
    };
    id
      ? (document.title = "Video Page - " + id)
      : (document.title = "Video Page");
    getVideos(id);
  }, [id]);
  //   useEffect(() => {
  //     document.title = "Video Page - 1"
  //  }, []);
  return (
    <>
      <div className="py-5">
        <Row className="justify-content-sm-center g-4" xs={1} md={2}>
          {isLoading || errMsg ? (
            <>
              <div class="d-flex justify-content-center">
                {errMsg ? (
                  <div class="alert alert-danger" role="alert">
                    {errMsg}
                  </div>
                ) : (
                  <div class="spinner-border" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                )}
              </div>
            </>
          ) : (
            <>
              {videos.map((video, i) => (
                <VideoContent key={i} video={video}></VideoContent>
              ))}
            </>
          )}
        </Row>
      </div>

      {videos.length > 0 && (
        <Pagination className="justify-content-center">
          {prevPage && <Pagination.Prev href={`./` + (parseInt(id) - 1)} />}
          {[...Array(totalPage)].map((x, i) => (
            <Pagination.Item
              key={i + 1}
              className={
                (id - 1 === i || (id === undefined && i === 0)) && "active"
              }
              href={
                id === undefined
                  ? `videos/` + (parseInt(i) + 1)
                  : `./${parseInt(i) + 1}`
              }
            >
              {i + 1}{" "}
            </Pagination.Item>
          ))}
          {nextPage && id === undefined && (
            <Pagination.Next href={`videos/` + 2}></Pagination.Next>
          )}
          {nextPage && id !== undefined && (
            <Pagination.Next
              href={
                id === undefined
                  ? `videos/` + (parseInt(id) + 1)
                  : `./${parseInt(id) + 1}`
              }
            ></Pagination.Next>
          )}
        </Pagination>
      )}
    </>
  );
};

export default Videos;
