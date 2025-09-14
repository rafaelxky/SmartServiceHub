import { useState } from 'preact/hooks';
import { Form, FormControl, Navbar, Nav, Container } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const Header = () => {
  const [search, setSearch] = useState('');

  return (
    <Navbar bg="light" expand="lg" fixed="top" className="shadow-sm w-100 mb-4">
      <Container className="justify-content-between">
        <Navbar.Brand href="#">MyApp</Navbar.Brand>
        <Nav className="me-auto">
          <Nav.Link href="#">Home</Nav.Link>
          <Nav.Link href="#">Account</Nav.Link>
          <Nav.Link href="#">Users</Nav.Link>
        </Nav>
        <Form className="d-flex">
          <FormControl
            type="search"
            placeholder="Search"
            className="me-2"
            value={search}
            onInput={(e: any) => setSearch(e.target.value)}
            style={{ height: '38px', minWidth: '150px' }}
          />
        </Form>
      </Container>
    </Navbar>
  );
};

export default Header;
