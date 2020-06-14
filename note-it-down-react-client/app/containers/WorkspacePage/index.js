import React, { memo, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { createStructuredSelector } from 'reselect';
import 'draft-js/dist/Draft.css';

import { useInjectReducer } from 'utils/injectReducer';
import { useInjectSaga } from 'utils/injectSaga';
import CustomEditor from '../../components/Editor/editor';

import reducer from './reducer';
import saga from './saga';
import { loadWorkspace } from './actions';
import { makeSelectWorkspace } from './selectors';
import createWs from './websocketHandler';

const key = 'workspace';

const workspaceWsSubscriber = {
  notifyMessage: () => {},
};

const webSocket = createWs([workspaceWsSubscriber]);

const createWSEvent = content => {
  const transactionId = new Date().getTime();
  return JSON.stringify({ transactionId, content });
};

export function WorkspacePage({ onLoadWorkspace, workspace }) {
  const [initialized, setInitialized] = React.useState(false);
  useInjectReducer({ key, reducer });
  useInjectSaga({ key, saga });
  useEffect(() => {
    onLoadWorkspace();
  }, []);

  useEffect(() => {
    setInitialized(true);
  }, [workspace]);

  return (
    <article>
      <Helmet>
        <title>Workspace</title>
        <meta
          name="description"
          content="Note it down application workspace page"
        />
      </Helmet>

      {initialized && (
        <div>
          <CustomEditor
            content={workspace.content}
            onEditorChange={content => {
              webSocket.send(createWSEvent(content));
            }}
          />
        </div>
      )}
    </article>
  );
}

WorkspacePage.propTypes = {
  onLoadWorkspace: PropTypes.func,
  workspace: PropTypes.any,
};

const mapStateToProps = createStructuredSelector({
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
