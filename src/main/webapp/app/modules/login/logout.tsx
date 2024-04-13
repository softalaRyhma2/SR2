import React, { useEffect, useLayoutEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';
import { Button } from 'reactstrap';
import { Translate } from 'react-jhipster';

export const Logout = () => {
  const logoutUrl = useAppSelector(state => state.authentication.logoutUrl);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [loggedOut, setLoggedOut] = useState(false);
  const [secondsRemaining, setSecondsRemaining] = useState(10);
  const [loading, setLoading] = useState(true);
  const [timeoutID, setTimeoutID] = useState<number | undefined>(undefined);

  useEffect(() => {
    if (timeoutID) {
      return () => {
        clearTimeout(timeoutID);
      };
    }
  }, [timeoutID]);

  useLayoutEffect(() => {
    dispatch(logout());
    if (logoutUrl) {
      window.location.href = logoutUrl;
    } else {
      const interval = setInterval(() => {
        setSecondsRemaining(prevSeconds => prevSeconds - 1);
      }, 1000);

      const id = setTimeout(() => {
        setLoggedOut(true);
        setLoading(false);
        navigate('/');
      }, secondsRemaining * 1000);

      setTimeoutID(id);
      return () => clearInterval(interval);
    }
  }, [dispatch, navigate, logoutUrl]);

  const handleHomeButtonClick = () => {
    navigate('/');
  };

  const centerStyle = {
    display: 'flex', // Enables Flexbox
    justifyContent: 'center', // Centers horizontally
    alignItems: 'center', // Centers vertically
  };

  const redirectStyle = {
    marginTop: '20px', // Top margin for redirect container
  };

  return (
    <>
      <div style={centerStyle}>
        {!loggedOut && (
          <div style={{ textAlign: 'center' }}>
            <h4>
              <Translate contentKey="authentication.logoutSuccess">Logged out successfully!</Translate>
              <br />
              <Translate contentKey="authentication.redirectMessage">You will be redirected to the main page in</Translate>{' '}
              {secondsRemaining} <Translate contentKey="authentication.seconds">seconds</Translate>.
            </h4>
          </div>
        )}
      </div>
      <div style={redirectStyle}>
        {!loggedOut && (
          <div style={{ textAlign: 'center' }}>
            <h4>
              <Translate contentKey="authentication.redirectButton">Or press the button</Translate>
            </h4>
            <Button color="primary" size="lg" className="home-button" onClick={handleHomeButtonClick}>
              <Translate contentKey="global.menu.home">Home</Translate>
            </Button>
          </div>
        )}
      </div>
    </>
  );
};

export default Logout;
