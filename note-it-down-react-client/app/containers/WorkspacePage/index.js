import React, { memo, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { createStructuredSelector } from 'reselect';

import { useInjectReducer } from 'utils/injectReducer';
import { useInjectSaga } from 'utils/injectSaga';
import reducer from './reducer';
import saga from './saga';
import { makeSelectUser } from '../App/selectors';
import { loadWorkspace } from './actions';
import { makeSelectWorkspace } from './selectors';

const key = 'workspace';

export function WorkspacePage({ user, onLoadWorkspace, workspace }) {
  useInjectReducer({ key, reducer });
  useInjectSaga({ key, saga });

  useEffect(() => {
    onLoadWorkspace();
  }, []);

  return (
    <article>
      <Helmet>
        <title>Workspace</title>
        <meta
          name="description"
          content="Note it down application workspace page"
        />
      </Helmet>
      <div>{`${workspace} ${user.email}`}!</div>
    </article>
  );
}

WorkspacePage.propTypes = {
  user: PropTypes.object,
  onLoadWorkspace: PropTypes.func,
  workspace: PropTypes.string,
};

const mapStateToProps = createStructuredSelector({
  user: makeSelectUser(),
  workspace: makeSelectWorkspace(),
});

export function mapDispatchToProps(dispatch) {
  return {
    onLoadWorkspace: () => dispatch(loadWorkspace()),
  };
}

const withConnect = connect(
  mapStateToProps,
  mapDispatchToProps,
);

export default compose(
  withConnect,
  memo,
)(WorkspacePage);
