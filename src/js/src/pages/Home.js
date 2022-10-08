import VideoContent from "../components/VideoContent";
import { Col, Row } from "react-bootstrap";
import "../css/style.css";
import { useEffect, useState } from "react";
import axios from "../api/axios";

const Home = () => {
  const [videos, setVideos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errMsg, setErrMsg] = useState(true);

  useEffect(() => {
    const getVideos = async (count) => {
      try {
        const response = await axios.get(`video/latest/${count}`, {
          headers: { "Content-Type": "application/json" },
        });
        if (response?.data) {
          setVideos(response.data.data);
          setIsLoading(false);
        }
      } catch (err) {
        if (!err?.response) {
          setErrMsg("No Server Response");
        } else if (err.response?.status === 400) {
          setErrMsg(err.response.data.message);
        } else {
          setErrMsg("Login Failed");
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
                    <div class="alert alert-danger" role="alert">
                      {errMsg}
                    </div>
                    <div class="spinner-border" role="status">
                      <span class="visually-hidden">Loading...</span>
                    </div>
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
