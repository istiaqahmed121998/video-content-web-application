import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import axios from "../api/axios";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import { toast } from 'react-toastify';
const Body = styled.div`
  height: 100%;
  display: -ms-flexbox;
  display: -webkit-box;
  display: flex;
  -ms-flex-align: center;
  -ms-flex-pack: center;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  padding-top: 40px;
  padding-bottom: 40px;
  background-color: #f5f5f5;
  min-height: 100vh;
`;
const Register = () => {
  const [email, setEmail] = useState("");
  const [firstname, setFirstName] = useState("");
  const [lastname, setLastName] = useState("");
  const [password, setPassword] = useState("");
  const [errormessage, setErrorMessage] = useState([]);
  const [message, setMessage] = useState();
  const [show, setShow] = useState(false);
  const navigate = useNavigate();
  const MySwal = withReactContent(Swal);
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "/registration",
        JSON.stringify({ firstname, lastname, email, password }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      if (response?.data) {
        setMessage(response.data.message);
        setShow(true);
        const { value: code } = await MySwal.fire({
          title: "Input Confirm Code",
          input: "text",
          inputLabel: "Check your email address",
          inputPlaceholder: "Enter your code",
        });
        if (code) {
          try{
            const confirmResponse = await axios.get(`/confirm?token=${code}`, {
              headers: { "Content-Type": "application/json" },
              withCredentials: true,
            });
            if (confirmResponse?.data) {
              if(confirmResponse?.data.status===200){
                toast.success(confirmResponse?.data.message)
                navigate('/login')
              }
            }
          }
          catch(e){
            console.log(e)
            if (!e?.response) {
              toast.error("Server Error")
            } else {
              toast.error(e?.response.data.message)
            }
          }
        }
      }
    } catch (err) {
      if (!err?.response) {
        setErrorMessage(["Server Error"]);
        setShow(true);
      } else {
        setErrorMessage(err?.response.data.errors);
        setShow(true);
      }
    }
  };
  useEffect(() => {
    setErrorMessage([]);
    setMessage("");
    setShow(false);
  }, [email, password, lastname, firstname]);
  useEffect(() => {
    document.title = "Register"
 }, []);
  return (
    <>
      <Body className="text-center">
        <div
          className="modal modal-signin position-static d-block"
          tabIndex="-1"
          role="dialog"
          id="modalSignin"
        >
          <div className="modal-dialog" role="document">
            <div className="modal-content rounded-4 shadow">
              <div className="modal-header p-5 pb-4 border-bottom-0">
                <h2 className="fw-bold mb-0">Sign up for free</h2>
              </div>

              <div className="modal-body p-5 pt-0">
                {show && message ? (
                  <div className="alert alert-success" role="alert">
                    <p>{message}</p>
                  </div>
                ) : (
                  <></>
                )}
                {show && errormessage.length > 0 ? (
                  <div className="alert alert-danger" role="alert">
                    <ul className="list-group list-group-flush">
                      {errormessage.map((val) => (
                        <li className="list-group-item list-group-item-danger">
                          {val}
                        </li>
                      ))}
                    </ul>
                  </div>
                ) : (
                  <></>
                )}
                <form onSubmit={handleSubmit}>
                  <div className="form-floating mb-3">
                    <input
                      type="text"
                      className="form-control rounded-3"
                      id="inputFname"
                      placeholder="First Name"
                      onChange={(e) => setFirstName(e.target.value)}
                      required
                    />
                    <label htmlFor="floatingInput">First Name</label>
                  </div>
                  <div className="form-floating mb-3">
                    <input
                      type="text"
                      className="form-control rounded-3"
                      id="inputLname"
                      placeholder="Last Name"
                      onChange={(e) => setLastName(e.target.value)}
                      required
                    />
                    <label htmlFor="floatingInput">Last Name</label>
                  </div>
                  <div className="form-floating mb-3">
                    <input
                      type="email"
                      className="form-control rounded-3"
                      id="inputEmail"
                      placeholder="name@example.com"
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                    <label htmlFor="floatingInput">Email address</label>
                  </div>
                  <div className="form-floating mb-3">
                    <input
                      type="password"
                      className="form-control rounded-3"
                      id="inputPassword"
                      placeholder="Password"
                      onChange={(e) => setPassword(e.target.value)}
                      required
                    />
                    <label htmlFor="floatingPassword">Password</label>
                  </div>
                  <button
                    className="w-100 mb-2 btn btn-lg rounded-3 btn-primary"
                    type="submit"
                  >
                    Sign up
                  </button>
                  <small className="text-muted">
                    By clicking Sign up, you agree to the terms of use.
                  </small>
                </form>
              </div>
            </div>
          </div>
        </div>
      </Body>
    </>
  );
};

export default Register;
