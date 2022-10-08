import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "../api/axios";
const AddVideo = () => {
  const [videourl, setVideoUrl] = useState("");
  const [errormessage, setErrorMessage] = useState([]);
  const [message, setMessage] = useState([]);
  const [show, setShow] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  useEffect(() => {
    setErrorMessage([]);
    setMessage("");
    setShow(false);
  }, [videourl]);
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "/video/add",
        JSON.stringify({ url: videourl }),
        {
          headers: { "Content-Type": "application/json" ,},
          //   withCredentials: true,
        }
      );
      if (response?.data) {
        setMessage(response.data.message);
        setShow(true);
      }
    } catch (err) {
      if (!err?.response) {
        setErrorMessage(["Server Error"]);
        setShow(true);
      } else if (err.response.status) {
        setErrorMessage(err.response.data.errors);
        setShow(true);

      }
    }
  };

  return (
    <div style={{ minHeight: "70vh" }}>
      <div
        class="modal modal-signin position-static d-block py-5"
        tabindex="-1"
        role="dialog"
        id="modalSignin"
      >
        <div class="modal-dialog modal-xl" role="document">
          <div class="modal-content rounded-4 shadow">
            <div class="modal-header p-5 pb-4 border-bottom-0">
              <h2 class="fw-bold mb-0">ADD VIDEO</h2>
            </div>

            <div class="modal-body p-5 pt-0">
              {show && message ? (
                <div class="alert alert-success" role="alert">
                  <p>{message}</p>
                </div>
              ) : (
                <></>
              )}
              {show && errormessage.length > 0 ? (
                <div class="alert alert-danger" role="alert">
                  <ul class="list-group list-group-flush">
                    {errormessage.map((val) => (
                      <li class="list-group-item list-group-item-danger">
                        {val}
                      </li>
                    ))}
                  </ul>
                </div>
              ) : (
                <></>
              )}
              <form onSubmit={handleSubmit}>
                <div class="form-floating mb-3">
                  <input
                    type="text"
                    class="form-control rounded-3"
                    id="floatingurl"
                    placeholder="Youtube Video URL"
                    onChange={(e) => setVideoUrl(e.target.value)}
                    value={videourl}
                  />
                  <label for="floatingPassword">Youtube Video Url</label>
                </div>
                <button
                  class="w-100 mb-2 btn btn-lg rounded-3 btn-primary"
                  type="submit"
                >
                  ADD
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddVideo;
