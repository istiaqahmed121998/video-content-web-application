import React from "react";
import { useState, useEffect } from "react";
import useRefreshToken from "../hooks/useRefreshToken";
import useAuth from "../hooks/useAuth";
import { Outlet } from "react-router-dom";

function PersistLogin() {
  const [isLoading, setIsLoading] = useState(true);

  const refresh = useRefreshToken();
  const { auth } = useAuth();
  useEffect(() => {
    let isMounted=true
    const verifyRefreshToken = async () => {
      try {
        await refresh();
      } catch (error) {
        console.error(error);
      } finally {
        isMounted && setIsLoading(false);
      }
      
    };
    // acces_token_nai=persistt_nai
    // localhost_access

    if(!auth?.email){
        verifyRefreshToken()
    }
    else{
        setIsLoading(false)
    }
    // !auth?.access_token ? 
    //         !accessToken?
    //             setIsLoading(false) : verifyRefreshToken()
    //         :
    //         setAuth(accessToken)
            

    return ()=> isMounted = false
  },[auth,refresh]);
  return <>{isLoading ? <p>Loading</p> : <Outlet />}</>;
}

export default PersistLogin;
