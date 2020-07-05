import {
  convertFromRaw,
  convertToRaw,
  Editor,
  EditorState,
  getDefaultKeyBinding,
  Modifier,
  RichUtils,
} from 'draft-js';
import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography';
import styled from 'styled-components';
import StyledPaper from '../../containers/WorkspacePage/StyledPaper';

const StyledWrapper = styled(`div`)`
  && {
    text-align: center;
    padding: ${props => props.theme.spacing(0.5)}px;
  }
`;

export default function CustomEditor({
  content,
  onEditorChange,
  syncStatus,
  initialized,
}) {
  const [editorState, setEditorState] = React.useState(
    EditorState.createEmpty(),
  );
  const editor = React.useRef(null);
  const [onTyping, setOnTyping] = React.useState(false);
  const onChange = st => {
    setOnTyping(true);
    setEditorState(st);
  };

  function focusEditor() {
    editor.current.focus();
  }

  const handleKeyCommand = (command, st) => {
    let newState;
    if (command === 'on-tab') {
      const newContentState = Modifier.replaceText(
        editorState.getCurrentContent(),
        editorState.getSelection(),
        '       ',
      );
      newState = EditorState.push(
        editorState,
        newContentState,
        'insert-characters',
      );
    } else {
      newState = RichUtils.handleKeyCommand(st, command);
    }
    if (newState) {
      onChange(newState);
      return 'handled';
    }
    return 'not-handled';
  };

  const myKeyBindingFn = e => {
    if (e.keyCode === 9) {
      return 'on-tab';
    }
    return getDefaultKeyBinding(e);
  };

  useEffect(() => {
    if (initialized && content) {
      setEditorState(EditorState.createWithContent(convertFromRaw(content)));
    }
  }, [initialized]);

  useEffect(() => {
    const timer = setTimeout(() => {
      onEditorChange(convertToRaw(editorState.getCurrentContent()), null, 2);
      setOnTyping(false);
    }, 2000);
    return () => clearTimeout(timer);
  }, [editorState]);

  return (
    <div>
      <StyledWrapper>
        <Typography variant="overline">
          {onTyping ? 'TYPING...' : syncStatus}
        </Typography>
      </StyledWrapper>
      <StyledPaper onClick={focusEditor} elevator={15}>
        <Editor
          ref={editor}
          // onTab={this.onTab}
          placeholder="Enter some text..."
          keyBindingFn={myKeyBindingFn}
          editorState={editorState}
          handleKeyCommand={handleKeyCommand}
          onChange={onChange}
          spellCheck
        />
      </StyledPaper>
    </div>
  );
}

CustomEditor.propTypes = {
  content: PropTypes.object,
  syncStatus: PropTypes.any,
  onEditorChange: PropTypes.func,
  initialized: PropTypes.bool,
};
