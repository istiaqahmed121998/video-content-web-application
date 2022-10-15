import { useEffect } from 'react';
import { Button, Image, Nav, Navbar, NavDropdown } from 'react-bootstrap';
import { Link, useLocation } from 'react-router-dom';
import useAuth from "../hooks/useAuth";
import useLogout from "../hooks/useLogout";
import useToggle from "../hooks/useToggle";
const NavbarLayout = () => {
  const [check, toggleCheck] = useToggle("remember_me", false);
  const { auth } = useAuth();
  const location=useLocation();
  const logout = useLogout();
  const signOut = async()=>{
    await logout();
    if(check)
      toggleCheck()
  }
  useEffect(()=>{

  },[check])

  return (
    <Navbar className="d-flex nav-pills nav-justified flex-wrap align-items-center justify-content-center justify-content-lg-start">
      <Navbar.Brand className="navbar-brand">
        <Link to={'/'} className="text-link">VShare</Link>
      </Navbar.Brand>
      <Button
        className="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </Button>
      <Navbar.Collapse
        className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start"
        id="navbarNav"
      >
        <Nav className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
          <li className="nav-item">
            <Link className={`nav-link ${location.pathname==='/'?'active':""} `} aria-current="page" to='/'>Home</Link>
          </li>
          <li className="nav-item">
            <Link className={`nav-link ${location.pathname==='/videos'?'active':""} `} aria-current="page" to="/videos">Videos</Link>
          </li>
          <li className="nav-item">
            <Link className={`nav-link ${location.pathname==='/dashboard/videos/add'?'active':""} `} aria-current="page" to="/dashboard/videos/add">Add</Link>
          </li>
        </Nav>
      </Navbar.Collapse>
      <form className="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3" role="search">
        <input
          type="search"
          className="form-control form-control-dark text-bg-light"
          placeholder="Search..."
          aria-label="Search"
        />
      </form>
      <div className="text-end">
        {auth?.email || check ? (
          <NavDropdown
            align="end"
            title={
              <Image
                src="https://github.com/mdo.png"
                alt="mdo"
                width="32"
                height="32"
                roundedCircle
              ></Image>
            }
            id="navbarScrollingDropdown"
            style={{backgroundColor:"inherit"}}
          >
            <NavDropdown.Item className="text-small" href="#action3">
              Dashboard
            </NavDropdown.Item>
            <NavDropdown.Divider />
            <NavDropdown.Item className="text-small" onClick={signOut}>
              Log out
            </NavDropdown.Item>
          </NavDropdown>
        ) : (
          <>
            <Link
              to="/login"
              type="button"
              className="btn btn-outline-dark me-2"
            >
              Login
            </Link>
            <Link to="/register" type="button" className="btn btn-warning">
              Register
            </Link>
          </>
        )}
      </div>
    </Navbar>
  );
};
export default NavbarLayout;
