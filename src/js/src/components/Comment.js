import { faThumbsDown, faThumbsUp } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Col, Row } from "react-bootstrap";

import { createAvatar } from "@dicebear/avatars";
import * as style from '@dicebear/avatars-initials-sprites';
function Comment(props) {
const intial=props.user.split(" ").map((n)=>n[0]).join("");
  let svg = createAvatar(style, {
    seed: intial,
    dataUri: true,
    size: 50,
  });
  return (
    <li class="list-group-item">
      <Row className="align-items-center">
        <Col className="d-flex justify-content-start" xs={2} md={1} lg={2} xl={1}>
          <img src={svg} class="rounded mx-auto d-block" alt="avatar"/>
        </Col>
        <Col xs={10} md={11} lg={10} xl={11}>
          <div className="vstack g-1">
            <div className="row">
              <div className="col py-2">
                <p class="fw-bolder" style={{ display: "inline" }}>
                  {props.user}
                </p>{" "}
                <p style={{ display: "inline" }}>{props.commentTime} Ago</p>
              </div>
            </div>
            <div>
              <p>
                <p class="lh-sm font-monospace">{props.comment}</p>
              </p>
            </div>
            <div>
              <FontAwesomeIcon icon={faThumbsUp} size="sm"></FontAwesomeIcon>
              <span className="px-2">
                <p style={{ display: "inline" }}>{props.like}</p>
              </span>
              <FontAwesomeIcon icon={faThumbsDown} size="sm"></FontAwesomeIcon>
              <span className="px-2">
                <p style={{ display: "inline" }}>{props.dislike}</p>
              </span>
            </div>
          </div>
        </Col>
      </Row>
    </li>
  );
}

export default Comment;
