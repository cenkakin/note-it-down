import React, { memo, useEffect, useState } from 'react';
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
import { getUserWrapper } from '../../utils/storage';

const key = 'workspace';

const user = getUserWrapper();

const ws = new WebSocket(
  `ws://localhost:8762/note/websocket/note?token=${user.token}&subject=${
    user.user.email
  }&id=${user.user.id}`,
);

export function WorkspacePage({ onLoadWorkspace, workspace }) {
  const [foo, setData] = useState([]);
  ws.onmessage = function(event) {
    console.log(event.data);
  };

  ws.onclose = console.warn;

  ws.onerror = console.error;

  const event = {
    transactionId: 123,
    operations: [],
  };

  ws.onopen = () => ws.send(JSON.stringify(event));

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
