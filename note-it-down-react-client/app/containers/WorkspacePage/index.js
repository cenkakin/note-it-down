import React, { memo, useContext, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { createStructuredSelector } from 'reselect';
import 'draft-js/dist/Draft.css';

import { useInjectReducer } from 'utils/injectReducer';
import { useInjectSaga } from 'utils/injectSaga';
import styled from 'styled-components';
import CustomEditor from '../../components/Editor/editor';

import reducer from './reducer';
import saga from './saga';
import { loadWorkspace } from './actions';
import { makeSelectWorkspace, makeSelectWorkspaceLoaded } from './selectors';
// eslint-disable-next-line import/named
import { WorkspaceContext } from './workspaceContext';

const StyledArticle = styled(`article`)`
  && {
    background-color: #f3ecec;
  }
`;

const key = 'workspace';

export function WorkspacePage({ onLoadWorkspace, workspace, workspaceLoaded }) {
  const [initialized, setInitialized] = React.useState(false);
  const context = useContext(WorkspaceContext);
  useInjectReducer({ key, reducer });
  useInjectSaga({ key, saga });
  useEffect(() => {
    onLoadWorkspace();
  }, []);

  useEffect(() => {
    if (workspaceLoaded) {
      setInitialized(true);
      context.init();
    }
  }, [workspaceLoaded]);

  const onEditorChange = content => {
    if (
      workspace &&
      JSON.stringify(content) !== JSON.stringify(workspace.content)
    ) {
      context.send(content);
    }
  };

  return (
    <StyledArticle>
      <Helmet>
        <title>Workspace</title>
        <meta
          name="description"
          content="Note it down application workspace page"
        />
      </Helmet>
      <div>
        <CustomEditor
          content={initialized ? workspace.content : null}
          syncStatus={context.syncStatus}
          onEditorChange={onEditorChange}
          initialized={initialized}
        />
      </div>
    </StyledArticle>
  );
}

WorkspacePage.propTypes = {
  onLoadWorkspace: PropTypes.func,
  workspace: PropTypes.any,
  workspaceLoaded: PropTypes.bool,
};

const mapStateToProps = createStructuredSelector({
  workspace: makeSelectWorkspace(),
  workspaceLoaded: makeSelectWorkspaceLoaded(),
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
