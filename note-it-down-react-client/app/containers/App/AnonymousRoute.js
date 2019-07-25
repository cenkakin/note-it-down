import React from 'react';
import { Redirect, Route } from 'react-router-dom';
import PropTypes from 'prop-types';

export const AnonymousRoute = ({ component: Component, loggedIn, ...rest }) => (
  <Route
    {...rest}
    render={props =>
      loggedIn ? <Redirect to="/workspace" /> : <Component {...props} />
    }
  />
);

AnonymousRoute.propTypes = {
  component: PropTypes.func,
  loggedIn: PropTypes.bool,
};
