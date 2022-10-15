import React from "react";
import {useState, useEffect} from "react";
import useRefreshToken from "../hooks/useRefreshToken";
import useAuth from "../hooks/useAuth";
import {Outlet} from "react-router-dom";
import useLocalStorage from "../hooks/useLocalStorage";

function PersistLogin() {
    const [isLoading, setIsLoading] = useState(true);
    const [remember_me] = useLocalStorage("remember_me", false);

    const refresh = useRefreshToken();
    const {auth} = useAuth();
    useEffect(() => {
        let isMounted = true;
        const verifyRefreshToken = async () => {
            try {
                await refresh();
            } catch (error) {
                console.error(error);
            } finally {
                isMounted && setIsLoading(false);
            }
        };
        if (!auth?.access_token && remember_me) {
            verifyRefreshToken();
        } else {
            setIsLoading(false);
        }

        return () => (isMounted = false);
    }, [auth, refresh, remember_me]);
    return (
        <>
            {
                <>
                    {!remember_me ? (
                        <Outlet/>
                    ) : isLoading ? (
                        <p>Loading...</p>
                    ) : (
                        <Outlet/>
                    )}
                </>
            }
        </>
    );
}

export default PersistLogin;
