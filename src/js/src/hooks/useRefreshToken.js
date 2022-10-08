import axios from "../api/axios";
import useAuth from "./useAuth";
import jwt_decode from "jwt-decode";
const useRefreshToken = () => {
  const {  setAuth } = useAuth();
  
  const refresh = async () => {
    try {
      const response = await axios.get("token/refresh", {
        withCredentials: true,
      });
      if (response.data?.access_token) {
        setAuth({
          email: jwt_decode(response.data.access_token).sub,
          access_token: response.data.access_token,
        });
      }
    } catch (error) {
      if (!error?.response) {
        console.error("No Server Response");
      } else if (error.response?.status === 403) {
        console.error("need to log in again");
      }
    }
  };
  return refresh;
};

export default useRefreshToken;
