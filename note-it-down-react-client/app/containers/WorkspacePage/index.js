import React, { memo, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Helmet } from 'react-helmet';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { createStructuredSelector } from 'reselect';

import { useInjectReducer } from 'utils/injectReducer';
import { useInjectSaga } from 'utils/injectSaga';
import TextField from '@material-ui/core/TextField';
import reducer from './reducer';
import saga from './saga';
import { loadWorkspace } from './actions';
import { makeSelectWorkspace } from './selectors';
import StyledPaper from './StyledPaper';

const key = 'workspace';

export function WorkspacePage({ onLoadWorkspace, workspace }) {
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

      <div>
        <StyledPaper elevator={15}>
          <TextField
            placeholder="Enter your first notes..."
            fullWidth
            defaultValue={workspace}
            InputProps={{ disableUnderline: true, style: { lineHeight: 2.4 } }}
            multiline
          />
        </StyledPaper>
      </div>
    </article>
  );
}

WorkspacePage.propTypes = {
  onLoadWorkspace: PropTypes.func,
  workspace: PropTypes.string,
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
