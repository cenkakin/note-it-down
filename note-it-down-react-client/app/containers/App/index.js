/**
 *
 * App
 *
 * This component is the skeleton around the actual pages, and should only
 * contain code that should be seen on all pages. (e.g. navigation bar)
 */

import React from 'react';
import { Helmet } from 'react-helmet';
import { Route, Switch } from 'react-router-dom';

import HomePage from 'containers/HomePage/Loadable';
import LoginPage from 'containers/LoginPage/Loadable';
import FeaturePage from 'containers/FeaturePage/Loadable';
import NotFoundPage from 'containers/NotFoundPage/Loadable';

export default function App() {
  return (
    <div>
      <Helmet
        titleTemplate="%s - React.js Boilerplate"
        defaultTitle="React.js Boilerplate"
      >
        <meta name="description" content="A React.js Boilerplate application"/>
      </Helmet>
      <Switch>
        <Route exact path="/" component={HomePage}/>
        <Route path="/login" component={LoginPage}/>
        <Route path="/features" component={FeaturePage}/>
        <Route path="" component={NotFoundPage}/>
      </Switch>
    </div>
  );
}
