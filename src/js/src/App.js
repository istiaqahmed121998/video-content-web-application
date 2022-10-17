import "bootstrap/dist/css/bootstrap.css";
import 'react-toastify/dist/ReactToastify.css';
import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Videos from "./pages/Videos";
import NoMatch from "./pages/NoMatch";
import VideoPage from "./pages/VideoPage";
import Layout from "./layout/Layout";
import Login from "./pages/Login";
import Register from "./pages/Register";
import AddVideo from "./pages/AddVideo";
import RequireAuth from "./components/RequireAuth";
import PersistLogin from "./components/PersistLogin";
import ConfirmEmail from "./pages/ConfirmEmail";
function App() {
  return (
    <>
      {/* Routes nest inside one another. Nested route paths build upon
            parent route paths, and nested route elements render inside
            parent route elements. See the note about <Outlet> below. */}
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route element={<PersistLogin />}>
            <Route index element={<Home />} />
            <Route path="videos" element={<Videos />} />
            <Route path="videos/:id" element={<Videos />} />
            <Route element={<RequireAuth />}>
              <Route path="dashboard/videos/add" element={<AddVideo />} />
            </Route>
            <Route path="video/:id" element={<VideoPage />} />
          </Route>
          <Route path="*" element={<NoMatch />} />
        </Route>
        <Route path="confirm/:token" element={<ConfirmEmail />} />
        <Route path="login" element={<Login />} />
        <Route path="register" element={<Register />} />
      </Routes>
    </>
  );
}

export default App;
