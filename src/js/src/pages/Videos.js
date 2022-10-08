import { Pagination, Row } from "react-bootstrap";
import VideoContent from "../components/VideoContent";

const Videos = () => {
  return (
    <>
      <div className="py-5">
        <Row className="justify-content-sm-center g-4" xs={1} md={2}>
          <VideoContent></VideoContent>
          <VideoContent></VideoContent>
          <VideoContent></VideoContent>
          <VideoContent></VideoContent>
          <VideoContent></VideoContent>
        </Row>
      </div>
      <Pagination className="justify-content-center">
        <Pagination.First />
        <Pagination.Prev />
        <Pagination.Item>{1}</Pagination.Item>
        <Pagination.Ellipsis />

        <Pagination.Item>{10}</Pagination.Item>
        <Pagination.Item>{11}</Pagination.Item>
        <Pagination.Item active>{12}</Pagination.Item>
        <Pagination.Item>{13}</Pagination.Item>
        <Pagination.Item disabled>{14}</Pagination.Item>

        <Pagination.Ellipsis />
        <Pagination.Item>{20}</Pagination.Item>
        <Pagination.Next />
        <Pagination.Last />
      </Pagination>
    </>
  );
};

export default Videos;
