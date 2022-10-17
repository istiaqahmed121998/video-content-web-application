import { useEffect } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import axios from "../api/axios";
import { toast } from 'react-toastify';
const ConfirmEmail = () => {
  const navigate = useNavigate();
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
  const { token } = useParams();
  useEffect(() => {
    const getToken = async (code) => {
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
        if (!e?.response) {
          toast.error("Server Error")
        } else {
          toast.error(e?.response.data.message)
        }
        navigate('/')
      }
    };

    getToken(token);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);
  return (
    <Body>
      <div class="h-100 d-flex align-items-center justify-content-center">
        <div
          class="spinner-border"
          style={{ width: "10rem", height: "10rem" }}
          role="status"
        >
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
    </Body>
  );
};

export default ConfirmEmail;
