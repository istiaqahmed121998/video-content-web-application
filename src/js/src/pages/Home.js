import { useEffect, useState } from "react";
import { Col, Row } from "react-bootstrap";
import axios from "../api/axios";
import VideoContent from "../components/VideoContent";
import "../css/style.css";

const Home = () => {
  const [videos, setVideos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errMsg, setErrMsg] = useState("");

  useEffect(() => {
    const getVideos = async (count) => {
      try {
        const response = await axios.get(`video/?size=${count}`, {
          headers: { "Content-Type": "application/json" },
        });

        if (response?.data && response.data?.httpStatusCode===200) {
          response.data.data.videoContents && response.data.data.videoContents.length > 0
            ? setVideos(response.data.data.videoContents)
            : setErrMsg("Not enough video");
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
    getVideos(5);
  }, []);
  return (
    <>
      <div className="p-1 mb-4 bg-light rounded-3">
        <div className="container-fluid py-1">
          <h1 className="display-5 fw-bold">Lastest Videos</h1>
        </div>
      </div>
      <div className="py-5 px-2">
        <Row>
          <Col lg="1"></Col>
          <Col lg="10">
            
            <Row className="justify-content-sm-center g-4" xs={1} md={2} lg={2}>
              {!isLoading && videos.length > 0 ? (
              
                videos.map((video, i) => (
                  <VideoContent key={i} video={video}></VideoContent>
                  
                ))
              ) : (
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
              )}
            </Row>
          </Col>
        </Row>
      </div>
    </>
  );
};

export default Home;
