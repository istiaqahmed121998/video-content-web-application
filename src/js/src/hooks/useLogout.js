import { useLocation, useNavigate } from "react-router-dom";
import axios from "../api/axios";
import useAuth from "./useAuth";
import {  toast } from 'react-toastify';

const useLogout = () => {
  const navigate = useNavigate();

  const location = useLocation();
  const { setAuth } = useAuth();

  const logout = async () => {
    try {
      const response = await axios("/logout", {
        withCredentials: true,
      });
      if (response?.data) {
        toast.info(response.data.message);
        setAuth({});

      }
      navigate(location.pathname);
    } catch (err) {
      console.error(err);
    }
  };

  return logout;
};

export default useLogout;
