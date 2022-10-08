import { Col, Card, Row, Button } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faThumbsUp } from "@fortawesome/free-solid-svg-icons";
import { faThumbsDown } from "@fortawesome/free-regular-svg-icons";
import { Link } from "react-router-dom";
import moment from "moment";
const VideoContent = (props) => {
  return (
    <>
      <Col xs md="6" lg="6">
        <Card className="h-100">
          <Link to={`../video/${props.video.id}`}>
            <img
              className="card-img-top"
              alt=""
              src={`https://i.ytimg.com/vi/${props.video.videoId}/sddefault.jpg`}
            />
          </Link>

          <Card.ImgOverlay>
            <Link
              className="text-decoration-none link-dark"
              to={`../video/${props.video.id}`}
            >
              <div className="shadow p-3 mb-5 bg-white rounded opacity-75">
                <h5>Link</h5>
              </div>
            </Link>
          </Card.ImgOverlay>

          <Card.Body>
            <Row className="align-middle">
              <Col xs lg="4">
                <div className="vstack gap-1">
                  <p className="text-start my-auto">Added </p>

                  <p className="text-start my-auto">
                    {props.video.addedByUser.profileName}
                  </p>
                  <p className="text-start my-auto">
                    {moment(props.video.addedAt).format("MMM Do YY")}
                  </p>
                </div>
              </Col>
              <Col md="4" lg="4" className="justify-content-end">
                <div className="vstack gap-1">
                  <div className="mx-auto">{props.video.likeCount}</div>
                  <button
                    type="button"
                    class="btn btn-primary active"
                    data-bs-toggle="button"
                    aria-pressed="true"
                  >
                    <span>
                      <FontAwesomeIcon icon={faThumbsUp} size="lg" />
                    </span>
                    <span>Like</span>
                  </button>
                </div>
              </Col>
              <Col xs md="5" lg="4" className="justify-content-end">
                <div className="vstack gap-1">
                  <div className="mx-auto">10k Dislike</div>
                  <Button variant="danger">
                    <span>
                      <FontAwesomeIcon icon={faThumbsDown} size="lg" />
                    </span>
                    <p style={{ display: "inline" }}>Dislike</p>
                    {/* <span ><p>Dislike</p></span> */}
                  </Button>
                </div>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      </Col>
    </>
  );
};

export default VideoContent;
