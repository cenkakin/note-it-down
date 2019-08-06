import React, { memo } from 'react';
import { Helmet } from 'react-helmet';
import { Route, Switch } from 'react-router-dom';

import LoginPage from 'containers/LoginPage/Loadable';
import NotFoundPage from 'containers/NotFoundPage/Loadable';
import { SnackbarProvider } from 'notistack';
import { createStructuredSelector } from 'reselect';
import { compose } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import SignUpPage from 'containers/SignUpPage/Loadable';
import WorkspacePage from 'containers/WorkspacePage/Loadable';
import { PrivateRoute } from './PrivateRoute';
import { makeSelectLoggedIn } from './selectors';
import { AnonymousRoute } from './AnonymousRoute';
import Header from '../../components/Header';

export function App({ loggedIn }) {
  return (
    <SnackbarProvider
      maxSnack={3}
      anchorOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
    >
      <div>
        <Helmet titleTemplate="%s - Note it down!" defaultTitle="Note it down!">
          <meta name="description" content="Note your things" />
        </Helmet>
        {loggedIn ? <Header /> : null}
        <Switch>
          <PrivateRoute
            loggedIn={loggedIn}
            exact
            path="/workspace"
            component={WorkspacePage}
          />
          <AnonymousRoute
            loggedIn={loggedIn}
            path="/login"
            component={LoginPage}
          />
          <AnonymousRoute
            loggedIn={loggedIn}
            path="/sign-up"
            component={SignUpPage}
          />
          <Route path="" component={NotFoundPage} />
        </Switch>
      </div>
    </SnackbarProvider>
  );
}

App.propTypes = {
  loggedIn: PropTypes.bool,
};

const mapStateToProps = createStructuredSelector({
  loggedIn: makeSelectLoggedIn(),
});

function mapDispatchToProps() {
  return {};
}

const withConnect = connect(
  mapStateToProps,
  mapDispatchToProps,
);

export default compose(
  withConnect,
  memo,
)(App);
