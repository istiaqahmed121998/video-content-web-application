import { useLocation, Navigate, Outlet } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const RequireAuth = () => {
  const { auth } = useAuth();
  // const navigate = useNavigate();
  const location = useLocation();
  return auth?.email ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ previousUrl: location }} replace />
    // navigate('/login', {
    //     state: {
    //         previousUrl: location,
    //     },
    //     replace:true
    // })
  );
};

export default RequireAuth;
