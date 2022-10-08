import {
  faShare,
  faThumbsDown,
  faThumbsUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import { Card, Col, ListGroup, Row } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { Image } from "react-bootstrap";
import auth from "../hooks/useAuth";
import { createAvatar } from "@dicebear/avatars";
import * as style from "@dicebear/avatars-initials-sprites";
import Comment from "../components/Comment";
import axios from "../api/axios";
const intial = auth?.email
  ? auth.email
      .split(" ")
      .map((n) => n[0])
      .join("")
  : "";
let svg = createAvatar(style, {
  seed: intial,
  dataUri: true,
  size: 50,
});
const VideoPage = () => {
  const [isMore, setIsMore] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [video, setVideo] = useState({});
  const [likes, setLikes] = useState(0);
  const [comments, setComments] = useState([]);
  const [isCommentUpdated, setIsCommentUpdated] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const params = useParams();
  const [youtubeInfo, setYoutubeInfo] = useState({});
  const [comment,setComment] = useState('');

  useEffect(() => {
    const youtubeVideoInfo = async (videoid) => {
      try {
        const videoResponse = await axios.get(
          `https://www.googleapis.com/youtube/v3/videos?part=id%2C+snippet&id=${videoid}&key=AIzaSyCQTslsFwC9aBDLEGIDMCzN71q-Tercfbk`
        );
        if (videoResponse?.data) {
          if (videoResponse.data.pageInfo.totalResults === 1) {
            setYoutubeInfo({
              title: videoResponse.data.items[0].snippet.title,
              description: videoResponse.data.items[0].snippet.description,
              channelTitle: videoResponse.data.items[0].snippet.channelTitle,
            });
            setIsLoading(false);
          }
        }
      } catch (error) {
        if (!error?.response) {
          setErrMsg("No Server Response");
        } else if (error.response?.status === 400) {
          setErrMsg("Missing Username or Password");
        } else if (error.response?.status === 401) {
          setErrMsg("Unauthorized");
        } else {
          setErrMsg("Login Failed");
        }
      }
    };
    const getVideoInfo = async (id) => {
      try {
        const response = await axios.get(`/video/${id}`, {
          headers: { "Content-Type": "application/json" },
        });
        if (response?.data) {
          const videocontent = response.data.data[0];
          setVideo(videocontent);
          setLikes(videocontent.likeCount);
          youtubeVideoInfo(videocontent.videoId);
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
    getVideoInfo(params.id);
  }, [params]);
  useEffect(()=>{
    setIsCommentUpdated(true)
  },[comments])

  const commentSubmission=(e)=>{
    e.preventDefault();
    alert("Upcoming")
  }
  return (
    <>
      <Row>
        <Col lg={7}>
          <div className="vstack gap-3">
            {isLoading ? (
              <>
                <div class="text-center">
                  <div
                    class="spinner-border"
                    style={{ width: "3rem", height: "3rem" }}
                    role="status"
                  >
                    <span class="visually-hidden">Loading...</span>
                  </div>
                </div>
              </>
            ) : (
              <>
                <div className="ratio ratio-16x9">
                  <iframe
                    src={`https://www.youtube.com/embed/${video.videoId}`}
                    title="YouTube video"
                    allowFullScreen
                  ></iframe>
                </div>
                <Row>
                  <p className="h5">{youtubeInfo.title}</p>
                </Row>
                <Row>
                  <Col lg={10}>
                    <span className="pe-3">
                      <strong>15,20,894 views 03-Dec-2017</strong>
                    </span>
                    {!isMore && (
                      <>
                        {youtubeInfo.description
                          .split(" ")
                          .slice(0, 15)
                          .join(" ")}{" "}
                        <span
                          className="bold"
                          style={{ cursor: "pointer" }}
                          onClick={(e) => {
                            setIsMore(true);
                          }}
                        >
                          <strong>more..</strong>
                        </span>
                      </>
                    )}
                    {isMore && youtubeInfo.description}
                  </Col>
                </Row>
              </>
            )}

            <Row>
              <Col lg={12}>
                <span
                  className="pe-3"
                  style={{ cursor: "pointer" }}
                  onClick={(e) => {
                    console.log("Liked");
                  }}
                >
                  <FontAwesomeIcon
                    icon={faThumbsUp}
                    size="lg"
                    className="font-upload"
                  ></FontAwesomeIcon>
                  <span className="ps-2">Like </span>
                </span>
                <span
                  className="px-3"
                  onClick={(e) => {
                    console.log("Dislike");
                  }}
                  style={{ cursor: "pointer" }}
                >
                  <FontAwesomeIcon
                    icon={faThumbsDown}
                    size="lg"
                  ></FontAwesomeIcon>
                  <span className="ps-2">Dislike</span>
                </span>
                <span
                  className="px-3"
                  onClick={(e) => {
                    console.log("Share");
                  }}
                  style={{ cursor: "pointer" }}
                >
                  <FontAwesomeIcon icon={faShare}></FontAwesomeIcon>
                  Share
                </span>
              </Col>
            </Row>
            <Row className="justify-content-around">
              <Col lg={5} className="border rounded border-light">
                <Image
                  src="https://yt3.ggpht.com/ytc/AMLnZu9zaROEq2bUuxEyDgsL_W8FXknYbfgA5RH2-XXUGw=s88-c-k-c0x00ffffff-no-rj"
                  roundedCircle
                />
                <span className=" px-3 my-2">SVF </span>
              </Col>
              <Col lg={5} className="border rounded border-light">
                <Image
                  src="https://yt3.ggpht.com/ytc/AMLnZu9zaROEq2bUuxEyDgsL_W8FXknYbfgA5RH2-XXUGw=s88-c-k-c0x00ffffff-no-rj"
                  roundedCircle
                />
                <span className=" px-3 my-2">SVF </span>
              </Col>
            </Row>
            <Row className="">
              <Col lg={5} className="border border-light">
                <span className="pe-3">500k</span>Comments Coming soon
              </Col>
            </Row>
            <Row>
              <form className="row row-cols-lg-auto g-2 align-items-center" onSubmit={commentSubmission}>
                <Col
                  xs={9}
                  sm={10}
                  md={10}
                  lg={10}
                  className={"d-flex justify-content-center"}
                >
                  <div className="input-group">
                    <span className="input-group-text">
                      <img
                        src={svg}
                        className="rounded mx-auto d-block"
                        alt="avatar"
                      />
                    </span>
                    <textarea
                      className="form-control"
                      aria-label="With textarea"
                      id="comment"
                      name="comment"
                      value={comment}
                      onChange={e=>setComment(e.target.value)}
        
                    ></textarea>
                  </div>
                </Col>
                <Col
                  xs={3}
                  sm={2}
                  md={2}
                  lg={2}
                  className={"d-flex justify-content-center"}
                >
                  <button type="submit" className="btn btn-primary">
                    Submit
                  </button>
                </Col>
              </form>
            </Row>
            <Row>
              <Col>
                <ul className="list-group list-group-flush">
                  {comments.length > 0 && 
                  comments.map((comm,i)=>{
                    <Comment
                      user={comm.user}
                      commentTime={comm.addedAt}
                      comment={
                        comm.commenttext}
                      like={comm.like}
                      dislike={comm.like}
                    />
                  })

                  }
                </ul>
              </Col>
            </Row>
          </div>
        </Col>
        <Col lg={5} className="justify-center">
          <div className="px-5">
            <ListGroup variant="flush">
              <ListGroup.Item>
                <Card border="light">
                  <Card.Img
                    variant="top"
                    src=" https://i.ytimg.com/vi/yb-kYt1lpnI/maxresdefault.jpg"
                  />
                  <Card.Body>
                    <Card.Title>Light Card Title</Card.Title>
                    <Card.Text>
                      Some quick example text to build on the card title and
                      make up the bulk of the card's content.
                    </Card.Text>
                  </Card.Body>
                </Card>
              </ListGroup.Item>
              <ListGroup.Item>
                <Card border="light">
                  <Card.Img
                    variant="top"
                    src=" https://i.ytimg.com/vi/yb-kYt1lpnI/maxresdefault.jpg"
                  />
                  <Card.Body>
                    <Card.Title>Light Card Title</Card.Title>
                    <Card.Text>
                      Some quick example text to build on the card title and
                      make up the bulk of the card's content.
                    </Card.Text>
                  </Card.Body>
                </Card>
              </ListGroup.Item>
              <ListGroup.Item>
                {" "}
                <Card border="light">
                  <Card.Img
                    variant="top"
                    src=" https://i.ytimg.com/vi/yb-kYt1lpnI/maxresdefault.jpg"
                  />
                  <Card.Body>
                    <Card.Title>Light Card Title</Card.Title>
                    <Card.Text>
                      Some quick example text to build on the card title and
                      make up the bulk of the card's content.
                    </Card.Text>
                  </Card.Body>
                </Card>
              </ListGroup.Item>
            </ListGroup>
          </div>
        </Col>
      </Row>
    </>
  );
};

export default VideoPage;
