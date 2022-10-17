import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styled from "styled-components";
import { toast } from "react-toastify";
import axios from "../api/axios";
import useAuth from "../hooks/useAuth";
import useToggle from "../hooks/useToggle";


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
const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errMsg, setErrMsg] = useState("");
  const { auth, setAuth } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [check, toggleCheck] = useToggle("remember_me", false);

  useEffect(() => {
    document.title = "Login"
 }, []);
  const handleSubmit = async (e) => {
    e.preventDefault();
    toast.promise(
      axios.post("/login", JSON.stringify({ email, password }), {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      }),
      {
        pending: {
          render() {
            return "I'm loading";
          },
          icon: false,

        },
        success: {
          render({ data }) {
            const access_token = data?.data?.access_token;
            const profile_name = data?.data?.profile_name;
            setAuth({ profile_name, email, access_token });
            navigate(
              location?.state?.previousUrl ? location.state.previousUrl : "/"
            );
            return `Hello ${profile_name}`;
          },
          // other options
          icon: "🟢"
        },
        error: {
          render({ data }) {
            console.log(data);
            // When the promise reject, data will contains the error
            if (!data?.response) {
              setErrMsg("No Server Response");
            } else{
              setErrMsg(data.response.data.message)
            }
            return `Try again`;
            
          },
          className: "black-background",
        },
      }
    );

    // try {
    //   const response = await
    //   if (response?.data) {
    //     const access_token = response?.data?.access_token;
    //     setAuth({ email, access_token });
    //     navigate(
    //       location?.state?.previousUrl ? location.state.previousUrl : "/"
    //     );
    //   }
    // } catch (err) {
    //   if (!err?.response) {
    //     setErrMsg("No Server Response");
    //   } else if (err.response?.status === 400) {
    //     setErrMsg("Missing Username or Password");
    //   } else if (err.response?.status === 401) {
    //     setErrMsg("Unauthorized");
    //   } else {
    //     setErrMsg("Login Failed");
    //   }
    // }
  };
  useEffect(() => {
    let isAuth = auth.email;
    if (isAuth && isAuth !== null) {
      navigate("/");
    }
  }, [auth.email, navigate]);
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
                <h2 className="fw-bold mb-0">Sign In</h2>
              </div>

              <div className="modal-body p-5 pt-0">
                {errMsg ? (
                  <div className="alert alert-success" role="alert">
                    <p>{errMsg}</p>
                  </div>
                ) : (
                  <></>
                )}
                <form onSubmit={handleSubmit}>
                  <div className="form-floating mb-3">
                    <input
                      type="email"
                      className="form-control rounded-3"
                      id="inputEmail"
                      placeholder="name@example.com"
                      onChange={(e) => setEmail(e.target.value)}
                      value={email}
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
                      value={password}
                      required
                    />
                    <label htmlFor="floatingPassword">Password</label>
                  </div>
                  <div className="checkbox mb-3">
                    <label>
                      <input
                        type="checkbox"
                        id="remember_me"
                        onChange={toggleCheck}
                        checked={check}
                      />
                      Remember me
                    </label>
                  </div>
                  <button
                    className="w-100 mb-2 btn btn-lg rounded-3 btn-primary"
                    type="submit"
                  >
                    Login
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </Body>
    </>
  );
};

export default Login;
