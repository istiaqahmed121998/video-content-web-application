import { Outlet } from "react-router-dom";
import NavbarLayout from "./NavbarLayout";
import { Container } from "react-bootstrap";

function Layout() {
  return (
    <>
      <header className="p-2 mb-3 border-bottom">
        <Container>
          <NavbarLayout></NavbarLayout>
        </Container>
      </header>
      <main>
        <Container>
          <Outlet />
        </Container>
      </main>
    </>
  );
}
export default Layout;
