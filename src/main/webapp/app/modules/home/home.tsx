import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row className="justify-content-center align-items-center text-center">
      <Col md="9">
        <h1 className="display-4">
          <Translate contentKey="home.title" interpolate={{ username: account.login }}>
            Welcome, {account.login}!
          </Translate>
        </h1>
        {account?.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>
            <Link to="/login">
              <Button color="primary" size="lg" className="login-button">
                <Translate contentKey="global.messages.info.authenticated.button">Sign in</Translate>
              </Button>
            </Link>

            {/* Link for registration is commented out */}
            {/* <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messa ges.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>*/}
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
